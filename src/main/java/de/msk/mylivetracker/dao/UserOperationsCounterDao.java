package de.msk.mylivetracker.dao;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.user.UserOperationsCounterVo;

public class UserOperationsCounterDao extends SqlMapClientDaoSupport implements IUserOperationsCounterDao {

	private static final String SQL_SELECT_USER_OPERATIONS_COUNTER_BY_USERID = "UserOperationsCounterVo.selectUserOperationsCounterByUserId";
	private static final String SQL_UPDATE_USER_OPERATIONS_COUNTER = "UserOperationsCounterVo.updateUserOperationsCounter";
	private static final String SQL_DELETE_USER_OPERATIONS_COUNTER = "UserOperationsCounterVo.deleteUserOperationsCounter";
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE)
	public UserOperationsCounterVo getUserOperationsCounter(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be empty.");
		}
		return getUserOperationsCounterAux(userId);
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE)
	public void incCountTracksCreated(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be empty.");
		}
		UserOperationsCounterVo userOperationsCounter = 
			getUserOperationsCounterAux(userId);
		userOperationsCounter.incCountTracksCreated();
		this.updateUserOperationsCounterAux(userOperationsCounter);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE)
	public void update(String userId, DataReceivedVo dataReceived) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be empty.");
		}
		if (dataReceived == null) {
			throw new IllegalArgumentException("dataReceived must not be null.");
		}
		UserOperationsCounterVo userOperationsCounter = 
			getUserOperationsCounterAux(userId);
		userOperationsCounter.update(dataReceived);
		this.updateUserOperationsCounterAux(userOperationsCounter);		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void deleteUserOperationsCounter(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be empty.");
		}
		this.getSqlMapClientTemplate().delete(SQL_DELETE_USER_OPERATIONS_COUNTER, userId);	
	}
	
	/*************************************************************************/
	
	private UserOperationsCounterVo getUserOperationsCounterAux(String userId) {
		UserOperationsCounterVo userOperationsCounter = (UserOperationsCounterVo)
			this.getSqlMapClientTemplate().queryForObject(
				SQL_SELECT_USER_OPERATIONS_COUNTER_BY_USERID, userId);
		if (userOperationsCounter == null) {
			userOperationsCounter = UserOperationsCounterVo.createDefault(userId);
			this.updateUserOperationsCounterAux(userOperationsCounter);
		}
		return userOperationsCounter;
	}
	
	private void updateUserOperationsCounterAux(UserOperationsCounterVo userOperationsCounter) {
		this.getSqlMapClientTemplate().update(SQL_UPDATE_USER_OPERATIONS_COUNTER, userOperationsCounter);
	}
}
