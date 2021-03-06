package de.msk.mylivetracker.web.uploader.processor.server.udp;

/**
 * UdpServerConfig.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UdpServerConfig {
	private static int DEF_LISTEN_PORT = 41395;
	
	private int listenPort = DEF_LISTEN_PORT;

	/**
	 * @return the listenPort
	 */
	public int getListenPort() {
		return listenPort;
	}

	/**
	 * @param listenPort the listenPort to set
	 */
	public void setListenPort(int listenPort) {
		this.listenPort = listenPort;
	}		
}
