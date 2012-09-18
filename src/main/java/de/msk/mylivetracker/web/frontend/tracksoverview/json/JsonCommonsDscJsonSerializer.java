package de.msk.mylivetracker.web.frontend.tracksoverview.json;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.util.datetime.DateTimeUtils;
import de.msk.mylivetracker.web.frontend.tracksoverview.TracksListCtrl.JsonCommonsDsc;
import de.msk.mylivetracker.web.frontend.util.json.AbstractVoJsonSerializer;

/**
 * JsonCommonsDscJsonSerializer.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-09-09
 * 
 */
public class JsonCommonsDscJsonSerializer extends AbstractVoJsonSerializer<JsonCommonsDsc> {

	
	public JsonCommonsDscJsonSerializer(HttpServletRequest request,
		UserWithoutRoleVo userWithoutRole, UserRole userRole) {
		super(request, userWithoutRole, userRole);
	}

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(JsonCommonsDsc jsonCommonsDsc, Type typeOfJsonCommonsDsc,
		JsonSerializationContext context) {
		JsonObject jsonJsonCommonsDsc = new JsonObject();
		jsonJsonCommonsDsc.addProperty("statusUpdated", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUserWithoutRole(), jsonCommonsDsc.statusUpdated));
		jsonJsonCommonsDsc.addProperty("maxCountOfRecordsExceeded", jsonCommonsDsc.maxCountOfRecordsExceeded);
		jsonJsonCommonsDsc.addProperty("countFoundTracks", jsonCommonsDsc.countFoundTracks);
		jsonJsonCommonsDsc.addProperty("countDisplayedTracks", jsonCommonsDsc.countDisplayedTracks);
		return jsonJsonCommonsDsc;
	}		
}
