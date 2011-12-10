package de.msk.mylivetracker.domain.statistics;

import de.msk.mylivetracker.Global;

/**
 * ApplicationStartUpVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ApplicationStartUpVo extends AbstractStatisticVo {

	private static final long serialVersionUID = -3105705216865123635L;
	
	private String version;
	
	public static ApplicationStartUpVo createInstance() {
		return new ApplicationStartUpVo(Global.getVersion());		
	}
		
	public ApplicationStartUpVo() {
	}

	/**
	 * @param version
	 */
	private ApplicationStartUpVo(String version) {
		this.version = version;
	}


	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
