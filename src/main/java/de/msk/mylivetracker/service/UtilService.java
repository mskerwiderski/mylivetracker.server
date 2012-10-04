package de.msk.mylivetracker.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import de.msk.mylivetracker.service.IApplicationService.Parameter;
import de.msk.mylivetracker.service.demo.IDemoService;
import de.msk.mylivetracker.service.statistics.IStatisticsService;
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
	private IDemoService demoService;
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUtilService#start()
	 */
	@Override
	public void start() {
		log.debug("util-service started.");
		utilTaskExecutor.initialize();
		boolean runDemo = 
			this.applicationService.
			getParameterValueAsBoolean(
				Parameter.RunDemoAfterStartup);
		if (runDemo) {
			this.demoService.runDemo();
			log.debug("demo started.");
		} else {
			log.debug("demo NOT started.");
		}
		
		boolean runCleanTasks =
			this.applicationService.
			getParameterValueAsBoolean(
				Parameter.RunCleanTasksAfterStartup);
		if (runCleanTasks) {
			utilTaskExecutor.execute(
				new TrackCleaner(
					this.applicationService.getParameterValueAsLong(
						Parameter.TrackLifeTimeInMSecs), 
					this.trackService));
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

	/**
	 * @return the applicationService
	 */
	public IApplicationService getApplicationService() {
		return applicationService;
	}

	/**
	 * @param applicationService the applicationService to set
	 */
	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @return the trackService
	 */
	public ITrackService getTrackService() {
		return trackService;
	}

	/**
	 * @param trackService the trackService to set
	 */
	public void setTrackService(ITrackService trackService) {
		this.trackService = trackService;
	}

	/**
	 * @return the statisticsService
	 */
	public IStatisticsService getStatisticsService() {
		return statisticsService;
	}

	/**
	 * @param statisticsService the statisticsService to set
	 */
	public void setStatisticsService(IStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}	

	/**
	 * @return the demoService
	 */
	public IDemoService getDemoService() {
		return demoService;
	}

	/**
	 * @param demoService the demoService to set
	 */
	public void setDemoService(IDemoService demoService) {
		this.demoService = demoService;
	}		
}
