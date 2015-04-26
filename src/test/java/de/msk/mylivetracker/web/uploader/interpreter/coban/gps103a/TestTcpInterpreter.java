package de.msk.mylivetracker.web.uploader.interpreter.coban.gps103a;

import java.util.TimeZone;

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
	
	private static DataReceivedVo interpret(String dataStr) {
		TcpInterpreter interpreter = new TcpInterpreter();
		DataReceivedVo dataReceived = DataReceivedVo.createInstance(null, RECEIVED);
		try {
			if (!interpreter.isDeviceCompliant(dataStr)) {
				dataReceived = null;
			} else {
				interpreter.process(dataReceived, dataStr, null);
			}
		} catch (Exception e) {
			System.out.println(e);
			dataReceived = null;
		}
		return dataReceived;
	}
	
	public void testValidPositionSent() throws Exception {
		String dataStr = 
			"imei:353451044755393,tracker,1111092255,,F,145548.000,A,5014.6008,N,00838.7714,E,7.14,,;";
		//String dataStr2 = 
		//	"imei:353451044755393,tracker,1111092255,,F,145548.000,A,5014.6008,N,00838.7714,E,7.14,0;";
		DataReceivedVo dataReceived = interpret(dataStr);
		
		checkSenderFromRequest(dataReceived, "353451044755393", null, null);
		System.out.println(dataReceived.getPosition().toString());
		checkPosition(dataReceived, RECEIVED,
			new DateTime("20111109145548", "yyyyMMddHHmmss", 
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC)),	
			50.24334666666667, 8.646189999999999, null, 7.14, true);		
	}				
}
