package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;

/**
 * ActionCreateTrack.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ActionCreateTrack extends AbstractActionWithoutRedirect {

	@Override
	public void preExecuteCheck(TracksOverviewCmd cmd, Services services)
		throws ActionExecutionException {
		if (!services.getSenderService().senderExists(cmd.getSelectedSenderForCreateTrack())) {
			throw new ActionExecutionException(
				"track cannot be created, because of an unknown sender.");
		}
	}

	@Override
	public void executeAux(Services services, UserWithRoleVo user,
		TracksOverviewCmd cmd) throws ActionExecutionException {
		SenderVo sender = services.getSenderService().getSender(
			cmd.getSelectedSenderForCreateTrack());
		services.getTrackService().createTrack(sender, 
			user.getOptions().getDefTrackName(), 
			user.getOptions().getDefTrackReleaseStatus());
	}
}
