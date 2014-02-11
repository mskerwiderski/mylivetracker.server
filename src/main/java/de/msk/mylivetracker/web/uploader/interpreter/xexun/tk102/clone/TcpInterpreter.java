package de.msk.mylivetracker.web.uploader.interpreter.xexun.tk102.clone;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils.Direction;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter;

/**
 * TcpInterpreter.
 * 
 * @author michael skerwiderski, (c)2014
 * 
 * @version 000
 * 
 * history
 * 000 2014-02-10 initial.
 * 
 */
public class TcpInterpreter extends AbstractDataStrWoDeviceSpecificInterpreter {

	// (027042205000BR00140203A5008.1142N00830.2298E000.71459010.000000000000L00000000)
	
	private static final int IDX_START_ID = 1;
	private static final int IDX_END_ID = 13;
	private static final int IDX_START_DATE = 17;
	private static final int IDX_END_DATE = 23;
	private static final int IDX_START_TIME = 50;
	private static final int IDX_END_TIME = 56;
	private static final int IDX_START_STRVALID = 23;
	private static final int IDX_END_STRVALID = 24;
	private static final int IDX_START_LATITUDE = 24;
	private static final int IDX_END_LATITUDE = 33;
	private static final int IDX_START_LONGITUDE = 34;
	private static final int IDX_END_LONGITUDE = 44;
	
	private static final String STRING_VALID_FLAG = "A";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStrInterpreter#isDeviceCompliant(java.lang.String)
	 */
	@Override
	protected boolean isDeviceCompliant(String dataStr)
			throws InterpreterException {
		return 
			(StringUtils.startsWith(dataStr, "(") || 
			StringUtils.endsWith(dataStr, ")") &&
			StringUtils.contains(dataStr, "BR") &&
			(StringUtils.length(dataStr) >= 80) &&
			StringUtils.substring(dataStr, IDX_START_STRVALID, IDX_END_STRVALID).
				equals(STRING_VALID_FLAG));
	}
		
    /* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter#processWoDeviceSpecific(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected void processWoDeviceSpecific(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		
		// sender id.
		String senderId = StringUtils.substring(dataStr, IDX_START_ID, IDX_END_ID);
		dataReceived.getSenderFromRequest().setSenderId(senderId);

		// time recorded.
		dataReceived.getPosition().setTimeRecorded(
			CommonUtils.getDateTime(
			StringUtils.substring(dataStr, IDX_START_DATE, IDX_END_DATE) +
			StringUtils.substring(dataStr, IDX_START_TIME, IDX_END_TIME), "yyMMddhhmmss"));

		// latitude.
		Double latitude = CommonUtils.degree2decimal(
			StringUtils.substring(dataStr, IDX_START_LATITUDE, IDX_END_LATITUDE), 
				Direction.get(StringUtils.substring(dataStr, 
					IDX_END_LATITUDE, IDX_END_LATITUDE+1)), null);
		dataReceived.getPosition().setLatitudeInDecimal(latitude);
		
		// longitude.
		Double longitude = CommonUtils.degree2decimal(
			StringUtils.substring(dataStr, IDX_START_LONGITUDE, IDX_END_LONGITUDE), 
				Direction.get(StringUtils.substring(dataStr, 
					IDX_END_LONGITUDE, IDX_END_LONGITUDE+1)), null);
		dataReceived.getPosition().setLongitudeInDecimal(longitude);
		
		// valid flag.
		dataReceived.getPosition().setValid(true);
	}
}
