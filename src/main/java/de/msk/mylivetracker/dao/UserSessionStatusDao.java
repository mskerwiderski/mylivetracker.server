package de.msk.mylivetracker.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import de.msk.mylivetracker.domain.user.UserSessionStatusVo;

/**
 * UserSessionStatusDao.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 2012-10-03 initial.
 * 
 */
public class UserSessionStatusDao extends SqlMapClientDaoSupport implements IUserSessionStatusDao {

	private static final String SQL_SELECT_USER_SESSION_STATUS_BY_USERID = "UserSessionStatusVo.selectUserSessionStatusByUserId";
	private static final String SQL_UPDATE_USER_SESSION_STATUS = "UserSessionStatusVo.updateUserSessionStatus";
	
	@Override
	public UserSessionStatusVo getUserSessionStatus(String userId) {
		return (UserSessionStatusVo)this.getSqlMapClientTemplate().
			queryForObject(SQL_SELECT_USER_SESSION_STATUS_BY_USERID, userId);
	}

	@Override
	public void updateUserSessionStatus(UserSessionStatusVo userSessionStatus) {
		this.getSqlMapClientTemplate().insert(SQL_UPDATE_USER_SESSION_STATUS, userSessionStatus);
	}
}
