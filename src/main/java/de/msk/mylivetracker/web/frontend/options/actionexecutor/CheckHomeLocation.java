package de.msk.mylivetracker.web.frontend.options.actionexecutor;

import java.util.Locale;

import org.springframework.context.MessageSource;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService.Address;
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

	@Override
	public String execute(
		Services services, 
		UserWithoutRoleVo user, OptionsCmd cmd,
		MessageSource messageSource, Locale locale)
		throws ActionExecutionException {
		checkAndSetHomeLocation(
			services.getGeocodingService(), user, cmd);		
		cmd.setInfoMessage(messageSource.getMessage(
			"options.success.home.location.checked", null, locale));
		return null;
	}
	
	public static void checkAndSetHomeLocation(
		AbstractGeocodingService geocodingService, UserWithoutRoleVo user, 
		OptionsCmd cmd) {
		LatLonPos pos = null;
		pos = geocodingService.getPositionOfAddress(
			new Address(
				cmd.getUserOptions().getHomeLocCountry(),
				cmd.getUserOptions().getHomeLocCity(),
				cmd.getUserOptions().getHomeLocStreet(),
				cmd.getUserOptions().getHomeLocHousenumber()),	
			user.getOptions().getGeocoderLanguage());			
		if (pos != null) {
			cmd.getUserOptions().setHomeLocLatitude(pos.getLatitude());
			cmd.getUserOptions().setHomeLocLongitude(pos.getLongitude());
			cmd.setHomeLocLatitudeStr(pos.getLatitude().toString());
			cmd.setHomeLocLongitudeStr(pos.getLongitude().toString());
		} else {
			cmd.getUserOptions().setHomeLocLatitude(null);
			cmd.getUserOptions().setHomeLocLongitude(null);
			cmd.setHomeLocLatitudeStr("");
			cmd.setHomeLocLongitudeStr("");
		}
	}
}
