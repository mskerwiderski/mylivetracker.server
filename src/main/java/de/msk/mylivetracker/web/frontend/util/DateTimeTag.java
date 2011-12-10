package de.msk.mylivetracker.web.frontend.util;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.user.UserWithoutRoleVo;
import de.msk.mylivetracker.util.datetime.DateTimeUtils;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * DateTimeTag.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-22
 * 
 */
public class DateTimeTag extends TagSupport {

	private static final long serialVersionUID = 8533210120825122008L;

	private DateTime dateTime = new DateTime();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
	 */
	@Override
	public int doStartTag() throws JspException {
		try {
			UserWithoutRoleVo user =
				WebUtils.getCurrentUserWithoutRole(pageContext.getSession());
			pageContext.getOut().print(
				DateTimeUtils.getDateTimeStr4UserRep(user, this.dateTime));
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
	 * @return the dateTime
	 */
	public DateTime getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            the dateTime to set
	 */
	public void setDateTime(DateTime dateTime) {
		this.dateTime = dateTime;
	}

}
