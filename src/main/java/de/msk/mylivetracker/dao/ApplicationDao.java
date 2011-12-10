package de.msk.mylivetracker.dao;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import de.msk.mylivetracker.domain.ParameterVo;

/**
 * ApplicationDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ApplicationDao extends SqlMapClientDaoSupport implements IApplicationDao {
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IApplicationDao#getAllParameters()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ParameterVo> getAllParameters() {
		return this.getSqlMapClientTemplate().
			queryForList("Application.getAllParameters");
	}	
}
