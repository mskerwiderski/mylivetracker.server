package de.msk.mylivetracker.web.frontend.tracking.json;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;

import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.util.datetime.DateTimeUtils;
import de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.JsonCommonsDsc;

/**
 * JsonCommonsDscJsonSerializer.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class JsonCommonsDscJsonSerializer extends AbstractVoJsonSerializer<JsonCommonsDsc> {

	
	public JsonCommonsDscJsonSerializer(HttpServletRequest request,
		UserWithoutRoleVo user) {
		super(request, user);
	}

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(JsonCommonsDsc jsonCommonsDsc, Type typeOfJsonCommonsDsc,
		JsonSerializationContext context) {
		JsonObject jsonJsonCommonsDsc = new JsonObject();
		jsonJsonCommonsDsc.addProperty("reqId", jsonCommonsDsc.reqId);
		jsonJsonCommonsDsc.addProperty("reqRejected", jsonCommonsDsc.reqRejected);
		jsonJsonCommonsDsc.addProperty("statusUpdated", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), jsonCommonsDsc.statusUpdated));
		jsonJsonCommonsDsc.addProperty("notFound", 
			jsonCommonsDsc.notFound);
		return jsonJsonCommonsDsc;
	}		
}
