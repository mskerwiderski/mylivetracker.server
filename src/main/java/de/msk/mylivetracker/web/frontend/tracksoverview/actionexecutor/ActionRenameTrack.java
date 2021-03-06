package de.msk.mylivetracker.web.frontend.tracksoverview.actionexecutor;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.user.UserWithRoleVo;
import de.msk.mylivetracker.service.Services;
import de.msk.mylivetracker.web.frontend.tracksoverview.command.TracksOverviewCmd;

/**
 * ActionRenameTrack.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ActionRenameTrack extends AbstractActionEditTrack {
	
	@Override
	public void executeAux(Services services, UserWithRoleVo user,
		TracksOverviewCmd cmd) throws ActionExecutionException {
		String trackName = cmd.getSelectedTrackName();
		if (StringUtils.isEmpty(trackName) || StringUtils.isWhitespace(trackName)) {
			trackName = user.getOptions().getDefTrackName();
		}
		services.getTrackService().renameTrack(
			user.getUserId(),	
			cmd.getSelectedTrackId(), trackName);
	}
}
