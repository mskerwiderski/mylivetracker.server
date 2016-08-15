package de.msk.mylivetracker.service.sms;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.ResourceBundleMessageSource;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.statistics.SmsTransportVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.statistics.IStatisticsService;
import de.msk.mylivetracker.service.user.IUserService;
import de.msk.mylivetracker.web.util.FmtUtils;
import de.msk.mylivetracker.web.util.UsersLocaleResolver;

/**
 * SmsService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SmsService implements ISmsService {

	private long minPauseInMSecsForUserBetweenTwoSmsSent;
	private IApplicationService applicationService;
	private IUserService userService;
	private IStatisticsService statisticsService;	
	private ResourceBundleMessageSource messageSource;
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.sms.ISmsService#sendTestSms(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	public SendingSmsResult sendTestSms(UserWithoutRoleVo user) {
		PositionVo position = new PositionVo();
		position.setTimeReceived(new DateTime());
		position.setAddress("Marienplatz, 80331 München");
		position.setLatitudeInDecimal(48.137601);
		position.setLongitudeInDecimal(11.575438);
		position.setValid(true);
		return this.sendSms(user, position, 
			"sms.template.emergency.activated", true);		
	}	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.sms.ISmsService#sendEmergencyDeactivatedSms(de.msk.mylivetracker.domain.user.UserWithoutRoleVo, de.msk.mylivetracker.domain.DataReceivedVo)
	 */
	@Override
	public SendingSmsResult sendEmergencyDeactivatedSms(UserWithoutRoleVo user,
		DataReceivedVo dataReceived) {
		return this.sendSms(user, dataReceived.getPosition(),
			"sms.template.emergency.deactivated", false);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.sms.ISmsService#sendEmergencyActivatedSms(de.msk.mylivetracker.domain.user.UserWithoutRoleVo, de.msk.mylivetracker.domain.DataReceivedVo)
	 */
	@Override
	public SendingSmsResult sendEmergencyActivatedSms(UserWithoutRoleVo user,
		DataReceivedVo dataReceived) {
		return this.sendSms(user, dataReceived.getPosition(),
			"sms.template.emergency.activated", false);
	}

	private SendingSmsResult sendSms(UserWithoutRoleVo user, PositionVo position, 
		String messageTemplate, boolean test) {
		String text = user.getEmergency().getSmsMessageTemplate();
		text = StringUtils.replace(text, "#MLT", 
			this.applicationService.getApplicationBaseUrl());
		text = StringUtils.replace(text, "#UID", 
			user.getUserId());
		text = StringUtils.replace(text, "#POS", 
			FmtUtils.getPositionAsStr(position, UsersLocaleResolver.getLocale(user), false, false));
		text = StringUtils.replace(text, "#PWD", 
			user.getOptions().getGuestAccPassword());
		String positionStr = "<unknown>";
		if (position != null) {
			positionStr = 
				position.getLatitudeInDecimal() + "+" + 
				position.getLongitudeInDecimal();
		}
		text = StringUtils.replace(text, "#GOO", 
			"http://maps.google.com/maps?&t=m&q=" +
			positionStr);
		
		if (test) {
			text = "TEST! " + text; 
		}
		    
		String[] recipients = StringUtils.split(user.getEmergency().getSmsRecipient(), ";");
		SendingSmsResult result = null;
		boolean success = true;
		for (int i=0; success && (i < recipients.length); i++) {
			result = SmsServiceSpecific.sendSms(
				user, recipients[i], text,
				minPauseInMSecsForUserBetweenTwoSmsSent);
			success = result.isSuccess();
		}
		
		statisticsService.logSmsTransport(new SmsTransportVo(
			user.getUserId(), 
			user.getEmergency().getSmsServiceProvider(), 
			user.getEmergency().getSmsSender(), 
			user.getEmergency().getSmsRecipient(),
			text, 
			result.getCode(),
			success));
				
		if (success) {
			user.getEmergency().updateSmsSentCount(recipients.length);
		}
		
		this.userService.updateUserEmergency(user);
		
		return result;
	}	

	/**
	 * @return the minPauseInMSecsForUserBetweenTwoSmsSent
	 */
	public long getMinPauseInMSecsForUserBetweenTwoSmsSent() {
		return minPauseInMSecsForUserBetweenTwoSmsSent;
	}

	/**
	 * @param minPauseInMSecsForUserBetweenTwoSmsSent the minPauseInMSecsForUserBetweenTwoSmsSent to set
	 */
	public void setMinPauseInMSecsForUserBetweenTwoSmsSent(
			long minPauseInMSecsForUserBetweenTwoSmsSent) {
		this.minPauseInMSecsForUserBetweenTwoSmsSent = minPauseInMSecsForUserBetweenTwoSmsSent;
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
	 * @return the messageSource
	 */
	public ResourceBundleMessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @return the statisticsService
	 */
	public IStatisticsService getStatisticsService() {
		return statisticsService;
	}

	/**
	 * @param statisticsService the statisticsService to set
	 */
	public void setStatisticsService(IStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}
}
