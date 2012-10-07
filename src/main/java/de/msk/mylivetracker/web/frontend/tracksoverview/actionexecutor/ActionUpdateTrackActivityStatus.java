package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;

/**
 * ActionUpdateTrackActivityStatus.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ActionUpdateTrackActivityStatus extends AbstractActionEditTrack {
	
	@Override
	public void executeAux(Services services, UserWithRoleVo user,
		TracksOverviewCmd cmd) throws ActionExecutionException {
		String trackId = cmd.getSelectedTrackId();
		TrackVo track = services.getTrackService().getTrackAsMin(trackId);
		if (cmd.getSelectedTrackActivityStatus() == Boolean.TRUE) {
			if (!TrackVo.canBeActivated(user.getRole(), track, 
				services.getSenderService())) {
				throw new ActionExecutionException(
					"track with trackid '" + trackId + 
					"' cannot be activated because sender doesn't exist.");
			} else {
				services.getTrackService().openTrack(trackId);
			}
		} else {
			services.getTrackService().closeTrack(trackId);
		}
	}

}
