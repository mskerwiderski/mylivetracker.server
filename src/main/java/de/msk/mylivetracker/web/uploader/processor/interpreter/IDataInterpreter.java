package de.msk.mylivetracker.web.uploader.processor.interpreter;

import java.util.Map;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.uploader.processor.DataPacket;
import de.msk.mylivetracker.web.uploader.processor.IDataCtx;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.SenderFromRequestVo;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;

/**
 * IDataInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IDataInterpreter {

	public String getId();
	public String getName();
	public String getVersion();
	
	public DataPacket createDataPacket(
		Services supportedServices, 
		SenderFromRequestVo senderFromRequest, 
		IDataCtx data, Map<String, Object> uploadProcessContext);
	
	public IDeviceSpecific process(DataReceivedVo dataReceived, IDataCtx data, Map<String, Object> uploadProcessContext) throws InterpreterException;
	public boolean isDeviceCompliant(DataReceivedVo dataReceived, IDataCtx data) throws InterpreterException;
	public String postProcess(DataReceivedVo dataReceived, IDeviceSpecific deviceSpecific) throws InterpreterException;
	public String onError(DataReceivedVo dataReceived, Exception exception, IDeviceSpecific deviceSpecific);
}
