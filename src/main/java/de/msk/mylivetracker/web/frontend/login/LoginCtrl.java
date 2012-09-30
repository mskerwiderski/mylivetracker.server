package de.msk.mylivetracker.web.frontend.login;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import de.msk.mylivetracker.web.util.TwitterUtils;
import de.msk.mylivetracker.web.util.UrlUtils;
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
public class LoginCtrl extends ParameterizableViewController {
	
	private static final Log log = LogFactory.getLog(LoginCtrl.class);
	
	public static final String REQUEST_PARAM_MESSAGE_CODE = "msgcode";
	public static final String ATTR_MESSAGE = "msg";	
	public static final String MESSAGE_LOGIN_FAILED = "login.failed";
	
	public static final String ATTR_TWITTER_MESSAGES = "twitterMessages";
	
	
	
	@Override
	public ModelAndView handleRequest(HttpServletRequest request,
		HttpServletResponse response) throws Exception {		
		
		String msgcode = request.getParameter(REQUEST_PARAM_MESSAGE_CODE);
		String message = "";
		if (!StringUtils.isEmpty(msgcode)) {
			message = WebUtils.getMessage(request, msgcode);
		}
		Map<String, Object> model = new HashMap<String, Object>();
		model.put(ATTR_MESSAGE, message);		
		log.debug("msgcode = " + msgcode);
		
		model.put(ATTR_TWITTER_MESSAGES, TwitterUtils.getTwitterMessages(3, 120));
		
		response.setHeader("Cache-Control", "no-cache");
		
		return new ModelAndView(UrlUtils.URL_LOGIN_VIEW, model);		
	}

	
}
