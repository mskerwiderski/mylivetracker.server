package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;

/**
 * ActionRefreshTrackOverview.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ActionRefreshTrackOverview extends AbstractActionWithoutRedirect {

	@Override
	public void executeAux(Services services, UserWithRoleVo user,
		TracksOverviewCmd cmd) throws ActionExecutionException {
		// default noop.
	}
}
