package de.msk.mylivetracker.service.statistics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * StatisticsCleaner.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class StatisticsCleaner implements Runnable {
	
	private static final Log log = LogFactory.getLog(StatisticsCleaner.class);
	
	private long olderThanInMSecs;
	private IStatisticsService statisticsService;
			
	public StatisticsCleaner(
		long olderThanInMSecs, IStatisticsService statisticsService) {
		this.olderThanInMSecs = olderThanInMSecs;
		this.statisticsService = statisticsService;		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {	
		log.debug("statistics cleaner started.");
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		boolean statusOk = true;
		while (statusOk) {
			try {
				//statisticsService.cleanApplicationStartUpTable(olderThanInMSecs);
				//statisticsService.cleanUploaderServerStatusTable(olderThanInMSecs);
				statisticsService.cleanUploadedDataProcessTable(olderThanInMSecs);
				statisticsService.cleanServiceCallCountTable(olderThanInMSecs);
				statisticsService.cleanStorePositionProcessorInfoTable(olderThanInMSecs);	
				statisticsService.cleanSmsTransportTable(olderThanInMSecs);
				log.debug("logcleaner: old logs removed.");
				Thread.sleep(1000 * 60 * 60);
			} catch (InterruptedException e) {
				statusOk = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		log.debug("statistics cleaner stopped.");
	}		
}
