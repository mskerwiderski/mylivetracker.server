package de.msk.mylivetracker.domain;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.track.TrackVo;

/**
 * EmergencySignalVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class EmergencySignalVo implements Serializable {

	private static final long serialVersionUID = 6942877027854269273L;

	private String emergencySignalId = UUID.randomUUID().toString();
	private TrackVo track;
	private PositionVo position;
	private DateTime timeReceived;
	private Boolean active;
	private String message;
	private Boolean smsSuccessfullySent;
	
	/**
	 * constructor.
	 */
	public EmergencySignalVo() {
	}

	public boolean isValid() {
		return
			!StringUtils.isEmpty(this.emergencySignalId) &&
			(this.timeReceived != null) &&
			(this.active != null);
	}
		
	/**
	 * @return the emergencySignalId
	 */
	public String getEmergencySignalId() {
		return emergencySignalId;
	}

	/**
	 * @param emergencySignalId the emergencySignalId to set
	 */
	public void setEmergencySignalId(String emergencySignalId) {
		this.emergencySignalId = emergencySignalId;
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
	 * @return the position
	 */
	public PositionVo getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(PositionVo position) {
		this.position = position;
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
	 * @return the active
	 */
	public Boolean getActive() {
		return active;
	}

	/**
	 * @param active the active to set
	 */
	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the smsSuccessfullySent
	 */
	public Boolean getSmsSuccessfullySent() {
		return smsSuccessfullySent;
	}

	/**
	 * @param smsSuccessfullySent the smsSuccessfullySent to set
	 */
	public void setSmsSuccessfullySent(Boolean smsSuccessfullySent) {
		this.smsSuccessfullySent = smsSuccessfullySent;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EmergencyVo [timeReceived=" + timeReceived + ", active="
			+ active + ", message=" + message + "]";
	}	
}
