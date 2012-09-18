package de.msk.mylivetracker.security;

import java.io.Serializable;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;

import de.msk.mylivetracker.domain.user.UserAutoLoginVo;
import de.msk.mylivetracker.domain.user.UserOptionsVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;

/**
 * PasswordEncoder.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class PasswordEncoder 
	implements org.springframework.security.authentication.encoding.PasswordEncoder,
	Serializable {

	private static final long serialVersionUID = -6428226417018889620L;

	public static String encode(String userId, 
		String realm, String plainPassword) {
		String encodedPassword = DigestUtils.md5Hex(
			userId + ":" +
			realm + ":" +
			plainPassword);
		return encodedPassword;
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.encoding.PasswordEncoder#encodePassword(java.lang.String, java.lang.Object)
	 */
	public String encodePassword(String plainPassword, Object salt)
			throws DataAccessException {
		UserWithRoleVo user = (UserWithRoleVo)salt;
		return encode(user.getUserId(),	user.getRealm(), plainPassword);		
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.authentication.encoding.PasswordEncoder#isPasswordValid(java.lang.String, java.lang.String, java.lang.Object)
	 */
	public boolean isPasswordValid(String password, String plainPassword, Object salt)
		throws DataAccessException {
		boolean res = false;
		UserWithRoleVo user = (UserWithRoleVo)salt;
		UserOptionsVo userOptions = user.getOptions();
		
		// check if this is a admin over user login.
		if (!StringUtils.isEmpty(user.getAdminUsername())) {
			res = StringUtils.equals(
				encode(user.getAdminUsername(), user.getRealm(), plainPassword), 
					user.getAdminPassword());	
			if (res) {
				user.setLoggedInAsAdmin(true);
			}
		} else if (BooleanUtils.isTrue(user.getAutoLogin().getAutoLoginEnabledForUser()) &&
			UserAutoLoginVo.isAutoLoginTicket(plainPassword) && 
			StringUtils.equals(user.getAutoLogin().getAutoLoginTicketForUser(), plainPassword)) {
			res = true;
		} else if (BooleanUtils.isTrue(user.getAutoLogin().getAutoLoginEnabledForGuest()) &&
				UserAutoLoginVo.isAutoLoginTicket(plainPassword) && 
				StringUtils.equals(user.getAutoLogin().getAutoLoginTicketForGuest(), plainPassword)) {
			res = true;
			user.setRole(UserWithRoleVo.UserRole.Guest);
		} else {
			// check if this is a normal user login.
			if (!res) {
				res = StringUtils.equals(encodePassword(plainPassword, salt), password);
			}
			
			// check if this is a valid guest login.
			if (!res && userOptions.getGuestAccEnabled()) {
				res = StringUtils.equals(plainPassword, userOptions.getGuestAccPassword());
				if (res) {
					// yes, this is a guest login.
					user.setRole(UserWithRoleVo.UserRole.Guest);
				}
			}
		}
		return res;			
	}

}
