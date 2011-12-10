package de.msk.mylivetracker.web.uploader.processor;

import java.io.Serializable;

/**
 * RemoteInfo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public final class RemoteInfo implements Serializable {
	private static final long serialVersionUID = -5357556821778811383L;
	
	public static final String UNKNOWN = "<unknown>";
	private String remoteIpAddress = UNKNOWN;
	private String remoteHost = UNKNOWN;
	private String remotePort = UNKNOWN;
	
	public RemoteInfo() {
	}
	
	public RemoteInfo(String remoteIpAddress, 
		String remoteHost, String remotePort) {
		this.remoteIpAddress = remoteIpAddress;
		this.remoteHost = remoteHost;
		this.remotePort = remotePort;
	}
	/**
	 * @return the remoteIpAddress
	 */
	public String getRemoteIpAddress() {
		return remoteIpAddress;
	}
	/**
	 * @return the remoteHost
	 */
	public String getRemoteHost() {
		return remoteHost;
	}
	/**
	 * @return the remotePort
	 */
	public String getRemotePort() {
		return remotePort;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.remoteHost + " [" +
			this.remoteIpAddress + ":" +
			this.remotePort + "]";
	}	
}
