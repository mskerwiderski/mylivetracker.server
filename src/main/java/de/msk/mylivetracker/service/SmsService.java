package de.msk.mylivetracker.service;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ResourceBundleMessageSource;

import de.cetix.SendSMS.Send;
import de.cetix.SendSMS.SendLocator;
import de.cetix.SendSMS.SendSoap;
import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.statistics.SmsTransportVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.IApplicationService.Parameter;
import de.msk.mylivetracker.service.statistics.IStatisticsService;
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
	
	// Versand mit variabler Absenderkennung.
	private static final String TRANSPORT_TYPE = "19";
	private static final String SMS_TYPE = "Emergency";
	private static final String BACKMAIL = "backmail:";	
	private static final String NUMBER_STRIP_CHARS = "()/-"; 
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ISmsService#sendTestSms(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	public void sendTestSms(UserWithoutRoleVo user) throws SmsServiceException {
		PositionVo position = new PositionVo();
		position.setTimeReceived(new DateTime());
		position.setAddress("Hauptstrasse, 12345 Musterhausen");
		position.setLatitudeInDecimal(44.4);
		position.setLongitudeInDecimal(11.1);
		position.setValid(true);
		this.sendSms(user, position, 
			"sms.template.emergency.activated", true);		
	}	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ISmsService#sendEmergencyDeactivatedSms(de.msk.mylivetracker.domain.user.UserWithoutRoleVo, de.msk.mylivetracker.domain.DataReceivedVo)
	 */
	@Override
	public void sendEmergencyDeactivatedSms(UserWithoutRoleVo user,
			DataReceivedVo dataReceived) throws SmsServiceException {
		this.sendSms(user, dataReceived.getPosition(),
			"sms.template.emergency.deactivated", false);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ISmsService#sendEmergencyActivatedSms(de.msk.mylivetracker.domain.user.UserWithoutRoleVo, de.msk.mylivetracker.domain.DataReceivedVo)
	 */
	@Override
	public void sendEmergencyActivatedSms(UserWithoutRoleVo user,
		DataReceivedVo dataReceived) throws SmsServiceException {				
		this.sendSms(user, dataReceived.getPosition(),
			"sms.template.emergency.activated", false);
	}

	private boolean checkMinPause(UserWithoutRoleVo user) {
		if (user.getEmergency().getSmsLastSent() == null) {
			return true;
		}
		return (
			((new DateTime()).getAsMSecs() -
			user.getEmergency().getSmsLastSent().getAsMSecs()) > 
			this.minPauseInMSecsForUserBetweenTwoSmsSent);
	}
	
	private void sendSms(UserWithoutRoleVo user, PositionVo position, 
		String messageTemplate, boolean test) throws SmsServiceException {
		String text = messageSource.getMessage(messageTemplate, 
			new Object[] {
				user.getUserId(),
				FmtUtils.getPositionAsStr(position, UsersLocaleResolver.getLocale(user), false, false),
				this.applicationService.getApplicationBaseUrl(),
				user.getUserId(),
				user.getOptions().getGuestAccPassword()
			}, 
			UsersLocaleResolver.getLocale(user.getOptions().getLanguage()));
		if (test) {
			text = "TEST! " + text; 
		}
		ByteBuffer textByteBuffer = Charset.forName("ISO-8859-1").encode(text);
		CharBuffer textCharBuffer = Charset.forName("ISO-8859-1").decode(textByteBuffer);
		text = textCharBuffer.toString();
		    
		String caption = BACKMAIL + user.getMasterData().getEmailAddress();
		String sender = StringUtils.replaceChars(
			user.getEmergency().getSmsSender(), NUMBER_STRIP_CHARS, null);
		String recipient = StringUtils.replaceChars(
			user.getEmergency().getSmsRecipient(), NUMBER_STRIP_CHARS, null);
		String resultCode = "<unknown>";
		boolean success = false;
		Exception exception = null;
		try {			
			if (!user.getEmergency().getSmsEnabled()) {
				throw new SmsServiceException("sending sms is not enabled for user.");
			}
			if (StringUtils.isEmpty(sender) || (StringUtils.isEmpty(recipient))) {
				throw new SmsServiceException("sender and recipient must not be empty.");
			}		
			if (checkMinPause(user)) {
				Send service = new SendLocator();
				SendSoap sendSoap = service.getSendSoap();
				resultCode = sendSoap.sendText(
					this.applicationService.getParameterValueAsString(Parameter.SmsCreatorDeUsername), 
					this.applicationService.getParameterValueAsString(Parameter.SmsCreatorDePassword), 
					BACKMAIL + user.getMasterData().getEmailAddress(), 
					sender, 
					recipient,
					text, 
					TRANSPORT_TYPE,	// typ 
					""); // date
				success = StringUtils.startsWith(resultCode, "OK");
			} else {
				success = true;
				resultCode = "OK, but not sent because checkMinPause returned false.";
			}
		} catch (Exception e) {
			exception = e;
			success = false;
		}
		statisticsService.logSmsTransport(new SmsTransportVo(
			user.getUserId(), 
			SMS_TYPE, 
			TRANSPORT_TYPE, 
			caption, 
			sender, 
			recipient, 
			text, 
			resultCode,
			(exception != null) ? exception.getMessage() : null,
			success));
		if (exception != null) {
			throw new SmsServiceException(exception);
		}
		if (!success) {
			throw new SmsServiceException(resultCode);	
		}		
		user.getEmergency().incSmsSentCount();
		this.userService.updateUserEmergency(user);
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
