package de.msk.mylivetracker.web.frontend.tracking;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.web.options.IntOptionDsc;

/**
 * TrackAsMapCtrl.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackAsMapCtrl extends AbstractTrackingCtrl {
	
	private List<IntOptionDsc> supportedMaps;
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl#addToJsonModel(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.UserAndRoleDsc, de.msk.mylivetracker.domain.track.TrackVo, java.util.Map)
	 */
	@Override
	protected void addToJsonModel(HttpServletRequest request,
		UserAndRoleDsc userAndRoleDsc, TrackVo track,
		Map<String, Object> model) {
		model.put("user", userAndRoleDsc.user);
		model.put("track", track);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl#addToResponse(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.UserAndRoleDsc, de.msk.mylivetracker.domain.track.TrackVo, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void addToResponse(HttpServletRequest request,
		UserAndRoleDsc userAndRoleDsc, TrackVo track,
		HttpServletResponse response) {
		// noop.				
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl#addToViewModel(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.UserAndRoleDsc, java.util.Map)
	 */
	@Override
	protected void addToViewModel(HttpServletRequest request,
		UserAndRoleDsc userAndRoleDsc, Map<String, Object> model) {
		model.put("supportedMaps", supportedMaps);
		model.put("mapsUsedStr", userAndRoleDsc.user.getOptions().getMapsUsed().getMapsUsedStr(supportedMaps.size()));
		model.put("routesUsed", userAndRoleDsc.user.getOptions().getRoutesUsed());
		model.put("defMapId", userAndRoleDsc.user.getOptions().getMapsUsed().getDefMapId());
		model.put("showTrackInfo", (Boolean)model.get(PARAM_SHOW_TRACK_INFO.getName()));		
	}

	public List<IntOptionDsc> getSupportedMaps() {
		return supportedMaps;
	}

	public void setSupportedMaps(List<IntOptionDsc> supportedMaps) {
		this.supportedMaps = supportedMaps;
	}
}
