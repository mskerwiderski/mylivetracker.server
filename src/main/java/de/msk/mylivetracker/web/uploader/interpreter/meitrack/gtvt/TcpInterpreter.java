package de.msk.mylivetracker.web.uploader.interpreter.meitrack.gtvt;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;
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
 * 000 initial 2011-08-11
 * 
 */
public class TcpInterpreter extends AbstractDataStrWoDeviceSpecificInterpreter {	
	
	private static final Log log = LogFactory.getLog(TcpInterpreter.class);
	
	// $$<L (2 bytes)><ID (7 bytes)><command (2 bytes)>[<alarm info>]<data><checksum (2 bytes)>\r\n
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStrInterpreter#isDeviceCompliant(java.lang.String)
	 */
	@Override
	protected boolean isDeviceCompliant(String dataStr)
			throws InterpreterException {
		return StringUtils.startsWith(dataStr, "$$");
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrWoDeviceSpecificInterpreter#processWoDeviceSpecific(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected void processWoDeviceSpecific(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		byte[] dataBytes = dataStr.getBytes();
		
		dataReceived.getSenderFromRequest().setSenderId(getSenderId(dataStr));
		
		int expectedPacketLen = getPacketLen(dataStr);
		CommonUtils.checkDataStrLength(dataStr, expectedPacketLen);
		
		// checksum computing seems to be buggy - on the trackers side?
//		String expectedChecksumInHex = toHexString(StringUtils.left(
//			StringUtils.right(data.getDataStr(), 4), 2));
//		if (CommonUtils.isCrc16ChecksumValid(
//			StringUtils.substring(data.getDataStr(), 0, -4),	
//			expectedChecksumInHex)) {
//			throw new InterpreterException("data string hex checksum is invalid.");
//		}
			
		Command command = Command.getCommand(dataBytes);
		if (command != null) {			
			// HDOP | Altitude | State | AD1,AD2
			String[] additionalDataStr =
				StringUtils.splitPreserveAllTokens(
					StringUtils.substring(
						StringUtils.substringAfter(dataStr, "|"), 
					0, -4), "|");		
	
			int dataOffset = 13; // if position report
			if (command.equals(Command.Alarm)) {
				ButtonPressed buttonPressed = ButtonPressed.getButtonPressed(dataBytes);
				if (buttonPressed != null) {
					if (buttonPressed.equals(ButtonPressed.ButtonSosPressed)) {
						dataReceived.setDeviceActionExecutor(DeviceActionExecutor.EmergencyActivated);	
					} else if (buttonPressed.equals(ButtonPressed.ButtonBPressed)) {
						dataReceived.setDeviceActionExecutor(DeviceActionExecutor.ResetTrack);
					} else if (buttonPressed.equals(ButtonPressed.ButtonCPressed)) {
						dataReceived.setDeviceActionExecutor(DeviceActionExecutor.CreateTrack);
					} else {
						dataReceived.getMessage().setContent(
							buttonPressed.getLabel());
					}
				}
				dataOffset = 14;
			}
			
			if (hasGpsFix(additionalDataStr)) {	
				String gprmcStr = "$GPRMC," +
					StringUtils.substring(
						StringUtils.substringBefore(dataStr, "|"), 
						dataOffset);		
				CommonUtils.setPositionFromGprmcStr(
					dataReceived.getPosition(), gprmcStr, true);
				if (additionalDataStr.length >= 2) {
					dataReceived.getPosition().setAltitudeInMtr(
						CommonUtils.string2double(
							additionalDataStr[1], null));
				}										
			}
		}
	}
			
	private static int getPacketLen(String dataStr) {
		String lenStr = "";
		for (int i=2; i <= 3; i++) {
			lenStr += Integer.toHexString(dataStr.charAt(i));			
		}
		return Integer.parseInt(lenStr, 16);				
	}
	
	private static String getSenderId(String dataStr) {
		String idStr = "";
		for (int i=4; i <= 10; i++) {
			idStr += StringUtils.leftPad(
				Integer.toHexString(dataStr.charAt(i)), 2, '0');
		}
		idStr = StringUtils.substringBefore(idStr, "f");
		log.debug("senderId=" + idStr);
		return idStr;				
	}	
	
	private static enum ButtonPressed {
		ButtonSosPressed("SOS button was pressed (Input 1 is active)!", "01"),
		ButtonBPressed("Button B was pressed (Input 2 is active)!", "02"),
		ButtonCPressed("Button C was pressed (Input 3 is active)!", "03"),
		BatteryLow("Low Battery Alarm!", "10"),
		Speeding("Speeding Alarm!", "11"),
		Movement("Movement Alarm!", "12"),
		Geofence("Geofence Alarm!", "13"),
		EnteringGpsBlindArea("Entering GPS blind area!", "15"),
		ExitingGpsBlindArea("Exiting GPS blind area!", "16"),
		ExternalPowerCut("External Power cut Alaam!", "50");
		private static Map<String, ButtonPressed> buttonPressedMap;
		private static Map<String, ButtonPressed> getButtonPressedMap() {
			if (buttonPressedMap == null) {
				buttonPressedMap = new HashMap<String, ButtonPressed>();
			}
			return buttonPressedMap;
		}
		private String label;
		private ButtonPressed(String label, String alarmStr) {
			this.label = label;
			getButtonPressedMap().put(alarmStr, this);
		}
		public String getLabel() {
			return label;
		}	
		public static ButtonPressed getButtonPressed(String alarmStr) {
			return getButtonPressedMap().get(alarmStr);
		}
		public static ButtonPressed getButtonPressed(byte[] dataBytes) {
			String str = StringUtils.leftPad( 
				Integer.toHexString(dataBytes[13]), 2, '0');						
			return getButtonPressed(str);
		}			
	}
	
	private static enum Command {		
		PositionReport("9955"),
		Alarm("9999");		
		private static Map<String, Command> commandMap;
		private static Map<String, Command> getCommandMap() {
			if (commandMap == null) {
				commandMap = new HashMap<String, Command>();
			}
			return commandMap;
		}
		private Command(String cmdStr) {
			getCommandMap().put(cmdStr, this);
		}			
		public static Command getCommand(String cmdStr) {
			return getCommandMap().get(cmdStr);
		}
		public static Command getCommand(byte[] dataBytes) {
			String str = "99"; // bugfix, because tracker sends sometime '9D' instead.
			for (int i=12; i <= 12; i++) {
				str += StringUtils.right( 
					Integer.toHexString(dataBytes[i]), 2);			
			}
			return getCommand(str);
		}		
	}	
	
	private static boolean hasGpsFix(String[] additionalDataStr) {
		boolean res = true;
		if ((additionalDataStr.length >= 1) &&
			StringUtils.isEmpty(additionalDataStr[0])) {
			// hdop is blank when the tracker has no GPS fix
			//res = false;
			res = true; // hdop check skipped.
		}
		return res;
	}
	
		
}
