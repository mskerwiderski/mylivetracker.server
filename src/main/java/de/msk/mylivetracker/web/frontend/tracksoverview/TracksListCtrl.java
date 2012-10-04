/**
 * 
 */
package de.msk.mylivetracker.web.frontend.tracksoverview;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.track.TrackFilterVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ITrackService;
import de.msk.mylivetracker.service.ITrackService.TrackListResult;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.SenderEntry;
import de.msk.mylivetracker.web.frontend.tracksoverview.json.JsonCommonsDscJsonSerializer;
import de.msk.mylivetracker.web.frontend.util.json.TrackVoJsonSerializer;
import de.msk.mylivetracker.web.util.WebUtils;
import de.msk.mylivetracker.web.util.request.ReqParam;

/**
 * TracksListCtrl.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-08-28
 * 
 */
public class TracksListCtrl extends AbstractController {
	private static final Log log = LogFactory.getLog(TracksListCtrl.class);

	public static final int MAX_SIZE_RESULT_SET = 30;
	
	public static ReqParam<String> PARAM_SENDER_ID = 
		new ReqParam<String>("senderId", String.class);
	public static ReqParam<Integer> PARAM_DATE_PERIOD = 
		new ReqParam<Integer>("datePeriod", Integer.class);
	public static ReqParam<String> PARAM_SEARCH_STR = 
		new ReqParam<String>("searchStr", String.class);
	public static ReqParam<Boolean> PARAM_ONLY_ACTIVE = 
		new ReqParam<Boolean>("onlyActive", Boolean.class);
	
	private static final int DATE_PERIOD_OPT_ALL_TRACKS = -1;
	
	private ITrackService trackService;
	private ISenderService senderService;
	
	public static class JsonCommonsDsc {
		public DateTime statusUpdated;
		public Boolean maxCountOfRecordsExceeded;
		public Integer countFoundTracks;
		public Integer countDisplayedTracks;
		public JsonCommonsDsc(DateTime statusUpdated, Boolean maxCountOfRecordsExceeded,
				Integer countFoundTracks, Integer countDisplayedTracks) {
			this.statusUpdated = statusUpdated;
			this.maxCountOfRecordsExceeded = maxCountOfRecordsExceeded;
			this.countFoundTracks = countFoundTracks;
			this.countDisplayedTracks = countDisplayedTracks;
		}
	}
	
	private TrackFilterVo createTrackFilter(UserWithRoleVo user, 
		String senderId, DateTime dateFrom, 
		String searchStr, boolean onlyActiveTracks, int maxCountOfRecords) {
		TrackFilterVo trackFilter = new TrackFilterVo();
		
		log.debug("userId=" + user.getUserId());
		log.debug("senderId=" + senderId);
		log.debug("dateFrom=" + ((dateFrom != null) ? dateFrom.toString() : "null"));
		log.debug("searchStr=" + searchStr);
		log.debug("onlyActiveTracks=" + onlyActiveTracks);
		log.debug("maxCountOfRecords=" + maxCountOfRecords);
		
		trackFilter.setMaxCountOfRecords(maxCountOfRecords);
		trackFilter.setUserId(user.getUserId());
		trackFilter.setUserRole(user.getRole());
		trackFilter.setGuestAccClosedTrEnabled(user.getOptions().getGuestAccClosedTrEnabled());
		trackFilter.setGuestAccPrivTrEnabled(user.getOptions().getGuestAccPrivTrEnabled());
				
		if (!SenderEntry.isAll(senderId)) {
			trackFilter.setBySenderId(senderId);
		}
		
		trackFilter.setByDateFrom(dateFrom);
		trackFilter.setByDateTo(null);
		
		if (!StringUtils.isEmpty(searchStr)) {
			searchStr = "%" + searchStr + "%";
			trackFilter.setBySearchStr(searchStr);
			log.debug(searchStr);
		}
		if (onlyActiveTracks) {
			trackFilter.setByActive(1);
		}
		
		log.debug("trackFilter=" + trackFilter.toString());
		
		return trackFilter;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
		
		UserWithRoleVo user = WebUtils.getCurrentUserWithRole();
		
		String senderId = PARAM_SENDER_ID.getValueFromReq(request, null);
		String searchStr = PARAM_SEARCH_STR.getValueFromReq(request, null);
		
		Integer datePeriod = PARAM_DATE_PERIOD.getValueFromReq(request, null);
		
		if (datePeriod == null) {
			throw new IllegalArgumentException("datePeriod must not be null.");
		}
		
		DateTime dateFrom = null;
		if (datePeriod != DATE_PERIOD_OPT_ALL_TRACKS) {
			long daysAgo = (new DateTime().getAsMSecs()) -
				(1000L * 60L * 60L * 24L * datePeriod.longValue());
			log.debug("daysAgo=" + daysAgo);
			dateFrom = new DateTime(daysAgo);	
		}

		boolean onlyActiveTracks = PARAM_ONLY_ACTIVE.getValueFromReq(request, Boolean.FALSE);

		TrackListResult trackListResult = 
			this.trackService.getTracksAsRecent(
				createTrackFilter(user, senderId, 
					dateFrom, searchStr, 
					onlyActiveTracks, MAX_SIZE_RESULT_SET));
		
		model.put("jsonCommons",	
			new JsonCommonsDsc(
				new DateTime(),	
				trackListResult.isMaxCountOfRecordsExceeded(),
				trackListResult.getCountFoundTracks(),
				MAX_SIZE_RESULT_SET));
		model.put("tracks", trackListResult.getTracks());
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.serializeNulls();
		gsonBuilder.registerTypeAdapter(JsonCommonsDsc.class, 
			new JsonCommonsDscJsonSerializer(request, 
				user, user.getRole()));
		gsonBuilder.registerTypeAdapter(TrackVo.class, 
			new TrackVoJsonSerializer(request, 
				user, user.getRole(),
				this.senderService));
		Gson gson = gsonBuilder.create();
		String modelAsJson = gson.toJson(model);
		
		log.debug(modelAsJson);
		
		response.getOutputStream().print(modelAsJson);
		
		return null;
	}

	public ITrackService getTrackService() {
		return trackService;
	}

	public void setTrackService(ITrackService trackService) {
		this.trackService = trackService;
	}

	public ISenderService getSenderService() {
		return senderService;
	}

	public void setSenderService(ISenderService senderService) {
		this.senderService = senderService;
	}
}
