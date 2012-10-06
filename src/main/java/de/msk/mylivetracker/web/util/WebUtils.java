package de.msk.mylivetracker.web.util;

import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.application.IApplicationService.Parameter;
import de.msk.mylivetracker.web.options.StrOptionDsc;

/**
 * WebUtils.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public final class WebUtils {

	private static final String BEAN_APPLICATION_SERVICE = "applicationService";
	private static final String BEAN_USER_OPTS_LANGUAGE = "userOptsLanguage";
	private static final String BEAN_LOCALE_RESOLVER = "localeResolver";
	private static final String BEAN_MESSAGE_SOURCE = "messageSource";
	
	/**
	 * private constructor.
	 */
	private WebUtils() {		
	}
	
	private static final String USER_WITHOUT_ROLE_OBJECT = "USER_WITHOUT_ROLE_OBJECT";
	
	public static void setCurrentUserWithoutRole(HttpSession session, UserWithoutRoleVo user) {
		if (session == null) {
			throw new IllegalArgumentException("session must not be null.");
		}
		if (user == null) {
			throw new IllegalArgumentException("user must not be null.");
		}
		session.setAttribute(USER_WITHOUT_ROLE_OBJECT, user);
	}
	
	/**
	 * get current user.
	 * @return the current user.
	 */
	public static UserWithoutRoleVo getCurrentUserWithoutRole(HttpSession session) {
		if (session == null) {
			throw new IllegalArgumentException("session must not be null.");
		}
		UserWithoutRoleVo user = getCurrentUserWithRole();
		if (user == null) {
			user = (UserWithoutRoleVo)session.getAttribute(USER_WITHOUT_ROLE_OBJECT);
		}		
		return user; 
	}
	
	/**
	 * get current user.
	 * @return the current user.
	 */
	public static UserWithRoleVo getCurrentUserWithRole() {
		UserWithRoleVo user = null;
		SecurityContext secCtx = SecurityContextHolder.getContext();
		Object principal = secCtx.getAuthentication().getPrincipal();
		if (principal instanceof UserWithRoleVo) {
			user = (UserWithRoleVo)principal;
		}
		return user; 
	}	
		
	public static UserWithRoleVo authenticateUser(HttpServletRequest request,
		String userId, String userPassword) {
		UserWithRoleVo user = null;
		AuthenticationManager authManager = 
			(AuthenticationManager)getBean(request, "authManager");
		Authentication auth = authManager.authenticate(
			new UsernamePasswordAuthenticationToken(userId, userPassword));
		Object principal = auth.getPrincipal();
		if (principal instanceof UserWithRoleVo) {
			user = (UserWithRoleVo)principal;
		}
		return user;
	}
	
	public static String getApplicationName(HttpServletRequest request) {		
		IApplicationService applicationService = (IApplicationService)
			getBean(request, BEAN_APPLICATION_SERVICE);
		 
		return applicationService.getParameterValueAsString(Parameter.ApplicationName);
	}
	
	public static String getApplicationBaseUrl(HttpServletRequest request) {		
		IApplicationService applicationService = (IApplicationService)
			getBean(request, BEAN_APPLICATION_SERVICE);
		 
		return applicationService.getApplicationBaseUrl();
	}
	
	public static String getTimezone(HttpServletRequest request) {
		UsersLocaleResolver localeResolver = (UsersLocaleResolver)
			getBean(request, BEAN_LOCALE_RESOLVER);
		UserWithoutRoleVo user = 
			getCurrentUserWithoutRole(request.getSession());
		String res = "";
		if (user != null) {
			TimeZone timeZone = TimeZone.getTimeZone(user.getOptions().getTimeZone());
			res = timeZone.getDisplayName(
				new DateTime().inDaylightTime(
					TimeZone.getTimeZone(user.getOptions().getTimeZone())), 
				TimeZone.LONG, 
				localeResolver.resolveLocale(request));
		} else {		
			TimeZone timeZone = TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC); 
			res = timeZone.getDisplayName(false, TimeZone.LONG, 
				localeResolver.resolveLocale(request));
		}
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public static List<StrOptionDsc> getUserOptsLanguage(HttpServletRequest request) {
		return (List<StrOptionDsc>)getBean(request, BEAN_USER_OPTS_LANGUAGE);
	}
	
	public static String getLanguage(HttpServletRequest request) {
		UsersLocaleResolver localeResolver = (UsersLocaleResolver)
			getBean(request, BEAN_LOCALE_RESOLVER);
		return localeResolver.resolveLocale(request).getLanguage();
	}
	
	public static Locale getLocale(HttpServletRequest request) {
		return ((UsersLocaleResolver)
			getBean(request, BEAN_LOCALE_RESOLVER)).resolveLocale(request);
	}
	
	public static MessageSource getMessageSource(HttpServletRequest request) {
		return (MessageSource)
			getBean(request, BEAN_MESSAGE_SOURCE);
	}
	
	public static String getMessageByLocale(
		HttpServletRequest request, Locale locale, 
		String code, Object ... args) {
		MessageSource messageSource = (MessageSource)
			getBean(request, BEAN_MESSAGE_SOURCE);
		return messageSource.getMessage(code, args, locale); 			
	}	
	
	public static String getMessage(MessageSource messageSource,
			UserWithoutRoleVo user,
			String code, Object ... args) {
		return messageSource.getMessage(code, args,  
			UsersLocaleResolver.getLocale(user.getOptions().getLanguage()));	
	}
	
	public static String getMessage(HttpServletRequest request, 
			String code, Object ... args) {
		MessageSource messageSource = (MessageSource)
			getBean(request, BEAN_MESSAGE_SOURCE);
		UsersLocaleResolver localeResolver = (UsersLocaleResolver)
			getBean(request, BEAN_LOCALE_RESOLVER);
		return messageSource.getMessage(code, args,  
			localeResolver.resolveLocale(request));	
	}	
	
	public static String getMessage(HttpServletRequest request, 
		String code, String defMsg, Object ... args) {
		MessageSource messageSource = (MessageSource)
			getBean(request, BEAN_MESSAGE_SOURCE);
		UsersLocaleResolver localeResolver = (UsersLocaleResolver)
			getBean(request, BEAN_LOCALE_RESOLVER);
		return messageSource.getMessage(code, args, defMsg, 
			localeResolver.resolveLocale(request));	
	}		
	
	private static Object getBean(ServletContext ctx, String beanId) {
		WebApplicationContext webAppCtx = WebApplicationContextUtils.getWebApplicationContext(ctx);
		return webAppCtx.getBean(beanId);		
	}
	
	private static Object getBean(HttpServletRequest request, String beanId) {
		return getBean(request.getSession().getServletContext(), beanId);		
	}
	
	public static Double miles2meter(Double distInMiles) {
		return distInMiles * 1000d / 0.62137d;
	}
	
	public static Double meter2miles(Double distInMtr) {
		return distInMtr / 1000d * 0.62137d;
	}
	
	public static Double meter2feet(Double distInMtr) {
		return distInMtr * 3.2808d;
	}
}
