package de.msk.mylivetracker.dao.statistics;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import de.msk.mylivetracker.commons.util.datetime.DateTime;
import de.msk.mylivetracker.domain.statistics.AbstractStatisticVo;
import de.msk.mylivetracker.domain.statistics.ApplicationStartUpVo;
import de.msk.mylivetracker.domain.statistics.ServiceCallCountVo;
import de.msk.mylivetracker.domain.statistics.SmsTransportVo;
import de.msk.mylivetracker.domain.statistics.StorePositionProcessorInfoVo;
import de.msk.mylivetracker.domain.statistics.UploadedDataProcessVo;
import de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo;

/**
 * StatisticsDao.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class StatisticsDao extends SqlMapClientDaoSupport implements IStatisticsDao {

	private static final Log log = LogFactory.getLog(StatisticsDao.class);
	
	private static final String SQL_INSERT_APPLICATION_START_UP = 
		"Statistics.insertApplicationStartUp";
	private static final String SQL_CLEAN_APPLICATION_START_UP_TABLE = 
		"Statistics.cleanApplicationStartUpTable";
	private static final String SQL_INSERT_UPLOADER_SERVER_STATUS = 
		"Statistics.insertUploaderServerStatus";
	private static final String SQL_CLEAN_UPLOADER_SERVER_STATUS_TABLE = 
		"Statistics.cleanUploaderServerStatusTable";
	private static final String SQL_INSERT_UPLOADED_DATA_PROCESS = 
		"Statistics.insertUploadedDataProcess";
	private static final String SQL_CLEAN_UPLOADED_DATA_PROCESS_TABLE = 
		"Statistics.cleanUploadedDataProcessTable";
	private static final String SQL_UPDATE_SERVICE_CALL_COUNT = 
		"Statistics.updateServiceCallCount";
	private static final String SQL_INSERT_SERVICE_CALL_COUNT = 
		"Statistics.insertServiceCallCount";
	private static final String SQL_SELECT_SERVICE_CALL_COUNT = 
		"Statistics.selectServiceCallCount";
	private static final String SQL_CLEAN_SERVICE_CALL_COUNT_TABLE = 
		"Statistics.cleanServiceCallCountTable";
	private static final String SQL_INSERT_STORE_POSITION_PROCESSOR_INFO = 
		"Statistics.insertStorePositionProcessorInfo";	
	private static final String SQL_CLEAN_STORE_POSITION_PROCESSOR_INFO_TABLE = 
		"Statistics.cleanStorePositionProcessorInfoTable";
	private static final String SQL_INSERT_SMS_TRANSPORT = 
		"Statistics.insertSmsTransport";	
	private static final String SQL_CLEAN_SMS_TRANSPORT_TABLE = 
		"Statistics.cleanSmsTransportTable";
	
	private void insertAux(String statement, AbstractStatisticVo valueObj) {
		try {
			this.getSqlMapClientTemplate().insert(statement, valueObj);
		} catch (Exception e) {
			e.printStackTrace();
			log.fatal(e);
		}
	}
	
	private void cleanAux(String statement, long olderThanInMSecs) {
		try {
			DateTime timestamp = 
				new DateTime(new DateTime().getAsMSecs() - olderThanInMSecs);
			this.getSqlMapClientTemplate().delete(statement, timestamp);
		} catch (Exception e) {
			log.fatal(e);
		}
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#logApplicationStartUp()
	 */
	@Override
	public void logApplicationStartUp() {
		insertAux(
			SQL_INSERT_APPLICATION_START_UP, 
			ApplicationStartUpVo.createInstance());
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#cleanApplicationStartUpTable(long)
	 */
	@Override
	public void cleanApplicationStartUpTable(long olderThanInMSecs) {
		cleanAux(
			SQL_CLEAN_APPLICATION_START_UP_TABLE, 
			olderThanInMSecs);	
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#logUploaderServerStatus(de.msk.mylivetracker.domain.statistics.UploaderServerStatusVo)
	 */
	@Override
	public void logUploaderServerStatus(
		UploaderServerStatusVo uploaderServerStatusVo) {
		insertAux(
			SQL_INSERT_UPLOADER_SERVER_STATUS, 
			uploaderServerStatusVo);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#cleanUploaderServerStatusTable(long)
	 */
	@Override
	public void cleanUploaderServerStatusTable(long olderThanInMSecs) {
		cleanAux(
			SQL_CLEAN_UPLOADER_SERVER_STATUS_TABLE, 
			olderThanInMSecs);			
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#logUploadedDataProcess(de.msk.mylivetracker.domain.statistics.UploadedDataProcessVo)
	 */
	@Override
	public void logUploadedDataProcess(UploadedDataProcessVo uploadedDataProcess) {
		insertAux(
			SQL_INSERT_UPLOADED_DATA_PROCESS, 
			uploadedDataProcess);
	}	
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#cleanUploadedDataProcessTable(long)
	 */
	@Override
	public void cleanUploadedDataProcessTable(long olderThanInMSecs) {
		cleanAux(
			SQL_CLEAN_UPLOADED_DATA_PROCESS_TABLE, 
			olderThanInMSecs);			
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#logServiceCallCount(java.lang.String)
	 */
	@Override
	public void logServiceCallCount(String service) {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("service", service);
			params.put("date", ServiceCallCountVo.getDayStr());
			
			ServiceCallCountVo serviceCallCount = (ServiceCallCountVo)
				this.getSqlMapClientTemplate().queryForObject(
					SQL_SELECT_SERVICE_CALL_COUNT, params);
			
			if (serviceCallCount == null) {
				serviceCallCount = ServiceCallCountVo.createInstance(
					service, 1);
				this.getSqlMapClientTemplate().insert(
					SQL_INSERT_SERVICE_CALL_COUNT, serviceCallCount);
			} else {
				serviceCallCount.setCallCount(serviceCallCount.getCallCount() + 1);
				this.getSqlMapClientTemplate().update(
					SQL_UPDATE_SERVICE_CALL_COUNT, serviceCallCount);
			}
		} catch (Exception e) {
			log.fatal(e);
		}
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#getServiceCallCount(java.lang.String)
	 */
	@Override
	public ServiceCallCountVo getServiceCallCount(String service) {
		ServiceCallCountVo serviceCallCount = null;
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("service", service);
			params.put("date", ServiceCallCountVo.getDayStr());
			
			serviceCallCount = (ServiceCallCountVo)
				this.getSqlMapClientTemplate().queryForObject(
					SQL_SELECT_SERVICE_CALL_COUNT, params);
			
			if (serviceCallCount == null) {
				serviceCallCount = ServiceCallCountVo.createInstance(service, 0);
				this.getSqlMapClientTemplate().insert(
					SQL_INSERT_SERVICE_CALL_COUNT, serviceCallCount);
			} 
		} catch (Exception e) {
			log.fatal(e);
		}	
		return serviceCallCount;
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#cleanServiceCallCountTable(long)
	 */
	@Override
	public void cleanServiceCallCountTable(long olderThanInMSecs) {
		cleanAux(
			SQL_CLEAN_SERVICE_CALL_COUNT_TABLE, 
			olderThanInMSecs);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#logStorePositionProcessorInfo(de.msk.mylivetracker.domain.statistics.StorePositionProcessorInfoVo)
	 */
	@Override
	public void logStorePositionProcessorInfo(
		StorePositionProcessorInfoVo storePositionProcessorInfo) {
		insertAux(
			SQL_INSERT_STORE_POSITION_PROCESSOR_INFO, 
			storePositionProcessorInfo);
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#cleanStorePositionProcessorInfoTable(long)
	 */
	@Override
	public void cleanStorePositionProcessorInfoTable(long olderThanInMSecs) {
		cleanAux(
			SQL_CLEAN_STORE_POSITION_PROCESSOR_INFO_TABLE, 
			olderThanInMSecs);		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#logSmsTransport(de.msk.mylivetracker.domain.statistics.SmsTransportVo)
	 */
	@Override
	public void logSmsTransport(SmsTransportVo smsTransport) {
		insertAux(
			SQL_INSERT_SMS_TRANSPORT, 
			smsTransport);		
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.dao.statistics.IStatisticsDao#cleanSmsTransportTable(long)
	 */
	@Override
	public void cleanSmsTransportTable(long olderThanInMSecs) {
		cleanAux(
			SQL_CLEAN_SMS_TRANSPORT_TABLE, 
			olderThanInMSecs);
	}

			
}
