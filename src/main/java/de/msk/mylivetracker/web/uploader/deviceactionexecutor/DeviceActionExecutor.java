package de.msk.mylivetracker.web.uploader.deviceactionexecutor;

import java.util.HashMap;
import java.util.Map;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.sms.ISmsService;
import de.msk.mylivetracker.service.track.ITrackService;

/**
 * DeviceActionExecutor.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public enum DeviceActionExecutor {
	CreateTrack("1", new DeviceActionCreateTrack("CreateTrack")),
	CloseTrack("2", new DeviceActionCloseTrack("CloseTrack")),
	GetTrackInfo("3", new DeviceActionGetTrackInfo("GetTrackInfo")),
	ToggleTrackReleaseStatus("4", new DeviceActionToggleTrackReleaseStatus("ToggleTrackReleaseStatus")),
	ResetTrack("5", new DeviceActionResetTrack("ResetTrack")),
	EmergencyActivated("7", new DeviceActionEmergencyActivated("EmergencyActivated")),
	EmergencyDeactivated("9", new DeviceActionEmergencyDeactivated("EmergencyDeactivated")),
	StorePositionMessage("0", new DeviceActionStorePositionMessage("StorePositionMessage"));
	
	private String code;
	private AbstractDeviceAction deviceAction;
	private static Map<String, DeviceActionExecutor> deviceActionExecutorMap;
	
	private DeviceActionExecutor(String code, AbstractDeviceAction deviceAction) {
		this.code = code;
		this.deviceAction = deviceAction;			
	}
	static {
		deviceActionExecutorMap =
			new HashMap<String, DeviceActionExecutor>();
		for (DeviceActionExecutor deviceActionExecutor : DeviceActionExecutor.values()) {
			deviceActionExecutorMap.put(deviceActionExecutor.code, deviceActionExecutor);
		}
	}
	
	public DeviceActionResult execute(
		UserWithoutRoleVo user,	
		ITrackService trackService,
		ISmsService smsService,
		DataReceivedVo dataReceived) {		
		return this.deviceAction.execute(user,
			trackService, smsService,
			dataReceived);
	}
	
	public static DeviceActionExecutor getDeviceActionExecutor(String deviceActionId) {
		return deviceActionExecutorMap.get(deviceActionId);		
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}	
}
