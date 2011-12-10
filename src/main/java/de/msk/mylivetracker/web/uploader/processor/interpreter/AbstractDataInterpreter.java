package de.msk.mylivetracker.web.uploader.processor.interpreter;

import java.text.ParseException;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.sender.SenderSwitchesVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
import de.msk.mylivetracker.web.uploader.processor.DataPacket;
import de.msk.mylivetracker.web.uploader.processor.IDataCtx;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.SenderFromRequestVo;
import de.msk.mylivetracker.web.uploader.processor.SupportedServices;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;

/**
 * AbstractDataInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractDataInterpreter implements IDataInterpreter {

	private static final Log log = LogFactory.getLog(AbstractDataInterpreter.class);
	
	private ISenderService senderService;
	private String id;
	private String name;
	private String version;	

	private void checkEmergencySignal(DataReceivedVo dataReceived) {
		boolean signalReceived = false;
		if (dataReceived.getDeviceActionExecutor().equals(
			DeviceActionExecutor.EmergencyActivated)) {
			dataReceived.getEmergencySignal().setActive(true);
			signalReceived = true;
		} else if (dataReceived.getDeviceActionExecutor().equals(
			DeviceActionExecutor.EmergencyDeactivated)) {
			dataReceived.getEmergencySignal().setActive(false);
			signalReceived = true;
		}
		
		if (signalReceived) {
			dataReceived.getEmergencySignal().setMessage(
				dataReceived.getMessage().getContent());
			dataReceived.getMessage().setContent(null);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#createDataPacket(de.msk.mylivetracker.web.uploader.processor.SupportedServices, de.msk.mylivetracker.web.uploader.processor.SenderFromRequestVo, de.msk.mylivetracker.web.uploader.processor.IDataCtx, java.util.Map)
	 */
	@Override
	public DataPacket createDataPacket(SupportedServices supportedServices,
		SenderFromRequestVo senderFromRequest, IDataCtx data,
		Map<String, Object> uploadProcessContext) {
		DataPacket dataPacket = null;	
		IDeviceSpecific deviceSpecific = null;
		DataReceivedVo dataReceived = 
			DataReceivedVo.createInstance(
				supportedServices.getSenderService(),
				new DateTime(), data, senderFromRequest);
		boolean isDeviceCompliant = false;
		if ((senderFromRequest != null) && senderFromRequest.isAuthorized()) {
			isDeviceCompliant = true;
		} else {
			try {			
				isDeviceCompliant = this.isDeviceCompliant(dataReceived, data);
			} catch (InterpreterException e) {
				isDeviceCompliant = false;
			}
		}
		if (isDeviceCompliant) {
			dataPacket = new DataPacket(this);
			dataPacket.setDetectedSenderType(this.name);
			try {
				deviceSpecific = this.process(dataReceived, data, uploadProcessContext);	
				if (dataReceived == null) {
					throw new InterpreterException("Interpreter did return null.");
				}				
				dataPacket.setException(null);
				dataPacket.setDataReceived(dataReceived);
				if (dataReceived.hasValidSenderFromRequest() &&
					!dataReceived.getSenderFromRequest().isAuthorized()) {
					dataPacket.authorize(supportedServices.getSenderService());
				}
				dataPacket.setCheckMsg(dataReceived.checkIfValid());				
				this.checkEmergencySignal(dataReceived);
				if (!dataPacket.hasCheckMsg() && 
					dataReceived.hasValidMobNwCell() && 
					StringUtils.isEmpty(dataReceived.getMobNwCell().getNetworkName())) {				
					dataReceived.getMobNwCell().setNetworkName(
						supportedServices.getMobileNetworkService().getNetworkName(
							dataReceived.getMobNwCell().getMobileCountryCode(),
							dataReceived.getMobNwCell().getMobileNetworkCode())); 
				}
				dataPacket.setResponseStr(
					this.postProcess(dataReceived, deviceSpecific));
				postProcessDataReceived(dataReceived);
			} catch (InterpreterException e) {
				dataPacket.setException(e);				
			} finally {
				if (dataPacket.hasException()) {
					String responseStr = this.onError(dataReceived, 
						dataPacket.getException(), deviceSpecific);
					if (!StringUtils.isEmpty(responseStr)) {
						dataPacket.setResponseStr(responseStr);
					}
				}
			}
		}		
		return dataPacket;
	}	

	private void postProcessDataReceived(DataReceivedVo dataReceived) {
		SenderVo sender = dataReceived.getSenderFromRequest().getSender();
		if (sender == null) return;
		
		// process switches.
		SenderSwitchesVo senderSwitches = sender.getSenderSwitches();
		if (senderSwitches.useTimestampAsReceived()) {
			if (dataReceived.getPosition().getTimeRecorded() != null) {
				dataReceived.getPosition().setTimeReceived(
					dataReceived.getPosition().getTimeRecorded());
			}
		}
		if (senderSwitches.ignoreSenderMileage()) {
			dataReceived.getClientInfo().setMileageInMtr(null);			
		}
		if (senderSwitches.ignoreLocationValidFlag()) {
			dataReceived.getPosition().setValid(true);			
		}
		
		// process senders timezone.
		if (dataReceived.getPosition().getTimeRecorded() == null) {
			dataReceived.getPosition().setTimeRecorded(
				dataReceived.getPosition().getTimeReceived());
		} else {
			if (!StringUtils.equals(DateTime.TIME_ZONE_UTC, sender.getTimeZone())) {
				log.debug("time recorded (not converted): " + 
					dataReceived.getPosition().getTimeRecorded());
				
				// date time string how it was received from sender.
				String dateTimeStrOriginal = 
					dataReceived.getPosition().getTimeRecorded().getAsStr(
					TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC), 
					DateTime.STD_DATE_TIME_FORMAT);
				
				DateTime converted = null;
				try {
					// do conversion in correct senders time zone.
					converted = new DateTime(
						dateTimeStrOriginal, DateTime.STD_DATE_TIME_FORMAT,
						TimeZone.getTimeZone(sender.getTimeZone()));
				} catch (ParseException e) {
					throw new RuntimeException(e);
				}
				dataReceived.getPosition().setTimeRecorded(converted);
				log.debug("time recorded (converted to " + sender.getTimeZone() + "): " + converted);
			}
		}
	}
	
	/**
	 * @return the senderService
	 */
	public ISenderService getSenderService() {
		return senderService;
	}

	/**
	 * @param senderService the senderService to set
	 */
	public void setSenderService(ISenderService senderService) {
		this.senderService = senderService;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#getId()
	 */
	@Override
	public String getId() {
		return this.id;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#getName()
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#getVersion()
	 */
	@Override
	public String getVersion() {
		return this.version;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.name + " [" +
			this.id + ":" +
			this.version + "]";
	}		
}

