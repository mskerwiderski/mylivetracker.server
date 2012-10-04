package de.msk.mylivetracker.service;

import de.msk.mylivetracker.domain.user.UserSessionStatusVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;

/**
 * IUserSessionStatusService.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 2012-10-03 initial.
 * 
 */
public interface IUserSessionStatusService {
	public UserSessionStatusVo getUserSessionStatus(UserWithRoleVo user);
	public void updateUserSessionStatus(UserSessionStatusVo userSessionStatus);
}
