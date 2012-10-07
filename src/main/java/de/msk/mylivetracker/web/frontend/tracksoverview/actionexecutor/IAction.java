package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import javax.servlet.http.HttpServletRequest;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;

/**
 * IAction.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IAction {
	
	public void preExecuteCheck(TracksOverviewCmd cmd,
		Services services) 
		throws ActionExecutionException;
	
	public String execute(
		Services services,
		HttpServletRequest request,
		UserWithRoleVo user,
		TracksOverviewCmd cmd) throws ActionExecutionException;
}
