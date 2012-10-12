package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * UserOperationsCounterVo.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-11
 * 
 */
public class UserOperationsCounterVo implements Serializable {
	
	private static final long serialVersionUID = -3521534990223846331L;

	private String userId;
	private Long versionId;
	private DateTime lastUpdated;
	private Integer countLoginUser;
	private Integer countLoginGuest;
	private Integer countTracksCreated;
	private Integer countTracksDeleted;
	private Integer countPositionsReceived;
	private Integer countMessagesReceived;
	private Integer countMobNwCellsReceived;
	private Integer countSenderStatesReceived;
	private Integer countCardiacFunctionsReceived;
	private Integer countEmergencySignalsReceived;
	private Integer countClientInfosReceived;
	
	public static UserOperationsCounterVo createDefault(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be null.");
		}
		UserOperationsCounterVo userOperationsCounter = new UserOperationsCounterVo();
		userOperationsCounter.userId = userId;
		userOperationsCounter.versionId = 0L;
		userOperationsCounter.lastUpdated = new DateTime();
		userOperationsCounter.countLoginUser = 0;
		userOperationsCounter.countLoginGuest = 0;
		userOperationsCounter.countTracksCreated = 0;
		userOperationsCounter.countTracksDeleted = 0;
		userOperationsCounter.countPositionsReceived = 0;
		userOperationsCounter.countMessagesReceived = 0;
		userOperationsCounter.countMobNwCellsReceived = 0;
		userOperationsCounter.countSenderStatesReceived = 0;
		userOperationsCounter.countCardiacFunctionsReceived = 0;
		userOperationsCounter.countEmergencySignalsReceived = 0;
		userOperationsCounter.countClientInfosReceived = 0;
		return userOperationsCounter;
	}
	
	public boolean isMoreRecentThan(Long versionId) {
		if (versionId == null) return true;
		return this.versionId.longValue() != 
			versionId.longValue();
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public DateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(DateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public Long getVersionId() {
		return versionId;
	}
	public void setVersionId(Long versionId) {
		this.versionId = versionId;
	}
	public Integer getCountLoginUser() {
		return countLoginUser;
	}
	public void setCountLoginUser(Integer countLoginUser) {
		this.countLoginUser = countLoginUser;
	}
	public Integer getCountLoginGuest() {
		return countLoginGuest;
	}
	public void setCountLoginGuest(Integer countLoginGuest) {
		this.countLoginGuest = countLoginGuest;
	}
	public Integer getCountTracksCreated() {
		return countTracksCreated;
	}
	public void setCountTracksCreated(Integer countTracksCreated) {
		this.countTracksCreated = countTracksCreated;
	}
	public Integer getCountTracksDeleted() {
		return countTracksDeleted;
	}
	public void setCountTracksDeleted(Integer countTracksDeleted) {
		this.countTracksDeleted = countTracksDeleted;
	}
	public Integer getCountPositionsReceived() {
		return countPositionsReceived;
	}
	public void setCountPositionsReceived(Integer countPositionsReceived) {
		this.countPositionsReceived = countPositionsReceived;
	}
	public Integer getCountMessagesReceived() {
		return countMessagesReceived;
	}
	public void setCountMessagesReceived(Integer countMessagesReceived) {
		this.countMessagesReceived = countMessagesReceived;
	}
	public Integer getCountMobNwCellsReceived() {
		return countMobNwCellsReceived;
	}
	public void setCountMobNwCellsReceived(Integer countMobNwCellsReceived) {
		this.countMobNwCellsReceived = countMobNwCellsReceived;
	}
	public Integer getCountSenderStatesReceived() {
		return countSenderStatesReceived;
	}
	public void setCountSenderStatesReceived(Integer countSenderStatesReceived) {
		this.countSenderStatesReceived = countSenderStatesReceived;
	}
	public Integer getCountCardiacFunctionsReceived() {
		return countCardiacFunctionsReceived;
	}
	public void setCountCardiacFunctionsReceived(
			Integer countCardiacFunctionsReceived) {
		this.countCardiacFunctionsReceived = countCardiacFunctionsReceived;
	}
	public Integer getCountEmergencySignalsReceived() {
		return countEmergencySignalsReceived;
	}
	public void setCountEmergencySignalsReceived(
			Integer countEmergencySignalsReceived) {
		this.countEmergencySignalsReceived = countEmergencySignalsReceived;
	}
	public Integer getCountClientInfosReceived() {
		return countClientInfosReceived;
	}
	public void setCountClientInfosReceived(Integer countClientInfosReceived) {
		this.countClientInfosReceived = countClientInfosReceived;
	}

	@Override
	public String toString() {
		return "UserOperationsCounterVo [userId=" + userId + ", versionId="
			+ versionId + ", lastUpdated=" + lastUpdated
			+ ", countLoginUser=" + countLoginUser + ", countLoginGuest="
			+ countLoginGuest + ", countTracksCreated="
			+ countTracksCreated + ", countTracksDeleted="
			+ countTracksDeleted + ", countPositionsReceived="
			+ countPositionsReceived + ", countMessagesReceived="
			+ countMessagesReceived + ", countMobNwCellsReceived="
			+ countMobNwCellsReceived + ", countSenderStatesReceived="
			+ countSenderStatesReceived
			+ ", countCardiacFunctionsReceived="
			+ countCardiacFunctionsReceived
			+ ", countEmergencySignalsReceived="
			+ countEmergencySignalsReceived + ", countClientInfosReceived="
			+ countClientInfosReceived + "]";
	}
}
