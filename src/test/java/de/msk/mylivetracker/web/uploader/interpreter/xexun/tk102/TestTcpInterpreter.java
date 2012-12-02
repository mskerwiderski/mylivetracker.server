package de.msk.mylivetracker.web.uploader.interpreter.xexun.tk102;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.interpreter.AbstractTestInterpreter;

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
public class TestTcpInterpreter extends AbstractTestInterpreter {
	
	private static final DateTime RECEIVED = new DateTime();
	
	@SuppressWarnings("unused")
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
		// todo
	}
		
	public void testInvalidPositionSent() throws Exception {
		// todo
	}	
	
}
