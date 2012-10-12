package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

/**
 * UserWithoutRoleVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UserWithoutRoleVo implements Serializable {

	private static final long serialVersionUID = -1630623388578148419L;
	
	protected UserMasterDataVo masterData;
	protected UserAutoLoginVo autoLogin;
	protected UserOptionsVo options;
	protected UserStatusPageVo statusPage;
	protected UserEmergencyVo emergency;

	protected String userId;
	protected String realm;
	protected Integer senderLimit;
	
	protected boolean enabled;
	protected boolean accountNonExpired = true;
	protected boolean accountNonLocked = true;
	protected boolean credentialsNonExpired = true;
		
	/**
	 * constructor.
	 */
	public UserWithoutRoleVo() { 
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
	 * @return the realm
	 */
	public String getRealm() {
		return realm;
	}

	/**
	 * @param realm the realm to set
	 */
	public void setRealm(String realm) {
		this.realm = realm;
	}

	/**
	 * @return the senderLimit
	 */
	public Integer getSenderLimit() {
		return senderLimit;
	}

	/**
	 * @param senderLimit the senderLimit to set
	 */
	public void setSenderLimit(Integer senderLimit) {
		this.senderLimit = senderLimit;
	}
	
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the accountNonExpired
	 */
	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	/**
	 * @param accountNonExpired the accountNonExpired to set
	 */
	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	/**
	 * @return the accountNonLocked
	 */
	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	/**
	 * @param accountNonLocked the accountNonLocked to set
	 */
	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	/**
	 * @return the credentialsNonExpired
	 */
	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	/**
	 * @param credentialsNonExpired the credentialsNonExpired to set
	 */
	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
	/**
	 * @return the masterData
	 */
	public UserMasterDataVo getMasterData() {
		return masterData;
	}

	/**
	 * @param masterData the masterData to set
	 */
	public void setMasterData(UserMasterDataVo masterData) {
		this.masterData = masterData;
	}

	public UserAutoLoginVo getAutoLogin() {
		return autoLogin;
	}

	public void setAutoLogin(UserAutoLoginVo autoLogin) {
		this.autoLogin = autoLogin;
	}

	/**
	 * @return the options
	 */
	public UserOptionsVo getOptions() {
		return options;
	}

	/**
	 * @param options the options to set
	 */
	public void setOptions(UserOptionsVo options) {
		this.options = options;
	}

	/**
	 * @return the statusPage
	 */
	public UserStatusPageVo getStatusPage() {
		return statusPage;
	}

	/**
	 * @param statusPage the statusPage to set
	 */
	public void setStatusPage(UserStatusPageVo statusPage) {
		this.statusPage = statusPage;
	}

	/**
	 * @return the emergency
	 */
	public UserEmergencyVo getEmergency() {
		return emergency;
	}

	/**
	 * @param emergency the emergency to set
	 */
	public void setEmergency(UserEmergencyVo emergency) {
		this.emergency = emergency;
	}
}
