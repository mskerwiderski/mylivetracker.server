package de.msk.mylivetracker.dao.typehandler;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import de.msk.mylivetracker.domain.user.RoutesUsedVo;

/**
 * RoutesUsedTypeHandlerCallback.
 * 
 * @author michael skerwiderski, (c)2016
 * 
 * @version 000
 * 
 * history
 * 000 initial 2016-08-13
 * 
 */
public class RoutesUsedTypeHandlerCallback implements TypeHandlerCallback {

	@Override
	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		if (parameter == null) {
			setter.setString("");
		} else {
			RoutesUsedVo routesUsed = (RoutesUsedVo)parameter;
			setter.setString(RoutesUsedVo.serializeToString(routesUsed));
		}
	}

	@Override
	public Object getResult(ResultGetter getter) throws SQLException {
		String routesUsedAsStr = getter.getString();
		return RoutesUsedVo.deserializeFromString(routesUsedAsStr);
	}

	@Override
	public Object valueOf(String s) {
		return s;
	}
}
