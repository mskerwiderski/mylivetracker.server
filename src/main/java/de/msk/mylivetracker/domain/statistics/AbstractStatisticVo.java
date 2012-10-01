package de.msk.mylivetracker.domain.statistics;

import java.io.Serializable;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * AbstractStatisticVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractStatisticVo implements Serializable {

	private static final long serialVersionUID = 599490757924550521L;
	
	private Integer logId;
	private DateTime logTimestamp = new DateTime();

	protected static final String UNKNOWN = "<unknown>";
	protected static final String EMPTY_STRING = "<empty string>";
	protected static final String EMPTY = "<./.>";
	
	public Integer getLogId() {
		return logId;
	}
	public void setLogId(Integer logId) {
		this.logId = logId;
	}
	public DateTime getLogTimestamp() {
		return logTimestamp;
	}
	public void setLogTimestamp(DateTime logTimestamp) {
		this.logTimestamp = logTimestamp;
	}	
}
