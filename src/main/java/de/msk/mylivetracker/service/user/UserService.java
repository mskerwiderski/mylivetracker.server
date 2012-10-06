package de.msk.mylivetracker.service.user;

import java.io.Serializable;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import de.msk.mylivetracker.dao.IUserDao;
import de.msk.mylivetracker.domain.user.UserAutoLoginVo;
import de.msk.mylivetracker.domain.user.UserPlainVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo.UserRole;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.application.IApplicationService.Parameter;
import de.msk.mylivetracker.service.sender.ISenderService;

/**
 * UserService.
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
public class UserService implements
	IUserService,
	org.springframework.security.core.userdetails.UserDetailsService, 
	Serializable {

	private static final long serialVersionUID = 2670206502752377050L;
	
	private IApplicationService applicationService;
	private ISenderService senderService;
	private Cache userWithoutRoleCache;
	private IUserDao userDao;
			
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUserService#registerNewUser(de.msk.mylivetracker.domain.user.UserPlainVo)
	 */
	@Override
	public boolean registerNewUser(UserPlainVo user) {
		return this.userDao.registerNewUser(user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUserService#getUserWithoutRole(java.lang.String)
	 */
	@Override
	public UserWithoutRoleVo getUserWithoutRole(String userId) {
		UserWithoutRoleVo user = null;
		if (userWithoutRoleCache.isKeyInCache(userId)) {
			user = (UserWithoutRoleVo)userWithoutRoleCache.get(userId).getObjectValue();
		} else {
			user = this.userDao.getUserWithoutRole(userId);
			userWithoutRoleCache.put(new Element(userId, user));
		}
		return this.userDao.getUserWithoutRole(userId);
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUserService#updateLoginInfo(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	public void updateLoginInfo(UserWithoutRoleVo user) {
		userWithoutRoleCache.remove(user.getUserId());
		userDao.updateLoginInfo(user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUserService#updateUserMasterData(de.msk.mylivetracker.domain.UserWithoutRoleVo)
	 */
	@Override
	public void updateUserMasterData(UserWithoutRoleVo user) {
		userWithoutRoleCache.remove(user.getUserId());
		userDao.updateUserMasterData(user);
	}
	
	@Override
	public void updateUserAutoLogin(UserWithoutRoleVo user) {
		userWithoutRoleCache.remove(user.getUserId());
		userDao.updateUserAutoLogin(user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUserService#updateEmergency(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	public void updateUserEmergency(UserWithoutRoleVo user) {
		userWithoutRoleCache.remove(user.getUserId());
		userDao.updateUserEmergency(user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUserService#updateUserOptions(de.msk.mylivetracker.domain.UserWithoutRoleVo)
	 */
	@Override
	public void updateUserOptions(UserWithoutRoleVo user) {
		userWithoutRoleCache.remove(user.getUserId());
		userDao.updateUserOptions(user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUserService#updateUserOptionsMapsUsed(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	public void updateUserOptionsMapsUsed(UserWithoutRoleVo user) {
		userWithoutRoleCache.remove(user.getUserId());
		userDao.updateUserOptionsMapsUsed(user);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUserService#updateUserStatusPage(de.msk.mylivetracker.domain.user.UserWithoutRoleVo)
	 */
	@Override
	public void updateUserStatusPage(UserWithoutRoleVo user) {
		userWithoutRoleCache.remove(user.getUserId());
		userDao.updateUserStatusPage(user);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUserService#getUserCount(boolean)
	 */
	@Override
	public Integer getUserCount(boolean adminsIncluded) {
		return userDao.getUserCount(adminsIncluded);
	}

	@Override
	public void deleteUser(String userId) {
		userWithoutRoleCache.remove(userId);
		userDao.deleteUser(userId);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		UserWithRoleVo user = null;
				
		try {			
			// if application is running in evaluation mode
			// only 1 user is allowed on database size
			// without admin mode.
			if (this.applicationService.getParameterValueAsBoolean(Parameter.EvaluationModeEnabled)) {
				if (this.getUserCount(true) > 1) {
					throw new RuntimeException(
						"EVALUATION MODE: " +
						"maximum of registered users exceeded, only 1 user is allowed.");
				}
			}
			
			// check if this is an admin@user - login.
			if (StringUtils.contains(username, '@')) {
				String[] usernameParts = StringUtils.split(username, '@');
				if (usernameParts.length == 2) {
					UserWithRoleVo adminUser = this.userDao.getUserWithRole(usernameParts[0]);
					if ((adminUser != null) && adminUser.getRole().equals(UserRole.Admin)) {
						username = usernameParts[1];
						user = this.userDao.getUserWithRole(username);
						if ((user != null) && (adminUser != null)) {
							user.setAdminUsername(adminUser.getUserId());
							user.setAdminPassword(adminUser.getPassword());
						}
					} 
				}
			} 
			
			// check if this is an auto-login for user.
			if (user == null) {
				if (UserAutoLoginVo.isAutoLoginTicket(username)) {
					user = this.userDao.getUserWithRoleByAutoLoginTicketForUser(username);
				}
			}
			
			// check if this is an auto-login for guest.
			if (user == null) {
				if (UserAutoLoginVo.isAutoLoginTicket(username)) {
					user = this.userDao.getUserWithRoleByAutoLoginTicketForGuest(username);
				}
			}
			
			// check if this is an normal login.
			if (user == null) {
				user = this.userDao.getUserWithRole(username);		
			}
			
			if (user == null) {
				throw new UsernameNotFoundException(
					"user with userId '" + username + "' not found.");
			}
						
			if (this.applicationService.getParameterValueAsBoolean(Parameter.EvaluationModeEnabled) &&
				!user.getRole().equals(UserRole.User)) {
				throw new RuntimeException(
					"EVALUATION MODE: " +
					"user must have the role 'User'.");
			}
			
			if (this.applicationService.getParameterValueAsBoolean(Parameter.EvaluationModeEnabled)) {
				if (this.senderService.getSenderCount(user.getUserId()) > 3) {
					throw new RuntimeException(
						"EVALUATION MODE: " +
						"maximum of registered senders exceeded, only 3 senders are allowed.");
				}
			}
			
			user.setRealm(this.applicationService.getParameterValueAsString(Parameter.ApplicationRealm));			
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw e;
		}
		return user;				
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

	/**
	 * @return the senderService
	 */
	public ISenderService getSenderService() {
		return senderService;
	}

	/**
	 * @param senderService the senderService to set
	 */
	public void setSenderService(ISenderService senderService) {
		this.senderService = senderService;
	}

	/**
	 * @return the userWithoutRoleCache
	 */
	public Cache getUserWithoutRoleCache() {
		return userWithoutRoleCache;
	}

	/**
	 * @param userWithoutRoleCache the userWithoutRoleCache to set
	 */
	public void setUserWithoutRoleCache(Cache userWithoutRoleCache) {
		this.userWithoutRoleCache = userWithoutRoleCache;
	}

	/**
	 * @return the userDao
	 */
	public IUserDao getUserDao() {
		return userDao;
	}

	/**
	 * @param userDao the userDao to set
	 */
	public void setUserDao(IUserDao userDao) {
		this.userDao = userDao;
	}	
}
