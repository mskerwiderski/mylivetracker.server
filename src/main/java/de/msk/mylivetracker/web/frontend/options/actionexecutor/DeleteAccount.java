package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.login.LoginCtrl;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * DeleteAccount.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 2012-10-06 initial.
 * 
 */
public class DeleteAccount implements IAction {

	@Override
	public String execute(
		Services services,
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		String userId = user.getUserId();
		services.getSenderService().deleteSendersOfUser(userId);
		services.getTrackService().removeAllTracksOfUsers(userId);
		services.getStatusParamsService().deleteAllStatusParamsOfUser(userId);
		services.getUserSessionStatusService().deleteUserSessionStatus(userId);
		services.getUserOperationsCounterService().deleteUserOperationsCounter(userId);
		services.getUserService().deleteUser(userId);
		return LoginCtrl.URL_LOGOUT;
	}
}
