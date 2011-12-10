package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ISmsService;
import de.msk.mylivetracker.service.IStatusParamsService;
import de.msk.mylivetracker.service.ITrackService;
import de.msk.mylivetracker.service.IUserService;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService.LatLonPos;
import de.msk.mylivetracker.web.frontend.options.OptionsCmd;

/**
 * CheckHomeLocation.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class CheckHomeLocation implements IAction {

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
		checkAndSetHomeLocation(geocodingService, user, cmd);		
		cmd.setInfoMessage(messageSource.getMessage(
			"options.success.home.location.checked", null, locale));
	}
	
	public static void checkAndSetHomeLocation(
		AbstractGeocodingService geocodingService, UserWithoutRoleVo user, 
		OptionsCmd cmd) {
		LatLonPos pos = null;
		String address = cmd.getUserOptions().getHomeLocAddress();
		if (!StringUtils.isEmpty(address)) {
			pos = geocodingService.getPositionOfAddress(
				address, user.getOptions().getGoogleLanguage());			
		}
		if (pos != null) {
			address = geocodingService.getAddressOfPosition(pos,
				user.getOptions().getGoogleLanguage());
			cmd.getUserOptions().setHomeLocAddress(address);
			cmd.getUserOptions().setHomeLocLatitude(pos.getLatitude());
			cmd.getUserOptions().setHomeLocLongitude(pos.getLongitude());
		} else {
			cmd.getUserOptions().setHomeLocLatitude(null);
			cmd.getUserOptions().setHomeLocLongitude(null);
		}
	}
}
