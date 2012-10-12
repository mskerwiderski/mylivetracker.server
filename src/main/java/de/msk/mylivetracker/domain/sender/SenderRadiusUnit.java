package de.msk.mylivetracker.domain.sender;

import java.text.NumberFormat;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.web.util.UsersLocaleResolver;

/**
 * RadiusUnit.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-12
 * 
 */
public enum SenderRadiusUnit {
	kilometer, miles, meter, foot;
	
	public static final int MIN_DISTANCE_IN_METER = 1000;
	public static final int MAX_DISTANCE_IN_METER = 10000000;
	public static final int MAX_DISTANCE_IN_KILOMETER = 10000;
	
	public static final int MIN_DISTANCE_IN_FOOT = 3280;
	public static final int MAX_DISTANCE_IN_FOOT = 32804640;
	public static final int MAX_DISTANCE_IN_MILES = 6213;

	public static class LocalizedRange {
		String min;
		String max;
		SenderRadiusUnit unitMin;
		SenderRadiusUnit unitMax;
		public LocalizedRange(String min, String max, 
			SenderRadiusUnit unitMin, SenderRadiusUnit unitMax) {
			this.min = min;
			this.max = max;
			this.unitMin = unitMin;
			this.unitMax = unitMax;
		}
		public String getMin() {
			return min;
		}
		public String getMax() {
			return max;
		}
		public SenderRadiusUnit getUnitMin() {
			return unitMin;
		}
		public SenderRadiusUnit getUnitMax() {
			return unitMax;
		}
	}
	
	public static LocalizedRange getLocalizedRange(UserWithRoleVo user) {
		LocalizedRange localizedRange = null;
		NumberFormat numberFormatter;
		numberFormatter = NumberFormat.getNumberInstance(UsersLocaleResolver.getLocale(user));
		if (UsersLocaleResolver.getLocale(user).equals(UsersLocaleResolver.SUPPORTED_LOCALE_GERMAN)) {
			localizedRange = new LocalizedRange(
				numberFormatter.format(MIN_DISTANCE_IN_METER),
				numberFormatter.format(MAX_DISTANCE_IN_KILOMETER),
				SenderRadiusUnit.meter,
				SenderRadiusUnit.kilometer);
		} else {
			localizedRange = new LocalizedRange(
				numberFormatter.format(MIN_DISTANCE_IN_FOOT),
				numberFormatter.format(MAX_DISTANCE_IN_MILES),
				SenderRadiusUnit.foot,
				SenderRadiusUnit.miles);
		}
		return localizedRange;
	}
	
	public static boolean isInRange(int value, SenderRadiusUnit unit) {
		int valueInMeter = convertToMeter(value, unit);
		int valueInFoot = convertToFoot(value, unit);
		return 
			((valueInMeter >= MIN_DISTANCE_IN_METER) || (valueInFoot >= MIN_DISTANCE_IN_FOOT)) &&
			((valueInMeter <= MAX_DISTANCE_IN_METER) || (valueInFoot <= MAX_DISTANCE_IN_FOOT));
	}
	
	public static int convertToMeter(int value, SenderRadiusUnit unit) {
		float factor = 1.0f;
		switch (unit) {
		case kilometer:
			factor = 1000.0f;
			break;
		case miles:
			factor = 1609.344f;
			break;
		case meter:
			factor = 1.0f;
			break;
		case foot:
			factor = 0.3048f;
			break;
		}
		return Math.round((float)value * factor);
	}
	
	public static int convertToFoot(int value, SenderRadiusUnit unit) {
		float factor = 1.0f;
		switch (unit) {
		case kilometer:
			factor = 3280.8399f;
			break;
		case miles:
			factor = 5280f;
			break;
		case meter:
			factor = 3.28084f;
			break;
		case foot:
			factor = 1.0f;
			break;
		}
		return Math.round((float)value * factor);
	}
}