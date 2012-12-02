package de.msk.mylivetracker.web.uploader.interpreter.wiebej.gps2opengts;

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
public class HttpInterpreter extends AbstractHttpServletRequestWoDeviceSpecificInterpreter {

	//
	// http://mylivetracker.de/upl_beg.sec?dev=did&gprmc=$GPRMC,115841,A,4806.3194,N,1136.9664,E,0,000.0,120311,,*36&acct=uid
	//
	private static final String REQUEST_PARAM_DEVICE_ID = "dev";
	private static final String REQUEST_PARAM_ACCOUNT_ID = "acct";
	private static final String REQUEST_PARAM_GPRMC = "gprmc";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.httpservlet.interpreter.AbstractHttpServletRequestInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, java.util.Map)
	 */
	@Override
	protected void process(
		DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException {
		dataReceived.getSenderFromRequest().setSenderId(
			parameterMap.get(REQUEST_PARAM_DEVICE_ID));
		dataReceived.getSenderFromRequest().setAuthUsername(
			parameterMap.get(REQUEST_PARAM_ACCOUNT_ID));
		dataReceived.getSenderFromRequest().setAuthPlainPassword(
			parameterMap.get(REQUEST_PARAM_ACCOUNT_ID));
		
		String gprmcStr = parameterMap.get(REQUEST_PARAM_GPRMC);
		if (!StringUtils.isEmpty(gprmcStr)) {
			CommonUtils.setPositionFromGprmcStr(
				dataReceived.getPosition(), gprmcStr, true);
		}		
	}
}
