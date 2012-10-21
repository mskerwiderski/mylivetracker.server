package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

import de.msk.mylivetracker.domain.TrackingFlyToMode;
import de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl;
import de.msk.mylivetracker.web.util.UrlUtils;
import de.msk.mylivetracker.web.util.request.ReqUrlStr;

/**
 * UserStatusPageVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UserStatusPageVo implements Cloneable, Serializable {
	
	private static final long serialVersionUID = 9145910797336109599L;
	
	private Boolean statusPageSaved;
	
	private String senderId;
	private Boolean trackingLive;
	private Integer trackingKeepRecentPositions;
	private Integer trackingUpdateIntervalInSecs;
	private TrackingFlyToMode trackingFlyToMode;
	private Integer windowWidth;
	private Boolean fullScreen;
	private Integer windowHeight;	
	private Boolean showTrackInfo;
	private String cssStyle;
	private String lastParamsId;
		
	public static String createStatusPageUrlForStatusInfo(
		String applicationBaseUrl, String statusParamsId) {
		return ReqUrlStr.
			create(applicationBaseUrl, UrlUtils.URL_TRACK_AS_STATUS_INFO_CTRL).
			addParamValue(AbstractTrackingCtrl.PARAM_PARAMS_ID, statusParamsId).
			toString();
	}
	
	public static String createStatusPageUrlForMap(
		String applicationBaseUrl, String statusParamsId) {
		return ReqUrlStr.
			create(applicationBaseUrl, UrlUtils.URL_TRACK_AS_MAP_CTRL).
			addParamValue(AbstractTrackingCtrl.PARAM_PARAMS_ID, statusParamsId).
			toString();
	}
		
	public UserStatusPageVo copy() {
		UserStatusPageVo statusPage = null;
		try {
			statusPage = (UserStatusPageVo)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return statusPage;
	}
	
	/**
	 * @return the statusPageSaved
	 */
	public Boolean getStatusPageSaved() {
		return statusPageSaved;
	}

	/**
	 * @param statusPageSaved the statusPageSaved to set
	 */
	public void setStatusPageSaved(Boolean statusPageSaved) {
		this.statusPageSaved = statusPageSaved;
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
	
	public TrackingFlyToMode getTrackingFlyToMode() {
		return trackingFlyToMode;
	}

	public void setTrackingFlyToMode(TrackingFlyToMode trackingFlyToMode) {
		this.trackingFlyToMode = trackingFlyToMode;
	}

	/**
	 * @return the fullScreen
	 */
	public Boolean getFullScreen() {
		return fullScreen;
	}

	/**
	 * @param fullScreen the fullScreen to set
	 */
	public void setFullScreen(Boolean fullScreen) {
		this.fullScreen = fullScreen;
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
	public String getLastParamsId() {
		return lastParamsId;
	}
	public void setLastParamsId(String lastParamsId) {
		this.lastParamsId = lastParamsId;
	}
}
