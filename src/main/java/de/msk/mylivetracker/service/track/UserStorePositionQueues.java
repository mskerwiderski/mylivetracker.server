package de.msk.mylivetracker.service.track;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.task.TaskExecutor;

import de.msk.mylivetracker.dao.ITrackDao;
import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.service.sms.ISmsService;
import de.msk.mylivetracker.service.statistics.IStatisticsService;

/**
 * UserStorePositionQueues.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UserStorePositionQueues {

	private static final Log log = LogFactory.getLog(UserStorePositionQueues.class);
	
	private ConcurrentMap<String, StorePositionProcessor> queueMap =
		new ConcurrentHashMap<String, StorePositionProcessor>();	
	
	private IStatisticsService statisticsService;
	private ITrackDao trackDao;
	private ISenderService senderService;
	private ISmsService smsService;
	private AbstractGeocodingService geocodingService;	
	private TaskExecutor storePositionTaskExecutor;
	private Integer maxNumberOfRetriesOnDataAccessException;
	private long pauseBeforeRetryOfStorePositionInMSecs;
	private long queueTimeoutInSecs;
	private long queuePollIntervalInMSecs;
	
	public synchronized void push(UserWithoutRoleVo user, 
		DataReceivedVo dataReceived) {
		SenderVo sender = dataReceived.getSenderFromRequest().getSender();
		StorePositionProcessor processor = null; 				
		// one queue for every sender.
		boolean create = true;	
		if (queueMap.containsKey(sender.getSenderId())) {
			processor = queueMap.get(sender.getSenderId());
			if (!processor.getInfo().isExpired()) {
				create = false;
				log.debug("StorePositionProcessor found for sender '" + 
					sender.getSenderId() + "': with id '" + processor.getId() + "'.");				
			} else {
				create = true;
				log.debug("Expired StorePositionProcessor found for sender '" + 
					sender.getSenderId() + "': with id '" + processor.getId() + "'.");									
			}
		} 
		
		if (create)	{
			processor =	new StorePositionProcessor(
				this.queueMap, user, sender, 
				this.trackDao, this.senderService, this.smsService,
				this.geocodingService, this.statisticsService,
				this.maxNumberOfRetriesOnDataAccessException,
				this.pauseBeforeRetryOfStorePositionInMSecs,
				this.queueTimeoutInSecs, this.queuePollIntervalInMSecs);			
			storePositionTaskExecutor.execute(processor);
			queueMap.put(sender.getSenderId(), processor);
			log.info("new StorePositionProcessor started for sender " + 
				sender.getSenderId());
		}
		
		processor.push(user, dataReceived);
	}
	
	/**
	 * @return the queueMap
	 */
	public Collection<StorePositionProcessor> getStorePositionsProcessorsRO() {
		return Collections.unmodifiableCollection(queueMap.values());
	}

	/**
	 * @return the queueMap
	 */
	public ConcurrentMap<String, StorePositionProcessor> getStorePositionsProcessors() {
		return queueMap;
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

	/**
	 * @return the storePositionTaskExecutor
	 */
	public TaskExecutor getStorePositionTaskExecutor() {
		return storePositionTaskExecutor;
	}

	/**
	 * @param storePositionTaskExecutor the storePositionTaskExecutor to set
	 */
	public void setStorePositionTaskExecutor(TaskExecutor storePositionTaskExecutor) {
		this.storePositionTaskExecutor = storePositionTaskExecutor;
	}

	/**
	 * @return the maxNumberOfRetriesOnDataAccessException
	 */
	public Integer getMaxNumberOfRetriesOnDataAccessException() {
		return maxNumberOfRetriesOnDataAccessException;
	}

	/**
	 * @param maxNumberOfRetriesOnDataAccessException the maxNumberOfRetriesOnDataAccessException to set
	 */
	public void setMaxNumberOfRetriesOnDataAccessException(
			Integer maxNumberOfRetriesOnDataAccessException) {
		this.maxNumberOfRetriesOnDataAccessException = maxNumberOfRetriesOnDataAccessException;
	}

	/**
	 * @return the pauseBeforeRetryOfStorePositionInMSecs
	 */
	public long getPauseBeforeRetryOfStorePositionInMSecs() {
		return pauseBeforeRetryOfStorePositionInMSecs;
	}

	/**
	 * @param pauseBeforeRetryOfStorePositionInMSecs the pauseBeforeRetryOfStorePositionInMSecs to set
	 */
	public void setPauseBeforeRetryOfStorePositionInMSecs(
			long pauseBeforeRetryOfStorePositionInMSecs) {
		this.pauseBeforeRetryOfStorePositionInMSecs = pauseBeforeRetryOfStorePositionInMSecs;
	}

	/**
	 * @return the queueTimeoutInSecs
	 */
	public long getQueueTimeoutInSecs() {
		return queueTimeoutInSecs;
	}

	/**
	 * @param queueTimeoutInSecs the queueTimeoutInSecs to set
	 */
	public void setQueueTimeoutInSecs(long queueTimeoutInSecs) {
		this.queueTimeoutInSecs = queueTimeoutInSecs;
	}

	/**
	 * @return the queuePollIntervalInMSecs
	 */
	public long getQueuePollIntervalInMSecs() {
		return queuePollIntervalInMSecs;
	}

	/**
	 * @param queuePollIntervalInMSecs the queuePollIntervalInMSecs to set
	 */
	public void setQueuePollIntervalInMSecs(long queuePollIntervalInMSecs) {
		this.queuePollIntervalInMSecs = queuePollIntervalInMSecs;
	}
}
