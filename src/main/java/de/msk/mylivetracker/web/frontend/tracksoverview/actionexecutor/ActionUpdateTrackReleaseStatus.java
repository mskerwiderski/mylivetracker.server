package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.service.track.ITrackService;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;

/**
 * ActionUpdateTrackReleaseStatus.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ActionUpdateTrackReleaseStatus extends AbstractActionEditTrack {
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor.AbstractActionWithoutRedirect#executeAux(de.msk.mylivetracker.domain.user.UserWithRoleVo, de.msk.mylivetracker.service.ITrackService, de.msk.mylivetracker.service.ISenderService, de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd)
	 */
	@Override
	public void executeAux(UserWithRoleVo user, ITrackService trackService,
		ISenderService senderService, TracksOverviewCmd cmd) {
		String trackId = cmd.getSelectedTrackId();
		if (cmd.getSelectedTrackReleaseStatus() == Boolean.TRUE) {
			trackService.publishTrack(trackId);
		} else {
			trackService.privatizeTrack(trackId);
		}
	}

}
