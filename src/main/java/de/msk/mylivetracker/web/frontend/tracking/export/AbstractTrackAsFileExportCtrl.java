package de.msk.mylivetracker.web.frontend.tracking.export;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.msk.mylivetracker.domain.track.TrackVo;
import de.msk.mylivetracker.web.frontend.tracking.AbstractTrackingCtrl;

/**
 * AbstractTrackAsFileExportCtrl.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public abstract class AbstractTrackAsFileExportCtrl extends AbstractTrackingCtrl {

	private ITrackExporter<UserAndRoleDsc> trackExporter;
	private String httpHeaderInfo;
	private String fileExtension;
			
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
				trackExporter.convert(request, track, userAndRoleDsc);		
			response.addHeader("Content-type", this.httpHeaderInfo);
			response.addHeader("Content-Disposition", 
				"attachment; filename=\"" + track.getName() + "." + this.fileExtension + "\"");
									
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
	 * @return the trackExporter
	 */
	public ITrackExporter<UserAndRoleDsc> getTrackExporter() {
		return trackExporter;
	}

	/**
	 * @param trackExporter the trackExporter to set
	 */
	public void setTrackExporter(ITrackExporter<UserAndRoleDsc> trackExporter) {
		this.trackExporter = trackExporter;
	}

	/**
	 * @return the httpHeaderInfo
	 */
	public String getHttpHeaderInfo() {
		return httpHeaderInfo;
	}

	/**
	 * @param httpHeaderInfo the httpHeaderInfo to set
	 */
	public void setHttpHeaderInfo(String httpHeaderInfo) {
		this.httpHeaderInfo = httpHeaderInfo;
	}

	/**
	 * @return the fileExtension
	 */
	public String getFileExtension() {
		return fileExtension;
	}

	/**
	 * @param fileExtension the fileExtension to set
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}	
}
