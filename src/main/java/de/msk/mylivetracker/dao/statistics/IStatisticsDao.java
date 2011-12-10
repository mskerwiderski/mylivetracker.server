package de.msk.mylivetracker.dao.statistics;

import de.msk.mylivetracker.domain.statistics.ServiceCallCountVo;
import de.msk.mylivetracker.domain.statistics.SmsTransportVo;
import de.msk.mylivetracker.domain.statistics.StorePositionProcessorInfoVo;
import de.msk.mylivetracker.domain.statistics.UploadedDataProcessVo;
import de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo;

/**
 * IStatisticsDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IStatisticsDao {
	// application start up.
	public void logApplicationStartUp();	
	public void cleanApplicationStartUpTable(long olderThanInMSecs);
	
	// uploader server status.
	public void logUploaderServerStatus(UploaderServerStatusVo uploaderServerStatusVo);	
	public void cleanUploaderServerStatusTable(long olderThanInMSecs);
		
	// uploaded data process.
	public void logUploadedDataProcess(UploadedDataProcessVo uploadedDataProcess);
	public void cleanUploadedDataProcessTable(long olderThanInMSecs);
	
	// service call count.
	public void logServiceCallCount(String service);
	public ServiceCallCountVo getServiceCallCount(String service);
	public void cleanServiceCallCountTable(long olderThanInMSecs);
	
	// uploaded data process.
	public void logStorePositionProcessorInfo(StorePositionProcessorInfoVo storePositionProcessorInfo);
	public void cleanStorePositionProcessorInfoTable(long olderThanInMSecs);
	
	// sms transport.
	public void logSmsTransport(SmsTransportVo smsTransport);
	public void cleanSmsTransportTable(long olderThanInMSecs);
}
