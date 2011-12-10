package de.msk.mylivetracker.web.uploader.processor;

/**
 * ProcessorType.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public enum ProcessorType {
	Udp("UdpSocketProcessor", "UDP"), 
	Tcp("TcpSocketProcessor", "TCP"), 
	Http("HttpServletProcessor", "HTTP");
	
	private String name;
	private String protocol;
	
	private ProcessorType(String name, String protocol) {
		this.name = name;
		this.protocol = protocol;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	@Override
	public String toString() {
		return this.name + " [" + this.protocol + "]";
	}	
}
