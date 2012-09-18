package de.msk.mylivetracker.domain.user;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

/**
 * UserAutoLoginVo.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-08-31
 * 
 */
public class UserAutoLoginVo implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 5847111930289558726L;
	
	private Boolean autoLoginEnabledForUser;
	private Boolean autoLoginEnabledForGuest;
	private String autoLoginTicketForUser;
	private String autoLoginTicketForGuest;	
		
	private static final String TICKET_PREFIX = "#";
    public static boolean isAutoLoginTicket(String str) {
    	return !StringUtils.isEmpty(str) && StringUtils.startsWith(str, TICKET_PREFIX);
    }
	public static String createAutoLoginTicket() {
		return TICKET_PREFIX + UUID.randomUUID().toString();
	}
	public void setDefaultValues() {	
		this.autoLoginEnabledForUser = false;
		this.autoLoginEnabledForGuest = false;
		this.autoLoginTicketForUser = createAutoLoginTicket();
		this.autoLoginTicketForGuest = createAutoLoginTicket();
	}
	
	public UserAutoLoginVo copy() {
		UserAutoLoginVo autoLogin = null;
		try {
			autoLogin = (UserAutoLoginVo)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return autoLogin;
	}
	public Boolean getAutoLoginEnabledForUser() {
		return autoLoginEnabledForUser;
	}
	public void setAutoLoginEnabledForUser(Boolean autoLoginEnabledForUser) {
		this.autoLoginEnabledForUser = autoLoginEnabledForUser;
	}
	public Boolean getAutoLoginEnabledForGuest() {
		return autoLoginEnabledForGuest;
	}
	public void setAutoLoginEnabledForGuest(Boolean autoLoginEnabledForGuest) {
		this.autoLoginEnabledForGuest = autoLoginEnabledForGuest;
	}
	public String getAutoLoginTicketForUser() {
		return autoLoginTicketForUser;
	}
	public void setAutoLoginTicketForUser(String autoLoginTicketForUser) {
		this.autoLoginTicketForUser = autoLoginTicketForUser;
	}
	public String getAutoLoginTicketForGuest() {
		return autoLoginTicketForGuest;
	}
	public void setAutoLoginTicketForGuest(String autoLoginTicketForGuest) {
		this.autoLoginTicketForGuest = autoLoginTicketForGuest;
	}
	
	@Override
	public String toString() {
		return "UserAutoLoginVo [autoLoginEnabledForUser="
				+ autoLoginEnabledForUser + ", autoLoginEnabledForGuest="
				+ autoLoginEnabledForGuest + ", autoLoginTicketForUser="
				+ autoLoginTicketForUser + ", autoLoginTicketForGuest="
				+ autoLoginTicketForGuest + "]";
	}
}
