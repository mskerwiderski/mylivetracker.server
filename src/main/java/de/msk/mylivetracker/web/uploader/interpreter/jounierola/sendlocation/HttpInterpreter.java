package de.msk.mylivetracker.web.uploader.interpreter.jounierola.sendlocation;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
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

	private static final String REQUEST_PARAM_DEVICEID = "deviceid";
	private static final String REQUEST_PARAM_USER_NAME = "usr";
	private static final String REQUEST_PARAM_USER_PASSWORD = "pwd";
	private static final String REQUEST_PARAM_LATITUDE = "lat";
	private static final String REQUEST_PARAM_LONGITUDE = "lon";
	private static final String REQUEST_PARAM_SPEED = "speed";	
	private static final String REQUEST_PARAM_ALTITUDE = "altitude";
	//private static final String REQUEST_PARAM_HEADING = "heading";
	private static final String REQUEST_PARAM_VACC = "vacc";
	private static final String REQUEST_PARAM_HACC = "hacc";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.httpservlet.interpreter.AbstractHttpServletRequestInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, java.util.Map)
	 */
	@Override
	protected void process(
		DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException {
		Locale locale = Locale.ENGLISH;		
		dataReceived.getSenderFromRequest().setSenderId(parameterMap.get(REQUEST_PARAM_DEVICEID));
		dataReceived.getSenderFromRequest().setAuthUsername(parameterMap.get(REQUEST_PARAM_USER_NAME));
		dataReceived.getSenderFromRequest().setAuthPlainPassword(parameterMap.get(REQUEST_PARAM_USER_PASSWORD));
		dataReceived.getPosition().setLatitudeInDecimal(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LATITUDE), locale, null));								
		dataReceived.getPosition().setLongitudeInDecimal(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LONGITUDE), locale, null));												
		dataReceived.getPosition().setAltitudeInMtr(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_ALTITUDE), locale, null));
		dataReceived.getPosition().setSpeedInKmPerHour(
			CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_SPEED), locale, null));
		
		boolean posValid = 
			accuracyIsAdequate(parameterMap, REQUEST_PARAM_VACC) &&
			accuracyIsAdequate(parameterMap, REQUEST_PARAM_HACC);
		
		dataReceived.getPosition().setValid(posValid);
	}
	
	private static boolean accuracyIsAdequate(Map<String, String> parameterMap, String parameterName) {
		boolean res = false;
		String valueStr = parameterMap.get(parameterName);
		Double value = null;
		if (!StringUtils.isEmpty(valueStr)) {
			try {
				value = Double.parseDouble(valueStr);
			} catch (NumberFormatException e) {
				value = null;
			}
		}
		if (value != null) {
			res = value > 0.0d;
		}
		return res;
	}
}
