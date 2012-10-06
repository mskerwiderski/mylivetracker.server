package de.msk.mylivetracker.service;

import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.service.demo.IDemoService;
import de.msk.mylivetracker.service.geocoding.AbstractGeocodingService;
import de.msk.mylivetracker.service.mobilenetwork.IMobileNetworkService;
import de.msk.mylivetracker.service.rpc.IRpcService;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.service.sms.ISmsService;
import de.msk.mylivetracker.service.statistics.IStatisticsService;
import de.msk.mylivetracker.service.statusparams.IStatusParamsService;
import de.msk.mylivetracker.service.ticket.ITicketService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.service.user.IUserService;
import de.msk.mylivetracker.service.user.IUserSessionStatusService;
import de.msk.mylivetracker.service.util.IUtilService;

/**
 * Services.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class Services {
	private IApplicationService applicationService;
	private IDemoService demoService;
	private AbstractGeocodingService geocodingService;
	private IMobileNetworkService mobileNetworkService;
	private IRpcService rpcService;
	private ISenderService senderService;
	private ISmsService smsService;
	private IStatisticsService statisticsService;
	private IStatusParamsService statusParamsService;
	private ITicketService ticketService;
	private ITrackService trackService;
	private IUserService userService;
	private IUserSessionStatusService userSessionStatusService;
	private IUtilService utilService;
	public IApplicationService getApplicationService() {
		return applicationService;
	}
	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}
	public IDemoService getDemoService() {
		return demoService;
	}
	public void setDemoService(IDemoService demoService) {
		this.demoService = demoService;
	}
	public AbstractGeocodingService getGeocodingService() {
		return geocodingService;
	}
	public void setGeocodingService(AbstractGeocodingService geocodingService) {
		this.geocodingService = geocodingService;
	}
	public IMobileNetworkService getMobileNetworkService() {
		return mobileNetworkService;
	}
	public void setMobileNetworkService(IMobileNetworkService mobileNetworkService) {
		this.mobileNetworkService = mobileNetworkService;
	}
	public IRpcService getRpcService() {
		return rpcService;
	}
	public void setRpcService(IRpcService rpcService) {
		this.rpcService = rpcService;
	}
	public ISenderService getSenderService() {
		return senderService;
	}
	public void setSenderService(ISenderService senderService) {
		this.senderService = senderService;
	}
	public ISmsService getSmsService() {
		return smsService;
	}
	public void setSmsService(ISmsService smsService) {
		this.smsService = smsService;
	}
	public IStatisticsService getStatisticsService() {
		return statisticsService;
	}
	public void setStatisticsService(IStatisticsService statisticsService) {
		this.statisticsService = statisticsService;
	}
	public IStatusParamsService getStatusParamsService() {
		return statusParamsService;
	}
	public void setStatusParamsService(IStatusParamsService statusParamsService) {
		this.statusParamsService = statusParamsService;
	}
	public ITicketService getTicketService() {
		return ticketService;
	}
	public void setTicketService(ITicketService ticketService) {
		this.ticketService = ticketService;
	}
	public ITrackService getTrackService() {
		return trackService;
	}
	public void setTrackService(ITrackService trackService) {
		this.trackService = trackService;
	}
	public IUserService getUserService() {
		return userService;
	}
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}
	public IUserSessionStatusService getUserSessionStatusService() {
		return userSessionStatusService;
	}
	public void setUserSessionStatusService(
			IUserSessionStatusService userSessionStatusService) {
		this.userSessionStatusService = userSessionStatusService;
	}
	public IUtilService getUtilService() {
		return utilService;
	}
	public void setUtilService(IUtilService utilService) {
		this.utilService = utilService;
	}
}
