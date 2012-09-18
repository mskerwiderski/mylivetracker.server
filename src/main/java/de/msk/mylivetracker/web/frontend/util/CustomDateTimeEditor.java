package de.msk.mylivetracker.web.frontend.util;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;

/**
 * CustomDateTimeEditor.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-22
 * 
 */
public class CustomDateTimeEditor extends PropertyEditorSupport {
	private static final Log log = LogFactory.getLog(CustomDateTimeEditor.class);
	
	private final String dateFormat;
	private final boolean allowEmpty;
	private final UserWithoutRoleVo user;

	public CustomDateTimeEditor(UserWithoutRoleVo user, String dateFormat, boolean allowEmpty) {
		this.dateFormat = dateFormat;
		this.allowEmpty = allowEmpty;
		this.user = user;
	}
	
	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.debug("setAsText");
		log.debug("dateStr=" + text);
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			// Treat empty String as null value.
			setValue(null);
		} else {
			try {
				setValue(new DateTime(text, this.dateFormat, 
					TimeZone.getTimeZone(user.getOptions().getTimeZone())));				
			}
			catch (ParseException e) {
				throw new IllegalArgumentException("Could not parse date: " + e.getMessage(), e);
			}
		}
		log.debug("dateObj=" + this.getValue());
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		log.debug("getAsText");
		DateTime value = (DateTime)getValue();
		log.debug("dateObj=" + value);
		String res = (value != null ? value.getAsStr(
			TimeZone.getTimeZone(user.getOptions().getTimeZone()), this.dateFormat) : "");
		log.debug("dateStr=" + res);
		return res;
	}
}
