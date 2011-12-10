package de.msk.mylivetracker.domain;

import java.io.Serializable;
import java.util.UUID;

/**
 * StatusParamsVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class StatusParamsVo implements Serializable {
	
	private static final long serialVersionUID = 3111256411117198388L;
	
	public enum TrackingFlyToMode {
		None, FlyToView, FlyToCurrentPosition
	};
	
	private String statusParamsId;
	private String userId;
	private String ticketId;
	private String senderId;
	private String trackId;
	private Boolean trackingLive;
	private Integer trackingKeepRecentPositions;
	private Integer trackingUpdateIntervalInSecs;
	private Integer trackingFlyToMode;
	private Boolean windowFullscreen;
	private Integer windowWidth;
	private Integer windowHeight;
	private Boolean showTrackInfo;
	private String cssStyle;
	
	public static StatusParamsVo createInstance() {
		StatusParamsVo res = new StatusParamsVo();
		res.statusParamsId = UUID.randomUUID().toString();
		return res;
	}
	
	/**
	 * @return the statusParamsId
	 */
	public String getStatusParamsId() {
		return statusParamsId;
	}

	/**
	 * @param statusParamsId the statusParamsId to set
	 */
	public void setStatusParamsId(String statusParamsId) {
		this.statusParamsId = statusParamsId;
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
	 * @return the ticketId
	 */
	public String getTicketId() {
		return ticketId;
	}
	/**
	 * @param ticketId the ticketId to set
	 */
	public void setTicketId(String ticketId) {
		this.ticketId = ticketId;
	}
	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}
	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
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
	 * @return the trackingLive
	 */
	public Boolean getTrackingLive() {
		return trackingLive;
	}
	/**
	 * @param trackingLive the trackingLive to set
	 */
	public void setTrackingLive(Boolean trackingLive) {
		this.trackingLive = trackingLive;
	}
	/**
	 * @return the trackingKeepRecentPositions
	 */
	public Integer getTrackingKeepRecentPositions() {
		return trackingKeepRecentPositions;
	}
	/**
	 * @param trackingKeepRecentPositions the trackingKeepRecentPositions to set
	 */
	public void setTrackingKeepRecentPositions(Integer trackingKeepRecentPositions) {
		this.trackingKeepRecentPositions = trackingKeepRecentPositions;
	}
	/**
	 * @return the trackingUpdateIntervalInSecs
	 */
	public Integer getTrackingUpdateIntervalInSecs() {
		return trackingUpdateIntervalInSecs;
	}
	/**
	 * @param trackingUpdateIntervalInSecs the trackingUpdateIntervalInSecs to set
	 */
	public void setTrackingUpdateIntervalInSecs(Integer trackingUpdateIntervalInSecs) {
		this.trackingUpdateIntervalInSecs = trackingUpdateIntervalInSecs;
	}
	
	/**
	 * @return the trackingFlyToMode
	 */
	public Integer getTrackingFlyToMode() {
		return trackingFlyToMode;
	}

	/**
	 * @param trackingFlyToMode the trackingFlyToMode to set
	 */
	public void setTrackingFlyToMode(Integer trackingFlyToMode) {
		this.trackingFlyToMode = trackingFlyToMode;
	}

	/**
	 * @return the windowFullscreen
	 */
	public Boolean getWindowFullscreen() {
		return windowFullscreen;
	}
	/**
	 * @param windowFullscreen the windowFullscreen to set
	 */
	public void setWindowFullscreen(Boolean windowFullscreen) {
		this.windowFullscreen = windowFullscreen;
	}
	/**
	 * @return the windowWidth
	 */
	public Integer getWindowWidth() {
		return windowWidth;
	}
	/**
	 * @param windowWidth the windowWidth to set
	 */
	public void setWindowWidth(Integer windowWidth) {
		this.windowWidth = windowWidth;
	}
	/**
	 * @return the windowHeight
	 */
	public Integer getWindowHeight() {
		return windowHeight;
	}
	/**
	 * @param windowHeight the windowHeight to set
	 */
	public void setWindowHeight(Integer windowHeight) {
		this.windowHeight = windowHeight;
	}
	/**
	 * @return the showTrackInfo
	 */
	public Boolean getShowTrackInfo() {
		return showTrackInfo;
	}
	/**
	 * @param showTrackInfo the showTrackInfo to set
	 */
	public void setShowTrackInfo(Boolean showTrackInfo) {
		this.showTrackInfo = showTrackInfo;
	}
	/**
	 * @return the cssStyle
	 */
	public String getCssStyle() {
		return cssStyle;
	}
	/**
	 * @param cssStyle the cssStyle to set
	 */
	public void setCssStyle(String cssStyle) {
		this.cssStyle = cssStyle;
	}
}
