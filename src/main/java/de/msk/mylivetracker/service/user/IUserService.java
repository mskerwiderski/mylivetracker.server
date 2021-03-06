package de.msk.mylivetracker.service.user;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;

/**
 * IUserService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 001
 * 
 * history
 * 001 2012-09-16 updateUserOptionsMapsUsed() added.
 * 000 2011-08-11 initial.
 * 
 */
public interface IUserService {
	public boolean insertUser(UserWithRoleVo user);
	public UserWithoutRoleVo getUserWithoutRole(String userId);
	public void updateUserOptions(UserWithoutRoleVo user);
	public void updateUserOptionsMapsAndRoutesUsed(UserWithoutRoleVo user);
	public void updateUserMasterData(UserWithoutRoleVo user, boolean updatePassword);
	public void updateUserAutoLogin(UserWithoutRoleVo user);
	public void updateUserStatusPage(UserWithoutRoleVo user);
	public void updateUserEmergency(UserWithoutRoleVo user);
	public Integer getUserCount(boolean adminsIncluded);
	public void deleteUser(String userId);
}
