package de.msk.mylivetracker.domain.statistics;

import java.io.Serializable;
import java.util.UUID;

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
	
	private String logId = UUID.randomUUID().toString();
	private DateTime logTimestamp = new DateTime();

	protected static final String UNKNOWN = "<unknown>";
	protected static final String EMPTY_STRING = "<empty string>";
	protected static final String EMPTY = "<./.>";
	
	/**
	 * @return the logId
	 */
	public String getLogId() {
		return logId;
	}
	/**
	 * @param logId the logId to set
	 */
	public void setLogId(String logId) {
		this.logId = logId;
	}
	/**
	 * @return the logTimestamp
	 */
	public DateTime getLogTimestamp() {
		return logTimestamp;
	}
	/**
	 * @param logTimestamp the logTimestamp to set
	 */
	public void setLogTimestamp(DateTime logTimestamp) {
		this.logTimestamp = logTimestamp;
	}	
}
