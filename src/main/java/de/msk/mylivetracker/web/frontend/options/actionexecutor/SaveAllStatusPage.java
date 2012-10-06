package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserStatusPageVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * SaveAllStatusPage.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SaveAllStatusPage implements IAction {

	@Override
	public String execute(
		Services services,
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		UserStatusPageVo userStatusPage = cmd.getUserStatusPage();		
		userStatusPage.generateLinks(
			services.getStatusParamsService(), 
			services.getApplicationService(), 
			user);
		user.setStatusPage(userStatusPage.copy());
		services.getUserService().updateUserStatusPage(user);
		cmd.setInfoMessage(messageSource.getMessage(
			"statuspage.success.saved", null, locale));
		return null;
	}
}
