package de.msk.mylivetracker.web.uploader.interpreter.brandsemotion.begps;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter;

/**
 * TcpInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TcpInterpreter extends AbstractDataStrWoDeviceSpecificInterpreter {
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStrInterpreter#isDeviceCompliant(java.lang.String)
	 */
	@Override
	protected boolean isDeviceCompliant(String dataStr)
			throws InterpreterException {
		return StringUtils.startsWith(dataStr, "PIN");
	}

    private static String PREFIX_GPGGA = "gpgga1=";
    private static String PREFIX_GPRMC = "gprmc1=";
    
    /* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter#processWoDeviceSpecific(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected void processWoDeviceSpecific(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		dataReceived.getSenderFromRequest().setSenderId(
			StringUtils.substringBetween(dataStr, "PIN=", "&"));		
		
		String[] dataItems = StringUtils.split(dataStr, "&");
		for (String dataItem : dataItems) {
			if (StringUtils.startsWith(dataItem, PREFIX_GPGGA)) {
				CommonUtils.setPositionFromGpggaStr(dataReceived.getPosition(), 
					StringUtils.substringAfter(dataItem, PREFIX_GPGGA), true);
			} else if (StringUtils.startsWith(dataItem, PREFIX_GPRMC)) {
				CommonUtils.setPositionFromGprmcStr(dataReceived.getPosition(), 
					StringUtils.substringAfter(dataItem, PREFIX_GPRMC), true);
			} 
		}
	}
}