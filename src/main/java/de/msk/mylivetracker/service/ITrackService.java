package de.msk.mylivetracker.service;

import java.util.List;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.track.TrackFilterVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;

/**
 * ITrackService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface ITrackService {	
		
	/**
	 * store position and message of sender.
	 * @param sender - sender.
	 * @param trackName - name of track.
	 * @param position - position.
	 * @param mobileNetworkPositionVo - mobile network position.
	 * @param message - message.
	 * @param options - the user specific options.
	 */
	public void storePositionAndMessage(
		UserWithoutRoleVo user, DataReceivedVo dataReceived);	
		
	/**
	 * create track for sender.
	 * @param sender - sender.
	 * @param trackName - name of track.
	 * @param trackReleased - Indicates whether to create the track as public or not.
	 */
	public TrackVo createTrack(SenderVo sender, String trackName, boolean trackReleased);
	
	public void updateTrackHeartbeat(DateTime heartbeat,
		SenderVo sender, String trackName, boolean trackReleased);
	
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
	
	public static final class TrackListResult {
		private int countFoundTracks;
		private List<TrackVo> tracks;		
		private boolean maxCountOfRecordsExceeded;
		public TrackListResult(int countFoundTracks, List<TrackVo> tracks,
			boolean maxCountOfRecordsExceeded) {
			this.countFoundTracks = countFoundTracks;
			this.tracks = tracks;
			this.maxCountOfRecordsExceeded = maxCountOfRecordsExceeded;
		}
		public int getCountFoundTracks() {
			return countFoundTracks;
		}
		public List<TrackVo> getTracks() {
			return tracks;
		}
		public boolean isMaxCountOfRecordsExceeded() {
			return maxCountOfRecordsExceeded;
		}
	}
	
	/**
	 * get all tracks, sorted by <code>timeUpdated</code>.
	 * @param trackFilter - the track filter.
	 * @return list of tracks.
	 */
	public TrackListResult getTracksAsRecent(TrackFilterVo trackFilter);	
	
		
	/**
	 * remove track (positions included).
	 * @param trackId - id of track.
	 */
	public void removeTrack(String trackId);
	
	/**
	 * remove closed tracks.
	 * @param userId - id of user.
	 */
	public void removeClosedTracks(String userId);
	
	/**
	 * remove old tracks.
	 * @param olderThanInMSecs - remove tracks which are older than <code>olderThanInMSecs</code>.
	 */
	public void removeOldTracks(long olderThanInMSecs);
	
	/**
	 * remove tracks with remove flag.
	 */
	public void removeTracksWithRemoveFlag();
	
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
	 * reset track.
	 * @param trackId - id of track.
	 */
	public void resetTrack(String trackId);
	
	/**
	 * reset active track.
	 * @param senderId - id of sender.
	 */
	public void resetActiveTrack(String senderId);
	
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
