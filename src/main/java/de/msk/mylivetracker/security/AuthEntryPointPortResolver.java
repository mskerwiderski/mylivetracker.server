package de.msk.mylivetracker.security;

import javax.servlet.ServletRequest;

import de.msk.mylivetracker.service.IApplicationService;

/**
 * AuthEntryPointPortResolver.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class AuthEntryPointPortResolver implements
		org.springframework.security.web.PortResolver {

	private IApplicationService applicationService;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.web.PortResolver#getServerPort(javax.servlet.ServletRequest)
	 */
	public int getServerPort(ServletRequest request) {
		return applicationService.getApplicationPort();
	}

	/**
	 * @return the applicationService
	 */
	public IApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}	
}
