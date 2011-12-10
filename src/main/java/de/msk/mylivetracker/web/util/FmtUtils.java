package de.msk.mylivetracker.web.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.CardiacFunctionVo;
import de.msk.mylivetracker.domain.MessageVo;
import de.msk.mylivetracker.domain.MobNwCellVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.SenderStateVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;

/**
 * FmtUtils.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public final class FmtUtils {

	public static final String noValue = "./.";
	public static final String notAvailable = "[NA]";
	
	private static final String int2DigitsFmt = "00";
	private static final String dblFmt = "#,##0.00";
	
	private static final String unitMeter = "m";
	private static final String unitKilometer = "km";
	private static final String unitKilometerPerHour = "km/h";
	private static final String unitFeet = "ft";
	private static final String unitMiles = "mls";
	private static final String unitMilesPerHour = "mph";
	
	private static final String templDistance = "#dst #unit";
	private static final String templSpeed = "#spd #unit";
	private static final String templPos = "#addr [lat:#lat/lon:#lon]";
	private static final String templMobNwCell = "#nwn [MCC:#mcc/MNC:#mnc/LAC:#lac/cell:#cell]";
	private static final String templCardFct = "bpm #hr [min:#min/avg:#avg/max:#max]";
	private static final String templMessage = "#msg";
	private static final String templSenderState = "[#state]";
	
	private FmtUtils() {
		
	}
	
	public static String getStrAsCompatibleFilename(String str, String defValue) {
		if (StringUtils.isEmpty(str)) {
			return defValue;
		}
		str = str.replaceAll(" ", "_");
		str = str.replaceAll("[^a-zA-Z0-9_.]+", "");
		if (StringUtils.isEmpty(str)) {
			str = defValue;
		}
		return str;
	}
	
	public static String getSenderLabel(SenderVo sender, boolean senderIdIncl, String defValue) {
		if (StringUtils.isEmpty(defValue)) {
			defValue = noValue;
		}
		defValue = notAvailable + " " + defValue;
		
		String senderLabel = (sender != null) ? sender.getName() : defValue; 
			
		if (StringUtils.isEmpty(senderLabel)) {
			senderLabel = noValue;
		}
		senderLabel = StringUtils.abbreviate(senderLabel, 30);
		
		if ((sender != null) && senderIdIncl) {
			String id =
				(StringUtils.isEmpty(sender.getSenderId()) ?
				noValue : StringUtils.abbreviate(sender.getSenderId(), 10));
			senderLabel += " [" + id + "]";
		}
		return senderLabel;
	}	
	
	public static String getMessageAsStr(MessageVo message, boolean htmlEscaped) {
		if ((message == null) || !message.isValid()) {
			return noValue;
		}
		String msg = 
			StringUtils.replace(templMessage, "#msg", message.getContent());
		return (htmlEscaped ? StringEscapeUtils.escapeHtml(msg) : msg);
	}
	
	public static String getSenderStateAsStr(SenderStateVo senderState, boolean htmlEscaped) {
		if ((senderState == null) || !senderState.isValid()) {
			return noValue;
		}
		String state = 
			StringUtils.replace(templSenderState, "#state", senderState.getState());
		return (htmlEscaped ? StringEscapeUtils.escapeHtml(state) : state);
	}
	
	public static String getDoubleAsStr(String value, int cntDigits, Locale locale, String defValue) {
		if (StringUtils.isEmpty(value)) return defValue;
		Double valueDbl = Double.valueOf(value);
		return getDoubleAsStr(valueDbl, cntDigits, locale, defValue);
	}
	
	public static String getDoubleAsStr(Double value, int cntDigits, Locale locale, String defValue) {
		if (value == null) return defValue;
		String dblFmt = "#,##0";
		if (cntDigits > 0) {
			dblFmt += "." + StringUtils.repeat("0", cntDigits);
		}
		DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance(locale);
		df.applyPattern(dblFmt);
		return df.format(value);
	}
	
	public static String getDistanceAsStr(Double distInMtr, Locale locale, boolean kmMls) {
		if (distInMtr == null) return noValue;
		DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance(locale);
		df.applyPattern(dblFmt);
		String distStr = templDistance;
		if (locale.equals(Locale.GERMAN)) {
			distStr = StringUtils.replace(distStr, "#dst",
				(kmMls ? df.format(distInMtr / 1000d) : df.format(distInMtr)));				
		} else {
			distStr = StringUtils.replace(distStr, "#dst",
				(kmMls ? df.format(WebUtils.meter2miles(distInMtr)) :
				df.format(WebUtils.meter2feet(distInMtr))));				
		}
		if (kmMls) {
			distStr = StringUtils.replace(distStr, "#unit", 
				(locale == Locale.GERMAN) ? unitKilometer : unitMiles);			
		} else {
			distStr = StringUtils.replace(distStr, "#unit", 
				(locale == Locale.GERMAN) ? unitMeter : unitFeet);
		}
		return distStr;
	}
	
	public static String getSpeedAsStr(Double speedInMtrPerHour, Locale locale) {
		if (speedInMtrPerHour == null) return null;
		DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance(locale);
		df.applyPattern(dblFmt);
		String speedStr = templSpeed;		
		if (locale.equals(Locale.GERMAN)) {
			speedStr = StringUtils.replace(speedStr, "#spd",
				df.format(speedInMtrPerHour / 1000d));				
			speedStr = StringUtils.replace(speedStr, "#unit",
				unitKilometerPerHour);	
		} else {
			speedStr = StringUtils.replace(speedStr, "#spd",
				df.format(WebUtils.meter2miles(speedInMtrPerHour)));
			speedStr = StringUtils.replace(speedStr, "#unit",
				unitMilesPerHour);
		}
		return speedStr;
	}		
	
	public static String getRuntimeAsStr(Long runtimeMSecs) {
		if (runtimeMSecs == null) return null;
		DecimalFormat df = (DecimalFormat)NumberFormat.getNumberInstance();
		df.applyPattern(int2DigitsFmt);
		Long hours = runtimeMSecs / 1000 / 3600;
		Long mins = (runtimeMSecs - hours * 1000 * 3600) / 1000 / 60;
		Long secs = (runtimeMSecs - hours * 1000 * 3600 - mins * 1000 * 60) / 1000;
		String runtimeStr = df.format(hours) + ":"	+ df.format(mins) + ","	+ df.format(secs) + " h";
		return runtimeStr;
	}
	
	public static String getHomePositionAsStr(UserWithoutRoleVo user, Locale locale, 
		boolean htmlEscaped, boolean abbr) {
		if (user == null) return noValue;
		return getPositionAsStr(
			user.getOptions().getHomeLocAddress(), 
			user.getOptions().getHomeLocLatitude(), 
			user.getOptions().getHomeLocLongitude(), 
			locale, false, htmlEscaped, abbr);
	}
	
	public static String getPositionAsStr(PositionVo position, Locale locale, 
		boolean latLonOnly, boolean htmlEscaped, boolean abbr) {
		if ((position == null) || !position.isValid()) return noValue;
		return getPositionAsStr(
			position.getAddress(), 
			position.getLatitudeInDecimal(), 
			position.getLongitudeInDecimal(), 
			locale, latLonOnly, htmlEscaped, abbr);
	}
	
	public static String getPositionAsStr(PositionVo position, Locale locale, 
		boolean htmlEscaped, boolean abbr) {
		if ((position == null) || !position.isValid()) return noValue;
		return getPositionAsStr(
			position.getAddress(), 
			position.getLatitudeInDecimal(), 
			position.getLongitudeInDecimal(), 
			locale, false, htmlEscaped, abbr);
	}
	
	private static String getPositionAsStr(String addr, Double lat, Double lon, 
		Locale locale, boolean onlyLatLon, boolean htmlEscaped, boolean abbr) {
		if (StringUtils.isEmpty(addr) && (lat == null) && (lon == null)) {
			return noValue;
		}
		String res = templPos;
		if (!onlyLatLon) {
			String addrStr = getStrAsStr(addr, htmlEscaped);
			if (abbr) {
				addrStr = StringUtils.abbreviate(addrStr, 40);
			}
			if (!StringUtils.isEmpty(addrStr)) {
				res = StringUtils.replace(res, "#addr", addrStr);
			} else {
				res = StringUtils.replace(res, "#addr ", "");
			}
		} else {
			res = StringUtils.replace(res, "#addr ", "");
		}
		res = StringUtils.replace(res, "#lat", getDoubleAsStr(lat, locale));
		res = StringUtils.replace(res, "#lon", getDoubleAsStr(lon, locale));
		return res;
	}
	
	public static String getPositionAsStr(Double lat, Double lon, Locale locale) {
		if ((lat == null) && (lon == null)) {
			return noValue;
		}
		String res = templPos;
		res = StringUtils.replace(res, "#addr ", "");
		res = StringUtils.replace(res, "#lat", getDoubleAsStr(lat, locale));
		res = StringUtils.replace(res, "#lon", getDoubleAsStr(lon, locale));
		return res;
	}
	
	private static String getStrAsStr(String str, boolean htmlEscaped) {
		if (StringUtils.isEmpty(str)) return "";
		return (htmlEscaped ? StringEscapeUtils.escapeHtml(str) : str);
	}
	
	private static String getDoubleAsStr(Double dbl, Locale locale) {
		if (dbl == null) return noValue;
		DecimalFormat df = new DecimalFormat("0.000000", 
			DecimalFormatSymbols.getInstance(locale));
		return df.format(dbl);	
	}
	
	public static String getMobNwCellAsStr(MobNwCellVo mobNwCell) {
		return getMobNwCellAsStr(mobNwCell, false);
	}
	
	public static String getMobNwCellAsStr(MobNwCellVo mobNwCell, boolean codesOnly) {
		if ((mobNwCell == null) || !mobNwCell.isValid()) return noValue;
		String res = templMobNwCell;
		if (!codesOnly) {
			res = StringUtils.replace(res, "#nwn", 
				StringUtils.isEmpty(mobNwCell.getNetworkName()) ? 
				noValue : mobNwCell.getNetworkName());
		} else {
			res = StringUtils.replace(res, "#nwn ", "");
		}
		res = StringUtils.replace(res, "#mcc", 
			StringUtils.isEmpty(mobNwCell.getMobileCountryCode()) ? 
			noValue : mobNwCell.getMobileCountryCode());
		res = StringUtils.replace(res, "#mnc", 
			StringUtils.isEmpty(mobNwCell.getMobileNetworkCode()) ? 
			noValue : mobNwCell.getMobileNetworkCode());
		res = StringUtils.replace(res, "#lac", 
			mobNwCell.getLocalAreaCode() == null ? 
			noValue : mobNwCell.getLocalAreaCode().toString());
		res = StringUtils.replace(res, "#cell", 
			mobNwCell.getCellId() == null ? 
			noValue : mobNwCell.getCellId().toString());		
		return res;
	}
	
	public static String getCardiacFunctionAsStr(CardiacFunctionVo cardiacFunction) {
		if ((cardiacFunction == null) || !cardiacFunction.isValid()) return noValue;
		String res = templCardFct;
		res = StringUtils.replace(res, "#hr", 
			cardiacFunction.getHeartrateInBpm() == null ? 
			noValue : String.valueOf(cardiacFunction.getHeartrateInBpm()));
		res = StringUtils.replace(res, "#min", 
			cardiacFunction.getHeartrateMinInBpm() == null ? 
			noValue : String.valueOf(cardiacFunction.getHeartrateMinInBpm()));
		res = StringUtils.replace(res, "#max", 
			cardiacFunction.getHeartrateMaxInBpm() == null ? 
			noValue : String.valueOf(cardiacFunction.getHeartrateMaxInBpm()));
		res = StringUtils.replace(res, "#avg", 
			cardiacFunction.getHeartrateAvgInBpm() == null ? 
			noValue : String.valueOf(cardiacFunction.getHeartrateAvgInBpm()));
		return res;
	}
}
