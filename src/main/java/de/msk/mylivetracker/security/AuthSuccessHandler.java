package de.msk.mylivetracker.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.Services;
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

	private Services services;
		
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler#onAuthenticationSuccess(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, org.springframework.security.core.Authentication)
	 */
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		this.setAlwaysUseDefaultTargetUrl(true);
		this.setDefaultTargetUrl(ReqUrlStr.create(
			this.services.getApplicationService().getApplicationBaseUrl(),
			UrlUtils.URL_TRACKS_OVERVIEW_CTRL).toString());
		UserWithRoleVo user = (UserWithRoleVo)authentication.getPrincipal();
		this.services.getUserOperationsCounterService().incCountLogin(user);
		super.onAuthenticationSuccess(request, response, authentication);
	}

	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
	}
}
