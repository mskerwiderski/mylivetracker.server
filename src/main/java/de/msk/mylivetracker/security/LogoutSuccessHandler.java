package de.msk.mylivetracker.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;

import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.web.frontend.login.LoginCtrl;
import de.msk.mylivetracker.web.util.request.ReqUrlStr;

/**
 * LogoutSuccessHandler.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class LogoutSuccessHandler extends SimpleUrlLogoutSuccessHandler {
	
	private IApplicationService applicationService;
		
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler#onLogoutSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onLogoutSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		this.setAlwaysUseDefaultTargetUrl(true);
		this.setDefaultTargetUrl(ReqUrlStr.create(
			this.applicationService.getApplicationBaseUrl(), 
			LoginCtrl.URL_LOGIN_CTRL).toString());
		super.onLogoutSuccess(request, response, authentication);
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
