package de.msk.mylivetracker.web.uploader.interpreter.aspicore.gsmtracker;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutorAndMessage;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.http.AbstractHttpServletRequestWoDeviceSpecificInterpreter;

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

	private static final String REQUEST_PARAM_IMEI = "imei";
	private static final String REQUEST_PARAM_USER_NAME = "uid";
	private static final String REQUEST_PARAM_USER_PASSWORD = "pwd";	
	private static final String REQUEST_PARAM_DATE_RECORDED = "date";
	private static final String REQUEST_PARAM_TIME_RECORDED = "time";
	private static final String DATE_FMT_REQUEST_PARAM_TIME_RECORDED = "ddMMyyHHmmss.SSS";
	private static final String REQUEST_PARAM_LATITUDE = "lat";
	private static final String REQUEST_PARAM_LONGITUDE = "lon";
	private static final String REQUEST_PARAM_ALTITUDE = "alt";
	private static final String REQUEST_PARAM_SPEED = "speed";
	private static final String REQUEST_PARAM_COURSE = "course";
	private static final String REQUEST_PARAM_HDOP = "hdop";
	private static final String REQUEST_PARAM_POSITION_IS_ACCURATE = "status";	
	private static final String REQUEST_PARAM_LABEL = "label";	
	private static final String REQUEST_PARAM_SIGNAL_STRENGTH = "ssi";
	private static final String REQUEST_PARAM_CELL_ID = "cell";
	private static final String REQUEST_PARAM_LOCAL_AREA_CODE = "lac";
	private static final String REQUEST_PARAM_NETWORK_NAME = "nwname";
	private static final String REQUEST_PARAM_MOBILE_COUNTRY_CODE = "mcc";
	private static final String REQUEST_PARAM_MOBILE_NETWORK_CODE = "mnc";
	private static final String REQUEST_PARAM_MOBILE_MODE = "mode";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.httpservlet.interpreter.AbstractHttpServletRequestInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, java.util.Map)
	 */
	@Override
	protected void process(
		DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException {
		Locale locale = Locale.ENGLISH;

		dataReceived.getSenderFromRequest().setSenderId(parameterMap.get(REQUEST_PARAM_IMEI));
		dataReceived.getSenderFromRequest().setAuthUsername(parameterMap.get(REQUEST_PARAM_USER_NAME));
		dataReceived.getSenderFromRequest().setAuthPlainPassword(parameterMap.get(REQUEST_PARAM_USER_PASSWORD));
		
		// label and device action executor.
		String label = parameterMap.get(REQUEST_PARAM_LABEL);
		DeviceActionExecutorAndMessage deviceActionExecutorAndMessage =
			getDeviceActionExecutorAndMessage(label);		
		dataReceived.setDeviceActionExecutor(deviceActionExecutorAndMessage.getDeviceActionExecutor());
		dataReceived.getMessage().setContent(deviceActionExecutorAndMessage.getMessage());		
		
		// time recorded.
		String dateRecordedStr = parameterMap.get(REQUEST_PARAM_DATE_RECORDED);
		String timeRecordedStr = parameterMap.get(REQUEST_PARAM_TIME_RECORDED);
				
		if (!StringUtils.isEmpty(dateRecordedStr) && 
			!StringUtils.isEmpty(timeRecordedStr)) {
			DateTime timeRecorded = null;
			try {
				timeRecorded = new DateTime(
					dateRecordedStr + timeRecordedStr,  
					DATE_FMT_REQUEST_PARAM_TIME_RECORDED,
					TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC));
			} catch (ParseException e) {
				throw new InterpreterException("invalid date/time received: " + 
					"date:" + dateRecordedStr + " " + 
					"time:" + timeRecordedStr);				
			}
			dataReceived.getPosition().setTimeRecorded(timeRecorded);
		}
		
		// status and position
		boolean latLonAccurate = false;
		String isAccurateStr = parameterMap.get(REQUEST_PARAM_POSITION_IS_ACCURATE);
		if (!StringUtils.isEmpty(isAccurateStr)) {			
			latLonAccurate = (isAccurateStr.equals("V") ? false : true);
		}
		dataReceived.getPosition().setValid(latLonAccurate);		
		dataReceived.getPosition().setLatitudeInDecimal(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LATITUDE), locale, null));								
		dataReceived.getPosition().setLongitudeInDecimal(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LONGITUDE), locale, null));												
		dataReceived.getPosition().setAltitudeInMtr(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_ALTITUDE), locale, null));
		dataReceived.getPosition().setSpeedInKmPerHour(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_SPEED), locale, null));		
								
		// mobile status.
		dataReceived.getMobNwCell().setCellId(
			CommonUtils.string2int(parameterMap.get(REQUEST_PARAM_CELL_ID), null));		
		dataReceived.getMobNwCell().setLocalAreaCode(
			CommonUtils.string2int(parameterMap.get(REQUEST_PARAM_LOCAL_AREA_CODE), null));		
		dataReceived.getMobNwCell().setNetworkName(
			parameterMap.get(REQUEST_PARAM_NETWORK_NAME));
		dataReceived.getMobNwCell().setMobileCountryCode(
			parameterMap.get(REQUEST_PARAM_MOBILE_COUNTRY_CODE));
		dataReceived.getMobNwCell().setMobileNetworkCode(
			parameterMap.get(REQUEST_PARAM_MOBILE_NETWORK_CODE));
		dataReceived.getMobNwCell().setMobileMode(
			CommonUtils.string2int(parameterMap.get(REQUEST_PARAM_MOBILE_MODE), null));		
		
		// other stati.
		String course = parameterMap.get(REQUEST_PARAM_COURSE);
		if (!StringUtils.isEmpty(course)) {
			dataReceived.getSenderState().addState("course: " + course);
		}
		String hdop = parameterMap.get(REQUEST_PARAM_HDOP);
		if (!StringUtils.isEmpty(hdop)) {
			dataReceived.getSenderState().addState("hdop: " + hdop);
		}
		String ssi = parameterMap.get(REQUEST_PARAM_SIGNAL_STRENGTH);
		if (!StringUtils.isEmpty(ssi)) {
			dataReceived.getSenderState().addState("ssi: " + ssi);
		}
	}

	public static DeviceActionExecutorAndMessage getDeviceActionExecutorAndMessage(String label) {
		DeviceActionExecutor deviceActionExecutor = null;
		String message = null;
		if (!StringUtils.isEmpty(label)) {
			String[] parts = StringUtils.split(label, ' ');
			if (parts.length > 0) {
				if (StringUtils.isNumeric(parts[0])) {
					deviceActionExecutor =
						DeviceActionExecutor.getDeviceActionExecutor(parts[0]);
					if (parts.length > 1) {
						message = StringUtils.right(label, label.length() - parts[0].length() - 1);
					}
				} else {
					message = label;
				}
			}
		}
		message = StringUtils.trimToNull(message);
		if (deviceActionExecutor == null) {
			deviceActionExecutor = DeviceActionExecutor.StorePositionMessage; 
		}
		return new DeviceActionExecutorAndMessage(deviceActionExecutor, message);
	}	

}
