package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserAutoLoginVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * RenewTicketForLoginByUrl.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-08-30
 * 
 */
public class RenewAutoLoginTicketForGuest implements IAction {

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		cmd.getUserAutoLogin().setAutoLoginTicketForGuest(UserAutoLoginVo.createAutoLoginTicket());
		cmd.setInfoMessage(messageSource.getMessage(
			"autologin.guest.ticket.renewed", null, locale));
		return null;
	}
}
