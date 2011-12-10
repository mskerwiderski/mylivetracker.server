package de.msk.mylivetracker.dao;

import de.msk.mylivetracker.domain.user.UserPlainVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;

/**
 * IUserDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IUserDao {
	public boolean registerNewUser(UserPlainVo user);
	public UserWithoutRoleVo getUserWithoutRole(String userId);
	public UserWithoutRoleVo getUserWithoutRoleByEmailAddress(String emailAddress);
	public UserWithRoleVo getUserWithRole(String userId);
	public void updateLoginInfo(UserWithoutRoleVo user);
	public void updateUserOptions(UserWithoutRoleVo user);
	public void updateUserMasterData(UserWithoutRoleVo user);
	public void updateUserStatusPage(UserWithoutRoleVo user);
	public void updateUserEmergency(UserWithoutRoleVo user);
	public Integer getUserCount(boolean adminsIncluded);	
	public String getEmailAddressesOfAllUsers();
}
