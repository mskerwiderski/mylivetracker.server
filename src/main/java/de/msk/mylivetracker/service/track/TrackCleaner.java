package de.msk.mylivetracker.service.track;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.service.track.ITrackService.DeleteTrackResult;

/**
 * TrackCleaner.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackCleaner implements Runnable {
	
	private static final Log log = LogFactory.getLog(TrackCleaner.class);
	
	private long olderThanInMSecs;
	private ITrackService trackService;
			
	public TrackCleaner(
		long olderThanInMSecs, ITrackService trackService) {
		this.olderThanInMSecs = olderThanInMSecs;
		this.trackService = trackService;		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {	
		log.debug("trackcleaner started.");
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean statusOk = true;
		while (statusOk) {
			try {
				trackService.removeOldTracks(this.olderThanInMSecs);
				log.debug("trackcleaner: old tracks removed.");
				Thread.sleep(1000 * 10);
				DeleteTrackResult deleteTrackResult = trackService.deleteOneRemovedTrack();
				log.debug("trackcleaner: " + deleteTrackResult.toString());
				Thread.sleep(1000 * 10);
			} catch (InterruptedException e) {
				statusOk = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("trackcleaner stopped.");
	}		
}
