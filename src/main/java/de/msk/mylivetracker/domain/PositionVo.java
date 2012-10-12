package de.msk.mylivetracker.domain;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.track.TrackVo;

/**
 * PositionVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class PositionVo  implements Serializable {
		
	private static final long serialVersionUID = -90570795107760402L;
	
	private String positionId = UUID.randomUUID().toString();
	private Integer sequenceId;
	private TrackVo track;
	private DateTime timeRecorded;
	private DateTime timeReceived;
	private boolean valid = true;
	private Double latitudeInDecimal;
	private Double longitudeInDecimal;
	private Double accuracyInMtr;
	private Double altitudeInMtr;
	private Double speedInKmPerHour;
	private String address;
	
	public PositionVo() {
	}
		
	public PositionVo(Double latitudeInDecimal, Double longitudeInDecimal) {
		this.latitudeInDecimal = latitudeInDecimal;
		this.longitudeInDecimal = longitudeInDecimal;
	}
	
	/**
	 * @param timeRecorded
	 * @param timeReceived
	 * @param latitudeInDecimal
	 * @param longitudeInDecimal
	 * @param accuracyInMtr
	 * @param altitudeInMtr
	 * @param speedInKmPerHour
	 */
	public PositionVo(DateTime timeRecorded, DateTime timeReceived,
			boolean valid,
			Double latitudeInDecimal, Double longitudeInDecimal,
			Double accuracyInMtr,
			Double altitudeInMtr, Double speedInKmPerHour) {
		this.timeRecorded = timeRecorded;
		this.timeReceived = timeReceived;
		this.latitudeInDecimal = latitudeInDecimal;
		this.longitudeInDecimal = longitudeInDecimal;
		this.accuracyInMtr = accuracyInMtr;
		this.altitudeInMtr = altitudeInMtr;
		this.speedInKmPerHour = speedInKmPerHour;		
	}

	public boolean isValid() {
		return 			
			!StringUtils.isEmpty(this.positionId) &&
			(this.timeReceived != null) &&
			this.valid &&
			(this.latitudeInDecimal != null) &&
			(this.longitudeInDecimal != null);
	}
	
	/**
	 * @return the positionId
	 */
	public String getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId the positionId to set
	 */
	public void setPositionId(String positionId) {
		this.positionId = positionId;
	}

	/**
	 * @return the sequenceId
	 */
	public Integer getSequenceId() {
		return sequenceId;
	}

	/**
	 * @param sequenceId the sequenceId to set
	 */
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}

	/**
	 * @return the track
	 */
	public TrackVo getTrack() {
		return track;
	}

	/**
	 * @param track the track to set
	 */
	public void setTrack(TrackVo track) {
		this.track = track;
	}

	/**
	 * @return the timeRecorded
	 */
	public DateTime getTimeRecorded() {
		return timeRecorded;
	}

	/**
	 * @param timeRecorded the timeRecorded to set
	 */
	public void setTimeRecorded(DateTime timeRecorded) {
		this.timeRecorded = timeRecorded;
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
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/**
	 * @return the latitudeInDecimal
	 */
	public Double getLatitudeInDecimal() {
		return latitudeInDecimal;
	}

	/**
	 * @param latitudeInDecimal the latitudeInDecimal to set
	 */
	public void setLatitudeInDecimal(Double latitudeInDecimal) {
		this.latitudeInDecimal = latitudeInDecimal;
	}

	/**
	 * @return the longitudeInDecimal
	 */
	public Double getLongitudeInDecimal() {
		return longitudeInDecimal;
	}

	/**
	 * @param longitudeInDecimal the longitudeInDecimal to set
	 */
	public void setLongitudeInDecimal(Double longitudeInDecimal) {
		this.longitudeInDecimal = longitudeInDecimal;
	}

	/**
	 * @return the accuracyInMtr
	 */
	public Double getAccuracyInMtr() {
		return accuracyInMtr;
	}

	/**
	 * @param accuracyInMtr the accuracyInMtr to set
	 */
	public void setAccuracyInMtr(Double accuracyInMtr) {
		this.accuracyInMtr = accuracyInMtr;
	}

	/**
	 * @return the altitudeInMtr
	 */
	public Double getAltitudeInMtr() {
		return altitudeInMtr;
	}

	/**
	 * @param altitudeInMtr the altitudeInMtr to set
	 */
	public void setAltitudeInMtr(Double altitudeInMtr) {
		this.altitudeInMtr = altitudeInMtr;
	}

	/**
	 * @return the speedInKmPerHour
	 */
	public Double getSpeedInKmPerHour() {
		return speedInKmPerHour;
	}

	/**
	 * @param speedInKmPerHour the speedInKmPerHour to set
	 */
	public void setSpeedInKmPerHour(Double speedInKmPerHour) {
		this.speedInKmPerHour = speedInKmPerHour;
	}	
	
	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PositionVo [positionId=" + positionId + ", sequenceId="
			+ sequenceId + ", track=" + track + ", timeRecorded="
			+ timeRecorded + ", timeReceived=" + timeReceived + ", valid="
			+ valid + ", latitudeInDecimal=" + latitudeInDecimal
			+ ", longitudeInDecimal=" + longitudeInDecimal
			+ ", altitudeInMtr=" + altitudeInMtr + ", speedInKmPerHour="
			+ speedInKmPerHour + ", address=" + address + "]";
	}	
}
