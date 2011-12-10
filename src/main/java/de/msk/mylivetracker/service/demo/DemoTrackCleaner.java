package de.msk.mylivetracker.service.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.service.ITrackService;

/**
 * DemoTrackCleaner.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DemoTrackCleaner extends Thread {
	private String userId;
	private ITrackService trackService;
			
	private static final Log log = LogFactory.getLog(DemoTrackCleaner.class);
	
	public DemoTrackCleaner(
		String userId, ITrackService trackService) {
		this.userId = userId;
		this.trackService = trackService;		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		log.info("DemoTrackCleaner has been interrupted.");
		super.interrupt();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {		
		boolean statusOk = true;
		while (statusOk) {
			try {
				trackService.removeClosedTracks(this.userId);
				Thread.sleep(1000 * 60 * 60);
			} catch (InterruptedException e) {
				statusOk = false;
			} catch (Exception e) {
				log.info("demo track cleaner stopped: " + e.getMessage());
				e.printStackTrace();
				statusOk = false;
			}
		}			
	}		
}
