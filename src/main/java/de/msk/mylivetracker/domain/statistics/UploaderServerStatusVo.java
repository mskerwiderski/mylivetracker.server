package de.msk.mylivetracker.domain.statistics;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * UploadedDataProcessVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-09-02
 * 
 */
public class UploaderServerStatusVo extends AbstractStatisticVo {

	private static final long serialVersionUID = -6457019107436659625L;
	
	public enum Status {
		Running, NotRunning
	}
	
	private String serverName;
	private int serverPort;
	private String serverProtocol;
	private Status status;
	private DateTime started;	
	private boolean optionDoReadLine;
	private boolean optionMultipleRecordsPerReception;
	private int optionSocketReadTimeoutInMSecs;
	private int optionMaxDataStrLengthInBytes;
	private int optionConnectionTimeoutInMSecs;
	public UploaderServerStatusVo(String serverName, int serverPort,
		String serverProtocol, Status status, DateTime started, boolean optionDoReadLine,
		boolean optionMultipleRecordsPerReception,
		int optionSocketReadTimeoutInMSecs,
		int optionMaxDataStrLengthInBytes,
		int optionConnectionTimeoutInMSecs) {
		this.serverName = serverName;
		this.serverPort = serverPort;
		this.serverProtocol = serverProtocol;
		this.status = status;
		this.started = started;
		this.optionDoReadLine = optionDoReadLine;
		this.optionMultipleRecordsPerReception = optionMultipleRecordsPerReception;
		this.optionSocketReadTimeoutInMSecs = optionSocketReadTimeoutInMSecs;
		this.optionMaxDataStrLengthInBytes = optionMaxDataStrLengthInBytes;
		this.optionConnectionTimeoutInMSecs = optionConnectionTimeoutInMSecs;
	}
	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	/**
	 * @return the serverPort
	 */
	public int getServerPort() {
		return serverPort;
	}
	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	/**
	 * @return the serverProtocol
	 */
	public String getServerProtocol() {
		return serverProtocol;
	}
	/**
	 * @param serverProtocol the serverProtocol to set
	 */
	public void setServerProtocol(String serverProtocol) {
		this.serverProtocol = serverProtocol;
	}
	/**
	 * @return the status
	 */
	public Status getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(Status status) {
		this.status = status;
	}
	/**
	 * @return the started
	 */
	public DateTime getStarted() {
		return started;
	}
	/**
	 * @param started the started to set
	 */
	public void setStarted(DateTime started) {
		this.started = started;
	}
	/**
	 * @return the optionDoReadLine
	 */
	public boolean isOptionDoReadLine() {
		return optionDoReadLine;
	}
	/**
	 * @param optionDoReadLine the optionDoReadLine to set
	 */
	public void setOptionDoReadLine(boolean optionDoReadLine) {
		this.optionDoReadLine = optionDoReadLine;
	}
	/**
	 * @return the optionMultipleRecordsPerReception
	 */
	public boolean isOptionMultipleRecordsPerReception() {
		return optionMultipleRecordsPerReception;
	}
	/**
	 * @param optionMultipleRecordsPerReception the optionMultipleRecordsPerReception to set
	 */
	public void setOptionMultipleRecordsPerReception(
			boolean optionMultipleRecordsPerReception) {
		this.optionMultipleRecordsPerReception = optionMultipleRecordsPerReception;
	}
	/**
	 * @return the optionSocketReadTimeoutInMSecs
	 */
	public int getOptionSocketReadTimeoutInMSecs() {
		return optionSocketReadTimeoutInMSecs;
	}
	/**
	 * @param optionSocketReadTimeoutInMSecs the optionSocketReadTimeoutInMSecs to set
	 */
	public void setOptionSocketReadTimeoutInMSecs(
		int optionSocketReadTimeoutInMSecs) {
		this.optionSocketReadTimeoutInMSecs = optionSocketReadTimeoutInMSecs;
	}
	/**
	 * @return the optionMaxDataStrLengthInBytes
	 */
	public int getOptionMaxDataStrLengthInBytes() {
		return optionMaxDataStrLengthInBytes;
	}
	/**
	 * @param optionMaxDataStrLengthInBytes the optionMaxDataStrLengthInBytes to set
	 */
	public void setOptionMaxDataStrLengthInBytes(int optionMaxDataStrLengthInBytes) {
		this.optionMaxDataStrLengthInBytes = optionMaxDataStrLengthInBytes;
	}
	/**
	 * @return the optionConnectionTimeoutInMSecs
	 */
	public int getOptionConnectionTimeoutInMSecs() {
		return optionConnectionTimeoutInMSecs;
	}
	/**
	 * @param optionConnectionTimeoutInMSecs the optionConnectionTimeoutInMSecs to set
	 */
	public void setOptionConnectionTimeoutInMSecs(
		int optionConnectionTimeoutInMSecs) {
		this.optionConnectionTimeoutInMSecs = optionConnectionTimeoutInMSecs;
	}	
}
