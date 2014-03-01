package de.msk.mylivetracker.web.frontend.admin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.TrackingFlyToMode;
import de.msk.mylivetracker.domain.statistics.DatabaseInfoVo;
import de.msk.mylivetracker.domain.track.TrackFilterVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserObjectUtils;
import de.msk.mylivetracker.domain.user.UserObjectUtils.CreateUserWithRoleResult;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.service.application.IApplicationService.Parameter;
import de.msk.mylivetracker.service.demo.IDemoService.DemoStatus;
import de.msk.mylivetracker.service.track.ITrackService.TrackListResult;
import de.msk.mylivetracker.service.track.StorePositionProcessor;
import de.msk.mylivetracker.service.track.UserStorePositionQueues;
import de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl;
import de.msk.mylivetracker.web.uploader.processor.UploadProcessor;
import de.msk.mylivetracker.web.util.UrlUtils;
import de.msk.mylivetracker.web.util.UsersLocaleResolver;
import de.msk.mylivetracker.web.util.WebUtils;
import de.msk.mylivetracker.web.util.request.ReqParamValues;
import de.msk.mylivetracker.web.util.request.ReqUrlStr;

/**
 * AdminCtrl.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class AdminCtrl extends ParameterizableViewController {
	
	Log log = LogFactory.getLog(AdminCtrl.class);
	
	private Services services;
	private Cache applicationCache;
	private Cache userWithoutRoleCache;
	private Cache senderCache;
	private Cache ticketCache;
	private UserStorePositionQueues userStorePositionQueues;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();				
		
		String result = null;
		String action = request.getParameter("action");
		
		if (StringUtils.equals(action, "reloadApplicationParameters")) {
			this.services.getApplicationService().reloadParameters();
			result = "application parameters reloaded.";
		} 
		
		if (StringUtils.equals(action, "clearUserCache")) {
			this.userWithoutRoleCache.removeAll();
			result = "user cache cleared.";
		}
		
		if (StringUtils.equals(action, "clearSenderCache")) {
			this.senderCache.removeAll();
			result = "sender cache cleared.";
		}
		
		if (StringUtils.equals(action, "clearTicketCache")) {
			this.ticketCache.removeAll();
			result = "ticket cache cleared.";
		}
		
		if (StringUtils.equals(action, "closeUploadProcessor")) {
			String closePosProcessorSenderId =
				request.getParameter("closePosProcessorSenderId");
			String closePosProcessorThreadId =
				request.getParameter("closePosProcessorThreadId");
			if (!StringUtils.isEmpty(closePosProcessorSenderId) &&
				!StringUtils.isEmpty(closePosProcessorThreadId)) {
				ConcurrentMap<String, StorePositionProcessor> 
					storePosProcessors =
					this.userStorePositionQueues.getStorePositionsProcessors();
				StorePositionProcessor proc =
					storePosProcessors.get(closePosProcessorSenderId);
				if (StringUtils.equals(String.valueOf(proc.getId()), 
						closePosProcessorThreadId)) {
					proc.adminStop();
					result = "processor with sender id '" + closePosProcessorSenderId +
						"' and thread id '" + closePosProcessorThreadId + 
						"' successfully closed.";
				} else {
					result = "processor with sender id '" + closePosProcessorSenderId +
						"' and thread id '" + closePosProcessorThreadId + 
						"' already closed.";
				}
			}
		}
		
		if (StringUtils.equals(action, "registerUser")) {
			String userId = request.getParameter("userId");
			String lastName = request.getParameter("lastName");
			String firstName = request.getParameter("firstName");				
			String emailAddress = request.getParameter("emailAddress");
			String language = request.getParameter("language");
			if (StringUtils.isEmpty(emailAddress)) {
				result = "Register user failed: Email address must not be empty.";
			} else if (UsersLocaleResolver.getLocale(language) == null) {
				result = "Register user failed: Only the languages 'en' and 'de' are supported.";
			} else {
				CreateUserWithRoleResult createUserWithRoleResult =
					UserObjectUtils.createUserWithRole(
						userId, 
						this.services.getApplicationService().getParameterValueAsString(Parameter.ApplicationRealm), 
						UserWithRoleVo.UserRole.User, 
						language, DateTime.TIME_ZONE_UTC, 
						lastName, firstName, emailAddress, 
						3);
				UserWithRoleVo user = createUserWithRoleResult.getUser();
				boolean success = this.services.getUserService().insertUser(user);
				if (success) {
					this.services.getAdminService().sendRegistrationEmailToUser(
						WebUtils.getCurrentUserWithRole(), user);
				}
				
				if (success) {
					result = "New user successfully registered: " +
						"[userId=" + userId + ", password=" + 
						createUserWithRoleResult.getPlainPassword() + 
						"]";
				} else {
					result = "New user registration failed: " +
						"[userId=" + userId + "]";
				}
			}
		}
	
		if (StringUtils.equals(action, "createDemoTrack")) {
			String trackId = request.getParameter("trackId");
			if (StringUtils.isEmpty(trackId)) {
				result = "Create demo track failed: Track id must not be empty.";
			} else {
				long count = this.services.getDemoService().
					insertTrackAsDemoTrack(trackId);
				if (count > 0) {
					result = "Demo track successfully created with " + count + " records.";
				} else {
					result = "Create demo track failed: Maybe Track id does not exist.";
				}
			}
		}
				
		if (StringUtils.equals(action, "startStopDemo")) {
			DemoStatus demoStatus = 
				this.services.getDemoService().getDemoStatus();
			if (demoStatus.isRunning()) {
				this.services.getDemoService().stopDemo();
				result = "Demo successfully stopped.";
			} else {
				this.services.getDemoService().runDemo();
				result = "Demo successfully started.";
			}
		}
		
		Collection<StorePositionProcessor> storePosProcessorsRO =
			this.userStorePositionQueues.getStorePositionsProcessorsRO();
		
		Map<?, ?> parameterMap = applicationCache.getAllWithLoader(applicationCache.getKeys(), null);
		Map<?, ?> userMap = userWithoutRoleCache.getAllWithLoader(userWithoutRoleCache.getKeys(), null);
		Map<?, ?> senderMap = senderCache.getAllWithLoader(senderCache.getKeys(), null);
		Map<?, ?> ticketMap = ticketCache.getAllWithLoader(ticketCache.getKeys(), null);
		
		List<UploadProcessor> uploadProcessors = new ArrayList<UploadProcessor>(); 
		uploadProcessors.addAll(UploadProcessor.getProcessorMap().values()); 
		Collections.sort(uploadProcessors, 
			new Comparator<UploadProcessor>() {
				@Override
				public int compare(UploadProcessor proc1, UploadProcessor proc2) {
					return -1 * Long.valueOf(proc1.getId()).compareTo(Long.valueOf(proc2.getId()));
				}
		});
		
		TrackFilterVo trackFilter = new TrackFilterVo();
		trackFilter.setUserRole(null);		
		
		UserWithRoleVo user = WebUtils.getCurrentUserWithRole();
				
		trackFilter.setByDateFrom(DateTime.getAsDayBased(new DateTime(), 
			TimeZone.getTimeZone(user.getOptions().getTimeZone()), true));
		trackFilter.setByDateTo(DateTime.getAsDayBased(new DateTime(), 
			TimeZone.getTimeZone(user.getOptions().getTimeZone()), false));
		trackFilter.setMaxCountOfRecords(10);
		trackFilter.setByActive(1);
		TrackListResult trackListResult = 
			this.services.getTrackService().getTracksAsRecent(trackFilter);
		
		ReqUrlStr trackUrlPrefix = 
			ReqUrlStr.create(
				this.services.getApplicationService().getApplicationBaseUrl())
			.addUrlPath(UrlUtils.URL_TRACK_AS_MAP_CTRL)
			.addParamValues(
				ReqParamValues.create()
				.add(AbstractTrackingCtrl.PARAM_REQ_TYPE, 
					AbstractTrackingCtrl.RequestType.viewGet.toString())
				.add(AbstractTrackingCtrl.PARAM_TRACKING_LIVE, true)	
				.add(AbstractTrackingCtrl.PARAM_TRACKING_KEEP_RECENT_POSITIONS, 0)
				.add(AbstractTrackingCtrl.PARAM_TRACKING_UPDATE_INTERVAL_IN_SECS, 5)
				.add(AbstractTrackingCtrl.PARAM_TRACKING_FLY_TO_MODE, TrackingFlyToMode.None.name())
				.add(AbstractTrackingCtrl.PARAM_WINDOW_FULLSCREEN, true)
				.add(AbstractTrackingCtrl.PARAM_SHOW_TRACK_INFO, true));							
		
		Map<String, String> trackUrlMap = new HashMap<String, String>();
		for (TrackVo track : trackListResult.getTracks()) {
			String trackUrl = trackUrlPrefix.addParamValue(
				AbstractTrackingCtrl.PARAM_TRACK_ID, track.getTrackId())
				.toString();
			trackUrlMap.put(track.getTrackId(), trackUrl);
		}

		DatabaseInfoVo databaseInfo = this.services.getStatisticsService().getDatabaseInfo();
		
		DemoStatus demoStatus = this.services.getDemoService().getDemoStatus();
		
		String accordionId = request.getParameter("accordionId");
		if (StringUtils.isEmpty(accordionId)) {
			accordionId = "0";
		}
		log.debug("accordionId=" + accordionId);
		
		model.put("parameterMap", parameterMap);
		model.put("parameterCacheSize", parameterMap.size());
		model.put("userMap", userMap);
		model.put("userCacheSize", userMap.size());
		model.put("senderMap", senderMap);
		model.put("senderCacheSize", senderMap.size());
		model.put("ticketMap", ticketMap);
		model.put("ticketCacheSize", ticketMap.size());
		model.put("uploadProcessors", uploadProcessors);
		model.put("storePosProcessors", storePosProcessorsRO);
		model.put("result", result);
		model.put("trackListResult", trackListResult);
		model.put("trackUrlMap", trackUrlMap);
		model.put("databaseInfo", databaseInfo);
		model.put("demoStatus", demoStatus);
		model.put("accordionId", accordionId);
				
		return new ModelAndView(this.getViewName(), model);
	}

	public Services getServices() {
		return services;
	}
	public void setServices(Services services) {
		this.services = services;
	}
	/**
	 * @return the applicationCache
	 */
	public Cache getApplicationCache() {
		return applicationCache;
	}

	/**
	 * @param applicationCache the applicationCache to set
	 */
	public void setApplicationCache(Cache applicationCache) {
		this.applicationCache = applicationCache;
	}
	
	/**
	 * @return the userWithoutRoleCache
	 */
	public Cache getUserWithoutRoleCache() {
		return userWithoutRoleCache;
	}

	/**
	 * @param userWithoutRoleCache the userWithoutRoleCache to set
	 */
	public void setUserWithoutRoleCache(Cache userWithoutRoleCache) {
		this.userWithoutRoleCache = userWithoutRoleCache;
	}

	/**
	 * @return the senderCache
	 */
	public Cache getSenderCache() {
		return senderCache;
	}

	/**
	 * @param senderCache the senderCache to set
	 */
	public void setSenderCache(Cache senderCache) {
		this.senderCache = senderCache;
	}

	/**
	 * @return the ticketCache
	 */
	public Cache getTicketCache() {
		return ticketCache;
	}

	/**
	 * @param ticketCache the ticketCache to set
	 */
	public void setTicketCache(Cache ticketCache) {
		this.ticketCache = ticketCache;
	}

	/**
	 * @return the userStorePositionQueues
	 */
	public UserStorePositionQueues getUserStorePositionQueues() {
		return userStorePositionQueues;
	}

	/**
	 * @param userStorePositionQueues the userStorePositionQueues to set
	 */
	public void setUserStorePositionQueues(
			UserStorePositionQueues userStorePositionQueues) {
		this.userStorePositionQueues = userStorePositionQueues;
	}	
}
