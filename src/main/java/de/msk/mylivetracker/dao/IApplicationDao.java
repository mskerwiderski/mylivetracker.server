package de.msk.mylivetracker.dao;

import java.util.List;

import de.msk.mylivetracker.domain.ParameterVo;

/**
 * IApplicationDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IApplicationDao {
	
	/**
	 * get all parameters.
	 * @return Returns all parameters.
	 */
	 public List<ParameterVo> getAllParameters();	
}
