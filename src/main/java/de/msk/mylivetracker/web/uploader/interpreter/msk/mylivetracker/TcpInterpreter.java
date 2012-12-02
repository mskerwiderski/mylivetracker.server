package de.msk.mylivetracker.web.uploader.interpreter.msk.mylivetracker;
        
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.protocol.EncDecoder;
import de.msk.mylivetracker.commons.protocol.KeyStore;
import de.msk.mylivetracker.commons.protocol.ProtocolUtils;
import de.msk.mylivetracker.commons.protocol.UploadDataPacket;
import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter;
import de.msk.mylivetracker.web.util.FmtUtils;

/**
 * TcpInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */         
public class TcpInterpreter extends AbstractDataStrWoDeviceSpecificInterpreter {
	
	private static KeyStore keyStore = new KeyStore(Keys.keyDscArrServer);
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStringInterpreter#isDeviceCompliant(java.lang.String)
	 */
	@Override
	protected boolean isDeviceCompliant(String dataStr)
		throws InterpreterException {	
		return EncDecoder.isValidDataStr(dataStr);
	}
	
	private static final String KEY_MAP_DECODER = "DECODER";
	private static EncDecoder getDecoder(Map<String, Object> uploadProcessContext) 
		throws InterpreterException {
		if (uploadProcessContext == null) {
			throw new IllegalArgumentException("uploadProcessContext must not be null.");
		}
		if (!uploadProcessContext.containsKey(KEY_MAP_DECODER) ||
			(uploadProcessContext.get(KEY_MAP_DECODER) == null)) {
			uploadProcessContext.put(KEY_MAP_DECODER, new EncDecoder(keyStore, true));
		}
		return (EncDecoder)uploadProcessContext.get(KEY_MAP_DECODER);
	}
	
	public static String processOneDataStr(EncDecoder encDecoder, 
		String dataStr, DataReceivedVo dataReceived) {
		String errMsg = null;
		
		Locale locale = Locale.ENGLISH;
		UploadDataPacket dataPacket = null;
		EncDecoder.ParsingResult result = 
			encDecoder.decode(dataStr);
		if (!result.isSuccess()) {
			errMsg = result.getErrorMessage();
		}
		if (errMsg == null) {
			dataPacket = result.getUploadDataPacket();
			if (!EncDecoder.isValidVersionCode(dataPacket)) {
				errMsg =
					"versioncode '" + dataPacket.getVersionCode() + "' is no longer supported. " +
					"minimum versioncode is '" + ProtocolUtils.PROTOCOL_VERSION_CODE + "'";
			}
		}
		if (errMsg == null) {
			dataReceived.getSenderState().addState("Version: " + 
				dataPacket.getVersionName() + "-" + dataPacket.getVersionCode());
				
			// authorization.
			dataReceived.getSenderFromRequest().setSenderId(dataPacket.getDeviceId());
			dataReceived.getSenderFromRequest().setAuthUsername(dataPacket.getUsername());
			dataReceived.getSenderFromRequest().setAuthPlainPassword(dataPacket.getSeed());
			dataReceived.getSenderFromRequest().setPasswordEncoder(
				new PasswordEncoder(dataPacket.getDeviceId(), dataPacket.getUsername()));
									
			dataReceived.getClientInfo().setTrackName(dataPacket.getTrackName());
			dataReceived.getClientInfo().setTrackId(dataPacket.getTrackId());
			dataReceived.getClientInfo().setPhoneNumber(dataPacket.getPhoneNumber());
			dataReceived.getClientInfo().setRuntimeWithPausesInMSecs(
				dataPacket.getRuntimeMSecsPausesIncl());
			dataReceived.getClientInfo().setRuntimeWithoutPausesInMSecs(
				dataPacket.getRuntimeMSecsPausesNotIncl());
			
			dataReceived.getPosition().setTimeRecorded(
				new DateTime(dataPacket.getTimestamp(), TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC)));
			
			if (dataPacket.getTrackDistanceInMtr() != null) {
				dataReceived.getClientInfo().setMileageInMtr(
					new Double(dataPacket.getTrackDistanceInMtr()));
				String distanceAsStr = FmtUtils.getDistanceAsStr(
					new Double(dataPacket.getTrackDistanceInMtr()), 
					locale, true);
				dataReceived.getSenderState().addState("Distance: " + distanceAsStr);			
			}
			
			if (dataPacket.getMileageInMtr() != null) {			
				String mileageAsStr = FmtUtils.getDistanceAsStr(
					dataPacket.getMileageInMtr(), locale, true);
				dataReceived.getSenderState().addState("Mileage: " + mileageAsStr);
			}
			
			String batteryPercent = 
				String.valueOf(dataPacket.getBatteryPowerInPercent());			
			String batteryVoltage =
				FmtUtils.getDoubleAsStr(dataPacket.getBatteryPowerInVoltage(), 2, locale, null);
			if (!StringUtils.isEmpty(batteryPercent) || 
				!StringUtils.isEmpty(batteryVoltage)) {
				if (batteryPercent == null) {
					batteryPercent = "--";
				} else if (batteryVoltage == null) {
					batteryVoltage = "--";
				}
				dataReceived.getSenderState().addState(
					"Battery: " + batteryPercent + "% [" + batteryVoltage + "V]");
			}				
			
			if (dataPacket.getCountSatellites() != null) {
				dataReceived.getSenderState().addState("Sat: " + dataPacket.getCountSatellites());
			}
			
			if (dataPacket.getLocationAccuracyInMtr() != null) {
				dataReceived.getSenderState().addState("Acc: " + dataPacket.getLocationAccuracyInMtr() + "m");
			}
			
			if ((dataPacket.getLatitudeInDecimal() != null) && 
				(dataPacket.getLongitudeInDecimal() != null)) {
				dataReceived.getPosition().setLatitudeInDecimal(dataPacket.getLatitudeInDecimal());								
				dataReceived.getPosition().setLongitudeInDecimal(dataPacket.getLongitudeInDecimal());
				dataReceived.getPosition().setAccuracyInMtr(dataPacket.getLocationAccuracyInMtr());
				dataReceived.getPosition().setAltitudeInMtr(dataPacket.getAltitudeInMtr());
				dataReceived.getPosition().setValid(dataPacket.getLocationValid());
				if (dataPacket.getSpeedInMtrPerSecs() != null) {
					dataReceived.getPosition().setSpeedInKmPerHour(
						dataPacket.getSpeedInMtrPerSecs() * 3.6);
				}
			}
	
			if (dataPacket.getHeartrateInBpm() != null) {
				dataReceived.getCardiacFunction().
					setHeartrateInBpm(dataPacket.getHeartrateInBpm().intValue());
			}			
			if (dataPacket.getHeartrateMinInBpm() != null) {
				dataReceived.getCardiacFunction().
					setHeartrateMinInBpm(dataPacket.getHeartrateMinInBpm().intValue());
			}
			if (dataPacket.getHeartrateMaxInBpm() != null) {
				dataReceived.getCardiacFunction().
					setHeartrateMaxInBpm(dataPacket.getHeartrateMaxInBpm().intValue());
			}
			if (dataPacket.getHeartrateAvgInBpm() != null) {
				dataReceived.getCardiacFunction().
					setHeartrateAvgInBpm(dataPacket.getHeartrateAvgInBpm().intValue());
			}
			
			if (dataPacket.getSosActivated() != null) {
				boolean sos = dataPacket.getSosActivated();
				dataReceived.getEmergencySignal().setActive(sos);
				if (sos) {
					dataReceived.setDeviceActionExecutor(DeviceActionExecutor.EmergencyActivated);
				} else {
					dataReceived.setDeviceActionExecutor(DeviceActionExecutor.EmergencyDeactivated);
				}
			}
			
			if (dataPacket.getPhoneType() != null) {
				dataReceived.getSenderState().addState(
					"phType: " + dataPacket.getPhoneType());
			}
			if (dataPacket.getMobileNetworkType() != null) {
				dataReceived.getSenderState().addState(
					"nwType: " + dataPacket.getMobileNetworkType());
			}		
			if (dataPacket.getMobileCountryCode() != null) {
				dataReceived.getMobNwCell().setMobileCountryCode(
					dataPacket.getMobileCountryCode());
			}
			if (dataPacket.getMobileNetworkCode() != null) {
				dataReceived.getMobNwCell().setMobileNetworkCode(
					dataPacket.getMobileNetworkCode());
			}
			if (dataPacket.getMobileNetworkName() != null) {
				dataReceived.getMobNwCell().setNetworkName(
					dataPacket.getMobileNetworkName());
			}
			if (dataPacket.getCellId() != null) {
				dataReceived.getMobNwCell().setCellId(
					dataPacket.getCellId());
			}
			if (dataPacket.getLocaleAreaCode() != null) {
				dataReceived.getMobNwCell().setLocalAreaCode(
					dataPacket.getLocaleAreaCode());
			}
			if (dataPacket.getMessage() != null) {
				dataReceived.getMessage().setContent(
					dataPacket.getMessage());
			}
		}
		return errMsg;
	}
	
    /* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter#processWoDeviceSpecific(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected void processWoDeviceSpecific(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		
		String errMsg = processOneDataStr(
			getDecoder(uploadProcessContext), dataStr, dataReceived);
		if (errMsg != null) {
			throw new InterpreterException(errMsg);
		}
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.http.AbstractHttpServletRequestInterpreter#postProcess(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific)
	 */
	@Override
	public String postProcess(DataReceivedVo dataReceived,
		IDeviceSpecific deviceSpecific) throws InterpreterException {
		String res = "OK";
		if (!dataReceived.isValid()) {
			res = "INVALID"; 
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.http.AbstractHttpServletRequestInterpreter#onError(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific)
	 */
	@Override
	public String onError(DataReceivedVo dataReceived,
		Exception exception,	
		IDeviceSpecific deviceSpecific) {		
		return "ERROR";
	}		
}
