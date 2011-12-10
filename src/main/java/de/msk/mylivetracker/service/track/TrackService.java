package de.msk.mylivetracker.service.track;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.dao.ITrackDao;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.track.TrackFilterVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.ITrackService;

/**
 * TrackService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackService implements ITrackService {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(TrackService.class);
	
	private ITrackDao trackDao;	
	private UserStorePositionQueues userStorePositionQueues;

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#createTrack(de.msk.mylivetracker.domain.SenderVo, java.lang.String, boolean)
	 */
	public TrackVo createTrack(SenderVo sender, 
		String trackName, boolean trackReleased) {
			return trackDao.createTrack(sender, trackName, trackReleased);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#updateTrackHeartbeat(java.util.Date, de.msk.mylivetracker.domain.SenderVo, java.lang.String, boolean)
	 */
	@Override
	public void updateTrackHeartbeat(DateTime heartbeat, SenderVo sender,
		String trackName, boolean trackReleased) {
		trackDao.updateTrackHeartbeat(heartbeat, sender, trackName, trackReleased);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#openTrack(java.lang.String)
	 */
	public void openTrack(String trackId) {
		trackDao.openTrack(trackId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#closeTrack(java.lang.String)
	 */
	public void closeTrack(String trackId) {
		trackDao.closeTrack(trackId);
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#closeActiveTrack(java.lang.String)
	 */
	@Override
	public void closeActiveTrack(String senderId) {
		trackDao.closeActiveTrack(senderId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#getActiveTrackAsMin(java.lang.String)
	 */
	@Override
	public TrackVo getActiveTrackAsMin(String senderId) {
		return trackDao.getActiveTrackAsMin(senderId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#getActiveTrackAsRecent(java.lang.String)
	 */
	@Override
	public TrackVo getActiveTrackAsRecent(String senderId) {
		return trackDao.getActiveTrackAsRecent(senderId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#getRecentTrackAsMin(de.msk.mylivetracker.domain.user.UserWithoutRoleVo, java.lang.String)
	 */
	@Override
	public TrackVo getRecentTrackAsMin(UserWithoutRoleVo user, String senderId) {
		return trackDao.getRecentTrackAsMin(user, senderId);
	}

	@Override
	public TrackVo getRecentTrackAsRecent(UserWithoutRoleVo user, String senderId) {
		return trackDao.getRecentTrackAsRecent(user, senderId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#getTrackAsRecent(java.lang.String)
	 */
	@Override
	public TrackVo getTrackAsRecent(String trackId) {
		return trackDao.getTrackAsRecent(trackId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#getTrackAsMin(java.lang.String)
	 */
	@Override
	public TrackVo getTrackAsMin(String trackId) {
		return trackDao.getTrackAsMin(trackId);
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#getTrackAsDetailed(java.lang.String, java.lang.Integer)
	 */
	@Override
	public TrackVo getTrackAsDetailed(String trackId, Integer cntCutPositions) {
		TrackVo track = trackDao.getTrackAsDetailed(trackId, cntCutPositions);
		return track;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#getTracksAsRecent(de.msk.mylivetracker.domain.track.TrackFilterVo)
	 */
	@Override
	public TrackListResult getTracksAsRecent(TrackFilterVo trackFilter) {
		return trackDao.getTracksAsRecent(trackFilter);
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#removeTrack(java.lang.String)
	 */
	public void removeTrack(String trackId) {
		trackDao.removeTrack(trackId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#removeClosedTracks(java.lang.String)
	 */
	@Override
	public void removeClosedTracks(String userId) {
		trackDao.removeClosedTracks(userId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#removeOldTracks(long)
	 */
	@Override
	public void removeOldTracks(long olderThanInMSecs) {
		trackDao.removeOldTracks(olderThanInMSecs);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#renameTrack(java.lang.String, java.lang.String)
	 */
	public void renameTrack(String trackId, String trackName) {
		trackDao.renameTrack(trackId, trackName);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#resetTrack(java.lang.String)
	 */
	@Override
	public void resetTrack(String trackId) {
		trackDao.resetTrack(trackId);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#resetActiveTrack(java.lang.String)
	 */
	@Override
	public void resetActiveTrack(String senderId) {
		trackDao.resetActiveTrack(senderId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#storePositionAndMessage(de.msk.mylivetracker.domain.user.UserWithoutRoleVo, de.msk.mylivetracker.domain.DataReceivedVo)
	 */
	@Override
	public void storePositionAndMessage(UserWithoutRoleVo user,
		DataReceivedVo dataReceived) {
		userStorePositionQueues.push(user, dataReceived);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#privatizeTrack(java.lang.String)
	 */
	public void privatizeTrack(String trackId) {
		trackDao.privatizeTrack(trackId);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.ITrackService#publishTrack(java.lang.String)
	 */
	public void publishTrack(String trackId) {
		trackDao.publishTrack(trackId);
	}	

	/**
	 * @return the trackDao
	 */
	public ITrackDao getTrackDao() {
		return trackDao;
	}

	/**
	 * @param trackDao the trackDao to set
	 */
	public void setTrackDao(ITrackDao trackDao) {
		this.trackDao = trackDao;
	}

	/**
	 * @return the userStorePositionQueues
	 */
	public UserStorePositionQueues getUserStorePositionQueues() {
		return userStorePositionQueues;
	}

	/**
	 * @param userStorePositionQueues the userStorePositionQueues to set
	 */
	public void setUserStorePositionQueues(
			UserStorePositionQueues userStorePositionQueues) {
		this.userStorePositionQueues = userStorePositionQueues;
	}		
}
