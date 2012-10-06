package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.util.PwdUtils;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * RenewGuestAccPassword.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class RenewGuestAccPassword implements IAction {

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		cmd.getUserOptions().setGuestAccPassword(PwdUtils.getPlainPassword());
		cmd.setInfoMessage(messageSource.getMessage(
			"options.success.guest.password.renewed", null, locale));
		return null;
	}
}
