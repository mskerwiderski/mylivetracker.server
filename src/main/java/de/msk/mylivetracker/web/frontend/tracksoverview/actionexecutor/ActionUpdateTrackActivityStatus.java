package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.sender.ISenderService;
import de.msk.mylivetracker.service.track.ITrackService;
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
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor.AbstractActionWithoutRedirect#executeAux(de.msk.mylivetracker.domain.user.UserWithRoleVo, de.msk.mylivetracker.service.ITrackService, de.msk.mylivetracker.service.ISenderService, de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd)
	 */
	@Override
	public void executeAux(UserWithRoleVo user, ITrackService trackService,
		ISenderService senderService, TracksOverviewCmd cmd)
		throws ActionExecutionException {
		String trackId = cmd.getSelectedTrackId();
		TrackVo track = trackService.getTrackAsMin(trackId);
		if (cmd.getSelectedTrackActivityStatus() == Boolean.TRUE) {
			if (!TrackVo.canBeActivated(user.getRole(), track, senderService)) {
				throw new ActionExecutionException(
					"track with trackid '" + trackId + 
					"' cannot be activated because sender doesn't exist.");
			} else {
				trackService.openTrack(trackId);
			}
		} else {
			trackService.closeTrack(trackId);
		}
	}

}
