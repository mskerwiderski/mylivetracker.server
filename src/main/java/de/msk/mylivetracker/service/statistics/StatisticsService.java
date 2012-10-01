package de.msk.mylivetracker.service.statistics;

import de.msk.mylivetracker.dao.statistics.IStatisticsDao;
import de.msk.mylivetracker.domain.statistics.ServiceCallVo;
import de.msk.mylivetracker.domain.statistics.SmsTransportVo;
import de.msk.mylivetracker.domain.statistics.StorePositionProcessorInfoVo;
import de.msk.mylivetracker.domain.statistics.UploadedDataProcessVo;
import de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo;

/**
 * StatisticsService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class StatisticsService implements IStatisticsService {

	private IStatisticsDao statisticsDao;
	private StatisticsLimits statisticsLimits;
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logApplicationStartUp()
	 */
	@Override
	public void logApplicationStartUp() {
		statisticsDao.logApplicationStartUp(
			statisticsLimits.getMaxStatAppStartUp());
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logUploaderServerStatus(de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo)
	 */
	@Override
	public void logUploaderServerStatus(
		UploaderServerStatusVo uploaderServerStatusVo) {
		statisticsDao.logUploaderServerStatus(
			statisticsLimits.getMaxStatUploaderServerStatus(), uploaderServerStatusVo);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logUploadedDataProcess(de.msk.mylivetracker.domain.statistics.UploadedDataProcessVo)
	 */
	@Override
	public void logUploadedDataProcess(UploadedDataProcessVo uploadedDataProcess) {
		statisticsDao.logUploadedDataProcess(
			statisticsLimits.getMaxStatUplDataProc(), uploadedDataProcess);		
	}

	
	@Override
	public void logServiceCall(ServiceCallVo serviceCall) {
		statisticsDao.logServiceCall(
			statisticsLimits.getMaxStatServiceCall(), serviceCall);
	}

	@Override
	public int getServiceCallCountOfToday(String service) {
		return statisticsDao.getServiceCallCountOfToday(service);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logStorePositionProcessorInfo(de.msk.mylivetracker.domain.statistics.StorePositionProcessorInfoVo)
	 */
	@Override
	public void logStorePositionProcessorInfo(
			StorePositionProcessorInfoVo storePositionProcessorInfo) {
		statisticsDao.logStorePositionProcessorInfo(
			statisticsLimits.getMaxStatStorePosProcInfo(), storePositionProcessorInfo);		
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logSmsTransport(de.msk.mylivetracker.domain.statistics.SmsTransportVo)
	 */
	@Override
	public void logSmsTransport(SmsTransportVo smsTransport) {
		statisticsDao.logSmsTransport(
			statisticsLimits.getMaxStatSmsTransport(), smsTransport);
	}
	
	public IStatisticsDao getStatisticsDao() {
		return statisticsDao;
	}

	public void setStatisticsDao(IStatisticsDao statisticsDao) {
		this.statisticsDao = statisticsDao;
	}

	public StatisticsLimits getStatisticsLimits() {
		return statisticsLimits;
	}

	public void setStatisticsLimits(StatisticsLimits statisticsLimits) {
		this.statisticsLimits = statisticsLimits;
	}
}
