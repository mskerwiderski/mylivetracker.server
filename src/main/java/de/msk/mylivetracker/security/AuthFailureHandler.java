package de.msk.mylivetracker.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.web.frontend.login.LoginCtrl;
import de.msk.mylivetracker.web.util.request.ReqParam;
import de.msk.mylivetracker.web.util.request.ReqUrlStr;

/**
 * AuthFailureHandler.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

	private static final Log log = LogFactory.getLog(AuthFailureHandler.class);
	
	public static final ReqParam<String> PARAM_MESSAGE_CODE = 
		new ReqParam<String>(LoginCtrl.REQUEST_PARAM_MESSAGE_CODE, String.class);
	
	private IApplicationService applicationService;
	
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler#onAuthenticationFailure(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.AuthenticationException)
	 */
	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException exception)
			throws IOException, ServletException {
		String applicationBaseUrl =
			this.applicationService.getApplicationBaseUrl();
		String defaultFailureUrl =
			ReqUrlStr.create(applicationBaseUrl, LoginCtrl.URL_LOGIN_CTRL).
			addParamValue(PARAM_MESSAGE_CODE, LoginCtrl.MESSAGE_LOGIN_FAILED).
			toString();
		this.setDefaultFailureUrl(defaultFailureUrl);
		log.info("defaultFailureUrl set to " + defaultFailureUrl);
		super.onAuthenticationFailure(request, response, exception);
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
