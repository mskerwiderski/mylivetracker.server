package de.msk.mylivetracker.service.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.application.IApplicationService.Parameter;
import de.msk.mylivetracker.service.statistics.IStatisticsService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.service.track.TrackCleaner;

/**
 * UtilService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UtilService implements IUtilService {

	private static final Log log = LogFactory.getLog(UtilService.class);
	
	private ThreadPoolTaskExecutor utilTaskExecutor;
	private IApplicationService applicationService;
	private ITrackService trackService;
	private IStatisticsService statisticsService;
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUtilService#start()
	 */
	@Override
	public void start() {
		log.debug("util-service started.");
		utilTaskExecutor.initialize();
		boolean runCleanTasks =
			this.applicationService.
			getParameterValueAsBoolean(
				Parameter.RunCleanTasksAfterStartup);
		if (runCleanTasks) {
			utilTaskExecutor.execute(
				new TrackCleaner(
					this.applicationService.
						getParameterValueAsLong(
							Parameter.TrackLifeTimeInMSecs), 
							trackService));
			log.debug("clean tasks started.");
		} else {
			log.debug("clean tasks NOT started.");
		}
		
		this.statisticsService.logApplicationStartUp();
	}

	/**
	 * @return the utilTaskExecutor
	 */
	public ThreadPoolTaskExecutor getUtilTaskExecutor() {
		return utilTaskExecutor;
	}

	/**
	 * @param utilTaskExecutor the utilTaskExecutor to set
	 */
	public void setUtilTaskExecutor(ThreadPoolTaskExecutor utilTaskExecutor) {
		this.utilTaskExecutor = utilTaskExecutor;
	}

	public IApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	public ITrackService getTrackService() {
		return trackService;
	}

	public void setTrackService(ITrackService trackService) {
		this.trackService = trackService;
	}

	public IStatisticsService getStatisticsService() {
		return statisticsService;
	}

	public void setStatisticsService(IStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}
}
