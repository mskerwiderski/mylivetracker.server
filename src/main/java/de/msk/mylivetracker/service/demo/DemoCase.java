package de.msk.mylivetracker.service.demo;

/**
 * DemoCase.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DemoCase {
	private String name;
	private String userId;
	private String senderId;
	private String trackId;
	private Integer startInSeconds;
	public DemoCase(String name,
		String userId, String senderId, 
		String trackId,	Integer startInSeconds) {
		this.name = name;
		this.userId = userId;
		this.senderId = senderId;
		this.trackId = trackId;
		this.startInSeconds = startInSeconds;
	}
	public String getName() {
		return name;
	}
	public String getUserId() {
		return userId;
	}
	public String getSenderId() {
		return senderId;
	}
	public String getTrackId() {
		return trackId;
	}
	public Integer getStartInSeconds() {
		return startInSeconds;
	}		
}
