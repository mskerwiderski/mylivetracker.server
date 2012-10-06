package de.msk.mylivetracker.service.statusparams;

import de.msk.mylivetracker.dao.IStatusParamsDao;
import de.msk.mylivetracker.domain.StatusParamsVo;

/**
 * StatusParamsService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class StatusParamsService implements IStatusParamsService {

	private IStatusParamsDao statusParamsDao;
	
	@Override
	public StatusParamsVo getStatusParams(String statusParamsId) {
		return this.statusParamsDao.getStatusParams(statusParamsId);
	}

	@Override
	public void deleteAllStatusParamsOfUser(String userId) {
		this.statusParamsDao.deleteAllStatusParamsOfUser(userId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IStatusParamsService#saveStatusParams(de.msk.mylivetracker.domain.StatusParamsVo)
	 */
	@Override
	public void saveStatusParams(StatusParamsVo statusParams) {
		this.statusParamsDao.saveStatusParams(statusParams);
	}

	/**
	 * @return the statusParamsDao
	 */
	public IStatusParamsDao getStatusParamsDao() {
		return statusParamsDao;
	}

	/**
	 * @param statusParamsDao the statusParamsDao to set
	 */
	public void setStatusParamsDao(IStatusParamsDao statusParamsDao) {
		this.statusParamsDao = statusParamsDao;
	}
}
