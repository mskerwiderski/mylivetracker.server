package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * AbstractActionEditTrack.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractActionEditTrack extends AbstractActionWithoutRedirect {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(AbstractActionEditTrack.class);

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.tracksoverview.actionexecutor.IAction#preExecuteCheck(de.msk.mylivetracker.web.tracksoverview.TracksOverviewCmd)
	 */
	public void preExecuteCheck(TracksOverviewCmd cmd)
		throws ActionExecutionException {
		UserWithRoleVo user = WebUtils.getCurrentUserWithRole();
		if (!TrackVo.isEditable(user.getRole())) {
			throw new ActionExecutionException(
				"user " + user.getUsername() + 
				" is not allowed to edit track with trackid '" + 
				cmd.getSelectedTrackId() + "'.");
		}
	}	
}
