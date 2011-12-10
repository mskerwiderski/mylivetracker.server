package de.msk.mylivetracker.util.datetime;

import java.util.TimeZone;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;

public class DateTimeUtils {
		
	private static final String DATE_FORMAT_FOR_USER_REPRESENTATION = "'['dd.MM.yyyy']' HH:mm:ss";
	private static final String NO_DATE_TIME_VALUE_FOR_USER_REPRESENTATION = "./.";
	
	private DateTimeUtils() {		
	}
	
	public static String getDateTimeStrDe4UserRep(DateTime date) {		
		return (date == null) ? "" : date.getAsStr(
			TimeZone.getTimeZone(DateTime.TIME_ZONE_GERMANY), 
			DATE_FORMAT_FOR_USER_REPRESENTATION);
	}
	
	public static String getDateTimeStr4UserRep(
		UserWithoutRoleVo user, DateTime date) {		
		return getDateTimeStr4UserRep(user, date, 
			NO_DATE_TIME_VALUE_FOR_USER_REPRESENTATION);
	}
	
	public static String getDateTimeStr4UserRep(
		UserWithoutRoleVo user, DateTime date, String defValue) {
		if (user == null) {
			throw new IllegalArgumentException("user must not be null.");
		}
		if (defValue == null) {
			defValue = "";
		}
		return (date == null) ? defValue : date.getAsStr(
			TimeZone.getTimeZone(user.getOptions().getTimeZone()), 
			DATE_FORMAT_FOR_USER_REPRESENTATION);
	}
}
