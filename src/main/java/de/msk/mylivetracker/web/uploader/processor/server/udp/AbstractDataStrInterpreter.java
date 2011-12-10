package de.msk.mylivetracker.web.uploader.processor.server.udp;

import java.util.Map;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.IDataCtx;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.VoidDeviceSpecific;
import de.msk.mylivetracker.web.uploader.processor.interpreter.AbstractDataInterpreter;
import de.msk.mylivetracker.web.uploader.processor.interpreter.util.InterpreterException;

/**
 * AbstractDataStrInterpreter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractDataStrInterpreter extends AbstractDataInterpreter {
	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#isDeviceCompliant(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDataCtx)
	 */
	@Override
	public boolean isDeviceCompliant(DataReceivedVo dataReceived, IDataCtx data)
		throws InterpreterException {
		return this.isDeviceCompliant(((DatagramPacketCtx)data).getDataStr());
	}	

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDataCtx, java.util.Map)
	 */
	@Override
	public IDeviceSpecific process(DataReceivedVo dataReceived, IDataCtx data,
		Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		this.process(dataReceived, ((DatagramPacketCtx)data).getDataStr());
		return VoidDeviceSpecific.VOID;
	}	

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#postProcess(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific)
	 */
	@Override
	public String postProcess(DataReceivedVo dataReceived,
		IDeviceSpecific deviceSpecific) throws InterpreterException {
		// it is udp, so there is nothing to send back.
		return null;
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#onError(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific)
	 */
	@Override
	public String onError(DataReceivedVo dataReceived,
		Exception exception,
		IDeviceSpecific deviceSpecific) {
		// it is udp, so there is nothing to send back.
		return null;
	}
	
	protected abstract void process(DataReceivedVo dataReceived,
		String dataStr) throws InterpreterException;
	protected abstract boolean isDeviceCompliant(String dataStr) throws InterpreterException;
}
