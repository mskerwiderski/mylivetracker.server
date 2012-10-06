package de.msk.mylivetracker.web.uploader.processor.server.http;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.uploader.processor.DataPacketCreator;
import de.msk.mylivetracker.web.uploader.processor.PreProcessResultDsc;
import de.msk.mylivetracker.web.uploader.processor.ProcessorType;
import de.msk.mylivetracker.web.uploader.processor.RemoteInfo;
import de.msk.mylivetracker.web.uploader.processor.ServerInfo;
import de.msk.mylivetracker.web.uploader.processor.UploadProcessor;

/**
 * HttpServletProcessor.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class HttpServletProcessor extends UploadProcessor {

	private static final Log log = LogFactory.getLog(HttpServletProcessor.class);
	
	private HttpServletRequest request;
	private HttpServletResponse response;
	
	public HttpServletProcessor(String serverName, 
		ProcessorType processorType,
		Services supportedServices,
		DataPacketCreator dataPacketCreator,
		HttpServletRequest request,
		HttpServletResponse response) {
		super(serverName, processorType, 
			supportedServices, dataPacketCreator,
			false);
		this.request = request;
		this.response = response;
	}	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.UploadProcessor#getRemoteInfo()
	 */
	@Override
	public RemoteInfo getRemoteInfo() {
		return new RemoteInfo(
			this.request.getRemoteAddr(),
			this.request.getRemoteHost(),
			String.valueOf(this.request.getServerPort())
		);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.UploadProcessor#getServerInfo()
	 */
	@Override
	public ServerInfo getServerInfo() {
		return new ServerInfo(this.getProcessorType().getName(),
			String.valueOf(this.request.getServerPort())
		);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.UploadProcessor#postProcess(de.msk.mylivetracker.web.uploader.processor.UploadProcessor.ProcessResult, boolean)
	 */
	@Override
	public void postProcess(ProcessResult result, boolean cutConnection) {
		this.response.setContentType("text/plain");
		this.response.setStatus(HttpServletResponse.SC_OK);
		try {
			String response = (result.getResponseStr() != null) ? 
				result.getResponseStr() : result.getResultMsg();
			this.response.getOutputStream().print(response);
		} catch (IOException e) {
			log.info(e.toString());
		}
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.UploadProcessor#preProcess()
	 */
	@Override
	public PreProcessResultDsc preProcess() {
		return new PreProcessResultDsc(new HttpServletRequestCtx(this.request));		
	}
}
