package de.msk.mylivetracker.web.uploader.processor;

import de.msk.mylivetracker.service.IMobileNetworkService;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ISmsService;
import de.msk.mylivetracker.service.ITrackService;
import de.msk.mylivetracker.service.IUserService;
import de.msk.mylivetracker.service.statistics.IStatisticsService;

/**
 * SupportedServices.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SupportedServices {
	private IStatisticsService statisticsService;
	private IUserService userService;
	private ISenderService senderService;
	private ITrackService trackService;
	private IMobileNetworkService mobileNetworkService;
	private ISmsService smsService;
	
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
	 * @return the mobileNetworkService
	 */
	public IMobileNetworkService getMobileNetworkService() {
		return mobileNetworkService;
	}
	/**
	 * @param mobileNetworkService the mobileNetworkService to set
	 */
	public void setMobileNetworkService(IMobileNetworkService mobileNetworkService) {
		this.mobileNetworkService = mobileNetworkService;
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
}
