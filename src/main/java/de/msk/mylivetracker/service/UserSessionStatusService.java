package de.msk.mylivetracker.service;

import java.util.List;

import de.msk.mylivetracker.dao.IUserSessionStatusDao;
import de.msk.mylivetracker.domain.user.UserSessionStatusVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.web.options.IntOptionDsc;

/**
 * UserSessionStatusService.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 2012-10-03 initial.
 * 
 */
public class UserSessionStatusService implements IUserSessionStatusService {

	private ISenderService senderService;
	private List<IntOptionDsc> liveTrackingOptsKeepRecentPos;
	private List<IntOptionDsc> liveTrackingOptsUpdateInterval;
	private List<IntOptionDsc> tracksOverviewOptsDatePeriod;
	private List<IntOptionDsc> tracksOverviewOptsRefresh;
	private IUserSessionStatusDao userSessionStatusDao;
	
	@Override
	public UserSessionStatusVo getUserSessionStatus(UserWithRoleVo user) {
		UserSessionStatusVo userSessionStatus = 
			this.userSessionStatusDao.getUserSessionStatus(user.getUserId());
		if (userSessionStatus == null) {
			userSessionStatus = UserSessionStatusVo.createDefault(
				user, 
				senderService,
				liveTrackingOptsKeepRecentPos,
				liveTrackingOptsUpdateInterval,
				tracksOverviewOptsDatePeriod,
				tracksOverviewOptsRefresh);
			this.updateUserSessionStatus(userSessionStatus);
		}
		return userSessionStatus;
	}

	@Override
	public void updateUserSessionStatus(UserSessionStatusVo userSessionStatus) {
		this.userSessionStatusDao.updateUserSessionStatus(userSessionStatus);
	}
	
	public ISenderService getSenderService() {
		return senderService;
	}

	public void setSenderService(ISenderService senderService) {
		this.senderService = senderService;
	}

	public List<IntOptionDsc> getLiveTrackingOptsKeepRecentPos() {
		return liveTrackingOptsKeepRecentPos;
	}

	public void setLiveTrackingOptsKeepRecentPos(
			List<IntOptionDsc> liveTrackingOptsKeepRecentPos) {
		this.liveTrackingOptsKeepRecentPos = liveTrackingOptsKeepRecentPos;
	}

	public List<IntOptionDsc> getLiveTrackingOptsUpdateInterval() {
		return liveTrackingOptsUpdateInterval;
	}

	public void setLiveTrackingOptsUpdateInterval(
			List<IntOptionDsc> liveTrackingOptsUpdateInterval) {
		this.liveTrackingOptsUpdateInterval = liveTrackingOptsUpdateInterval;
	}

	public List<IntOptionDsc> getTracksOverviewOptsDatePeriod() {
		return tracksOverviewOptsDatePeriod;
	}

	public void setTracksOverviewOptsDatePeriod(
			List<IntOptionDsc> tracksOverviewOptsDatePeriod) {
		this.tracksOverviewOptsDatePeriod = tracksOverviewOptsDatePeriod;
	}

	public List<IntOptionDsc> getTracksOverviewOptsRefresh() {
		return tracksOverviewOptsRefresh;
	}

	public void setTracksOverviewOptsRefresh(
			List<IntOptionDsc> tracksOverviewOptsRefresh) {
		this.tracksOverviewOptsRefresh = tracksOverviewOptsRefresh;
	}

	public IUserSessionStatusDao getUserSessionStatusDao() {
		return userSessionStatusDao;
	}

	public void setUserSessionStatusDao(IUserSessionStatusDao userSessionStatusDao) {
		this.userSessionStatusDao = userSessionStatusDao;
	}
}
