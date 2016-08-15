package de.msk.mylivetracker.web.uploader.interpreter.sarmini;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;

/**
 * Utils.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public final class Utils {

	public static String cleanStr(String dataStr) {
		dataStr = StringUtils.remove(dataStr, "\r");
		dataStr = StringUtils.remove(dataStr, "\n");
		dataStr = StringUtils.remove(dataStr, ";");
		return dataStr;
	}
	
	public static void interpretLogin(
		DataReceivedVo dataReceived, String loginStr) 
		throws InterpreterException {
		if (dataReceived == null) {
			throw new IllegalArgumentException("dataReceived must not be null.");
		}
		if (StringUtils.isEmpty(loginStr)) {
			throw new IllegalArgumentException("loginStr must not be empty.");
		}
		dataReceived.getSenderFromRequest().setSender(null);
		String[] parts = StringUtils.splitPreserveAllTokens(loginStr, ",");
		if (parts.length != 2) { 
			throw new InterpreterException("login data string is invalid: found " +
				parts.length + " field values, but expected 2 field values.");				
		}
		dataReceived.getSenderFromRequest().setSenderId(parts[1]);
	}
	
	public static void interpretPositionA(
		DataReceivedVo dataReceived, String positionStr) 
		throws InterpreterException {
		if (dataReceived == null) {
			throw new IllegalArgumentException("dataReceived must not be null.");
		}
		if (StringUtils.isEmpty(positionStr)) {
			throw new IllegalArgumentException("positionStr must not be empty.");
		}
		String[] parts = StringUtils.splitPreserveAllTokens(positionStr, ",");
		if (parts.length != 8) {
			throw new InterpreterException("position data string is invalid: found " +
				parts.length + " field values, but expected 8 field values.");				
		}
		// !A,01/12/10,13:25:35,22.641724,114.023666,000.1,281.6,0
		dataReceived.getPosition().setTimeRecorded(
			CommonUtils.getDateTime(
				StringUtils.join(new String[] { parts[1], ",", parts[2] }), 
				"dd/MM/yy,hh:mm:ss"));
		dataReceived.getPosition().setLatitudeInDecimal(
			CommonUtils.string2double(parts[3], null));								
		dataReceived.getPosition().setLongitudeInDecimal(
			CommonUtils.string2double(parts[4], null));												
		dataReceived.getPosition().setSpeedInKmPerHour(
			CommonUtils.string2double(parts[5], null));
		if (StringUtils.equals(parts[6], "14")) {
			dataReceived.getEmergencySignal().setActive(true);
			dataReceived.setDeviceActionExecutor(DeviceActionExecutor.EmergencyActivated);
		}
	}
	
	public static void interpretPositionD(
		DataReceivedVo dataReceived, String positionStr) 
		throws InterpreterException {
		if (dataReceived == null) {
			throw new IllegalArgumentException("dataReceived must not be null.");
		}
		if (StringUtils.isEmpty(positionStr)) {
			throw new IllegalArgumentException("positionStr must not be empty.");
		}
		String[] parts = StringUtils.splitPreserveAllTokens(positionStr, ",");
		if (parts.length != 13) {
			throw new InterpreterException("position data string is invalid: found " +
				parts.length + " field values, but expected 13 field values.");				
		}
		// !D,24/06/16,10:24:13,53.108624,8.889404,2,384,0e0000,-0.3,95,5,13,0
		dataReceived.getPosition().setTimeRecorded(
			CommonUtils.getDateTime(
				StringUtils.join(new String[] { parts[1], ",", parts[2] }), 
				"dd/MM/yy,hh:mm:ss"));
		dataReceived.getPosition().setLatitudeInDecimal(
			CommonUtils.string2double(parts[3], null));								
		dataReceived.getPosition().setLongitudeInDecimal(
			CommonUtils.string2double(parts[4], null));												
	}
}
