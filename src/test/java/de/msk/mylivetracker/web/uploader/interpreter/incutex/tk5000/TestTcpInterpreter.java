package de.msk.mylivetracker.web.uploader.interpreter.incutex.tk5000;

import java.util.TimeZone;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.interpreter.AbstractTestTcpInterpreter;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * TestTcpInterpreter (Tk5000).
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-09-15
 * 
 */
public class TestTcpInterpreter extends AbstractTestTcpInterpreter {
	
	private static final DateTime RECEIVED = new DateTime();
	
	private static DataReceivedVo interpret(String dataStr) {
		TcpInterpreter interpreter = new TcpInterpreter();
		DataReceivedVo dataReceived = DataReceivedVo.createInstance(null, RECEIVED);
		try {
			if (!interpreter.isDeviceCompliant(dataStr)) {
				dataReceived = null;
			} else {
				interpreter.processWoDeviceSpecific(dataReceived, dataStr, null);
			}
		} catch (Exception e) {
			System.out.println(e);
			dataReceived = null;
		}
		return dataReceived;
	}
	
	public void testInvalidDataStrSent() {
		assertNull(interpret(""));
		assertNull(interpret("T160119682,20110914083224,11.61514768,48.1365126,8.25,344.1,575.299,7,2"));
		assertNull(interpret("160119682,20110914083224,11.61514768,48.1365126,8.25,344.1,575.299,7"));
		assertNull(interpret("160119682,20110914083224,11.61514768,48.1365126,8.25,344.1,575.299,7,2,200,0"));
		assertNull(interpret(
			"160119682,20110914083224,11.61514768,48.1365126,8.25,344.1,575.2999877929688,7,2,120.3" +
			"160119682,20110914083224,11.61514768,48.1365126,8.25,344.1,575.2999877929688,7,2,120.3"));		
	}
	
	public void testValidPositionWithoutMileageSent() throws Exception {
		String dataStr = 
			"160119682,20110914083224,11.61514768,48.1365126,8.25,344.1,575.2999877929688,7,2";
		DataReceivedVo dataReceived = interpret(dataStr);
		checkSenderFromRequest(dataReceived, "160119682", null, null);
		checkPosition(dataReceived, RECEIVED,
			new DateTime("20110914083224", "yyyyMMddHHmmss", 
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC)),	
			48.1365126, 11.61514768, 575.2999877929688, 8.25, true);
		checkSenderState(dataReceived, RECEIVED, null);
		checkEmergencySignal(dataReceived, RECEIVED, null);
		checkClientInfo(dataReceived, RECEIVED, null, null, null, null, null);
	}
	
	public void testValidPositionWithMileageSent() throws Exception {
		String dataStr = 
			"160119682,20110914083224,11.61514768,48.1365126,8.25,344.1,575.2999877929688,7,2,120.3";
		DataReceivedVo dataReceived = interpret(dataStr);
		checkSenderFromRequest(dataReceived, "160119682", null, null);
		checkPosition(dataReceived, RECEIVED,
			new DateTime("20110914083224", "yyyyMMddHHmmss", 
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC)),	
			48.1365126, 11.61514768, 575.2999877929688, 8.25, true);
		checkSenderState(dataReceived, RECEIVED, "Mileage: 120.30 mls");
		checkEmergencySignal(dataReceived, RECEIVED, null);
		checkClientInfo(dataReceived, RECEIVED, WebUtils.miles2meter(120.3), null, null, null, null);
	}
	
	public void testInvalidPositionSent() throws Exception {
		String dataStr = 
			"160119682,20110914083224,11.61514768,48.1365126,8.25,344.1,575.2999877929688,3,2";
		DataReceivedVo dataReceived = interpret(dataStr);
		checkSenderFromRequest(dataReceived, "160119682", null, null);
		checkPosition(dataReceived, RECEIVED,
			new DateTime("20110914083224", "yyyyMMddHHmmss", 
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC)),	
			48.1365126, 11.61514768, 575.2999877929688, 8.25, false);
		checkSenderState(dataReceived, RECEIVED, null);
		checkEmergencySignal(dataReceived, RECEIVED, null);
		checkClientInfo(dataReceived, RECEIVED, null, null, null, null, null);
	}
	
	public void testEmergencySignalSent() throws Exception {
		String dataStr = 
			"160119682,20110914083224,11.61514768,48.1365126,8.25,344.1,575.2999877929688,7,4";
		DataReceivedVo dataReceived = interpret(dataStr);
		checkSenderFromRequest(dataReceived, "160119682", null, null);
		checkPosition(dataReceived, RECEIVED,
			new DateTime("20110914083224", "yyyyMMddHHmmss", 
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC)),	
			48.1365126, 11.61514768, 575.2999877929688, 8.25, true);
		checkSenderState(dataReceived, RECEIVED, null);
		checkEmergencySignal(dataReceived, RECEIVED, Boolean.TRUE);
		checkClientInfo(dataReceived, RECEIVED, null, null, null, null, null);
	}
	
	public void testLowBatterySignalSent() throws Exception {
		String dataStr = 
			"160119682,20110914083224,11.61514768,48.1365126,8.25,344.1,575.2999877929688,7,40";
		DataReceivedVo dataReceived = interpret(dataStr);
		checkSenderFromRequest(dataReceived, "160119682", null, null);
		checkPosition(dataReceived, RECEIVED,
			new DateTime("20110914083224", "yyyyMMddHHmmss", 
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC)),	
			48.1365126, 11.61514768, 575.2999877929688, 8.25, true);
		checkSenderState(dataReceived, RECEIVED, TcpInterpreter.MSG_LOW_BATTERY);
		checkEmergencySignal(dataReceived, RECEIVED, null);
		checkClientInfo(dataReceived, RECEIVED, null, null, null, null, null);
	}
	
	
}
