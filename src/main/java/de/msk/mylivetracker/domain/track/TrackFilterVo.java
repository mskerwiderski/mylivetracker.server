package de.msk.mylivetracker.domain.track;

import java.io.Serializable;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;

/**
 * TrackFilterVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackFilterVo implements Serializable {

	private static final long serialVersionUID = -8825211722592425417L;
	
	private int maxCountOfRecords;
	
	// *********************************************************
	// filter options to ensure that the user only gets tracks
	// he is allowed to see.
	// *********************************************************
	
	/*
	 * if userRole==Admin --> user is allowed to see all tracks.
	 * if userRole==User  --> user is allowed to see all his own tracks.
	 * if userRole==Guest --> user is allowed to see users tracks 
	 * (depending on guestAccClosedTrEnabled and guestAccPrivTrEnabled flags).
	 */
	private String userId;
	private UserRole userRole;
	private Boolean guestAccClosedTrEnabled;
	private Boolean guestAccPrivTrEnabled;
	
	// *********************************************************
	// filter options to ensure that the user only gets tracks
	// he likes to see.
	// *********************************************************
	
	private String bySenderId = null;
	private DateTime byDateFrom = null;
	private DateTime byDateTo = null;
	private String bySearchStr = null;
	private Integer byActive = null;
	
	/**
	 * @return the maxCountOfRecords
	 */
	public int getMaxCountOfRecords() {
		return maxCountOfRecords;
	}
	/**
	 * @param maxCountOfRecords the maxCountOfRecords to set
	 */
	public void setMaxCountOfRecords(int maxCountOfRecords) {
		this.maxCountOfRecords = maxCountOfRecords;
	}
	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * @return the userRole
	 */
	public UserRole getUserRole() {
		return userRole;
	}
	/**
	 * @param userRole the userRole to set
	 */
	public void setUserRole(UserRole userRole) {
		this.userRole = userRole;
	}	
	/**
	 * @return the guestAccClosedTrEnabled
	 */
	public Boolean getGuestAccClosedTrEnabled() {
		return guestAccClosedTrEnabled;
	}
	/**
	 * @param guestAccClosedTrEnabled the guestAccClosedTrEnabled to set
	 */
	public void setGuestAccClosedTrEnabled(Boolean guestAccClosedTrEnabled) {
		this.guestAccClosedTrEnabled = guestAccClosedTrEnabled;
	}
	/**
	 * @return the guestAccPrivTrEnabled
	 */
	public Boolean getGuestAccPrivTrEnabled() {
		return guestAccPrivTrEnabled;
	}
	/**
	 * @param guestAccPrivTrEnabled the guestAccPrivTrEnabled to set
	 */
	public void setGuestAccPrivTrEnabled(Boolean guestAccPrivTrEnabled) {
		this.guestAccPrivTrEnabled = guestAccPrivTrEnabled;
	}	
	/**
	 * @return the bySenderId
	 */
	public String getBySenderId() {
		return bySenderId;
	}
	/**
	 * @param bySenderId the bySenderId to set
	 */
	public void setBySenderId(String bySenderId) {
		this.bySenderId = bySenderId;
	}
	/**
	 * @return the byDateFrom
	 */
	public DateTime getByDateFrom() {
		return byDateFrom;
	}
	/**
	 * @param byDateFrom the byDateFrom to set
	 */
	public void setByDateFrom(DateTime byDateFrom) {
		this.byDateFrom = byDateFrom;
	}
	/**
	 * @return the byDateTo
	 */
	public DateTime getByDateTo() {
		return byDateTo;
	}
	/**
	 * @param byDateTo the byDateTo to set
	 */
	public void setByDateTo(DateTime byDateTo) {
		this.byDateTo = byDateTo;
	}
	/**
	 * @return the bySearchStr
	 */
	public String getBySearchStr() {
		return bySearchStr;
	}
	/**
	 * @param bySearchStr the bySearchStr to set
	 */
	public void setBySearchStr(String bySearchStr) {
		this.bySearchStr = bySearchStr;
	}
	/**
	 * @return the byActive
	 */
	public Integer getByActive() {
		return byActive;
	}
	/**
	 * @param byActive the byActive to set
	 */
	public void setByActive(Integer byActive) {
		this.byActive = byActive;
	}		
}
