package de.msk.mylivetracker.domain;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.track.TrackVo;

/**
 * CardiacFunctionVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class CardiacFunctionVo implements Serializable {

	private static final long serialVersionUID = 3016470540035865103L;
	
	private String cardiacFunctionId = UUID.randomUUID().toString();
	private TrackVo track;
	private PositionVo position;
	private DateTime timeReceived;
	
	private Integer heartrateInBpm;
	private Integer heartrateMinInBpm;
	private Integer heartrateMaxInBpm;
	private Integer heartrateAvgInBpm;	
	
	/**
	 * constructor.
	 */
	public CardiacFunctionVo() {
	}

	public boolean isValid() {
		return
			!StringUtils.isEmpty(this.cardiacFunctionId) &&
			(this.timeReceived != null) &&
			((this.heartrateInBpm != null) ||
			 (this.heartrateMinInBpm != null) ||
			 (this.heartrateMaxInBpm != null) ||
			 (this.heartrateAvgInBpm != null));
	}

	/**
	 * @return the cardiacFunctionId
	 */
	public String getCardiacFunctionId() {
		return cardiacFunctionId;
	}

	/**
	 * @param cardiacFunctionId the cardiacFunctionId to set
	 */
	public void setCardiacFunctionId(String cardiacFunctionId) {
		this.cardiacFunctionId = cardiacFunctionId;
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
	 * @return the heartrateInBpm
	 */
	public Integer getHeartrateInBpm() {
		return heartrateInBpm;
	}

	/**
	 * @param heartrateInBpm the heartrateInBpm to set
	 */
	public void setHeartrateInBpm(Integer heartrateInBpm) {
		this.heartrateInBpm = heartrateInBpm;
	}

	/**
	 * @return the heartrateMinInBpm
	 */
	public Integer getHeartrateMinInBpm() {
		return heartrateMinInBpm;
	}

	/**
	 * @param heartrateMinInBpm the heartrateMinInBpm to set
	 */
	public void setHeartrateMinInBpm(Integer heartrateMinInBpm) {
		this.heartrateMinInBpm = heartrateMinInBpm;
	}

	/**
	 * @return the heartrateMaxInBpm
	 */
	public Integer getHeartrateMaxInBpm() {
		return heartrateMaxInBpm;
	}

	/**
	 * @param heartrateMaxInBpm the heartrateMaxInBpm to set
	 */
	public void setHeartrateMaxInBpm(Integer heartrateMaxInBpm) {
		this.heartrateMaxInBpm = heartrateMaxInBpm;
	}

	/**
	 * @return the heartrateAvgInBpm
	 */
	public Integer getHeartrateAvgInBpm() {
		return heartrateAvgInBpm;
	}

	/**
	 * @param heartrateAvgInBpm the heartrateAvgInBpm to set
	 */
	public void setHeartrateAvgInBpm(Integer heartrateAvgInBpm) {
		this.heartrateAvgInBpm = heartrateAvgInBpm;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CardiacFunctionVo [cardiacFunctionId=" + cardiacFunctionId
			+ ", timeReceived=" + timeReceived + ", heartrateInBpm="
			+ heartrateInBpm + ", heartrateMinInBpm=" + heartrateMinInBpm
			+ ", heartrateMaxInBpm=" + heartrateMaxInBpm
			+ ", heartrateAvgInBpm=" + heartrateAvgInBpm + "]";
	}
}
