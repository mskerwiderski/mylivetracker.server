package de.msk.mylivetracker.web.exception;

import javax.servlet.http.HttpServletRequest;

import de.msk.mylivetracker.web.util.WebUtils;

/**
 * MyLiveTrackerException.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class MyLiveTrackerException extends RuntimeException {

	private static final long serialVersionUID = 6571199051678516039L;

	public enum ExceptionCode {
		TrackDoesNotExist("exception.track.does.not.exist"),
		NotAuthorizedForTrack("exception.not.authorized.for.track");
		private String msgCode;
		private ExceptionCode(String msgCode) {
			this.msgCode = msgCode;
		}
		public String getMsgCode() {
			return msgCode;
		}
	}
	
	public MyLiveTrackerException(HttpServletRequest request, 
		ExceptionCode exceptionCode, String...args) {		
		super(WebUtils.getMessage(
			request, exceptionCode.getMsgCode(), (Object[])args));
	}	
}
