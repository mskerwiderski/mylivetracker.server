package de.msk.mylivetracker.service.demo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;

import de.msk.mylivetracker.dao.IDemoDao;
import de.msk.mylivetracker.domain.demo.DemoTrackVo;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ITrackService;
import de.msk.mylivetracker.service.IUserService;

/**
 * DemoRunner.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DemoRunner extends Thread {

	private List<DemoCase> demoCases;	
	private IDemoDao demoDao;
	private IUserService userService;
	private ISenderService senderService;
	private ITrackService trackService;
	private TaskExecutor demoTaskExecutor;
	
	private static final Log log = LogFactory.getLog(DemoRunner.class);
	
	/**
	 * @param demoCases
	 * @param demoDao
	 * @param userService
	 * @param trackService
	 * @param demoTaskExecutor
	 */
	public DemoRunner(List<DemoCase> demoCases,
		IDemoDao demoDao,
		IUserService userService,
		ISenderService senderService,
		ITrackService trackService, 
		TaskExecutor demoTaskExecutor) {
		this.demoCases = demoCases;
		this.demoDao = demoDao;
		this.userService = userService;
		this.senderService = senderService;
		this.trackService = trackService;
		this.demoTaskExecutor = demoTaskExecutor;
	}	
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		log.info("DemoRunner has been interrupted.");
		super.interrupt();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {	
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		Set<String> userIds = new HashSet<String>();
		for (int i=0; i < demoCases.size(); i++) {
			DemoCase demoCase = demoCases.get(i);
			userIds.add(demoCase.getUserId());
			DemoTrackVo demoTrack = 
				this.demoDao.getDemoTrack(demoCase.getTrackId());
			if (demoTrack != null) {
				this.demoTaskExecutor.execute(
					new DemoTrackRunner(
						demoCase, demoTrack, 
						userService, senderService, 
						trackService));
				log.info("demo track '" + demoCase.getName() + "' found and started.");
			} else {
				log.info("demo track '" + demoCase.getName() + "' not found.");
			}
		}
	}	
}
