package de.msk.mylivetracker.web.frontend.options;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.sender.SenderSwitchesVo;
import de.msk.mylivetracker.domain.sender.SenderSymbol;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.UserAutoLoginVo;
import de.msk.mylivetracker.domain.user.UserEmergencyVo;
import de.msk.mylivetracker.domain.user.UserMasterDataVo;
import de.msk.mylivetracker.domain.user.UserOptionsVo;
import de.msk.mylivetracker.domain.user.UserStatusPageVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.web.frontend.options.actionexecutor.ActionExecutor;
import de.msk.mylivetracker.web.options.BoolOptionDsc;
import de.msk.mylivetracker.web.options.IntOptionDsc;
import de.msk.mylivetracker.web.options.StrOptionDsc;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * OptionsCmd.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class OptionsCmd {

	private static final String MESSAGE_CODE_NO_SENDER_SELECTED = "sendermaintenance.no.sender.selected";
	
	private int currentTabId = 0;
	private String[] infoMessage = new String[] {"", "", "", "", "", ""};
	
	private List<StrOptionDsc> userOptsLanguage;
	private List<StrOptionDsc> userOptsGeocoder;
	private List<BoolOptionDsc> trackOptsReleaseStatus;
	private List<IntOptionDsc> trackOptsAutoClose;
	private List<IntOptionDsc> trackRouteOptsWidth;	
	private List<BoolOptionDsc> commonsOptsYesNo;
	private List<BoolOptionDsc> commonsOptsOnOff;
	private List<BoolOptionDsc> commonsOptsEnDisabled;
	private List<StrOptionDsc> commonsOptsTimeZone;
	private List<StrOptionDsc> senderOptsRadiusUnit;
	private List<StrOptionDsc> senderOptsRunningMode;
	
	private List<StrOptionDsc> stPgTrOptsFlyToMode;
	private List<IntOptionDsc> stPgTrOptsKeepRecentPos;
	private List<IntOptionDsc> stPgTrOptsUpdateInterval;
	
	private List<IntOptionDsc> supportedMaps;
	private ServerInfo serverInfo;
	
	private UserMasterDataVo userMasterData;	
	private UserAutoLoginVo userAutoLogin;
	private UserOptionsVo userOptions;
	private String homeLocLatitudeStr;
	private String homeLocLongitudeStr;
	
	private UserStatusPageVo userStatusPage;
	private UserEmergencyVo userEmergency;
	private int senderLimit;
	private List<SenderEntry> senderEntries;
	private List<SenderEntry> redirectEntries;
	private List<SymbolEntry> symbolEntries;
	private Map<String, SenderVo> senderMap;
	private String selectedSenderId;
	private SenderVo senderDetails;
	private String retypedPassword;
	private ActionExecutor actionExecutor;

	private String supportedSenderSwitches = SenderSwitchesVo.supported();
	
	private String autoLoginUrlForUser;
	private String autoLoginUrlForGuest;

	private boolean statusParamsIdExists;
	private String iframeTrackAsStatusInfo;
	private String iframeTrackAsMap;
	private String urlTrackAsStatusInfo;
	private String urlTrackAsMap;
	
	private static final String IFRAME_TEMPLATE =
		"<iframe style=\"width:$WIDTH$;height:$HEIGHT$\" " +
		"frameborder=\"0\" scrolling=\"no\" " +	
		"marginheight=\"0\" marginwidth=\"0\" " + 
		"src=\"$SRC$\">$NOTSUPPORTED$</iframe>";
	
	public void buildUpLinks(HttpServletRequest request,
		String applicationBaseUrl, UserWithoutRoleVo user) {
		
		autoLoginUrlForUser = UserAutoLoginVo.createAutoLoginUrl(
			applicationBaseUrl, userAutoLogin.getAutoLoginTicketForUser());
		autoLoginUrlForGuest = UserAutoLoginVo.createAutoLoginUrl(
			applicationBaseUrl, userAutoLogin.getAutoLoginTicketForGuest());
		
		if (StringUtils.isEmpty(user.getStatusPage().getLastParamsId())) {
			this.statusParamsIdExists = false;
			String infoMsg = 
				WebUtils.getMessage(request, "statuspage.no.paramsid.exists");
			this.iframeTrackAsStatusInfo = infoMsg;
			this.iframeTrackAsMap = infoMsg;
			this.urlTrackAsStatusInfo = infoMsg;
			this.urlTrackAsMap = infoMsg;
		} else {
			this.statusParamsIdExists = true;
			this.urlTrackAsStatusInfo = 
				UserStatusPageVo.createStatusPageUrlForStatusInfo(
					applicationBaseUrl, 
					user.getStatusPage().getLastParamsId());
			this.urlTrackAsMap = 
				UserStatusPageVo.createStatusPageUrlForMap(
					applicationBaseUrl, 
					user.getStatusPage().getLastParamsId());	
			String width = (user.getStatusPage().getWindowWidth() == null ? 
				"null" : user.getStatusPage().getWindowWidth().toString() + "px");
			String height = (user.getStatusPage().getWindowHeight() == null ?
				"null" : user.getStatusPage().getWindowHeight().toString() + "px");
			if (user.getStatusPage().getFullScreen()) {
				width = "100%";
				height = "100%";
			}
			String snippet = StringUtils.replace(
				IFRAME_TEMPLATE, "$WIDTH$", width);
			snippet = StringUtils.replace(snippet, "$HEIGHT$", height);
			snippet = StringUtils.replace(snippet, "$NOTSUPPORTED$", 
				WebUtils.getMessage(request, "statuspage.iframe.not.supported"));
			this.iframeTrackAsStatusInfo = StringUtils.replace(
				snippet, "$SRC$", this.urlTrackAsStatusInfo);
			this.iframeTrackAsMap = StringUtils.replace(
				snippet, "$SRC$", this.urlTrackAsMap);
		}
	}
	
	public void buildUpSenderEntries(HttpServletRequest request, 
		List<SenderVo> senders) {
		this.senderEntries = new ArrayList<SenderEntry>();
		this.redirectEntries = new ArrayList<SenderEntry>();
		this.senderMap = new HashMap<String, SenderVo>();
		this.senderEntries.add(
			new SenderEntry(null, 
				WebUtils.getMessage(request, MESSAGE_CODE_NO_SENDER_SELECTED)));
		this.redirectEntries.add(
			new SenderEntry(null, 
				WebUtils.getMessage(request, MESSAGE_CODE_NO_SENDER_SELECTED)));
		for (SenderVo sender : senders) {
			this.senderEntries.add(new SenderEntry(sender, null));
			if (StringUtils.isEmpty(selectedSenderId) ||
				!selectedSenderId.equals(sender.getSenderId())) {
				this.redirectEntries.add(new SenderEntry(sender, null));
			}
			this.senderMap.put(sender.getSenderId(), sender);
		}
	}
	
	public void buildUpSymbolEntries(HttpServletRequest request, 
		List<SenderVo> senders) {
		this.symbolEntries = new ArrayList<SymbolEntry>();
		for (SenderSymbol carrier : SenderSymbol.values()) {
			this.symbolEntries.add(new SymbolEntry(request, carrier));
		}
	}
	
	/**
	 * @return the currentTabId
	 */
	public int getCurrentTabId() {
		return currentTabId;
	}
	/**
	 * @param currentTabId the currentTabId to set
	 */
	public void setCurrentTabId(int currentTabId) {
		this.currentTabId = currentTabId;
	}
	/**
	 * @return the infoMessage
	 */
	public String getInfoMessage() {
		return infoMessage[this.currentTabId];
	}
	/**
	 * @param infoMessage the infoMessage to set
	 */
	public void setInfoMessage(String infoMessage) {
		this.infoMessage[this.currentTabId] = infoMessage;
	}
	/**
	 * @return the userOptsLanguage
	 */
	public List<StrOptionDsc> getUserOptsLanguage() {
		return userOptsLanguage;
	}
	/**
	 * @param userOptsLanguage the userOptsLanguage to set
	 */
	public void setUserOptsLanguage(List<StrOptionDsc> userOptsLanguage) {
		this.userOptsLanguage = userOptsLanguage;
	}
	/**
	 * @return the userOptsGeocoder
	 */
	public List<StrOptionDsc> getUserOptsGeocoder() {
		return userOptsGeocoder;
	}

	/**
	 * @param userOptsGeocoder the userOptsGeocoder to set
	 */
	public void setUserOptsGeocoder(List<StrOptionDsc> userOptsGeocoder) {
		this.userOptsGeocoder = userOptsGeocoder;
	}

	/**
	 * @return the trackOptsReleaseStatus
	 */
	public List<BoolOptionDsc> getTrackOptsReleaseStatus() {
		return trackOptsReleaseStatus;
	}
	/**
	 * @param trackOptsReleaseStatus the trackOptsReleaseStatus to set
	 */
	public void setTrackOptsReleaseStatus(List<BoolOptionDsc> trackOptsReleaseStatus) {
		this.trackOptsReleaseStatus = trackOptsReleaseStatus;
	}	
	/**
	 * @return the trackOptsAutoClose
	 */
	public List<IntOptionDsc> getTrackOptsAutoClose() {
		return trackOptsAutoClose;
	}
	/**
	 * @param trackOptsAutoClose the trackOptsAutoClose to set
	 */
	public void setTrackOptsAutoClose(List<IntOptionDsc> trackOptsAutoClose) {
		this.trackOptsAutoClose = trackOptsAutoClose;
	}
	/**
	 * @return the trackRouteOptsWidth
	 */
	public List<IntOptionDsc> getTrackRouteOptsWidth() {
		return trackRouteOptsWidth;
	}
	/**
	 * @param trackRouteOptsWidth the trackRouteOptsWidth to set
	 */
	public void setTrackRouteOptsWidth(List<IntOptionDsc> trackRouteOptsWidth) {
		this.trackRouteOptsWidth = trackRouteOptsWidth;
	}

	public List<StrOptionDsc> getStPgTrOptsFlyToMode() {
		return stPgTrOptsFlyToMode;
	}

	public void setStPgTrOptsFlyToMode(List<StrOptionDsc> stPgTrOptsFlyToMode) {
		this.stPgTrOptsFlyToMode = stPgTrOptsFlyToMode;
	}

	/**
	 * @return the stPgTrOptsKeepRecentPos
	 */
	public List<IntOptionDsc> getStPgTrOptsKeepRecentPos() {
		return stPgTrOptsKeepRecentPos;
	}

	/**
	 * @param stPgTrOptsKeepRecentPos the stPgTrOptsKeepRecentPos to set
	 */
	public void setStPgTrOptsKeepRecentPos(
			List<IntOptionDsc> stPgTrOptsKeepRecentPos) {
		this.stPgTrOptsKeepRecentPos = stPgTrOptsKeepRecentPos;
	}
	
	/**
	 * @return the stPgTrOptsUpdateInterval
	 */
	public List<IntOptionDsc> getStPgTrOptsUpdateInterval() {
		return stPgTrOptsUpdateInterval;
	}

	/**
	 * @param stPgTrOptsUpdateInterval the stPgTrOptsUpdateInterval to set
	 */
	public void setStPgTrOptsUpdateInterval(
			List<IntOptionDsc> stPgTrOptsUpdateInterval) {
		this.stPgTrOptsUpdateInterval = stPgTrOptsUpdateInterval;
	}	

	/**
	 * @return the supportedMaps
	 */
	public List<IntOptionDsc> getSupportedMaps() {
		return supportedMaps;
	}

	/**
	 * @param supportedMaps the supportedMaps to set
	 */
	public void setSupportedMaps(List<IntOptionDsc> supportedMaps) {
		this.supportedMaps = supportedMaps;
	}

	public ServerInfo getServerInfo() {
		return serverInfo;
	}

	public void setServerInfo(ServerInfo serverInfo) {
		this.serverInfo = serverInfo;
	}

	/**
	 * @return the commonsOptsYesNo
	 */
	public List<BoolOptionDsc> getCommonsOptsYesNo() {
		return commonsOptsYesNo;
	}
	/**
	 * @param commonsOptsYesNo the commonsOptsYesNo to set
	 */
	public void setCommonsOptsYesNo(List<BoolOptionDsc> commonsOptsYesNo) {
		this.commonsOptsYesNo = commonsOptsYesNo;
	}
	
	/**
	 * @return the commonsOptsOnOff
	 */
	public List<BoolOptionDsc> getCommonsOptsOnOff() {
		return commonsOptsOnOff;
	}

	/**
	 * @param commonsOptsOnOff the commonsOptsOnOff to set
	 */
	public void setCommonsOptsOnOff(List<BoolOptionDsc> commonsOptsOnOff) {
		this.commonsOptsOnOff = commonsOptsOnOff;
	}

	/**
	 * @return the commonsOptsEnDisabled
	 */
	public List<BoolOptionDsc> getCommonsOptsEnDisabled() {
		return commonsOptsEnDisabled;
	}

	/**
	 * @param commonsOptsEnDisabled the commonsOptsEnDisabled to set
	 */
	public void setCommonsOptsEnDisabled(List<BoolOptionDsc> commonsOptsEnDisabled) {
		this.commonsOptsEnDisabled = commonsOptsEnDisabled;
	}

	/**
	 * @return the commonsOptsTimeZone
	 */
	public List<StrOptionDsc> getCommonsOptsTimeZone() {
		return commonsOptsTimeZone;
	}

	/**
	 * @param commonsOptsTimeZone the commonsOptsTimeZone to set
	 */
	public void setCommonsOptsTimeZone(List<StrOptionDsc> commonsOptsTimeZone) {
		this.commonsOptsTimeZone = commonsOptsTimeZone;
	}

	public List<StrOptionDsc> getSenderOptsRadiusUnit() {
		return senderOptsRadiusUnit;
	}

	public void setSenderOptsRadiusUnit(List<StrOptionDsc> senderOptsRadiusUnit) {
		this.senderOptsRadiusUnit = senderOptsRadiusUnit;
	}

	/**
	 * @return the senderOptsRunningMode
	 */
	public List<StrOptionDsc> getSenderOptsRunningMode() {
		return senderOptsRunningMode;
	}
	/**
	 * @param senderOptsRunningMode the senderOptsRunningMode to set
	 */
	public void setSenderOptsRunningMode(List<StrOptionDsc> senderOptsRunningMode) {
		this.senderOptsRunningMode = senderOptsRunningMode;
	}	
	/**
	 * @return the userMasterData
	 */
	public UserMasterDataVo getUserMasterData() {
		return userMasterData;
	}
	/**
	 * @param userMasterData the userMasterData to set
	 */
	public void setUserMasterData(UserMasterDataVo userMasterData) {
		this.userMasterData = userMasterData;
	}
	
	public UserAutoLoginVo getUserAutoLogin() {
		return userAutoLogin;
	}

	public void setUserAutoLogin(UserAutoLoginVo userAutoLogin) {
		this.userAutoLogin = userAutoLogin;
	}

	/**
	 * @return the userOptions
	 */
	public UserOptionsVo getUserOptions() {
		return userOptions;
	}
	/**
	 * @param userOptions the userOptions to set
	 */
	public void setUserOptions(UserOptionsVo userOptions) {
		this.userOptions = userOptions;
	}				
	public String getHomeLocLatitudeStr() {
		return homeLocLatitudeStr;
	}
	public void setHomeLocLatitudeStr(String homeLocLatitudeStr) {
		this.homeLocLatitudeStr = homeLocLatitudeStr;
	}
	public String getHomeLocLongitudeStr() {
		return homeLocLongitudeStr;
	}

	public void setHomeLocLongitudeStr(String homeLocLongitudeStr) {
		this.homeLocLongitudeStr = homeLocLongitudeStr;
	}

	/**
	 * @return the userStatusPage
	 */
	public UserStatusPageVo getUserStatusPage() {
		return userStatusPage;
	}

	/**
	 * @param userStatusPage the userStatusPage to set
	 */
	public void setUserStatusPage(UserStatusPageVo userStatusPage) {
		this.userStatusPage = userStatusPage;
	}

	/**
	 * @return the userEmergency
	 */
	public UserEmergencyVo getUserEmergency() {
		return userEmergency;
	}

	/**
	 * @param userEmergency the userEmergency to set
	 */
	public void setUserEmergency(UserEmergencyVo userEmergency) {
		this.userEmergency = userEmergency;
	}

	/**
	 * @return the senderEntries
	 */
	public List<SenderEntry> getSenderEntries() {
		return senderEntries;
	}
	/**
	 * @param senderEntries the senderEntries to set
	 */
	public void setSenderEntries(List<SenderEntry> senderEntries) {
		this.senderEntries = senderEntries;
	}
	/**
	 * @return the redirectEntries
	 */
	public List<SenderEntry> getRedirectEntries() {
		return redirectEntries;
	}

	/**
	 * @param redirectEntries the redirectEntries to set
	 */
	public void setRedirectEntries(List<SenderEntry> redirectEntries) {
		this.redirectEntries = redirectEntries;
	}

	public List<SymbolEntry> getSymbolEntries() {
		return symbolEntries;
	}

	public void setSymbolEntries(List<SymbolEntry> symbolEntries) {
		this.symbolEntries = symbolEntries;
	}

	/**
	 * @return the senderMap
	 */
	public Map<String, SenderVo> getSenderMap() {
		return senderMap;
	}

	/**
	 * @param senderMap the senderMap to set
	 */
	public void setSenderMap(Map<String, SenderVo> senderMap) {
		this.senderMap = senderMap;
	}

	/**
	 * @return the selectedSenderId
	 */
	public String getSelectedSenderId() {
		return selectedSenderId;
	}
	/**
	 * @param selectedSenderId the selectedSenderId to set
	 */
	public void setSelectedSenderId(String selectedSenderId) {
		this.selectedSenderId = selectedSenderId;
	}
	/**
	 * @return the senderDetails
	 */
	public SenderVo getSenderDetails() {
		return senderDetails;
	}
	/**
	 * @param senderDetails the senderDetails to set
	 */
	public void setSenderDetails(SenderVo senderDetails) {
		this.senderDetails = senderDetails;
	}
	/**
	 * @return the retypedPassword
	 */
	public String getRetypedPassword() {
		return retypedPassword;
	}
	/**
	 * @param retypedPassword the retypedPassword to set
	 */
	public void setRetypedPassword(String retypedPassword) {
		this.retypedPassword = retypedPassword;
	}
	
	/**
	 * @return the actionExecutor
	 */
	public ActionExecutor getActionExecutor() {
		return actionExecutor;
	}
	/**
	 * @param actionExecutor the actionExecutor to set
	 */
	public void setActionExecutor(ActionExecutor actionExecutor) {
		this.actionExecutor = actionExecutor;
	}	
	public boolean isStatusParamsIdExists() {
		return statusParamsIdExists;
	}
	public String getIframeTrackAsStatusInfo() {
		return iframeTrackAsStatusInfo;
	}
	public String getIframeTrackAsMap() {
		return iframeTrackAsMap;
	}
	public String getUrlTrackAsStatusInfo() {
		return urlTrackAsStatusInfo;
	}
	public String getUrlTrackAsMap() {
		return urlTrackAsMap;
	}
	public String getAutoLoginUrlForUser() {
		return autoLoginUrlForUser;
	}
	public String getAutoLoginUrlForGuest() {
		return autoLoginUrlForGuest;
	}

	/**
	 * @return the senderLimit
	 */
	public int getSenderLimit() {
		return senderLimit;
	}

	/**
	 * @param senderLimit the senderLimit to set
	 */
	public void setSenderLimit(int senderLimit) {
		this.senderLimit = senderLimit;
	}

	/**
	 * @return the supportedSenderSwitches
	 */
	public String getSupportedSenderSwitches() {
		return supportedSenderSwitches;
	}				
}
