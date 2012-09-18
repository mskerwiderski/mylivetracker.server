package de.msk.mylivetracker.web.frontend.options;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;

import de.msk.mylivetracker.domain.sender.SenderSymbolVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.UserOptionsVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ISmsService;
import de.msk.mylivetracker.service.IStatusParamsService;
import de.msk.mylivetracker.service.ITrackService;
import de.msk.mylivetracker.service.IUserService;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
import de.msk.mylivetracker.web.frontend.util.CustomSenderSymbolEditor;
import de.msk.mylivetracker.web.options.BoolOptionDsc;
import de.msk.mylivetracker.web.options.IntOptionDsc;
import de.msk.mylivetracker.web.options.StrOptionDsc;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * OptionsCtrl.
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
public class OptionsCtrl extends SimpleFormController {

	private List<StrOptionDsc> userOptsLanguage;
	private List<BoolOptionDsc> trackOptsReleaseStatus;
	private List<IntOptionDsc> trackOptsAutoClose;
	private List<IntOptionDsc> trackRouteOptsWidth;
	private List<BoolOptionDsc> commonsOptsYesNo;
	private List<BoolOptionDsc> commonsOptsOnOff;
	private List<BoolOptionDsc> commonsOptsEnDisabled;
	private List<StrOptionDsc> commonsOptsTimeZone;
	private List<StrOptionDsc> senderOptsRunningMode;
	private List<IntOptionDsc> stPgTrOptsFlyToMode;
	private List<IntOptionDsc> stPgTrOptsKeepRecentPos;
	private List<IntOptionDsc> stPgTrOptsUpdateInterval;
	private List<IntOptionDsc> supportedMaps;
	
	private IApplicationService applicationService;
	private IUserService userService;
	private ISenderService senderService;
	private AbstractGeocodingService geocodingService;
	private ISmsService smsService;
	private IStatusParamsService statusParamsService;
	private ITrackService trackService;
	
	private void updateCommandObject(HttpServletRequest request, OptionsCmd cmd) {	
		UserWithoutRoleVo user = this.userService.getUserWithoutRole(
			WebUtils.getCurrentUserWithRole().getUserId());
		List<SenderVo> senderList = senderService.getSenders(user.getUserId());
		cmd.buildUpSenderEntries(request, senderList);
		cmd.buildUpSymbolEntries(request, senderList);
		cmd.buildUpLinks(request, applicationService.getApplicationBaseUrl(), user);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		OptionsCmd cmd = (OptionsCmd)
			request.getSession().getAttribute(this.getCommandName());
		if (cmd == null) {
			cmd = new OptionsCmd();			
			cmd.setUserOptsLanguage(userOptsLanguage);
			cmd.setTrackOptsReleaseStatus(trackOptsReleaseStatus);
			cmd.setTrackOptsAutoClose(trackOptsAutoClose);
			cmd.setTrackRouteOptsWidth(trackRouteOptsWidth);			
			UserWithoutRoleVo user = this.userService.getUserWithoutRole(
				WebUtils.getCurrentUserWithRole().getUserId());
			cmd.setSenderLimit(user.getSenderLimit());
			cmd.setUserMasterData(user.getMasterData().copyWoPwd());
			cmd.setUserAutoLogin(user.getAutoLogin().copy());
			cmd.setUserOptions(user.getOptions().copy());
			cmd.setHomeLocLatitudeStr(
				(user.getOptions().getHomeLocLatitude() == null) ? 
				"" : user.getOptions().getHomeLocLatitude().toString());
			cmd.setHomeLocLongitudeStr(
				(user.getOptions().getHomeLocLongitude() == null) ? 
				"" : user.getOptions().getHomeLocLongitude().toString());
			cmd.setUserStatusPage(user.getStatusPage().copy());
			cmd.setUserEmergency(user.getEmergency().copy());
			cmd.setCommonsOptsYesNo(commonsOptsYesNo);
			cmd.setCommonsOptsOnOff(commonsOptsOnOff);
			cmd.setCommonsOptsEnDisabled(commonsOptsEnDisabled);
			cmd.setCommonsOptsTimeZone(commonsOptsTimeZone);
			cmd.setSenderOptsRunningMode(senderOptsRunningMode);
			cmd.setStPgTrOptsFlyToMode(stPgTrOptsFlyToMode);
			cmd.setStPgTrOptsKeepRecentPos(stPgTrOptsKeepRecentPos);
			cmd.setStPgTrOptsUpdateInterval(stPgTrOptsUpdateInterval);
			cmd.setSupportedMaps(supportedMaps);
			request.getSession().setAttribute(this.getCommandName(), cmd);
		}
		cmd.setInfoMessage(null);
		return cmd;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)
	 */
	@SuppressWarnings({ "rawtypes" })
	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
		Errors errors) throws Exception {
		OptionsCmd cmd = (OptionsCmd)command;
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
		OptionsCmd cmd = (OptionsCmd)command;						
		
		try {
			if (StringUtils.isEmpty(cmd.getUserOptions().getDefTrackName())) {
				cmd.getUserOptions().setDefTrackName(
					WebUtils.getMessage(request, UserOptionsVo.CODE_DEF_TRACK_NAME));
			}
			
			cmd.getActionExecutor().execute(
				this.applicationService,
				this.statusParamsService,
				this.userService,
				this.senderService,
				this.geocodingService, 
				this.smsService,
				this.trackService,
				WebUtils.getCurrentUserWithRole(), cmd,
				WebUtils.getMessageSource(request),
				WebUtils.getLocale(request));		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		updateCommandObject(request, cmd);
		
		return super.onSubmit(request, response, command, errors);
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request,
		ServletRequestDataBinder binder) throws Exception {
		CustomSenderSymbolEditor editor = new CustomSenderSymbolEditor();
		binder.registerCustomEditor(SenderSymbolVo.class, "senderDetails.symbol", editor);
		super.initBinder(request, binder);
	}

	/**
	 * @return the userOptsLanguage
	 */
	public List<StrOptionDsc> getUserOptsLanguage() {
		return userOptsLanguage;
	}

	/**
	 * @param userOptsLanguage the userOptsLanguage to set
	 */
	public void setUserOptsLanguage(List<StrOptionDsc> userOptsLanguage) {
		this.userOptsLanguage = userOptsLanguage;
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
	 * @return the trackOptsAutoClose
	 */
	public List<IntOptionDsc> getTrackOptsAutoClose() {
		return trackOptsAutoClose;
	}

	/**
	 * @param trackOptsAutoClose the trackOptsAutoClose to set
	 */
	public void setTrackOptsAutoClose(List<IntOptionDsc> trackOptsAutoClose) {
		this.trackOptsAutoClose = trackOptsAutoClose;
	}

	/**
	 * @return the trackRouteOptsWidth
	 */
	public List<IntOptionDsc> getTrackRouteOptsWidth() {
		return trackRouteOptsWidth;
	}

	/**
	 * @param trackRouteOptsWidth the trackRouteOptsWidth to set
	 */
	public void setTrackRouteOptsWidth(List<IntOptionDsc> trackRouteOptsWidth) {
		this.trackRouteOptsWidth = trackRouteOptsWidth;
	}

	/**
	 * @return the commonsOptsYesNo
	 */
	public List<BoolOptionDsc> getCommonsOptsYesNo() {
		return commonsOptsYesNo;
	}

	/**
	 * @param commonsOptsYesNo the commonsOptsYesNo to set
	 */
	public void setCommonsOptsYesNo(List<BoolOptionDsc> commonsOptsYesNo) {
		this.commonsOptsYesNo = commonsOptsYesNo;
	}

	/**
	 * @return the commonsOptsOnOff
	 */
	public List<BoolOptionDsc> getCommonsOptsOnOff() {
		return commonsOptsOnOff;
	}

	/**
	 * @param commonsOptsOnOff the commonsOptsOnOff to set
	 */
	public void setCommonsOptsOnOff(List<BoolOptionDsc> commonsOptsOnOff) {
		this.commonsOptsOnOff = commonsOptsOnOff;
	}

	/**
	 * @return the commonsOptsEnDisabled
	 */
	public List<BoolOptionDsc> getCommonsOptsEnDisabled() {
		return commonsOptsEnDisabled;
	}

	/**
	 * @param commonsOptsEnDisabled the commonsOptsEnDisabled to set
	 */
	public void setCommonsOptsEnDisabled(List<BoolOptionDsc> commonsOptsEnDisabled) {
		this.commonsOptsEnDisabled = commonsOptsEnDisabled;
	}

	/**
	 * @return the commonsOptsTimeZone
	 */
	public List<StrOptionDsc> getCommonsOptsTimeZone() {
		return commonsOptsTimeZone;
	}

	/**
	 * @param commonsOptsTimeZone the commonsOptsTimeZone to set
	 */
	public void setCommonsOptsTimeZone(List<StrOptionDsc> commonsOptsTimeZone) {
		this.commonsOptsTimeZone = commonsOptsTimeZone;
	}

	/**
	 * @return the senderOptsRunningMode
	 */
	public List<StrOptionDsc> getSenderOptsRunningMode() {
		return senderOptsRunningMode;
	}

	/**
	 * @param senderOptsRunningMode the senderOptsRunningMode to set
	 */
	public void setSenderOptsRunningMode(List<StrOptionDsc> senderOptsRunningMode) {
		this.senderOptsRunningMode = senderOptsRunningMode;
	}		

	/**
	 * @return the stPgTrOptsFlyToMode
	 */
	public List<IntOptionDsc> getStPgTrOptsFlyToMode() {
		return stPgTrOptsFlyToMode;
	}

	/**
	 * @param stPgTrOptsFlyToMode the stPgTrOptsFlyToMode to set
	 */
	public void setStPgTrOptsFlyToMode(List<IntOptionDsc> stPgTrOptsFlyToMode) {
		this.stPgTrOptsFlyToMode = stPgTrOptsFlyToMode;
	}

	/**
	 * @return the stPgTrOptsKeepRecentPos
	 */
	public List<IntOptionDsc> getStPgTrOptsKeepRecentPos() {
		return stPgTrOptsKeepRecentPos;
	}

	/**
	 * @param stPgTrOptsKeepRecentPos the stPgTrOptsKeepRecentPos to set
	 */
	public void setStPgTrOptsKeepRecentPos(
			List<IntOptionDsc> stPgTrOptsKeepRecentPos) {
		this.stPgTrOptsKeepRecentPos = stPgTrOptsKeepRecentPos;
	}
	
	/**
	 * @return the stPgTrOptsUpdateInterval
	 */
	public List<IntOptionDsc> getStPgTrOptsUpdateInterval() {
		return stPgTrOptsUpdateInterval;
	}

	/**
	 * @param stPgTrOptsUpdateInterval the stPgTrOptsUpdateInterval to set
	 */
	public void setStPgTrOptsUpdateInterval(
			List<IntOptionDsc> stPgTrOptsUpdateInterval) {
		this.stPgTrOptsUpdateInterval = stPgTrOptsUpdateInterval;
	}	

	/**
	 * @return the supportedMaps
	 */
	public List<IntOptionDsc> getSupportedMaps() {
		return supportedMaps;
	}

	/**
	 * @param supportedMaps the supportedMaps to set
	 */
	public void setSupportedMaps(List<IntOptionDsc> supportedMaps) {
		this.supportedMaps = supportedMaps;
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
	 * @return the geocodingService
	 */
	public AbstractGeocodingService getGeocodingService() {
		return geocodingService;
	}

	/**
	 * @param geocodingService the geocodingService to set
	 */
	public void setGeocodingService(AbstractGeocodingService geocodingService) {
		this.geocodingService = geocodingService;
	}

	/**
	 * @return the smsService
	 */
	public ISmsService getSmsService() {
		return smsService;
	}

	/**
	 * @param smsService the smsService to set
	 */
	public void setSmsService(ISmsService smsService) {
		this.smsService = smsService;
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
}
