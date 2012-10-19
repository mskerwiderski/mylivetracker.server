package de.msk.mylivetracker.web.frontend.options;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.StringUtils;

import de.msk.mylivetracker.service.application.IApplicationService;
import de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter;
import de.msk.mylivetracker.web.uploader.processor.server.http.HttpServer;
import de.msk.mylivetracker.web.uploader.processor.server.tcp.TcpServer;
import de.msk.mylivetracker.web.uploader.processor.server.udp.UdpServer;
import de.msk.mylivetracker.web.util.request.ReqUrlStr;

/**
 * ServerInfo.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-19
 * 
 */
public class ServerInfo {

	public List<TcpServer> tcpServers;
	public List<UdpServer> udpServers;
	
	public static class HttpServerDsc {
		private HttpServer server;
		private List<HttpInterpreterDsc> dscs;
		public HttpServer getServer() {
			return server;
		}
		public List<HttpInterpreterDsc> getDscs() {
			return dscs;
		}
	}
	
	public static class HttpInterpreterDsc {
		private IDataInterpreter interpreter;
		private String url;
		public IDataInterpreter getInterpreter() {
			return interpreter;
		}
		public String getUrl() {
			return url;
		}
	}
	
	public List<HttpServer> httpServers;
	public String httpUrlTemplate;
	public IApplicationService applicationService;
	
	private static final String NOT_DETECTED = "NOT DETECTED";
	
	public ServerInfo() {
		
	}
	
	public String getDnsName() {
		return this.applicationService.getServerAddress();
	}
	public String getIpAddress() {
		String ipAddress = NOT_DETECTED;
		try {
			ipAddress = InetAddress.getByName(
				this.getDnsName()).getHostAddress();
		} catch (UnknownHostException e) {
			ipAddress = NOT_DETECTED;
		}
		return ipAddress;
	}
	
	public List<TcpServer> getTcpServers() {
		List<TcpServer> tcpServers = new ArrayList<TcpServer>();
		for (TcpServer tcpServer : this.tcpServers) {
			if (this.applicationService.isTcpPortUsed(
				tcpServer.getTcpServerConfig().getListenPort())) {
				tcpServers.add(tcpServer);
			}
		}
		return tcpServers;
	}
	public void setTcpServers(List<TcpServer> tcpServers) {
		this.tcpServers = tcpServers;
	}
	public List<UdpServer> getUdpServers() {
		List<UdpServer> udpServers = new ArrayList<UdpServer>();
		for (UdpServer udpServer : this.udpServers) {
			if (this.applicationService.isUdpPortUsed(
				udpServer.getUdpServerConfig().getListenPort())) {
				udpServers.add(udpServer);
			}
		}
		return udpServers;
	}
	public void setUdpServers(List<UdpServer> udpServers) {
		this.udpServers = udpServers;
	}
	public List<HttpServerDsc> getHttpServerDscs() {
		List<HttpServerDsc> httpServerDscs = new ArrayList<HttpServerDsc>();
		for (HttpServer server : this.httpServers) {
			HttpServerDsc httpServerDsc = new HttpServerDsc();
			httpServerDsc.server = server;
			httpServerDsc.dscs = new ArrayList<HttpInterpreterDsc>();
			for (IDataInterpreter interpreter : 
				server.getDataPacketCreator().getDataInterpreters()) {
				HttpInterpreterDsc httpInterpreterDsc = new HttpInterpreterDsc();
				httpInterpreterDsc.interpreter = interpreter;
				String httpCtrlName = this.httpUrlTemplate;
				httpCtrlName = StringUtils.replace(httpCtrlName, "*", interpreter.getId());
				httpInterpreterDsc.url = ReqUrlStr.create(
					this.applicationService.getApplicationBaseUrl(), httpCtrlName).toString();
				httpServerDsc.dscs.add(httpInterpreterDsc);
			}
			httpServerDscs.add(httpServerDsc);
		}
		return httpServerDscs;
	}
	public List<HttpServer> getHttpServers() {
		return httpServers;
	}
	public void setHttpServers(List<HttpServer> httpServers) {
		this.httpServers = httpServers;
	}
	public String getHttpUrlTemplate() {
		return httpUrlTemplate;
	}
	public void setHttpUrlTemplate(String httpUrlTemplate) {
		this.httpUrlTemplate = httpUrlTemplate;
	}
	public IApplicationService getApplicationService() {
		return applicationService;
	}
	public void setApplicationService(IApplicationService applicationService) {
		this.applicationService = applicationService;
	}
}
