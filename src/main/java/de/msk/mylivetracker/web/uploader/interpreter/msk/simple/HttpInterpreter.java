package de.msk.mylivetracker.web.uploader.interpreter.msk.simple;

import java.util.Locale;
import java.util.Map;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
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

	private static final String REQUEST_PARAM_DEVICE_ID = "id";
	private static final String REQUEST_PARAM_LATITUDE = "lat";
	private static final String REQUEST_PARAM_LONGITUDE = "lon";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.httpservlet.interpreter.AbstractHttpServletRequestInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, java.util.Map)
	 */
	@Override
	protected void process(
		DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException {
		Locale locale = Locale.ENGLISH;
		
		dataReceived.getSenderFromRequest().setSenderId(parameterMap.get(REQUEST_PARAM_DEVICE_ID));
				
		if (parameterMap.containsKey(REQUEST_PARAM_LATITUDE) &&
			parameterMap.containsKey(REQUEST_PARAM_LONGITUDE)) {
			dataReceived.getPosition().setLatitudeInDecimal(
				CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LATITUDE), locale, null));								
			dataReceived.getPosition().setLongitudeInDecimal(
				CommonUtils.string2double(parameterMap.get(REQUEST_PARAM_LONGITUDE), locale, null));															
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
