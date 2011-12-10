package de.msk.mylivetracker.web.uploader.processor.server.tcp;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.web.uploader.processor.IDataCtx;

/**
 * DataStrsCtx.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DataStrsCtx implements IDataCtx {

	private Queue<String> dataStrQueue = 
		new LinkedList<String>();
	
	public DataStrsCtx(String dataStr) {
		if (!StringUtils.isEmpty(dataStr)) {
			dataStr = StringUtils.replace(dataStr, "\r\n", "\n");
			dataStr = StringUtils.replace(dataStr, "\r", "\n");
			String[] dataStrArr = StringUtils.split(dataStr, "\n");			
			for (int i=0; i < dataStrArr.length; i++) {
				dataStrQueue.add(dataStrArr[i]);
			}	
		}
	}
	
	public DataStrCtx poll() {
		DataStrCtx res = null;
		if (!this.dataStrQueue.isEmpty()) {
			res = new DataStrCtx(this.dataStrQueue.poll()); 
		}
		return res;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.IDataCtx#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.dataStrQueue.isEmpty();
	}	
}
