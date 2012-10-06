package de.msk.mylivetracker.web.uploader.processor;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.web.uploader.processor.interpreter.IDataInterpreter;

/**
 * DataPacket.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DataPacket implements Serializable {	
	
	private static final long serialVersionUID = 6303439233411096228L;
	
	private IDataInterpreter dataInterpreter = null;	
	private DataReceivedVo dataReceived = null;		
	private Exception exception = null;
	private String checkMsg = null;
	private String responseStr = null;
	private String detectedSenderType = null;
	
	public boolean isAuthorized() {
		return this.dataReceived.getSenderFromRequest().isAuthorized();
	}
	
	public void authorize(ISenderService senderService) {
		this.dataReceived.getSenderFromRequest().authorize(senderService);
	}
	
	/**
	 * @param dataInterpreter
	 */
	public DataPacket(
		IDataInterpreter dataInterpreter) {
		this.dataInterpreter = dataInterpreter;
	}
	
	/**
	 * @return the dataInterpreter
	 */
	public IDataInterpreter getDataInterpreter() {
		return dataInterpreter;
	}

	public boolean hasException() {
		return this.exception != null;
	}	
	public boolean hasCheckMsg() {
		return !StringUtils.isEmpty(this.checkMsg);
	}
	/**
	 * @return the exception
	 */
	public Exception getException() {
		return exception;
	}
	/**
	 * @param exception the exception to set
	 */
	public void setException(Exception exception) {
		this.exception = exception;
	}
	/**
	 * @return the checkMsg
	 */
	public String getCheckMsg() {
		return checkMsg;
	}
	/**
	 * @param checkMsg the checkMsg to set
	 */
	public void setCheckMsg(String checkMsg) {
		this.checkMsg = checkMsg;
	}
	/**
	 * @return the dataReceived
	 */
	public DataReceivedVo getDataReceived() {
		return dataReceived;
	}
	/**
	 * @param dataReceived the dataReceived to set
	 */
	public void setDataReceived(DataReceivedVo dataReceived) {
		this.dataReceived = dataReceived;
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

	/**
	 * @return the detectedSenderType
	 */
	public String getDetectedSenderType() {
		return detectedSenderType;
	}

	/**
	 * @param detectedSenderType the detectedSenderType to set
	 */
	public void setDetectedSenderType(String detectedSenderType) {
		this.detectedSenderType = detectedSenderType;
	}					
}
