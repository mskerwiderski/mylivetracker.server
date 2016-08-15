package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * UserEmergencyVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 001 smsServiceUsername / smsServicePassword added. 2016-08-07
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
	private String smsServiceProvider;
	private String smsServiceUsername;
	private String smsServicePassword;
	private String smsServiceParams;
	private String smsMessageTemplate;
	
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

	public void updateSmsSentCount(int sentSms) {
		this.smsLastSent = new DateTime();
		this.smsSentCount += sentSms;
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

	public String[] getSmsRecipientArr() {
		return StringUtils.split(this.smsRecipient, ";");
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

	/**
	 * @return the smsServiceProvider
	 */
	public String getSmsServiceProvider() {
		return smsServiceProvider;
	}

	/**
	 * @param smsServiceProvider the smsServiceProvider to set
	 */
	public void setSmsServiceProvider(String smsServiceProvider) {
		this.smsServiceProvider = smsServiceProvider;
	}

	/**
	 * @return the smsServiceUsername
	 */
	public String getSmsServiceUsername() {
		return smsServiceUsername;
	}

	/**
	 * @param smsServiceUsername the smsServiceUsername to set
	 */
	public void setSmsServiceUsername(String smsServiceUsername) {
		this.smsServiceUsername = smsServiceUsername;
	}

	/**
	 * @return the smsServicePassword
	 */
	public String getSmsServicePassword() {
		return smsServicePassword;
	}

	/**
	 * @param smsServicePassword the smsServicePassword to set
	 */
	public void setSmsServicePassword(String smsServicePassword) {
		this.smsServicePassword = smsServicePassword;
	}

	/**
	 * @return the smsServiceParams
	 */
	public String getSmsServiceParams() {
		return smsServiceParams;
	}

	/**
	 * @param smsServiceParams the smsServiceParams to set
	 */
	public void setSmsServiceParams(String smsServiceParams) {
		this.smsServiceParams = smsServiceParams;
	}

	/**
	 * @return the smsMessageTemplate
	 */
	public String getSmsMessageTemplate() {
		return smsMessageTemplate;
	}

	/**
	 * @param smsMessageTemplate the smsMessageTemplate to set
	 */
	public void setSmsMessageTemplate(String smsMessageTemplate) {
		this.smsMessageTemplate = smsMessageTemplate;
	}
}
