package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * SaveAllMaps.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 2012-09-14 initial.
 * 
 */
public class SaveAllMaps implements IAction {

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {		
		user.getOptions().setMapsUsed(cmd.getUserOptions().getMapsUsed().copy());
		user.getOptions().setRoutesUsed(cmd.getUserOptions().getRoutesUsed().copy());
		services.getUserService().updateUserOptionsMapsUsed(user);
		cmd.setInfoMessage(messageSource.getMessage(
			"maps.success.saved", null, locale));
		return null;
	}
}
