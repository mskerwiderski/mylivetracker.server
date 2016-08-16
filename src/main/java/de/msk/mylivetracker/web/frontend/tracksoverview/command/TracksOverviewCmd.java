package de.msk.mylivetracker.web.frontend.tracksoverview.command;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.TrackingFlyToMode;
import de.msk.mylivetracker.domain.TracksOverviewMapFlyToModeVo;
import de.msk.mylivetracker.domain.TracksViewVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.MapsUsedVo;
import de.msk.mylivetracker.domain.user.RoutesUsedVo;
import de.msk.mylivetracker.domain.user.UserSessionStatusVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor.ActionExecutor;
import de.msk.mylivetracker.web.options.BoolOptionDsc;
import de.msk.mylivetracker.web.options.IntOptionDsc;
import de.msk.mylivetracker.web.options.StrOptionDsc;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * TracksOverviewCmd.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TracksOverviewCmd {	
	public static final String REQUEST_PARAM_ACTION_EXECUTOR = "actionExecutor";
	
	public boolean showVersionInfo;
	public String versionStr;
	public String forumLink;
	
	public String selectedTrackId = null;	
	public String selectedTrackName = null;
	public Boolean selectedTrackActivityStatus = null;
	public Boolean selectedTrackReleaseStatus = null;

	private ActionExecutor actionExecutor = null;
	
	private TracksViewVo selectedTracksView = null;
	private String mapsUsedStr = null;
	private int defMapId = 0;
	private RoutesUsedVo routesUsed= null;
	
	private List<SenderEntry> senderEntriesForCreateTrack = new ArrayList<SenderEntry>();
	private String selectedSenderForCreateTrack = null;
	private List<SenderEntry> senderEntriesForFilter = new ArrayList<SenderEntry>();
	private List<IntOptionDsc> tracksOverviewOptsDatePeriod;
	private Integer selectedDatePeriodFilter = null;
	private String selectedSenderFilter = null;
	private String selectedSearchStrFilter = null;
	
	private List<BoolOptionDsc> liveTrackingOpts;
	private Boolean selectedLiveTrackingOpt = null;	
	private List<IntOptionDsc> liveTrackingOptsKeepRecentPos;
	private Integer selectedLiveTrackingOptKeepRecentPos = null;
	private List<IntOptionDsc> liveTrackingOptsUpdateInterval;
	private Integer selectedLiveTrackingOptUpdateInterval = null;
	private List<StrOptionDsc> liveTrackingOptsFlyToMode;
	private String selectedLiveTrackingOptFlyToMode = null;
	
	private List<StrOptionDsc> tracksOverviewOptsFlyToMode;
	private String selectedTracksOverviewOptFlyToMode = null;
	
	private List<IntOptionDsc> tracksOverviewOptsRefresh;
	private Integer selectedTracksOverviewOptRefresh = null;
	
	private List<BoolOptionDsc> trackOptsReleaseStatus;
	private List<BoolOptionDsc> trackOptsActivityStatus;
	
	private List<IntOptionDsc> supportedMaps;
	
	public UserSessionStatusVo getUserSessionStatus() {
		UserSessionStatusVo userSessionStatus = new UserSessionStatusVo();
		userSessionStatus.setUserId(WebUtils.getCurrentUserWithRole().getUserId());
		userSessionStatus.setToSelSenderForCreateTrack(this.selectedSenderForCreateTrack);
		userSessionStatus.setToSelLiveTrackingOpt(this.selectedLiveTrackingOpt);
		userSessionStatus.setToSelLiveTrackingOptKeepRecentPos(this.selectedLiveTrackingOptKeepRecentPos);
		userSessionStatus.setToSelLiveTrackingOptUpdateInterval(this.selectedLiveTrackingOptUpdateInterval);
		userSessionStatus.setToSelLiveTrackingOptFlyToMode(TrackingFlyToMode.valueOf(this.selectedLiveTrackingOptFlyToMode));
		userSessionStatus.setToSelSenderFilter(this.selectedSenderFilter);
		userSessionStatus.setToSelDatePeriodFilter(this.selectedDatePeriodFilter);
		userSessionStatus.setToSelSearchStrFilter(this.selectedSearchStrFilter);
		userSessionStatus.setToSelTracksView(this.selectedTracksView);
		userSessionStatus.setToSelTracksOverviewOptFlyToMode(TracksOverviewMapFlyToModeVo.valueOf(this.selectedTracksOverviewOptFlyToMode));
		userSessionStatus.setToSelTracksOverviewOptRefresh(this.selectedTracksOverviewOptRefresh);
		return userSessionStatus;
	}
	
	public void init(HttpServletRequest request, ISenderService senderService, 
		UserSessionStatusVo userSessionStatus) {
		UserWithRoleVo user = WebUtils.getCurrentUserWithRole();
		// maps.
		MapsUsedVo mapsUsed = user.getOptions().getMapsUsed();
		this.setMapsUsedStr(mapsUsed.getMapsUsedStr(this.supportedMaps.size()));
		this.setDefMapId(mapsUsed.getDefMapId());
		this.setRoutesUsed(user.getOptions().getRoutesUsed());
		// sender entries (senders for create track, senders for filter).
		List<SenderVo> senders = senderService.getSenders(user.getUsername());
		this.senderEntriesForCreateTrack.clear();
		this.senderEntriesForFilter.clear();
		this.senderEntriesForFilter.add(new SenderEntry(request)); // 'all' entry.
		if ((senders != null) && !senders.isEmpty()) {
			for (int i=0; i < senders.size(); i++) {
				SenderVo sender = senders.get(i);
				if (sender.isActive()) {
					this.senderEntriesForCreateTrack.add(new SenderEntry(sender));
					this.senderEntriesForFilter.add(new SenderEntry(sender));
					if ((this.selectedSenderForCreateTrack == null) && 
						StringUtils.equals(
							sender.getSenderId(), 
							userSessionStatus.getToSelSenderForCreateTrack())) {
						this.selectedSenderForCreateTrack = sender.getSenderId();
					}
					if ((this.selectedSenderFilter == null) && 
						StringUtils.equals(
							sender.getSenderId(), 
							userSessionStatus.getToSelSenderFilter())) {
						this.selectedSenderFilter = sender.getSenderId();
					}
				}
			}	
		}
		if (this.selectedSenderFilter == null) {
			this.selectedSenderFilter = SenderEntry.VALUE_ALL;
		}
		// live tracking options.
		if (this.selectedLiveTrackingOpt == null) {
			this.selectedLiveTrackingOpt = userSessionStatus.getToSelLiveTrackingOpt();
		}
		if (this.selectedLiveTrackingOptKeepRecentPos == null) {
			this.selectedLiveTrackingOptKeepRecentPos = userSessionStatus.getToSelLiveTrackingOptKeepRecentPos();
		}
		if (this.selectedLiveTrackingOptUpdateInterval == null) {
			this.selectedLiveTrackingOptUpdateInterval = userSessionStatus.getToSelLiveTrackingOptUpdateInterval();
		}
		if (this.selectedLiveTrackingOptFlyToMode == null) {
			this.selectedLiveTrackingOptFlyToMode = userSessionStatus.getToSelLiveTrackingOptFlyToMode().name();
		}
		// date period filter
		if (this.selectedDatePeriodFilter == null) {
			this.selectedDatePeriodFilter = userSessionStatus.getToSelDatePeriodFilter();
		}
		// search string filter.
		if (this.selectedSearchStrFilter == null) {
			this.selectedSearchStrFilter = userSessionStatus.getToSelSearchStrFilter();
		}
		// tracks view
		if (this.selectedTracksView == null) {
			this.selectedTracksView = userSessionStatus.getToSelTracksView();
		}
		// zoom mode for senders on map.
		if (this.selectedTracksOverviewOptFlyToMode == null) {
			this.selectedTracksOverviewOptFlyToMode = userSessionStatus.getToSelTracksOverviewOptFlyToMode().name();
		}
		// refresh interval.
		if (this.selectedTracksOverviewOptRefresh == null) {
			this.selectedTracksOverviewOptRefresh = userSessionStatus.getToSelTracksOverviewOptRefresh();
		}
	}
		
	public static boolean requestHasValidActionExecutor(HttpServletRequest request) {
		String actionExecutor = request.getParameter(REQUEST_PARAM_ACTION_EXECUTOR);
		return (!StringUtils.isEmpty(actionExecutor) &&
			(ActionExecutor.isValidActionExecutor(actionExecutor)));		
	}	
	
	/**
	 * @return the selectedTrackId
	 */
	public String getSelectedTrackId() {
		return selectedTrackId;
	}

	/**
	 * @param selectedTrackId the selectedTrackId to set
	 */
	public void setSelectedTrackId(String selectedTrackId) {
		this.selectedTrackId = selectedTrackId;
	}

	/**
	 * @return the selectedTrackName
	 */
	public String getSelectedTrackName() {
		return selectedTrackName;
	}

	/**
	 * @param selectedTrackName the selectedTrackName to set
	 */
	public void setSelectedTrackName(String selectedTrackName) {
		this.selectedTrackName = selectedTrackName;
	}

	/**
	 * @return the selectedTrackActivityStatus
	 */
	public Boolean getSelectedTrackActivityStatus() {
		return selectedTrackActivityStatus;
	}

	/**
	 * @param selectedTrackActivityStatus the selectedTrackActivityStatus to set
	 */
	public void setSelectedTrackActivityStatus(Boolean selectedTrackActivityStatus) {
		this.selectedTrackActivityStatus = selectedTrackActivityStatus;
	}

	/**
	 * @return the selectedTrackReleaseStatus
	 */
	public Boolean getSelectedTrackReleaseStatus() {
		return selectedTrackReleaseStatus;
	}

	/**
	 * @param selectedTrackReleaseStatus the selectedTrackReleaseStatus to set
	 */
	public void setSelectedTrackReleaseStatus(Boolean selectedTrackReleaseStatus) {
		this.selectedTrackReleaseStatus = selectedTrackReleaseStatus;
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

	public TracksViewVo getSelectedTracksView() {
		return selectedTracksView;
	}

	public void setSelectedTracksView(TracksViewVo selectedTracksView) {
		this.selectedTracksView = selectedTracksView;
	}

	/**
	 * @return the mapsUsedStr
	 */
	public String getMapsUsedStr() {
		return mapsUsedStr;
	}

	/**
	 * @param mapsUsedStr the mapsUsedStr to set
	 */
	public void setMapsUsedStr(String mapsUsedStr) {
		this.mapsUsedStr = mapsUsedStr;
	}

	/**
	 * @return the defMapId
	 */
	public int getDefMapId() {
		return defMapId;
	}

	/**
	 * @param defMapId the defMapId to set
	 */
	public void setDefMapId(int defMapId) {
		this.defMapId = defMapId;
	}

	/**
	 * @return the routesUsed
	 */
	public RoutesUsedVo getRoutesUsed() {
		return routesUsed;
	}

	/**
	 * @param routesUsed the routesUsed to set
	 */
	public void setRoutesUsed(RoutesUsedVo routesUsed) {
		this.routesUsed = routesUsed;
	}

	public String getSelectedSenderForCreateTrack() {
		return selectedSenderForCreateTrack;
	}

	public void setSelectedSenderForCreateTrack(String selectedSenderForCreateTrack) {
		this.selectedSenderForCreateTrack = selectedSenderForCreateTrack;
	}

	public List<SenderEntry> getSenderEntriesForCreateTrack() {
		return senderEntriesForCreateTrack;
	}

	public List<SenderEntry> getSenderEntriesForFilter() {
		return senderEntriesForFilter;
	}

	/**
	 * @return the liveTrackingOpts
	 */
	public List<BoolOptionDsc> getLiveTrackingOpts() {
		return liveTrackingOpts;
	}
	/**
	 * @param liveTrackingOpts the liveTrackingOpts to set
	 */
	public void setLiveTrackingOpts(List<BoolOptionDsc> liveTrackingOpts) {
		this.liveTrackingOpts = liveTrackingOpts;
	}
	/**
	 * @return the selectedLiveTrackingOpt
	 */
	public Boolean getSelectedLiveTrackingOpt() {
		return selectedLiveTrackingOpt;
	}
	/**
	 * @param selectedLiveTrackingOpt the selectedLiveTrackingOpt to set
	 */
	public void setSelectedLiveTrackingOpt(Boolean selectedLiveTrackingOpt) {
		this.selectedLiveTrackingOpt = selectedLiveTrackingOpt;
	}
	/**
	 * @return the selectedLiveTrackingOptKeepRecentPos
	 */
	public Integer getSelectedLiveTrackingOptKeepRecentPos() {
		return selectedLiveTrackingOptKeepRecentPos;
	}

	/**
	 * @param selectedLiveTrackingOptKeepRecentPos the selectedLiveTrackingOptKeepRecentPos to set
	 */
	public void setSelectedLiveTrackingOptKeepRecentPos(
			Integer selectedLiveTrackingOptKeepRecentPos) {
		this.selectedLiveTrackingOptKeepRecentPos = selectedLiveTrackingOptKeepRecentPos;
	}

	/**
	 * @return the liveTrackingOptsKeepRecentPos
	 */
	public List<IntOptionDsc> getLiveTrackingOptsKeepRecentPos() {
		return liveTrackingOptsKeepRecentPos;
	}

	/**
	 * @param liveTrackingOptsKeepRecentPos the liveTrackingOptsKeepRecentPos to set
	 */
	public void setLiveTrackingOptsKeepRecentPos(
			List<IntOptionDsc> liveTrackingOptsKeepRecentPos) {
		this.liveTrackingOptsKeepRecentPos = liveTrackingOptsKeepRecentPos;
	}

	/**
	 * @return the liveTrackingOptsUpdateInterval
	 */
	public List<IntOptionDsc> getLiveTrackingOptsUpdateInterval() {
		return liveTrackingOptsUpdateInterval;
	}

	/**
	 * @param liveTrackingOptsUpdateInterval the liveTrackingOptsUpdateInterval to set
	 */
	public void setLiveTrackingOptsUpdateInterval(
			List<IntOptionDsc> liveTrackingOptsUpdateInterval) {
		this.liveTrackingOptsUpdateInterval = liveTrackingOptsUpdateInterval;
	}

	/**
	 * @return the selectedLiveTrackingOptUpdateInterval
	 */
	public Integer getSelectedLiveTrackingOptUpdateInterval() {
		return selectedLiveTrackingOptUpdateInterval;
	}

	/**
	 * @param selectedLiveTrackingOptUpdateInterval the selectedLiveTrackingOptUpdateInterval to set
	 */
	public void setSelectedLiveTrackingOptUpdateInterval(
			Integer selectedLiveTrackingOptUpdateInterval) {
		this.selectedLiveTrackingOptUpdateInterval = selectedLiveTrackingOptUpdateInterval;
	}	

	public List<StrOptionDsc> getTracksOverviewOptsFlyToMode() {
		return tracksOverviewOptsFlyToMode;
	}

	public void setTracksOverviewOptsFlyToMode(
			List<StrOptionDsc> tracksOverviewOptsFlyToMode) {
		this.tracksOverviewOptsFlyToMode = tracksOverviewOptsFlyToMode;
	}

	public String getSelectedTracksOverviewOptFlyToMode() {
		return selectedTracksOverviewOptFlyToMode;
	}

	public void setSelectedTracksOverviewOptFlyToMode(
			String selectedTracksOverviewOptFlyToMode) {
		this.selectedTracksOverviewOptFlyToMode = selectedTracksOverviewOptFlyToMode;
	}

	public List<IntOptionDsc> getTracksOverviewOptsDatePeriod() {
		return tracksOverviewOptsDatePeriod;
	}

	public void setTracksOverviewOptsDatePeriod(
			List<IntOptionDsc> tracksOverviewOptsDatePeriod) {
		this.tracksOverviewOptsDatePeriod = tracksOverviewOptsDatePeriod;
	}

	public Integer getSelectedDatePeriodFilter() {
		return selectedDatePeriodFilter;
	}

	public void setSelectedDatePeriodFilter(Integer selectedDatePeriodFilter) {
		this.selectedDatePeriodFilter = selectedDatePeriodFilter;
	}

	/**
	 * @return the tracksOverviewOptsRefresh
	 */
	public List<IntOptionDsc> getTracksOverviewOptsRefresh() {
		return tracksOverviewOptsRefresh;
	}

	/**
	 * @param tracksOverviewOptsRefresh the tracksOverviewOptsRefresh to set
	 */
	public void setTracksOverviewOptsRefresh(
			List<IntOptionDsc> tracksOverviewOptsRefresh) {
		this.tracksOverviewOptsRefresh = tracksOverviewOptsRefresh;
	}

	public Integer getSelectedTracksOverviewOptRefresh() {
		return selectedTracksOverviewOptRefresh;
	}

	public void setSelectedTracksOverviewOptRefresh(
			Integer selectedTracksOverviewOptRefresh) {
		this.selectedTracksOverviewOptRefresh = selectedTracksOverviewOptRefresh;
	}

	public List<StrOptionDsc> getLiveTrackingOptsFlyToMode() {
		return liveTrackingOptsFlyToMode;
	}

	public void setLiveTrackingOptsFlyToMode(
			List<StrOptionDsc> liveTrackingOptsFlyToMode) {
		this.liveTrackingOptsFlyToMode = liveTrackingOptsFlyToMode;
	}

	public String getSelectedLiveTrackingOptFlyToMode() {
		return selectedLiveTrackingOptFlyToMode;
	}

	public void setSelectedLiveTrackingOptFlyToMode(
			String selectedLiveTrackingOptFlyToMode) {
		this.selectedLiveTrackingOptFlyToMode = selectedLiveTrackingOptFlyToMode;
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
	 * @return the trackOptsActivityStatus
	 */
	public List<BoolOptionDsc> getTrackOptsActivityStatus() {
		return trackOptsActivityStatus;
	}

	/**
	 * @param trackOptsActivityStatus the trackOptsActivityStatus to set
	 */
	public void setTrackOptsActivityStatus(
			List<BoolOptionDsc> trackOptsActivityStatus) {
		this.trackOptsActivityStatus = trackOptsActivityStatus;
	}

	public String getSelectedSenderFilter() {
		return selectedSenderFilter;
	}

	public void setSelectedSenderFilter(String selectedSenderFilter) {
		this.selectedSenderFilter = selectedSenderFilter;
	}

	/**
	 * @return the selectedSearchStrFilter
	 */
	public String getSelectedSearchStrFilter() {
		return selectedSearchStrFilter;
	}

	/**
	 * @param selectedSearchStrFilter the selectedSearchStrFilter to set
	 */
	public void setSelectedSearchStrFilter(String selectedSearchStrFilter) {
		if (!StringUtils.isEmpty(selectedSearchStrFilter)) {
			if (StringUtils.containsOnly(selectedSearchStrFilter, " ")) {
				selectedSearchStrFilter = "";
			}
		}
		this.selectedSearchStrFilter = selectedSearchStrFilter;
	}

	public List<IntOptionDsc> getSupportedMaps() {
		return supportedMaps;
	}

	public void setSupportedMaps(List<IntOptionDsc> supportedMaps) {
		this.supportedMaps = supportedMaps;
	}

	/**
	 * @return the showVersionInfo
	 */
	public boolean isShowVersionInfo() {
		return showVersionInfo;
	}

	/**
	 * @param showVersionInfo the showVersionInfo to set
	 */
	public void setShowVersionInfo(boolean showVersionInfo) {
		this.showVersionInfo = showVersionInfo;
	}

	/**
	 * @return the versionStr
	 */
	public String getVersionStr() {
		return versionStr;
	}

	/**
	 * @param versionStr the versionStr to set
	 */
	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}

	/**
	 * @return the forumLink
	 */
	public String getForumLink() {
		return forumLink;
	}

	/**
	 * @param forumLink the forumLink to set
	 */
	public void setForumLink(String forumLink) {
		this.forumLink = forumLink;
	}
}
