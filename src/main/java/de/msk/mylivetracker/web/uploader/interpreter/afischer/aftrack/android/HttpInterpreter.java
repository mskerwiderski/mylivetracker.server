package de.msk.mylivetracker.web.uploader.interpreter.afischer.aftrack.android;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.interpreter.afischer.aftrack.HttpRequestParams;
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

	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.httpservlet.interpreter.AbstractHttpServletRequestInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, java.util.Map)
	 */
	@Override
	protected void process(
		DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException {
		// authorization.
		dataReceived.getSenderFromRequest().setSenderId(parameterMap.get(HttpRequestParams.IMEI));
		dataReceived.getSenderFromRequest().setAuthUsername(
			parameterMap.get(HttpRequestParams.IMEI));
		dataReceived.getSenderFromRequest().setAuthPlainPassword(
			parameterMap.get(HttpRequestParams.USER_PASSWORD));		
		dataReceived.getSenderFromRequest().setPasswordEncoder(
			new PasswordEncoder());
		
		String trackNameStr = parameterMap.get(HttpRequestParams.TRACK_NAME);
		if (!StringUtils.isEmpty(trackNameStr)) {
			dataReceived.getClientInfo().setTrackName(trackNameStr);
		}
		
		String dateTimeStr = parameterMap.get(HttpRequestParams.DATE_TIME);
		if (!StringUtils.isEmpty(dateTimeStr)) {
			dataReceived.getPosition().setTimeRecorded(
				CommonUtils.getDateTime(dateTimeStr, 
					"yyyy-MM-dd'T'hh:mm:ss"));
		}
		
		dataReceived.getPosition().setLatitudeInDecimal(
			CommonUtils.string2double(parameterMap.get(HttpRequestParams.LATITUDE), null));								
		dataReceived.getPosition().setLongitudeInDecimal(
			CommonUtils.string2double(parameterMap.get(HttpRequestParams.LONGITUDE), null));												
		dataReceived.getPosition().setAltitudeInMtr(
			CommonUtils.string2double(parameterMap.get(HttpRequestParams.ALTITUDE), null));
		dataReceived.getPosition().setSpeedInKmPerHour(
			CommonUtils.string2double(parameterMap.get(HttpRequestParams.SPEED), null));					
	}
}
