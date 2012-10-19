package de.msk.mylivetracker.web.uploader.processor.server.http;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo;
import de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo.Status;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.uploader.processor.DataPacketCreator;
import de.msk.mylivetracker.web.uploader.processor.ProcessorType;

/**
 * HttpServer.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class HttpServer extends AbstractController {
	
	private Services services;
	private DataPacketCreator dataPacketCreator;
	private String name;
	private boolean running;
	private int listenPort;
	
	/* (non-Javadoc)
	 * @see org.springframework.web.context.support.WebApplicationObjectSupport#initServletContext(javax.servlet.ServletContext)
	 */
	@Override
	protected void initServletContext(ServletContext servletContext) {
		this.listenPort = this.services.getApplicationService().getApplicationPort();
		this.services.getStatisticsService().logUploaderServerStatus(
			new UploaderServerStatusVo(
				"HttpServerAt" + this.services.getApplicationService().getApplicationPort(),
				this.services.getApplicationService().getApplicationPort().intValue(), 
				ProcessorType.Http.getProtocol(), 
				Status.Running, 
				new DateTime(), 
				false, 
				false, 
				-1, 
				-1, 
				-1));
		this.running = true;
	}

	/* (non-Javadoc)
	 * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		HttpServletProcessor httpServletProcessor =
			new HttpServletProcessor("HttpServerAt80",
				ProcessorType.Http,
				services,
				dataPacketCreator,
				request,
				response);
		httpServletProcessor.process();
		this.running = true;
		return null;
	}
	public Services getServices() {
		return services;
	}
	public void setServices(Services services) {
		this.services = services;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isRunning() {
		return running;
	}
	public int getListenPort() {
		return listenPort;
	}
}
