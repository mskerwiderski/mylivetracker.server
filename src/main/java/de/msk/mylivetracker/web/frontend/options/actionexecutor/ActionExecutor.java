package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;

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
 * ActionExecutor.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public enum ActionExecutor {
	ResetAllMasterData(new ResetAllMasterData()),
	SaveAllMasterData(new SaveAllMasterData()),
	RenewAutoLoginTicketForUser(new RenewAutoLoginTicketForUser()),
	RenewAutoLoginTicketForGuest(new RenewAutoLoginTicketForGuest()),
	ResetAllOptions(new ResetAllOptions()),
	SaveAllOptions(new SaveAllOptions()),
	ResetAllStatusPage(new ResetAllStatusPage()),
	SetStatusPageStyleToDefault(new SetStatusPageStyleToDefault()),
	SaveAllStatusPage(new SaveAllStatusPage()),
	RenewRecTrAccCode(new RenewRecTrAccCode()),
	RenewGuestAccPassword(new RenewGuestAccPassword()),
	CheckHomeLocation(new CheckHomeLocation()),
	SetSenderFields(new SetSenderFields()),
	UpdateSender(new StoreSender()),
	AddSender(new StoreSender()),
	RemoveSender(new RemoveSender()),
	ResetAllMaps(new ResetAllMaps()),
	SaveAllMaps(new SaveAllMaps()),
	ResetAllEmergency(new ResetAllEmergency()),
	SaveAllEmergency(new SaveAllEmergency()),
	SaveAndSendTestSms(new SaveAndSendTestSms());
		
	private IAction action;
	private static Map<String, ActionExecutor> actionValueMap;
	
	private ActionExecutor(IAction action) {
		this.action = action;			
	}
	static {
		actionValueMap =
			new HashMap<String, ActionExecutor>();
		for (ActionExecutor actionId : ActionExecutor.values()) {
			actionValueMap.put(actionId.name(), actionId);
		}
	}
	public void execute(
		IApplicationService applicationService,
		IStatusParamsService urlService,	
		IUserService userService, 
		ISenderService senderService,
		AbstractGeocodingService geocodingService,
		ISmsService smsService,
		ITrackService trackService,
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale) 
		throws ActionExecutionException {
		this.action.execute(
			applicationService,
			urlService,
			userService, 
			senderService,
			geocodingService,
			smsService,
			trackService,
			user, cmd, 
			messageSource, locale);
	}
	
	public static boolean isValidActionExecutor(String actionExecutor) {
		return actionValueMap.containsKey(actionExecutor);
	}
}
