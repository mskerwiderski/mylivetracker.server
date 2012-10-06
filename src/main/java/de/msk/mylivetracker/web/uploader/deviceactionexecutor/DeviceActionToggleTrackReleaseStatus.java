package de.msk.mylivetracker.web.uploader.deviceactionexecutor;

import de.msk.mylivetracker.domain.DataReceivedVo;
import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.service.sms.ISmsService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.web.uploader.deviceactionexecutor.DeviceActionResult.Param;

/**
 * DeviceActionToggleTrackReleaseStatus.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class DeviceActionToggleTrackReleaseStatus extends AbstractDeviceAction {	
	
	public DeviceActionToggleTrackReleaseStatus(String name) {
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
		TrackVo track = trackService.getActiveTrackAsMin(sender.getSenderId());
		if (track != null) {
			if (!track.isReleased()) {
				trackService.publishTrack(track.getTrackId());
			} else {
				trackService.privatizeTrack(track.getTrackId());
			}
			res.append(Param.TrackId, track.getTrackId())
				.append(Param.TrackName, track.getName())
				.append(Param.TrackIsReleased, !track.isReleased());
		} else {
			res.append(Param.AddInfo, ADD_INFO_NO_ACTIVE_TRACK_FOUND);
		}
		
		return res;
	}
	
}
