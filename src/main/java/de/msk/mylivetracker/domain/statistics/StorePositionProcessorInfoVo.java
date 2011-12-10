package de.msk.mylivetracker.domain.statistics;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * StorePositionProcessorInfoVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class StorePositionProcessorInfoVo extends AbstractStatisticVo {
	
	private static final long serialVersionUID = 5612922182542756330L;
	
	private String senderId;
	private DateTime started;	
	private DateTime updated;
	private long processedPositions;
	private long skippedPositions;
	private long errorCount;
	private long retryCount;
	private int maxNumberOfRetriesOccurred;
	private String lastErrorMsg;
	private DateTime lastErrorOccurred;
	private Boolean lastStatus;
	private String reasonStopped;
	
	public StorePositionProcessorInfoVo() {
	}
	
	/**
	 * @param senderId
	 * @param started
	 * @param expired
	 * @param updated
	 * @param processedPositions
	 * @param skippedPositions
	 * @param errorCount
	 * @param retryCount
	 * @param maxNumberOfRetriesOccurred
	 * @param lastErrorMsg
	 * @param lastErrorOccurred
	 * @param lastStatus
	 */
	public StorePositionProcessorInfoVo(String senderId, DateTime started,
		DateTime updated, long processedPositions,
		long skippedPositions, long errorCount, long retryCount,
		int maxNumberOfRetriesOccurred, String lastErrorMsg,
		DateTime lastErrorOccurred, Boolean lastStatus, String reasonStopped) {
		this.senderId =
			(StringUtils.isEmpty(senderId) ? UNKNOWN : senderId);
		this.started = started;
		this.updated = updated;
		this.processedPositions = processedPositions;
		this.skippedPositions = skippedPositions;
		this.errorCount = errorCount;
		this.retryCount = retryCount;
		this.maxNumberOfRetriesOccurred = maxNumberOfRetriesOccurred;
		this.lastErrorMsg =
			(StringUtils.isEmpty(lastErrorMsg) ? EMPTY : lastErrorMsg);
		this.lastErrorOccurred = lastErrorOccurred;
		this.lastStatus = lastStatus;
		this.reasonStopped =
			(StringUtils.isEmpty(reasonStopped) ? UNKNOWN : reasonStopped);
	}
	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}
	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	/**
	 * @return the started
	 */
	public DateTime getStarted() {
		return started;
	}
	/**
	 * @param started the started to set
	 */
	public void setStarted(DateTime started) {
		this.started = started;
	}	
	/**
	 * @return the updated
	 */
	public DateTime getUpdated() {
		return updated;
	}
	/**
	 * @param updated the updated to set
	 */
	public void setUpdated(DateTime updated) {
		this.updated = updated;
	}
	/**
	 * @return the processedPositions
	 */
	public long getProcessedPositions() {
		return processedPositions;
	}
	/**
	 * @param processedPositions the processedPositions to set
	 */
	public void setProcessedPositions(long processedPositions) {
		this.processedPositions = processedPositions;
	}
	/**
	 * @return the skippedPositions
	 */
	public long getSkippedPositions() {
		return skippedPositions;
	}
	/**
	 * @param skippedPositions the skippedPositions to set
	 */
	public void setSkippedPositions(long skippedPositions) {
		this.skippedPositions = skippedPositions;
	}
	/**
	 * @return the errorCount
	 */
	public long getErrorCount() {
		return errorCount;
	}
	/**
	 * @param errorCount the errorCount to set
	 */
	public void setErrorCount(long errorCount) {
		this.errorCount = errorCount;
	}
	/**
	 * @return the retryCount
	 */
	public long getRetryCount() {
		return retryCount;
	}
	/**
	 * @param retryCount the retryCount to set
	 */
	public void setRetryCount(long retryCount) {
		this.retryCount = retryCount;
	}
	/**
	 * @return the maxNumberOfRetriesOccurred
	 */
	public int getMaxNumberOfRetriesOccurred() {
		return maxNumberOfRetriesOccurred;
	}
	/**
	 * @param maxNumberOfRetriesOccurred the maxNumberOfRetriesOccurred to set
	 */
	public void setMaxNumberOfRetriesOccurred(int maxNumberOfRetriesOccurred) {
		this.maxNumberOfRetriesOccurred = maxNumberOfRetriesOccurred;
	}
	/**
	 * @return the lastErrorMsg
	 */
	public String getLastErrorMsg() {
		return lastErrorMsg;
	}
	/**
	 * @param lastErrorMsg the lastErrorMsg to set
	 */
	public void setLastErrorMsg(String lastErrorMsg) {
		this.lastErrorMsg = lastErrorMsg;
	}
	/**
	 * @return the lastErrorOccurred
	 */
	public DateTime getLastErrorOccurred() {
		return lastErrorOccurred;
	}
	/**
	 * @param lastErrorOccurred the lastErrorOccurred to set
	 */
	public void setLastErrorOccurred(DateTime lastErrorOccurred) {
		this.lastErrorOccurred = lastErrorOccurred;
	}
	/**
	 * @return the lastStatus
	 */
	public Boolean getLastStatus() {
		return lastStatus;
	}
	/**
	 * @param lastStatus the lastStatus to set
	 */
	public void setLastStatus(Boolean lastStatus) {
		this.lastStatus = lastStatus;
	}

	/**
	 * @return the reasonStopped
	 */
	public String getReasonStopped() {
		return reasonStopped;
	}

	/**
	 * @param reasonStopped the reasonStopped to set
	 */
	public void setReasonStopped(String reasonStopped) {
		this.reasonStopped = reasonStopped;
	}	
}
