package de.msk.mylivetracker.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.msk.mylivetracker.domain.StatusParamsVo;

/**
 * StatusParamsDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class StatusParamsDao extends SqlMapClientDaoSupport implements IStatusParamsDao {
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IStatusParamsDao#getStatusParams(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public StatusParamsVo getStatusParams(String statusParamsId) {
		return (StatusParamsVo)this.getSqlMapClientTemplate().queryForObject(
			"StatusParams.getStatusParamsByStatusParamsId",
			statusParamsId);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void deleteAllStatusParamsOfUser(String userId) {
		this.getSqlMapClientTemplate().delete(
			"StatusParams.deleteStatusParamsByUserId", userId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IStatusParamsDao#saveStatusParams(de.msk.mylivetracker.domain.StatusParamsVo)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void saveStatusParams(StatusParamsVo statusParams) {
		this.getSqlMapClientTemplate().insert(
			"StatusParams.saveStatusParams", statusParams);
	}

}
