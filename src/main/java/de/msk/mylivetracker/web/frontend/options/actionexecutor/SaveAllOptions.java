package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * SaveAllOptions.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SaveAllOptions implements IAction {

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {		
		user.setOptions(cmd.getUserOptions().copy());		
		services.getUserService().updateUserOptions(user);
		cmd.setInfoMessage(messageSource.getMessage(
			"options.success.saved", null, locale));
		return null;
	}
}
