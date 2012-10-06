package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.service.sms.ISmsService.SmsServiceException;
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

	final static String FIVE = "5";
	final static int FIVE_MINUTES = 1000 * 60 * 5;
	
	
	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		if (!user.getEmergency().getSmsUnlocked()) {
			throw new ActionExecutionException("user is not unlocked for sms features.");
		}
		String errTxt = null;
		String msgTxt = messageSource.getMessage(
			"emergency.wait.sms.test", new Object[] {FIVE}, locale);
		if ((user.getEmergency().getSmsLastSent() == null) ||
			(((new DateTime().getAsMSecs() - 
				user.getEmergency().getSmsLastSent().getAsMSecs()) > FIVE_MINUTES))) {
			msgTxt = messageSource.getMessage(
				"emergency.success.sms.test", null, locale);
			user.setEmergency(cmd.getUserEmergency().copy());	
			services.getUserService().updateUserEmergency(user);
			try {
				services.getSmsService().sendTestSms(user);
				cmd.getUserEmergency().setSmsSentCount(
					user.getEmergency().getSmsSentCount());
				cmd.getUserEmergency().setSmsLastSent(
					user.getEmergency().getSmsLastSent());
			} catch (SmsServiceException e) {
				errTxt = e.getMessage();
			}
		}
		msgTxt = (errTxt == null) ? 
			msgTxt :
			messageSource.getMessage(
				"emergency.error.sms.test", null, locale);
		cmd.setInfoMessage(msgTxt);
		return null;
	}
}
