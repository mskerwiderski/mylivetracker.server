package de.msk.mylivetracker.domain.sender;

import java.io.Serializable;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * SenderVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 002 01.09.2012 carrier added.
 * 001 18.11.2011 senderType added.
 * 000 initial 2011-08-11
 * 
 */
public class SenderVo implements Serializable {

	private static final long serialVersionUID = 7097707478860598895L;
		
	public static final SenderSymbolVo DEFAULT_SYMBOL = SenderSymbolVo.Phone;
	
	private String senderId;
	private String senderType;
	private String userId;
	private String name;
	private String timeZone = DateTime.TIME_ZONE_UTC;
	private String switches;
	private SenderSwitchesVo senderSwitches;
	private String authUsername;
	private String authPlainPassword;
	private boolean authRequired;
	private boolean active;
	private String redirectTo;		
	private SenderSymbolVo symbol = DEFAULT_SYMBOL;
	
	/**
	 * constructor.
	 */
	public SenderVo() {
	}	

	/**
	 * @return the senderSwitches
	 */
	public SenderSwitchesVo getSenderSwitches() {
		if (this.senderSwitches == null) {
			this.senderSwitches = new SenderSwitchesVo(this.switches);
		}
		return this.senderSwitches;
	}

	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}
	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getSenderType() {
		return senderType;
	}
	public void setSenderType(String senderType) {
		this.senderType = senderType;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return timeZone;
	}

	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		this.timeZone = timeZone;
	}

	/**
	 * @return the switches
	 */
	public String getSwitches() {
		return switches;
	}

	/**
	 * @param switches the switches to set
	 */
	public void setSwitches(String switches) {
		this.switches = switches;
	}

	/**
	 * @return the authUsername
	 */
	public String getAuthUsername() {
		return authUsername;
	}
	/**
	 * @param authUsername the authUsername to set
	 */
	public void setAuthUsername(String authUsername) {
		this.authUsername = authUsername;
	}
	/**
	 * @return the authPlainPassword
	 */
	public String getAuthPlainPassword() {
		return authPlainPassword;
	}
	/**
	 * @param authPlainPassword the authPlainPassword to set
	 */
	public void setAuthPlainPassword(String authPlainPassword) {
		this.authPlainPassword = authPlainPassword;
	}
	/**
	 * @return the authRequired
	 */
	public boolean isAuthRequired() {
		return authRequired;
	}
	/**
	 * @param authRequired the authRequired to set
	 */
	public void setAuthRequired(boolean authRequired) {
		this.authRequired = authRequired;
	}
	/**
	 * @return the active
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
		
	/**
	 * @return the redirectTo
	 */
	public String getRedirectTo() {
		return redirectTo;
	}

	/**
	 * @param redirectTo the redirectTo to set
	 */
	public void setRedirectTo(String redirectTo) {
		this.redirectTo = redirectTo;
	}

	public SenderSymbolVo getSymbol() {
		return symbol;
	}

	public void setSymbol(SenderSymbolVo symbol) {
		this.symbol = symbol;
	}

	@Override
	public String toString() {
		return "SenderVo [senderId=" + senderId + ", senderType=" + senderType
			+ ", userId=" + userId + ", name=" + name + ", timeZone="
			+ timeZone + ", switches=" + switches + ", senderSwitches="
			+ senderSwitches + ", authUsername=" + authUsername
			+ ", authPlainPassword=" + authPlainPassword
			+ ", authRequired=" + authRequired + ", active=" + active
			+ ", redirectTo=" + redirectTo + ", symbol=" + symbol + "]";
	}
}
