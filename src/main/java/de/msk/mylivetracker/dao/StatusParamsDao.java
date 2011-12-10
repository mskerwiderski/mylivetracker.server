package de.msk.mylivetracker.dao;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

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
	public StatusParamsVo getStatusParams(String statusParamsId) {
		return (StatusParamsVo)this.getSqlMapClientTemplate().queryForObject(
			"StatusParams.getStatusParamsByStatusParamsId",
			statusParamsId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IStatusParamsDao#removeAllStatusParamsOfUser(java.lang.String)
	 */
	@Override
	public void removeAllStatusParamsOfUser(String userId) {
		this.getSqlMapClientTemplate().delete(
			"StatusParams.removeStatusParamsByUserId", userId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IStatusParamsDao#saveStatusParams(de.msk.mylivetracker.domain.StatusParamsVo)
	 */
	@Override
	public void saveStatusParams(StatusParamsVo statusParams) {
		this.getSqlMapClientTemplate().insert(
			"StatusParams.saveStatusParams", statusParams);
	}

}
