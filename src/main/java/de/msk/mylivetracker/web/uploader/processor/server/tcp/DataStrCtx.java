package de.msk.mylivetracker.web.uploader.processor.server.tcp;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.web.uploader.processor.IDataCtx;

/**
 * DataStrCtx.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DataStrCtx implements IDataCtx {

	private String dataStr;
	
	public DataStrCtx(String dataStr) {
		this.dataStr = dataStr;		
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.IDataCtx#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return StringUtils.isEmpty(this.dataStr);
	}
	
	/**
	 * @return the dataStr
	 */
	public String getDataStr() {
		return dataStr;
	}	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getDataStr();
	}	
}
