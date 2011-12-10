package de.msk.mylivetracker.web.uploader.interpreter.aspicore.gsmtracker;
        
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutorAndMessage;
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
 * 000 initial 2011-08-11
 * 
 */          
public class TcpInterpreter extends AbstractDataStrWoDeviceSpecificInterpreter {

	//
	// IMEI 353785041163477
	// No GPS info
	// $GPRMC,125735.000,A,6010.34349,N,02445.72838,E,1.0,101.7,050509,6.9,W,A*1F
	// $GPGGA,125735.000,6010.34349,N,02445.72838,E,1,05,1.7,0.9,M,35.1,M,,*59
	// In Cell 273931 LAC 29120 Name elisa MCC 244 MNC 05 MODE 6 SSI 95
	// Label Test 32
	// *A70E8709
	//
		
	private static final String PATTERN_IMEI = "IMEI (\\d+)";
	private static final String PATTERN_NO_GPS_INFO = "No GPS info";
	private static final String PATTERN_IN_CELL_INFO = "In Cell (\\d+) LAC (\\d+) Name ([\\S ]*) MCC (\\d+) MNC (\\d+)( MODE (\\d+))?( SSI (\\d+))?";
	private static final String PATTERN_CUR_CELL_INFO = "CurCell (\\d+) LAC (\\d+) Name ([\\S ]*) MCC (\\d+) MNC (\\d+)( MODE (\\d+))?( SSI (\\d+))?";
	private static final String PATTERN_LABEL = "Label ([\\S ]*)";	
	private static final String DATA_SEP_CRLF = "\r\n";	
	private static final String DATA_STR_STARTS_WITH_IDENTIFIER = "IMEI ";

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStringInterpreter#isDeviceCompliant(java.lang.String)
	 */
	@Override
	protected boolean isDeviceCompliant(String dataStr)
		throws InterpreterException {	
		return StringUtils.startsWith(dataStr, DATA_STR_STARTS_WITH_IDENTIFIER);
	}	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter#processWoDeviceSpecific(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected void processWoDeviceSpecific(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		CommonUtils.checkCrc32Checksum(
			StringUtils.substringBeforeLast(dataStr, "*"),
			StringUtils.trim(StringUtils.substringAfterLast(dataStr, "*")));
		
		String[] dataArr = StringUtils.split(dataStr, DATA_SEP_CRLF);
		for (String dataLine : dataArr) {
			boolean found = false;
			if (!found) {
				String imei = RegExUtils.getDataItemIfRegExMatches(
					PATTERN_IMEI, dataLine, false);
				if (!StringUtils.isEmpty(imei)) {
					found = true;
					dataReceived.getSenderFromRequest().
						setSenderId(imei);					
				} 
			}
			if (!found) {
				if (StringUtils.equals(PATTERN_NO_GPS_INFO, dataLine)) {
					found = true;
					dataReceived.getPosition().setValid(false);
				} 
			}
			if (!found) {
				if (StringUtils.contains(dataLine, CommonUtils.GPRMC_IDENTIFIER)) {				
					found = true;
					CommonUtils.setPositionFromGprmcStr(dataReceived.getPosition(), 
						dataLine, true);					
				} 
			}
			if (!found) {
				if (StringUtils.contains(dataLine, CommonUtils.GPGGA_IDENTIFIER)) {				
					found = true;
					CommonUtils.setPositionFromGpggaStr(dataReceived.getPosition(), 
						dataLine, true);					
				} 
			}
			if (!found) {
				String[] mobNwItems = RegExUtils.getDataItemsIfRegExMatches(
					PATTERN_CUR_CELL_INFO, dataLine, false);
				if (mobNwItems == null) {
					mobNwItems = RegExUtils.getDataItemsIfRegExMatches(
						PATTERN_IN_CELL_INFO, dataLine, false);					
				}
				if (mobNwItems != null) {					
					found = true;
					dataReceived.getMobNwCell().setCellId(Integer.valueOf(mobNwItems[0]));
					dataReceived.getMobNwCell().setLocalAreaCode(Integer.valueOf(mobNwItems[1]));
					dataReceived.getMobNwCell().setNetworkName(mobNwItems[2]);
					dataReceived.getMobNwCell().setMobileCountryCode(mobNwItems[3]);
					dataReceived.getMobNwCell().setMobileNetworkCode(mobNwItems[4]);
					if (mobNwItems.length >= 6) {
						dataReceived.getMobNwCell().setMobileMode(Integer.valueOf(mobNwItems[6]));						
					}
					if (mobNwItems.length >= 8) {
						dataReceived.getSenderState().addState("ssi: " + mobNwItems[8]);						
					}
				} 
			}
			
			if (!found) {
				String label = RegExUtils.getDataItemIfRegExMatches(
					PATTERN_LABEL, dataLine, false);
				if (!StringUtils.isEmpty(label)) {
					found = true;
					DeviceActionExecutorAndMessage deviceActionExecutorAndMessage =
						de.msk.mylivetracker.web.uploader.interpreter.aspicore.gsmtracker.HttpInterpreter.
							getDeviceActionExecutorAndMessage(label);
					dataReceived.getMessage().setContent(deviceActionExecutorAndMessage.getMessage());
					dataReceived.setDeviceActionExecutor(deviceActionExecutorAndMessage.getDeviceActionExecutor());					
				} 
			}
		}	
	}	
}
