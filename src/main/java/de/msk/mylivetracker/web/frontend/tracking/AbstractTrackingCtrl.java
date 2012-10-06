package de.msk.mylivetracker.web.frontend.tracking;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.StatusParamsVo;
import de.msk.mylivetracker.domain.TicketVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.service.statusparams.IStatusParamsService;
import de.msk.mylivetracker.service.ticket.ITicketService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.service.user.IUserService;
import de.msk.mylivetracker.web.exception.MyLiveTrackerException;
import de.msk.mylivetracker.web.frontend.tracking.json.JsonCommonsDscJsonSerializer;
import de.msk.mylivetracker.web.frontend.util.json.TrackVoJsonSerializer;
import de.msk.mylivetracker.web.frontend.util.json.UserVoJsonSerializer;
import de.msk.mylivetracker.web.util.FmtUtils;
import de.msk.mylivetracker.web.util.WebUtils;
import de.msk.mylivetracker.web.util.request.ReqParam;

/**
 * AbstractTrackingCtrl.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractTrackingCtrl extends AbstractController {

	private static final Log log = LogFactory.getLog(AbstractTrackingCtrl.class);
		
	public static final ReqParam<String> PARAM_REQ_TYPE = 
		new ReqParam<String>("reqType", String.class);
	public static final ReqParam<String> PARAM_REQ_ID = 
		new ReqParam<String>("reqId", String.class);
	public static ReqParam<String> PARAM_USER_ID = 
		new ReqParam<String>("userId", String.class);
	public static ReqParam<String> PARAM_TICKET_ID = 
		new ReqParam<String>("ticketId", String.class);
	public static ReqParam<String> PARAM_SENDER_ID = 
		new ReqParam<String>("senderId", String.class);
	public static ReqParam<String> PARAM_TRACK_ID = 
		new ReqParam<String>("trackId", String.class);
	public static ReqParam<Boolean> PARAM_TRACKING_LIVE = 
		new ReqParam<Boolean>("live", Boolean.class);
	public static ReqParam<Integer> PARAM_TRACKING_KEEP_RECENT_POSITIONS = 
		new ReqParam<Integer>("keepRecPos", Integer.class);
	public static ReqParam<Integer> PARAM_TRACKING_COUNT_POSITIONS_DRAWED = 
		new ReqParam<Integer>("cntPosDrawed", Integer.class);	
	public static ReqParam<String> PARAM_TRACKING_RECENT_TRACK_ID = 
		new ReqParam<String>("recTrackId", String.class);
	public static ReqParam<Integer> PARAM_TRACKING_UPDATE_INTERVAL_IN_SECS = 
		new ReqParam<Integer>("intervalSecs", Integer.class);
	public static ReqParam<String> PARAM_TRACKING_FLY_TO_MODE = 
		new ReqParam<String>("flyToMode", String.class);
	public static ReqParam<Boolean> PARAM_WINDOW_FULLSCREEN = 
		new ReqParam<Boolean>("fullscreen", Boolean.class);
	public static ReqParam<Integer> PARAM_WINDOW_WIDTH = 
		new ReqParam<Integer>("width", Integer.class);
	public static ReqParam<Integer> PARAM_WINDOW_HEIGHT = 
		new ReqParam<Integer>("height", Integer.class);	
	public static ReqParam<Boolean> PARAM_SHOW_TRACK_INFO = 
		new ReqParam<Boolean>("showTrackInfo", Boolean.class);
	public static ReqParam<String> PARAM_CSS_STYLE = 
		new ReqParam<String>("css", String.class, true);
	public static ReqParam<String> PARAM_PARAMS_ID = 
		new ReqParam<String>("pid", String.class);
	
	private String view = null;
	private String viewLayoutSimple = null;
	private boolean useJsonReqId = false;
	private boolean trackAsDetailed = false;	
	private IUserService userService;
	private ITrackService trackService;
	private ISenderService senderService;
	private IApplicationService applicationService;
	private ITicketService ticketService;	
	private IStatusParamsService statusParamsService;
	
	public enum RequestType {
		json, binary, viewGet;
	}
	
	public static class UserAndRoleDsc {
		public UserWithoutRoleVo user;
		public UserRole role;
		public StatusParamsVo statusParams;
		public UserAndRoleDsc(UserWithoutRoleVo user, UserRole role, 
			StatusParamsVo statusParams) {
			this.user = user;
			this.role = role;
			this.statusParams = statusParams;
		}		
		public boolean hasStatusParams() {
			return statusParams != null;
		}
	}
	
	public static class JsonCommonsDsc {
		public String reqId;
		public Boolean reqRejected;
		public DateTime statusUpdated;
		public String notFound;
		public JsonCommonsDsc(String reqId,	Boolean reqRejected, 
			DateTime statusUpdated, String notFound) {
			this.reqId = reqId;
			this.reqRejected = reqRejected;
			this.statusUpdated = statusUpdated;
			this.notFound = notFound;
		}		
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		ModelAndView modelAndView = null;							
		try {
			UserAndRoleDsc userAndRoleDsc = this.checkAndGetAuthorizedUser(request);		
			
			Map<String, Object> model = new HashMap<String, Object>();
					
			RequestType requestType = this.getRequestType(request); 
			if (requestType.equals(RequestType.viewGet)) {
				String viewName = this.getViewName(userAndRoleDsc);
				this.initViewModel(request, requestType, userAndRoleDsc, model);
				this.addToViewModel(request, userAndRoleDsc, model);
				modelAndView = new ModelAndView(viewName, model);
			} else if (requestType.equals(RequestType.json)) {
				boolean reqRejected = false;
				String newTicketId = null;
				if (this.isUseJsonReqId()) {
					String reqId = PARAM_REQ_ID.getValueFromReq(request, null);
					reqRejected = !this.ticketService.checkAndDiscardTicket(reqId);
					log.debug("reqId=" + reqId + ", reqRejected=" + reqRejected);
					newTicketId = this.ticketService.createTicket(
						TicketVo.Type.JsonRequestId,	
						userAndRoleDsc.user.getUserId(), 
						userAndRoleDsc.role); 
				}
				TrackVo track = (reqRejected ? 
					null : this.getTrack(request, userAndRoleDsc));
				model.put("jsonCommons",	
					new JsonCommonsDsc(
						newTicketId,
						reqRejected,
						new DateTime(), 
						FmtUtils.noValue));
				this.addToJsonModel(request, 
					userAndRoleDsc, track, model);
				String modelAsJson = modelToJson(request, model, 
					userAndRoleDsc.user, userAndRoleDsc.role);
				//log.debug("json-response: " + 
				//	modelAsJson);
				//log.debug("json-response: " + 
				//	modelAsJson.length() + " bytes");
				response.getOutputStream().print(modelAsJson);
			} else if (requestType.equals(RequestType.binary)) {
				TrackVo track = this.getTrack(request, userAndRoleDsc);
				this.addToResponse(request, 
					userAndRoleDsc, track, response);
			}								
			response.setStatus(HttpServletResponse.SC_OK);					
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return modelAndView;		
	}

	private static String modelToJson(
		HttpServletRequest request, Map<String, Object> model,
		UserWithoutRoleVo user, UserRole userRole) {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.serializeNulls();		
		gsonBuilder.registerTypeAdapter(JsonCommonsDsc.class, 
			new JsonCommonsDscJsonSerializer(request, user, userRole));
		gsonBuilder.registerTypeAdapter(UserWithRoleVo.class, 
			new UserVoJsonSerializer<UserWithRoleVo>(request, user, userRole));
		gsonBuilder.registerTypeAdapter(UserWithoutRoleVo.class, 
			new UserVoJsonSerializer<UserWithoutRoleVo>(request, user, userRole));
		gsonBuilder.registerTypeAdapter(TrackVo.class, 
			new TrackVoJsonSerializer(request, user, userRole, null));
		Gson gson = gsonBuilder.create();
		return gson.toJson(model);
	}
	
	protected void initViewModel(HttpServletRequest request, 
		RequestType requestType, UserAndRoleDsc userAndRoleDsc, Map<String, Object> model) {
		if (this.useJsonReqId) {
			model.put(PARAM_REQ_ID.getName(), 
				this.ticketService.createTicket(
					TicketVo.Type.JsonRequestId,	
					userAndRoleDsc.user.getUserId(), 
					userAndRoleDsc.role));
		}	
		
		if (userAndRoleDsc.statusParams != null) {
			model.put(PARAM_USER_ID.getName(), userAndRoleDsc.statusParams.getUserId());
			model.put(PARAM_TICKET_ID.getName(), userAndRoleDsc.statusParams.getTicketId());
			model.put(PARAM_SENDER_ID.getName(), userAndRoleDsc.statusParams.getSenderId());
			model.put(PARAM_TRACK_ID.getName(), userAndRoleDsc.statusParams.getTrackId());
			model.put(PARAM_TRACKING_LIVE.getName(), userAndRoleDsc.statusParams.getTrackingLive());				
			model.put(PARAM_TRACKING_KEEP_RECENT_POSITIONS.getName(), 
				userAndRoleDsc.statusParams.getTrackingKeepRecentPositions());				
			model.put(PARAM_TRACKING_UPDATE_INTERVAL_IN_SECS.getName(),
				userAndRoleDsc.statusParams.getTrackingUpdateIntervalInSecs());
			model.put(PARAM_TRACKING_FLY_TO_MODE.getName(), 
				userAndRoleDsc.statusParams.getTrackingFlyToMode());
			model.put(PARAM_WINDOW_FULLSCREEN.getName(), 
				userAndRoleDsc.statusParams.getWindowFullscreen());
			boolean isFullscreen = BooleanUtils.toBoolean( 
				(Boolean)model.get(PARAM_WINDOW_FULLSCREEN.getName()));		
			if (!isFullscreen) {		
				model.put(PARAM_WINDOW_WIDTH.getName(), 
					userAndRoleDsc.statusParams.getWindowWidth());
				model.put(PARAM_WINDOW_HEIGHT.getName(), 
					userAndRoleDsc.statusParams.getWindowHeight());
			}
			model.put(PARAM_SHOW_TRACK_INFO.getName(), 
				userAndRoleDsc.statusParams.getShowTrackInfo());
			model.put(PARAM_CSS_STYLE.getName(), 
				userAndRoleDsc.statusParams.getCssStyle());
		} else {
			model.put(PARAM_USER_ID.getName(), PARAM_USER_ID.getValueFromReq(request));
			model.put(PARAM_TICKET_ID.getName(), PARAM_TICKET_ID.getValueFromReq(request));
			model.put(PARAM_SENDER_ID.getName(), PARAM_SENDER_ID.getValueFromReq(request));
			model.put(PARAM_TRACK_ID.getName(), PARAM_TRACK_ID.getValueFromReq(request));
			model.put(PARAM_TRACKING_LIVE.getName(), 
				PARAM_TRACKING_LIVE.getValueFromReq(request, null));
			model.put(PARAM_TRACKING_KEEP_RECENT_POSITIONS.getName(), 
				PARAM_TRACKING_KEEP_RECENT_POSITIONS.getValueFromReq(request, null));
			model.put(PARAM_TRACKING_UPDATE_INTERVAL_IN_SECS.getName(), 
				PARAM_TRACKING_UPDATE_INTERVAL_IN_SECS.getValueFromReq(request, null));
			model.put(PARAM_TRACKING_FLY_TO_MODE.getName(), 
				PARAM_TRACKING_FLY_TO_MODE.getValueFromReq(request, null));
			model.put(PARAM_WINDOW_FULLSCREEN.getName(), 
				PARAM_WINDOW_FULLSCREEN.getValueFromReq(request, null));
			boolean isFullscreen = BooleanUtils.toBoolean( 
				(Boolean)model.get(PARAM_WINDOW_FULLSCREEN.getName()));		
			if (!isFullscreen) {		
				model.put(PARAM_WINDOW_WIDTH.getName(), 
					PARAM_WINDOW_WIDTH.getValueFromReq(request, null));
				model.put(PARAM_WINDOW_HEIGHT.getName(), 
					PARAM_WINDOW_HEIGHT.getValueFromReq(request, null));
			}
			model.put(PARAM_SHOW_TRACK_INFO.getName(), 
				PARAM_SHOW_TRACK_INFO.getValueFromReq(request, null));
			model.put(PARAM_CSS_STYLE.getName(), 
				PARAM_CSS_STYLE.getValueFromReq(request, null));
		}
	}
	
	protected abstract void addToResponse(HttpServletRequest request, 
		UserAndRoleDsc userAndRoleDsc, TrackVo track, 
		HttpServletResponse response);
	
	protected abstract void addToJsonModel(HttpServletRequest request, 
		UserAndRoleDsc userAndRoleDsc, TrackVo track, 
		Map<String, Object> model);
	
	protected abstract void addToViewModel(HttpServletRequest request, 
		UserAndRoleDsc userAndRoleDsc, Map<String, Object> model);
	
	protected RequestType getRequestType(HttpServletRequest request) {		
		return RequestType.valueOf(
			PARAM_REQ_TYPE.getValueFromReq(request, 
				RequestType.viewGet.toString()));			
	}
	
	protected String getViewName(UserAndRoleDsc userAndRoleDsc) {
		if (userAndRoleDsc == null) {
			throw new IllegalArgumentException("userAndRoleDsc must not be null.");
		}
		
		return (!userAndRoleDsc.role.equals(UserRole.RecentTrackAccessor) ?
			this.view : this.viewLayoutSimple);	
	}
	
	protected UserAndRoleDsc checkAndGetAuthorizedUser(HttpServletRequest request) {			
		if (request == null) {
			throw new IllegalArgumentException("request must not be null");
		}
		UserAndRoleDsc userAndRoleDsc = null;
		StatusParamsVo statusParams = null;
		if (PARAM_PARAMS_ID.valueExists(request)) {
			statusParams = 
				this.statusParamsService.getStatusParams(PARAM_PARAMS_ID.getValueFromReq(request));
			if (statusParams == null) {
				throw new IllegalArgumentException("StatusParams not found.");
			} 
		}
				
		String userId = 
			(statusParams == null) ? 
				PARAM_USER_ID.getValueFromReq(request) :
				statusParams.getUserId();
		String ticketId = 
			(statusParams == null) ?
				PARAM_TICKET_ID.getValueFromReq(request) :
				statusParams.getTicketId();
		String trackId = 
			(statusParams == null) ?
				PARAM_TRACK_ID.getValueFromReq(request) :
				statusParams.getTrackId();		
		
		if (!StringUtils.isEmpty(userId) && (!StringUtils.isEmpty(ticketId))) {
			UserWithoutRoleVo user = this.userService.getUserWithoutRole(userId);
			if (user != null) {
				TicketVo ticket = this.ticketService.useTicket(ticketId);
				if (ticket != null) {				
					userAndRoleDsc = new UserAndRoleDsc(
						user, ticket.getAuthorizedRole(), statusParams);					
				} else if (user.getOptions().getRecTrAccEnabled() &&
					ticketId.equals(user.getOptions().getRecTrAccCode())) {
					userAndRoleDsc = new UserAndRoleDsc(
						user, UserRole.RecentTrackAccessor,
						statusParams);
					WebUtils.setCurrentUserWithoutRole(
						request.getSession(), user);
				}				
			} 
		} else {
			UserWithRoleVo user = WebUtils.getCurrentUserWithRole();
			if (user != null) {
				userAndRoleDsc = new UserAndRoleDsc(user, user.getRole(),
					statusParams);
			}
		}
		
		if ((userAndRoleDsc != null) && !StringUtils.isEmpty(trackId)) {
			TrackVo track = this.trackService.getTrackAsMin(trackId);
			if (track == null) {
				throw new MyLiveTrackerException(request,
					MyLiveTrackerException.ExceptionCode.TrackDoesNotExist);
			}
			if (!userAndRoleDsc.role.equals(UserRole.Admin)) {
				if (!userAndRoleDsc.user.getUserId().equals(track.getUserId())) {
					throw new MyLiveTrackerException(request,
						MyLiveTrackerException.ExceptionCode.NotAuthorizedForTrack);
				}
			}
		}
		
		if (userAndRoleDsc == null) {
			throw new MyLiveTrackerException(request,
				MyLiveTrackerException.ExceptionCode.NotAuthorizedForTrack);
		} else {
			log.debug("user with userId='" + userAndRoleDsc.user.getUserId() + 
				"' authorized with role '" + userAndRoleDsc.role.toString() + "'");
		}
		
		return userAndRoleDsc;
	}
	
	protected TrackVo getTrack(HttpServletRequest request, UserAndRoleDsc userAndRoleDsc) {
		if (request == null) {
			throw new IllegalArgumentException("request must not be null");
		}
		if (userAndRoleDsc == null) {
			throw new IllegalArgumentException("userAndRoleDsc must not be null.");
		}				
		TrackVo track = null;
		String senderId = PARAM_SENDER_ID.getValueFromReq(request);
		String trackId = PARAM_TRACK_ID.getValueFromReq(request);			
		Integer keepRecentPositions = PARAM_TRACKING_KEEP_RECENT_POSITIONS.getValueFromReq(request, 0);
		String recTrackId = PARAM_TRACKING_RECENT_TRACK_ID.getValueFromReq(request);
		Integer countPositionsDrawed = PARAM_TRACKING_COUNT_POSITIONS_DRAWED.getValueFromReq(request, 0);
		
		log.debug("senderId: " + senderId);
		log.debug("trackId: " + trackId);
		log.debug("keepRecentPositions: " + keepRecentPositions);
		log.debug("recTrackId: " + recTrackId);		
		log.debug("countPositionsDrawed: " + countPositionsDrawed);
		
		Integer cntCutPositions = keepRecentPositions;
		if (cntCutPositions == 0) {
			cntCutPositions = -1 * countPositionsDrawed;
		}				
				
		log.debug("cntCutPositions: " + cntCutPositions);
		
		if (!StringUtils.isEmpty(trackId)) {
			track = (this.trackAsDetailed ? 
				this.trackService.getTrackAsDetailed(trackId, 
					cntCutPositions, true, true, false, false, false) :
				this.trackService.getTrackAsRecent(trackId));									
		} else {
			track = null;
			if (this.trackAsDetailed) {
				track = this.trackService.getRecentTrackAsMin(userAndRoleDsc.user, senderId);
				if (track != null) {					
					if (!StringUtils.equals(track.getTrackId(), recTrackId)) {
						cntCutPositions = keepRecentPositions;
						log.debug("track has changed --> cntCutPositions is set to keepRecentPositions.");
					}
					track = this.trackService.getTrackAsDetailed(
						track.getTrackId(), cntCutPositions, true, true, false, false, false);
				}
			} else {
				track = this.trackService.getRecentTrackAsRecent(
					userAndRoleDsc.user, senderId);
			}
				
			if (track == null) {
				log.debug("no recent track found for user '" + 
					userAndRoleDsc.user.getUserId() + "'.");
			} else {
				log.debug("recent track with track id = '" + 
					track.getTrackId() + "' found of user '" + 
					userAndRoleDsc.user.getUserId() + "'.");
			}
		}
		
		if (track != null) {
			// authority checks.
			boolean authorized = true;
			if (!userAndRoleDsc.role.equals(UserRole.Admin)) {
				// check if user is allowed to access track if he is not an admin.
				if (!StringUtils.equals(track.getUserId(), userAndRoleDsc.user.getUserId())) {
					authorized = false;
				}
			} 		
			if (userAndRoleDsc.role.equals(UserRole.Guest)) {
				if (!track.isActive() && 
					!userAndRoleDsc.user.getOptions().getGuestAccClosedTrEnabled()) {
					authorized = false;
				}
				if (!track.isReleased() && 
					!userAndRoleDsc.user.getOptions().getGuestAccPrivTrEnabled()) {
					authorized = false;	
				}
			} else if (!userAndRoleDsc.role.equals(UserRole.RecentTrackAccessor)) {
				// noop.
			}			
			if (!authorized) {
				throw new MyLiveTrackerException(request,
					MyLiveTrackerException.ExceptionCode.NotAuthorizedForTrack);				
			}		 
		}
		if (track != null) {
			log.debug("track found with trackId='" + track.getTrackId() + "'" +
				" and " + track.getPositions().size() + " loaded positions.");
		} else {
			log.debug("no track found!");
		}
		
		return track;
	}
		
	/**
	 * @return the view
	 */
	public String getView() {
		return view;
	}

	/**
	 * @param view the view to set
	 */
	public void setView(String view) {
		this.view = view;
	}

	/**
	 * @return the viewLayoutSimple
	 */
	public String getViewLayoutSimple() {
		return viewLayoutSimple;
	}

	/**
	 * @param viewLayoutSimple the viewLayoutSimple to set
	 */
	public void setViewLayoutSimple(String viewLayoutSimple) {
		this.viewLayoutSimple = viewLayoutSimple;
	}

	/**
	 * @return the useJsonReqId
	 */
	public boolean isUseJsonReqId() {
		return useJsonReqId;
	}

	/**
	 * @param useJsonReqId the useJsonReqId to set
	 */
	public void setUseJsonReqId(boolean useJsonReqId) {
		this.useJsonReqId = useJsonReqId;
	}

	/**
	 * @return the userService
	 */
	public IUserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the applicationService
	 */
	public IApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @return the ticketService
	 */
	public ITicketService getTicketService() {
		return ticketService;
	}

	/**
	 * @param ticketService the ticketService to set
	 */
	public void setTicketService(ITicketService ticketService) {
		this.ticketService = ticketService;
	}

	/**
	 * @return the trackService
	 */
	public ITrackService getTrackService() {
		return trackService;
	}

	/**
	 * @param trackService the trackService to set
	 */
	public void setTrackService(ITrackService trackService) {
		this.trackService = trackService;
	}

	/**
	 * @return the senderService
	 */
	public ISenderService getSenderService() {
		return senderService;
	}

	/**
	 * @param senderService the senderService to set
	 */
	public void setSenderService(ISenderService senderService) {
		this.senderService = senderService;
	}

	/**
	 * @return the statusParamsService
	 */
	public IStatusParamsService getStatusParamsService() {
		return statusParamsService;
	}

	/**
	 * @param statusParamsService the statusParamsService to set
	 */
	public void setStatusParamsService(IStatusParamsService statusParamsService) {
		this.statusParamsService = statusParamsService;
	}

	/**
	 * @return the trackAsDetailed
	 */
	public boolean isTrackAsDetailed() {
		return trackAsDetailed;
	}

	/**
	 * @param trackAsDetailed the trackAsDetailed to set
	 */
	public void setTrackAsDetailed(boolean trackAsDetailed) {
		this.trackAsDetailed = trackAsDetailed;
	}			
}
