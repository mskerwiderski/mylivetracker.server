package de.msk.mylivetracker.web.frontend.tracking.json;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import de.msk.mylivetracker.domain.user.UserOptionsVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.web.util.FmtUtils;

/**
 * UserVoJsonSerializer.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UserVoJsonSerializer<T extends UserWithoutRoleVo> extends AbstractVoJsonSerializer<T> {
	
	public UserVoJsonSerializer(HttpServletRequest request,
		UserWithoutRoleVo user) {
		super(request, user);
	}

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(T user, Type typeOfUser,
		JsonSerializationContext context) {

		JsonObject jsonUser = new JsonObject();
		
		UserOptionsVo options = user.getOptions();
		Boolean hasHome = 
			(options.getHomeLocLatitude() != null) &&
			(options.getHomeLocLongitude() != null);
		
		jsonUser.addProperty("userId", user.getUserId());
		String name = "";
		if (!StringUtils.isEmpty(user.getMasterData().getFirstName())) {
			name += user.getMasterData().getFirstName();
		} 
		if (!StringUtils.isEmpty(user.getMasterData().getLastName())) {
			name += " " + user.getMasterData().getLastName();
		}
		if (StringUtils.isEmpty(name)) {
			name = user.getUserId();
		}		
		jsonUser.addProperty("name", StringUtils.strip(name));
		jsonUser.addProperty("hasHome", hasHome);
		if (hasHome) {
			jsonUser.addProperty("homeLat", options.getHomeLocLatitude());
			jsonUser.addProperty("homeLon", options.getHomeLocLongitude());
				
		}
		jsonUser.addProperty("homeAddr", 
			FmtUtils.getHomePositionAsStr(user, this.getLocale(), true, true));
		jsonUser.addProperty("homeAddrTitle", 
			FmtUtils.getHomePositionAsStr(user, this.getLocale(), true, false));
		jsonUser.addProperty("routeColor", options.getTrackRouteColor());
		jsonUser.addProperty("routeWidth", options.getTrackRouteWidth());
				
		return jsonUser;
	}		
}
