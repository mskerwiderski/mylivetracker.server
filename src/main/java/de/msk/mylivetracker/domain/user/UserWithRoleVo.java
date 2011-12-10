package de.msk.mylivetracker.domain.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * UserWithRoleVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UserWithRoleVo extends UserWithoutRoleVo implements UserDetails {

	private static final long serialVersionUID = 875948160901832325L;

	public enum UserRole { Admin, User, Guest, RecentTrackAccessor };
	protected UserRole role;
	
	private String adminUsername = null;
	private String adminPassword = null;
	private boolean loggedInAsAdmin = false;
	
	/**
	 * constructor.
	 */
	public UserWithRoleVo() { 
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getUsername()
	 */
	@Override
	public String getUsername() {
		return this.getUserId();
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetails#getPassword()
	 */
	@Override
	public String getPassword() {
		return this.getMasterData().getPassword();
	}
	
	/**
	 * @return the role
	 */
	public UserRole getRole() {
		return role;
	}

	/**
	 * @param role the role to set
	 */
	public void setRole(UserRole role) {
		this.role = role;
	}
	
	/**
	 * @return the adminUsername
	 */
	public String getAdminUsername() {
		return adminUsername;
	}

	/**
	 * @param adminUsername the adminUsername to set
	 */
	public void setAdminUsername(String adminUsername) {
		this.adminUsername = adminUsername;
	}

	/**
	 * @return the adminPassword
	 */
	public String getAdminPassword() {
		return adminPassword;
	}

	/**
	 * @param adminPassword the adminPassword to set
	 */
	public void setAdminPassword(String adminPassword) {
		this.adminPassword = adminPassword;
	}

	/**
	 * @return the loggedInAsAdmin
	 */
	public boolean isLoggedInAsAdmin() {
		return loggedInAsAdmin;
	}

	/**
	 * @param loggedInAsAdmin the loggedInAsAdmin to set
	 */
	public void setLoggedInAsAdmin(boolean loggedInAsAdmin) {
		this.loggedInAsAdmin = loggedInAsAdmin;
	}

	/**
	 * @return the authorities
	 */
	@Override
	public List<GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new GrantedAuthorityImpl(role.name()));
		return authorities;
	}
}
