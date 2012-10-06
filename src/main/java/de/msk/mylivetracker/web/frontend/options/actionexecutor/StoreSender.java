package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * StoreSender.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class StoreSender implements IAction {

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		SenderVo sender = cmd.getSenderDetails();
		sender.setUserId(user.getUserId());
		services.getSenderService().storeSender(sender);		
		if (cmd.getActionExecutor().equals(ActionExecutor.AddSender)) {
			cmd.setInfoMessage(messageSource.getMessage(
				"sendermaintenance.success.sender.added", 
				new Object[] {sender.getSenderId()},
				locale));
		} else {
			cmd.setInfoMessage(messageSource.getMessage(
				"sendermaintenance.success.sender.updated", 
				new Object[] {sender.getSenderId()}, 
				locale));
		}
		cmd.setSelectedSenderId(sender.getSenderId());
		return null;
	}
}
