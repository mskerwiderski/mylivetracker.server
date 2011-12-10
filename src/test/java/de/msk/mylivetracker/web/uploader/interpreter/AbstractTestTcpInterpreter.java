package de.msk.mylivetracker.web.uploader.interpreter;

import junit.framework.TestCase;
import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.ClientInfoVo;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.EmergencySignalVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.SenderStateVo;
import de.msk.mylivetracker.web.uploader.processor.SenderFromRequestVo;

/**
 * AbstractTestInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-09-27
 * 
 */
public class AbstractTestTcpInterpreter extends TestCase {
	
	protected void checkSenderFromRequest(
		DataReceivedVo dataReceived, String expSenderId,
		String expAuthUsername, String expAuthPlainPassword) {
		assertNotNull(dataReceived);
		SenderFromRequestVo senderFromRequestVo = 
			dataReceived.getSenderFromRequest();
		assertNotNull(senderFromRequestVo);		
		assertEquals(expSenderId, 
			senderFromRequestVo.getSenderId());
		assertEquals(expAuthUsername, 
			senderFromRequestVo.getAuthUsername());
		assertEquals(expAuthPlainPassword, 
			senderFromRequestVo.getAuthPlainPassword());		
	}
		
	protected void checkPosition(DataReceivedVo dataReceived,
		DateTime expTimeReceived, DateTime expTimeRecorded,	
		Double expLatitude, Double expLongitude, 
		Double expAltitude, Double expSpeed,
		boolean isValid) {
		assertNotNull(dataReceived);
		PositionVo position = dataReceived.getPosition();
		assertNotNull(position);
		assertEquals(expTimeReceived.toString(), position.getTimeReceived().toString());
		assertEquals(expTimeRecorded.toString(), position.getTimeRecorded().toString());
		assertEquals(expLatitude, position.getLatitudeInDecimal());
		assertEquals(expLongitude, position.getLongitudeInDecimal());
		assertEquals(expAltitude, position.getAltitudeInMtr());
		assertEquals(expSpeed, position.getSpeedInKmPerHour());
		assertEquals(isValid, position.isValid());
	}
	
	protected void checkSenderState(DataReceivedVo dataReceived,
		DateTime expTimeReceived, String expSenderState) {
		assertNotNull(dataReceived);
		SenderStateVo senderState = dataReceived.getSenderState();
		assertNotNull(senderState);
		assertEquals(expTimeReceived.toString(), senderState.getTimeReceived().toString());
		assertEquals(expSenderState, senderState.getState());
	}
	
	protected void checkEmergencySignal(DataReceivedVo dataReceived,
		DateTime expTimeReceived, Boolean active) {
		assertNotNull(dataReceived);
		EmergencySignalVo emergencySignal = dataReceived.getEmergencySignal();
		assertNotNull(emergencySignal);
		assertEquals(expTimeReceived.toString(), emergencySignal.getTimeReceived().toString());
		assertEquals(active, emergencySignal.getActive());
	}
	
	protected void checkClientInfo(DataReceivedVo dataReceived,
		DateTime expTimeReceived, Double expMileage, 
		String expPhoneNumber, Long expRuntimeWOPauses,
		Long expRuntimeWPauses, String expTrackName) {
		assertNotNull(dataReceived);
		ClientInfoVo clientInfo = dataReceived.getClientInfo();
		assertNotNull(clientInfo);
		assertEquals(expTimeReceived.toString(), clientInfo.getTimeReceived().toString());
		assertEquals(expMileage, clientInfo.getMileageInMtr());
		assertEquals(expPhoneNumber, clientInfo.getPhoneNumber());
		assertEquals(expRuntimeWOPauses, clientInfo.getRuntimeWithoutPausesInMSecs());
		assertEquals(expRuntimeWPauses, clientInfo.getRuntimeWithPausesInMSecs());
		assertEquals(expTrackName, clientInfo.getTrackName());
	}
}
