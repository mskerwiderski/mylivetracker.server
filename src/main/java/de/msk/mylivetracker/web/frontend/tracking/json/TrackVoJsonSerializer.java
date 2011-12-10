package de.msk.mylivetracker.web.frontend.tracking.json;

import java.lang.reflect.Type;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.CardiacFunctionVo;
import de.msk.mylivetracker.domain.EmergencySignalVo;
import de.msk.mylivetracker.domain.MessageVo;
import de.msk.mylivetracker.domain.MobNwCellVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.SenderStateVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.util.datetime.DateTimeUtils;
import de.msk.mylivetracker.web.util.FmtUtils;

/**
 * TrackVoJsonSerializer.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackVoJsonSerializer extends AbstractVoJsonSerializer<TrackVo> {
			
	public TrackVoJsonSerializer(HttpServletRequest request,
		UserWithoutRoleVo user) {
		super(request, user);
	}

	/* (non-Javadoc)
	 * @see com.google.gson.JsonSerializer#serialize(java.lang.Object, java.lang.reflect.Type, com.google.gson.JsonSerializationContext)
	 */
	@Override
	public JsonElement serialize(TrackVo track, Type typeOfTrack,
		JsonSerializationContext context) {

		JsonObject jsonTrack = new JsonObject();
		
		DateTime trackCreated = track.getTimestamps().getTrackCreated();
		DateTime trackUpdated = track.getTimestamps().getTrackUpdated();
		DateTime firstPosRcvd = track.getTimestamps().getFirstPositionReceived();
		DateTime recPosRcvd = track.getTimestamps().getRecentPositionUpdated();
		Long runtime = track.getTimestamps().getTrackRuntimeInMSecs();
		Long runtimeWithPauses = track.getClientRuntimeWithPausesInMSecs();
		Long runtimeWithoutPauses = track.getClientRuntimeWithoutPausesInMSecs();
		 
		jsonTrack.addProperty("trackId", track.getTrackId());
		jsonTrack.addProperty("versionMajor", track.getVersionMajor());
		jsonTrack.addProperty("name",
			StringEscapeUtils.escapeHtml(	
				StringUtils.abbreviate(track.getName(), 30)));
		jsonTrack.addProperty("active", track.isActive());
		jsonTrack.addProperty("released", track.isReleased());
		jsonTrack.addProperty("senderId", track.getSenderId());
		jsonTrack.addProperty("senderName",
			StringEscapeUtils.escapeHtml(	
				StringUtils.abbreviate(track.getSenderName(), 30)));
		jsonTrack.addProperty("created",
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), trackCreated));					
		jsonTrack.addProperty("updated", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), trackUpdated));
		jsonTrack.addProperty("firstPosRcvd", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), firstPosRcvd));	
		jsonTrack.addProperty("recPosUpd", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), recPosRcvd));	
		if ((runtimeWithPauses != null) && (runtimeWithoutPauses != null)) {
			jsonTrack.addProperty("runtimeWithPauses", FmtUtils.getRuntimeAsStr(runtimeWithPauses));
			jsonTrack.addProperty("runtimeWithoutPauses", FmtUtils.getRuntimeAsStr(runtimeWithoutPauses));
		} else {
			jsonTrack.addProperty("runtimeWithPauses", FmtUtils.getRuntimeAsStr(runtime));
			jsonTrack.addProperty("runtimeWithoutPauses", FmtUtils.getRuntimeAsStr(null));
		}
		jsonTrack.addProperty("mileage", FmtUtils.getDistanceAsStr(track.getClientStartMileageInMtr(), this.getLocale(), true));
		jsonTrack.addProperty("distance", FmtUtils.getDistanceAsStr(track.getDistanceInMtr(), this.getLocale(), true));
		jsonTrack.addProperty("posSpeed", FmtUtils.getSpeedAsStr(track.getSpeedInMtrPerHour(), this.getLocale()));		
		jsonTrack.addProperty("avgSpeedWithPauses", FmtUtils.getSpeedAsStr(track.getAvgSpeedInMtrPerHourWithPauses(), this.getLocale()));
		jsonTrack.addProperty("avgSpeedWithoutPauses", FmtUtils.getSpeedAsStr(track.getAvgSpeedInMtrPerHourWithoutPauses(), this.getLocale()));
		jsonTrack.addProperty("posAlt", FmtUtils.getDistanceAsStr(track.getPosAltitudeInMtr(), this.getLocale(), false));
		
		jsonTrack.addProperty("cntPos", track.getCountPositions());		
		jsonTrack.add("recPos", (track.getRecentPosition() == null) ?
			new JsonNull() :
			this.getJsonPosition(track.getRecentPosition()));			
		JsonArray jsonPositions = new JsonArray();
		for (PositionVo position : track.getPositions()) {
			if ((position.getLatitudeInDecimal() != null) &&
				(position.getLongitudeInDecimal() != null) &&
				(position.getTimeReceived() != null)) {
				jsonPositions.add(this.getJsonPosition(position));
			}
		}
		jsonTrack.add("positions", jsonPositions);
		
		jsonTrack.addProperty("cntMsg", track.getCountMessages());
		jsonTrack.add("recMsg", (track.getRecentMessage() == null) ?
			new JsonNull() :
			this.getJsonMessage(track.getRecentMessage()));	
		JsonArray jsonMessages = new JsonArray();
		for (MessageVo message : track.getMessages()) {
			if (message.getPosition() != null) {
				jsonMessages.add(this.getJsonMessage(message));
			}
		}
		jsonTrack.add("messages", jsonMessages);
		
		jsonTrack.addProperty("cntEmSi", track.getCountEmergencySignals());
		jsonTrack.add("recEmSi", (track.getRecentEmergencySignal() == null) ?
			new JsonNull() :
			this.getJsonEmergencySignal(track.getRecentEmergencySignal()));	
		JsonArray jsonEmergencySignals = new JsonArray();
		for (EmergencySignalVo emergencySignal : track.getEmergencySignals()) {
			if (emergencySignal.getPosition() != null) {
				jsonEmergencySignals.add(this.getJsonEmergencySignal(emergencySignal));
			}
		}
		jsonTrack.add("emergencySignals", jsonEmergencySignals);
		
		jsonTrack.addProperty("cntMobNwCells", track.getCountMobNwCells());
		jsonTrack.add("recMobNwCell", (track.getRecentMobNwCell() == null) ?
			new JsonNull() :
			this.getJsonMobNwCell(track.getRecentMobNwCell()));	
		
		jsonTrack.addProperty("cntSenderStates", track.getCountSenderStates());
		jsonTrack.add("recSenderState", (track.getRecentSenderState() == null) ?
			new JsonNull() :
			this.getJsonSenderState(track.getRecentSenderState()));
		
		jsonTrack.addProperty("cntCardiacFunctions", track.getCountCardiacFunctions());
		jsonTrack.add("recCardiacFunction", (track.getRecentCardiacFunction() == null) ?
			new JsonNull() :
			this.getJsonCardiacFunction(track.getRecentCardiacFunction()));
		
		return jsonTrack;
	}
	
	public JsonElement getJsonPosition(PositionVo position) {
		JsonObject jsonPosition = new JsonObject();		
		jsonPosition.addProperty("lat", position.getLatitudeInDecimal());
		jsonPosition.addProperty("lon", position.getLongitudeInDecimal());
		jsonPosition.addProperty("alt", FmtUtils.getDistanceAsStr(position.getAltitudeInMtr(), this.getLocale(), false));
		jsonPosition.addProperty("addr", (StringUtils.isEmpty(position.getAddress()) ? "" : position.getAddress()));
		jsonPosition.addProperty("spd", FmtUtils.getSpeedAsStr(position.getSpeedInKmPerHour(), this.getLocale()));
		jsonPosition.addProperty("rcvd", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), position.getTimeReceived()));	
		jsonPosition.addProperty("rec", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), position.getTimeRecorded()));	
		jsonPosition.addProperty("info", FmtUtils.getPositionAsStr(position, this.getLocale(), true, true));
		jsonPosition.addProperty("infoTitle", FmtUtils.getPositionAsStr(position, this.getLocale(), true, false));
		return jsonPosition;
	}	
	
	public JsonElement getJsonMessage(MessageVo message) {
		JsonObject jsonMessage = new JsonObject();
		jsonMessage.addProperty("rcvd", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), message.getTimeReceived()));	
		jsonMessage.add("msg", new JsonPrimitive(
			StringEscapeUtils.escapeHtml(	
				StringUtils.abbreviate(message.getContent(), 40))));
		Boolean hasPosition = (message.getPosition() != null);
		jsonMessage.addProperty("hasPos", hasPosition);
		if (hasPosition) {
			jsonMessage.addProperty("lat", message.getPosition().getLatitudeInDecimal());
			jsonMessage.addProperty("lon", message.getPosition().getLongitudeInDecimal());
		} 
		return jsonMessage;
	}	
	
	public JsonElement getJsonEmergencySignal(EmergencySignalVo emergencySignal) {
		JsonObject jsonEmergencySignal = new JsonObject();
		jsonEmergencySignal.addProperty("rcvd", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), emergencySignal.getTimeReceived()));	
		jsonEmergencySignal.addProperty("active", emergencySignal.getActive());
		String msg = StringEscapeUtils.escapeHtml( 
			StringUtils.abbreviate(emergencySignal.getMessage(), 40));
		if (msg != null) {
			jsonEmergencySignal.addProperty("hasMsg", true);
			jsonEmergencySignal.add("msg", new JsonPrimitive(msg));	
		} else {
			jsonEmergencySignal.addProperty("hasMsg", false);
		}
		Boolean hasPosition = (emergencySignal.getPosition() != null);
		jsonEmergencySignal.addProperty("hasPos", hasPosition);
		if (hasPosition) {
			jsonEmergencySignal.addProperty("lat", emergencySignal.getPosition().getLatitudeInDecimal());
			jsonEmergencySignal.addProperty("lon", emergencySignal.getPosition().getLongitudeInDecimal());
		} 
		return jsonEmergencySignal;
	}
	
	public JsonElement getJsonMobNwCell(MobNwCellVo mobNwCell) {
		JsonObject jsonMobNwCell = new JsonObject();
		jsonMobNwCell.addProperty("info", FmtUtils.getMobNwCellAsStr(mobNwCell));
		jsonMobNwCell.addProperty("rcvd", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), mobNwCell.getTimeReceived()));	
		Boolean hasPosition = (mobNwCell.getPosition() != null);
		jsonMobNwCell.addProperty("hasPos", hasPosition);
		if (hasPosition) {
			jsonMobNwCell.addProperty("lat", mobNwCell.getPosition().getLatitudeInDecimal());
			jsonMobNwCell.addProperty("lon", mobNwCell.getPosition().getLongitudeInDecimal());
		} 
		return jsonMobNwCell;
	}
	
	public JsonElement getJsonSenderState(SenderStateVo senderState) {
		JsonObject jsonSenderState = new JsonObject();
		jsonSenderState.addProperty("info", FmtUtils.getSenderStateAsStr(senderState, true));
		jsonSenderState.addProperty("rcvd", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), senderState.getTimeReceived()));		
		Boolean hasPosition = (senderState.getPosition() != null);
		jsonSenderState.addProperty("hasPos", hasPosition);
		if (hasPosition) {
			jsonSenderState.addProperty("lat", senderState.getPosition().getLatitudeInDecimal());
			jsonSenderState.addProperty("lon", senderState.getPosition().getLongitudeInDecimal());
		} 
		return jsonSenderState;
	}
	
	public JsonElement getJsonCardiacFunction(CardiacFunctionVo cardiacFunction) {
		JsonObject jsonCardiacFunction = new JsonObject();
		jsonCardiacFunction.addProperty("info", FmtUtils.getCardiacFunctionAsStr(cardiacFunction));
		jsonCardiacFunction.addProperty("rcvd", 
			DateTimeUtils.getDateTimeStr4UserRep(this.getUser(), cardiacFunction.getTimeReceived()));		
		Boolean hasPosition = (cardiacFunction.getPosition() != null);
		jsonCardiacFunction.addProperty("hasPos", hasPosition);
		if (hasPosition) {
			jsonCardiacFunction.addProperty("lat", cardiacFunction.getPosition().getLatitudeInDecimal());
			jsonCardiacFunction.addProperty("lon", cardiacFunction.getPosition().getLongitudeInDecimal());
		} 
		return jsonCardiacFunction;
	}
}
