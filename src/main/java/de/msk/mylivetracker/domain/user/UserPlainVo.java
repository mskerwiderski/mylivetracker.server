package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;

/**
 * UserPlainVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UserPlainVo implements Serializable {

	private static final long serialVersionUID = -1408524780887905058L;
	
	private String userId;
	private String lastName;
	private String firstName;	
	private String emailAddress;
	private String password;
	private UserRole role;
	protected Integer senderLimit;	
	protected boolean enabled;

	public UserPlainVo(String userId, String lastName, String firstName,
		String emailAddress, String password, UserRole role,
		Integer senderLimit, boolean enabled) {
		this.userId = userId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.emailAddress = emailAddress;
		this.password = password;
		this.role = role;
		this.senderLimit = senderLimit;
		this.enabled = enabled;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserPlainVo [userId=" + userId + ", lastName=" + lastName
			+ ", firstName=" + firstName + ", emailAddress=" + emailAddress
			+ ", password=" + password + ", role=" + role
			+ ", senderLimit=" + senderLimit + ", enabled=" + enabled + "]";
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the role
	 */
	public UserRole getRole() {
		return role;
	}

	/**
	 * @return the senderLimit
	 */
	public Integer getSenderLimit() {
		return senderLimit;
	}

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}	
}
