package de.msk.mylivetracker.web.frontend.tracking;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.TrackingFlyToMode;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.web.util.UrlUtils;
import de.msk.mylivetracker.web.util.WebUtils;
import de.msk.mylivetracker.web.util.request.ReqParamValues;
import de.msk.mylivetracker.web.util.request.ReqUrlStr;

/**
 * TrackAsStatusInfoCtrl.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackAsStatusInfoCtrl extends AbstractTrackingCtrl {
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl#addToJsonModel(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.UserAndRoleDsc, de.msk.mylivetracker.domain.track.TrackVo, java.util.Map)
	 */
	@Override
	protected void addToJsonModel(HttpServletRequest request,
		UserAndRoleDsc userAndRoleDsc, TrackVo track,
		Map<String, Object> model) {		
		model.put("user", userAndRoleDsc.user);
		model.put("track", track);	
		
		if (track == null) {
			model.put("valueHomeGoogleMapsUrl", 
				getPositionShownOnGoogleMapsUrl(
				userAndRoleDsc.user.getOptions().getHomeLocLatitude(),
				userAndRoleDsc.user.getOptions().getHomeLocLongitude()));
		} else {
			if (track.getRecentPosition() != null) {
				PositionVo position = track.getRecentPosition();
				model.put("valuePositionGoogleMapsUrl", 
					getPositionShownOnGoogleMapsUrl(
						position.getLatitudeInDecimal(),
						position.getLongitudeInDecimal()));										
			}
			ReqParamValues reqParamValues = ReqParamValues.create()
				.add(AbstractTrackingCtrl.PARAM_REQ_TYPE, 
					AbstractTrackingCtrl.RequestType.viewGet.toString())
				.add(AbstractTrackingCtrl.PARAM_USER_ID, 
					userAndRoleDsc.user.getUserId())
				.add(AbstractTrackingCtrl.PARAM_TICKET_ID, 
					userAndRoleDsc.user.getOptions().getRecTrAccCode())
				.add(AbstractTrackingCtrl.PARAM_SHOW_TRACK_INFO, 
					true)	
				.add(AbstractTrackingCtrl.PARAM_WINDOW_FULLSCREEN, 
					true)
				.add(AbstractTrackingCtrl.PARAM_TRACKING_LIVE, true)		
				.add(AbstractTrackingCtrl.PARAM_TRACKING_FLY_TO_MODE, TrackingFlyToMode.FlyToView.name())
				.add(AbstractTrackingCtrl.PARAM_TRACKING_KEEP_RECENT_POSITIONS, 0)
				.add(AbstractTrackingCtrl.PARAM_TRACKING_UPDATE_INTERVAL_IN_SECS, 10)
				.add(AbstractTrackingCtrl.PARAM_TRACK_ID,
					track.getTrackId());					
			
			String googleMapsUrl = ReqUrlStr.create(
				WebUtils.getApplicationBaseUrl(request),	
				UrlUtils.URL_TRACK_AS_MAP_CTRL)
				.addParamValues(reqParamValues).toString();
			
			model.put("valueStatusAsGoogleMaps", googleMapsUrl);
		}												
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl#addToResponse(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.UserAndRoleDsc, de.msk.mylivetracker.domain.track.TrackVo, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void addToResponse(HttpServletRequest request,
		UserAndRoleDsc userAndRoleDsc, TrackVo track,
		HttpServletResponse response) {
		// noop.				
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl#addToViewModel(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.UserAndRoleDsc, java.util.Map)
	 */
	@Override
	protected void addToViewModel(HttpServletRequest request,
		UserAndRoleDsc userAndRoleDsc, Map<String, Object> model) {
	}
		
	private static final String URL_POSITION_SHOWN_ON_GOOGLE_MAPS = 
		"http://maps.google.com/maps?f=q&source=s_q&hl=de&geocode=&q=";
	
	/**
	 * get position shown on google maps url.
	 * @param latitude - the latitude of position.
	 * @param longitude - the longitude of position.
	 * @return Returns google maps url.
	 */
	public static String getPositionShownOnGoogleMapsUrl(Double latitude, Double longitude) {
		return URL_POSITION_SHOWN_ON_GOOGLE_MAPS +
			String.valueOf(latitude) + "," + String.valueOf(longitude);	
	}
}
