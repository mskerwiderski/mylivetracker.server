package de.msk.mylivetracker.service.application;

import java.util.List;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.msk.mylivetracker.dao.IApplicationDao;
import de.msk.mylivetracker.domain.ParameterVo;

/**
 * ApplicationService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ApplicationService implements IApplicationService {

	private static final Log log = LogFactory.getLog(ApplicationService.class);
	
	private IApplicationDao applicationDao;
	private Cache applicationCache;	
		
	public void init() {
		List<ParameterVo> parameters = applicationDao.getAllParameters();
		log.info("MyLiveTracker is running with following parameters:");
		for (ParameterVo parameter : parameters) {
			String parameterValue = System.getenv(parameter.getName());
			if (StringUtils.isEmpty(parameterValue)) {
				parameterValue = parameter.getValue();
			}
			applicationCache.put(
				new Element(parameter.getName(), parameterValue));
			log.info(parameter.getName() + "='" + parameterValue + "'");
		}
	}
	
	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IApplicationService#reloadParameters()
	 */
	@Override
	public void reloadParameters() {
		applicationCache.removeAll();
		init();
	}	
		
	private boolean isPortUsed(String ports, int port) {
		boolean isUsed = false;
		String[] portArr = 
			StringUtils.split(ports, ',');
		if ((portArr == null) || portArr.length == 0) return isUsed;
		for (int i=0; !isUsed && (i < portArr.length); i++) {
			isUsed = StringUtils.equals(portArr[i], String.valueOf(port));
		}
		return isUsed;	
	}
	
	@Override
	public boolean isTcpPortUsed(int port) {
		return isPortUsed(
			this.getParameterValueAsString(Parameter.ClientTcpPorts), 
			port);
	}

	@Override
	public boolean isUdpPortUsed(int port) {
		return isPortUsed(
			this.getParameterValueAsString(Parameter.ClientUdpPorts), 
			port);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IApplicationService#getServerAddress()
	 */
	@Override
	public String getServerAddress() {
		return this.getParameterValueAsString(Parameter.ServerAddress);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IApplicationService#getApplicationBaseUrl()
	 */
	@Override
	public String getApplicationBaseUrl() {
		return this.getParameterValueAsString(Parameter.ApplicationBaseUrl);
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IApplicationService#getApplicationPort()
	 */
	@Override
	public Integer getApplicationPort() {
		return Integer.valueOf(this.getParameterValueAsString(Parameter.ApplicationPort));		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IApplicationService#getParameterValueAsString(de.msk.mylivetracker.service.IApplicationService.Parameter)
	 */
	@Override
	public String getParameterValueAsString(Parameter parameter) {
		if (!applicationCache.isKeyInCache(parameter.name())) {
			throw new IllegalArgumentException("parameter '" + 
				parameter.name() + "' does not exist.");
		} 
		return (String)applicationCache.get(parameter.name()).getObjectValue();										
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IApplicationService#getParameterValueAsLong(de.msk.mylivetracker.service.IApplicationService.Parameter)
	 */
	@Override
	public Long getParameterValueAsLong(Parameter parameter) {
		return NumberUtils.toLong(
			getParameterValueAsString(parameter));		
	}

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.service.IApplicationService#getParameterValueAsBoolean(de.msk.mylivetracker.service.IApplicationService.Parameter)
	 */
	@Override
	public Boolean getParameterValueAsBoolean(Parameter parameter) {
		return BooleanUtils.toBoolean(
			getParameterValueAsString(parameter));		
	}

	/**
	 * @return the applicationDao
	 */
	public IApplicationDao getApplicationDao() {
		return applicationDao;
	}

	/**
	 * @param applicationDao the applicationDao to set
	 */
	public void setApplicationDao(IApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	/**
	 * @return the applicationCache
	 */
	public Cache getApplicationCache() {
		return applicationCache;
	}

	/**
	 * @param applicationCache the applicationCache to set
	 */
	public void setApplicationCache(Cache applicationCache) {
		this.applicationCache = applicationCache;
	}	
}
