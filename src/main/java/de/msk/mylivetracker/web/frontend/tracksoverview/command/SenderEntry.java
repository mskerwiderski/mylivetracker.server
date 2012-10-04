package de.msk.mylivetracker.web.frontend.tracksoverview.command;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.web.util.FmtUtils;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * SenderEntry.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SenderEntry implements Serializable {

	private static final long serialVersionUID = 4459904924545546422L;
	
	public static final String VALUE_ALL = "all";
	public static final String LABEL_ALL = "common.opts.all"; 
	
	private String value;
	private String label;
	
	public SenderEntry(HttpServletRequest request) {
		this.value = VALUE_ALL;
		this.label = WebUtils.getMessage(request, LABEL_ALL);
	}
	
	public SenderEntry(SenderVo sender) {
		if (sender == null) {
			throw new IllegalArgumentException("sender must not be null.");
		}
		this.value = sender.getSenderId();
		this.label = FmtUtils.getSenderLabel(sender, false, FmtUtils.noValue);			
	}
	
	public static boolean isAll(String value) {
		return StringUtils.equals(value, VALUE_ALL);
	}
	
	public boolean isAll() {
		return isAll(this.value);
	}
	
	public String getValue() {
		return value;
	}
	public String getLabel() {
		return label;
	}
}
