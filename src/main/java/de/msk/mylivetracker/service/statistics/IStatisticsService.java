package de.msk.mylivetracker.service.statistics;

import de.msk.mylivetracker.domain.statistics.ServiceCallVo;
import de.msk.mylivetracker.domain.statistics.SmsTransportVo;
import de.msk.mylivetracker.domain.statistics.StorePositionProcessorErrorVo;
import de.msk.mylivetracker.domain.statistics.StorePositionProcessorInfoVo;
import de.msk.mylivetracker.domain.statistics.UploadedDataProcessVo;
import de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo;

/**
 * IStatisticsService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IStatisticsService {
	public void logApplicationStartUp();	
	public void logUploaderServerStatus(UploaderServerStatusVo uploaderServerStatusVo);	
	public void logUploadedDataProcess(UploadedDataProcessVo uploadedDataProcess);
	public void logServiceCall(ServiceCallVo serviceCall);
	public int getServiceCallCountOfToday(String service);
	public void logStorePositionProcessorInfo(StorePositionProcessorInfoVo storePositionProcessorInfo);
	public void logStorePositionProcessorError(StorePositionProcessorErrorVo storePositionProcessorError);
	public void logSmsTransport(SmsTransportVo smsTransport);
}
