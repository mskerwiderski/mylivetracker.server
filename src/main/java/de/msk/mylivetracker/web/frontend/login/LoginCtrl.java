package de.msk.mylivetracker.web.frontend.login;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.SimpleFormController;

import de.msk.mylivetracker.domain.user.UserAutoLoginVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.IApplicationService.Parameter;
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
	
	public static final String REQUEST_PARAM_MESSAGE_CODE = "msgcode";
	public static final String MESSAGE_LOGIN_FAILED = "login.failed";

	private IApplicationService applicationService;

	@Override
	protected Object formBackingObject(HttpServletRequest request)
		throws Exception {
		LoginCmd cmd = (LoginCmd)
			request.getSession().getAttribute(this.getCommandName());
		if (cmd == null) {
			cmd = new LoginCmd();	
			cmd.setLoginResultMessage("");
			cmd.setAutoLoginUrlForDemoGuest(
				UserAutoLoginVo.createAutoLoginUrl(
					this.applicationService.getApplicationBaseUrl(), 
					this.applicationService.getParameterValueAsString(
						Parameter.AutoLoginTicketForDemoGuest)));
			cmd.setTwitterMessages(TwitterUtils.getTwitterMessages(3, 120));
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
		String msgcode = request.getParameter(REQUEST_PARAM_MESSAGE_CODE);
		String loginResultMessage = "";
		if (!StringUtils.isEmpty(msgcode)) {
			loginResultMessage = WebUtils.getMessage(request, msgcode);
		}
		cmd.setLoginResultMessage(loginResultMessage);
		log.debug(cmd);
		return super.referenceData(request, command, errors);
	}
	
	public IApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}
}
