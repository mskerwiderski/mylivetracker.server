package de.msk.mylivetracker.web.uploader.processor.interpreter.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.PositionVo;

/**
 * CommonUtils.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public final class CommonUtils {
	private CommonUtils() {		
	}
	
	public static final int IDX_NOT_SET = -1;

	//
	// $GPRMC,191410,A,4735.5634,N,00739.3538,E,0.0,0.0,181102,0.4,E,A*19
	// 	      ^      ^ ^           ^            ^   ^   ^      ^     ^
	// 	      |      | |           |            |   |   |      |     |
	// 	      |      | |           |            |   |   |      |     Neu in NMEA 2.3:
	// 	      |      | |           |            |   |   |      |     Art der Bestimmung
	// 	      |      | |           |            |   |   |      |     A=autonomous (selbst)
	// 	      |      | |           |            |   |   |      |     D=differential
	// 	      |      | |           |            |   |   |      |     E=estimated (geschätzt)
	// 	      |      | |           |            |   |   |      |     N=not valid (ungültig)
	// 	      |      | |           |            |   |   |      |     S=simulator
	// 	      |      | |           |            |   |   |      |   
	// 	      |      | |           |            |   |   |      Missweisung (mit Richtung)
	// 	      |      | |           |            |   |   |     
	// 	      |      | |           |            |   |   Datum: 18.11.2002     
	// 	      |      | |           |            |   |        
	// 	      |      | |           |            |   Bewegungsrichtung in Grad (wahr)
	// 	      |      | |           |            |
	// 	      |      | |           |            Geschwindigkeit über Grund (Knoten)
	// 	      |      | |           |            
	// 	      |      | |           Längengrad mit (Vorzeichen)-Richtung (E=Ost, W=West)
	// 	      |      | |           007° 39.3538' Ost
	// 	      |      | |                        
	// 	      |      | Breitengrad mit (Vorzeichen)-Richtung (N=Nord, S=Süd)
	// 	      |      | 46° 35.5634' Nord
	// 	      |      |
	// 	      |      Status der Bestimmung: A=Active (gültig); V=void (ungültig)
	// 	      |
	// 	      Uhrzeit der Bestimmung: 19:14:10 (UTC-Zeit)
	//
	public static final String GPRMC_IDENTIFIER = "GPRMC";
	private static final int GPRMC_COUNT_ITEMS_MIN = 12;
	private static final int GPRMC_COUNT_ITEMS_MAX = 13;
	private static final int GPRMC_IDX_VALID_INDICATOR = 2;
	private static final int GPRMC_IDX_DATE_RECORDED = 9;
	private static final int GPRMC_IDX_TIME_RECORDED = 1;
	private static final int GPRMC_IDX_LATITUDE = 3;
	private static final int GPRMC_IDX_LONGITUDE = 5;
	private static final int GPRMC_IDX_ALTITUDE = IDX_NOT_SET;
	private static final int GPRMC_IDX_SPEED = 7;
	
	private static final String PATTERN_GPRMC = "(\\$?GPRMC,.*\\*.{2})";
	public static void setPositionFromGprmcStrInDataStr( 
		PositionVo position, String dataStr,
		boolean overrideIfNotNull) 
		throws InterpreterException {
		Matcher matcher = Pattern.compile(PATTERN_GPRMC).matcher(dataStr);
		if (!matcher.find() || (matcher.groupCount() != 1) || 
			(StringUtils.isEmpty(matcher.group(1)))) {
			throw new InterpreterException(
				"gprmc string not found in data string '" + dataStr + "'.");
		}		
		setPositionFromGprmcStr(position, matcher.group(1), overrideIfNotNull);
	}
		
	public static void setPositionFromGprmcStr(
		PositionVo position, String gprmcStr,
		boolean overrideIfNotNull) 
		throws InterpreterException {
		if (position == null) {
			throw new InterpreterException("position must not be null.");
		}
		if (StringUtils.isEmpty(gprmcStr)) {
			throw new InterpreterException("gprmcStr must not be empty.");
		}		
		setPositionFromNmea183Str(
			position, gprmcStr, overrideIfNotNull,
			GPRMC_IDENTIFIER,
			GPRMC_COUNT_ITEMS_MIN,
			GPRMC_COUNT_ITEMS_MAX,
			GPRMC_IDX_VALID_INDICATOR,
			GPRMC_IDX_DATE_RECORDED,
			GPRMC_IDX_TIME_RECORDED,
			GPRMC_IDX_LATITUDE,
			GPRMC_IDX_LONGITUDE,
			GPRMC_IDX_ALTITUDE,
			GPRMC_IDX_SPEED);		 
	}
	
	//      
	// $GPGGA,191410,4735.5634,N,00739.3538,E,1,04,4.4,351.5,M,48.0,M,,*45
	// 	      ^      ^           ^            ^ ^  ^   ^       ^     
	// 	      |      |           |            | |  |   |       |    
	// 	      |      |           |            | |  |   |       Hoehe Geoid minus 
	// 	      |      |           |            | |  |   |       Hoehe Ellipsoid (WGS84)
	// 	      |      |           |            | |  |   |       in Metern (48.0hm)
	// 	      |      |           |            | |  |   |        
	//        |      |           |            | |  |   Hoehe ueber Meer (ueber Geoid)
	// 	      |      |           |            | |  |   in Metern (351.5hm)
	// 	      |      |           |            | |  |
	// 	      |      |           |            | |  HDOP (horizontal dilution
	// 	      |      |           |            | |  of precision) Genauigkeit
	// 	      |      |           |            | |
	// 	      |      |           |            | Anzahl der erfassten Satelliten
	// 	      |      |           |            | 
	// 	      |      |           |            Qualitaet der Messung
	// 	      |      |           |            (0 = ungueltig)
	// 	      |      |           |            (1 = GPS)
	// 	      |      |           |            (2 = DGPS)
	// 	      |      |           |            (6 = geschaetzt nur NMEA-0183 2.3)
	// 	      |      |           | 
	// 	      |      |           Laengengrad
	// 	      |      |
	// 	      |      Breitengrad 
	// 	      |
	// 	      Uhrzeit
	//
	public static final String GPGGA_IDENTIFIER = "GPGGA";
	private static final int GPGGA_COUNT_ITEMS_MIN = 15;
	private static final int GPGGA_COUNT_ITEMS_MAX = 15;
	private static final int GPGGA_IDX_VALID_INDICATOR = 6;
	private static final int GPGGA_IDX_DATE_RECORDED = IDX_NOT_SET;
	private static final int GPGGA_IDX_TIME_RECORDED = 1;
	private static final int GPGGA_IDX_LATITUDE = 2;
	private static final int GPGGA_IDX_LONGITUDE = 4;
	private static final int GPGGA_IDX_ALTITUDE = 9;
	private static final int GPGGA_IDX_SPEED = IDX_NOT_SET;
	
	public static void setPositionFromGpggaStr(
		PositionVo position, String gpggaStr,
		boolean overrideIfNotNull) 
		throws InterpreterException {
		if (position == null) {
			throw new InterpreterException("position must not be null.");
		}
		if (StringUtils.isEmpty(gpggaStr)) {
			throw new InterpreterException("gpggaStr must not be empty.");
		}		
		setPositionFromNmea183Str(
			position, gpggaStr, overrideIfNotNull,
			GPGGA_IDENTIFIER,
			GPGGA_COUNT_ITEMS_MIN,
			GPGGA_COUNT_ITEMS_MAX,
			GPGGA_IDX_VALID_INDICATOR,
			GPGGA_IDX_DATE_RECORDED,
			GPGGA_IDX_TIME_RECORDED,
			GPGGA_IDX_LATITUDE,
			GPGGA_IDX_LONGITUDE,
			GPGGA_IDX_ALTITUDE,
			GPGGA_IDX_SPEED);
	}
	
	public static void setPositionFromNmea183Str(
		PositionVo position, String nmeaStr, 
		boolean overrideIfNotNull, String nmeaStrIdentifier,
		int countItemsMin, int countItemsMax, int idxValidIndicator,
		int idxDateRecorded, int idxTimeRecorded,
		int idxLatitude, int idxLongitude, 
		int idxAltitude, int idxSpeed) 
		throws InterpreterException {
		
		if (StringUtils.isEmpty(nmeaStr)) {
			throw new InterpreterException("nmeaStr string must not be null.");
		}
		
		if (StringUtils.isEmpty(nmeaStrIdentifier)) {
			throw new InterpreterException("nmeaStrIdentifier string must not be null.");
		}
		
		// check count of data items
		String[] dataItems = StringUtils.splitPreserveAllTokens(nmeaStr, ",");
		if ((dataItems.length < countItemsMin) || (dataItems.length > countItemsMax)) {
			throw new InterpreterException("invalid nmea string: " +
				"expected " + countItemsMin + "-" + countItemsMax + " items, " +
				"but found " + dataItems.length + " items.");
		}
		
		// check if nmea string starts with correct sentence identifier.
		if (!StringUtils.startsWith(dataItems[0], nmeaStrIdentifier) &&
			!StringUtils.startsWith(dataItems[0], "$" + nmeaStrIdentifier)) {
			throw new InterpreterException(
				"nmea string does not contain identifier '" + nmeaStrIdentifier + "'.");
		}
		if (!StringUtils.startsWith(nmeaStr, "$")) {
			nmeaStr = "$" + nmeaStr;
		}
		 
		// check checksum
		String checksum = StringUtils.substringAfterLast(nmeaStr, "*");
		if (StringUtils.isEmpty(checksum)) {
			throw new InterpreterException(
				"checksum value of nmea string must not be null.");
		} 		
		checkHexChecksum(
			StringUtils.mid(nmeaStr, 1, 
			nmeaStr.length() - checksum.length() - 2), 
			checksum);		
		
		// check if position data is valid.
		if (idxValidIndicator != IDX_NOT_SET) {
			position.setValid(!StringUtils.equals(dataItems[idxValidIndicator], "V") &&
				!StringUtils.equals(dataItems[idxValidIndicator], "0"));
		} else {
			position.setValid(true);
		}
		
		// set timestamp.
		position.setTimeRecorded(
			CommonUtils.getDateTime(
				(idxDateRecorded != -1) ? dataItems[idxDateRecorded] : null, 
				(idxTimeRecorded != -1) ? dataItems[idxTimeRecorded] : null,
				"ddMMyy", "hhmmss"));		
		
		// set latitude and longitude.
		if (!StringUtils.isEmpty(dataItems[idxLatitude]) &&
			((position.getLatitudeInDecimal() == null) || overrideIfNotNull)) {
			Double latitude =
				degree2decimal(dataItems[idxLatitude], 
					Direction.get(dataItems[idxLatitude + 1]), null);
			position.setLatitudeInDecimal(latitude);
		}
		if (!StringUtils.isEmpty(dataItems[idxLongitude]) &&
			((position.getLongitudeInDecimal() == null) || overrideIfNotNull)) {
			Double longitude =
				degree2decimal(dataItems[idxLongitude], 
					Direction.get(dataItems[idxLongitude + 1]), null);			
			position.setLongitudeInDecimal(longitude);
		}
		
		// set speed.
		if ((idxSpeed != IDX_NOT_SET) && 
			((position.getSpeedInKmPerHour() == null) || overrideIfNotNull)) {
			position.setSpeedInKmPerHour(
				string2double(dataItems[idxSpeed], null));		    
		}
		
		// set altitude.
		if ((idxAltitude != IDX_NOT_SET) && 
			((position.getAltitudeInMtr() == null) || overrideIfNotNull)) {
			position.setAltitudeInMtr(
				string2double(dataItems[idxAltitude], null));		    
		}		
	}
	
	public static DateTime getDateTime(String dateTimeStr, String fmtDateTimeStr) 
		throws InterpreterException {
		if (StringUtils.isEmpty(fmtDateTimeStr)) {
			throw new InterpreterException("fmtDateTimeStr must not be null.");
		}
		DateTime res = null;
		try {
			res = new DateTime(dateTimeStr, fmtDateTimeStr, 
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC));
		} catch (ParseException e) {
			throw new InterpreterException("parsing date/time '" +
				dateTimeStr + "' failed: " + e.toString());
		}
		return res;
	}
	
	public static DateTime getDateTime(String dateStr, String timeStr,
		String fmtDateStr, String fmtTimeStr) 
		throws InterpreterException {
		if (StringUtils.isEmpty(fmtDateStr)) {
			throw new InterpreterException("fmtDateStr must not be null.");
		}
		if (StringUtils.isEmpty(fmtTimeStr)) {
			throw new InterpreterException("fmtTimeStr must not be null.");
		}
		if (StringUtils.isEmpty(dateStr)) {
			dateStr = (new DateTime()).getAsStr(
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC), fmtDateStr);
		}
		if (StringUtils.isEmpty(timeStr)) {
			timeStr = (new DateTime()).getAsStr(
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC), fmtTimeStr);
		}
		
		return getDateTime(dateStr + timeStr, fmtDateStr + fmtTimeStr);
	}
		
	public static Integer string2int(String str, Integer defValue) 
		throws InterpreterException {
		Integer val = defValue;
		if (!StringUtils.isEmpty(str)) {
			try {
				val = Integer.parseInt(str);
			} catch (NumberFormatException e) {
				throw new InterpreterException(e.toString());
			}
		}
		return val;
	}
	
	public static Double string2double(String str, Double defValue)
		throws InterpreterException {
		return string2double(str, Locale.ENGLISH, null);
	}
	
	public static Double string2double(String str, Locale locale, Double defValue) 
		throws InterpreterException {
		if (locale == null) {
			throw new InterpreterException("locale must not be null.");
		}
		Double dbl = defValue;
		if (!StringUtils.isEmpty(str)) {
			str = StringUtils.trimToEmpty(str);
			str = StringUtils.removeStart(str, "+");
			final NumberFormat DBL_FMT = DecimalFormat.getInstance(locale);
			try {
				dbl = DBL_FMT.parse(str).doubleValue();
			} catch (ParseException e) {
				throw new InterpreterException(e.toString());
			}
		}
		return dbl;
	}
	
	public enum Direction {
		North("N", 1d), West("W", -1d), 
		South("S", -1d), East("E", 1d);
		private double mul;
		private static Map<String, Direction> dirMap;
		private static Map<String, Direction> getDirMap() {
			if (dirMap == null) {
				dirMap = new HashMap<String, Direction>();
			}
			return dirMap;
		}
		private Direction(String abb, double mul) {
			this.mul = mul;			
			getDirMap().put(abb, this);			
		}
		public double getMul() {
			return mul;
		}		
		public static Direction get(String abb) 
			throws InterpreterException {
			Direction dir = getDirMap().get(abb);
			if (dir == null) {
				throw new InterpreterException(
					"direction not found, unknown abbreviation '" + abb + "'.");
			}
			return dir;
		}
	}
		
	public static Double degree2decimal(String str, Direction dir,
		Double defValue) throws InterpreterException {
		return degree2decimal(str, dir, Locale.ENGLISH, defValue);
	}
	
	public static Double degree2decimal(String str, Direction dir,
		Locale locale, Double defValue) 
		throws InterpreterException {
		if (str == null) {
			throw new InterpreterException("str must not be null.");
		}
		if (dir == null) {
			throw new InterpreterException("dir must not be null.");
		}
		if (locale == null) {
			throw new InterpreterException("locale must not be null.");
		}
		Double dbl = defValue;
		if (!StringUtils.isEmpty(str)) {
			dbl = string2double(str, locale, null);
			if (dbl == null) {
				dbl = defValue;
			} else {
				Double gm = dbl / 100d;
				Double g = Math.floor(gm);
				Double gk = (gm - g) / 0.6;	
				dbl = g + gk;
				dbl = dbl * dir.getMul();
			}
		}
		return dbl;
	}
	
	public static void checkDataStrLength(String dataStr, 
		int expectedLength) throws InterpreterException {
		if (StringUtils.isEmpty(dataStr)) {
			throw new InterpreterException("dataStr must not be null.");
		}
		if (expectedLength <= 0) {
			throw new InterpreterException(
				"expectedLength must not be less or equal than zero.");
		}
		if (dataStr.length() != expectedLength) {
			throw new InterpreterException("invalid length of data string: " +
				"expected '" + expectedLength + "' " +
				"but found '" + dataStr.length() + "'.");
		}
	}
	
	public static String calcHexChecksum(String dataStr, int checksumLen) 
		throws InterpreterException {
		if (StringUtils.isEmpty(dataStr)) {
			throw new InterpreterException("dataStr must not be null.");
		}
		int calcChecksum = 0;
		for (int idx=0; idx < dataStr.length(); idx++) {
			calcChecksum = calcChecksum ^ dataStr.charAt(idx);
		}		
		String calcChecksumStr = Integer.toHexString(calcChecksum);
		calcChecksumStr = StringUtils.leftPad(calcChecksumStr, 
			checksumLen, '0');
		calcChecksumStr = StringUtils.upperCase(calcChecksumStr);
		return calcChecksumStr;
	}
	
	public static void checkHexChecksum(String dataStr, 
		String expectedChecksumAsHex) throws InterpreterException {
		if (StringUtils.isEmpty(dataStr)) {
			throw new InterpreterException("dataStr must not be null.");
		}
		if (StringUtils.isEmpty(expectedChecksumAsHex)) {
			throw new InterpreterException("expectedChecksumAsHex must not be null.");
		}		
		String calcChecksumStr = calcHexChecksum(
			dataStr, expectedChecksumAsHex.length());
		if (!StringUtils.equalsIgnoreCase(
			calcChecksumStr, expectedChecksumAsHex)) {
			throw new InterpreterException("invalid checksum of data string: " +
				"expected '" + expectedChecksumAsHex + "' " +
				"but calculated '" + calcChecksumStr + "'.");
		}
	}
	
	public static void checkCrc16Checksum(String dataStr, 
		String expectedChecksumAsHex) throws InterpreterException {
		if (StringUtils.isEmpty(dataStr)) {
			throw new InterpreterException("dataStr must not be null.");
		}
		if (StringUtils.isEmpty(expectedChecksumAsHex)) {
			throw new InterpreterException("expectedChecksumAsHex must not be null.");
		}	
	    int crc = 0xFFFF;          // initial value
        int polynomial = 0x1021;   // 0001 0000 0010 0001  (0, 5, 12) 

        byte[] bytes = dataStr.getBytes();

        for (byte b : bytes) {
        	for (int i = 0; i < 8; i++) {
                boolean bit = ((b   >> (7-i) & 1) == 1);
                boolean c15 = ((crc >> 15    & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) crc ^= polynomial;
             }
        }

        crc &= 0xffff;        
	  
		String calcChecksumStr = Integer.toHexString(crc);
		calcChecksumStr = StringUtils.leftPad(calcChecksumStr, 
			expectedChecksumAsHex.length(), '0');
		if (!StringUtils.equalsIgnoreCase(
			calcChecksumStr, expectedChecksumAsHex)) {
			throw new InterpreterException("invalid crc16 checksum of data string: " +
				"expected '" + expectedChecksumAsHex + "' " +
				"but calculated '" + calcChecksumStr + "'.");
		}
    }

	public static void checkCrc32Checksum(String dataStr, 
		String expectedChecksumAsHex) throws InterpreterException {
		if (StringUtils.isEmpty(dataStr)) {
			throw new InterpreterException("dataStr must not be null.");
		}
		if (StringUtils.isEmpty(expectedChecksumAsHex)) {
			throw new InterpreterException("expectedChecksumAsHex must not be null.");
		}	
		CRC32 crc32 = new CRC32(); 
		crc32.update(dataStr.getBytes());
	
		String calcChecksumStr = Long.toHexString(crc32.getValue());
		calcChecksumStr = StringUtils.leftPad(calcChecksumStr, 
			expectedChecksumAsHex.length(), '0');
		if (!StringUtils.equalsIgnoreCase(
			calcChecksumStr, expectedChecksumAsHex)) {
			throw new InterpreterException("invalid crc32 checksum of data string: " +
				"expected '" + expectedChecksumAsHex + "' " +
				"but calculated '" + calcChecksumStr + "'.");
		}
	}
}
