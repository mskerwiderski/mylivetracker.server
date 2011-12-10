package de.msk.mylivetracker.web.uploader.processor.server.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import de.msk.mylivetracker.web.uploader.processor.DataPacketCreator;
import de.msk.mylivetracker.web.uploader.processor.PreProcessResultDsc;
import de.msk.mylivetracker.web.uploader.processor.ProcessorType;
import de.msk.mylivetracker.web.uploader.processor.RemoteInfo;
import de.msk.mylivetracker.web.uploader.processor.ServerInfo;
import de.msk.mylivetracker.web.uploader.processor.SupportedServices;
import de.msk.mylivetracker.web.uploader.processor.UploadProcessor;

/**
 * UdpSocketProcessor.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UdpSocketProcessor extends UploadProcessor {
	
	private DatagramSocket socket;
	private DatagramPacket packet;
	
	/**
	 * @param serverName
	 * @param processorType
	 * @param supportedServices
	 * @param dataPacketCreator
	 * @param dataInterpreter
	 * @param socketProcessorConfig
	 * @param socket
	 */
	public UdpSocketProcessor(String serverName, 
		ProcessorType processorType,		
		SupportedServices supportedServices,
		DataPacketCreator dataPacketCreator,
		DatagramSocket socket,
		DatagramPacket packet) {
		super(serverName, 
			processorType, 			
			supportedServices, 
			dataPacketCreator,
			false);
		this.socket = socket;
		this.packet = packet;		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.UploadProcessor#getRemoteInfo()
	 */
	@Override
	public RemoteInfo getRemoteInfo() {		
		return new RemoteInfo(
			(this.packet.getAddress() != null) ? 
				this.packet.getAddress().getHostAddress() : RemoteInfo.UNKNOWN,
			(this.packet.getAddress() != null) ? 
				this.packet.getAddress().getHostName() : RemoteInfo.UNKNOWN,
			String.valueOf(this.packet.getPort()));
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.tcpserver.UploadProcessor#getServerInfo()
	 */
	@Override
	public ServerInfo getServerInfo() {
		return new ServerInfo(
			this.getServerName(),
			String.valueOf(this.socket.getLocalPort())); 		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.UploadProcessor#postProcess(de.msk.mylivetracker.web.uploader.processor.UploadProcessor.ProcessResult, boolean)
	 */
	@Override
	public void postProcess(
			de.msk.mylivetracker.web.uploader.processor.UploadProcessor.ProcessResult result,
			boolean cutConnection) {
		// nothing to do.
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.UploadProcessor#preProcess()
	 */
	@Override
	public PreProcessResultDsc preProcess() {
		return new PreProcessResultDsc(new DatagramPacketCtx(packet));	
	}		
	
	
	
}
