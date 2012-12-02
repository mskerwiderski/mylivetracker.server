package de.msk.mylivetracker.web.uploader.interpreter.msk.mylivetracker;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.interpreter.AbstractTestInterpreter;
import de.msk.mylivetracker.web.uploader.interpreter.TestUtils;

/**
 * TestHttpInterpreter (MyLiveTracker HTTP protocol).
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-11-24
 * 
 */
public class TestHttpInterpreter extends AbstractTestInterpreter {
	
	private static final DateTime RECEIVED = new DateTime();
	
	private static DataReceivedVo interpret(String httpUrl) {
		if (StringUtils.isEmpty(httpUrl)) {
			throw new IllegalArgumentException("httpUrl must not be null");
		}
		Map<String, String> parameterValues = TestUtils.getParameterValues(httpUrl);
		HttpInterpreter interpreter = new HttpInterpreter();
		DataReceivedVo dataReceived = DataReceivedVo.createInstance(null, RECEIVED);
		try {
			interpreter.process(dataReceived, parameterValues);
		} catch (Exception e) {
			System.out.println(e);
			dataReceived = null;
		}
		return dataReceived;
	}	
	
	public void testValidPositionSent() throws Exception {
		DataReceivedVo dataReceived = interpret("http://portal.mylivetracker.de/upl_mlt.sec?" +
			"pow=96&pht=GSM&tid=9d20c028-7cf0-47bb-80cd-6239a90a85c2&spd=0.0&lon=11.61550577&" +
			"did=358510043733001&sat=10&trn=MyTrack&nwt=HSPA&tst=241112144451021&usr=msk&" +
			"mil=37251.33&pwd=bc3b204bb67f39cd97bf279b538b199e&mcc=262&vco=150&dst=8043.284&" +
			"rtm=1320023&rtp=11533085&cid=17353&vna=1.5.0 alpha&val=true&lac=5903740&vol=4.17&" +
			"mnn=Telekom.de&alt=585.7000122070313&mnc=01&lat=48.10487616&acc=9.0");
		System.out.println(dataReceived.toString());
	}
		
	public void testInvalidPositionSent() throws Exception {
		// todo
	}	
}
