package de.msk.mylivetracker.domain.statistics;

import org.apache.commons.lang.StringUtils;

/**
 * SmsTransportVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SmsTransportVo extends AbstractStatisticVo {

	private static final long serialVersionUID = 6696389056380447292L;

	private String userId;
	private String smsProvider;
	private String sender;
	private String recipient;
	private String message;
	private String resultCode;
	private boolean success;
	
	
	public SmsTransportVo() {
	}

	public SmsTransportVo(String userId, String smsProvider, 
			String sender, String recipient, String message,
			String resultCode, boolean success) {
		this.userId = 
			(StringUtils.isEmpty(userId) ? UNKNOWN : userId);
		this.smsProvider = 
			(StringUtils.isEmpty(smsProvider) ? UNKNOWN : smsProvider);
		this.sender = 
			(StringUtils.isEmpty(sender) ? UNKNOWN : sender);
		this.recipient = 
			(StringUtils.isEmpty(recipient) ? UNKNOWN : recipient);
		this.message = 
			(StringUtils.isEmpty(message) ? EMPTY_STRING : message);
		this.resultCode = 
			(StringUtils.isEmpty(resultCode) ? EMPTY : resultCode);
		this.success = success;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the smsProvider
	 */
	public String getSmsProvider() {
		return smsProvider;
	}

	/**
	 * @param smsProvider the smsProvider to set
	 */
	public void setSmsProvider(String smsProvider) {
		this.smsProvider = smsProvider;
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * @return the recipient
	 */
	public String getRecipient() {
		return recipient;
	}

	/**
	 * @param recipient the recipient to set
	 */
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the resultCode
	 */
	public String getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}
}
