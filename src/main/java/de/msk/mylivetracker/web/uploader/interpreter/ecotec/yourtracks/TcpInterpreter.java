package de.msk.mylivetracker.web.uploader.interpreter.ecotec.yourtracks;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.Global;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.interpreter.ecotec.yourtracks.DeviceSpecific.RequestType;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.CommonUtils;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrInterpreter;

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
public class TcpInterpreter extends AbstractDataStrInterpreter {
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStrInterpreter#isDeviceCompliant(java.lang.String)
	 */
	@Override
	protected boolean isDeviceCompliant(String dataStr)
			throws InterpreterException {
		return 
			(StringUtils.startsWith(dataStr, PREFIX_LOGIN) ||
			 StringUtils.startsWith(dataStr, PREFIX_VERSION) ||		
			(StringUtils.startsWith(dataStr, PREFIX_POSITION) ||
			(StringUtils.startsWith(dataStr, PREFIX_VALUES))));
	}
		
	private static String PREFIX_LOGIN = "$FRLIN";	
		
	private static String PREFIX_VERSION = "$FRVER";
	private static String VERSION_RESPONSE_PREFIX = "$FRVER,1,1,MyLiveTracker ";
	
	public static final String PREFIX_FRPOS = "$FRPOS";
	private static final String PREFIX_FRVAL = "$FRVAL";
	
	private static String PREFIX_POSITION = "$FRWDT";
	private static String PREFIX_GPRMC = "$GPRMC";
    
       
    private static String PREFIX_VALUES = "$FRCMD";
    private static String VALUES_RESPONSE = "$FRRET\r\n";
    
    /* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected IDeviceSpecific process(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		DeviceSpecific deviceSpecific = null;
		
		dataStr = Utils.cleanStr(dataStr);
		
		if (StringUtils.startsWith(dataStr, PREFIX_LOGIN)) {
			deviceSpecific = new DeviceSpecific(RequestType.Login);
			Utils.interpretFrlin(PREFIX_LOGIN, dataReceived, dataStr);		
		} else if (StringUtils.startsWith(dataStr, PREFIX_POSITION)) {
			deviceSpecific = new DeviceSpecific(RequestType.Position);
			Utils.interpretFrwdt(PREFIX_POSITION, PREFIX_GPRMC, dataReceived, dataStr);
		} else if (StringUtils.startsWith(dataStr, PREFIX_GPRMC)) {
			deviceSpecific = new DeviceSpecific(RequestType.Position);
			CommonUtils.setPositionFromGprmcStr(dataReceived.getPosition(), dataStr, true);
		} else if (StringUtils.startsWith(dataStr, PREFIX_VALUES)) {
			deviceSpecific = new DeviceSpecific(RequestType.Position);
			String[] records = StringUtils.split(dataStr, "$");
			for (String record : records) {
				record = "$" + record;
				if (StringUtils.startsWith(record, PREFIX_FRPOS)) {
					Utils.interpretFrpos(PREFIX_FRPOS, dataReceived, record);
				}
				if (StringUtils.startsWith(record, PREFIX_FRVAL)) {
					Utils.interpretFrval(PREFIX_FRVAL, dataReceived, record);
				}
			}
		} else if (StringUtils.startsWith(dataStr, PREFIX_VERSION)) {
			deviceSpecific = new DeviceSpecific(RequestType.Version);			
		}
		
		return deviceSpecific;
	}	
		
	private static String LOGIN_SUCCESSFUL = "$FRSES,100*4C\r\n";
	private static String LOGIN_ERROR = "$FRERR,AuthErr,sender is not authorized*7F\r\n";
	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStrInterpreter#postProcess(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific)
	 */
	@Override
	public String postProcess(DataReceivedVo dataReceived,
		IDeviceSpecific deviceSpecific) throws InterpreterException {
		RequestType requestType = ((DeviceSpecific)deviceSpecific).getRequestType();
		String responseStr = null;
		if (requestType.equals(RequestType.Login)) {
			if (dataReceived.getSenderFromRequest().isAuthorized()) {
				responseStr = LOGIN_SUCCESSFUL;
			} else {
				responseStr = LOGIN_ERROR;
			}			
		} else if (requestType.equals(RequestType.Version)) {
			responseStr = VERSION_RESPONSE_PREFIX + Global.getVersion() + "*";
			responseStr = responseStr + 
				CommonUtils.calcHexChecksum(StringUtils.substringBetween(responseStr, "$", "*"), 2);			
		} else if (requestType.equals(RequestType.Position)) {
			responseStr = VALUES_RESPONSE;
		}
		return responseStr;
	}
	
	
}