package de.msk.mylivetracker.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import de.msk.mylivetracker.domain.user.MapsUsedVo;
import de.msk.mylivetracker.domain.user.UserAutoLoginVo;
import de.msk.mylivetracker.domain.user.UserPlainVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.IStatusParamsService;

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

	private static final Log log = LogFactory.getLog(UserDao.class);	
	
	private MessageSource messageSource;
	private PasswordEncoder passwordEncoder;
	private IStatusParamsService statusParamsService;
	private IApplicationService applicationService;
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#registerNewUser(de.msk.mylivetracker.domain.user.UserPlainVo)
	 */
	@Override
	public boolean registerNewUser(UserPlainVo user) {
		boolean userAlreadyExists = (getUserAux(user.getUserId(), 
			"UserVo.selectUserWithoutRoleByUserId") != null);
		if (!userAlreadyExists) {
			this.getSqlMapClientTemplate().insert("UserVo.registerUser", user);
			UserWithRoleVo userWithRole = this.getUserWithRole(user.getUserId());
			if (userWithRole != null) {
				log.debug("userPlainVo inserted: " + user.toString());
			} else {
				log.debug("insert userPlainVo FAILED");
			}
		}
		return !userAlreadyExists;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#getUserWithoutRole(java.lang.String)
	 */
	@Override
	public UserWithoutRoleVo getUserWithoutRole(String userId) {
		return getUserAux(userId, 
			"UserVo.selectUserWithoutRoleByUserId");		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#getUserWithoutRoleByEmailAddress(java.lang.String)
	 */
	@Override
	public UserWithoutRoleVo getUserWithoutRoleByEmailAddress(
			String emailAddress) {
		return getUserAux(emailAddress, 
			"UserVo.selectUserWithoutRoleByEmailAddress"); 			
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#getUserWithRole(java.lang.String)
	 */
	@Override
	public UserWithRoleVo getUserWithRole(String userId) {
		return (UserWithRoleVo)getUserAux(userId, "UserVo.selectUserWithRoleByUserId");
	}

	@Override
	public UserWithRoleVo getUserWithRoleByAutoLoginTicketForUser(
		String autoLoginTicket) {
		return (UserWithRoleVo)getUserAux(autoLoginTicket, 
			"UserVo.selectUserWithRoleByAutoLoginTicketForUser");
	}

	@Override
	public UserWithRoleVo getUserWithRoleByAutoLoginTicketForGuest(
		String autoLoginTicket) {
		return (UserWithRoleVo)getUserAux(autoLoginTicket, 
			"UserVo.selectUserWithRoleByAutoLoginTicketForGuest");
	}

	private UserWithoutRoleVo getUserAux(String searchCriteria, String selectStrId) {
		UserWithoutRoleVo user = (UserWithoutRoleVo)this.getSqlMapClientTemplate().
			queryForObject(selectStrId, searchCriteria);		
		if (user != null) {
			if (user.getAutoLogin() == null) {
				user.setAutoLogin(new UserAutoLoginVo());
				user.getAutoLogin().setDefaultValues();
				this.updateUserAutoLogin(user);	
			}
			if (BooleanUtils.isNotTrue(user.getOptions().getOptionsSaved())) {
				user.getOptions().setDefaultValues(messageSource);
				user.getEmergency().setDefaultValues();
				this.updateUserOptions(user);
				this.updateUserEmergency(user);
			}
			if (user.getOptions().getMapsUsed() == null) {
				user.getOptions().setMapsUsed(new MapsUsedVo());
				this.updateUserOptionsMapsUsed(user);
			}
			if (BooleanUtils.isNotTrue(user.getStatusPage().getStatusPageSaved())) {
				user.getStatusPage().setDefaultValues(
					this.statusParamsService, this.applicationService, user);
				this.updateUserStatusPage(user);
			}			
		}		
		return user;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateUserMasterData(de.msk.mylivetracker.domain.UserWithoutRoleVo)
	 */
	@Override
	public void updateUserMasterData(UserWithoutRoleVo user) {
		String passwordPlain = user.getMasterData().getPassword();
		if (!StringUtils.isEmpty(passwordPlain)) {
			user.getMasterData().setPassword(
				this.passwordEncoder.encodePassword(passwordPlain, user));
		}
		this.getSqlMapClientTemplate().update("UserVo.updateUserMasterDataByUserId", user);		
	}

	@Override
	public void updateUserAutoLogin(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateUserAutoLoginByUserId", user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateEmergency(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	public void updateUserEmergency(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateUserEmergencyByUserId", user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateUserStatusPage(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	public void updateUserStatusPage(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateUserStatusPageByUserId", user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#getUserCount(boolean)
	 */
	@Override
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
	public void updateLoginInfo(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateLoginInfoByUserId", user);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateUserOptions(de.msk.mylivetracker.domain.UserWithoutRoleVo)
	 */
	@Override
	public void updateUserOptions(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateUserOptionsByUserId", user);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#updateUserOptionsMapsUsed(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	public void updateUserOptionsMapsUsed(UserWithoutRoleVo user) {
		this.getSqlMapClientTemplate().update("UserVo.updateUserOptionsMapsUsedByUserId", user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.IUserDao#getEmailAddressesOfAllUsers()
	 */
	@Override
	public String getEmailAddressesOfAllUsers() {
		String res = "";
		@SuppressWarnings("unchecked")
		List<String> emailAddresses = (List<String>)
			this.getSqlMapClientTemplate().queryForList("UserVo.selectEmailAddressesOfAllUsers");
		if ((emailAddresses != null) && !emailAddresses.isEmpty()) {
			for (String emailAddress : emailAddresses) {
				if (!StringUtils.isEmpty(res)) {
					res += ";";
				}
				res += emailAddress;
			}
		}
		return res;
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
