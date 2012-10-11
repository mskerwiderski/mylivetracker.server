package de.msk.mylivetracker.service.user;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.user.UserOperationsCounterVo;

/**
 * IUserOperationsCounterService.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 2012-10-11 initial.
 * 
 */
public interface IUserOperationsCounterService {
	public UserOperationsCounterVo getUserOperationsCounter(String userId);
	public void incCountTracksCreated(String userId);
	public void update(String userId, DataReceivedVo dataReceived);
	public void deleteUserOperationsCounter(String userId);
}
