package de.msk.mylivetracker.web.uploader.interpreter.coban.gps103a;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter;

/**
 * TcpInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-11-11
 * 
 */
public class TcpInterpreter extends AbstractDataStrWoDeviceSpecificInterpreter {

	//
	// two types of data strings are possible:
	// o imei:353451044755393,tracker,1111170401,,F,200154.000,A,5014.6126,N,00838.7954,E,0.00,,;
	// o imei:359710040951866,tracker,1306210838,+491633792586,F,083801.000,A,4902.5013,N,01121.2885,E,0.00,0;
	// o imei:359587010124900,tracker,0809231929,13554900601,F,112909.397,A,2234.4669,N,11354.3287,E,0.11,; 
	// o ##,imei:353451044755393,A;
	//
	// 00: imei:353451044755393	: imei.
	// 01: tracker				: tracker name.
	// 02: 1111092257			: date time (yymmddhhmm)
	// 03: ? 					: ?
	// 04: F					: gps signal indicator (F=good, L=bad).
	// 05: 145709.000			: time (utc)
	// 06: A                    : data string valid indicator (A, V)
	// 07: 5014.6043			: latitude in degree.
	// 08: N					: latitude indicator.
	// 09: 00838.7944			: longitude in degree.
	// 10: E					: longitude indicator.
	// 11: 0.64					: speed in km/h.
	// 12: 64.91				: course (DEG).
	// 13: ?					: optional: ?
	// 14: ;					: end of data string.
	//
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStrInterpreter#isDeviceCompliant(java.lang.String)
	 */
	@Override
	protected boolean isDeviceCompliant(String dataStr)
			throws InterpreterException {
		return 
			(StringUtils.startsWith(dataStr, "imei:") ||
			 StringUtils.startsWith(dataStr, "##,imei:")) &&
			StringUtils.endsWith(dataStr, ";");
	}		
		
    /* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter#processWoDeviceSpecific(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected void processWoDeviceSpecific(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		
		String[] dataItems = 
			StringUtils.splitPreserveAllTokens(dataStr, ",");
		
		if ((dataItems.length != 13) && (dataItems.length != 14) && (dataItems.length != 3)) {
			throw new InterpreterException("invalid count of data items: " + dataStr);
		}				
				
		if (dataItems.length == 3) {
			// sender id
			String senderId = StringUtils.substringAfter(dataItems[1], ":");
			if (StringUtils.isEmpty(senderId)) {
				throw new InterpreterException("senderId is empty: " + dataItems[0]);
			}
			dataReceived.getSenderFromRequest().setSenderId(senderId);
			dataReceived.getPosition().setValid(false);
		} else {
			// sender id
			String senderId = StringUtils.substringAfter(dataItems[0], ":");
			if (StringUtils.isEmpty(senderId)) {
				throw new InterpreterException("senderId is empty: " + dataItems[0]);
			}
			dataReceived.getSenderFromRequest().setSenderId(senderId);
			// gprmc string.
			String gprmcStr = "GPRMC";
			for (int i=5; i <=12; i++) {
				gprmcStr += "," + dataItems[i];
			}
			String dateStr = dataItems[2];
			dateStr = 
				StringUtils.substring(dateStr, 4, 6) +
				StringUtils.substring(dateStr, 2, 4) +
				StringUtils.substring(dateStr, 0, 2);
			gprmcStr += "," + dateStr + ",,,A";
			gprmcStr += "*" + CommonUtils.calcHexChecksum(gprmcStr, 2);
			CommonUtils.setPositionFromGprmcStrInDataStr(dataReceived.getPosition(), gprmcStr, true);
	
			// gps signal indicator.
			boolean locValid = StringUtils.equals(dataItems[4], "F");		
			dataReceived.getPosition().setValid(locValid);
		}
	}
}
