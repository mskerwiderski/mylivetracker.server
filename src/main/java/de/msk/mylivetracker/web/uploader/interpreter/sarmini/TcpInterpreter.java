package de.msk.mylivetracker.web.uploader.interpreter.sarmini;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.interpreter.sarmini.DeviceSpecific.RequestType;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrInterpreter;

/**
 * TcpInterpreter.
 * 
 * @author michael skerwiderski, (c)2016
 * 
 * @version 000
 * 
 * history
 * 000 2016-06-22 initial.
 * 
 */
public class TcpInterpreter extends AbstractDataStrInterpreter {

	//
	// !A,date    ,time    ,lat      ,lot       ,speed,degrees,flag;
	// !A,01/12/10,13:25:35,22.641724,114.023666,000.1,281.6  ,0;
	//
	// !D,date    ,time    ,lat      ,lot     ,???  ,???    ,???   ,??? ,???,???,???,???;
	// !D,25/06/16,13:11:14,53.108768,8.888988,0    ,260    ,100000,70.4,100,4  ,13 ,0;
	//

	private static String PREFIX_LOGIN = "!1";	
	private static String PREFIX_POSITION_A = "!A";
	private static String PREFIX_POSITION_D = "!D";
	
	@Override
	protected boolean isDeviceCompliant(String dataStr)
			throws InterpreterException {
		return 
			(StringUtils.startsWith(dataStr, PREFIX_LOGIN) ||
			 StringUtils.startsWith(dataStr, PREFIX_POSITION_A) ||
			 StringUtils.startsWith(dataStr, PREFIX_POSITION_D));
	}
	
	@Override
	protected IDeviceSpecific process(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		DeviceSpecific deviceSpecific = null;
		
		dataStr = Utils.cleanStr(dataStr);
		
		if (StringUtils.startsWith(dataStr, PREFIX_LOGIN)) {
			deviceSpecific = new DeviceSpecific(RequestType.Login);
			Utils.interpretLogin(dataReceived, dataStr);		
		} else if (StringUtils.startsWith(dataStr, PREFIX_POSITION_A)) {
			deviceSpecific = new DeviceSpecific(RequestType.Position);
			Utils.interpretPositionA(dataReceived, dataStr);
		} else if (StringUtils.startsWith(dataStr, PREFIX_POSITION_D)) {
				deviceSpecific = new DeviceSpecific(RequestType.Position);
				Utils.interpretPositionD(dataReceived, dataStr);
		}
		
		return deviceSpecific;
	}
	
	private static final String RES_SUCCESSFUL = "OK";
	private static final String RES_FAILED = "ERROR";
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.interpreter.AbstractDataStrInterpreter#postProcess(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific)
	 */
	@Override
	public String postProcess(DataReceivedVo dataReceived,
		IDeviceSpecific deviceSpecific) throws InterpreterException {
		RequestType requestType = ((DeviceSpecific)deviceSpecific).getRequestType();
		String responseStr = RES_SUCCESSFUL;
		if (requestType.equals(RequestType.Login)) {
			if (!dataReceived.getSenderFromRequest().isAuthorized()) {
				responseStr = RES_FAILED;
			}			
		} 
		return responseStr;
	}
}
