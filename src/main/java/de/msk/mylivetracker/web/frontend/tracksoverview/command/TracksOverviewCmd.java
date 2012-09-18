package de.msk.mylivetracker.web.frontend.tracksoverview.command;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.StatusParamsVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.IUserService;
import de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor.ActionExecutor;
import de.msk.mylivetracker.web.options.BoolOptionDsc;
import de.msk.mylivetracker.web.options.IntOptionDsc;
import de.msk.mylivetracker.web.options.StrOptionDsc;
import de.msk.mylivetracker.web.util.FmtUtils;

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
	private static final Log log = LogFactory.getLog(TracksOverviewCmd.class);
	
	public static final String REQUEST_PARAM_ACTION_EXECUTOR = "actionExecutor";
	public static final StrOptionDsc STR_OPTION_VALUE_ALL = new StrOptionDsc("all", "common.opts.all");
	
	public String selectedTrackId = null;	
	public String selectedTrackName = null;
	public Boolean selectedTrackActivityStatus = null;
	public Boolean selectedTrackReleaseStatus = null;

	private ActionExecutor actionExecutor = null;
	
	public enum TracksView {
		Table, Map
	}
	private TracksView tracksView = TracksView.Table;
	private String cloudmadeApiKey = null;
	private String mapsUsedStr = null;
	private int defMapId = 0;
	
	private Integer selectedSenderEntryIdx = null;
	
	private List<SenderEntry> senderEntries = new ArrayList<SenderEntry>();
	
	private List<StrOptionDsc> senderFilterOptions = new ArrayList<StrOptionDsc>();
	private String selectedSenderFilter = null;
	private DateTime selectedDateFromFilter = null;
	private DateTime selectedDateToFilter = null;
	private String selectedSearchStrFilter = null;
	
	private List<BoolOptionDsc> liveTrackingOpts;
	private Boolean selectedLiveTrackingOpt = true;	
	private List<IntOptionDsc> liveTrackingOptsKeepRecentPos;
	private Integer selectedLiveTrackingOptKeepRecentPos;
	private List<IntOptionDsc> liveTrackingOptsUpdateInterval;
	private Integer selectedLiveTrackingOptUpdateInterval;
	
	private List<IntOptionDsc> liveTrackingOptsFlyToMode;
	private Integer selectedLiveTrackingOptFlyToMode = StatusParamsVo.TrackingFlyToMode.None.ordinal();
	
	private List<IntOptionDsc> tracksOverviewOptsRefresh;
	private Integer selectedTracksOverviewOptsRefresh;
	
	private List<BoolOptionDsc> trackOptsReleaseStatus;
	private List<BoolOptionDsc> trackOptsActivityStatus;
	
	public void buildUpSenderEntries(List<SenderVo> senders) {
		senderEntries.clear();
		for (SenderVo sender : senders) {
			if (sender.isActive()) {
				senderEntries.add(new SenderEntry(sender));
			}
		}	
	}
	
	public void buildUpTrackFilters(UserWithoutRoleVo user,
		IUserService userService, ISenderService senderService) {		
		List<SenderVo> senders = senderService.getSenders(user.getUserId());
		senderFilterOptions.clear();
		Set<String> senderIdsSet = new HashSet<String>();
		
		for (SenderVo sender : senders) {
			String label = FmtUtils.getSenderLabel(sender, false, FmtUtils.noValue);			
			senderFilterOptions.add(
				new StrOptionDsc(sender.getSenderId(), label));
			senderIdsSet.add(sender.getSenderId());
		}		
		senderFilterOptions.add(0, STR_OPTION_VALUE_ALL);
		if ((selectedSenderFilter == null) || !senderIdsSet.contains(selectedSenderFilter)) {
			selectedSenderFilter = senderFilterOptions.get(0).getValue(); 
		}
		
		if (selectedDateFromFilter == null) {
			long twoWeeksAgo = (new DateTime().getAsMSecs()) - (1000 * 60 * 60 * 24 * 14);
			selectedDateFromFilter = new DateTime(twoWeeksAgo);
		}
		
		if (selectedDateToFilter == null) {			
			selectedDateToFilter = new DateTime();
		}
		selectedDateFromFilter =
			DateTime.getAsDayBased(selectedDateFromFilter, 
				TimeZone.getTimeZone(user.getOptions().getTimeZone()), true);							
		selectedDateToFilter =
			DateTime.getAsDayBased(selectedDateToFilter, 
				TimeZone.getTimeZone(user.getOptions().getTimeZone()), false);
		
		log.debug("selectedDateFromFilter = " + selectedDateFromFilter);
		log.debug("selectedDateToFilter = " + selectedDateToFilter);
		
		if (selectedSearchStrFilter == null) {
			selectedSearchStrFilter = "";
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

	public TracksView getTracksView() {
		return tracksView;
	}

	public void setTracksView(TracksView tracksView) {
		this.tracksView = tracksView;
	}

	public String getCloudmadeApiKey() {
		return cloudmadeApiKey;
	}

	public void setCloudmadeApiKey(String cloudmadeApiKey) {
		this.cloudmadeApiKey = cloudmadeApiKey;
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
	 * @return the selectedSenderEntryIdx
	 */
	public Integer getSelectedSenderEntryIdx() {
		return selectedSenderEntryIdx;
	}

	/**
	 * @param selectedSenderEntryIdx the selectedSenderEntryIdx to set
	 */
	public void setSelectedSenderEntryIdx(Integer selectedSenderEntryIdx) {
		this.selectedSenderEntryIdx = selectedSenderEntryIdx;
	}

	/**
	 * @return the senderEntries
	 */
	public List<SenderEntry> getSenderEntries() {
		return senderEntries;
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

	/**
	 * @return the selectedTracksOverviewOptsRefresh
	 */
	public Integer getSelectedTracksOverviewOptsRefresh() {
		return selectedTracksOverviewOptsRefresh;
	}

	/**
	 * @param selectedTracksOverviewOptsRefresh the selectedTracksOverviewOptsRefresh to set
	 */
	public void setSelectedTracksOverviewOptsRefresh(
			Integer selectedTracksOverviewOptsRefresh) {
		this.selectedTracksOverviewOptsRefresh = selectedTracksOverviewOptsRefresh;
	}

	/**
	 * @return the liveTrackingOptsFlyToMode
	 */
	public List<IntOptionDsc> getLiveTrackingOptsFlyToMode() {
		return liveTrackingOptsFlyToMode;
	}
	/**
	 * @param liveTrackingOptsFlyToMode the liveTrackingOptsFlyToMode to set
	 */
	public void setLiveTrackingOptsFlyToMode(
			List<IntOptionDsc> liveTrackingOptsFlyToMode) {
		this.liveTrackingOptsFlyToMode = liveTrackingOptsFlyToMode;
	}
	/**
	 * @return the selectedLiveTrackingOptFlyToMode
	 */
	public Integer getSelectedLiveTrackingOptFlyToMode() {
		return selectedLiveTrackingOptFlyToMode;
	}
	/**
	 * @param selectedLiveTrackingOptFlyToMode the selectedLiveTrackingOptFlyToMode to set
	 */
	public void setSelectedLiveTrackingOptFlyToMode(
			Integer selectedLiveTrackingOptFlyToMode) {
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

	/**
	 * @return the senderFilterOptions
	 */
	public List<StrOptionDsc> getSenderFilterOptions() {
		return senderFilterOptions;
	}

	/**
	 * @return the selectedSenderFilter
	 */
	public String getSelectedSenderFilter() {
		return selectedSenderFilter;
	}

	/**
	 * @param selectedSenderFilter the selectedSenderFilter to set
	 */
	public void setSelectedSenderFilter(String selectedSenderFilter) {
		this.selectedSenderFilter = selectedSenderFilter;
	}

	/**
	 * @return the selectedDateFromFilter
	 */
	public DateTime getSelectedDateFromFilter() {
		return selectedDateFromFilter;
	}

	/**
	 * @param selectedDateFromFilter the selectedDateFromFilter to set
	 */
	public void setSelectedDateFromFilter(DateTime selectedDateFromFilter) {
		this.selectedDateFromFilter = selectedDateFromFilter;
	}

	/**
	 * @return the selectedDateToFilter
	 */
	public DateTime getSelectedDateToFilter() {
		return selectedDateToFilter;
	}

	/**
	 * @param selectedDateToFilter the selectedDateToFilter to set
	 */
	public void setSelectedDateToFilter(DateTime selectedDateToFilter) {
		this.selectedDateToFilter = selectedDateToFilter;
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
		this.selectedSearchStrFilter = selectedSearchStrFilter;
	}	
}
