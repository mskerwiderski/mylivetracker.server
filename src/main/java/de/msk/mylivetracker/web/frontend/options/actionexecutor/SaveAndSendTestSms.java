package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.service.sms.ISmsService.SendingSmsResult;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * SaveAndSendTestSms.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SaveAndSendTestSms implements IAction {

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		user.setEmergency(cmd.getUserEmergency().copy());	
		services.getUserService().updateUserEmergency(user);
		SendingSmsResult result = services.getSmsService().sendTestSms(user);
		String resTxt = "";
		if (result.isSuccess()) {
			cmd.getUserEmergency().setSmsSentCount(
				user.getEmergency().getSmsSentCount());
			cmd.getUserEmergency().setSmsLastSent(
				user.getEmergency().getSmsLastSent());
			resTxt = messageSource.getMessage(
				"emergency.success.sms.test", 
				new Object[] {result.getCode()}, locale);
		} else {
			resTxt = messageSource.getMessage(
				"emergency.error.sms.test", 
				new Object[] {result.getCode()}, locale);
		}
		cmd.setInfoMessage(resTxt);
		return null;
	}
}
