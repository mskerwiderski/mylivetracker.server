package de.msk.mylivetracker.domain.demo;

import java.io.Serializable;
import java.util.List;

/**
 * DemoTrackVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DemoTrackVo implements Serializable {
		
	private static final long serialVersionUID = -1056995479386628868L;
	
	private String trackId;
	private String trackName;
	private List<DemoPositionVo> positions;
	
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
	 * @return the positions
	 */
	public List<DemoPositionVo> getPositions() {
		return positions;
	}
	/**
	 * @param positions the positions to set
	 */
	public void setPositions(List<DemoPositionVo> positions) {
		this.positions = positions;
	}	
}
