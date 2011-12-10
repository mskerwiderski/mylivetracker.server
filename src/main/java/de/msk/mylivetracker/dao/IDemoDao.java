package de.msk.mylivetracker.dao;

import de.msk.mylivetracker.domain.demo.DemoTrackVo;
import de.msk.mylivetracker.domain.track.TrackVo;

/**
 * IDemoDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IDemoDao {

	/**
	 * get demo track.
	 * @param trackId
	 * @return Returns DemoTrackVo.
	 */
	public DemoTrackVo getDemoTrack(String trackId);
	
	/**
	 * insert track as demo track.
	 * @param track - the track.
	 * @return count of records inserted.
	 */
	public int insertTrackAsDemoTrack(TrackVo track);
}
