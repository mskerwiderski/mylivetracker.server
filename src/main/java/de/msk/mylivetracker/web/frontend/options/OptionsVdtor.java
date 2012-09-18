package de.msk.mylivetracker.web.frontend.options;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import de.msk.mylivetracker.domain.sender.SenderSwitchesVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.web.frontend.options.actionexecutor.ActionExecutor;

/**
 * OptionsVdtor.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 001 bugfix  2011-11-16 regular expression for email address check
 * 000 initial 2011-08-11
 * 
 */
public class OptionsVdtor implements Validator {

	private static int PASSWORD_MIN_LEN = 5;
	private static int PASSWORD_MAX_LEN = 20;
	private static int WINDOW_MIN_SIZE = 50;
	private static int WINDOW_MAX_SIZE = 3000;

	private ISenderService senderService;
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		 return OptionsCmd.class.equals(clazz);
	}

	private static boolean checkPasswordComplexity(String password) {
		boolean res = false;
		int pwdLen = StringUtils.length(password);
		res = (pwdLen >= PASSWORD_MIN_LEN) && (pwdLen <= PASSWORD_MAX_LEN);
		return res;
	}
	
	private static boolean isValidEmailAddress(String emailAddress){  
		if (StringUtils.isEmpty(emailAddress)) return false;
		String  expression="^([\\-\\.\\w])+[\\w]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";  
		CharSequence inputStr = emailAddress;  
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);  
		Matcher matcher = pattern.matcher(inputStr);  
		return matcher.matches();  
	} 
	
	private static boolean isValidPhoneNumber(String phoneNumber) {
		if (StringUtils.isEmpty(phoneNumber)) return false;
		return StringUtils.containsOnly(phoneNumber, "0123456789.-()/");
	}
	
	private static boolean isValidHomeAddressDetail(String detail) {
		if (StringUtils.isEmpty(detail)) return true;
		String  expression="^[ a-z0-9ŠšŸ§-]*$";
		CharSequence inputStr = detail;  
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);  
		Matcher matcher = pattern.matcher(inputStr);  
		return matcher.matches();  
	}
	
	private static boolean isValidHomePositionDetail(String detail) {
		if (StringUtils.isEmpty(detail)) return true;
		String  expression="^[+-]?\\d*\\.?\\d*$";
		CharSequence inputStr = detail;  
		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);  
		Matcher matcher = pattern.matcher(inputStr);  
		return matcher.matches();  
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		OptionsCmd cmd = (OptionsCmd)target;
		
		if (cmd.getActionExecutor().equals(ActionExecutor.SaveAllMasterData)) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, 
				"userMasterData.firstName", "masterdata.error.first.name.invalid");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, 
				"userMasterData.lastName", "masterdata.error.last.name.invalid");
					
			if (!isValidEmailAddress(cmd.getUserMasterData().getEmailAddress())) {
				errors.rejectValue("userMasterData.emailAddress", 
					"masterdata.error.email.address.invalid");			
			}					
			
			String password = cmd.getUserMasterData().getPassword();
			String retypedPassword = cmd.getRetypedPassword();
			
			if (!StringUtils.isEmpty(password) ||
				!StringUtils.isEmpty(retypedPassword)) {
				if (!password.equals(retypedPassword)) {
					errors.rejectValue("retypedPassword", 
						"masterdata.error.retyped.password.invalid");					
				} else if (!checkPasswordComplexity(password)) {
					errors.rejectValue("retypedPassword", 
						"masterdata.error.password.invalid", 
						new Object[] { PASSWORD_MIN_LEN, PASSWORD_MAX_LEN },
						"INVALID PASSWORD");
				}
			}
		} else if (cmd.getActionExecutor().equals(ActionExecutor.SaveAllOptions)) {
			if (!isValidHomeAddressDetail(cmd.getUserOptions().getHomeLocCountry()) ||
				!isValidHomeAddressDetail(cmd.getUserOptions().getHomeLocCity()) ||
				!isValidHomeAddressDetail(cmd.getUserOptions().getHomeLocStreet()) ||
				!isValidHomeAddressDetail(cmd.getUserOptions().getHomeLocHousenumber())) {
				errors.rejectValue("userOptions.homeLocAddress", 
					"options.home.location.address.error.invalid");			
			}
			if (!isValidHomePositionDetail(cmd.getHomeLocLatitudeStr()) ||
				!isValidHomePositionDetail(cmd.getHomeLocLongitudeStr())) {
				errors.rejectValue("userOptions.homeLocLatitude", 
					"options.home.location.position.error.invalid");
			} else {
				if (StringUtils.isEmpty(cmd.getHomeLocLatitudeStr())) {
					cmd.getUserOptions().setHomeLocLatitude(null);
				} else {
					cmd.getUserOptions().setHomeLocLatitude(Double.valueOf(cmd.getHomeLocLatitudeStr()));
				}
				if (StringUtils.isEmpty(cmd.getHomeLocLatitudeStr())) {
					cmd.getUserOptions().setHomeLocLongitude(null);
				} else {
					cmd.getUserOptions().setHomeLocLongitude(Double.valueOf(cmd.getHomeLocLongitudeStr()));
				}
			}
		} else if (cmd.getActionExecutor().equals(ActionExecutor.CheckHomeLocation)) {
			if (!isValidHomeAddressDetail(cmd.getUserOptions().getHomeLocCountry()) ||
				!isValidHomeAddressDetail(cmd.getUserOptions().getHomeLocCity()) ||
				!isValidHomeAddressDetail(cmd.getUserOptions().getHomeLocStreet()) ||
				!isValidHomeAddressDetail(cmd.getUserOptions().getHomeLocHousenumber())) {
				errors.rejectValue("userOptions.homeLocAddress", 
					"options.home.location.address.error.invalid");			
			}
		} else if (cmd.getActionExecutor().equals(ActionExecutor.UpdateSender) ||
			cmd.getActionExecutor().equals(ActionExecutor.AddSender)) {
			// sender limit check only if the user tries to add a new sender.
			int cntSender = cmd.getSenderEntries().size() - 1; // minus 'no sender selected' entry.
			if (cmd.getActionExecutor().equals(ActionExecutor.AddSender) &&
				cmd.getSenderLimit() <= cntSender) {
				errors.rejectValue("selectedSenderId", 
					"sendermaintenance.error.sender.limit.exceeded");
			} 
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, 
				"senderDetails.senderId", "sendermaintenance.error.sender.details.senderid.invalid");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, 
				"senderDetails.name", "sendermaintenance.error.sender.details.name.invalid");
			if (cmd.getSenderDetails().isAuthRequired()) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, 
					"senderDetails.authUsername", "sendermaintenance.error.sender.details.auth.username.invalid");
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, 
					"senderDetails.authPlainPassword", "sendermaintenance.error.sender.details.auth.plain.password.invalid");
			}
			if (cmd.getActionExecutor().equals(ActionExecutor.AddSender)) {
				SenderVo sender = this.senderService.getSender(cmd.getSenderDetails().getSenderId());
				if (sender != null) {
					if (sender.getUserId().equals(cmd.getSenderDetails().getUserId())) {
						errors.rejectValue("senderDetails.senderId", 
							"sendermaintenance.error.sender.details.senderid.already.exists.local");
					} else {
						errors.rejectValue("senderDetails.senderId", 
							"sendermaintenance.error.sender.details.senderid.already.exists.global");
					}
				}
			}
			if (!SenderSwitchesVo.isValid(cmd.getSenderDetails().getSwitches())) {
				errors.rejectValue("senderDetails.switches", 
					"sendermaintenance.error.sender.details.switches.invalid");
			}
		} else if (cmd.getActionExecutor().equals(ActionExecutor.SaveAllStatusPage)) {
			Integer width = cmd.getUserStatusPage().getWindowWidth();
			Integer height = cmd.getUserStatusPage().getWindowHeight();
			if (BooleanUtils.isFalse(cmd.getUserStatusPage().getFullScreen())) {
				if ((width == null) || (width < WINDOW_MIN_SIZE) || (width > WINDOW_MAX_SIZE)) {
					errors.rejectValue("userStatusPage.windowWidth", 
						"statuspage.error.window.size.width.invalid",
						new Object[] { WINDOW_MIN_SIZE, WINDOW_MAX_SIZE }, 
						"INVALID WIDTH");
				}
				if ((height == null) || (height < WINDOW_MIN_SIZE) || (height > WINDOW_MAX_SIZE)) {
					errors.rejectValue("userStatusPage.windowHeight", 
						"statuspage.error.window.size.height.invalid",
						new Object[] { WINDOW_MIN_SIZE, WINDOW_MAX_SIZE }, 
						"INVALID HEIGHT");
				}
			}			
		} else if (cmd.getActionExecutor().equals(ActionExecutor.SaveAllMaps)) {
			if (!cmd.getUserOptions().getMapsUsed().isDefMapIdValid()) {
				errors.rejectValue("userOptions.mapsUsed.defMapId", 
					"maps.error.defaultMap.invalid");
			}
		} else if (cmd.getActionExecutor().equals(ActionExecutor.SaveAllEmergency)) {
			if (cmd.getUserEmergency().getSmsEnabled() &&
				!isValidPhoneNumber(cmd.getUserEmergency().getSmsSender())) {
				errors.rejectValue("userEmergency.smsSender", 
					"emergency.error.number.invalid");
			}			
			if (cmd.getUserEmergency().getSmsEnabled() &&
				!isValidPhoneNumber(cmd.getUserEmergency().getSmsRecipient())) {
				errors.rejectValue("userEmergency.smsRecipient", 
					"emergency.error.number.invalid");
			}
		} else if (cmd.getActionExecutor().equals(ActionExecutor.SaveAndSendTestSms)) {
			if (!cmd.getUserEmergency().getSmsEnabled()) {
				errors.rejectValue("userEmergency.smsEnabled", 
					"emergency.error.sms.not.enabled");
			}
		}
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
