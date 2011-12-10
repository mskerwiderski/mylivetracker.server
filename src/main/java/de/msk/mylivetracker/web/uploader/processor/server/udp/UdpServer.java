package de.msk.mylivetracker.web.uploader.processor.server.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo;
import de.msk.mylivetracker.service.statistics.IStatisticsService;
import de.msk.mylivetracker.web.uploader.processor.DataPacketCreator;
import de.msk.mylivetracker.web.uploader.processor.ProcessorType;
import de.msk.mylivetracker.web.uploader.processor.SupportedServices;

/**
 * UdpServer.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UdpServer extends Thread {

	private static final Log log = LogFactory.getLog(UdpServer.class);
	
	private IStatisticsService statisticsService;
	
	private UdpServerConfig udpServerConfig;
	private SupportedServices supportedServices;
	private DataPacketCreator dataPacketCreator;
		
	private DatagramSocket serverSocket = null;		
	private boolean shutdown;
				
	/**
	 * @name name
	 * @param listenPort
	 */
	public UdpServer(String name) {
		this.setName(name + " [" + this.getId() + "]");		
	}
	
	/**
	 * @return the shutdown
	 */
	private synchronized boolean isShutdown() {
		return shutdown;
	}

	/**
	 * @param shutdown the shutdown to set
	 */
	private synchronized void setShutdown(boolean shutdown) {
		this.shutdown = shutdown;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#start()
	 */
	@Override
	public synchronized void start() {
		boolean running = false;
		try {
			serverSocket = new DatagramSocket(udpServerConfig.getListenPort());
			setShutdown(false);
			super.start();
			log.info(getName() + 
				": server at port " + udpServerConfig.getListenPort() + " successfully started.");
			running = true;
		} catch (IOException e) {
			setShutdown(true);
			log.info(getName() + 
				": exception while starting server at port " + udpServerConfig.getListenPort() + ": " +
				e.getMessage());			
			e.printStackTrace();
			log.info(getName() + 
				": server start aborted.");
			running = false;
		}		
		
		this.statisticsService.logUploaderServerStatus(
			new UploaderServerStatusVo(
				getName(),
				udpServerConfig.getListenPort(), 
				ProcessorType.Udp.getProtocol(), 
				running ? UploaderServerStatusVo.Status.Running : UploaderServerStatusVo.Status.NotRunning, 
				new DateTime(), 
				false, 
				false, 
				-1, 
				-1, 
				-1));
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		setShutdown(true);
		this.serverSocket.close();			
		log.info(this.getName() + ": server at port " +
			this.serverSocket.getLocalPort() + " stopped (server-socket closed).");
		super.interrupt();
	}
	 
	protected void createAndRunProcessor(DatagramSocket socket, DatagramPacket packet) {
		UdpSocketProcessor processor = 
			new UdpSocketProcessor(
				this.getName(),		
				ProcessorType.Udp,
				this.supportedServices,
				this.dataPacketCreator,				
				socket, packet);							
		processor.process();		
	}	
		
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		byte[] buffer = new byte[65536];
		while (!this.isShutdown()) {
			try {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				serverSocket.receive(packet);	
				this.createAndRunProcessor(serverSocket, packet);				
			} catch (Exception e) {
				log.info(this.getName() + 
					": exception while running server: " + 
					e.getMessage());	
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * @return the udpServerConfig
	 */
	public UdpServerConfig getUdpServerConfig() {
		return udpServerConfig;
	}

	/**
	 * @param udpServerConfig the udpServerConfig to set
	 */
	public void setUdpServerConfig(UdpServerConfig udpServerConfig) {
		this.udpServerConfig = udpServerConfig;
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
	public void setDataPacketCreator(DataPacketCreator dataPacketCreator) {
		this.dataPacketCreator = dataPacketCreator;
	}

	/**
	 * @return the serverSocket
	 */
	public DatagramSocket getServerSocket() {
		return serverSocket;
	}

	/**
	 * @param serverSocket the serverSocket to set
	 */
	public void setServerSocket(DatagramSocket serverSocket) {
		this.serverSocket = serverSocket;
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
}
