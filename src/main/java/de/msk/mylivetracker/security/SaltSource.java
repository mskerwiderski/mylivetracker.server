package de.msk.mylivetracker.security;

import java.io.Serializable;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * SaltSource.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SaltSource implements
		org.springframework.security.authentication.dao.SaltSource, Serializable {

	private static final long serialVersionUID = 1745483303758167647L;
		
	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.dao.SaltSource#getSalt(org.springframework.security.core.userdetails.UserDetails)
	 */
	public Object getSalt(UserDetails userDetails) {
		return userDetails;		
	}
}
