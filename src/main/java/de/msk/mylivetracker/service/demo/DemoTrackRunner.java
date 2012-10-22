package de.msk.mylivetracker.service.demo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.demo.DemoPositionVo;
import de.msk.mylivetracker.domain.demo.DemoTrackVo;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.service.user.IUserService;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionExecutor;

/**
 * DemoTrackRunner.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DemoTrackRunner extends Thread {
	
	private static final Log log = LogFactory.getLog(DemoTrackRunner.class);
	
	private DemoCase demoCase;
	private DemoTrackVo demoTrack;
	private IUserService userService;
	private ISenderService senderService;
	private ITrackService trackService;
	
	public DemoTrackRunner(
		DemoCase demoCase,
		DemoTrackVo demoTrack,
		IUserService userService,
		ISenderService senderService,
		ITrackService trackService) {
		this.demoCase = demoCase;
		this.demoTrack = demoTrack;
		this.userService = userService;
		this.senderService = senderService;
		this.trackService = trackService;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		log.info("DemoTrackRunner[" + demoTrack.getTrackId() + "] has been interrupted.");
		super.interrupt();
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {		
		boolean statusOk = true;		
		
		while (statusOk) {
			long timeSecs = 0;
			try {
				// sleep 'startInSeconds'.
				Thread.sleep(this.demoCase.getStartInSeconds() * 1000);
				log.debug("start demo case: " + this.demoCase.getTrackId());
				
				// reset active track of sender if exists otherwise create a new track.
				boolean activeTrackExists = 
					this.trackService.resetActiveTrack(this.demoCase.getSenderId());
				log.debug("demo active track resetted: " + activeTrackExists);
				if (!activeTrackExists) {
					this.trackService.createTrack(
						demoCase.getUserId(),
						this.senderService.getSender(demoCase.getSenderId()), 
						demoCase.getName(), 
						this.userService.getUserWithoutRole(
							demoCase.getUserId()).getOptions().getDefTrackReleaseStatus());		
					log.debug("demo track created: " + demoTrack.getTrackName());
				}
				
				// run track ('send' positions).
				Thread.sleep(3000);				
				for (DemoPositionVo pos : demoTrack.getPositions()) {						
					Thread.sleep((pos.getOffsetInSeconds() - timeSecs) * 1000);
					timeSecs = pos.getOffsetInSeconds();					
					DateTime timestamp = new DateTime();
					DataReceivedVo dataReceived = DataReceivedVo.createInstance(senderService, timestamp);
					dataReceived.setDeviceActionExecutor(DeviceActionExecutor.StorePositionMessage);
					dataReceived.getSenderFromRequest().setSenderId(demoCase.getSenderId());
					dataReceived.getSenderFromRequest().authorize(senderService);
					dataReceived.getPosition().setTimeRecorded(timestamp);
					dataReceived.getPosition().setLatitudeInDecimal(pos.getLatitudeInDecimal());
					dataReceived.getPosition().setLongitudeInDecimal(pos.getLongitudeInDecimal());			
					dataReceived.getPosition().setAddress(pos.getAddress());
					dataReceived.getSenderState().addState(pos.getSenderState());					
					dataReceived.getMessage().setContent(pos.getMessage());
					dataReceived.getMobNwCell().setMobileCountryCode(pos.getMobileCountryCode());
					dataReceived.getMobNwCell().setMobileNetworkCode(pos.getMobileNetworkCode());
					dataReceived.getMobNwCell().setNetworkName(pos.getMobileNetworkName());
					dataReceived.getMobNwCell().setCellId(pos.getMobileCellId());
					dataReceived.getMobNwCell().setLocalAreaCode(pos.getMobileLocalAreaCode());
					dataReceived.getCardiacFunction().setHeartrateInBpm(pos.getHeartrateInBpm());
					dataReceived.getCardiacFunction().setHeartrateMinInBpm(pos.getHeartrateMinInBpm());
					dataReceived.getCardiacFunction().setHeartrateMaxInBpm(pos.getHeartrateMaxInBpm());
					dataReceived.getCardiacFunction().setHeartrateAvgInBpm(pos.getHeartrateAvgInBpm());
					trackService.storePositionAndMessage(
						this.userService.getUserWithoutRole(
							demoCase.getUserId()), dataReceived);									
				}				
			} catch (InterruptedException e) {
				statusOk = false;
			} catch (Exception e) {
				log.info("demo track runner stopped: " + e.getMessage());
				statusOk = false;
			}
		}			
	}		
}
