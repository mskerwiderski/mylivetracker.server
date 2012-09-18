package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ITrackService;
import de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;
import de.msk.mylivetracker.web.util.UrlUtils;

/**
 * ActionExecutor.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public enum ActionExecutor {
	RefreshTrackOverview(new ActionRefreshTrackOverview()),
	CreateTrack(new ActionCreateTrack()),
	UpdateTrackReleaseStatus(new ActionUpdateTrackReleaseStatus()),
	UpdateTrackActivityStatus(new ActionUpdateTrackActivityStatus()),
	RenameTrack(new ActionRenameTrack()),
	ResetTrack(new ActionResetTrack()),
	RemoveTrack(new ActionRemoveTrack()),
	RedirectToTrackAsMapCtrl(
		new ActionStartTracking(UrlUtils.URL_TRACK_AS_MAP_CTRL,
			AbstractTrackingCtrl.RequestType.viewGet)),		
	RedirectToTrackAsStatusInfoCtrl(
		new ActionStartTracking(UrlUtils.URL_TRACK_AS_STATUS_INFO_CTRL,
			AbstractTrackingCtrl.RequestType.viewGet)),
	RedirectToTrackAsGoogleEarthCtrl(
		new ActionStartTracking(UrlUtils.URL_TRACK_AS_GOOGLE_EARTH_CTRL,
			AbstractTrackingCtrl.RequestType.binary)),
	RedirectToTrackAsKmlFileCtrl(
		new ActionStartTracking(UrlUtils.URL_TRACK_AS_KML_FILE_CTRL,
			AbstractTrackingCtrl.RequestType.binary)),
	RedirectToTrackAsGpxFileCtrl(
		new ActionStartTracking(UrlUtils.URL_TRACK_AS_GPX_FILE_CTRL,
			AbstractTrackingCtrl.RequestType.binary));
		
	private IAction action;	
	private static Map<String, ActionExecutor> actionValueMap;
	
	private ActionExecutor(IAction action) {
		this.action = action;			
	}
	static {
		actionValueMap =
			new HashMap<String, ActionExecutor>();
		for (ActionExecutor actionId : ActionExecutor.values()) {
			actionValueMap.put(actionId.name(), actionId);
		}
	}
	public String execute(
		HttpServletRequest request, UserWithRoleVo user,	
		IApplicationService applicationService,
		ITrackService trackService, ISenderService senderService,
		TracksOverviewCmd cmd) 
		throws ActionExecutionException {
		this.action.preExecuteCheck(cmd);
		return this.action.execute(request, user, 
			applicationService, trackService, senderService, cmd);
	}
	
	public static boolean isValidActionExecutor(String actionExecutor) {
		return actionValueMap.containsKey(actionExecutor);
	}
}
