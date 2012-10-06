package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.domain.TrackingFlyToModeVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl;
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
	 * @see de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor.IAction#execute(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.domain.user.UserWithRoleVo, de.msk.mylivetracker.service.IApplicationService, de.msk.mylivetracker.service.ITrackService, de.msk.mylivetracker.service.ISenderService, de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd)
	 */
	@Override
	public String execute(HttpServletRequest request, UserWithRoleVo user,
		IApplicationService applicationService, ITrackService trackService,
		ISenderService senderService, TracksOverviewCmd cmd)
		throws ActionExecutionException {

		TrackVo track = trackService.getTrackAsMin(cmd.getSelectedTrackId());
		
		ReqParamValues reqParamValues = ReqParamValues.create()
			.add(AbstractTrackingCtrl.PARAM_REQ_TYPE, 
				this.requestType.toString())
			.add(AbstractTrackingCtrl.PARAM_TRACK_ID,
				cmd.getSelectedTrackId())
			.add(AbstractTrackingCtrl.PARAM_SHOW_TRACK_INFO,
				true)
			.add(AbstractTrackingCtrl.PARAM_WINDOW_FULLSCREEN,
				true);
		
		if (track.isActive()) {
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
				.add(AbstractTrackingCtrl.PARAM_TRACKING_FLY_TO_MODE, TrackingFlyToModeVo.None.name());						
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

	@Override
	public void preExecuteCheck(TracksOverviewCmd cmd,
		ISenderService senderService) throws ActionExecutionException {
		// noop.
	}
}
