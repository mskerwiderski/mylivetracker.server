package de.msk.mylivetracker.web.uploader.processor;

/**
 * PreProcessResultDsc.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class PreProcessResultDsc {
	public enum ConnectionStatus {
		Running, Lost, Timeout, TcpError;
		
		public boolean isOk() {
			return this.equals(ConnectionStatus.Running);
		}
	}
	private IDataCtx[] data;
	private ConnectionStatus connectionStatus;
	
	public PreProcessResultDsc(ConnectionStatus connectionStatus) {
		this.connectionStatus = connectionStatus;
	}	
	public PreProcessResultDsc(IDataCtx...data) {
		this.data = data;
		this.connectionStatus = ConnectionStatus.Running;
	}
	public PreProcessResultDsc(ConnectionStatus connectionStatus, IDataCtx...data) {
		this.data = data;
		this.connectionStatus = connectionStatus;
	}
	public boolean isEmpty() {
		return (data == null) || (data.length == 0);
	}
	/**
	 * @return the data
	 */
	public IDataCtx[] getData() {
		return data;
	}
	/**
	 * @return the status
	 */
	public ConnectionStatus getConnectionStatus() {
		return connectionStatus;
	}		
}
