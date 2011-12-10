package de.msk.mylivetracker.web.uploader.processor.server.tcp;

import java.util.Map;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.VoidDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;

/**
 * AbstractDataStrWoDeviceSpecificInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractDataStrWoDeviceSpecificInterpreter extends AbstractDataStrInterpreter {

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.server.tcp.AbstractDataStrInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, java.lang.String, java.util.Map)
	 */
	@Override
	protected IDeviceSpecific process(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		processWoDeviceSpecific(dataReceived, dataStr, uploadProcessContext);
		return VoidDeviceSpecific.VOID;
	}
	
	protected abstract void processWoDeviceSpecific(DataReceivedVo dataReceived,
		String dataStr, Map<String, Object> uploadProcessContext) throws InterpreterException;
}
