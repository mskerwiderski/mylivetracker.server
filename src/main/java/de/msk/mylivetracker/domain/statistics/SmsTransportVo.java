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
	private String smsType;
	private String transportType;
	private String caption;
	private String sender;
	private String recipient;
	private String message;
	private String resultCode;
	private String exception;
	private boolean success;
	
	
	public SmsTransportVo() {
	}

	/**
	 * @param userId
	 * @param smsType
	 * @param transportType
	 * @param caption
	 * @param sender
	 * @param recipient
	 * @param message
	 * @param resultCode
	 * @param success
	 */
	public SmsTransportVo(String userId, String smsType, String transportType,
			String caption, String sender, String recipient, String message,
			String resultCode, String exception, boolean success) {
		this.userId = 
			(StringUtils.isEmpty(userId) ? UNKNOWN : userId);
		this.smsType = 
			(StringUtils.isEmpty(smsType) ? UNKNOWN : smsType);
		this.transportType = 
			(StringUtils.isEmpty(transportType) ? UNKNOWN : transportType);
		this.caption = 
			(StringUtils.isEmpty(caption) ? UNKNOWN : caption);
		this.sender = 
			(StringUtils.isEmpty(sender) ? UNKNOWN : sender);
		this.recipient = 
			(StringUtils.isEmpty(recipient) ? UNKNOWN : recipient);
		this.message = 
			(StringUtils.isEmpty(message) ? EMPTY_STRING : message);
		this.resultCode = 
			(StringUtils.isEmpty(resultCode) ? EMPTY : resultCode);
		this.exception = 
			(StringUtils.isEmpty(exception) ? EMPTY : exception);
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
	 * @return the smsType
	 */
	public String getSmsType() {
		return smsType;
	}

	/**
	 * @param smsType the smsType to set
	 */
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}

	/**
	 * @return the transportType
	 */
	public String getTransportType() {
		return transportType;
	}

	/**
	 * @param transportType the transportType to set
	 */
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}

	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
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
	 * @return the exception
	 */
	public String getException() {
		return exception;
	}

	/**
	 * @param exception the exception to set
	 */
	public void setException(String exception) {
		this.exception = exception;
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
