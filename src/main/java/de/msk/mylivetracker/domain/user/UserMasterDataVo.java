package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

/**
 * UserMasterDataVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UserMasterDataVo implements Cloneable, Serializable {
	
	private static final long serialVersionUID = -6833444830925245717L;
	
	private String lastName;
	private String firstName;	
	private String emailAddress;
	private String password;
		
	public UserMasterDataVo copyWoPwd() {
		UserMasterDataVo masterData = this.copy();
		masterData.setPassword("");
		return masterData;
	}
	
	public UserMasterDataVo copy() {
		UserMasterDataVo masterData = null;
		try {
			masterData = (UserMasterDataVo)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return masterData;
	}
	
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return the emailAddress
	 */
	public String getEmailAddress() {
		return emailAddress;
	}

	/**
	 * @param emailAddress the emailAddress to set
	 */
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}	
}
