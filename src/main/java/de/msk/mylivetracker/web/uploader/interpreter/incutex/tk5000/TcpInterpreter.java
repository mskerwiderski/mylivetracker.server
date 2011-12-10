package de.msk.mylivetracker.web.uploader.interpreter.incutex.tk5000;

import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter;
import de.msk.mylivetracker.web.util.FmtUtils;
import de.msk.mylivetracker.web.util.WebUtils;

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

	//
	// 1321947318,20071215185638,11.537557,52.136087,0,110,0,6,0
	// 1111111111,20110420114253,13.094078,49.062390,0,156,614,6,2,0.0
	// 1111111111,20110427134436,13.093906,49.062461,0,000,629,8,2,0.3
	// 012403001898750,20110812211646,0,0,0,0,0,0,2
	//
	// 1000000001		: id
	// 20100416174142	: timestamp (YYYYMMDDHHMMSS)
	// 13.093991		: longitude
	// 49.062643		: latitude
	// 0				: speed
	// 050				: direction
	// 635				: altitude
	// 8				: count satellites
	// 2				: message code
	//
	
	private static final String SEPERATOR = ",";
	private static final String EVENT_ID_TRACKING_POSITIONS = "2";
	private static final String EVENT_ID_BATTERY_LOW = "40";
	private static final String EVENT_ID_EMERGENCY_SIGNAL = "4";
	protected static final String MSG_LOW_BATTERY = "Low battery!";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStrInterpreter#isDeviceCompliant(java.lang.String)
	 */
	@Override
	protected boolean isDeviceCompliant(String dataStr)
			throws InterpreterException {
		dataStr = StringUtils.chomp(dataStr);
		int cntSeps = StringUtils.countMatches(dataStr, SEPERATOR);
		return 
			StringUtils.containsOnly(dataStr, "0123456789.,") &&
			(cntSeps >= 8);
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter#processWoDeviceSpecific(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected void processWoDeviceSpecific(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		Locale locale = Locale.ENGLISH;
		
		dataStr = StringUtils.chomp(dataStr);
		String[] dataItems = StringUtils.splitPreserveAllTokens(dataStr, ",");
		
		if ((dataItems.length < 9) || (dataItems.length > 10)) {
			throw new InterpreterException("invalid count of data items: " + dataItems.length);
		}
		
		// sender id.
		String senderId = dataItems[0];		
		dataReceived.getSenderFromRequest().setSenderId(senderId);
		
		if (dataItems.length > 9) {
			Double distInMiles = CommonUtils.string2double(dataItems[9], locale, null);
			if (distInMiles != null) {
				Double mileageInMtr = WebUtils.miles2meter(distInMiles);
				dataReceived.getClientInfo().setMileageInMtr(mileageInMtr);				
				String mileageAsStr = FmtUtils.getDistanceAsStr(mileageInMtr, Locale.ENGLISH, true);
				dataReceived.getSenderState().addState("Mileage: " + mileageAsStr);
			}
		}		
		
		// time recorded.
		dataReceived.getPosition().setTimeRecorded(
			CommonUtils.getDateTime(
				dataItems[1], "yyyyMMddhhmmss"));
		
		// latitudeInDecimal / longitudeInDecimal.
		dataReceived.getPosition().setLongitudeInDecimal(
			CommonUtils.string2double(dataItems[2], null));
		dataReceived.getPosition().setLatitudeInDecimal(
			CommonUtils.string2double(dataItems[3], null));	
	
		// speed.
		dataReceived.getPosition().setSpeedInKmPerHour(
			CommonUtils.string2double(dataItems[4], null));
		
		// altitude.
		dataReceived.getPosition().setAltitudeInMtr(
			CommonUtils.string2double(dataItems[6], null));	
		
		// count satellites.
		dataReceived.getPosition().setValid(
			CommonUtils.string2int(dataItems[7], 0) >= 4);
		
		// event id.
		if (StringUtils.equals(dataItems[8], EVENT_ID_EMERGENCY_SIGNAL)) {
			dataReceived.getEmergencySignal().setActive(Boolean.TRUE);
		} else if (StringUtils.equals(dataItems[8], EVENT_ID_BATTERY_LOW)) {
			dataReceived.getSenderState().addState(MSG_LOW_BATTERY);
		} else if (StringUtils.equals(dataItems[8], EVENT_ID_TRACKING_POSITIONS)) {
			// noop.
		} else {
			// noop.
		}
	}
}
