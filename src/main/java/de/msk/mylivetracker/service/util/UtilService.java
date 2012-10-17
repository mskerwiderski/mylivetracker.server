package de.msk.mylivetracker.service.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.service.application.IApplicationService.Parameter;
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
	private Services services;
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IUtilService#start()
	 */
	@Override
	public void start() {
		log.debug("util-service started.");
		try {
			Thread.sleep(10000);
		
			utilTaskExecutor.initialize();
			boolean runDemo = 
				this.services.getApplicationService().
				getParameterValueAsBoolean(
					Parameter.RunDemoAfterStartup);
			if (runDemo) {
				this.services.getDemoService().runDemo();
				log.debug("demo started.");
			} else {
				log.debug("demo NOT started.");
			}
			
			boolean runCleanTasks =
				this.services.getApplicationService().
				getParameterValueAsBoolean(
					Parameter.RunCleanTasksAfterStartup);
			if (runCleanTasks) {
				utilTaskExecutor.execute(
					new TrackCleaner(
						this.services.getApplicationService().
							getParameterValueAsLong(
								Parameter.TrackLifeTimeInMSecs), 
						this.services));
				log.debug("clean tasks started.");
			} else {
				log.debug("clean tasks NOT started.");
			}
			
			this.services.getStatisticsService().logApplicationStartUp();
		} catch (InterruptedException e) {
			log.info(e);
		}
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

	public Services getServices() {
		return services;
	}

	public void setServices(Services services) {
		this.services = services;
	}
}
