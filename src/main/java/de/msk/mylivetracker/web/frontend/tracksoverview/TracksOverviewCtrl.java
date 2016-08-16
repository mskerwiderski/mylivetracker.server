package de.msk.mylivetracker.web.frontend.tracksoverview;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import de.msk.mylivetracker.Global;
import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.MapsUsedVo;
import de.msk.mylivetracker.domain.user.RoutesUsedVo;
import de.msk.mylivetracker.domain.user.UserSessionStatusVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;
import de.msk.mylivetracker.web.frontend.util.CustomDateTimeEditor;
import de.msk.mylivetracker.web.options.BoolOptionDsc;
import de.msk.mylivetracker.web.options.IntOptionDsc;
import de.msk.mylivetracker.web.options.StrOptionDsc;
import de.msk.mylivetracker.web.util.WebUtils;
 
/**
 * TracksOverviewCtrl.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
@SuppressWarnings("deprecation")
public class TracksOverviewCtrl extends SimpleFormController {
		
	private static final Log log = LogFactory.getLog(TracksOverviewCtrl.class);
	
	private Services services;
	private List<BoolOptionDsc> liveTrackingOpts;
	private List<IntOptionDsc> liveTrackingOptsKeepRecentPos;
	private List<IntOptionDsc> liveTrackingOptsUpdateInterval;
	private List<StrOptionDsc> liveTrackingOptsFlyToMode;
	
	private List<BoolOptionDsc> trackOptsReleaseStatus;
	private List<BoolOptionDsc> trackOptsActivityStatus;
	
	private List<StrOptionDsc> tracksOverviewOptsFlyToMode;
	private List<IntOptionDsc> tracksOverviewOptsDatePeriod;
	private List<IntOptionDsc> tracksOverviewOptsRefresh;
	
	private List<IntOptionDsc> supportedMaps;
	
	private void updateCommandObject(HttpServletRequest request, TracksOverviewCmd cmd) {
		UserWithRoleVo user = WebUtils.getCurrentUserWithRole(); 
		// isUser = user is not a 'guest' and not a 'admin logged in as a user'.
		boolean isUser = 
			!user.getRole().equals(UserRole.Guest) && 
			StringUtils.isEmpty(user.getAdminUsername());
		log.debug("isUser=" + isUser);
		UserSessionStatusVo currentUserSessionStatus = isUser ?
			this.services.getUserSessionStatusService().getUserSessionStatus(
				WebUtils.getCurrentUserWithRole()) :
			UserSessionStatusVo.createDefault(
				WebUtils.getCurrentUserWithRole(), 
				this.services.getSenderService(), 
				this.liveTrackingOptsKeepRecentPos, 
				this.liveTrackingOptsUpdateInterval,
				this.tracksOverviewOptsDatePeriod,
				this.tracksOverviewOptsRefresh);
		log.debug("currentUserSessionStatus=" + currentUserSessionStatus);
		cmd.init(request, this.services.getSenderService(), currentUserSessionStatus);
		if (isUser) {
			UserSessionStatusVo newUserSessionStatus = 
					cmd.getUserSessionStatus();
			log.debug("newUserSessionStatus=" + newUserSessionStatus);
			if (!currentUserSessionStatus.equals(newUserSessionStatus)) {
				this.services.getUserSessionStatusService().updateUserSessionStatus(
					newUserSessionStatus);
				log.debug("saved newUserSessionStatus to database");
			}
		}
	}

	private void checkIfVersionInfoMustBeDisplayed(TracksOverviewCmd cmd) {
		boolean isUser = 
			!WebUtils.getCurrentUserWithRole().getRole().equals(UserRole.Guest) && 
			StringUtils.isEmpty(WebUtils.getCurrentUserWithRole().getAdminUsername());
		if (isUser) {
			UserWithoutRoleVo user = this.services.getUserService().
				getUserWithoutRole(WebUtils.getCurrentUserWithRole().getUserId());
			boolean showVersionInfo = !StringUtils.equals(
				user.getMasterData().getLastVersionInfoDisplayed(), 
				Global.getVersion());
			cmd.setShowVersionInfo(showVersionInfo);
			if (showVersionInfo) {
				user.getMasterData().setLastVersionInfoDisplayed(Global.getVersion());
				this.services.getUserService().updateUserMasterData(user, false);
			}
		} else {
			cmd.setShowVersionInfo(false);
		}
		cmd.setVersionStr(Global.getVersion());
		cmd.setForumLink(Global.getForumLink());
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		TracksOverviewCmd cmd = (TracksOverviewCmd)
			request.getSession().getAttribute(this.getCommandName());
		if (cmd == null) {
			cmd = new TracksOverviewCmd();
			cmd.setLiveTrackingOpts(this.getLiveTrackingOpts());
			cmd.setLiveTrackingOptsKeepRecentPos(this.getLiveTrackingOptsKeepRecentPos());
			cmd.setLiveTrackingOptsUpdateInterval(this.getLiveTrackingOptsUpdateInterval());
			cmd.setLiveTrackingOptsFlyToMode(this.getLiveTrackingOptsFlyToMode());
			cmd.setTracksOverviewOptsFlyToMode(this.getTracksOverviewOptsFlyToMode());
			cmd.setTracksOverviewOptsDatePeriod(this.getTracksOverviewOptsDatePeriod());
			cmd.setTracksOverviewOptsRefresh(this.getTracksOverviewOptsRefresh());
			cmd.setTrackOptsReleaseStatus(this.trackOptsReleaseStatus);
			cmd.setTrackOptsActivityStatus(this.trackOptsActivityStatus);
			cmd.setSupportedMaps(this.supportedMaps);
			MapsUsedVo mapsUsed = WebUtils.getCurrentUserWithRole().getOptions().getMapsUsed();
			cmd.setMapsUsedStr(mapsUsed.getMapsUsedStr(this.supportedMaps.size()));
			cmd.setDefMapId(mapsUsed.getDefMapId());
			RoutesUsedVo routesUsed = WebUtils.getCurrentUserWithRole().getOptions().getRoutesUsed();
			cmd.setRoutesUsed(routesUsed);
			request.getSession().setAttribute(this.getCommandName(), cmd);
		}
		checkIfVersionInfoMustBeDisplayed(cmd);
		return cmd;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
		Errors errors) throws Exception {
		TracksOverviewCmd cmd = (TracksOverviewCmd)command;
		updateCommandObject(request, cmd);
		return super.referenceData(request, command, errors);
	}	

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
		HttpServletResponse response, Object command, BindException errors)
		throws Exception {
		TracksOverviewCmd cmd = (TracksOverviewCmd)command;
		String redirectUrl = null;
		try {
			UserWithRoleVo user = WebUtils.getCurrentUserWithRole();
			redirectUrl = cmd.getActionExecutor().execute(
				services, request, user, cmd);		
		} catch (Exception e) {
			e.printStackTrace();
		}
		ModelAndView mv = null;
		if (StringUtils.isEmpty(redirectUrl)) {
			updateCommandObject(request, cmd);
			mv = super.onSubmit(request, response, command, errors);
		} else {
			mv = new ModelAndView("redirect:" + redirectUrl);
		}
		return mv; 
	}

	@Override
	protected boolean isFormSubmission(HttpServletRequest request) {
		return TracksOverviewCmd.requestHasValidActionExecutor(request);
	}	

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request,
		ServletRequestDataBinder binder) throws Exception {
		CustomDateTimeEditor editor = new CustomDateTimeEditor(
			WebUtils.getCurrentUserWithRole(), DateTime.PRETTY_DATE_FORMAT, false);
			binder.registerCustomEditor(DateTime.class, "selectedDateFromFilter", editor);
			binder.registerCustomEditor(DateTime.class, "selectedDateToFilter", editor);
		super.initBinder(request, binder);
	}

	/**
	 * @return the liveTrackingOptsKeepRecentPos
	 */
	public List<IntOptionDsc> getLiveTrackingOptsKeepRecentPos() {
		return liveTrackingOptsKeepRecentPos;
	}

	/**
	 * @param liveTrackingOptsKeepRecentPos the liveTrackingOptsKeepRecentPos to set
	 */
	public void setLiveTrackingOptsKeepRecentPos(
			List<IntOptionDsc> liveTrackingOptsKeepRecentPos) {
		this.liveTrackingOptsKeepRecentPos = liveTrackingOptsKeepRecentPos;
	}

	/**
	 * @return the trackOptsReleaseStatus
	 */
	public List<BoolOptionDsc> getTrackOptsReleaseStatus() {
		return trackOptsReleaseStatus;
	}

	/**
	 * @param trackOptsReleaseStatus the trackOptsReleaseStatus to set
	 */
	public void setTrackOptsReleaseStatus(List<BoolOptionDsc> trackOptsReleaseStatus) {
		this.trackOptsReleaseStatus = trackOptsReleaseStatus;
	}

	/**
	 * @return the trackOptsActivityStatus
	 */
	public List<BoolOptionDsc> getTrackOptsActivityStatus() {
		return trackOptsActivityStatus;
	}

	/**
	 * @param trackOptsActivityStatus the trackOptsActivityStatus to set
	 */
	public void setTrackOptsActivityStatus(
			List<BoolOptionDsc> trackOptsActivityStatus) {
		this.trackOptsActivityStatus = trackOptsActivityStatus;
	}

	/**
	 * @return the liveTrackingOpts
	 */
	public List<BoolOptionDsc> getLiveTrackingOpts() {
		return liveTrackingOpts;
	}

	/**
	 * @param liveTrackingOpts the liveTrackingOpts to set
	 */
	public void setLiveTrackingOpts(List<BoolOptionDsc> liveTrackingOpts) {
		this.liveTrackingOpts = liveTrackingOpts;
	}

	/**
	 * @return the liveTrackingOptsUpdateInterval
	 */
	public List<IntOptionDsc> getLiveTrackingOptsUpdateInterval() {
		return liveTrackingOptsUpdateInterval;
	}

	/**
	 * @param liveTrackingOptsUpdateInterval the liveTrackingOptsUpdateInterval to set
	 */
	public void setLiveTrackingOptsUpdateInterval(
			List<IntOptionDsc> liveTrackingOptsUpdateInterval) {
		this.liveTrackingOptsUpdateInterval = liveTrackingOptsUpdateInterval;
	}

	public List<StrOptionDsc> getLiveTrackingOptsFlyToMode() {
		return liveTrackingOptsFlyToMode;
	}

	public void setLiveTrackingOptsFlyToMode(
			List<StrOptionDsc> liveTrackingOptsFlyToMode) {
		this.liveTrackingOptsFlyToMode = liveTrackingOptsFlyToMode;
	}

	public List<StrOptionDsc> getTracksOverviewOptsFlyToMode() {
		return tracksOverviewOptsFlyToMode;
	}

	public void setTracksOverviewOptsFlyToMode(
			List<StrOptionDsc> tracksOverviewOptsFlyToMode) {
		this.tracksOverviewOptsFlyToMode = tracksOverviewOptsFlyToMode;
	}

	public List<IntOptionDsc> getTracksOverviewOptsDatePeriod() {
		return tracksOverviewOptsDatePeriod;
	}

	public void setTracksOverviewOptsDatePeriod(
			List<IntOptionDsc> tracksOverviewOptsDatePeriod) {
		this.tracksOverviewOptsDatePeriod = tracksOverviewOptsDatePeriod;
	}

	/**
	 * @return the tracksOverviewOptsRefresh
	 */
	public List<IntOptionDsc> getTracksOverviewOptsRefresh() {
		return tracksOverviewOptsRefresh;
	}

	/**
	 * @param tracksOverviewOptsRefresh the tracksOverviewOptsRefresh to set
	 */
	public void setTracksOverviewOptsRefresh(
			List<IntOptionDsc> tracksOverviewOptsRefresh) {
		this.tracksOverviewOptsRefresh = tracksOverviewOptsRefresh;
	}

	public List<IntOptionDsc> getSupportedMaps() {
		return supportedMaps;
	}

	public void setSupportedMaps(List<IntOptionDsc> supportedMaps) {
		this.supportedMaps = supportedMaps;
	}

	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
	}
}
