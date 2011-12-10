package de.msk.mylivetracker.domain.track;

import java.io.Serializable;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.PositionVo;

/**
 * TrackTimestampsVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackTimestampsVo implements Serializable {
	private static final long serialVersionUID = -4639046945296447465L;
	
	/**
	 * timestamp when track was created.
	 */
	private DateTime trackCreated = null;
	/**
	 * timestamp when track was updated in any way.
	 */
	private DateTime trackUpdated = null;
	/**
	 * timestamp when first position was received.
	 */
	private DateTime firstPositionReceived = null;
	/**
	 * timestamp when recent position was updated.
	 */
	private DateTime recentPositionUpdated = null;
	/**
	 * timestamp when tracking client sent data.
	 */
	private DateTime lastHeartbeatSent = null;
	
	/**
	 * constructor.
	 */
	public TrackTimestampsVo() {		
	}
	
	/**
	 * constructor.
	 * @param trackCreated
	 */
	public TrackTimestampsVo(DateTime trackCreated) {
		if (trackCreated == null) {
			throw new IllegalArgumentException("trackCreated must not be null.");
		}
		this.trackCreated = trackCreated;	
		this.trackUpdated = trackCreated;		
	}
	
	public void update(PositionVo position) {
		if (position != null) {
			this.recentPositionUpdated = position.getTimeReceived();
			if (this.firstPositionReceived == null) {
				this.firstPositionReceived = this.recentPositionUpdated;
			}
		}
		this.trackUpdated = new DateTime();		
	}
	
	/**
	 * get track runtime in milliseconds.
	 * @return Returns track runtime in milliseconds.
	 */
	public long getTrackRuntimeInMSecs() {
		long runtimeInMSecs = 0;
		if ((this.firstPositionReceived != null) && (this.recentPositionUpdated != null)) {
			runtimeInMSecs = this.recentPositionUpdated.getAsMSecs() - 
				this.firstPositionReceived.getAsMSecs();
		}
		return runtimeInMSecs;
	}
	
	/**
	 * @return the trackCreated
	 */
	public DateTime getTrackCreated() {
		return trackCreated;
	}
	/**
	 * @param trackCreated the trackCreated to set
	 */
	public void setTrackCreated(DateTime trackCreated) {
		this.trackCreated = trackCreated;
	}
	/**
	 * @return the trackUpdated
	 */
	public DateTime getTrackUpdated() {
		return trackUpdated;
	}
	/**
	 * @param trackUpdated the trackUpdated to set
	 */
	public void setTrackUpdated(DateTime trackUpdated) {
		this.trackUpdated = trackUpdated;
	}
	/**
	 * @return the firstPositionReceived
	 */
	public DateTime getFirstPositionReceived() {
		return firstPositionReceived;
	}
	/**
	 * @param firstPositionReceived the firstPositionReceived to set
	 */
	public void setFirstPositionReceived(DateTime firstPositionReceived) {
		this.firstPositionReceived = firstPositionReceived;
	}
	/**
	 * @return the recentPositionUpdated
	 */
	public DateTime getRecentPositionUpdated() {
		return recentPositionUpdated;
	}
	/**
	 * @param recentPositionUpdated the recentPositionUpdated to set
	 */
	public void setRecentPositionUpdated(DateTime recentPositionUpdated) {
		this.recentPositionUpdated = recentPositionUpdated;
	}

	/**
	 * @return the lastHeartbeatSent
	 */
	public DateTime getLastHeartbeatSent() {
		return lastHeartbeatSent;
	}

	/**
	 * @param lastHeartbeatSent the lastHeartbeatSent to set
	 */
	public void setLastHeartbeatSent(DateTime lastHeartbeatSent) {
		this.lastHeartbeatSent = lastHeartbeatSent;
	}	
}
