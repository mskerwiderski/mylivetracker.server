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

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.MapsUsedVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ITrackService;
import de.msk.mylivetracker.service.IUserService;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;
import de.msk.mylivetracker.web.frontend.util.CustomDateTimeEditor;
import de.msk.mylivetracker.web.options.BoolOptionDsc;
import de.msk.mylivetracker.web.options.IntOptionDsc;
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
		
	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(TracksOverviewCtrl.class);
	
	private List<BoolOptionDsc> liveTrackingOpts;
	private List<IntOptionDsc> liveTrackingOptsKeepRecentPos;
	private List<IntOptionDsc> liveTrackingOptsUpdateInterval;
	private List<IntOptionDsc> liveTrackingOptsFlyToMode;
	
	private List<BoolOptionDsc> trackOptsReleaseStatus;
	private List<BoolOptionDsc> trackOptsActivityStatus;
	
	private List<IntOptionDsc> tracksOverviewOptsRefresh;
	
	private IUserService userService;
	private IApplicationService applicationService;
	private ITrackService trackService;
	private ISenderService senderService;
	
	private void updateCommandObject(HttpServletRequest request, TracksOverviewCmd cmd) {
		UserWithRoleVo user = WebUtils.getCurrentUserWithRole();
		MapsUsedVo mapsUsed = user.getOptions().getMapsUsed();
		cmd.setMapsUsedStr(mapsUsed.getMapsUsedStr());
		cmd.setDefMapId(mapsUsed.getDefMapId());
		cmd.buildUpTrackFilters(user, this.userService, this.senderService);
		List<SenderVo> senders =
			this.senderService.getSenders(user.getUsername());
		cmd.buildUpSenderEntries(senders);
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
			cmd.setTracksOverviewOptsRefresh(this.getTracksOverviewOptsRefresh());
			cmd.setTrackOptsReleaseStatus(this.trackOptsReleaseStatus);
			cmd.setTrackOptsActivityStatus(this.trackOptsActivityStatus);
			MapsUsedVo mapsUsed = WebUtils.getCurrentUserWithRole().getOptions().getMapsUsed();
			cmd.setMapsUsedStr(mapsUsed.getMapsUsedStr());
			cmd.setDefMapId(mapsUsed.getDefMapId());
			request.getSession().setAttribute(this.getCommandName(), cmd);
		}
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
				request, user, 
				this.applicationService, 
				this.trackService,
				this.senderService,
				cmd);		
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
		
	/**
	 * @return the liveTrackingOptsFlyToMode
	 */
	public List<IntOptionDsc> getLiveTrackingOptsFlyToMode() {
		return liveTrackingOptsFlyToMode;
	}

	/**
	 * @param liveTrackingOptsFlyToMode the liveTrackingOptsFlyToMode to set
	 */
	public void setLiveTrackingOptsFlyToMode(
			List<IntOptionDsc> liveTrackingOptsFlyToMode) {
		this.liveTrackingOptsFlyToMode = liveTrackingOptsFlyToMode;
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
}
