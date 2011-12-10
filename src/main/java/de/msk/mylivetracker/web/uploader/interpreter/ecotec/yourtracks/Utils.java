package de.msk.mylivetracker.web.uploader.interpreter.ecotec.yourtracks;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;

/**
 * Utils.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public final class Utils {

	public static String cleanStr(String dataStr) {
		dataStr = StringUtils.remove(dataStr, "\r");
		dataStr = StringUtils.remove(dataStr, "\n");
		return dataStr;
	}
	
	private static String FR_SEPARATORS = ",*";	
	
	private static int POSITION_COUNT_FIELDS = 3;
	private static String POSITION_DATATYPE_NMEA = "NMEA";
	private static int POSITION_VALUE_IDX_DATATYPE = 1;
	private static int POSITION_VALUE_IDX_CHECKSUM = 2;	
	
	public static void interpretFrwdt(
		String prefix, String prefixGprmc, DataReceivedVo dataReceived, String frwdtStr) 
		throws InterpreterException {
		String[] parts = StringUtils.splitPreserveAllTokens(
			StringUtils.substringBefore(frwdtStr, prefixGprmc), FR_SEPARATORS);
		if (parts.length != POSITION_COUNT_FIELDS) {
			throw new InterpreterException(prefix + " data string is invalid: found " +
				parts.length + " field values, but expected " + POSITION_COUNT_FIELDS + " field values.");												
		}
		if (!StringUtils.equals(parts[POSITION_VALUE_IDX_DATATYPE], POSITION_DATATYPE_NMEA)) {
			throw new InterpreterException(prefix + " data type is invalid: found '" +
				parts[POSITION_VALUE_IDX_DATATYPE] + "', but expected '" +
				POSITION_DATATYPE_NMEA + "'.");
		}		
		CommonUtils.checkHexChecksum(
			StringUtils.substringBetween(frwdtStr, "$", "*"), parts[POSITION_VALUE_IDX_CHECKSUM]);
		CommonUtils.setPositionFromGprmcStr(dataReceived.getPosition(), 
			prefixGprmc + StringUtils.substringAfter(frwdtStr, prefixGprmc), true);
	}
	
	private static int LOGIN_IMEI_COUNT_FIELDS = 4;
	private static int LOGIN_IMEI_VALUE_IDX_IMEI = 2;
	private static int LOGIN_IMEI_VALUE_IDX_CHECKSUM = 3;
	private static int LOGIN_USR_PWD_COUNT_FIELDS = 5;
	private static int LOGIN_USR_PWD_VALUE_IDX_USERNAME = 2;
	private static int LOGIN_USR_PWD_VALUE_IDX_PASSWORD = 3;
	private static int LOGIN_USR_PWD_VALUE_IDX_CHECKSUM = 4;
	
	public static void interpretFrlin(
		String prefix, DataReceivedVo dataReceived, String frlinStr) 
		throws InterpreterException {
		dataReceived.getSenderFromRequest().setSender(null);
		String[] parts = StringUtils.splitPreserveAllTokens(frlinStr, FR_SEPARATORS);
		if ((parts.length != LOGIN_IMEI_COUNT_FIELDS) && 
			(parts.length != LOGIN_USR_PWD_COUNT_FIELDS)) {
			throw new InterpreterException(prefix + " data string is invalid: found " +
				parts.length + " field values, but expected " + 
				LOGIN_IMEI_COUNT_FIELDS + " or " + LOGIN_USR_PWD_COUNT_FIELDS +
				" field values.");				
		}
		if (parts.length == LOGIN_IMEI_COUNT_FIELDS) {
			CommonUtils.checkHexChecksum(
				StringUtils.substringBetween(frlinStr, "$", "*"), parts[LOGIN_IMEI_VALUE_IDX_CHECKSUM]);
			dataReceived.getSenderFromRequest().setSenderId(parts[LOGIN_IMEI_VALUE_IDX_IMEI]);
		} else if  (parts.length == LOGIN_USR_PWD_COUNT_FIELDS) {
			CommonUtils.checkHexChecksum(
				StringUtils.substringBetween(frlinStr, "$", "*"), parts[LOGIN_USR_PWD_VALUE_IDX_CHECKSUM]);
			dataReceived.getSenderFromRequest().setSenderId(parts[LOGIN_USR_PWD_VALUE_IDX_USERNAME]);
			dataReceived.getSenderFromRequest().setAuthUsername(parts[LOGIN_USR_PWD_VALUE_IDX_USERNAME]);
			dataReceived.getSenderFromRequest().setAuthPlainPassword(parts[LOGIN_USR_PWD_VALUE_IDX_PASSWORD]);
			dataReceived.getSenderFromRequest().setPasswordEncoder(new PasswordEncoder());
		}			
	}
	
	private static final int FRPOS_COUNT_ITEMS_MIN = 11;
	private static final int FRPOS_COUNT_ITEMS_MAX = 11;
	private static final int FRPOS_IDX_VALID_INDICATOR = CommonUtils.IDX_NOT_SET;
	private static final int FRPOS_IDX_DATE_RECORDED = 8;
	private static final int FRPOS_IDX_TIME_RECORDED = 9;
	private static final int FRPOS_IDX_LATITUDE = 1;
	private static final int FRPOS_IDX_LONGITUDE = 3;
	private static final int FRPOS_IDX_ALTITUDE = 5;
	private static final int FRPOS_IDX_SPEED = 6;
	
	public static void interpretFrpos(
		String prefix, DataReceivedVo dataReceived, String frposStr) 
		throws InterpreterException {
		
		if (dataReceived == null) {
			throw new IllegalArgumentException("dataReceived must not be null.");
		}
		if (StringUtils.isEmpty(frposStr)) {
			throw new IllegalArgumentException("frposStr must not be empty.");
		}		
		CommonUtils.setPositionFromNmea183Str(
			dataReceived.getPosition(), frposStr, true,
			prefix,
			FRPOS_COUNT_ITEMS_MIN,
			FRPOS_COUNT_ITEMS_MAX,
			FRPOS_IDX_VALID_INDICATOR,
			FRPOS_IDX_DATE_RECORDED,
			FRPOS_IDX_TIME_RECORDED,
			FRPOS_IDX_LATITUDE,
			FRPOS_IDX_LONGITUDE,
			FRPOS_IDX_ALTITUDE,
			FRPOS_IDX_SPEED);	
		if ((dataReceived.getPosition().getLatitudeInDecimal() == 0d) &&
			(dataReceived.getPosition().getLongitudeInDecimal() == 0d)) {
			dataReceived.getPosition().setValid(false);
		}
	}
	
	private static final int FRVAL_COUNT_FIELDS = 6;
	private static final int FRVAL_VALUE_IDX_STATE = 2;
	private static final int FRVAL_VALUE_IDX_CHECKSUM = 5;
	private static String FRVAL_INDICATOR_SOS = "Button1";
	private static String FRVAL_INDICATOR_BATTERY = "Analog1";
	private static String FRVAL_INDICATOR_DEVICESTATE = "Device";
	private static String FRVAL_INDICATOR_CARRIER = "Carrier";
	
	public static void interpretFrval(
		String prefix, DataReceivedVo dataReceived, String frvalStr) 
		throws InterpreterException {
		if (dataReceived == null) {
			throw new IllegalArgumentException("dataReceived must not be null.");
		}
		if (StringUtils.isEmpty(frvalStr)) {
			throw new IllegalArgumentException("frvalStr must not be empty.");
		}
		String[] parts = StringUtils.splitPreserveAllTokens(frvalStr, ",*");
		if (parts.length != FRVAL_COUNT_FIELDS) {
			throw new InterpreterException(prefix + " data string is invalid: found " +
				parts.length + " field values, but expected " + FRVAL_COUNT_FIELDS + " field values.");				
		}
		CommonUtils.checkHexChecksum(
			StringUtils.substringBetween(frvalStr, "$", "*"), parts[FRVAL_VALUE_IDX_CHECKSUM]);
		if (StringUtils.contains(frvalStr, FRVAL_INDICATOR_SOS)) {
			if (StringUtils.equals(parts[FRVAL_VALUE_IDX_STATE], "1")) {
				dataReceived.getEmergencySignal().setActive(true);
				dataReceived.setDeviceActionExecutor(DeviceActionExecutor.EmergencyActivated);
			}
		} else if (StringUtils.contains(frvalStr, FRVAL_INDICATOR_BATTERY)) {
			dataReceived.getSenderState().addState(
				"Battery = " + parts[FRVAL_VALUE_IDX_STATE] + "%");
		} else if (StringUtils.contains(frvalStr, FRVAL_INDICATOR_DEVICESTATE)) {
			dataReceived.getSenderState().addState(parts[FRVAL_VALUE_IDX_STATE]);
		} else if (StringUtils.contains(frvalStr, FRVAL_INDICATOR_CARRIER)) {
			dataReceived.getSenderState().addState(parts[FRVAL_VALUE_IDX_STATE]);
		}
	}
}
