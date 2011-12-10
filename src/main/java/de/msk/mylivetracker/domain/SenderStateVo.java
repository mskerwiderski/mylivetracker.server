package de.msk.mylivetracker.domain;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.track.TrackVo;

/**
 * SenderStateVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SenderStateVo implements Serializable {

	private static final long serialVersionUID = -2505223487859156006L;
	
	private String senderStateId = UUID.randomUUID().toString();
	private TrackVo track;
	private PositionVo position;
	private DateTime timeReceived;
	private String state;
	
	/**
	 * constructor.
	 */
	public SenderStateVo() {
	}

	public boolean isValid() {
		return
			!StringUtils.isEmpty(this.senderStateId) &&
			(this.timeReceived != null) &&
			(!StringUtils.isEmpty(this.state));
	}
	
	public void addState(String state) {
		if (!StringUtils.isEmpty(this.state)) {
			this.state += ", " + state;
		} else {
			this.state = state;
		}
	}
	
	/**
	 * @return the senderStateId
	 */
	public String getSenderStateId() {
		return senderStateId;
	}

	/**
	 * @param senderStateId the senderStateId to set
	 */
	public void setSenderStateId(String senderStateId) {
		this.senderStateId = senderStateId;
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
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(String state) {
		this.state = state;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SenderStateVo [timeReceived=" + timeReceived
			+ ", state=" + state + "]";
	}			
}
