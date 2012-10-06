package de.msk.mylivetracker.domain.statistics;

import org.apache.commons.lang.StringUtils;

/**
 * StorePositionProcessorErrorVo.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-06
 * 
 */
public class StorePositionProcessorErrorVo extends AbstractStatisticVo {
	
	private static final long serialVersionUID = 7966225238735405492L;

	private String userId;
	private String senderId;
	private String positionId;
	private int retry;
	private String error;
	private String positionDump;	
	private String mobNwCellDump;
	private String messageDump;
	private String senderStateDump;
	private String cardiacFunctionDump;
	private String emergencySignalDump;
	private String clientInfoDump;
	
	public StorePositionProcessorErrorVo() {
	}
	
	public StorePositionProcessorErrorVo(String userId, String senderId,
		String positionId, int retry, String error, String positionDump,
		String mobNwCellDump, String messageDump, String senderStateDump,
		String cardiacFunctionDump, String emergencySignalDump,
		String clientInfoDump) {
		this.userId = (StringUtils.isEmpty(userId) ? UNKNOWN : userId);
		this.senderId = (StringUtils.isEmpty(senderId) ? UNKNOWN : senderId);
		this.positionId = (StringUtils.isEmpty(positionId) ? UNKNOWN : positionId);
		this.retry = retry;
		this.error = (StringUtils.isEmpty(error) ? EMPTY : error);
		this.positionDump = (StringUtils.isEmpty(positionDump) ? EMPTY : positionDump);
		this.mobNwCellDump = (StringUtils.isEmpty(mobNwCellDump) ? EMPTY : mobNwCellDump);
		this.messageDump = (StringUtils.isEmpty(messageDump) ? EMPTY : messageDump);
		this.senderStateDump = (StringUtils.isEmpty(senderStateDump) ? EMPTY : senderStateDump);
		this.cardiacFunctionDump = (StringUtils.isEmpty(cardiacFunctionDump) ? EMPTY : cardiacFunctionDump);
		this.emergencySignalDump = (StringUtils.isEmpty(emergencySignalDump) ? EMPTY : emergencySignalDump);
		this.clientInfoDump = (StringUtils.isEmpty(clientInfoDump) ? EMPTY : clientInfoDump);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSenderId() {
		return senderId;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	public String getPositionId() {
		return positionId;
	}

	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	public int getRetry() {
		return retry;
	}

	public void setRetry(int retry) {
		this.retry = retry;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getPositionDump() {
		return positionDump;
	}

	public void setPositionDump(String positionDump) {
		this.positionDump = positionDump;
	}

	public String getMobNwCellDump() {
		return mobNwCellDump;
	}

	public void setMobNwCellDump(String mobNwCellDump) {
		this.mobNwCellDump = mobNwCellDump;
	}

	public String getMessageDump() {
		return messageDump;
	}

	public void setMessageDump(String messageDump) {
		this.messageDump = messageDump;
	}

	public String getSenderStateDump() {
		return senderStateDump;
	}

	public void setSenderStateDump(String senderStateDump) {
		this.senderStateDump = senderStateDump;
	}

	public String getCardiacFunctionDump() {
		return cardiacFunctionDump;
	}

	public void setCardiacFunctionDump(String cardiacFunctionDump) {
		this.cardiacFunctionDump = cardiacFunctionDump;
	}

	public String getEmergencySignalDump() {
		return emergencySignalDump;
	}

	public void setEmergencySignalDump(String emergencySignalDump) {
		this.emergencySignalDump = emergencySignalDump;
	}

	public String getClientInfoDump() {
		return clientInfoDump;
	}

	public void setClientInfoDump(String clientInfoDump) {
		this.clientInfoDump = clientInfoDump;
	}
}
