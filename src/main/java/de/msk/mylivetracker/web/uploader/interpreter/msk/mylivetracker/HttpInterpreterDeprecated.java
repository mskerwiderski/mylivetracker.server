package de.msk.mylivetracker.web.uploader.interpreter.msk.mylivetracker;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.BooleanUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.util.FmtUtils;

/**
 * HttpInterpreterDeprecated.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class HttpInterpreterDeprecated {

	private static final String REQUEST_PARAM_TIMESTAMP = "tst";
	private static final String REQUEST_PARAM_USER_NAME = "usr";
	private static final String REQUEST_PARAM_USER_PASSWORD = "pwd";	
	private static final String REQUEST_PARAM_DEVICE_ID = "did";
	private static final String REQUEST_PARAM_TRACK_ID = "tid";
	private static final String REQUEST_PARAM_TRACK_NAME = "trn";
	private static final String REQUEST_PARAM_LATITUDE = "lat";
	private static final String REQUEST_PARAM_LONGITUDE = "lon";
	private static final String REQUEST_PARAM_ALTITUDE = "alt";
	private static final String REQUEST_PARAM_SPEED = "spd";
	private static final String REQUEST_PARAM_ACCURACY = "acc";
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
	private static final String DATE_FMT_REQUEST_PARAM_TIMESTAMP = "ddMMyyHHmmssSSS";
	
	protected static void process(
		DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException {
		Locale locale = Locale.ENGLISH;

		dataReceived.getSenderFromRequest().setPasswordEncoder(new PasswordEncoderDeprecated());
		dataReceived.getSenderFromRequest().setSenderId(parameterMap.get(REQUEST_PARAM_DEVICE_ID));
		dataReceived.getSenderFromRequest().setAuthUsername(parameterMap.get(REQUEST_PARAM_USER_NAME));
		dataReceived.getSenderFromRequest().setAuthPlainPassword(parameterMap.get(REQUEST_PARAM_USER_PASSWORD));
		
		dataReceived.getClientInfo().setTrackName(parameterMap.get(REQUEST_PARAM_TRACK_NAME));
		dataReceived.getClientInfo().setTrackId(parameterMap.get(REQUEST_PARAM_TRACK_ID));		
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
			String distanceAsStr = FmtUtils.getDistanceAsStr(distanceInMtr, Locale.GERMAN, false);
			dataReceived.getSenderState().addState("DistanceOrg: " + distanceAsStr);			
		}
		
		if (parameterMap.containsKey(REQUEST_PARAM_MILEAGE)) {
			Double mileageInMtr = 
				Double.valueOf(parameterMap.get(REQUEST_PARAM_MILEAGE));
			String mileageAsStr = FmtUtils.getDistanceAsStr(mileageInMtr, Locale.GERMAN, false);
			dataReceived.getSenderState().addState("MileageOrg: " + mileageAsStr);
		}
		
		if (parameterMap.containsKey(REQUEST_PARAM_ACCURACY)) {
			String accuracyStr = parameterMap.get(REQUEST_PARAM_ACCURACY);
			dataReceived.getSenderState().addState("Acc: " + accuracyStr);
		}
		
		if (parameterMap.containsKey(REQUEST_PARAM_LATITUDE) &&
			parameterMap.containsKey(REQUEST_PARAM_LONGITUDE)) {
			dataReceived.getPosition().setLatitudeInDecimal(
				CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LATITUDE), locale, null));								
			dataReceived.getPosition().setLongitudeInDecimal(
				CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LONGITUDE), locale, null));												
			dataReceived.getPosition().setAltitudeInMtr(
				CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_ALTITUDE), locale, null));
			dataReceived.getPosition().setSpeedInKmPerHour(
				CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_SPEED), locale, null));		
		}

		if (parameterMap.containsKey(REQUEST_PARAM_HEARTRATE)) {
			Integer heartrateInBpm = Integer.valueOf(
				parameterMap.get(REQUEST_PARAM_HEARTRATE));
			dataReceived.getCardiacFunction().
				setHeartrateInBpm(heartrateInBpm);
		}
		if (parameterMap.containsKey(REQUEST_PARAM_HEARTRATE_MIN)) {
			Integer heartrateMinInBpm = Integer.valueOf(
				parameterMap.get(REQUEST_PARAM_HEARTRATE_MIN));
			dataReceived.getCardiacFunction().
				setHeartrateMinInBpm(heartrateMinInBpm);
		}
		if (parameterMap.containsKey(REQUEST_PARAM_HEARTRATE_MAX)) {
			Integer heartrateMaxInBpm = Integer.valueOf(
				parameterMap.get(REQUEST_PARAM_HEARTRATE_MAX));
			dataReceived.getCardiacFunction().
				setHeartrateMaxInBpm(heartrateMaxInBpm);
		}
		if (parameterMap.containsKey(REQUEST_PARAM_HEARTRATE_AVG)) {
			Integer heartrateAvgInBpm = Integer.valueOf(
				parameterMap.get(REQUEST_PARAM_HEARTRATE_AVG));
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
	}
}
