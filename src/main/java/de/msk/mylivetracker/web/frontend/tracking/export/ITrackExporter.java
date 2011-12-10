package de.msk.mylivetracker.web.frontend.tracking.export;

import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;

import de.msk.mylivetracker.domain.track.TrackVo;

/**
 * ITrackExporter.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface ITrackExporter<T> {
	public ByteArrayOutputStream convert(HttpServletRequest request,
		TrackVo track, T addDsc) throws Exception;
}
