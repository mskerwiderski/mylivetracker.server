package de.msk.mylivetracker.web.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.web.util.request.ReqParam;

/**
 * UsersLocaleResolver.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UsersLocaleResolver implements LocaleResolver {
	
	public static final Locale SUPPORTED_LOCALE_ENGLISH = Locale.ENGLISH;	
	public static final Locale SUPPORTED_LOCALE_GERMAN = Locale.GERMAN;
	public static final Locale DEFAULT_LOCALE = SUPPORTED_LOCALE_ENGLISH;
	
	private static final ReqParam<String> PARAM_LOCALE = 
		new ReqParam<String>("locale", String.class);
	
	private static class LocaleDsc {
		String language;
		Locale locale;
		public LocaleDsc(String language, Locale locale) {
			this.language = language;
			this.locale = locale;
		}		
	}
	
	public static final LocaleDsc[] SUPPORTED_LOCALES = {
		new LocaleDsc("en", SUPPORTED_LOCALE_ENGLISH),
		new LocaleDsc("de", SUPPORTED_LOCALE_GERMAN)
	};
	
	public static Locale getScaleUnit(UserWithoutRoleVo user) {
		String language = null;
		if (user != null) {
			language = user.getOptions().getScaleUnit();
		}
		return getLocale(language);
	}
	
	public static Locale getLocale(UserWithoutRoleVo user) {
		String language = null;
		if (user != null) {
			language = user.getOptions().getLanguage();
		}
		return getLocale(language);
	}
	
	public static Locale getLocale(String language) {
		Locale locale = null;
		for (int i=0; 
		!StringUtils.isEmpty(language) && (locale == null) && (i < SUPPORTED_LOCALES.length); 
		i++) {
			if (SUPPORTED_LOCALES[i].language.equals(language)) {
				locale = SUPPORTED_LOCALES[i].locale;
			}
		}
		return locale;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.LocaleResolver#resolveLocale(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = null;
		
		// try to get locale from user object.
		if (locale == null) {
			UserWithoutRoleVo user = 
				WebUtils.getCurrentUserWithoutRole(request.getSession());
			if (user != null) {
				locale = getLocale(user.getOptions().getLanguage());
			}
		}
		
		// try to get locale from url parameter.
		if (locale == null) {
			String localeStr = PARAM_LOCALE.getValueFromReq(request);
			if (!StringUtils.isEmpty(localeStr)) {
				// store locale in session if found in url.
				request.getSession().setAttribute(PARAM_LOCALE.getName(), localeStr);
			} else {
				// try to get locale from session. 
				localeStr = PARAM_LOCALE.getValueFromSess(request);	
			}
			if (!StringUtils.isEmpty(localeStr)) {
				locale = getLocale(localeStr);
			}
		}
		
		// try to get locale from request.
		if (locale == null) {
			locale = request.getLocale();
			boolean supported = false;
			for (int i=0; !supported && (i < SUPPORTED_LOCALES.length); i++) {
				supported = SUPPORTED_LOCALES[i].language.
					equals(locale.getLanguage());
			}
			if (!supported) {
				locale = null;
			}
		}
		
		// set default locale if no supported locale was found before.
		if (locale == null) {
			locale = DEFAULT_LOCALE;
		}
		return locale;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.LocaleResolver#setLocale(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.util.Locale)
	 */
	@Override
	public void setLocale(HttpServletRequest request,
		HttpServletResponse response, Locale locale) {
		throw new UnsupportedOperationException(
			"setLocale() is not supported by UsersLocaleResolver!");
	}
}
