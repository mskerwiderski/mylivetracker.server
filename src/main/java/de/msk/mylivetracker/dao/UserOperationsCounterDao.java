package de.msk.mylivetracker.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.user.UserOperationsCounterVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;

public class UserOperationsCounterDao extends SqlMapClientDaoSupport implements IUserOperationsCounterDao {

	private static final String SQL_SELECT_USER_OPERATIONS_COUNTER_BY_USERID = "UserOperationsCounterVo.selectUserOperationsCounterByUserId";
	private static final String SQL_INSERT_USER_OPERATIONS_COUNTER = "UserOperationsCounterVo.insertUserOperationsCounter";
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
	public void incVersion(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be empty.");
		}
		Map<String, Object> updateParamsMap =
			this.getUpdateParamsMap(userId);
		this.updateUserOperationsCounterAux(userId, updateParamsMap);		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE)
	public void incCountLogin(UserWithRoleVo user) {
		if (user == null) {
			throw new IllegalArgumentException("user must not be null.");
		}
		Map<String, Object> updateParamsMap = null;
		if ((user.getRole().equals(UserRole.User) ||
			user.getRole().equals(UserRole.Admin)) && 
			StringUtils.isEmpty(user.getAdminUsername())) {
			updateParamsMap =
				this.getUpdateParamsMap(user.getUserId(), 
				"countLoginUser");
		} else if (user.getRole().equals(UserRole.Guest)) {
			updateParamsMap =
				this.getUpdateParamsMap(user.getUserId(), 
				"countLoginGuest");
		}
		if (updateParamsMap != null) {
			this.updateUserOperationsCounterAux(user.getUserId(), 
				updateParamsMap);
		}
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE)
	public void incCountTracksCreated(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be empty.");
		}
		Map<String, Object> updateParamsMap =
			this.getUpdateParamsMap(userId, "countTracksCreated");
		this.updateUserOperationsCounterAux(userId, updateParamsMap);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE)
	public void incCountTracksDeleted(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be empty.");
		}
		Map<String, Object> updateParamsMap =
			this.getUpdateParamsMap(userId, "countTracksDeleted");
		this.updateUserOperationsCounterAux(userId, updateParamsMap);
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
		Map<String, Object> updateParamsMap =
			this.getUpdateParamsMap(userId, dataReceived);
		this.updateUserOperationsCounterAux(userId, updateParamsMap);		
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
			this.insertUserOperationsCounterAux(userOperationsCounter);
		}
		return userOperationsCounter;
	}
	
	private void insertUserOperationsCounterAux(UserOperationsCounterVo userOperationsCounter) {
		this.getSqlMapClientTemplate().update(
			SQL_INSERT_USER_OPERATIONS_COUNTER, userOperationsCounter);
	}
	
	private void updateUserOperationsCounterAux(String userId, 
		Map<String, Object> updateParamsMap) {
		this.getUserOperationsCounterAux(userId);
		this.getSqlMapClientTemplate().update(
			SQL_UPDATE_USER_OPERATIONS_COUNTER, updateParamsMap);
	}
	
	public Map<String, Object> getUpdateParamsMap(String userId, String...properties) {
		if (StringUtils.isEmpty(userId)) return null;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", userId);
		params.put("lastUpdated", new DateTime());
		for (String property : properties) {
			params.put(property, Boolean.TRUE);
		}
		return params;
	}
	
	public Map<String, Object> getUpdateParamsMap(String userId, DataReceivedVo dataReceived) {
		if (StringUtils.isEmpty(userId)) return null;
		if ((dataReceived == null) || !dataReceived.isValid()) return null;
		Map<String, Object> params = getUpdateParamsMap(userId);
		if (dataReceived.hasValidPosition()) {
			params.put("countPositionsReceived", Boolean.TRUE);
		}
		if (dataReceived.hasValidMessage()) {
			params.put("countMessagesReceived", Boolean.TRUE);
		}
		if (dataReceived.hasValidMobNwCell()) {
			params.put("countMobNwCellsReceived", Boolean.TRUE);
		}
		if (dataReceived.hasValidSenderState()) {
			params.put("countSenderStatesReceived", Boolean.TRUE);
		}
		if (dataReceived.hasValidCardiacFunction()) {
			params.put("countCardiacFunctionsReceived", Boolean.TRUE);
		}
		if (dataReceived.hasValidEmergencySignal()) {
			params.put("countEmergencySignalsReceived", Boolean.TRUE);
		}
		if (dataReceived.hasValidClientInfo()) {
			params.put("countClientInfosReceived", Boolean.TRUE);
		}
		return params;
	}
}
