package de.msk.mylivetracker.dao.statistics;

import de.msk.mylivetracker.domain.statistics.ServiceCallVo;
import de.msk.mylivetracker.domain.statistics.SmsTransportVo;
import de.msk.mylivetracker.domain.statistics.StorePositionProcessorErrorVo;
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
	public void logApplicationStartUp(Integer maxStatAppStartUp);	
	public void logUploaderServerStatus(Integer maxStatUploaderServerStatus, UploaderServerStatusVo uploaderServerStatusVo);	
	public void logUploadedDataProcess(Integer maxStatUplDataProc, UploadedDataProcessVo uploadedDataProcess);
	public void logServiceCall(Integer maxStatServiceCallCount, ServiceCallVo serviceCall);
	public int getServiceCallCountOfToday(String service);
	public void logStorePositionProcessorInfo(Integer maxStatStorePosProcInfo, StorePositionProcessorInfoVo storePositionProcessorInfo);
	public void logStorePositionProcessorError(Integer maxStatStorePosProcError, StorePositionProcessorErrorVo storePositionProcessorError);
	public void logSmsTransport(Integer maxStatSmsTransport, SmsTransportVo smsTransport);
}
