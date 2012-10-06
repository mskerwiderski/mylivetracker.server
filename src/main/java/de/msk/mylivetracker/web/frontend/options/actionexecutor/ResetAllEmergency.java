package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * ResetAllEmergency.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ResetAllEmergency implements IAction {

	@Override
	public String execute(
		Services services,
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		if (!user.getEmergency().getSmsUnlocked()) {
			throw new ActionExecutionException("user is not unlocked for sms features.");
		}
		cmd.setUserEmergency(user.getEmergency().copy());	
		cmd.setInfoMessage(messageSource.getMessage(
			"emergency.success.resetted", null, locale));
		return null;		
	}
}
