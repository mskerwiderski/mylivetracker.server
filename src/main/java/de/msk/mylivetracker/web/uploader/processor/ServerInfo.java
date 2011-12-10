package de.msk.mylivetracker.web.uploader.processor;

import java.io.Serializable;

/**
 * ServerInfo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ServerInfo implements Serializable {
	private static final long serialVersionUID = 8721684162550822276L;

	private String serverName;
	private String serverPort;
	public ServerInfo(String serverName, String serverPort) {
		this.serverName = serverName;
		this.serverPort = serverPort;
	}
	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * @return the serverPort
	 */
	public String getServerPort() {
		return serverPort;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ServerInfo [serverName=" + serverName + ", serverPort="
			+ serverPort + "]";
	}			
}
