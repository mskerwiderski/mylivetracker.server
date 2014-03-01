package de.msk.mylivetracker.web.uploader.interpreter.xexun.tk102.clone.xaiox;

import java.util.TimeZone;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.interpreter.AbstractTestInterpreter;
import de.msk.mylivetracker.web.uploader.interpreter.xexun.tk102.clone.xaiox.TcpInterpreter;

/**
 * TestTcpInterpreter (Tk102 Clone).
 * 
 * @author michael skerwiderski, (c)2014
 * 
 * @version 000
 * 
 * history
 * 000 initial 2014-03-01
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
	
	// #35312263748#13610001016#1#0000#AUT#1#36012#00648.9669,W,5311.2871,N,055.00,000#070114#074248##"
	// #35312263748#13610001016#1#0000#AUT#01#26200201c3dbef#951.500900,E,5355.149700,N,0.00,0.00#220214#131610.000##
	
	public void testValidPositionSent() throws Exception {
		String dataStr = 
			"#35312263748#13610001016#1#0000#AUT#1#36012#00648.9669,W,5311.2871,N,055.00,000#070114#074248.444##";
		DataReceivedVo dataReceived = interpret(dataStr);
		checkSenderFromRequest(dataReceived, "35312263748", "13610001016", "0000");
		checkPosition(dataReceived, 
			53.18811833333333, -6.816115, 
			RECEIVED, 
			new DateTime("20140107074248", "yyyyMMddHHmmss", 
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC)), 
			true);
	}
		
	public void testInvalidPositionSent() throws Exception {
		// todo
	}	
	
}
