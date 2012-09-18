package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

import de.msk.mylivetracker.domain.StatusParamsVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.IStatusParamsService;
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
	private Integer trackingFlyToMode;
	private Integer windowWidth;
	private Boolean fullScreen;
	private Integer windowHeight;	
	private Boolean showTrackInfo;
	private String cssStyle;
	private String linkTrackAsStatusInfo;
	private String linkTrackAsMap;
			
	public void setDefaultValues(IStatusParamsService urlService, 
		IApplicationService applicationService, UserWithoutRoleVo user) {		
		this.statusPageSaved = true;
		this.senderId = null;
		this.trackingLive = true;
		this.trackingKeepRecentPositions = -1;
		this.trackingUpdateIntervalInSecs = 30;
		this.trackingFlyToMode = StatusParamsVo.TrackingFlyToMode.None.ordinal();
		this.fullScreen = false;
		this.windowWidth = 260;
		this.windowHeight = 260;		
		this.showTrackInfo = true;
		this.setStyleToDefaultValue();
		this.generateLinks(urlService, applicationService, user);
	}
	
	public void generateLinks(IStatusParamsService statusParamsService, 
		IApplicationService applicationService, UserWithoutRoleVo user) {
		StatusParamsVo statusParams = StatusParamsVo.createInstance();
		statusParams.setUserId(user.getUserId());
		statusParams.setTicketId(user.getOptions().getRecTrAccCode());
		statusParams.setSenderId(this.getSenderId());
		statusParams.setTrackingLive(this.getTrackingLive());
		statusParams.setTrackingKeepRecentPositions(this.getTrackingKeepRecentPositions());
		statusParams.setTrackingUpdateIntervalInSecs(this.getTrackingUpdateIntervalInSecs());
		statusParams.setTrackingFlyToMode(this.getTrackingFlyToMode());
		statusParams.setWindowFullscreen(this.getFullScreen());
		statusParams.setShowTrackInfo(this.getShowTrackInfo());
		statusParams.setCssStyle(this.getCssStyle());
		statusParams.setWindowWidth(this.getWindowWidth());
		statusParams.setWindowHeight(this.getWindowHeight());
		
		statusParamsService.saveStatusParams(statusParams);
		
		String appBaseUrl = applicationService.getApplicationBaseUrl();
		this.setLinkTrackAsStatusInfo( 				
			ReqUrlStr.create(appBaseUrl, UrlUtils.URL_TRACK_AS_STATUS_INFO_CTRL)
				.addParamValue(AbstractTrackingCtrl.PARAM_PARAMS_ID, 
					statusParams.getStatusParamsId())
				.toString());
		this.setLinkTrackAsMap(
			ReqUrlStr.create(appBaseUrl, UrlUtils.URL_TRACK_AS_MAP_CTRL)
				.addParamValue(AbstractTrackingCtrl.PARAM_PARAMS_ID, 
					statusParams.getStatusParamsId())
				.toString());				
	}
	
	private static final String CRLF = "\r\n";
	public void setStyleToDefaultValue() {
		this.cssStyle = 
			".track-status-content { background: #d4d0c8; font-family: Verdana,Arial,sans-serif; font-variant: small-caps; font-size: x-small;}" + CRLF +
			".track-status-username { color: red; font-weight: bold; }" + CRLF + 
			".track-status-trackname { color: green; font-weight: bold; }" + CRLF +	
			".track-status-noactivetrack { color: red; font-weight: bold }" + CRLF +
			".track-status-labels { color: blue; font-size: small; }" + CRLF +
			".track-status-values { color: #125be2; font-size: small; }" + CRLF +
			".track-status-footer { color: white; font-size: 10px; font-variant: normal; }";
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

	/**
	 * @return the linkTrackAsStatusInfo
	 */
	public String getLinkTrackAsStatusInfo() {
		return linkTrackAsStatusInfo;
	}

	/**
	 * @param linkTrackAsStatusInfo the linkTrackAsStatusInfo to set
	 */
	public void setLinkTrackAsStatusInfo(String linkTrackAsStatusInfo) {
		this.linkTrackAsStatusInfo = linkTrackAsStatusInfo;
	}

	public String getLinkTrackAsMap() {
		return linkTrackAsMap;
	}

	public void setLinkTrackAsMap(String linkTrackAsMap) {
		this.linkTrackAsMap = linkTrackAsMap;
	}
}
