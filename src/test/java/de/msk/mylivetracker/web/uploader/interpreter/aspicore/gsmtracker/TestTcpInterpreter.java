package de.msk.mylivetracker.web.uploader.interpreter.aspicore.gsmtracker;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.interpreter.AbstractTestTcpInterpreter;

/**
 * TestTcpInterpreter (GSMTracker).
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-09-27
 * 
 */
public class TestTcpInterpreter extends AbstractTestTcpInterpreter {
	
	protected static final DateTime RECEIVED = new DateTime();
	
	protected static DataReceivedVo interpret(String dataStr) {
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
	}
	
	public void testValidPositionSent() throws Exception {
		
	}
	
	
	public void testInvalidPositionSent() throws Exception {
		String dataStr = 				  
			"IMEI 353094027101381\r\n$GPRMC,,V,,,,,,,,,,N*53\r\nCurCell 10167 LAC 17249 Name T-Mobile D MCC 262 MNC 01 MODE 2 SSI 82\r\n*25C7F5C1";
		DataReceivedVo dataReceived = interpret(dataStr);
		checkSenderFromRequest(dataReceived, "353094027101381", null, null);
		
	}
	
	public void testEmergencySignalSent() throws Exception {
		
	}		
}
