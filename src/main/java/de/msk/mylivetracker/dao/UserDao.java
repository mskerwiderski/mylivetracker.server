package de.msk.mylivetracker.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.statusparams.IStatusParamsService;

/**
 * UserDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 001
 * 
 * history
 * 001 2012-09-16 updateUserOptionsMapsUsed() added.
 * 000 2011-08-11 initial.
 * 
 */
public class UserDao extends SqlMapClientDaoSupport implements IUserDao {

	private MessageSource messageSource;
	private PasswordEncoder passwordEncoder;
	private IStatusParamsService statusParamsService;
	private IApplicationService applicationService;
	
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public boolean insertUser(UserWithRoleVo user) {
		if (user == null) {
			throw new IllegalArgumentException("user must not be null");
		}
		boolean userAlreadyExists = (getUserAux(user.getUserId(), 
			"UserVo.selectUserWithoutRoleByUserId") != null);
		if (!userAlreadyExists) {
			this.getSqlMapClientTemplate().insert("UserVo.insertUser", user);
		}
		return !userAlreadyExists;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#getUserWithoutRole(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public UserWithoutRoleVo getUserWithoutRole(String userId) {
		return getUserAux(userId, 
			"UserVo.selectUserWithoutRoleByUserId");		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#getUserWithoutRoleByEmailAddress(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public UserWithoutRoleVo getUserWithoutRoleByEmailAddress(
			String emailAddress) {
		return getUserAux(emailAddress, 
			"UserVo.selectUserWithoutRoleByEmailAddress"); 			
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#getUserWithRole(java.lang.String)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public UserWithRoleVo getUserWithRole(String userId) {
		return (UserWithRoleVo)getUserAux(userId, "UserVo.selectUserWithRoleByUserId");
	}

	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public UserWithRoleVo getUserWithRoleByAutoLoginTicketForUser(
		String autoLoginTicket) {
		return (UserWithRoleVo)getUserAux(autoLoginTicket, 
			"UserVo.selectUserWithRoleByAutoLoginTicketForUser");
	}

	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public UserWithRoleVo getUserWithRoleByAutoLoginTicketForGuest(
		String autoLoginTicket) {
		return (UserWithRoleVo)getUserAux(autoLoginTicket, 
			"UserVo.selectUserWithRoleByAutoLoginTicketForGuest");
	}

	/*
	 * not transactional. 
	 */
	private UserWithoutRoleVo getUserAux(String searchCriteria, String selectStrId) {
		UserWithoutRoleVo user = (UserWithoutRoleVo)this.getSqlMapClientTemplate().
			queryForObject(selectStrId, searchCriteria);		
		return user;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateUserMasterData(de.msk.mylivetracker.domain.UserWithoutRoleVo)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateUserMasterData(UserWithoutRoleVo user) {
		String passwordPlain = user.getMasterData().getPassword();
		if (!StringUtils.isEmpty(passwordPlain)) {
			user.getMasterData().setPassword(
				this.passwordEncoder.encodePassword(passwordPlain, user));
		}
		this.getSqlMapClientTemplate().update("UserVo.updateUserMasterDataByUserId", user);		
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateUserAutoLogin(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateUserAutoLoginByUserId", user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateEmergency(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateUserEmergency(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateUserEmergencyByUserId", user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateUserStatusPage(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateUserStatusPage(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateUserStatusPageByUserId", user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#getUserCount(boolean)
	 */
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public Integer getUserCount(boolean adminsIncluded) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("excludeRole", !adminsIncluded);
		params.put("role", UserWithRoleVo.UserRole.Admin);
		return (Integer)this.getSqlMapClientTemplate().
			queryForObject("UserVo.selectUserCount", params);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateLoginInfo(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateLoginInfo(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateLoginInfoByUserId", user);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateUserOptions(de.msk.mylivetracker.domain.UserWithoutRoleVo)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateUserOptions(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateUserOptionsByUserId", user);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateUserOptionsMapsUsed(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void updateUserOptionsMapsUsed(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateUserOptionsMapsUsedByUserId", user);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void deleteUser(String userId) {
		this.getSqlMapClientTemplate().delete("UserVo.deleteUser", userId);
	}

	/**
	 * @return the messageSource
	 */
	public MessageSource getMessageSource() {
		return messageSource;
	}

	/**
	 * @param messageSource the messageSource to set
	 */
	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	/**
	 * @return the passwordEncoder
	 */
	public PasswordEncoder getPasswordEncoder() {
		return passwordEncoder;
	}

	/**
	 * @param passwordEncoder the passwordEncoder to set
	 */
	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}	

	/**
	 * @return the statusParamsService
	 */
	public IStatusParamsService getStatusParamsService() {
		return statusParamsService;
	}

	/**
	 * @param statusParamsService the statusParamsService to set
	 */
	public void setStatusParamsService(IStatusParamsService statusParamsService) {
		this.statusParamsService = statusParamsService;
	}

	/**
	 * @return the applicationService
	 */
	public IApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}
}
