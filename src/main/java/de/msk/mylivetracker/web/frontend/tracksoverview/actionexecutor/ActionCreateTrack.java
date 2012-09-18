package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.ISenderService;
import de.msk.mylivetracker.service.ITrackService;
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

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.tracksoverview.actionexecutor.IAction#preExecuteCheck(de.msk.mylivetracker.web.tracksoverview.TracksOverviewCmd)
	 */
	public void preExecuteCheck(TracksOverviewCmd cmd)
		throws ActionExecutionException {
		if (cmd.getSenderEntries().get(cmd.getSelectedSenderEntryIdx()) == null) {
			throw new ActionExecutionException(
				"track cannot be created, because of an unknown sender.");
		}
	}
		
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor.AbstractActionWithoutRedirect#executeAux(de.msk.mylivetracker.domain.user.UserWithRoleVo, de.msk.mylivetracker.service.ITrackService, de.msk.mylivetracker.service.ISenderService, de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd)
	 */
	@Override
	public void executeAux(UserWithRoleVo user, ITrackService trackService,
		ISenderService senderService, TracksOverviewCmd cmd)
		throws ActionExecutionException {
		SenderVo sender =
			cmd.getSenderEntries().
			get(cmd.getSelectedSenderEntryIdx()).
			getSender();
		trackService.createTrack(sender, 
			user.getOptions().getDefTrackName(), 
			user.getOptions().getDefTrackReleaseStatus());
	}
}
