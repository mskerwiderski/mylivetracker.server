package de.msk.mylivetracker.domain.user;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

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
	
	public RoutesUsedVo(String routesUsedStr) {
		setRoutesUsedStr(routesUsedStr);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return new RoutesUsedVo(this.toString());
	}

	public RoutesUsedVo copy() {
		RoutesUsedVo mapsUsed = null;
		try {
			mapsUsed = (RoutesUsedVo)clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return mapsUsed;
	}
	
	public String getRoutesUsedStr() {
		return this.toString();
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

	public void setRoutesUsedStr(String routesUsedStr) {
		this.routesUsed = StringUtils.split(routesUsedStr, SEP);
	}
	
	public int getRoutesUsedCount() {
		int count = 0;
		if (this.routesUsed != null) {
			count = this.routesUsed.length;
		}
		return count;
	}
	
	@Override
	public String toString() {
		if (this.routesUsed == null) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (String route : this.routesUsed) {
			if (!StringUtils.isEmpty(route)) {
				buf.append(route).append(SEP);
			}
		}
		return buf.toString();
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