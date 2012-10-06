package de.msk.mylivetracker.dao;

import de.msk.mylivetracker.domain.user.UserSessionStatusVo;

/**
 * IUserSessionStatusDao.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 2012-10-03 initial.
 * 
 */
public interface IUserSessionStatusDao {
	public UserSessionStatusVo getUserSessionStatus(String userId);
	public void updateUserSessionStatus(UserSessionStatusVo userSessionStatus);
	public void deleteUserSessionStatus(String userId);
}
