package de.msk.mylivetracker.web.frontend.tracking;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.web.frontend.tracking.export.ITrackExporter;

/**
 * TrackAsGoogleEarthCtrl.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class TrackAsGoogleEarthCtrl extends AbstractTrackingCtrl {

	private ITrackExporter<UserAndRoleDsc> trackExporterKml;
			
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl#addToJsonModel(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.UserAndRoleDsc, de.msk.mylivetracker.domain.track.TrackVo, java.util.Map)
	 */
	@Override
	protected void addToJsonModel(HttpServletRequest request,
		UserAndRoleDsc userAndRoleDsc, TrackVo track,
		Map<String, Object> model) {
		// noop.
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl#addToResponse(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.UserAndRoleDsc, de.msk.mylivetracker.domain.track.TrackVo, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void addToResponse(HttpServletRequest request,
		UserAndRoleDsc userAndRoleDsc, TrackVo track,
		HttpServletResponse response) {
		try {
			ByteArrayOutputStream ostr = 
				trackExporterKml.convert(request, track, userAndRoleDsc);				
			response.addHeader("Content-type", "application/vnd.google-earth.kmz");
			response.addHeader("Content-Disposition", 
				"attachment; filename=\"" + track.getTrackId() + ".kmz\"");
									
			response.getOutputStream().write(ostr.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
				
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl#addToViewModel(javax.servlet.http.HttpServletRequest, de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl.UserAndRoleDsc, java.util.Map)
	 */
	@Override
	protected void addToViewModel(HttpServletRequest request,
		UserAndRoleDsc userAndRoleDsc, Map<String, Object> model) {
		// noop.	
	}

	/**
	 * @return the trackExporterKml
	 */
	public ITrackExporter<UserAndRoleDsc> getTrackExporterKml() {
		return trackExporterKml;
	}

	/**
	 * @param trackExporterKml the trackExporterKml to set
	 */
	public void setTrackExporterKml(ITrackExporter<UserAndRoleDsc> trackExporterKml) {
		this.trackExporterKml = trackExporterKml;
	}		
}
