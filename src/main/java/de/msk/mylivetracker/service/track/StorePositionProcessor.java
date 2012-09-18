package de.msk.mylivetracker.service.track;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.dao.ITrackDao;
import de.msk.mylivetracker.domain.CardiacFunctionVo;
import de.msk.mylivetracker.domain.ClientInfoVo;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.EmergencySignalVo;
import de.msk.mylivetracker.domain.MessageVo;
import de.msk.mylivetracker.domain.MobNwCellVo;
import de.msk.mylivetracker.domain.PositionVo;
import de.msk.mylivetracker.domain.SenderStateVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.statistics.StorePositionProcessorInfoVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ISmsService;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService.LatLonPos;
import de.msk.mylivetracker.service.statistics.IStatisticsService;

/**
 * StorePositionProcessor.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class StorePositionProcessor extends Thread {

	private static final Log log = LogFactory.getLog(StorePositionProcessor.class);
	
	public static class Info {
		private final UserWithoutRoleVo user;
		private final SenderVo sender;
		private final DateTime started = new DateTime();	
		private DateTime updated = null;
		private boolean expired = false;
		private long processedPositions = 0;
		private long skippedPositions = 0;
		private long errorCount = 0;
		private long retryCount = 0;
		private int maxNumberOfRetriesOccurred = 0;
		private String lastErrorMsg = null;
		private DateTime lastErrorOccurred = null;
		private Boolean lastStatus = true;
		private String reasonStopped = null;
		private int sendEmergencySmsDone = 0;
		private int sendEmergencySmsFailed = 0;
		private String lastSmsErrorMsg = null;
		private DateTime lastSmsErrorOccurred = null;
		private DateTime lastSmsSuccessfullySent = null;
		protected Info(UserWithoutRoleVo user, SenderVo sender) {
			this.user = user;
			this.sender = sender;
		}
		protected void storePosNotDone(StoreInDbResult storeInDbResult) {
			this.maxNumberOfRetriesOccurred = Math.max(
				this.maxNumberOfRetriesOccurred, storeInDbResult.getRetriesNeeded());
			this.retryCount = this.retryCount + storeInDbResult.getRetriesNeeded();
		}
		protected void setLastSmsSendStatus(SendSmsResult sendSmsResult) {
			if (sendSmsResult != null) {
				if (sendSmsResult.isDone()) {
					this.sendEmergencySmsDone++;
					this.lastSmsSuccessfullySent = new DateTime();
				} else {
					this.sendEmergencySmsFailed++;
					this.lastSmsErrorMsg = sendSmsResult.getLastErrorMsg();
					this.lastSmsErrorOccurred = new DateTime();
				}
			}
		}
		protected void exceptionOccurred(Exception e) {
			this.errorCount++;
			this.skippedPositions++;
			this.lastErrorOccurred = this.updated;
			this.lastErrorMsg = e.getLocalizedMessage();
			this.lastStatus = false;					
		}
		protected void resetLastStatus() {
			this.lastStatus = true;
		}
		protected void finallyReached(String reasonStopped) {
			this.processedPositions++;
			this.updated = new DateTime();
			this.reasonStopped = reasonStopped;
		}
		protected void setExpiredTrue() {
			this.expired = true;
		}
		public UserWithoutRoleVo getUser() {
			return user;
		}		
		public SenderVo getSender() {
			return sender;
		}
		public DateTime getStarted() {
			return started;
		}
		public DateTime getUpdated() {
			return updated;
		}
		public boolean isExpired() {			
			return expired;
		}
		public long getProcessedPositions() {
			return processedPositions;
		}
		public long getSkippedPositions() {
			return skippedPositions;
		}
		public long getErrorCount() {
			return errorCount;
		}
		public long getRetryCount() {
			return retryCount;
		}
		public int getMaxNumberOfRetriesOccurred() {
			return maxNumberOfRetriesOccurred;
		}		
		public String getLastErrorMsg() {
			return lastErrorMsg;
		}
		public String getLastErrorMsgAbbr() {
			return StringUtils.abbreviate(lastErrorMsg, 30);
		}
		public DateTime getLastErrorOccurred() {
			return lastErrorOccurred;
		}
		public Boolean getLastStatus() {
			return lastStatus;
		}
		public String getReasonStopped() {
			return reasonStopped;
		}
		public int getSendEmergencySmsDone() {
			return sendEmergencySmsDone;
		}
		public int getSendEmergencySmsFailed() {
			return sendEmergencySmsFailed;
		}
		public String getLastSmsErrorMsg() {
			return lastSmsErrorMsg;
		}
		public DateTime getLastSmsErrorOccurred() {
			return lastSmsErrorOccurred;
		}
		public DateTime getLastSmsSuccessfullySent() {
			return lastSmsSuccessfullySent;
		}				
	}
	
	private ConcurrentMap<String, StorePositionProcessor> queueMap = null;	
	private SenderVo sender;
	private Info info = null;
	private ConcurrentLinkedQueue<StorePositionDsc> processorQueue = null;
	private ITrackDao trackDao;
	private ISenderService senderService;
	private ISmsService smsService;
	private AbstractGeocodingService geocodingService;	
	private IStatisticsService statisticsService;
	private Integer maxNumberOfRetriesOnDataAccessException;
	private long pauseBeforeRetryOfStorePositionInMSecs = 10;
	private long queueTimeoutInSecs;
	private long queuePollIntervalInMSecs;
	private boolean stopped = false;
		
	/**
	 * @return the stopped
	 */
	public boolean isStopped() {
		return this.stopped;
	}

	/**
	 * @param stopped the stopped to set
	 */
	public void adminStop() {
		this.stopped = true;
	}

	/**
	 * @param senderId
	 * @param trackDao
	 * @param senderService
	 * @param geocodingService
	 */
	public StorePositionProcessor(
		ConcurrentMap<String, StorePositionProcessor> queueMap,	
		UserWithoutRoleVo user, SenderVo sender, ITrackDao trackDao,
		ISenderService senderService, ISmsService smsService,
		AbstractGeocodingService geocodingService,
		IStatisticsService statisticsService,
		Integer maxNumberOfRetriesOnDataAccessException,
		long pauseBeforeRetryOfStorePositionInMSecs,
		long queueTimeoutInSecs, long queuePollIntervalInMSecs) {
		this.queueMap = queueMap;
		this.sender = sender;
		this.info = new Info(user, sender);
		this.trackDao = trackDao;
		this.senderService = senderService;
		this.smsService = smsService;
		this.geocodingService = geocodingService;
		this.statisticsService = statisticsService;
		this.maxNumberOfRetriesOnDataAccessException = 
			maxNumberOfRetriesOnDataAccessException;
		this.pauseBeforeRetryOfStorePositionInMSecs =
			pauseBeforeRetryOfStorePositionInMSecs;
		this.queueTimeoutInSecs = queueTimeoutInSecs;
		this.queuePollIntervalInMSecs = queuePollIntervalInMSecs;
		processorQueue = new ConcurrentLinkedQueue<StorePositionDsc>();
	}

	public static class StorePositionDsc {
		private UserWithoutRoleVo user;
		private DataReceivedVo dataReceived;
		public StorePositionDsc(
			UserWithoutRoleVo user, 
			DataReceivedVo dataReceived) {
			this.user = user;			
			this.dataReceived = dataReceived;
		}
		public UserWithoutRoleVo getUser() {
			return user;
		}
		public DataReceivedVo getDataReceived() {
			return dataReceived;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "StorePositionDsc [user=" + user
				+ ", dataReceived=" + dataReceived + "]";
		}			
	}
		
	public Info getInfo() {
		return info;
	}

	public int getPositionsInQueue() {
		return processorQueue.size();
	}
	
	public void push(UserWithoutRoleVo user, 
		DataReceivedVo dataReceived) {
		processorQueue.add(
			new StorePositionDsc(user, dataReceived));
	}	

	public void clear() {
		processorQueue.clear();
	}
	
	private static class PositionNotStoredException extends Exception {
		private static final long serialVersionUID = -5613968555624176580L;
		private PositionNotStoredException() {
		}
		private PositionNotStoredException(String message, Throwable cause) {
			super(message, cause);
		}
		private PositionNotStoredException(String message) {
			super(message);
		}
		private PositionNotStoredException(Throwable cause) {
			super(cause);
		}		
	}
	
	private StorePositionDsc takeStorePositionDsc() 
		throws InterruptedException {
		long takeStarted = (new DateTime()).getAsMSecs(); 
		StorePositionDsc storePositionDsc = null;
		
		while ((((new DateTime()).getAsMSecs() - takeStarted) <
			(this.queueTimeoutInSecs * 1000)) && 
			(storePositionDsc == null)) {
			if (this.isStopped()) {
			    throw new InterruptedException("thread stopped by administrator.");
			}
			storePositionDsc = this.processorQueue.poll();			
			if (storePositionDsc == null) {
				Thread.sleep(this.queuePollIntervalInMSecs);
			}
		}
		
		return storePositionDsc;
	}		
		
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		boolean doLoop = true;
		String reasonStopped = "unknown";
		while (doLoop) {
			try {
				StorePositionDsc storePositionDsc = this.takeStorePositionDsc();
				if (storePositionDsc != null) {
					StoreResult storeResult = storePosition(storePositionDsc);
					this.info.setLastSmsSendStatus(storeResult.getSendSmsResult());
					if (!storeResult.getStoreInDbResult().isDone()) {
						this.info.storePosNotDone(storeResult.getStoreInDbResult());
						throw new PositionNotStoredException(
							"storePosition(" + storePositionDsc.toString() + ") failed: " + 
							storeResult.getStoreInDbResult().getLastErrorMsg());
					}
					this.info.resetLastStatus();
				} else {
					log.info("StorePositionProcessor for sender '" + 
						this.info.getSender().getSenderId() + "' has been expired.");
					doLoop = false;
					reasonStopped = "expired";
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
				log.info(e.toString());
				doLoop = false;
				reasonStopped = e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				log.info(e.toString());
				this.info.exceptionOccurred(e);			
				doLoop = true;
				reasonStopped = e.getMessage();
			} finally {
				this.info.finallyReached(reasonStopped);				
			}
		}	
		this.info.setExpiredTrue();
		this.queueMap.remove(this.sender.getSenderId());
		StorePositionProcessorInfoVo storePositionProcessorInfo = 
			new StorePositionProcessorInfoVo(
				this.info.getSender().getSenderId(),
				this.info.getStarted(),						
				this.info.getUpdated(),
				this.info.getProcessedPositions(),
				this.info.getSkippedPositions(),
				this.info.getErrorCount(),
				this.info.getRetryCount(),
				this.info.getMaxNumberOfRetriesOccurred(),
				this.info.getLastErrorMsg(),
				this.info.getLastErrorOccurred(),
				this.info.getLastStatus(),
				this.info.getReasonStopped());				
		statisticsService.logStorePositionProcessorInfo(storePositionProcessorInfo);
		log.debug("Expired StorePositionProcessor with id '" + 
			this.getId() + "' has been removed from queueMap.");
	}	

	private static final class StoreResult {
		private StoreInDbResult storeInDbResult;
		private SendSmsResult sendSmsResult;
		/**
		 * @param storeInDbResult
		 * @param sendSmsResult
		 */
		private StoreResult(StoreInDbResult storeInDbResult,
				SendSmsResult sendSmsResult) {
			this.storeInDbResult = storeInDbResult;
			this.sendSmsResult = sendSmsResult;
		}
		public StoreInDbResult getStoreInDbResult() {
			return storeInDbResult;
		}
		public SendSmsResult getSendSmsResult() {
			return sendSmsResult;
		}		
	}
	
	private static final class StoreInDbResult {
		private boolean done = false;		
		private int retriesNeeded = 0;
		private String lastErrorMsg = null;
		public StoreInDbResult(boolean done, int retriesNeeded,
			String lastErrorMsg) {
			this.done = done;
			this.retriesNeeded = retriesNeeded;
			this.lastErrorMsg = lastErrorMsg;
		}
		public boolean isDone() {
			return done;
		}
		public int getRetriesNeeded() {
			return retriesNeeded;
		}
		public String getLastErrorMsg() {
			return lastErrorMsg;
		}		
	}
	
	private static final class SendSmsResult {
		private boolean done = false;		
		private String lastErrorMsg = null;
		public SendSmsResult(boolean done,
			String lastErrorMsg) {
			this.done = done;
			this.lastErrorMsg = lastErrorMsg;
		}
		public boolean isDone() {
			return done;
		}
		public String getLastErrorMsg() {
			return lastErrorMsg;
		}		
	}
	
	private SendSmsResult sendSmsIfEmergencySignalReceived(
		UserWithoutRoleVo user, DataReceivedVo dataReceived) {
		boolean done = true;
		String lastErrorMsg = "";
		try {
			if (dataReceived.getEmergencySignal().getActive()) {
				smsService.sendEmergencyActivatedSms(user, dataReceived);
			} else {
				smsService.sendEmergencyDeactivatedSms(user, dataReceived);
			}
		} catch (Exception e) {
			done = false;
			lastErrorMsg = e.getMessage();
		}
		return new SendSmsResult(done, lastErrorMsg);
	}
	
	private StoreInDbResult storeInDatabase(UserWithoutRoleVo user, SenderVo sender,
		PositionVo position, MobNwCellVo mobNwCell,
		MessageVo message, SenderStateVo senderState, CardiacFunctionVo cardiacFunction,
		EmergencySignalVo emergencySignal, ClientInfoVo clientInfo) {
		boolean done = false;
		String lastErrorMsg = null;
		int retries = 0;
		while (!done && (retries < maxNumberOfRetriesOnDataAccessException)) {
			try {
				trackDao.storePositionAndMessage(
					user, sender, 
					position, mobNwCell, 
					message, senderState,
					cardiacFunction,
					emergencySignal,
					clientInfo,
					user.getOptions());				
				done = true;				
			} catch (DataAccessException e) {
				log.fatal(e.toString());
				lastErrorMsg = e.getLocalizedMessage();
				done = false;			
			} finally {
				if (!done) {
					retries++;
					try {
						Thread.sleep(this.pauseBeforeRetryOfStorePositionInMSecs);
					} catch (InterruptedException e) {
						log.fatal(e.toString());
					}
				}
			}
		}		 		
		return new StoreInDbResult(done, retries, lastErrorMsg);
	}
	
	private StoreResult storePosition(StorePositionDsc storePositionDsc) {
		log.debug("store position " + storePositionDsc.toString());
		DataReceivedVo dataReceived = 
			storePositionDsc.getDataReceived();						
		StoreInDbResult storeInDbResult = null;
		SendSmsResult sendSmsResult = null;
		
		// store position and/or message if exists.
		if (!dataReceived.hasValidData()) {
			storeInDbResult = new StoreInDbResult(false, 0, "no valid data found.");
		} else {
			if (storePositionDsc.getUser().getOptions().getGeocodingEnabled() &&
				dataReceived.hasValidPosition() && 
				StringUtils.isEmpty(dataReceived.getPosition().getAddress())) {
				dataReceived.getPosition().
					setAddress(geocodingService.getAddressOfPosition(
						new LatLonPos(
							dataReceived.getPosition().getLatitudeInDecimal(), 
							dataReceived.getPosition().getLongitudeInDecimal()),
					storePositionDsc.user.getOptions().getGeocoderLanguage()));
			}
			SenderVo sender = storePositionDsc.getDataReceived().
				getSenderFromRequest().getSender();
			// check if sender runs in redirection mode.
			if (!StringUtils.isEmpty(sender.getRedirectTo())) {
				
				SenderVo redirectToSender = senderService.getSender(sender.getRedirectTo());
				if (redirectToSender == null) {
					log.debug("position cannot be redirected from " +
						sender.getSenderId() + 
						" to " + sender.getRedirectTo() +
						" because target sender does not exist.");
				} else {					
					log.debug("position will be redirected from " + 
						sender.getSenderId() + 
						" to " + sender.getRedirectTo());
					sender = redirectToSender;
				}
				// sender in redirection mode is not allowed to set track id or track name.
				dataReceived.getClientInfo().setTrackId(null);
				dataReceived.getClientInfo().setTrackName(null);
			}	
			
			if (dataReceived.hasValidEmergencySignal() &&
				storePositionDsc.getUser().getEmergency().getSmsUnlocked() &&
				storePositionDsc.getUser().getEmergency().getSmsEnabled()) {
				sendSmsResult = sendSmsIfEmergencySignalReceived(
					storePositionDsc.getUser(), dataReceived);
				dataReceived.getEmergencySignal().
					setSmsSuccessfullySent(sendSmsResult.isDone());
			}
			storeInDbResult = storeInDatabase(
				storePositionDsc.getUser(),
				sender, 
				dataReceived.getPosition(),
				dataReceived.getMobNwCell(),
				dataReceived.getMessage(),
				dataReceived.getSenderState(),
				dataReceived.getCardiacFunction(),
				dataReceived.getEmergencySignal(),
				dataReceived.getClientInfo());			
		}
		return new StoreResult(storeInDbResult, sendSmsResult);
	}
	
	/**
	 * @return the trackDao
	 */
	public ITrackDao getTrackDao() {
		return trackDao;
	}

	/**
	 * @param trackDao the trackDao to set
	 */
	public void setTrackDao(ITrackDao trackDao) {
		this.trackDao = trackDao;
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
	 * @return the smsService
	 */
	public ISmsService getSmsService() {
		return smsService;
	}

	/**
	 * @param smsService the smsService to set
	 */
	public void setSmsService(ISmsService smsService) {
		this.smsService = smsService;
	}

	/**
	 * @return the geocodingService
	 */
	public AbstractGeocodingService getGeocodingService() {
		return geocodingService;
	}

	/**
	 * @param geocodingService the geocodingService to set
	 */
	public void setGeocodingService(AbstractGeocodingService geocodingService) {
		this.geocodingService = geocodingService;
	}	
}
