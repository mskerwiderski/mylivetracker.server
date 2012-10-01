package de.msk.mylivetracker.service.statistics;

/**
 * StatisticsLimits.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-01
 * 
 */
public class StatisticsLimits {

	private Integer maxStatAppStartUp = 100;
	private Integer maxStatServiceCall = 500;
	private Integer maxStatSmsTransport = 100;
	private Integer maxStatStorePosProcInfo = 500;
	private Integer maxStatUplDataProc = 100000;
	private Integer maxStatUploaderServerStatus = 500;
	
	public Integer getMaxStatAppStartUp() {
		return maxStatAppStartUp;
	}
	public void setMaxStatAppStartUp(Integer maxStatAppStartUp) {
		this.maxStatAppStartUp = maxStatAppStartUp;
	}
	public Integer getMaxStatServiceCall() {
		return maxStatServiceCall;
	}
	public void setMaxStatServiceCall(Integer maxStatServiceCall) {
		this.maxStatServiceCall = maxStatServiceCall;
	}
	public Integer getMaxStatSmsTransport() {
		return maxStatSmsTransport;
	}
	public void setMaxStatSmsTransport(Integer maxStatSmsTransport) {
		this.maxStatSmsTransport = maxStatSmsTransport;
	}
	public Integer getMaxStatStorePosProcInfo() {
		return maxStatStorePosProcInfo;
	}
	public void setMaxStatStorePosProcInfo(Integer maxStatStorePosProcInfo) {
		this.maxStatStorePosProcInfo = maxStatStorePosProcInfo;
	}
	public Integer getMaxStatUplDataProc() {
		return maxStatUplDataProc;
	}
	public void setMaxStatUplDataProc(Integer maxStatUplDataProc) {
		this.maxStatUplDataProc = maxStatUplDataProc;
	}
	public Integer getMaxStatUploaderServerStatus() {
		return maxStatUploaderServerStatus;
	}
	public void setMaxStatUploaderServerStatus(Integer maxStatUploaderServerStatus) {
		this.maxStatUploaderServerStatus = maxStatUploaderServerStatus;
	}
}
