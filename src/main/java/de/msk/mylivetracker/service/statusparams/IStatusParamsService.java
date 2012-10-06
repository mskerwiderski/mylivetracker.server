package de.msk.mylivetracker.service.statusparams;

import de.msk.mylivetracker.domain.StatusParamsVo;

/**
 * IStatusParamsService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IStatusParamsService {

	public void saveStatusParams(StatusParamsVo statusParams);
	public StatusParamsVo getStatusParams(String statusParamsId);
	public void deleteAllStatusParamsOfUser(String userId);
}
