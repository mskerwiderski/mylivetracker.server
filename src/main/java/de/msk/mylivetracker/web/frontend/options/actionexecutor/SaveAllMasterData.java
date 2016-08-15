package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * SaveAllMasterData.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SaveAllMasterData implements IAction {

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		user.setMasterData(cmd.getUserMasterData().copy());	
		services.getUserService().updateUserMasterData(user, true);
		user.setAutoLogin(cmd.getUserAutoLogin().copy());	
		services.getUserService().updateUserAutoLogin(user);
		cmd.setInfoMessage(messageSource.getMessage(
			"masterdata.success.saved", null, locale));
		return null;
	}
}
