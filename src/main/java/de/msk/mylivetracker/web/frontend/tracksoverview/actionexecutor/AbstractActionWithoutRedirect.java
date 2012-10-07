package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import javax.servlet.http.HttpServletRequest;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.Services;
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
	public void preExecuteCheck(TracksOverviewCmd cmd, Services services)
		throws ActionExecutionException {
		// default noop.
	}

	@Override
	public String execute(Services services, HttpServletRequest request,
		UserWithRoleVo user, TracksOverviewCmd cmd)
		throws ActionExecutionException {
		executeAux(services, user, cmd);
		return null;
	}	
	
	public abstract void executeAux(
		Services services,	
		UserWithRoleVo user,
		TracksOverviewCmd cmd) throws ActionExecutionException;
}
