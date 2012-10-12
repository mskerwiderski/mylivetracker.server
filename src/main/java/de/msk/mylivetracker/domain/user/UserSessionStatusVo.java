package de.msk.mylivetracker.domain.user;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.TrackingFlyToMode;
import de.msk.mylivetracker.domain.TracksOverviewMapFlyToModeVo;
import de.msk.mylivetracker.domain.TracksViewVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.SenderEntry;
import de.msk.mylivetracker.web.options.IntOptionDsc;

/**
 * UserSessionStatusVo.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-11
 * 
 */
public class UserSessionStatusVo implements Serializable {
	
	private static final long serialVersionUID = 7063852164890562807L;
	
	private String userId;
	private DateTime lastUpdated = new DateTime();
	private String toSelSenderForCreateTrack;
	private Boolean toSelLiveTrackingOpt;
	private Integer toSelLiveTrackingOptKeepRecentPos;
	private Integer toSelLiveTrackingOptUpdateInterval;
	private TrackingFlyToMode toSelLiveTrackingOptFlyToMode;
	private String toSelSenderFilter;
	private Integer toSelDatePeriodFilter;
	private String toSelSearchStrFilter;
	private TracksViewVo toSelTracksView;
	private TracksOverviewMapFlyToModeVo toSelTracksOverviewOptFlyToMode;
	private Integer toSelTracksOverviewOptRefresh;
	
	public static UserSessionStatusVo createDefault(
		UserWithRoleVo user, 
		ISenderService senderService,
		List<IntOptionDsc> liveTrackingOptsKeepRecentPos,
		List<IntOptionDsc> liveTrackingOptsUpdateInterval,
		List<IntOptionDsc> tracksOverviewOptsDatePeriod,
		List<IntOptionDsc> tracksOverviewOptsRefresh) {
		if (user == null) {
			throw new IllegalArgumentException("user must not be null.");
		}
		if (senderService == null) {
			throw new IllegalArgumentException("senderService must not be null.");
		}
		if ((liveTrackingOptsKeepRecentPos == null) || liveTrackingOptsKeepRecentPos.isEmpty()) {
			throw new IllegalArgumentException("liveTrackingOptsKeepRecentPos must not be null or empty.");
		}
		if ((liveTrackingOptsUpdateInterval == null) || liveTrackingOptsUpdateInterval.isEmpty()) {
			throw new IllegalArgumentException("liveTrackingOptsUpdateInterval must not be null or empty.");
		}
		if ((tracksOverviewOptsDatePeriod == null) || tracksOverviewOptsDatePeriod.isEmpty()) {
			throw new IllegalArgumentException("tracksOverviewOptsDatePeriod must not be null or empty.");
		}
		if ((tracksOverviewOptsRefresh == null) || tracksOverviewOptsRefresh.isEmpty()) {
			throw new IllegalArgumentException("tracksOverviewOptsRefresh must not be null or empty.");
		}
		UserSessionStatusVo userSessionStatus = new UserSessionStatusVo();
		userSessionStatus.setUserId(user.getUserId());
		List<SenderVo> senders = senderService.getSenders(user.getUserId());
		if ((senders == null) || (senders.isEmpty())) {
			userSessionStatus.setToSelSenderForCreateTrack(null);
		} else {
			userSessionStatus.setToSelSenderForCreateTrack(senders.get(0).getSenderId());
		}
		userSessionStatus.setToSelLiveTrackingOpt(true);
		userSessionStatus.setToSelLiveTrackingOptKeepRecentPos(
			liveTrackingOptsKeepRecentPos.get(0).getValue());
		userSessionStatus.setToSelLiveTrackingOptUpdateInterval(
			liveTrackingOptsUpdateInterval.get(0).getValue());
		userSessionStatus.setToSelLiveTrackingOptFlyToMode(TrackingFlyToMode.FlyToView);
		userSessionStatus.setToSelSenderFilter(SenderEntry.VALUE_ALL);
		userSessionStatus.setToSelDatePeriodFilter(
			tracksOverviewOptsDatePeriod.get(0).getValue());
		userSessionStatus.setToSelSearchStrFilter("");
		userSessionStatus.setToSelTracksView(TracksViewVo.Map);
		userSessionStatus.setToSelTracksOverviewOptFlyToMode(TracksOverviewMapFlyToModeVo.FlyToView);
		userSessionStatus.setToSelTracksOverviewOptRefresh(
			tracksOverviewOptsRefresh.get(0).getValue());
		return userSessionStatus;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) { return false; }
		if (obj == this) { return true; }
		if (obj.getClass() != getClass()) {
			return false;
		}
		UserSessionStatusVo uss = (UserSessionStatusVo)obj;
		return new EqualsBuilder().
            append(userId, uss.userId).
			append(toSelSenderForCreateTrack, uss.toSelSenderForCreateTrack).
			append(toSelLiveTrackingOpt, uss.toSelLiveTrackingOpt).
			append(toSelLiveTrackingOptKeepRecentPos, uss.toSelLiveTrackingOptKeepRecentPos).
			append(toSelLiveTrackingOptUpdateInterval, uss.toSelLiveTrackingOptUpdateInterval).
			append(toSelLiveTrackingOptFlyToMode, uss.toSelLiveTrackingOptFlyToMode).
			append(toSelSenderFilter, uss.toSelSenderFilter).
			append(toSelDatePeriodFilter, uss.toSelDatePeriodFilter).
			append(toSelSearchStrFilter, uss.toSelSearchStrFilter).
			append(toSelTracksView, uss.toSelTracksView).
			append(toSelTracksOverviewOptFlyToMode, uss.toSelTracksOverviewOptFlyToMode).
			append(toSelTracksOverviewOptRefresh, uss.toSelTracksOverviewOptRefresh).
            isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).
			append(userId).
			append(toSelSenderForCreateTrack).
			append(toSelLiveTrackingOpt).
			append(toSelLiveTrackingOptKeepRecentPos).
			append(toSelLiveTrackingOptUpdateInterval).
			append(toSelLiveTrackingOptFlyToMode).
			append(toSelSenderFilter).
			append(toSelDatePeriodFilter).
			append(toSelSearchStrFilter).
			append(toSelTracksView).
			append(toSelTracksOverviewOptFlyToMode).
			append(toSelTracksOverviewOptRefresh).
			toHashCode();
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public DateTime getLastUpdated() {
		return lastUpdated;
	}
	public void setLastUpdated(DateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}
	public String getToSelSenderForCreateTrack() {
		return toSelSenderForCreateTrack;
	}
	public void setToSelSenderForCreateTrack(String toSelSenderForCreateTrack) {
		this.toSelSenderForCreateTrack = toSelSenderForCreateTrack;
	}
	public Boolean getToSelLiveTrackingOpt() {
		return toSelLiveTrackingOpt;
	}
	public void setToSelLiveTrackingOpt(Boolean toSelLiveTrackingOpt) {
		this.toSelLiveTrackingOpt = toSelLiveTrackingOpt;
	}
	public Integer getToSelLiveTrackingOptKeepRecentPos() {
		return toSelLiveTrackingOptKeepRecentPos;
	}
	public void setToSelLiveTrackingOptKeepRecentPos(
			Integer toSelLiveTrackingOptKeepRecentPos) {
		this.toSelLiveTrackingOptKeepRecentPos = toSelLiveTrackingOptKeepRecentPos;
	}
	public Integer getToSelLiveTrackingOptUpdateInterval() {
		return toSelLiveTrackingOptUpdateInterval;
	}
	public void setToSelLiveTrackingOptUpdateInterval(
			Integer toSelLiveTrackingOptUpdateInterval) {
		this.toSelLiveTrackingOptUpdateInterval = toSelLiveTrackingOptUpdateInterval;
	}
	public TrackingFlyToMode getToSelLiveTrackingOptFlyToMode() {
		return toSelLiveTrackingOptFlyToMode;
	}
	public void setToSelLiveTrackingOptFlyToMode(
			TrackingFlyToMode toSelLiveTrackingOptFlyToMode) {
		this.toSelLiveTrackingOptFlyToMode = toSelLiveTrackingOptFlyToMode;
	}
	public String getToSelSenderFilter() {
		return toSelSenderFilter;
	}
	public void setToSelSenderFilter(String toSelSenderFilter) {
		this.toSelSenderFilter = toSelSenderFilter;
	}
	public Integer getToSelDatePeriodFilter() {
		return toSelDatePeriodFilter;
	}
	public void setToSelDatePeriodFilter(Integer toSelDatePeriodFilter) {
		this.toSelDatePeriodFilter = toSelDatePeriodFilter;
	}
	public String getToSelSearchStrFilter() {
		return toSelSearchStrFilter;
	}
	public void setToSelSearchStrFilter(String toSelSearchStrFilter) {
		this.toSelSearchStrFilter = toSelSearchStrFilter;
	}
	public TracksViewVo getToSelTracksView() {
		return toSelTracksView;
	}
	public void setToSelTracksView(TracksViewVo toSelTracksView) {
		this.toSelTracksView = toSelTracksView;
	}
	public TracksOverviewMapFlyToModeVo getToSelTracksOverviewOptFlyToMode() {
		return toSelTracksOverviewOptFlyToMode;
	}
	public void setToSelTracksOverviewOptFlyToMode(
			TracksOverviewMapFlyToModeVo toSelTracksOverviewOptFlyToMode) {
		this.toSelTracksOverviewOptFlyToMode = toSelTracksOverviewOptFlyToMode;
	}
	public Integer getToSelTracksOverviewOptRefresh() {
		return toSelTracksOverviewOptRefresh;
	}
	public void setToSelTracksOverviewOptRefresh(
			Integer toSelTracksOverviewOptRefresh) {
		this.toSelTracksOverviewOptRefresh = toSelTracksOverviewOptRefresh;
	}

	@Override
	public String toString() {
		return "UserSessionStatusVo [userId=" + userId + ", lastUpdated="
			+ lastUpdated + ", toSelSenderForCreateTrack="
			+ toSelSenderForCreateTrack + ", toSelLiveTrackingOpt="
			+ toSelLiveTrackingOpt + ", toSelLiveTrackingOptKeepRecentPos="
			+ toSelLiveTrackingOptKeepRecentPos
			+ ", toSelLiveTrackingOptUpdateInterval="
			+ toSelLiveTrackingOptUpdateInterval
			+ ", toSelLiveTrackingOptFlyToMode="
			+ toSelLiveTrackingOptFlyToMode + ", toSelSenderFilter="
			+ toSelSenderFilter + ", toSelDatePeriodFilter=" + toSelDatePeriodFilter
			+ ", toSelSearchStrFilter=" + toSelSearchStrFilter
			+ ", toSelTracksView=" + toSelTracksView
			+ ", toSelTracksOverviewOptFlyToMode="
			+ toSelTracksOverviewOptFlyToMode
			+ ", toSelTracksOverviewOptRefresh="
			+ toSelTracksOverviewOptRefresh + "]";
	}
}
