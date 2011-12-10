package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ISmsService;
import de.msk.mylivetracker.service.ISmsService.SmsServiceException;
import de.msk.mylivetracker.service.IStatusParamsService;
import de.msk.mylivetracker.service.ITrackService;
import de.msk.mylivetracker.service.IUserService;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
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
	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.options.actionexecutor.IAction#execute(de.msk.mylivetracker.service.IApplicationService, de.msk.mylivetracker.service.IStatusParamsService, de.msk.mylivetracker.service.IUserService, de.msk.mylivetracker.service.ISenderService, de.msk.mylivetracker.service.geocoding.AbstractGeocodingService, de.msk.mylivetracker.service.ISmsService, de.msk.mylivetracker.service.ITrackService, de.msk.mylivetracker.domain.user.UserWithRoleVo, de.msk.mylivetracker.web.frontend.options.OptionsCmd, org.springframework.context.MessageSource, java.util.Locale)
	 */
	@Override
	public void execute(IApplicationService applicationService,
		IStatusParamsService statusParamsService, IUserService userService,
		ISenderService senderService,
		AbstractGeocodingService geocodingService, ISmsService smsService,
		ITrackService trackService, UserWithoutRoleVo user, OptionsCmd cmd,
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
			userService.updateUserEmergency(user);
			try {
				smsService.sendTestSms(user);
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
	}
}
