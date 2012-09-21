package de.msk.mylivetracker.web.uploader.processor.server.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo;
import de.msk.mylivetracker.service.statistics.IStatisticsService;
import de.msk.mylivetracker.web.uploader.processor.DataPacketCreator;
import de.msk.mylivetracker.web.uploader.processor.ProcessorType;
import de.msk.mylivetracker.web.uploader.processor.SupportedServices;

/**
 * TcpServer.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TcpServer extends Thread {

 	private static final Log log = LogFactory.getLog(TcpServer.class);
	
 	private IStatisticsService statisticsService;
 	
	private TcpServerConfig tcpServerConfig;
	private SocketProcessorConfig socketProcessorConfig;	
	private SupportedServices supportedServices;
	private ThreadPoolTaskExecutor taskExecutorForProcessors;
	private DataPacketCreator dataPacketCreator;
		
	private ServerSocket serverSocket = null;		
	private boolean shutdown;
				
	/**
	 * @name name
	 * @param listenPort
	 */
	public TcpServer(String name) {
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
			serverSocket = new ServerSocket(tcpServerConfig.getListenPort());
			setShutdown(false);
			super.start();			
			log.info(getName() + 
				": server at port " + tcpServerConfig.getListenPort() + " successfully started [" +
				"socketReadTimeoutInMSecs=" + socketProcessorConfig.getSocketReadTimeoutInMSecs() + "," +
				"maxDataStringLengthInBytes=" + socketProcessorConfig.getMaxDataStringLengthInBytes() + "," +
				"connectionTimeoutInMSecs=" + socketProcessorConfig.getConnectionTimeoutInMSecs() + "].");
			running = true;
		} catch (IOException e) {
			setShutdown(true);
			log.info(getName() + 
				": exception while starting server at port " + tcpServerConfig.getListenPort() + ": " +
				e.getMessage());			
			e.printStackTrace();
			log.info(getName() + 
				": server start aborted.");
			running = false;
		}
		
		this.statisticsService.logUploaderServerStatus(
			new UploaderServerStatusVo(
				getName(),
				tcpServerConfig.getListenPort(), 
				ProcessorType.Tcp.getProtocol(), 
				running ? UploaderServerStatusVo.Status.Running : UploaderServerStatusVo.Status.NotRunning, 
				new DateTime(), 
				socketProcessorConfig.isDoReadLine(), 
				socketProcessorConfig.isSupportsMultipleRecordsPerReception(), 
				socketProcessorConfig.getSocketReadTimeoutInMSecs(), 
				socketProcessorConfig.getMaxDataStringLengthInBytes(), 
				socketProcessorConfig.getConnectionTimeoutInMSecs()));
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#interrupt()
	 */
	@Override
	public void interrupt() {
		try {			
			setShutdown(true);
			this.serverSocket.close();			
			log.info(this.getName() + ": server at port " +
				this.serverSocket.getLocalPort() + " stopped (server-socket closed).");
		} catch (IOException e) {
			log.info(this.getName() + 
				": exception while stopping server at port " + 
				this.serverSocket.getLocalPort() + ": " + e.getMessage());	
			e.printStackTrace();
		}
		super.interrupt();
	}
	 
	protected TcpSocketProcessor createAndRunProcessor(Socket socket) {
		TcpSocketProcessor processor =
			new TcpSocketProcessor(				
				this.getName(),		
				ProcessorType.Tcp,
				this.supportedServices,
				this.dataPacketCreator,
				this.socketProcessorConfig,
				socket);				
		this.taskExecutorForProcessors.execute(processor);		
		return processor;
	}	
		
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (!this.isShutdown()) {
			try {
				Socket socket = serverSocket.accept();	
				if (socketProcessorConfig.getMaxDataStringLengthInBytes() > 0) {
					socket.setReceiveBufferSize(socketProcessorConfig.getMaxDataStringLengthInBytes());
				}
				socket.setKeepAlive(true);
				log.info(this.getName() + 
					": connection to a client established, inetAddess: " + 
					socket.getInetAddress().toString());
				TcpSocketProcessor processor = this.createAndRunProcessor(socket);
				if (processor == null) {
					throw new RuntimeException(this.getName() + 
						": createAndRunProcessor failed.");
				} else {					
					log.info(this.getName() + 
						": processor " + processor.getName() + 
						"started.");
				}
			} catch (Exception e) {
				log.info(this.getName() + 
					": exception while running server: " + 
					e.getMessage());	
				e.printStackTrace();
			}
		}		
	}

	/**
	 * @return the tcpServerConfig
	 */
	public TcpServerConfig getTcpServerConfig() {
		return tcpServerConfig;
	}

	/**
	 * @param tcpServerConfig the tcpServerConfig to set
	 */
	public void setTcpServerConfig(TcpServerConfig tcpServerConfig) {
		this.tcpServerConfig = tcpServerConfig;
	}

	/**
	 * @return the socketProcessorConfig
	 */
	public SocketProcessorConfig getSocketProcessorConfig() {
		return socketProcessorConfig;
	}

	/**
	 * @param socketProcessorConfig the socketProcessorConfig to set
	 */
	public void setSocketProcessorConfig(SocketProcessorConfig socketProcessorConfig) {
		this.socketProcessorConfig = socketProcessorConfig;
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
	 * @return the taskExecutorForProcessors
	 */
	public ThreadPoolTaskExecutor getTaskExecutorForProcessors() {
		return taskExecutorForProcessors;
	}

	/**
	 * @param taskExecutorForProcessors the taskExecutorForProcessors to set
	 */
	public void setTaskExecutorForProcessors(
			ThreadPoolTaskExecutor taskExecutorForProcessors) {
		this.taskExecutorForProcessors = taskExecutorForProcessors;
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
	public ServerSocket getServerSocket() {
		return serverSocket;
	}

	/**
	 * @param serverSocket the serverSocket to set
	 */
	public void setServerSocket(ServerSocket serverSocket) {
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