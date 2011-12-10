package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.domain.StatusParamsVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.ITrackService;
import de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TrackEntry;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;
import de.msk.mylivetracker.web.util.request.ReqParamValues;
import de.msk.mylivetracker.web.util.request.ReqUrlStr;

/**
 * ActionStartTracking.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ActionStartTracking implements IAction {

	private static final Log log = LogFactory.getLog(ActionStartTracking.class);
	
	private String targetUrl;
	private AbstractTrackingCtrl.RequestType requestType;
	
	/**
	 * @param targetUrl
	 */
	public ActionStartTracking(String targetUrl, 
		AbstractTrackingCtrl.RequestType requestType) {
		this.targetUrl = targetUrl;
		this.requestType = requestType;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor.IAction#execute(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.domain.user.UserWithRoleVo, de.msk.mylivetracker.service.IApplicationService, de.msk.mylivetracker.service.ITrackService, de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd)
	 */
	@Override
	public String execute(HttpServletRequest request, UserWithRoleVo user,
		IApplicationService applicationService, ITrackService trackService,
		TracksOverviewCmd cmd) throws ActionExecutionException {
		TrackEntry selectedTrackEntry =
			cmd.getTrackEntries().get(cmd.getSelectedTrackEntryIdx());
		
		ReqParamValues reqParamValues = ReqParamValues.create()
			.add(AbstractTrackingCtrl.PARAM_REQ_TYPE, 
				this.requestType.toString())
			.add(AbstractTrackingCtrl.PARAM_TRACK_ID,
				selectedTrackEntry.getTrack().getTrackId())
			.add(AbstractTrackingCtrl.PARAM_SHOW_TRACK_INFO,
				true)
			.add(AbstractTrackingCtrl.PARAM_WINDOW_FULLSCREEN,
				true);
		
		if (selectedTrackEntry.getTrack().isActive()) {
			reqParamValues
				.add(AbstractTrackingCtrl.PARAM_TRACKING_LIVE,
					cmd.getSelectedLiveTrackingOpt())	
				.add(AbstractTrackingCtrl.PARAM_TRACKING_KEEP_RECENT_POSITIONS,
					cmd.getSelectedLiveTrackingOptKeepRecentPos())
				.add(AbstractTrackingCtrl.PARAM_TRACKING_UPDATE_INTERVAL_IN_SECS,
					cmd.getSelectedLiveTrackingOptUpdateInterval())	
				.add(AbstractTrackingCtrl.PARAM_TRACKING_FLY_TO_MODE,
					cmd.getSelectedLiveTrackingOptFlyToMode());															
		} else {
			// ignore live tracking options for inactive tracks.
			reqParamValues
				.add(AbstractTrackingCtrl.PARAM_TRACKING_LIVE, false)	
				.add(AbstractTrackingCtrl.PARAM_TRACKING_KEEP_RECENT_POSITIONS, 0)
				.add(AbstractTrackingCtrl.PARAM_TRACKING_UPDATE_INTERVAL_IN_SECS, 0)	
				.add(AbstractTrackingCtrl.PARAM_TRACKING_FLY_TO_MODE, StatusParamsVo.TrackingFlyToMode.None.ordinal());						
			log.debug("live tracking options ignored because of inactive track.");
		}		
						
		return ReqUrlStr.create(
			applicationService.getApplicationBaseUrl(), targetUrl).
			addParamValues(reqParamValues).toString();  				
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.tracksoverview.actionexecutor.IAction#preExecuteCheck(de.msk.mylivetracker.web.tracksoverview.command.TracksOverviewCmd)
	 */
	public void preExecuteCheck(TracksOverviewCmd cmd)
			throws ActionExecutionException {
		// noop.
	}
}
