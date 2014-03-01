package de.msk.mylivetracker.web.uploader.interpreter.xexun.tk102.clone.xaiox;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils.Direction;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * TcpInterpreter.
 * 
 * @author michael skerwiderski, (c)2014
 * 
 * @version 000
 * 
 * history
 * 000 2014-03-01 initial.
 * 
 */
public class TcpInterpreter extends AbstractDataStrWoDeviceSpecificInterpreter {

	// #EMI15digits#username#statusdigit#password#datatype#datacapacity#base station information#longitude,E,latitude,N,speed,direction#date#time##
	// 
	// e.g.:
	// #353818051525580#13610001016#1#0000#AUT#1#36012#00648.9669,W,5311.2871,N,055.00,000#070114#074248##
	// #353588020140340##1#0000#AUT#01#26200201c3dbef#951.500900,E,5355.149700,N,0.00,0.00#220214#131610.000##
	//
	// 0: Imei Numebr 15 digits 
	// 1: Username is an 11 digit number assigned on the server side e.g. 13610001004 unique ID of the tracker
	// 2: Status Digit is always 1, it is 0 for when it cannot locate.
	// 3: Password is a 4 digit pin for the device, default is 0000 probably not needed in any way with this so may leave at 0000
	// 4: datatype, there are 3 possibilities, AUT proper correct location details, SOS sos alert, LPD Low power alert
	// 5: Data capacity is number of locations being transmitted. i'd imagine easiest would be to just do one location at a time, so this would be 1
	// 6: Base station is the cell tower identifier number
	// 7a: latitude and longitude is a little complicated, but i think they are the same as the TK102 gps. 
	//    this lat/long 53.26932,-6.512361 would be converted like this. 
	//    0.26932*60.15920 so  53.26932  will be convert to be 5316.15920 
	//    0.512361*600.741660  -6.512361 will be -630.741660
	// 7b: Speed in mph (nnn.nn)
	// 7c: direction  in degrees (nnn)
	// 8: date in format DDMMYY
	// 9: Time in format HHMMSS
	//
		
	private static final int IDX_IMEI = 0;
	private static final int IDX_USERNAME = 1;
	private static final int IDX_STATUS = 2;
	private static final int IDX_PASSWORD = 3;
	private static final int IDX_DATATYPE = 4;
	private static final int IDX_POSITION = 7;
	private static final int IDX_DATE = 8;
	private static final int IDX_TIME = 9;
	
	private static final String DATATYPE_AUT = "AUT"; // everything is ok.
	private static final String DATATYPE_SOS = "SOS"; // emergency alert.
	private static final String DATATYPE_LPD = "LPD"; // low power alert.
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStrInterpreter#isDeviceCompliant(java.lang.String)
	 */
	@Override
	protected boolean isDeviceCompliant(String dataStr)
			throws InterpreterException {
		return 
			StringUtils.startsWith(dataStr, "#") && 
			StringUtils.endsWith(dataStr, "##");
	}
		
    /* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter#processWoDeviceSpecific(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected void processWoDeviceSpecific(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		
		// eleminate '#' at the beginning and at the end.
		dataStr = StringUtils.mid(dataStr, 1, dataStr.length() - 3);

		String[] parts = StringUtils.splitPreserveAllTokens(dataStr, '#');
		
		// sender id.
		String senderId = parts[IDX_IMEI];
		dataReceived.getSenderFromRequest().setSenderId(senderId);

		// username and password.
		String username = parts[IDX_USERNAME];
		String password = parts[IDX_PASSWORD];
		dataReceived.getSenderFromRequest().setAuthUsername(username);
		dataReceived.getSenderFromRequest().setAuthPlainPassword(password);
		
		// time recorded.
		String dateTime = parts[IDX_DATE] + StringUtils.left(parts[IDX_TIME], 6);
		dataReceived.getPosition().setTimeRecorded(
			CommonUtils.getDateTime(dateTime, "ddMMyyhhmmss"));

		// datatype.
		String messageStr = parts[IDX_DATATYPE];
		if (!StringUtils.isEmpty(messageStr)) {
			if (StringUtils.equals(messageStr, DATATYPE_SOS)) {
				dataReceived.getEmergencySignal().setActive(true);
				dataReceived.setDeviceActionExecutor(DeviceActionExecutor.EmergencyActivated);
				dataReceived.getSenderState().addState("SOS");
			} else if (StringUtils.equals(messageStr, DATATYPE_LPD)) { 
				dataReceived.getSenderState().addState("low power alert");
			} else  if (StringUtils.equals(messageStr, DATATYPE_AUT)) {
				dataReceived.getSenderState().addState("ok");
			} else {
				dataReceived.getSenderState().addState("unknown");
			}
		}
				
		// position.
		String position = parts[IDX_POSITION];
		String[] positionParts = StringUtils.splitPreserveAllTokens(position, ',');
		
		// longitude.
		Double longitude = CommonUtils.degree2decimal(
			positionParts[0], Direction.get(positionParts[1]), null);
		dataReceived.getPosition().setLongitudeInDecimal(longitude);
				
		// latitude.
		Double latitude = CommonUtils.degree2decimal(
			positionParts[2], Direction.get(positionParts[3]), null);
		dataReceived.getPosition().setLatitudeInDecimal(latitude);
		
		// speed.
		Double speedInMph = CommonUtils.string2double(positionParts[4], 0.0d);
		dataReceived.getPosition().setSpeedInKmPerHour(WebUtils.miles2kilometer(speedInMph));
		
		// direction.
		// Double direction = CommonUtils.string2double(positionParts[5], 0.0d);
		
		// valid flag.
		dataReceived.getPosition().setValid(
			StringUtils.equals(parts[IDX_STATUS], "1"));
	}
}
