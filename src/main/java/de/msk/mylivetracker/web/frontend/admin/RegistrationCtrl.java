package de.msk.mylivetracker.web.frontend.admin;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.util.UsersLocaleResolver;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * RegistrationCtrl.
 * 
 * @author michael skerwiderski, (c)2015
 * 
 * @version 000
 * 
 * history
 * 000 initial 2015-03-13
 * 
 */
public class RegistrationCtrl extends ParameterizableViewController {
	
	Log log = LogFactory.getLog(RegistrationCtrl.class);
	
	private Services services;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.ParameterizableViewController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
		HttpServletResponse response) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();				

		String result = "";
		
		String userId = request.getParameter("userId");
		String lastName = request.getParameter("lastName");
		String firstName = request.getParameter("firstName");				
		String emailAddress = request.getParameter("emailAddress");
		String language = request.getParameter("language");
		if (StringUtils.isEmpty(emailAddress)) {
			result = "Register user failed: Email address must not be empty.";
		} else if (UsersLocaleResolver.getLocale(language) == null) {
			result = "Register user failed: Only the languages 'en' and 'de' are supported.";
		} else {
			boolean success = this.services.getAdminService().registerNewUser(
				WebUtils.getCurrentUserWithRole(), 
				userId, firstName, lastName, emailAddress, 
				language);
			
			if (success) {
				result = "New user successfully registered: " + userId;
			} else {
				result = "UserId already registered: " + userId;
			}
		}
	
		model.put("result", result);
				
		return new ModelAndView(this.getViewName(), model);
	}

	public Services getServices() {
		return services;
	}
	public void setServices(Services services) {
		this.services = services;
	}
}
