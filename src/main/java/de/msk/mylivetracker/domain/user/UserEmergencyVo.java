package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * UserEmergencyVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UserEmergencyVo implements Cloneable, Serializable {
		
	private static final long serialVersionUID = 263326226253162501L;
	
	private Boolean smsUnlocked;
	private Boolean smsEnabled;
	private String smsSender;
	private String smsRecipient;
	private DateTime smsLastSent;
	private Integer smsSentCount;
	
	public void setDefaultValues() {	
		smsEnabled = false;
		smsSentCount = 0;
	}
	
	public UserEmergencyVo copy() {
		UserEmergencyVo emergency = null;
		try {
			emergency = (UserEmergencyVo)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return emergency;
	}

	public void incSmsSentCount() {
		this.smsLastSent = new DateTime();
		this.smsSentCount++;
	}
		
	/**
	 * @return the smsUnlocked
	 */
	public Boolean getSmsUnlocked() {
		return smsUnlocked;
	}

	/**
	 * @param smsUnlocked the smsUnlocked to set
	 */
	public void setSmsUnlocked(Boolean smsUnlocked) {
		this.smsUnlocked = smsUnlocked;
	}

	/**
	 * @return the smsEnabled
	 */
	public Boolean getSmsEnabled() {
		return smsEnabled;
	}

	/**
	 * @param smsEnabled the smsEnabled to set
	 */
	public void setSmsEnabled(Boolean smsEnabled) {
		this.smsEnabled = smsEnabled;
	}

	/**
	 * @return the smsSender
	 */
	public String getSmsSender() {
		return smsSender;
	}

	/**
	 * @param smsSender the smsSender to set
	 */
	public void setSmsSender(String smsSender) {
		this.smsSender = smsSender;
	}

	/**
	 * @return the smsRecipient
	 */
	public String getSmsRecipient() {
		return smsRecipient;
	}

	/**
	 * @param smsRecipient the smsRecipient to set
	 */
	public void setSmsRecipient(String smsRecipient) {
		this.smsRecipient = smsRecipient;
	}

	/**
	 * @return the smsLastSent
	 */
	public DateTime getSmsLastSent() {
		return smsLastSent;
	}

	/**
	 * @param smsLastSent the smsLastSent to set
	 */
	public void setSmsLastSent(DateTime smsLastSent) {
		this.smsLastSent = smsLastSent;
	}

	/**
	 * @return the smsSentCount
	 */
	public Integer getSmsSentCount() {
		return smsSentCount;
	}

	/**
	 * @param smsSentCount the smsSentCount to set
	 */
	public void setSmsSentCount(Integer smsSentCount) {
		this.smsSentCount = smsSentCount;
	}
}
