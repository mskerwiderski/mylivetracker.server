package de.msk.mylivetracker.web.uploader.processor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.statistics.UploadedDataProcessVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionResult;
import de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter;

/**
 * UploadProcessor.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class UploadProcessor {

	private static final Log log = LogFactory.getLog(UploadProcessor.class);
	
	private static ConcurrentMap<Long, UploadProcessor> processorMap = 
		new ConcurrentHashMap<Long, UploadProcessor>();
		
	/**
	 * @return the processorMap
	 */
	public static ConcurrentMap<Long, UploadProcessor> getProcessorMap() {
		return processorMap;
	}

	private boolean handleInProcessorMap;
	
	public UploadProcessor(
		String serverName,
		ProcessorType processorType,
		Services supportedServices,
		DataPacketCreator dataPacketCreator,
		boolean handleInProcessorMap) {
		this.serverName = serverName;
		this.processorType = processorType;
		this.supportedServices = supportedServices;
		this.dataPacketCreator = dataPacketCreator;
		this.processorId = UploadProcessor.nextProcessorNumber();
		this.handleInProcessorMap = handleInProcessorMap;
		this.context = new HashMap<String, Object>();
		if (this.handleInProcessorMap) {
			processorMap.put(this.processorId, this);
		}
	}
		
	public abstract RemoteInfo getRemoteInfo();
	public abstract ServerInfo getServerInfo();
	public abstract PreProcessResultDsc preProcess();
	public abstract void postProcess(ProcessResult result, boolean cutConnection);
	
	public String getName() {
		return this.processorType.getName();
	}
	
	private DateTime lastDataReceived = null;	
	public DateTime getLastDataReceived() {
		return this.lastDataReceived;
	}

	private String lastDataString = null;
	public String getLastDataString() {
		return this.lastDataString;
	}
	
	private ProcessResult lastProcessResult = null;
	public ProcessResult getLastProcessResult() {
		return this.lastProcessResult;
	}
	
	private long processorId;
	public long getId() {
		return this.processorId;
	}
	public String getProcessorInfo() {
		return this.getName() + "[" + this.getId() + "]"; 
	}
	
	private String serverName;
	private ProcessorType processorType;
	private Services supportedServices;
	private DataPacketCreator dataPacketCreator;
	private Map<String, Object> context;
	
	private IDataInterpreter dataInterpreter = null;
	public IDataInterpreter getDataInterpreter() {
		return this.dataInterpreter;
	}
	
	private SenderFromRequestVo senderFromRequest = null;
	public SenderFromRequestVo getSenderFromRequest() {
		return this.senderFromRequest;
	}
				
	private static long processorSeqNumber;
	
	private static synchronized long nextProcessorNumber() {
		return ++processorSeqNumber;
	}
	
	private void processSingleDataCtx(PreProcessResultDsc preProcessResultDsc,
		IDataCtx dataItem, Integer dataItemNumber) {
		try {
			this.lastDataReceived = new DateTime();
			this.lastDataString = dataItem.toString();
			this.lastProcessResult = this.processAux(dataItem);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.toString());
			this.lastProcessResult = new ProcessResult(
				false, "MLT-Exception: " + e.toString(), null);
		} finally {
			log.debug(
				this.getProcessorInfo() + 
				": data string processed: " + this.lastProcessResult);
			this.logUploadedDataProcess(
				dataItem, 
				dataItemNumber,	
				this.getRemoteInfo(),
				this.getServerInfo(),
				senderFromRequest, 				
				preProcessResultDsc,
				dataInterpreter, 
				this.lastProcessResult != null ? this.lastProcessResult.getResultMsg() : null,
				this.lastProcessResult != null ? this.lastProcessResult.getResponseStr() : null);			
		}
	}
	
	public void process() {			
		PreProcessResultDsc preProcessResultDsc = this.preProcess();				
		boolean cutConnection = !preProcessResultDsc.getConnectionStatus().isOk();		
		if (!preProcessResultDsc.isEmpty()) {		
			IDataCtx[] dataItems = preProcessResultDsc.getData();			
			for (int i=0; i < dataItems.length; i++) {				 
				this.processSingleDataCtx(
					preProcessResultDsc, dataItems[i], i);				
			}
		}	
		this.postProcess(this.lastProcessResult, cutConnection);		
		if (cutConnection && 
			this.handleInProcessorMap && 
			processorMap.containsKey(this.processorId)) {
			processorMap.remove(this.processorId);
		}
	}
		
	public static class ProcessResult {
		private boolean success;
		private String resultMsg;
		private String responseStr;
		private ProcessResult(boolean success, 
			String resultMsg, String responseStr) {
			this.success = success;
			this.resultMsg = resultMsg;
			this.responseStr = responseStr;
		}
		public boolean isSuccess() {
			return success;
		}

		public String getResultMsg() {
			return resultMsg;
		}
		public String getResponseStr() {
			return responseStr;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "ProcessResult [success=" + success + ", resultMsg="
				+ resultMsg + ", responseStr=" + responseStr + "]";
		}
	}
	
	public ProcessResult processAux(IDataCtx data) {		
		DataPacket dataPacket = null;
		String resultMsg = "";
		String responseStr = null;
		dataPacket = this.dataPacketCreator.create(
			data, supportedServices, 
			dataInterpreter, senderFromRequest,
			context);
		boolean successfullyProcessed = false;
		if (dataPacket == null) {
			resultMsg = "NoCompliantInterpreterFound";
		} else {
			if (dataInterpreter == null) {
				dataInterpreter = dataPacket.getDataInterpreter();					
			}
			if ((senderFromRequest == null) && (dataPacket.getDataReceived() != null)) {
				senderFromRequest = dataPacket.getDataReceived().getSenderFromRequest();
			}
			responseStr = dataPacket.getResponseStr();
			if (dataPacket.hasException()) {
				resultMsg = "InterpretingFailed: " + dataPacket.getException().toString();
			} else if (dataPacket.hasCheckMsg()) {
				resultMsg = "InterpretingDone, but: " + dataPacket.getCheckMsg();
				successfullyProcessed = true;
			} else {
				DeviceActionResult result =	this.processDataPacket(dataPacket);
				if ((result == null) || result.hasException()) {
					resultMsg = "StoringDataFailed: " + result.getException().toString();
				} else {
					successfullyProcessed = true;
					resultMsg = result.toString();
					this.supportedServices.getSenderService().updateSenderType(
						dataPacket.getDataReceived().getSenderFromRequest().getSenderId(),	
						dataPacket.getDetectedSenderType());
				}
			}				
		}				
		return new ProcessResult(successfullyProcessed, resultMsg, responseStr);
	}
	
	protected DeviceActionResult processDataPacket(DataPacket dataPacket) {
		DataReceivedVo dataReceived = dataPacket.getDataReceived();		
		return dataReceived.getDeviceActionExecutor().execute(
			this.supportedServices.getUserService().
				getUserWithoutRole(
				dataReceived.getSenderFromRequest().getSender().getUserId()),
			this.supportedServices.getTrackService(), 
			this.supportedServices.getSmsService(),
				dataReceived);			
	}
	
	private void logUploadedDataProcess(
		IDataCtx dataItem, Integer dataItemNumber,
		RemoteInfo remoteInfo, ServerInfo serverInfo,		
		SenderFromRequestVo senderFromRequest, PreProcessResultDsc preProcessResultDsc, 
		IDataInterpreter dataInterpreter, String addInfo, String responseStr) {		
		this.supportedServices.getStatisticsService().logUploadedDataProcess(
			new UploadedDataProcessVo(
				this.processorType.getProtocol(),
				remoteInfo.getRemoteIpAddress(), 
				remoteInfo.getRemoteHost(), 
				remoteInfo.getRemotePort(),
				serverInfo.getServerName(),
				serverInfo.getServerPort(), 				
				this.getProcessorInfo(), 
				(dataInterpreter == null) ? null : dataInterpreter.getName(),
				(dataInterpreter == null) ? null : dataInterpreter.getVersion(),		
				(senderFromRequest == null) ? null : senderFromRequest.getSenderId(), 
				(dataItemNumber == null) ? null : StringUtils.leftPad(String.valueOf(dataItemNumber), 2, '0'),		
				dataItem.toString(),
				addInfo,
				responseStr,
				preProcessResultDsc.getConnectionStatus().toString())); 				
	}

	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * @return the processorType
	 */
	public ProcessorType getProcessorType() {
		return processorType;
	}
	/**
	 * @return the supportedServices
	 */
	public Services getSupportedServices() {
		return supportedServices;
	}

	/**
	 * @return the context
	 */
	public Map<String, Object> getContext() {
		return context;
	}		
}
