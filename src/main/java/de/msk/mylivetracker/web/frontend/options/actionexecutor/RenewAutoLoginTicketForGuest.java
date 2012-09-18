package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserAutoLoginVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ISmsService;
import de.msk.mylivetracker.service.IStatusParamsService;
import de.msk.mylivetracker.service.ITrackService;
import de.msk.mylivetracker.service.IUserService;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
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
		cmd.getUserAutoLogin().setAutoLoginTicketForGuest(UserAutoLoginVo.createAutoLoginTicket());
		cmd.setInfoMessage(messageSource.getMessage(
			"autologin.guest.ticket.renewed", null, locale));
	}
}
