package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.StatusParamsVo;
import de.msk.mylivetracker.domain.user.UserStatusPageVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * SaveAllStatusPage.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SaveAllStatusPage implements IAction {

	@Override
	public String execute(
		Services services,
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		UserStatusPageVo statusPage = cmd.getUserStatusPage();
		StatusParamsVo statusParams = StatusParamsVo.createInstance();
		statusParams.setUserId(user.getUserId());
		statusParams.setTicketId(user.getOptions().getRecTrAccCode());
		statusParams.setSenderId(statusPage.getSenderId());
		statusParams.setTrackingLive(statusPage.getTrackingLive());
		statusParams.setTrackingKeepRecentPositions(statusPage.getTrackingKeepRecentPositions());
		statusParams.setTrackingUpdateIntervalInSecs(statusPage.getTrackingUpdateIntervalInSecs());
		statusParams.setTrackingFlyToMode(statusPage.getTrackingFlyToMode());
		statusParams.setWindowFullscreen(statusPage.getFullScreen());
		statusParams.setShowTrackInfo(statusPage.getShowTrackInfo());
		statusParams.setCssStyle(statusPage.getCssStyle());
		statusParams.setWindowWidth(statusPage.getWindowWidth());
		statusParams.setWindowHeight(statusPage.getWindowHeight());
		services.getStatusParamsService().saveStatusParams(statusParams);
		statusPage.setLastParamsId(statusParams.getStatusParamsId());
		user.setStatusPage(statusPage.copy());
		services.getUserService().updateUserStatusPage(user);
		cmd.setInfoMessage(messageSource.getMessage(
			"statuspage.success.saved", null, locale));
		return null;
	}
}
