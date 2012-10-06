package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;
import java.util.UUID;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * RenewRecTrAccCode.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class RenewRecTrAccCode implements IAction {

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		cmd.getUserOptions().setRecTrAccCode(UUID.randomUUID().toString());
		services.getStatusParamsService().
			deleteAllStatusParamsOfUser(user.getUserId());
		cmd.setInfoMessage(messageSource.getMessage(
			"options.success.track.access.code.renewed", null, locale));
		return null;
	}
}
