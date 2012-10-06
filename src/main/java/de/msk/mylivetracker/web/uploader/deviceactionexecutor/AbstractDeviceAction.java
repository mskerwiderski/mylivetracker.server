package de.msk.mylivetracker.web.uploader.deviceactionexecutor;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.sms.ISmsService;
import de.msk.mylivetracker.service.track.ITrackService;

/**
 * AbstractDeviceAction.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractDeviceAction {	
	
	public static final String ADD_INFO_NO_ACTIVE_TRACK_FOUND = "no active track found.";
	public static final String ADD_INFO_NOT_YET_IMPLEMENTED = "not yet implemented.";
	
	private String name;
	
	public AbstractDeviceAction(String name) {
		this.name = name;
	}
	
	public DeviceActionResult execute(
		UserWithoutRoleVo user,
		ITrackService trackService,
		ISmsService smsService,
		DataReceivedVo dataReceived) {
		DeviceActionResult res;
		try {
			res = this.executeInternal(
				user,
				trackService,
				smsService,
				dataReceived);
		} catch (Exception e) {
			res = DeviceActionResult.create(this.getName(), e);
		}
		return res;
	}

	public abstract DeviceActionResult executeInternal(
		UserWithoutRoleVo user,		
		ITrackService trackService, 
		ISmsService smsService,
		DataReceivedVo dataReceived);

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}	
}
