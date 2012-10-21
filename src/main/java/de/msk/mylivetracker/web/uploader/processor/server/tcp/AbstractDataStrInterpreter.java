package de.msk.mylivetracker.web.uploader.processor.server.tcp;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.web.uploader.processor.IDataCtx;
import de.msk.mylivetracker.web.uploader.processor.IDeviceSpecific;
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
	
	private DataStrOpt dataStrOpt = DataStrOpt.None;
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#isDeviceCompliant(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDataCtx)
	 */
	@Override
	public boolean isDeviceCompliant(DataReceivedVo dataReceived, IDataCtx data)
		throws InterpreterException {
		String dataStr = ((DataStrCtx)data).getDataStr();
		if (!this.dataStrOpt.equals(DataStrOpt.None)) {
			dataStr = this.getSingleDataStr(dataStr, 
				this.dataStrOpt.equals(DataStrOpt.GetFirstSingleDataStr));
		}
		return this.isDeviceCompliant(dataStr);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter#process(de.msk.mylivetracker.domain.DataReceivedVo, de.msk.mylivetracker.web.uploader.processor.IDataCtx, java.util.Map)
	 */
	@Override
	public IDeviceSpecific process(DataReceivedVo dataReceived, IDataCtx data,
		Map<String, Object> uploadProcessContext)
		throws InterpreterException {
		String dataStr = ((DataStrCtx)data).getDataStr();
		if (!this.dataStrOpt.equals(DataStrOpt.None)) {
			dataStr = this.getSingleDataStr(dataStr, 
				this.dataStrOpt.equals(DataStrOpt.GetFirstSingleDataStr));
		}
		return this.process(dataReceived, dataStr,
			uploadProcessContext);
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
		String dataStr, Map<String, Object> uploadProcessContext) throws InterpreterException;
	
	protected abstract boolean isDeviceCompliant(String dataStr) throws InterpreterException;
	
	protected String getSingleDataStr(String dataStr, boolean first) throws InterpreterException {
		String singleDataString = dataStr;
		if (!StringUtils.isEmpty(dataStr)) {
			dataStr = StringUtils.replace(dataStr, "\r\n", "\n");
			dataStr = StringUtils.replace(dataStr, "\r", "\n");
			String[] dataStrArr = StringUtils.split(dataStr, "\n");
			if ((dataStrArr != null) && (dataStrArr.length > 0)) {
				singleDataString = (first ? 
					dataStrArr[0] : dataStrArr[dataStrArr.length-1]);
			}
		}
		return singleDataString;
	}

	public DataStrOpt getDataStrOpt() {
		return dataStrOpt;
	}

	public void setDataStrOpt(DataStrOpt dataStrOpt) {
		this.dataStrOpt = dataStrOpt;
	}
}
