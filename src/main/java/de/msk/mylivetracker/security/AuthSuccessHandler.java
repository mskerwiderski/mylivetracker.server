package de.msk.mylivetracker.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.IUserService;
import de.msk.mylivetracker.web.util.UrlUtils;
import de.msk.mylivetracker.web.util.request.ReqUrlStr;

/**
 * AuthSuccessHandler.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

	private static final Log log = LogFactory.getLog(AuthSuccessHandler.class);
	
	private IApplicationService applicationService;
	private IUserService userService;
		
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		this.setAlwaysUseDefaultTargetUrl(true);
		this.setDefaultTargetUrl(ReqUrlStr.create(
			this.applicationService.getApplicationBaseUrl(),
			UrlUtils.URL_TRACKS_OVERVIEW_CTRL).toString());
			
		UserWithRoleVo user = (UserWithRoleVo)authentication.getPrincipal();
		if (StringUtils.isEmpty(user.getAdminUsername())) {
			user.setLastLogin(new DateTime());
			if (user.getLoginCount() == null) {
				user.setLoginCount(1);
			} else {
				user.setLoginCount(user.getLoginCount() + 1);
			}
			log.debug("login info for user '" + user.getUserId() + "' updated: " +
				"last login=" + user.getLastLogin() + ", " +
				"login count=" + user.getLoginCount());
			this.userService.updateLoginInfo(user);
		}
		super.onAuthenticationSuccess(request, response, authentication);
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

	/**
	 * @return the userService
	 */
	public IUserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}	
}
