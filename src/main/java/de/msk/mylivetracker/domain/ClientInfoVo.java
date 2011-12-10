package de.msk.mylivetracker.domain;

import java.io.Serializable;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * ClientInfoVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ClientInfoVo implements Serializable {

	private static final long serialVersionUID = -6878807430305767097L;
	private DateTime timeReceived = null;
	private String trackName = null;
	private String trackId = null;
	private String phoneNumber = null;
	private Double mileageInMtr = null;
	private Long runtimeWithPausesInMSecs = null;
	private Long runtimeWithoutPausesInMSecs = null;
	
	public boolean isValid() {
		return (this.timeReceived != null);
	}
	
	/**
	 * @return the timeReceived
	 */
	public DateTime getTimeReceived() {
		return timeReceived;
	}
	/**
	 * @param timeReceived the timeReceived to set
	 */
	public void setTimeReceived(DateTime timeReceived) {
		this.timeReceived = timeReceived;
	}
	/**
	 * @return the trackName
	 */
	public String getTrackName() {
		return trackName;
	}
	/**
	 * @param trackName the trackName to set
	 */
	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}
	/**
	 * @return the trackId
	 */
	public String getTrackId() {
		return trackId;
	}
	/**
	 * @param trackId the trackId to set
	 */
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}	
	/**
	 * @return the phoneNumber
	 */
	public String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * @param phoneNumber the phoneNumber to set
	 */
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * @return the mileageInMtr
	 */
	public Double getMileageInMtr() {
		return mileageInMtr;
	}
	/**
	 * @param mileageInMtr the mileageInMtr to set
	 */
	public void setMileageInMtr(Double mileageInMtr) {
		this.mileageInMtr = mileageInMtr;
	}
	/**
	 * @return the runtimeWithPausesInMSecs
	 */
	public Long getRuntimeWithPausesInMSecs() {
		return runtimeWithPausesInMSecs;
	}
	/**
	 * @param runtimeWithPausesInMSecs the runtimeWithPausesInMSecs to set
	 */
	public void setRuntimeWithPausesInMSecs(Long runtimeWithPausesInMSecs) {
		this.runtimeWithPausesInMSecs = runtimeWithPausesInMSecs;
	}
	/**
	 * @return the runtimeWithoutPausesInMSecs
	 */
	public Long getRuntimeWithoutPausesInMSecs() {
		return runtimeWithoutPausesInMSecs;
	}
	/**
	 * @param runtimeWithoutPausesInMSecs the runtimeWithoutPausesInMSecs to set
	 */
	public void setRuntimeWithoutPausesInMSecs(Long runtimeWithoutPausesInMSecs) {
		this.runtimeWithoutPausesInMSecs = runtimeWithoutPausesInMSecs;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ClientInfoVo [timeReceived=" + timeReceived + ", trackName="
			+ trackName + ", trackId=" + trackId + ", phoneNumber="
			+ phoneNumber + ", mileageInMtr=" + mileageInMtr
			+ ", runtimeWithPausesInMSecs=" + runtimeWithPausesInMSecs
			+ ", runtimeWithoutPausesInMSecs="
			+ runtimeWithoutPausesInMSecs + "]";
	}
}
