package de.msk.mylivetracker.web.frontend.util.json;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonSerializer;

import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.web.util.UsersLocaleResolver;

/**
 * AbstractVoJsonSerializer.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractVoJsonSerializer<T> implements JsonSerializer<T> {

	private HttpServletRequest request;
	private UserWithoutRoleVo userWithoutRole;
	private UserRole userRole;
	private Locale locale;
	
	/**
	 * @param request
	 */
	public AbstractVoJsonSerializer(HttpServletRequest request, 
		UserWithoutRoleVo userWithoutRole, UserRole userRole) {
		if (request == null) {
			throw new IllegalArgumentException("request must not be null.");
		}
		if (userWithoutRole == null) {
			throw new IllegalArgumentException("userWithoutRole must not be null.");
		}
		if (userRole == null) {
			throw new IllegalArgumentException("userRole must not be null.");
		}
		this.request = request;
		this.userWithoutRole = userWithoutRole;
		this.userRole = userRole;
		this.locale = UsersLocaleResolver.getLocale(
			userWithoutRole.getOptions().getScaleUnit());		
	}	
		
	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	public UserWithoutRoleVo getUserWithoutRole() {
		return userWithoutRole;
	}

	public UserRole getUserRole() {
		return userRole;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}	
}
