package de.msk.mylivetracker.web.uploader.deviceactionexecutor;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.sms.ISmsService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionResult.Param;

/**
 * DeviceActionEmergencyActivated.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DeviceActionEmergencyActivated extends AbstractDeviceAction {

	/**
	 * @param name
	 */
	public DeviceActionEmergencyActivated(String name) {
		super(name);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.deviceactionexecutor.AbstractDeviceAction#executeInternal(de.msk.mylivetracker.domain.user.UserWithoutRoleVo, de.msk.mylivetracker.service.ITrackService, de.msk.mylivetracker.service.ISmsService, de.msk.mylivetracker.domain.DataReceivedVo)
	 */
	@Override
	public DeviceActionResult executeInternal(UserWithoutRoleVo user,
		ITrackService trackService, ISmsService smsService,
		DataReceivedVo dataReceived) {
		SenderVo sender = dataReceived.getSenderFromRequest().getSender();
		DeviceActionResult res = DeviceActionResult.create(this.getName())
			.append(Param.SenderId, sender.getSenderId());	
			
		if (dataReceived.hasValidData()) {
			trackService.storePositionAndMessage( 
				user, dataReceived);
		}
	
		return res;
	}
}
