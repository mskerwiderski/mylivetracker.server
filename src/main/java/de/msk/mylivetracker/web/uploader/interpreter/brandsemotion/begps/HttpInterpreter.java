package de.msk.mylivetracker.web.uploader.interpreter.brandsemotion.begps;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

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
public class HttpInterpreter extends AbstractHttpServletRequestInterpreter {

	//
	// PIN=2268A60F&gpgga1=$GPGGA,114906,,,,,0,,,,M,,M,,*6D&gprmc1=$GPRMC,114906,V,,,,,,,060211,,,N*5C
	//
	private static final String REQUEST_PARAM_SENDER_ID = "PIN";
	private static final String REQUEST_PARAM_GPGGA = "gpgga1";	
	private static final String REQUEST_PARAM_GPRMC = "gprmc1";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.httpservlet.interpreter.AbstractHttpServletRequestInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, java.util.Map)
	 */
	@Override
	protected void process(
		DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException {
		dataReceived.getSenderFromRequest().setSenderId(
			parameterMap.get(REQUEST_PARAM_SENDER_ID));
	
		String gpggaStr = parameterMap.get(REQUEST_PARAM_GPGGA);
		if (!StringUtils.isEmpty(gpggaStr)) {
			CommonUtils.setPositionFromGpggaStr(
				dataReceived.getPosition(), gpggaStr, true);
		}
		
		String gprmcStr = parameterMap.get(REQUEST_PARAM_GPRMC);
		if (!StringUtils.isEmpty(gprmcStr)) {
			CommonUtils.setPositionFromGprmcStr(
				dataReceived.getPosition(), gprmcStr, true);
		}		
	}
}
