package de.msk.mylivetracker.web.uploader.interpreter.xexun.tk102.clone.beijing;

import java.util.TimeZone;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.interpreter.AbstractTestInterpreter;
import de.msk.mylivetracker.web.uploader.interpreter.xexun.tk102.clone.beijing.TcpInterpreter;

/**
 * TestTcpInterpreter (Tk102 Clone).
 * 
 * @author michael skerwiderski, (c)2014
 * 
 * @version 000
 * 
 * history
 * 000 initial 2014-02-10
 * 
 */
public class TestTcpInterpreter extends AbstractTestInterpreter {
	
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
	
	public void testValidPositionSent() throws Exception {
		String dataStr = 
				"(027042205000BR00140203A5008.1142N00830.2298E000.71459010.000000000000L00000000)";
		DataReceivedVo dataReceived = interpret(dataStr);
		checkSenderFromRequest(dataReceived, "027042205000", null, null);
		checkPosition(dataReceived, 
			50.135236666666664, 8.50383, 
			RECEIVED, 
			new DateTime("20140203145901", "yyyyMMddHHmmss", 
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC)), 
			true);
	}
		
	public void testInvalidPositionSent() throws Exception {
		// todo
	}	
	
}
