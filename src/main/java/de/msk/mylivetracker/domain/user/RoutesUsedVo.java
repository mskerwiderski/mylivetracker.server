package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

/**
 * RoutesUsedVo.
 * 
 * @author michael skerwiderski, (c)2016
 * 
 * @version 000
 * 
 * history
 * 000 2016-08-13 initial 
 * 
 */
public class RoutesUsedVo implements Cloneable, Serializable {

	private static final long serialVersionUID = 4282206775751767155L;

	private static final String SEP = ";";
	
	private String[] routesUsed = null;
	private String routeColor = null;
	private Integer routeWidth = null;

	public RoutesUsedVo() {
	}
	
	public static RoutesUsedVo deserializeFromString(String routesUsedStr) {
		RoutesUsedVo routesUsed = new RoutesUsedVo();
		if (!StringUtils.isEmpty(routesUsedStr)) {
			String[] parts = StringUtils.splitPreserveAllTokens(routesUsedStr, SEP);
			if (parts.length < 3) {
				throw new IllegalArgumentException("invalid routesUsedStr='" + routesUsedStr + "'.");
			} 
			routesUsed.routeColor = parts[0];
			routesUsed.routeWidth = Integer.valueOf(parts[1]);
			routesUsed.routesUsed = ArrayUtils.subarray(parts, 2, parts.length-1);
		}
		return routesUsed;
	}
	
	public static String serializeToString(RoutesUsedVo routesUsed) {
		StringBuffer buf = new StringBuffer();
		buf.append(routesUsed.routeColor == null ? "" : routesUsed.routeColor).append(SEP);
		buf.append(routesUsed.routeWidth == null ? "" : routesUsed.routeWidth).append(SEP);
		buf.append(routesUsed.getRoutesUsed());
		return buf.toString();		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return deserializeFromString(serializeToString(this));
	}

	public RoutesUsedVo copy() {
		RoutesUsedVo routesUsed = null;
		try {
			routesUsed = (RoutesUsedVo)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return routesUsed;
	}
	
	public void setRoutesUsed(String routesUsedStr) {
		this.routesUsed = StringUtils.split(routesUsedStr, SEP);
	}
	
	public String getRoutesUsed() {
		StringBuffer buf = new StringBuffer();
		if (routesUsed != null) {
			for (String route : routesUsed) {
				if (!StringUtils.isEmpty(route)) {
					buf.append(route).append(SEP);
				}
			}
		}
		return buf.toString();		
	}

	public String[] getRoutesUsedParsed() {
		if ((this.routesUsed == null) || (this.routesUsed.length == 0)) {
			return new String[0];
		}
		String[] routesUsedParsed = new String[this.routesUsed.length];
		for (int i=0; i < this.routesUsed.length; i++) {
			routesUsedParsed[i] = directAccessLinkParser(this.routesUsed[i]);
		}
		return routesUsedParsed;
	}
	
	public int getRoutesUsedCount() {
		int count = 0;
		if (this.routesUsed != null) {
			count = this.routesUsed.length;
		}
		return count;
	}
	
	/**
	 * @return the routeColor
	 */
	public String getRouteColor() {
		return routeColor;
	}

	/**
	 * @param routeColor the routeColor to set
	 */
	public void setRouteColor(String routeColor) {
		this.routeColor = routeColor;
	}

	/**
	 * @return the routeWidth
	 */
	public Integer getRouteWidth() {
		return routeWidth;
	}

	/**
	 * @param routeWidth the routeWidth to set
	 */
	public void setRouteWidth(Integer routeWidth) {
		this.routeWidth = routeWidth;
	}

	
	
	private static final String LINK_DROPBOX = "www.dropbox.com";
	private static final String DIRECT_ACCESS_LINK_DROPBOX = "dl.dropboxusercontent.com";
	
	// Google Drive direct links does not work!
	//private static final String LINK_GOOGLE_DRIVE = "drive.google.com";
	//private static final String DIRECT_ACCESS_LINK_GOOGLE_DRIVE = "https://drive.google.com/uc?export=download&id=FILE_ID";
	
	private String directAccessLinkParser(String url) {
		if (StringUtils.contains(url, LINK_DROPBOX)) {
			url = StringUtils.replace(url, LINK_DROPBOX, DIRECT_ACCESS_LINK_DROPBOX);
		}
		/*
		else if (StringUtils.contains(url, LINK_GOOGLE_DRIVE)) {
			// from https://drive.google.com/open?id=110akplNmLgIvKQ4tKKuB8jIdeNU
			// to https://drive.google.com/uc?export=download&id=FILE_ID
			String fileId = StringUtils.replace(url, "https://drive.google.com/open?id=", "");
			url = DIRECT_ACCESS_LINK_GOOGLE_DRIVE;
			url = StringUtils.replace(url, "FILE_ID", fileId);
		}
		*/
		return url;
	}
}