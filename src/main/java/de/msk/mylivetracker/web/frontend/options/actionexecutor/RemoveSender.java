package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * RemoveSender.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class RemoveSender implements IAction {

	@Override
	public String execute(Services services,
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		services.getTrackService().closeActiveTrack(cmd.getSelectedSenderId());
		services.getSenderService().deleteSender(cmd.getSelectedSenderId());			
		cmd.setInfoMessage(messageSource.getMessage(
			"sendermaintenance.success.sender.removed", 
			new Object[] {cmd.getSelectedSenderId()}, 
			locale));
		cmd.setSenderDetails(null);
		cmd.setSelectedSenderId(null);
		return null;
	}
}
