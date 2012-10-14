package de.msk.mylivetracker.dao;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.CardiacFunctionVo;
import de.msk.mylivetracker.domain.ClientInfoVo;
import de.msk.mylivetracker.domain.EmergencySignalVo;
import de.msk.mylivetracker.domain.MessageVo;
import de.msk.mylivetracker.domain.MobNwCellVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.SenderStateVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.track.TrackFilterVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserOptionsVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.track.DeletedTrackInfoVo;
import de.msk.mylivetracker.service.track.ITrackService.TrackListResult;

/**
 * ITrackDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface ITrackDao {	
	
	/**
	 * store position and message of sender.
	 * @param user - user.
	 * @param sender - sender.
	 * @param trackName - name of track.
	 * @param position - position.
	 * @param mobileNetworkPositionVo - mobile network position.
	 * @param message - message.
	 * @param senderState - sender state.
	 * @param cardiacFunction - cardia function.
	 * @param emergencySignal - emergency signal.
	 * @param options - the user specific options.
	 */
	public void storePositionAndMessage(
		UserWithoutRoleVo user,
		SenderVo sender, 
		PositionVo position,
		MobNwCellVo mobNwCell,
		MessageVo message,
		SenderStateVo senderState,
		CardiacFunctionVo cardiacFunction,
		EmergencySignalVo emergencySignal,
		ClientInfoVo clientInfo,
		UserOptionsVo options);	
		
	/**
	 * create track for sender.
	 * @param sender - sender.
	 * @param trackName - name of track.
	 * @param trackReleased - Indicates whether to create the track is released or not.
	 */
	public TrackVo createTrack(SenderVo sender, String trackName, boolean trackReleased);
	
	public void updateTrackHeartbeat(DateTime heartbeat,
		SenderVo sender, String trackName, boolean trackReleased);
	
	/**
	 * get track with count recent positions.
	 * @param trackId - the track id.
	 * @param cntCutPositions - 
	 * count of excluded positions at the beginning or included recent positions. 
	 * if cntCutPositions is greater zero, the recent cntCutPositions positions are loaded.
	 * if cntCutPositions is less zero, the first cntCutPositions positions are skipped.
	 * if cntCutPositions is zero or null, all position are loaded.
	 * @return the track or <code>null</code> if the track was not found.
	 */
	public TrackVo getTrackAsDetailed(String trackId, Integer cntCutPositions,
		boolean messagesIncl, boolean emergencySignalsIncl,
		boolean cardiacFunctionsIncl, boolean mobNwCellsIncl, boolean senderStatesIncl);
	
	/**
	 * get track.
	 * @param trackId - the track id.
	 * @return the track or <code>null</code> if the track was not found.
	 */
	public TrackVo getTrackAsRecent(String trackId);
	
	/**
	 * get track.
	 * @param trackId - the track id.
	 * @return the track or <code>null</code> if the track was not found.
	 */
	public TrackVo getTrackAsMin(String trackId);
	
	/**
	 * get recent track.
	 * o if senderId is not null, recent track of specified sender is returned.
	 * o if senderId is null, recent track of specified user is returned.
	 * @param user - the user.
	 * @param senderId - the sender id.
	 * @return the track or <code>null</code> if the track was not found.
	 */
	public TrackVo getRecentTrackAsMin(UserWithoutRoleVo user, String senderId);				
		
	/**
	 * get recent track.
	 * o if senderId is not null, recent track of specified sender is returned.
	 * o if senderId is null, recent track of specified user is returned.
	 * @param user - the user.
	 * @param senderId - the sender id.
	 * @return the track or <code>null</code> if the track was not found.
	 */
	public TrackVo getRecentTrackAsRecent(UserWithoutRoleVo user, String senderId);				
		
	/**
	 * get active track of sender.
	 * @param senderId
	 * @return
	 */
	public TrackVo getActiveTrackAsMin(String senderId);
	
	/**
	 * get active track of sender.
	 * @param senderId
	 * @return
	 */
	public TrackVo getActiveTrackAsRecent(String senderId);
	
	/**
	 * get all tracks, sorted by <code>timeUpdated</code>.
	 * @param trackFilter - the track filter.
	 * @return list of tracks.
	 */
	public TrackListResult getTracksAsRecent(TrackFilterVo trackFilter);		
	
	/**
	 * reset track.
	 * @param trackId - id of track.
	 */
	public void resetTrack(String trackId);
	
	/**
	 * reset active track.
	 * @param senderId - id of sender.
	 */
	public boolean resetActiveTrack(String senderId);
	
	/**
	 * remove track (positions included).
	 * @param trackId - id of track.
	 */
	public void removeTrack(String trackId);
	
	/**
	 * remove old tracks.
	 * @param olderThanInMSecs - remove tracks which are older than <code>olderThanInMSecs</code>.
	 */
	public void removeOldTracks(long olderThanInMSecs);
	
	/**
	 * remove all track of a user.
	 * @param userId - id of user.
	 */
	public void removeAllTracksOfUsers(String userId);
	
	/**
	 * delete one removed track.
	 */
	public DeletedTrackInfoVo deleteOneRemovedTrack();

	/**
	 * open track.
	 * @param trackId - id of track.
	 */
	public void openTrack(String trackId);
	
	/**
	 * close track.
	 * @param trackId - id of track.
	 */
	public void closeTrack(String trackId);
	
	/**
	 * close active track.
	 * @param senderId - id of sender.
	 */
	public void closeActiveTrack(String senderId);
	
	/**
	 * rename track.
	 * @param trackId - id of track.
	 * @param trackName - name of track.
	 */
	public void renameTrack(String trackId, String trackName);
	
	/**
	 * publish track.
	 * @param trackId - id of track.
	 */
	public void publishTrack(String trackId);
	
	/**
	 * privatize track.
	 * @param trackId - id of track.
	 */
	public void privatizeTrack(String trackId);				
}
