package de.msk.mylivetracker.service.admin;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserObjectUtils;
import de.msk.mylivetracker.domain.user.UserObjectUtils.CreateUserWithRoleResult;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.application.IApplicationService.Parameter;
import de.msk.mylivetracker.service.user.IUserService;
import de.msk.mylivetracker.web.util.UsersLocaleResolver;

/**
 * AdminService.
 * 
 * @author michael skerwiderski, (c)2014
 * 
 * @version 000
 * 
 * history
 * 000 initial 2014-03-01
 * 
 */
public class AdminService implements IAdminService {

	private IApplicationService applicationService;
	private IUserService userService;
	
	private JavaMailSender javaMailSender;
	private VelocityEngine velocityEngine;
	private String emailAddressFrom;
	private String registrationEmailTemplateDe;
	private String registrationEmailTemplateEn;
	
	private static class MltMimeMessagePreperator implements MimeMessagePreparator {
		private VelocityEngine velocityEngine;
		private String emailAddressFrom;
		private String registrationEmailTemplateDe;
		private String registrationEmailTemplateEn;
		private UserWithRoleVo admin;
		private UserWithRoleVo user;
		private String plainPassword;
		
		public MltMimeMessagePreperator(
			VelocityEngine velocityEngine,
			String emailAddressFrom,
			String registrationEmailTemplateDe,
			String registrationEmailTemplateEn,
			UserWithRoleVo admin, UserWithRoleVo user,
			String plainPassword) {
			this.velocityEngine = velocityEngine;
			this.emailAddressFrom = emailAddressFrom;
			this.registrationEmailTemplateDe = registrationEmailTemplateDe;
			this.registrationEmailTemplateEn = registrationEmailTemplateEn;		
			this.admin = admin;
			this.user = user;
			this.plainPassword = plainPassword;
		}

		@Override
		public void prepare(MimeMessage mimeMessage) throws Exception {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(this.user.getMasterData().getEmailAddress());
            message.setBcc(this.admin.getMasterData().getEmailAddress());
            message.setFrom(this.emailAddressFrom);
            
            Map<String, String> model = new HashMap<String, String>();
            model.put("firstname", this.user.getMasterData().getFirstName());
            model.put("username", this.user.getUsername());
            model.put("password", this.plainPassword);
            String emailTemplate = this.registrationEmailTemplateEn;
            String subject = "Welcome to MyLiveTracker";
            if (UsersLocaleResolver.hasLocaleGerman(this.user)) {
            	emailTemplate = this.registrationEmailTemplateDe;
            	subject = "Willkommen bei MyLiveTracker";
            }
            String text = VelocityEngineUtils.mergeTemplateIntoString(
               velocityEngine, emailTemplate, model);
            message.setSubject(subject);
            message.setText(text, true);			
		}
	}
	
	@Override
	public boolean registerNewUser(UserWithRoleVo admin, String userId,
		String firstName, String lastName, String emailAddress,
		String languageCode) {
		
		CreateUserWithRoleResult createUserWithRoleResult =
				UserObjectUtils.createUserWithRole(
					userId, 
					this.applicationService.getParameterValueAsString(Parameter.ApplicationRealm), 
					UserWithRoleVo.UserRole.User, 
					languageCode, DateTime.TIME_ZONE_UTC, 
					lastName, firstName, emailAddress, 
					3);
		UserWithRoleVo user = createUserWithRoleResult.getUser();
		boolean success = this.userService.insertUser(user);
		if (success) {
			MltMimeMessagePreperator preparator = new MltMimeMessagePreperator(
				this.velocityEngine, this.emailAddressFrom,
				this.registrationEmailTemplateDe, this.registrationEmailTemplateEn,
				admin, user, createUserWithRoleResult.getPlainPassword());
			this.javaMailSender.send(preparator);
		}
		return success;
	}
	public IApplicationService getApplicationService() {
		return applicationService;
	}
	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}
	public IUserService getUserService() {
		return userService;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public String getRegistrationEmailTemplateDe() {
		return registrationEmailTemplateDe;
	}
	public void setRegistrationEmailTemplateDe(String registrationEmailTemplateDe) {
		this.registrationEmailTemplateDe = registrationEmailTemplateDe;
	}
	public String getRegistrationEmailTemplateEn() {
		return registrationEmailTemplateEn;
	}
	public void setRegistrationEmailTemplateEn(String registrationEmailTemplateEn) {
		this.registrationEmailTemplateEn = registrationEmailTemplateEn;
	}
	public JavaMailSender getJavaMailSender() {
		return javaMailSender;
	}
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}
	public String getEmailAddressFrom() {
		return emailAddressFrom;
	}
	public void setEmailAddressFrom(String emailAddressFrom) {
		this.emailAddressFrom = emailAddressFrom;
	}
}
