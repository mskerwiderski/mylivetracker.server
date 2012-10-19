package de.msk.mylivetracker.web.uploader.processor;

import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.service.sender.ISenderService;

/**
 * SenderFromRequestVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SenderFromRequestVo {

	private String senderId = null;
	private String authUsername = null;
	private String authPlainPassword = null;
	private SenderVo sender = null;
	private IPasswordEncoder passwordEncoder = null;

	public boolean isAuthorized() {
		return sender != null;
	}
	
	public void authorize(ISenderService senderService) {
		this.sender = 
			senderService.getAuthorizedSender(this);
	}
	
	/**
	 * constructor.
	 */
	public SenderFromRequestVo() {
	}

	
	/**
	 * @param senderId
	 */
	public SenderFromRequestVo(String senderId) {
		this.senderId = senderId;
	}


	/**
	 * @param senderId
	 * @param authUsername
	 * @param authPlainPassword
	 */
	public SenderFromRequestVo(String senderId, String authUsername,
			String authPlainPassword) {
		this.senderId = senderId;
		this.authUsername = authUsername;
		this.authPlainPassword = authPlainPassword;
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
	 * @return the passwordEncoder
	 */
	public IPasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	/**
	 * @param passwordEncoder the passwordEncoder to set
	 */
	public void setPasswordEncoder(IPasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	/**
	 * @return the sender
	 */
	public SenderVo getSender() {
		return sender;
	}

	/**
	 * @param sender the sender to set
	 */
	public void setSender(SenderVo sender) {
		this.sender = sender;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SenderFromRequestVo [senderId=" + senderId + ", authUsername="
				+ authUsername + ", authPlainPassword=" + authPlainPassword
				+ ", sender=" + sender + "]";
	}	
}
