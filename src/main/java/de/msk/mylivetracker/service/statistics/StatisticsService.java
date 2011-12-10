package de.msk.mylivetracker.service.statistics;

import de.msk.mylivetracker.dao.statistics.IStatisticsDao;
import de.msk.mylivetracker.domain.statistics.ServiceCallCountVo;
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
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logApplicationStartUp()
	 */
	@Override
	public void logApplicationStartUp() {
		statisticsDao.logApplicationStartUp();
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#cleanApplicationStartUpTable(long)
	 */
	@Override
	public void cleanApplicationStartUpTable(long olderThanInMSecs) {
		statisticsDao.cleanApplicationStartUpTable(olderThanInMSecs);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logUploaderServerStatus(de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo)
	 */
	@Override
	public void logUploaderServerStatus(
		UploaderServerStatusVo uploaderServerStatusVo) {
		statisticsDao.logUploaderServerStatus(uploaderServerStatusVo);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#cleanUploaderServerStatusTable(long)
	 */
	@Override
	public void cleanUploaderServerStatusTable(long olderThanInMSecs) {
		statisticsDao.cleanUploaderServerStatusTable(olderThanInMSecs);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logUploadedDataProcess(de.msk.mylivetracker.domain.statistics.UploadedDataProcessVo)
	 */
	@Override
	public void logUploadedDataProcess(UploadedDataProcessVo uploadedDataProcess) {
		statisticsDao.logUploadedDataProcess(uploadedDataProcess);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#cleanUploadedDataProcessTable(long)
	 */
	@Override
	public void cleanUploadedDataProcessTable(long olderThanInMSecs) {
		statisticsDao.cleanUploadedDataProcessTable(olderThanInMSecs);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logServiceCallCount(java.lang.String)
	 */
	@Override
	public void logServiceCallCount(String service) {
		statisticsDao.logServiceCallCount(service);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#getServiceCallCount(java.lang.String)
	 */
	@Override
	public ServiceCallCountVo getServiceCallCount(String service) {
		return statisticsDao.getServiceCallCount(service);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#cleanServiceCallCountTable(long)
	 */
	@Override
	public void cleanServiceCallCountTable(long olderThanInMSecs) {
		statisticsDao.cleanServiceCallCountTable(olderThanInMSecs);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logStorePositionProcessorInfo(de.msk.mylivetracker.domain.statistics.StorePositionProcessorInfoVo)
	 */
	@Override
	public void logStorePositionProcessorInfo(
			StorePositionProcessorInfoVo storePositionProcessorInfo) {
		statisticsDao.logStorePositionProcessorInfo(storePositionProcessorInfo);		
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#cleanStorePositionProcessorInfoTable(long)
	 */
	@Override
	public void cleanStorePositionProcessorInfoTable(long olderThanInMSecs) {
		statisticsDao.cleanStorePositionProcessorInfoTable(olderThanInMSecs);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#logSmsTransport(de.msk.mylivetracker.domain.statistics.SmsTransportVo)
	 */
	@Override
	public void logSmsTransport(SmsTransportVo smsTransport) {
		statisticsDao.logSmsTransport(smsTransport);
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.statistics.IStatisticsService#cleanSmsTransportTable(long)
	 */
	@Override
	public void cleanSmsTransportTable(long olderThanInMSecs) {
		statisticsDao.cleanSmsTransportTable(olderThanInMSecs);
	}
	
	/**
	 * @return the statisticsDao
	 */
	public IStatisticsDao getStatisticsDao() {
		return statisticsDao;
	}

	/**
	 * @param statisticsDao the statisticsDao to set
	 */
	public void setStatisticsDao(IStatisticsDao statisticsDao) {
		this.statisticsDao = statisticsDao;
	}
}
