package de.msk.mylivetracker.web.frontend.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import de.msk.mylivetracker.web.util.FmtUtils;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * PositionTag.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-24
 * 
 */
public class PositionTag extends TagSupport {

	private static final long serialVersionUID = -389423724066370880L;
	
	private Double latitude = null;
	private Double longitude = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		try {
			pageContext.getOut().print(
				FmtUtils.getPositionAsStr(this.latitude, this.longitude, 
					WebUtils.getLocale((HttpServletRequest)pageContext.getRequest()))); 
		} catch (Exception e) {
			throw new JspTagException("DateTimeTag: " + e.getMessage());
		}
		return SKIP_BODY;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
	 */
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	/**
	 * @return the latitude
	 */
	public Double getLatitude() {
		return latitude;
	}

	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	/**
	 * @return the longitude
	 */
	public Double getLongitude() {
		return longitude;
	}

	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}
}
