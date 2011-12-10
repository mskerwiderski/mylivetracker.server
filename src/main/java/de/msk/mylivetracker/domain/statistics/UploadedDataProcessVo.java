package de.msk.mylivetracker.domain.statistics;

import org.apache.commons.lang.StringUtils;

/**
 * UploadedDataProcessVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class UploadedDataProcessVo extends AbstractStatisticVo {
	
	private static final long serialVersionUID = -8389085866508851165L;
	
	private String protocol;
	private String remoteIpAddress;
	private String remoteHost;	
	private String remotePort;
	private String server;
	private String serverPort;	
	private String processor;
	private String interpreter;
	private String interpreterVersion;
	private String senderId;
	private String itemNumber;
	private String dataStr; 
	private String addInfo;
	private String responseStr;
	private String connectionStatus;	
	
	public UploadedDataProcessVo(String protocol,
			String remoteIpAddress, String remoteHost, String remotePort,
			String server, String serverPort,
			String processor, String interpreter, String interpreterVersion,
			String senderId, String itemNumber, String dataStr, String addInfo,
			String responseStr, String connectionStatus) {
		this.protocol = 
			(StringUtils.isEmpty(protocol) ? UNKNOWN : protocol);
		this.remoteIpAddress = 
			(StringUtils.isEmpty(remoteIpAddress) ? UNKNOWN : remoteIpAddress);
		this.remoteHost = 
			(StringUtils.isEmpty(remoteHost) ? UNKNOWN : remoteHost);
		this.remotePort =
			(StringUtils.isEmpty(remotePort) ? UNKNOWN : remotePort);
		this.server =
			(StringUtils.isEmpty(server) ? UNKNOWN : server);
		this.serverPort =
			(StringUtils.isEmpty(serverPort) ? UNKNOWN : serverPort);
		this.processor = 
			(StringUtils.isEmpty(processor) ? UNKNOWN : processor);		
		this.interpreter = 
			(StringUtils.isEmpty(interpreter) ? UNKNOWN : interpreter);
		this.interpreterVersion = 
			(StringUtils.isEmpty(interpreterVersion) ? UNKNOWN : interpreterVersion);
		this.senderId = 
			(StringUtils.isEmpty(senderId) ? UNKNOWN : senderId);
		this.itemNumber = 
			(StringUtils.isEmpty(itemNumber) ? UNKNOWN : itemNumber);
		this.dataStr =
			(StringUtils.isEmpty(dataStr) ? EMPTY_STRING : dataStr);
		this.addInfo = 
			(StringUtils.isEmpty(addInfo) ? EMPTY : addInfo);
		this.responseStr = 
			(StringUtils.isEmpty(responseStr) ? EMPTY : responseStr);
		this.connectionStatus =
			(StringUtils.isEmpty(connectionStatus) ? UNKNOWN : connectionStatus);		
	}
	
	/**
	 * 
	 */
	public UploadedDataProcessVo() {
	}

	/**
	 * @return the protocol
	 */
	public String getProtocol() {
		return protocol;
	}

	/**
	 * @param protocol the protocol to set
	 */
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	/**
	 * @return the remoteIpAddress
	 */
	public String getRemoteIpAddress() {
		return remoteIpAddress;
	}

	/**
	 * @param remoteIpAddress the remoteIpAddress to set
	 */
	public void setRemoteIpAddress(String remoteIpAddress) {
		this.remoteIpAddress = remoteIpAddress;
	}

	/**
	 * @return the remoteHost
	 */
	public String getRemoteHost() {
		return remoteHost;
	}

	/**
	 * @param remoteHost the remoteHost to set
	 */
	public void setRemoteHost(String remoteHost) {
		this.remoteHost = remoteHost;
	}

	/**
	 * @return the remotePort
	 */
	public String getRemotePort() {
		return remotePort;
	}

	/**
	 * @param remotePort the remotePort to set
	 */
	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}

	/**
	 * @return the server
	 */
	public String getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * @return the serverPort
	 */
	public String getServerPort() {
		return serverPort;
	}

	/**
	 * @param serverPort the serverPort to set
	 */
	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * @return the processor
	 */
	public String getProcessor() {
		return processor;
	}

	/**
	 * @param processor the processor to set
	 */
	public void setProcessor(String processor) {
		this.processor = processor;
	}
	
	/**
	 * @return the interpreter
	 */
	public String getInterpreter() {
		return interpreter;
	}

	/**
	 * @param interpreter the interpreter to set
	 */
	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}

	/**
	 * @return the interpreterVersion
	 */
	public String getInterpreterVersion() {
		return interpreterVersion;
	}

	/**
	 * @param interpreterVersion the interpreterVersion to set
	 */
	public void setInterpreterVersion(String interpreterVersion) {
		this.interpreterVersion = interpreterVersion;
	}

	/**
	 * @return the senderId
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * @param senderId the senderId to set
	 */
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	
	/**
	 * @return the itemNumber
	 */
	public String getItemNumber() {
		return itemNumber;
	}

	/**
	 * @param itemNumber the itemNumber to set
	 */
	public void setItemNumber(String itemNumber) {
		this.itemNumber = itemNumber;
	}

	/**
	 * @return the dataStr
	 */
	public String getDataStr() {
		return dataStr;
	}

	/**
	 * @param dataStr the dataStr to set
	 */
	public void setDataStr(String dataStr) {
		this.dataStr = dataStr;
	}

	/**
	 * @return the connectionStatus
	 */
	public String getConnectionStatus() {
		return connectionStatus;
	}

	/**
	 * @param connectionStatus the connectionStatus to set
	 */
	public void setConnectionStatus(String connectionStatus) {
		this.connectionStatus = connectionStatus;
	}

	/**
	 * @return the addInfo
	 */
	public String getAddInfo() {
		return addInfo;
	}

	/**
	 * @param addInfo the addInfo to set
	 */
	public void setAddInfo(String addInfo) {
		this.addInfo = addInfo;
	}

	/**
	 * @return the responseStr
	 */
	public String getResponseStr() {
		return responseStr;
	}

	/**
	 * @param responseStr the responseStr to set
	 */
	public void setResponseStr(String responseStr) {
		this.responseStr = responseStr;
	}	
}
