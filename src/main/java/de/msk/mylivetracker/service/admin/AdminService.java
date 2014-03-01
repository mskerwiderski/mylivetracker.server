package de.msk.mylivetracker.service.admin;

import java.util.HashMap;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
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
		
		public MltMimeMessagePreperator(
			VelocityEngine velocityEngine,
			String emailAddressFrom,
			String registrationEmailTemplateDe,
			String registrationEmailTemplateEn,
			UserWithRoleVo admin, UserWithRoleVo user) {
			this.velocityEngine = velocityEngine;
			this.emailAddressFrom = emailAddressFrom;
			this.registrationEmailTemplateDe = registrationEmailTemplateDe;
			this.registrationEmailTemplateEn = registrationEmailTemplateEn;		
			this.admin = admin;
			this.user = user;
		}

		@Override
		public void prepare(MimeMessage mimeMessage) throws Exception {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
            message.setTo(this.user.getMasterData().getEmailAddress());
            message.setBcc(this.admin.getMasterData().getEmailAddress());
            message.setFrom(this.emailAddressFrom);
            Map<String, String> model = new HashMap<String, String>();
            model.put("firstname", this.user.getMasterData().getFirstName());
            String emailTemplate = this.registrationEmailTemplateEn;
            if (UsersLocaleResolver.hasLocaleGerman(this.user)) {
            	emailTemplate = this.registrationEmailTemplateDe;
            }
            String text = VelocityEngineUtils.mergeTemplateIntoString(
               velocityEngine, emailTemplate, model);
            message.setText(text, true);			
		}
	}
	
	@Override
	public void sendRegistrationEmailToUser(
		UserWithRoleVo admin, UserWithRoleVo user) {
		MltMimeMessagePreperator preparator = new MltMimeMessagePreperator(
			this.velocityEngine, this.emailAddressFrom,
			this.registrationEmailTemplateDe, this.registrationEmailTemplateEn,
			admin, user);
		this.javaMailSender.send(preparator);		
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
