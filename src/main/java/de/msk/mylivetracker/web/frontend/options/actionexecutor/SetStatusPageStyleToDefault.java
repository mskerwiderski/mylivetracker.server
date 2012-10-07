package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserObjectUtils;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * SetStatusPageStyleToDefault.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SetStatusPageStyleToDefault implements IAction {

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		user.getStatusPage().setCssStyle(UserObjectUtils.DEF_USER_STATUS_PAGE_CSS_STYLE);
		services.getUserService().updateUserStatusPage(user);
		cmd.setUserStatusPage(user.getStatusPage().copy());	
		cmd.setInfoMessage(messageSource.getMessage(
			"statuspage.success.setdefstyle", null, locale));
		return null;
	}
}
