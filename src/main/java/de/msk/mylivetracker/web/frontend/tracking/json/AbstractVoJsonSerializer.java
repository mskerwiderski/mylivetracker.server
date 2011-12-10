package de.msk.mylivetracker.web.frontend.tracking.json;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonSerializer;

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
	private UserWithoutRoleVo user;
	private Locale locale;
	
	/**
	 * @param request
	 */
	public AbstractVoJsonSerializer(HttpServletRequest request, 
		UserWithoutRoleVo user) {
		if (request == null) {
			throw new IllegalArgumentException("request must not be null.");
		}
		if (user == null) {
			throw new IllegalArgumentException("user must not be null.");
		}
		this.request = request;
		this.user = user;
		this.locale = UsersLocaleResolver.getLocale(
			user.getOptions().getScaleUnit());		
	}	
		
	/**
	 * @return the request
	 */
	public HttpServletRequest getRequest() {
		return request;
	}

	/**
	 * @return the user
	 */
	public UserWithoutRoleVo getUser() {
		return user;
	}

	/**
	 * @return the locale
	 */
	public Locale getLocale() {
		return locale;
	}	
}
