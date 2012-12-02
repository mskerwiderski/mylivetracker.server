package de.msk.mylivetracker.web.uploader.processor.server.http;

import java.util.Map;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.VoidDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;

/**
 * AbstractHttpServletRequestInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractHttpServletRequestWoDeviceSpecificInterpreter	extends AbstractHttpServletRequestInterpreter {	
		
	@Override
	protected IDeviceSpecific process(DataReceivedVo dataReceived,
			Map<String, String> parameterMap,
			Map<String, Object> uploadProcessContext)
			throws InterpreterException {
		this.process(dataReceived, parameterMap);
		return VoidDeviceSpecific.VOID;
	}

	protected abstract void	process(DataReceivedVo dataReceived,
		Map<String, String> parameterMap) throws InterpreterException;	
}
