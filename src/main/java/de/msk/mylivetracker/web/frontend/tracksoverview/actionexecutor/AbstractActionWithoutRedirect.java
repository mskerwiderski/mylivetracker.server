package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import javax.servlet.http.HttpServletRequest;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;

/**
 * AbstractActionWithoutRedirect.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractActionWithoutRedirect implements IAction {

	@Override
	public void preExecuteCheck(TracksOverviewCmd cmd,
		ISenderService senderService) throws ActionExecutionException {
		// noop.
	}

	@Override
	public String execute(HttpServletRequest request, UserWithRoleVo user,
		IApplicationService applicationService, ITrackService trackService,
		ISenderService senderService, TracksOverviewCmd cmd) throws ActionExecutionException {
		executeAux(user, trackService, senderService, cmd);
		return null;
	}	
	
	public abstract void executeAux(
		UserWithRoleVo user,
		ITrackService trackService,
		ISenderService senderService,
		TracksOverviewCmd cmd) throws ActionExecutionException;
}
