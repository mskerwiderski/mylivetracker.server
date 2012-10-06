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
import de.msk.mylivetracker.domain.user.GeocoderModeVo;
import de.msk.mylivetracker.domain.user.UserOptionsVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.util.CustomGeocoderModeEditor;
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
	private List<StrOptionDsc> userOptsGeocoder;
	private List<BoolOptionDsc> trackOptsReleaseStatus;
	private List<IntOptionDsc> trackOptsAutoClose;
	private List<IntOptionDsc> trackRouteOptsWidth;
	private List<BoolOptionDsc> commonsOptsYesNo;
	private List<BoolOptionDsc> commonsOptsOnOff;
	private List<BoolOptionDsc> commonsOptsEnDisabled;
	private List<StrOptionDsc> commonsOptsTimeZone;
	private List<StrOptionDsc> senderOptsRunningMode;
	private List<StrOptionDsc> stPgTrOptsFlyToMode;
	private List<IntOptionDsc> stPgTrOptsKeepRecentPos;
	private List<IntOptionDsc> stPgTrOptsUpdateInterval;
	private List<IntOptionDsc> supportedMaps;
	
	private Services services;
	
	private void updateCommandObject(HttpServletRequest request, OptionsCmd cmd) {	
		UserWithoutRoleVo user = 
			this.services.getUserService().getUserWithoutRole(
			WebUtils.getCurrentUserWithRole().getUserId());
		List<SenderVo> senderList = 
			this.services.getSenderService().getSenders(user.getUserId());
		cmd.buildUpSenderEntries(request, senderList);
		cmd.buildUpSymbolEntries(request, senderList);
		cmd.buildUpLinks(request, 
			this.services.getApplicationService().getApplicationBaseUrl(), 
			user);
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
			cmd.setUserOptsGeocoder(userOptsGeocoder);
			cmd.setTrackOptsReleaseStatus(trackOptsReleaseStatus);
			cmd.setTrackOptsAutoClose(trackOptsAutoClose);
			cmd.setTrackRouteOptsWidth(trackRouteOptsWidth);			
			UserWithoutRoleVo user = 
				this.services.getUserService().getUserWithoutRole(
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
		String redirectUrl = null;
		try {
			if (StringUtils.isEmpty(cmd.getUserOptions().getDefTrackName())) {
				cmd.getUserOptions().setDefTrackName(
					WebUtils.getMessage(request, UserOptionsVo.CODE_DEF_TRACK_NAME));
			}
			
			redirectUrl = 
				cmd.getActionExecutor().execute(
				this.services,
				WebUtils.getCurrentUserWithRole(), cmd,
				WebUtils.getMessageSource(request),
				WebUtils.getLocale(request));		
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

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest, org.springframework.web.bind.ServletRequestDataBinder)
	 */
	@Override
	protected void initBinder(HttpServletRequest request,
		ServletRequestDataBinder binder) throws Exception {
		CustomSenderSymbolEditor editorSenderSymbol = new CustomSenderSymbolEditor();
		binder.registerCustomEditor(SenderSymbolVo.class, "senderDetails.symbol", editorSenderSymbol);
		CustomGeocoderModeEditor editorGeocoderMode = new CustomGeocoderModeEditor();
		binder.registerCustomEditor(GeocoderModeVo.class, "userOptions.geocoderMode", editorGeocoderMode);
		super.initBinder(request, binder);
	}

	public Services getServices() {
		return services;
	}
	public void setServices(Services services) {
		this.services = services;
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
	 * @return the userOptsGeocoder
	 */
	public List<StrOptionDsc> getUserOptsGeocoder() {
		return userOptsGeocoder;
	}

	/**
	 * @param userOptsGeocoder the userOptsGeocoder to set
	 */
	public void setUserOptsGeocoder(List<StrOptionDsc> userOptsGeocoder) {
		this.userOptsGeocoder = userOptsGeocoder;
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

	public List<StrOptionDsc> getStPgTrOptsFlyToMode() {
		return stPgTrOptsFlyToMode;
	}

	public void setStPgTrOptsFlyToMode(List<StrOptionDsc> stPgTrOptsFlyToMode) {
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
}
