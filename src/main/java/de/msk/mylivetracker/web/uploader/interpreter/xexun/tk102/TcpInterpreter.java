package de.msk.mylivetracker.web.uploader.interpreter.xexun.tk102;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.RegExUtils;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter;

/**
 * TcpInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 001 2012-11-02 support for itakka-firmware added.
 * 000 2011-08-11 initial.
 * 
 */
public class TcpInterpreter extends AbstractDataStrWoDeviceSpecificInterpreter {

	//
	// 00: 100206141942			: locale date and time (yyMMddHHmmss).
	// 01: +4917666694616		: authorized mobile number.
	// 02: GPRMC				: GPRMC indicator.
	// 03: 131942.000			: utc time (hhmmss.ms).
	// 04: A					: data string valid indicator (A, V).
	// 05: 4806.3037			: latitude in degree.
	// 06: N					: latitude indicator.
	// 07: 01137.0012			: longitude in degree.
	// 08: E					: longitude indicator.
	// 09: 2.01					: speed in km/h.
	// 10: 326.60				: course (DEG).
	// 11: 060210				: date (ddMMyy).
	// 12: 						: unknown.
	// 13:                      : unknown.
	// 14: A*6F					: checksum.
	// 15: F					: gps signal indicator (F=good, L=bad).
	// 16: help me				: optional: message (help me, low battery, ...).
	// 17: imei:354776036839170 : imei.
	// 18: 06					: optional: number of satellites.
	// 19: 530.7				: optional: altitude in meter.
	// 20: F:3.79V				: optional: battery state (F=full/L=empty) and battery voltage in volt.
	// 21: 0					: optional: akku loading.
	// 22: 142					: number of bytes.	
	// 23: 33729				: optional: gsm id.	
	// 24: 262					: optional: mobile country code.
	// 25: 01					: optional: mobile network code.
	// 26: 8707					: optional: local area code (as hex value).
	// 27: 27B7					: optional: cell id (as hex value).
	// 28: itakka_fw492         : optional: firmware info.
	//
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStrInterpreter#isDeviceCompliant(java.lang.String)
	 */
	@Override
	protected boolean isDeviceCompliant(String dataStr)
			throws InterpreterException {
		return 
			(StringUtils.contains(dataStr, ",imei:") || 
			StringUtils.contains(dataStr, ", imei:")) &&
			StringUtils.contains(dataStr, "GPRMC");
	}
		
	private static final String PATTERN_IMEI = "imei:(\\d*)";
	private static final String PATTERN_LOCATION_VALID_FLAG = "\\*.{2},([FL]),"; 
	private static final String PATTERN_PHONE_NUMBER = "\\d*,(.*),GPRMC";
	private static final String PATTERN_MESSAGE = "\\*.{2},[FL],((.*),)?\\s?imei:";
	private static final String PATTERN_BATTERY = "([FL]):([\\d\\.]*)V";
	private static final String PATTERN_OPTIONAL_PARAMS = "imei:\\d*,(.*)\\r?\\n?";
		
    /* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter#processWoDeviceSpecific(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected void processWoDeviceSpecific(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		
		// sender id
		dataReceived.getSenderFromRequest().setSenderId(
			RegExUtils.getDataItemIfRegExFind(
				PATTERN_IMEI, dataStr, true));
				
		// username, password.
		dataReceived.getSenderFromRequest().setAuthUsername(
			dataReceived.getSenderFromRequest().getSenderId());
		String phoneNumber = RegExUtils.getDataItemIfRegExFind(
			PATTERN_PHONE_NUMBER, dataStr, false);
		dataReceived.getSenderFromRequest().
			setAuthPlainPassword(phoneNumber);
			
		dataReceived.getClientInfo().setPhoneNumber(phoneNumber);
		
		// gprmc string.
		CommonUtils.setPositionFromGprmcStrInDataStr(dataReceived.getPosition(), dataStr, true);

		// gps signal indicator.
		boolean locValid = StringUtils.equals(
			RegExUtils.getDataItemIfRegExFind(PATTERN_LOCATION_VALID_FLAG, dataStr, true), "F");		
		dataReceived.getPosition().setValid(locValid);
		
		// sos.
		String messageStr = 
			RegExUtils.getDataItemsIfRegExFind(PATTERN_MESSAGE, dataStr, true)[1];
		if (!StringUtils.isEmpty(messageStr)) {
			if (StringUtils.contains(messageStr, "help me")) {
				dataReceived.getEmergencySignal().setActive(true);
				dataReceived.setDeviceActionExecutor(DeviceActionExecutor.EmergencyActivated);
				dataReceived.getSenderState().addState("help me");
			} else if (StringUtils.contains(messageStr, "low battery")) { 
				dataReceived.getSenderState().addState("low battery");
			}
		}
		
		// battery (optional!).
		String[] batteryItems = 
			RegExUtils.getDataItemsIfRegExFind(PATTERN_BATTERY, dataStr, false);
		if ((batteryItems != null) && (batteryItems.length == 2)) {
			if (!StringUtils.equals(batteryItems[0], "F")) {
				dataReceived.getSenderState().addState("battery voltage state NOT ok.");
			}
			dataReceived.getSenderState().addState("battery voltage = " + batteryItems[1]);			
		}
		
		// number of satellites, altitude, mobile network, firmware infos.
		String optionalParams = 
			RegExUtils.getDataItemIfRegExFind(PATTERN_OPTIONAL_PARAMS, dataStr, false);
		String[] optionalItems = StringUtils.splitPreserveAllTokens(optionalParams, ",");
		if (optionalItems.length == 1) {
			// only altitude is avaiable
			dataReceived.getPosition().setAltitudeInMtr(
				CommonUtils.string2double(optionalItems[0], null));				
		} else if (optionalItems.length >= 10) {
			// number of satellites.
			Integer numberOfSatellites = CommonUtils.string2int(optionalItems[0], null);
			if (numberOfSatellites != null) {
				dataReceived.getSenderState().addState(
					"satellites=" + numberOfSatellites);
			}
			// altitude.
			dataReceived.getPosition().setAltitudeInMtr(
				CommonUtils.string2double(optionalItems[1], null));
			// mobile network cell.
			dataReceived.getMobNwCell().setMobileCountryCode(optionalItems[6]);
			dataReceived.getMobNwCell().setMobileNetworkCode(optionalItems[7]);
			String localAreaCodeStr = optionalItems[8];
			if (!StringUtils.isEmpty(localAreaCodeStr)) {
				dataReceived.getMobNwCell().setLocalAreaCode(
					Integer.parseInt(localAreaCodeStr, 16));	
			}			
			String cellIdStr = optionalItems[9];
			if (!StringUtils.isEmpty(cellIdStr)) {
				dataReceived.getMobNwCell().setCellId(
					Integer.parseInt(cellIdStr, 16));
			}
			// firmware info.
			if (optionalItems.length == 11) {
				String firmwareInfoStr = optionalItems[10];
				if (!StringUtils.isEmpty(firmwareInfoStr)) {
					dataReceived.getSenderState().addState(
						"firmware=" + firmwareInfoStr);
				}
			}
		}
	}
}
