package de.msk.mylivetracker.dao;

import java.util.List;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public List<ParameterVo> getAllParameters() {
		return this.getSqlMapClientTemplate().
			queryForList("Application.getAllParameters");
	}	
}
