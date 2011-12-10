package de.msk.mylivetracker.web.uploader.processor.server.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo;
import de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo.Status;
import de.msk.mylivetracker.service.IApplicationService;
import de.msk.mylivetracker.service.statistics.IStatisticsService;
import de.msk.mylivetracker.web.uploader.processor.DataPacketCreator;
import de.msk.mylivetracker.web.uploader.processor.ProcessorType;
import de.msk.mylivetracker.web.uploader.processor.SupportedServices;

/**
 * HttpServletCtrl.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class HttpServletCtrl extends AbstractController {
	
	private IApplicationService applicationService;
	private IStatisticsService statisticsService;
	
	private SupportedServices supportedServices;
	private DataPacketCreator dataPacketCreator;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.context.support.WebApplicationObjectSupport#initServletContext(javax.servlet.ServletContext)
	 */
	@Override
	protected void initServletContext(ServletContext servletContext) {
		this.statisticsService.logUploaderServerStatus(
			new UploaderServerStatusVo(
				"HttpServerAt" + this.applicationService.getApplicationPort(),
				this.applicationService.getApplicationPort().intValue(), 
				ProcessorType.Http.getProtocol(), 
				Status.Running, 
				new DateTime(), 
				false, 
				false, 
				-1, 
				-1, 
				-1));
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpServletProcessor httpServletProcessor =
			new HttpServletProcessor("HttpServletAt80",
				ProcessorType.Http,
				supportedServices,
				dataPacketCreator,
				request,
				response);
		httpServletProcessor.process();
		return null;
	}

	/**
	 * @return the supportedServices
	 */
	public SupportedServices getSupportedServices() {
		return supportedServices;
	}

	/**
	 * @param supportedServices the supportedServices to set
	 */
	public void setSupportedServices(SupportedServices supportedServices) {
		this.supportedServices = supportedServices;
	}

	/**
	 * @return the dataPacketCreator
	 */
	public DataPacketCreator getDataPacketCreator() {
		return dataPacketCreator;
	}

	/**
	 * @param dataPacketCreator the dataPacketCreator to set
	 */
	public void setDataPacketCreator(
			DataPacketCreator dataPacketCreator) {
		this.dataPacketCreator = dataPacketCreator;
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
}
