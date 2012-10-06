package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
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
	SaveAndSendTestSms(new SaveAndSendTestSms()),
	DeleteAccount(new DeleteAccount());
		
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
	public String execute(
		Services services,
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale) 
		throws ActionExecutionException {
		return this.action.execute(
			services,
			user, cmd, 
			messageSource, locale);
	}
	
	public static boolean isValidActionExecutor(String actionExecutor) {
		return actionValueMap.containsKey(actionExecutor);
	}
}
