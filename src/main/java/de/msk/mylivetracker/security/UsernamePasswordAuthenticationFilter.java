package de.msk.mylivetracker.security;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.user.UserAutoLoginVo;
import de.msk.mylivetracker.web.util.request.ReqParam;

/**
 * UsernamePasswordAuthenticationFilter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UsernamePasswordAuthenticationFilter
	extends
	org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter {
	
	private static final String USERID_KEY = "plainUserId";
    private static final String PASSWORD_KEY = "plainPassword";
    
    public static final ReqParam<String> PARAM_USER_ID = 
		new ReqParam<String>(USERID_KEY, String.class);
    	
    public static final ReqParam<String> PARAM_PASSWORD = 
		new ReqParam<String>(PASSWORD_KEY, String.class);
    
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter#obtainUsername(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String obtainUsername(HttpServletRequest request) {
		return PARAM_USER_ID.getValueFromReq(request);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter#obtainPassword(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected String obtainPassword(HttpServletRequest request) {
		String res = PARAM_PASSWORD.getValueFromReq(request);
		if (StringUtils.isEmpty(res)) {
			String userId = PARAM_USER_ID.getValueFromReq(request);
			if (UserAutoLoginVo.isAutoLoginTicket(userId)) {
				res = userId;
			}
		}
		return res;
	}
		
}
