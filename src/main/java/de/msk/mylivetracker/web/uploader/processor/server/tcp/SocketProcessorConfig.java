package de.msk.mylivetracker.web.uploader.processor.server.tcp;

/**
 * SocketProcessorConfig.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SocketProcessorConfig {
	
	private static boolean DEF_DO_READ_LINE = false;
	private static boolean DEF_MULTIPLE_RECORDS_PER_RECEPTION = false;
	private static int DEF_SOCKET_READ_TIMEOUT_IN_MSECS = 500;
	private static int DEF_MAX_DATA_STRING_LENGTH_IN_BYTES = 1024;
	private static int DEF_CONNECTION_TIMEOUT_IN_MSECS = 1000 * 60 * 30;
	
	private boolean doReadLine = DEF_DO_READ_LINE;
	private boolean supportsMultipleRecordsPerReception = DEF_MULTIPLE_RECORDS_PER_RECEPTION;
	private int socketReadTimeoutInMSecs = DEF_SOCKET_READ_TIMEOUT_IN_MSECS;
	private int maxDataStringLengthInBytes = DEF_MAX_DATA_STRING_LENGTH_IN_BYTES;
	private int connectionTimeoutInMSecs = DEF_CONNECTION_TIMEOUT_IN_MSECS;	
	
	/**
	 * @return the doReadLine
	 */
	public boolean isDoReadLine() {
		return doReadLine;
	}
	/**
	 * @param doReadLine the doReadLine to set
	 */
	public void setDoReadLine(boolean doReadLine) {
		this.doReadLine = doReadLine;
	}
	/**
	 * @return the supportsMultipleRecordsPerReception
	 */
	public boolean isSupportsMultipleRecordsPerReception() {
		return supportsMultipleRecordsPerReception;
	}
	/**
	 * @param supportsMultipleRecordsPerReception the supportsMultipleRecordsPerReception to set
	 */
	public void setSupportsMultipleRecordsPerReception(
			boolean supportsMultipleRecordsPerReception) {
		this.supportsMultipleRecordsPerReception = supportsMultipleRecordsPerReception;
	}
	/**
	 * @return the socketReadTimeoutInMSecs
	 */
	public int getSocketReadTimeoutInMSecs() {
		return socketReadTimeoutInMSecs;
	}
	/**
	 * @param socketReadTimeoutInMSecs the socketReadTimeoutInMSecs to set
	 */
	public void setSocketReadTimeoutInMSecs(int socketReadTimeoutInMSecs) {
		this.socketReadTimeoutInMSecs = socketReadTimeoutInMSecs;
	}
	public int getMaxDataStringLengthInBytes() {
		return maxDataStringLengthInBytes;
	}
	public void setMaxDataStringLengthInBytes(int maxDataStringLengthInBytes) {
		this.maxDataStringLengthInBytes = maxDataStringLengthInBytes;
	}
	/**
	 * @return the connectionTimeoutInMSecs
	 */
	public int getConnectionTimeoutInMSecs() {
		return connectionTimeoutInMSecs;
	}
	/**
	 * @param connectionTimeoutInMSecs the connectionTimeoutInMSecs to set
	 */
	public void setConnectionTimeoutInMSecs(int connectionTimeoutInMSecs) {
		this.connectionTimeoutInMSecs = connectionTimeoutInMSecs;
	}	
}
