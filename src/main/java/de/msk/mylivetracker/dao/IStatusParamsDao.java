package de.msk.mylivetracker.dao;

import de.msk.mylivetracker.domain.StatusParamsVo;

/**
 * IStatusParamsDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IStatusParamsDao {

	public void saveStatusParams(StatusParamsVo statusParams);
	public StatusParamsVo getStatusParams(String statusParamsId);
	public void removeAllStatusParamsOfUser(String userId);
}
