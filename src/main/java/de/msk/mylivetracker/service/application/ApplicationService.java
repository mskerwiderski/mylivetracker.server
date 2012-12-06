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
		
	public void loadParameters() {
		if ((this.applicationCache != null) && 
			this.applicationCache.getSize() > 0) return;
		log.info("initialize application parameter cache.");
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
	
	@Override
	public void reloadParameters() {
		applicationCache.removeAll();
		loadParameters();
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

	@Override
	public String getServerAddress() {
		return this.getParameterValueAsString(Parameter.ServerAddress);
	}

	@Override
	public String getApplicationBaseUrl() {
		return this.getParameterValueAsString(Parameter.ApplicationBaseUrl);
	}

	@Override
	public Integer getApplicationPort() {
		return Integer.valueOf(this.getParameterValueAsString(Parameter.ApplicationPort));		
	}

	@Override
	public String getParameterValueAsString(Parameter parameter) {
		if (!applicationCache.isKeyInCache(parameter.name())) {
			throw new IllegalArgumentException("parameter '" + 
				parameter.name() + "' does not exist.");
		} 
		return (String)applicationCache.get(parameter.name()).getObjectValue();										
	}

	@Override
	public Integer getParameterValueAsInteger(Parameter parameter) {
		return NumberUtils.toInt(
			getParameterValueAsString(parameter));
	}
	
	@Override
	public Long getParameterValueAsLong(Parameter parameter) {
		return NumberUtils.toLong(
			getParameterValueAsString(parameter));		
	}
	
	@Override
	public Boolean getParameterValueAsBoolean(Parameter parameter) {
		return BooleanUtils.toBoolean(
			getParameterValueAsString(parameter));		
	}
	
	public IApplicationDao getApplicationDao() {
		return applicationDao;
	}
	public void setApplicationDao(IApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}
	public Cache getApplicationCache() {
		return applicationCache;
	}
	public void setApplicationCache(Cache applicationCache) {
		this.applicationCache = applicationCache;
	}	
}
