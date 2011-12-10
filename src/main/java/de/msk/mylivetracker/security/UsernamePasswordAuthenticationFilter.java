package de.msk.mylivetracker.security;

import javax.servlet.http.HttpServletRequest;

/**
 * UsernamePasswordAuthenticationFilter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UsernamePasswordAuthenticationFilter
	extends
	org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter {
	
	public static final String FORM_USERID_KEY = "plainUserId";
    public static final String FORM_PASSWORD_KEY = "plainPassword";
    
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter#obtainUsername(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String obtainUsername(HttpServletRequest request) {
		return request.getParameter(FORM_USERID_KEY); 
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter#obtainPassword(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(FORM_PASSWORD_KEY); 		
	}
		
}
