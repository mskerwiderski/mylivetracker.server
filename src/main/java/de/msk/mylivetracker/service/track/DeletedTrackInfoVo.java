package de.msk.mylivetracker.service.track;

import org.apache.commons.lang.StringUtils;


public class DeletedTrackInfoVo {
	private String userId = null;
	private String trackId = null;
	private int deletedRecords = 0;
	public boolean isValid() {
		return !StringUtils.isEmpty(userId) &&
			!StringUtils.isEmpty(trackId);	
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getTrackId() {
		return trackId;
	}
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}
	public int getDeletedRecords() {
		return deletedRecords;
	}
	public void setDeletedRecords(int deletedRecords) {
		this.deletedRecords = deletedRecords;
	}
	@Override
	public String toString() {
		return "DeletedTrackInfoVo [userId=" + userId + ", trackId=" + trackId
			+ ", deletedRecords=" + deletedRecords + "]";
	}
}