package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.Services;
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
	
	@Override
	public void executeAux(Services services, UserWithRoleVo user,
		TracksOverviewCmd cmd) throws ActionExecutionException {
		String trackId = cmd.getSelectedTrackId();
		if (cmd.getSelectedTrackReleaseStatus() == Boolean.TRUE) {
			services.getTrackService().publishTrack(trackId);
		} else {
			services.getTrackService().privatizeTrack(trackId);
		}
	}

}
