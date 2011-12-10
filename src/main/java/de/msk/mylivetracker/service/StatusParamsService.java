package de.msk.mylivetracker.service;

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
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IStatusParamsService#getStatusParams(java.lang.String)
	 */
	@Override
	public StatusParamsVo getStatusParams(String statusParamsId) {
		return this.statusParamsDao.getStatusParams(statusParamsId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IStatusParamsService#removeAllStatusParamsOfUser(java.lang.String)
	 */
	@Override
	public void removeAllStatusParamsOfUser(String userId) {
		this.statusParamsDao.removeAllStatusParamsOfUser(userId);
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
