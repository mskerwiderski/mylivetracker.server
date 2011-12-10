package de.msk.mylivetracker.web.uploader.deviceactionexecutor;

/**
 * DeviceActionExecutorAndMessage.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DeviceActionExecutorAndMessage {
	private DeviceActionExecutor deviceActionExecutor;
	private String message;
	public DeviceActionExecutorAndMessage(
		DeviceActionExecutor deviceActionExecutor, String message) {
		this.deviceActionExecutor = deviceActionExecutor;
		this.message = message;
	}		
	public DeviceActionExecutor getDeviceActionExecutor() {
		return deviceActionExecutor;
	}
	public String getMessage() {
		return message;
	}
	@Override
	public String toString() {
		return "DeviceActionExecutorAndMessage [deviceActionExecutor="
				+ deviceActionExecutor + ", message=" + message + "]";
	}	
}
