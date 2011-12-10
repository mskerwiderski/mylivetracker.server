package de.msk.mylivetracker.web.uploader.interpreter.afischer.aftrack;

import java.util.Map;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.http.AbstractHttpServletRequestInterpreter;

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
		AbstractHttpServletRequestInterpreter {

	private static final String REQUEST_PARAM_IMEI = "imei";
	private static final String REQUEST_PARAM_USER_PASSWORD = "pwd";
	private static final String REQUEST_PARAM_TRACK_NAME = "trname";
	private static final String REQUEST_PARAM_DATE_TIME = "datetime";
	private static final String REQUEST_PARAM_LATITUDE = "lat";
	private static final String REQUEST_PARAM_LONGITUDE = "lon";
	private static final String REQUEST_PARAM_ALTITUDE = "alt";
	private static final String REQUEST_PARAM_SPEED = "speed";
	@SuppressWarnings("unused")
	private static final String REQUEST_PARAM_COURSE = "course";	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.httpservlet.interpreter.AbstractHttpServletRequestInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, java.util.Map)
	 */
	@Override
	protected void process(
		DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException {
		dataReceived.getSenderFromRequest().setSenderId(parameterMap.get(REQUEST_PARAM_IMEI));
		dataReceived.getSenderFromRequest().setAuthUsername(
			parameterMap.get(REQUEST_PARAM_IMEI));
		dataReceived.getSenderFromRequest().setAuthPlainPassword(
			parameterMap.get(REQUEST_PARAM_USER_PASSWORD));		
		dataReceived.getClientInfo().setTrackName(parameterMap.get(REQUEST_PARAM_TRACK_NAME));
		dataReceived.getPosition().setTimeRecorded(
			CommonUtils.getDateTime(parameterMap.get(REQUEST_PARAM_DATE_TIME), 
				"yyyy-MM-dd'T'hh:mm:ss"));		
		dataReceived.getPosition().setLatitudeInDecimal(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LATITUDE), null));								
		dataReceived.getPosition().setLongitudeInDecimal(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LONGITUDE), null));												
		dataReceived.getPosition().setAltitudeInMtr(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_ALTITUDE), null));
		dataReceived.getPosition().setSpeedInKmPerHour(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_SPEED), null));			
	}
}
