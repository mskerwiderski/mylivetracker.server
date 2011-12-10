package de.msk.mylivetracker.domain;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
import de.msk.mylivetracker.web.uploader.processor.IDataCtx;
import de.msk.mylivetracker.web.uploader.processor.SenderFromRequestVo;

/**
 * DataReceivedVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DataReceivedVo {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(DataReceivedVo.class);
	
	private ISenderService senderService;
	private SenderFromRequestVo senderFromRequest = 
		new SenderFromRequestVo();
	private DeviceActionExecutor deviceActionExecutor 
		= DeviceActionExecutor.StorePositionMessage;	
	private PositionVo position = new PositionVo();
	private MessageVo message = new MessageVo();
	private MobNwCellVo mobNwCell = new MobNwCellVo();
	private SenderStateVo senderState = new SenderStateVo();
	private CardiacFunctionVo cardiacFunction = new CardiacFunctionVo();
	private EmergencySignalVo emergencySignal = new EmergencySignalVo();
	private ClientInfoVo clientInfo = new ClientInfoVo();
	
	private DataReceivedVo() {		
	}
	
	public static DataReceivedVo createInstance(
		ISenderService senderService,
		DateTime timeReceived) {
		DataReceivedVo dataReceived = new DataReceivedVo();
		dataReceived.senderService = senderService;
		dataReceived.getPosition().setTimeReceived(timeReceived);
		dataReceived.getPosition().setValid(true);
		dataReceived.getMessage().setTimeReceived(timeReceived);
		dataReceived.getMobNwCell().setTimeReceived(timeReceived);
		dataReceived.getSenderState().setTimeReceived(timeReceived);
		dataReceived.getCardiacFunction().setTimeReceived(timeReceived);
		dataReceived.getEmergencySignal().setTimeReceived(timeReceived);
		dataReceived.getClientInfo().setTimeReceived(timeReceived);
		return dataReceived;
	}
	
	public static DataReceivedVo createInstance(ISenderService senderService,
		DateTime timeReceived, IDataCtx data, SenderFromRequestVo senderFromRequest) {
		DataReceivedVo dataReceived = new DataReceivedVo();
		dataReceived.senderService = senderService;
		if (senderFromRequest != null) {
			dataReceived.senderFromRequest = senderFromRequest;
		}
		dataReceived.getPosition().setTimeReceived(timeReceived);
		dataReceived.getPosition().setValid(true);
		dataReceived.getMessage().setTimeReceived(timeReceived);
		dataReceived.getMobNwCell().setTimeReceived(timeReceived);
		dataReceived.getSenderState().setTimeReceived(timeReceived);
		dataReceived.getCardiacFunction().setTimeReceived(timeReceived);
		dataReceived.getEmergencySignal().setTimeReceived(timeReceived);
		dataReceived.getClientInfo().setTimeReceived(timeReceived);
		return dataReceived;
	}
		
	public void authorize() {
		this.getSenderFromRequest().authorize(
			this.senderService);
	}
	
	public boolean hasValidData() {
		return hasValidPosition() || hasValidMessage() ||
			hasValidMobNwCell() || hasValidSenderState() ||
			hasValidCardiacFunction() || hasValidEmergencySignal() ||
			hasValidClientInfo();
	}	
	
	public boolean isValid() {
		return checkIfValid() == null;
	}
	
	public String checkIfValid() {
		String msg = null;
		if (!this.hasValidSenderFromRequest()) {
			msg = "no valid sender from request found.";
		} else if (!this.getSenderFromRequest().isAuthorized()) {
			msg = "sender is not authorized.";
		} else if (this.deviceActionExecutor == null) {
			msg = "no valid device action executor found.";			
		} else if (this.deviceActionExecutor.equals(DeviceActionExecutor.StorePositionMessage) &&
			!hasValidData()) {
			msg = "no valid data found.";
		}
		return msg;
	}
	
	public boolean hasValidSenderFromRequest() {
		return !StringUtils.isEmpty(senderFromRequest.getSenderId());
	}
	
	public boolean hasValidPosition() {
		return position.isValid(); 			
	}
	
	public boolean hasValidMessage() {
		return message.isValid();
	}
	
	public boolean hasValidMobNwCell() {
		return mobNwCell.isValid();
	}
	
	public boolean hasValidSenderState() {
		return senderState.isValid();
	}
	
	public boolean hasValidCardiacFunction() {
		return cardiacFunction.isValid();
	}
	
	public boolean hasValidEmergencySignal() {
		return emergencySignal.isValid();
	}

	public boolean hasValidClientInfo() {
		return clientInfo.isValid();
	}
	
	/**
	 * @return the senderFromRequest
	 */
	public SenderFromRequestVo getSenderFromRequest() {
		return senderFromRequest;
	}

	/**
	 * @return the deviceActionExecutor
	 */
	public DeviceActionExecutor getDeviceActionExecutor() {
		return deviceActionExecutor;
	}

	/**
	 * @param deviceActionExecutor the deviceActionExecutor to set
	 */
	public void setDeviceActionExecutor(DeviceActionExecutor deviceActionExecutor) {
		this.deviceActionExecutor = deviceActionExecutor;
	}
	
	/**
	 * @return the position
	 */
	public PositionVo getPosition() {
		return position;
	}
	/**
	 * @return the message
	 */
	public MessageVo getMessage() {
		return message;
	}
	/**
	 * @return the mobNwCell
	 */
	public MobNwCellVo getMobNwCell() {
		return mobNwCell;
	}
	/**
	 * @return the senderState
	 */
	public SenderStateVo getSenderState() {
		return senderState;
	}
	/**
	 * @return the cardiacFunction
	 */
	public CardiacFunctionVo getCardiacFunction() {
		return cardiacFunction;
	}
	/**
	 * @return the emergencySignal
	 */
	public EmergencySignalVo getEmergencySignal() {
		return emergencySignal;
	}
	/**
	 * @return the clientInfo
	 */
	public ClientInfoVo getClientInfo() {
		return clientInfo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DataReceivedVo [senderFromRequest=" + senderFromRequest
			+ ", deviceActionExecutor=" + deviceActionExecutor
			+ ", position=" + position
			+ ", message=" + message + ", mobNwCell=" + mobNwCell
			+ ", senderState=" + senderState
			+ ", cardiacFunction=" + cardiacFunction
			+ ", emergencySignal=" + emergencySignal
			+ ", clientInfo=" + clientInfo + "]";
	}	
}
