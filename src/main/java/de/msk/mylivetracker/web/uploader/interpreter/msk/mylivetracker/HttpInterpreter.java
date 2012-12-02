package de.msk.mylivetracker.web.uploader.interpreter.msk.mylivetracker;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.http.AbstractHttpServletRequestWoDeviceSpecificInterpreter;
import de.msk.mylivetracker.web.util.FmtUtils;

/**
 * HttpInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class HttpInterpreter extends
		AbstractHttpServletRequestWoDeviceSpecificInterpreter {

	private static final String REQUEST_PARAM_VERSION_CODE = "vco";
	private static final String REQUEST_PARAM_VERSION_NAME = "vna";
	private static final String REQUEST_PARAM_TIMESTAMP = "tst";
	private static final String REQUEST_PARAM_USER_NAME = "usr";
	private static final String REQUEST_PARAM_USER_PASSWORD = "pwd";
	private static final String REQUEST_PARAM_PHONE_NUMBER = "phn";	
	private static final String REQUEST_PARAM_DEVICE_ID = "did";
	private static final String REQUEST_PARAM_TRACK_ID = "tid";
	private static final String REQUEST_PARAM_BATTERY_PERCENT = "pow";
	private static final String REQUEST_PARAM_BATTERY_VOLTAGE = "vol";
	private static final String REQUEST_PARAM_TRACK_NAME = "trn";
	private static final String REQUEST_PARAM_LOCATION_VALID = "val";
	private static final String REQUEST_PARAM_LATITUDE = "lat";
	private static final String REQUEST_PARAM_LONGITUDE = "lon";
	private static final String REQUEST_PARAM_ALTITUDE = "alt";
	private static final String REQUEST_PARAM_SPEED = "spd";
	private static final String REQUEST_PARAM_ACCURACY = "acc";
	private static final String REQUEST_PARAM_COUNT_SATELLITES = "sat";
	private static final String REQUEST_PARAM_DISTANCE = "dst";
	private static final String REQUEST_PARAM_MILEAGE = "mil";
	private static final String REQUEST_PARAM_RUNTIME_WITHOUT_PAUSES = "rtm";
	private static final String REQUEST_PARAM_RUNTIME_WITH_PAUSES = "rtp";
	private static final String REQUEST_PARAM_HEARTRATE = "hrt";
	private static final String REQUEST_PARAM_HEARTRATE_MIN = "hrn";
	private static final String REQUEST_PARAM_HEARTRATE_MAX = "hrm";
	private static final String REQUEST_PARAM_HEARTRATE_AVG = "hra";
	private static final String REQUEST_PARAM_SOS = "sos";
	//private static final String REQUEST_PARAM_SOS_ID = "sosid";
	private static final String REQUEST_PARAM_PHONE_TYPE = "pht";
	private static final String REQUEST_PARAM_NETWORK_TYPE = "nwt";	
	private static final String REQUEST_PARAM_MOBILE_COUNTRY_CODE = "mcc";
	private static final String REQUEST_PARAM_MOBILE_NETWORK_CODE = "mnc";
	private static final String REQUEST_PARAM_MOBILE_NETWORK_NAME = "mnn";
	private static final String REQUEST_PARAM_CELL_ID = "cid";
	private static final String REQUEST_PARAM_LOCAL_AREA_CODE = "lac";
	private static final String REQUEST_PARAM_MESSAGE = "msg";
	private static final String DATE_FMT_REQUEST_PARAM_TIMESTAMP = "ddMMyyHHmmssSSS";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.httpservlet.interpreter.AbstractHttpServletRequestInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, java.util.Map)
	 */
	@Override
	protected void process(
		DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException {
		
		if (parameterMap.containsKey(REQUEST_PARAM_VERSION_CODE)) {
			this.processAux(dataReceived, parameterMap);
		} else {
			HttpInterpreterDeprecated.process(dataReceived, parameterMap);
		}
	}
	
	private static final int MINIMUM_VERSION_CODE = 10;
	
	private void processAux(
		DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException {
		Locale locale = Locale.ENGLISH;

		if (parameterMap.containsKey(REQUEST_PARAM_VERSION_CODE) &&
			parameterMap.containsKey(REQUEST_PARAM_VERSION_NAME)) {			
			String versionCode = parameterMap.get(REQUEST_PARAM_VERSION_CODE);
			Integer versionCodeInt = Integer.valueOf(versionCode);
			String versionName = parameterMap.get(REQUEST_PARAM_VERSION_NAME);
			if (versionCodeInt < MINIMUM_VERSION_CODE) {
				throw new InterpreterException(
					"versioncode '" + versionCodeInt + "' is no longer supported. " +
					"minimum versioncode is '" + MINIMUM_VERSION_CODE + "'");
			} else {
				dataReceived.getSenderState().addState("Version: " + 
					versionName + "-" + versionCode);
			}
		} else {
			throw new InterpreterException(
				"no versioncode and/or versionname found.");
		}
				
		dataReceived.getSenderFromRequest().setPasswordEncoder(
			new PasswordEncoder(
				parameterMap.get(REQUEST_PARAM_DEVICE_ID), 
				parameterMap.get(REQUEST_PARAM_USER_NAME)));
		dataReceived.getSenderFromRequest().setSenderId(parameterMap.get(REQUEST_PARAM_DEVICE_ID));
		dataReceived.getSenderFromRequest().setAuthUsername(parameterMap.get(REQUEST_PARAM_USER_NAME));
		dataReceived.getSenderFromRequest().setAuthPlainPassword(parameterMap.get(REQUEST_PARAM_USER_PASSWORD));
		
		dataReceived.getClientInfo().setTrackName(parameterMap.get(REQUEST_PARAM_TRACK_NAME));
		dataReceived.getClientInfo().setTrackId(parameterMap.get(REQUEST_PARAM_TRACK_ID));
		dataReceived.getClientInfo().setPhoneNumber(parameterMap.get(REQUEST_PARAM_PHONE_NUMBER));
		dataReceived.getClientInfo().setRuntimeWithPausesInMSecs(
			Long.valueOf(parameterMap.get(REQUEST_PARAM_RUNTIME_WITH_PAUSES)));
		dataReceived.getClientInfo().setRuntimeWithoutPausesInMSecs(
			Long.valueOf(parameterMap.get(REQUEST_PARAM_RUNTIME_WITHOUT_PAUSES)));
		
		// timestamp.
		String timestampStr = parameterMap.get(REQUEST_PARAM_TIMESTAMP);
		try {			
			DateTime timestamp = new DateTime(
				timestampStr, DATE_FMT_REQUEST_PARAM_TIMESTAMP, 
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC)); 				
			dataReceived.getPosition().setTimeRecorded(timestamp);
		} catch (ParseException e) {
			throw new InterpreterException("invalid timstamp received: " + timestampStr); 
		}							
		
		if (parameterMap.containsKey(REQUEST_PARAM_DISTANCE)) {
			Double distanceInMtr = 
				Double.valueOf(parameterMap.get(REQUEST_PARAM_DISTANCE));
			dataReceived.getClientInfo().setMileageInMtr(distanceInMtr);
			String distanceAsStr = FmtUtils.getDistanceAsStr(distanceInMtr, locale, true);
			dataReceived.getSenderState().addState("Distance: " + distanceAsStr);			
		}
		
		if (parameterMap.containsKey(REQUEST_PARAM_MILEAGE)) {
			Double mileageInMtr = 
				Double.valueOf(parameterMap.get(REQUEST_PARAM_MILEAGE));
			String mileageAsStr = FmtUtils.getDistanceAsStr(mileageInMtr, locale, true);
			dataReceived.getSenderState().addState("Mileage: " + mileageAsStr);
		}
		
		String batteryPercent = parameterMap.get(REQUEST_PARAM_BATTERY_PERCENT);			
		String batteryVoltage =
			FmtUtils.getDoubleAsStr(parameterMap.get(REQUEST_PARAM_BATTERY_VOLTAGE), 2, locale, null);
		if (!StringUtils.isEmpty(batteryPercent) || 
			!StringUtils.isEmpty(batteryVoltage)) {
			if (batteryPercent == null) {
				batteryPercent = "--";
			} else if (batteryVoltage == null) {
				batteryVoltage = "--";
			}
			dataReceived.getSenderState().addState(
				"Battery: " + batteryPercent + "% [" + batteryVoltage + "V]");
		}
		
		if (parameterMap.containsKey(REQUEST_PARAM_COUNT_SATELLITES)) {
			String countSatellitesStr = parameterMap.get(REQUEST_PARAM_COUNT_SATELLITES);
			dataReceived.getSenderState().addState("Sat: " + countSatellitesStr);
		}
		
		if (parameterMap.containsKey(REQUEST_PARAM_ACCURACY)) {
			String accuracyStr = parameterMap.get(REQUEST_PARAM_ACCURACY);
			dataReceived.getSenderState().addState("Acc: " + accuracyStr + "m");
		}
		
		if (parameterMap.containsKey(REQUEST_PARAM_LATITUDE) &&
			parameterMap.containsKey(REQUEST_PARAM_LONGITUDE)) {
			dataReceived.getPosition().setLatitudeInDecimal(
				CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LATITUDE), locale, null));								
			dataReceived.getPosition().setLongitudeInDecimal(
				CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LONGITUDE), locale, null));												
			dataReceived.getPosition().setAltitudeInMtr(
				CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_ALTITUDE), locale, null));
			Double speedInMtrPerSecs =
				CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_SPEED), locale, null);			
			if (speedInMtrPerSecs != null) {
				dataReceived.getPosition().setSpeedInKmPerHour(speedInMtrPerSecs * 3.6);
			}
			dataReceived.getPosition().setValid(BooleanUtils.toBoolean(parameterMap.get(REQUEST_PARAM_LOCATION_VALID)));
		}

		if (parameterMap.containsKey(REQUEST_PARAM_HEARTRATE)) {
			Integer heartrateInBpm = 
				CommonUtils.string2int(parameterMap.get(REQUEST_PARAM_HEARTRATE), null);
			dataReceived.getCardiacFunction().
				setHeartrateInBpm(heartrateInBpm);
		}
		if (parameterMap.containsKey(REQUEST_PARAM_HEARTRATE_MIN)) {
			Integer heartrateMinInBpm = 
				CommonUtils.string2int(parameterMap.get(REQUEST_PARAM_HEARTRATE_MIN), null);
			dataReceived.getCardiacFunction().
				setHeartrateMinInBpm(heartrateMinInBpm);
		}
		if (parameterMap.containsKey(REQUEST_PARAM_HEARTRATE_MAX)) {
			Integer heartrateMaxInBpm = 
				CommonUtils.string2int(parameterMap.get(REQUEST_PARAM_HEARTRATE_MAX), null);			
			dataReceived.getCardiacFunction().
				setHeartrateMaxInBpm(heartrateMaxInBpm);
		}
		if (parameterMap.containsKey(REQUEST_PARAM_HEARTRATE_AVG)) {
			Integer heartrateAvgInBpm = 
				CommonUtils.string2int(parameterMap.get(REQUEST_PARAM_HEARTRATE_AVG), null);
			dataReceived.getCardiacFunction().
				setHeartrateAvgInBpm(heartrateAvgInBpm);
		}
		
		if (parameterMap.containsKey(REQUEST_PARAM_SOS)) {
			boolean sos = 
				BooleanUtils.toBoolean(parameterMap.get(REQUEST_PARAM_SOS));
			dataReceived.getEmergencySignal().setActive(sos);
			if (sos) {
				dataReceived.setDeviceActionExecutor(DeviceActionExecutor.EmergencyActivated);
			} else {
				dataReceived.setDeviceActionExecutor(DeviceActionExecutor.EmergencyDeactivated);
			}
		}
		if (parameterMap.containsKey(REQUEST_PARAM_PHONE_TYPE)) {
			dataReceived.getSenderState().addState(
				"phType: " + parameterMap.get(REQUEST_PARAM_PHONE_TYPE));
		}
		if (parameterMap.containsKey(REQUEST_PARAM_NETWORK_TYPE)) {
			dataReceived.getSenderState().addState(
				"nwType: " + parameterMap.get(REQUEST_PARAM_NETWORK_TYPE));
		}
		if (parameterMap.containsKey(REQUEST_PARAM_MOBILE_COUNTRY_CODE)) {
			dataReceived.getMobNwCell().setMobileCountryCode(
				parameterMap.get(REQUEST_PARAM_MOBILE_COUNTRY_CODE));
		}
		if (parameterMap.containsKey(REQUEST_PARAM_MOBILE_NETWORK_CODE)) {
			dataReceived.getMobNwCell().setMobileNetworkCode(
				parameterMap.get(REQUEST_PARAM_MOBILE_NETWORK_CODE));
		}
		if (parameterMap.containsKey(REQUEST_PARAM_MOBILE_NETWORK_NAME)) {
			dataReceived.getMobNwCell().setNetworkName(
				parameterMap.get(REQUEST_PARAM_MOBILE_NETWORK_NAME));
		}
		if (parameterMap.containsKey(REQUEST_PARAM_CELL_ID)) {
			dataReceived.getMobNwCell().setCellId(
				Integer.valueOf(parameterMap.get(REQUEST_PARAM_CELL_ID)));
		}
		if (parameterMap.containsKey(REQUEST_PARAM_LOCAL_AREA_CODE)) {
			dataReceived.getMobNwCell().setLocalAreaCode(
				Integer.valueOf(parameterMap.get(REQUEST_PARAM_LOCAL_AREA_CODE)));
		}
		if (parameterMap.containsKey(REQUEST_PARAM_MESSAGE)) {
			dataReceived.getMessage().setContent(
				parameterMap.get(REQUEST_PARAM_MESSAGE));
		}
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.http.AbstractHttpServletRequestInterpreter#postProcess(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific)
	 */
	@Override
	public String postProcess(DataReceivedVo dataReceived,
		IDeviceSpecific deviceSpecific) throws InterpreterException {
		String res = "OK";
		if (!dataReceived.isValid()) {
			res = "INVALID"; 
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.http.AbstractHttpServletRequestInterpreter#onError(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific)
	 */
	@Override
	public String onError(DataReceivedVo dataReceived,
		Exception exception,	
		IDeviceSpecific deviceSpecific) {		
		return "ERROR";
	}					
}
