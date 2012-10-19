package de.msk.mylivetracker.service.demo;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import de.msk.mylivetracker.dao.IDemoDao;
import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.application.IApplicationService.Parameter;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.service.user.IUserService;

/**
 * DemoService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DemoService implements IDemoService {
	private static final Log log = LogFactory.getLog(DemoService.class);
	
	private List<DemoCase> demoCases;
	private IDemoDao demoDao;
	private IApplicationService applicationService;
	private IUserService userService;
	private ISenderService senderService;
	private ITrackService trackService;
	private ThreadPoolTaskExecutor demoTaskExecutor;
	
	private static final String STATUS_RUNNING = "RUNNING";
	private ConcurrentMap<String, String> status = new ConcurrentHashMap<String, String>();
	
	public void runIfAutostart() {
		boolean runDemo = 
			this.applicationService.
			getParameterValueAsBoolean(
				Parameter.RunDemoAfterStartup);
		if (runDemo) {
			this.runDemo();
			log.debug("demo started.");
		} else {
			log.debug("demo NOT started.");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IDemoService#runDemo()
	 */
	@Override
	public void runDemo() {		
		if (this.getDemoStatus().isRunning()) {
			return;
		}
		demoTaskExecutor.initialize();
		demoTaskExecutor.execute(
			new DemoRunner(demoCases, 
				demoDao, userService, senderService,
				trackService, demoTaskExecutor));
		status.put(STATUS_RUNNING, BooleanUtils.toStringYesNo(true));
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.demo.IDemoService#stopDemo()
	 */
	@Override
	public void stopDemo() {
		if (!this.getDemoStatus().isRunning()) {
			return;
		}
		demoTaskExecutor.shutdown();
		status.put(STATUS_RUNNING, BooleanUtils.toStringYesNo(false));
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.demo.IDemoService#getDemoStatus()
	 */
	@Override
	public DemoStatus getDemoStatus() {
		int countTracks = 0;
		if (this.demoCases != null) {
			countTracks = this.demoCases.size();
		}
		return new DemoStatus(
			BooleanUtils.toBoolean(status.get(STATUS_RUNNING)), 
			countTracks);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.demo.IDemoService#insertTrackAsDemoTrack(java.lang.String)
	 */
	@Override
	public int insertTrackAsDemoTrack(String trackId) {
		return this.demoDao.insertTrackAsDemoTrack(
			this.trackService.getTrackAsDetailed(trackId, null, true, true, true, true, true));
	}

	/**
	 * @return the demoCases
	 */
	public List<DemoCase> getDemoCases() {
		return demoCases;
	}

	/**
	 * @param demoCases the demoCases to set
	 */
	public void setDemoCases(List<DemoCase> demoCases) {
		this.demoCases = demoCases;
	}	

	/**
	 * @return the demoDao
	 */
	public IDemoDao getDemoDao() {
		return demoDao;
	}

	/**
	 * @param demoDao the demoDao to set
	 */
	public void setDemoDao(IDemoDao demoDao) {
		this.demoDao = demoDao;
	}

	public IApplicationService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}

	/**
	 * @return the userService
	 */
	public IUserService getUserService() {
		return userService;
	}

	/**
	 * @param userService the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * @return the senderService
	 */
	public ISenderService getSenderService() {
		return senderService;
	}

	/**
	 * @param senderService the senderService to set
	 */
	public void setSenderService(ISenderService senderService) {
		this.senderService = senderService;
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
	 * @return the demoTaskExecutor
	 */
	public ThreadPoolTaskExecutor getDemoTaskExecutor() {
		return demoTaskExecutor;
	}

	/**
	 * @param demoTaskExecutor the demoTaskExecutor to set
	 */
	public void setDemoTaskExecutor(ThreadPoolTaskExecutor demoTaskExecutor) {
		this.demoTaskExecutor = demoTaskExecutor;
	}
}
