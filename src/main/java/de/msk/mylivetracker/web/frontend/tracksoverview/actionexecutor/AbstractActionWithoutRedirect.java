package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import javax.servlet.http.HttpServletRequest;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ITrackService;
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

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor.IAction#preExecuteCheck(de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd)
	 */
	@Override
	public void preExecuteCheck(TracksOverviewCmd cmd)
			throws ActionExecutionException {
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