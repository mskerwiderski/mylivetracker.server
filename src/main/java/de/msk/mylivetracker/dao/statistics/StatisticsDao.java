package de.msk.mylivetracker.dao.statistics;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import de.msk.mylivetracker.domain.statistics.AbstractStatisticVo;
import de.msk.mylivetracker.domain.statistics.ApplicationStartUpVo;
import de.msk.mylivetracker.domain.statistics.DatabaseInfoVo;
import de.msk.mylivetracker.domain.statistics.ServiceCallVo;
import de.msk.mylivetracker.domain.statistics.SmsTransportVo;
import de.msk.mylivetracker.domain.statistics.StorePositionProcessorErrorVo;
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

	/*************************************************************************/
	
	private static final String SQL_SELECT_DATABASE_INFO = 
		"Statistics.selectDatabaseInfo";

	/*************************************************************************/
	
	private static final String SQL_SELECT_LAST_LOG_ID_FROM_APPLICATION_START_UP = 
		"Statistics.selectLastLogIdFromApplicationStartUp";
	private static final String SQL_INSERT_APPLICATION_START_UP = 
		"Statistics.insertApplicationStartUp";
	
	/*************************************************************************/
	
	private static final String SQL_SELECT_LAST_LOG_ID_FROM_UPLOADER_SERVER_STATUS = 
		"Statistics.selectLastLogIdFromUploaderServerStatus";
	private static final String SQL_INSERT_UPLOADER_SERVER_STATUS = 
		"Statistics.insertUploaderServerStatus";
	
	/*************************************************************************/
	
	private static final String SQL_SELECT_LAST_LOG_ID_FROM_UPLOADED_DATA_PROCESS = 
		"Statistics.selectLastLogIdFromUploadedDataProcess";
	private static final String SQL_INSERT_UPLOADED_DATA_PROCESS = 
		"Statistics.insertUploadedDataProcess";
	
	/*************************************************************************/
	
	private static final String SQL_SELECT_LAST_LOG_ID_FROM_SERVICE_CALL = 
		"Statistics.selectLastLogIdFromServiceCall";
	private static final String SQL_INSERT_SERVICE_CALL = 
		"Statistics.insertServiceCall";
	private static final String SQL_SELECT_SERVICE_CALL_COUNT = 
		"Statistics.selectServiceCallCount";
	
	/*************************************************************************/
	
	private static final String SQL_SELECT_LAST_LOG_ID_FROM_STORE_POSITION_PROCESSOR_INFO = 
		"Statistics.selectLastLogIdFromStorePositionProcessorInfo";
	private static final String SQL_INSERT_STORE_POSITION_PROCESSOR_INFO = 
		"Statistics.insertStorePositionProcessorInfo";	
	
	/*************************************************************************/
	
	private static final String SQL_SELECT_LAST_LOG_ID_FROM_STORE_POSITION_PROCESSOR_ERROR = 
		"Statistics.selectLastLogIdFromStorePositionProcessorError";
	private static final String SQL_INSERT_STORE_POSITION_PROCESSOR_ERROR = 
		"Statistics.insertStorePositionProcessorError";	
		
	/*************************************************************************/
		
	private static final String SQL_SELECT_LAST_LOG_ID_FROM_SMS_TRANSPORT = 
		"Statistics.selectLastLogIdFromSmsTransport";
	private static final String SQL_INSERT_SMS_TRANSPORT = 
		"Statistics.insertSmsTransport";	
	
	/*************************************************************************/
	
	private static void logError(String logFct, Exception e) {
		log.fatal("call of '" + logFct + "' failed: " + e);
	}
	
	@Transactional(propagation=Propagation.REQUIRES_NEW, isolation=Isolation.SERIALIZABLE)
	private void insertAux(String logFct, Integer maxStat, 
		String selectStatement, String insertStatement, AbstractStatisticVo valueObj) {
		try {
			Integer logId = (Integer)this.getSqlMapClientTemplate().queryForObject(selectStatement);
			if ((logId == null) || (logId == (maxStat-1))) {
				logId = 0;
			} else {
				logId++;
			}
			valueObj.setLogId(logId);
			this.getSqlMapClientTemplate().insert(
				insertStatement, valueObj);
		} catch (Exception e) {
			logError(logFct, e);
		}
	}
	
	/*************************************************************************/

	@Transactional(propagation=Propagation.NEVER, readOnly=true)
	public DatabaseInfoVo getDatabaseInfo(String tableSchema) {
		return (DatabaseInfoVo)
			this.getSqlMapClientTemplate().queryForObject(
				SQL_SELECT_DATABASE_INFO, tableSchema);
	}
	
	/*************************************************************************/
	
	@Override
	public void logApplicationStartUp(Integer maxStatAppStartUp) {
		insertAux(
			"logApplicationStartUp",	
			maxStatAppStartUp,	
			SQL_SELECT_LAST_LOG_ID_FROM_APPLICATION_START_UP,	
			SQL_INSERT_APPLICATION_START_UP, 
			ApplicationStartUpVo.createInstance());
	}
	
	
	@Override
	public void logUploaderServerStatus(Integer maxStatUploaderServerStatus,
		UploaderServerStatusVo uploaderServerStatusVo) {
		insertAux(
			"logUploaderServerStatus",
			maxStatUploaderServerStatus,	
			SQL_SELECT_LAST_LOG_ID_FROM_UPLOADER_SERVER_STATUS,	
			SQL_INSERT_UPLOADER_SERVER_STATUS, 
			uploaderServerStatusVo);		
	}

	@Override
	public void logUploadedDataProcess(Integer maxStatUplDataProc,
		UploadedDataProcessVo uploadedDataProcess) {
		insertAux(
			"logUploadedDataProcess",
			maxStatUplDataProc,	
			SQL_SELECT_LAST_LOG_ID_FROM_UPLOADED_DATA_PROCESS,	
			SQL_INSERT_UPLOADED_DATA_PROCESS, 
			uploadedDataProcess);	
	}	

	
	@Override
	public void logServiceCall(Integer maxStatServiceCallCount,
		ServiceCallVo serviceCall) {
		insertAux(
			"logServiceCall",
			maxStatServiceCallCount,	
			SQL_SELECT_LAST_LOG_ID_FROM_SERVICE_CALL,	
			SQL_INSERT_SERVICE_CALL, 
			serviceCall);		
	}

	
	@Override
	@Transactional(propagation=Propagation.NEVER, readOnly=true, isolation=Isolation.SERIALIZABLE)
	public int getServiceCallCountOfToday(String service) {
		int res = 0;
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("service", service);
		params.put("date", ServiceCallVo.getDateStr());
		
		Integer callCount = (Integer)
			this.getSqlMapClientTemplate().queryForObject(
				SQL_SELECT_SERVICE_CALL_COUNT, params);
		
		if (callCount != null) {
			res = callCount.intValue();
		} 
		return res;
	}

	@Override
	public void logStorePositionProcessorInfo(Integer maxStatStorePosProcInfo,
		StorePositionProcessorInfoVo storePositionProcessorInfo) {
		insertAux(
			"logStorePositionProcessorInfo",
			maxStatStorePosProcInfo,	
			SQL_SELECT_LAST_LOG_ID_FROM_STORE_POSITION_PROCESSOR_INFO,	
			SQL_INSERT_STORE_POSITION_PROCESSOR_INFO, 
			storePositionProcessorInfo);
	}

	@Override
	public void logStorePositionProcessorError(Integer maxStatStorePosProcError,
		StorePositionProcessorErrorVo storePositionProcessorError) {
		insertAux(
			"logStorePositionProcessorError",
			maxStatStorePosProcError,	
			SQL_SELECT_LAST_LOG_ID_FROM_STORE_POSITION_PROCESSOR_ERROR,	
			SQL_INSERT_STORE_POSITION_PROCESSOR_ERROR, 
			storePositionProcessorError);
	}

	@Override
	public void logSmsTransport(Integer maxStatSmsTransport,
		SmsTransportVo smsTransport) {
		insertAux(
			"logSmsTransport",	
			maxStatSmsTransport,
			SQL_SELECT_LAST_LOG_ID_FROM_SMS_TRANSPORT,
			SQL_INSERT_SMS_TRANSPORT, 
			smsTransport);		
	}
}
