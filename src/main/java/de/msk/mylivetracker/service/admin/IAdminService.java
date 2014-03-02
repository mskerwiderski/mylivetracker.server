package de.msk.mylivetracker.service.admin;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;

/**
 * IAdminService.
 * 
 * @author michael skerwiderski, (c)2014
 * 
 * @version 000
 * 
 * history
 * 000 initial 2014-03-01
 * 
 */
public interface IAdminService {

	public boolean registerNewUser(UserWithRoleVo admin,
		String userId, String firstName, String lastName, 
		String emailAddress, String languageCode);
}
