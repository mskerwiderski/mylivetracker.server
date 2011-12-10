package de.msk.mylivetracker.domain;

import java.io.Serializable;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.track.TrackVo;

/**
 * MessageVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class MessageVo implements Serializable {

	private static final long serialVersionUID = -4187347366863534149L;

	private String messageId = UUID.randomUUID().toString();
	private TrackVo track;
	private PositionVo position;	
	private String content;
	private DateTime timeReceived;
		
	/**
	 * constructor.
	 */
	public MessageVo() {		
	}
	
	/**
	 * @param content
	 * @param timeReceived
	 */
	public MessageVo(String content, DateTime timeReceived) {
		this.content = content;
		this.timeReceived = timeReceived;
	}
	
	public boolean isValid() {
		return
		!StringUtils.isEmpty(this.messageId) &&
		(this.timeReceived != null) &&
		!StringUtils.isEmpty(this.content);
	}
	
	/**
	 * @return the messageId
	 */
	public String getMessageId() {
		return messageId;
	}

	/**
	 * @param messageId the messageId to set
	 */
	public void setMessageId(String messageId) {
		this.messageId = messageId;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageVo [timeReceived=" + timeReceived + ", content="
				+ content + "]";
	}	
}
