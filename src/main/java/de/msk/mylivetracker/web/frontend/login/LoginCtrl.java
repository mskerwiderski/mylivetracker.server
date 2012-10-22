package de.msk.mylivetracker.web.frontend.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.SimpleFormController;

import de.msk.mylivetracker.domain.user.UserAutoLoginVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.service.application.IApplicationService.Parameter;
import de.msk.mylivetracker.web.util.TwitterUtils;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * LoginCtrl.
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
public class LoginCtrl extends SimpleFormController {
	
	private static final Log log = LogFactory.getLog(LoginCtrl.class);
	
	public static final String URL_LOGIN_VIEW = "login";
	public static final String URL_LOGIN_CTRL = "login.sec";
	public static final String URL_LOGIN = "login";
	public static final String URL_LOGOUT = "logout";
	
	public static final String REQUEST_PARAM_MESSAGE_CODE = "msgcode";
	public static final String MESSAGE_LOGIN_FAILED = "login.failed";

	private Services services;

	private void updateCommandObject(HttpServletRequest request, LoginCmd cmd) {
		String msgcode = request.getParameter(REQUEST_PARAM_MESSAGE_CODE);
		String loginResultMessage = "";
		if (!StringUtils.isEmpty(msgcode)) {
			loginResultMessage = WebUtils.getMessage(request, msgcode);
		}
		cmd.setLoginResultMessage(loginResultMessage);
		log.info("isLocaleGerman=" + WebUtils.isLocaleGerman(request));
		String autoLoginTicketForDemoGuest =
			WebUtils.isLocaleGerman(request) ? 
				this.services.getApplicationService().getParameterValueAsString(
					Parameter.AutoLoginTicketForDemoGuestDe) :
				this.services.getApplicationService().getParameterValueAsString(
					Parameter.AutoLoginTicketForDemoGuestEn);	
		log.info("autoLoginTicketForDemoGuest=" + autoLoginTicketForDemoGuest);			
		cmd.setAutoLoginUrlForDemoGuest(
			UserAutoLoginVo.createAutoLoginUrl(
				this.services.getApplicationService().getApplicationBaseUrl(), 
				autoLoginTicketForDemoGuest));
		cmd.setTwitterMessages(TwitterUtils.getTwitterMessages(3, 120));		
	}
	
	@Override
	protected Object formBackingObject(HttpServletRequest request)
		throws Exception {
		LoginCmd cmd = (LoginCmd)
			request.getSession().getAttribute(this.getCommandName());
		if (cmd == null) {
			cmd = new LoginCmd();	
			this.updateCommandObject(request, cmd);
			request.getSession().setAttribute(this.getCommandName(), cmd);
		}
		log.debug(cmd);
		return cmd;
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected Map referenceData(
		HttpServletRequest request, Object command,
		Errors errors) throws Exception {
		LoginCmd cmd = (LoginCmd)command;
		this.updateCommandObject(request, cmd);
		log.debug(cmd);
		return super.referenceData(request, command, errors);
	}

	public Services getServices() {
		return services;
	}
	public void setServices(Services services) {
		this.services = services;
	}
}
