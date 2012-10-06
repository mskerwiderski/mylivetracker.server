package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * ResetAllOptions.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ResetAllOptions implements IAction {

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		cmd.setUserOptions(user.getOptions().copy());	
		cmd.setHomeLocLatitudeStr(
			(user.getOptions().getHomeLocLatitude() == null) ? 
			"" : user.getOptions().getHomeLocLatitude().toString());
		cmd.setHomeLocLongitudeStr(
			(user.getOptions().getHomeLocLongitude() == null) ? 
			"" : user.getOptions().getHomeLocLongitude().toString());
		cmd.setInfoMessage(messageSource.getMessage(
			"options.success.resetted", null, locale));
		return null;
	}
}
