package de.msk.mylivetracker.service.user;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.dao.IUserOperationsCounterDao;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.user.UserOperationsCounterVo;

/**
 * UserOperationsCounterService.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 2012-10-11 initial.
 * 
 */
public class UserOperationsCounterService implements IUserOperationsCounterService {

	private IUserOperationsCounterDao userOperationsCounterDao;

	@Override
	public UserOperationsCounterVo getUserOperationsCounter(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be empty.");
		}
		return userOperationsCounterDao.getUserOperationsCounter(userId);
	}

	@Override
	public void incCountTracksCreated(String userId) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be empty.");
		}
		this.userOperationsCounterDao.incCountTracksCreated(userId);
	}

	@Override
	public void update(String userId, DataReceivedVo dataReceived) {
		if (StringUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("userId must not be empty.");
		}
		if (dataReceived == null) {
			throw new IllegalArgumentException("dataReceived must not be null.");
		}
		this.userOperationsCounterDao.update(userId, dataReceived);
	}

	@Override
	public void deleteUserOperationsCounter(String userId) {
		userOperationsCounterDao.deleteUserOperationsCounter(userId);		
	}

	public IUserOperationsCounterDao getUserOperationsCounterDao() {
		return userOperationsCounterDao;
	}

	public void setUserOperationsCounterDao(
			IUserOperationsCounterDao userOperationsCounterDao) {
		this.userOperationsCounterDao = userOperationsCounterDao;
	}
}
