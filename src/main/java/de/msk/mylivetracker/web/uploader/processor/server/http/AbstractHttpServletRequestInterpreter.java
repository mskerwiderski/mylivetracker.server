package de.msk.mylivetracker.web.uploader.processor.server.http;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.IDataCtx;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.interpreter.AbstractDataInterpreter;
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
public abstract class AbstractHttpServletRequestInterpreter	extends AbstractDataInterpreter {	
		
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#isDeviceCompliant(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDataCtx)
	 */
	@Override
	public boolean isDeviceCompliant(DataReceivedVo dataReceived, IDataCtx data)
		throws InterpreterException {
		boolean res = false;
		String servletPath = ((HttpServletRequestCtx)data).getRequest().getServletPath();
		Pattern pattern = Pattern.compile("\\/upl_(.*)\\.sec\\b");
		Matcher matcher = pattern.matcher(servletPath);
		if (matcher.matches()) {
			res = StringUtils.equals(this.getId(), matcher.group(1));
		} 		
		return res;
	}	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDataCtx, java.util.Map)
	 */
	@Override
	public IDeviceSpecific process(DataReceivedVo dataReceived, IDataCtx data,
		Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		IDeviceSpecific deviceSpecific = this.process(dataReceived, 
			((HttpServletRequestCtx)data).getParameterMap(),
			uploadProcessContext);
		return deviceSpecific;
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#postProcess(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific)
	 */
	@Override
	public String postProcess(DataReceivedVo dataReceived,
		IDeviceSpecific deviceSpecific) throws InterpreterException {
		return null;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#onError(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific)
	 */
	@Override
	public String onError(DataReceivedVo dataReceived,
		Exception exception,	
		IDeviceSpecific deviceSpecific) {
		return null;
	}

	protected abstract IDeviceSpecific process(DataReceivedVo dataReceived,
		Map<String, String> parameterMap, Map<String, Object> uploadProcessContext) throws InterpreterException;	
}
