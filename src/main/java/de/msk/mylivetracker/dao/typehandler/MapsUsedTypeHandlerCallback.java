package de.msk.mylivetracker.dao.typehandler;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import de.msk.mylivetracker.domain.user.MapsUsedVo;

/**
 * MapsUsedTypeHandlerCallback.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-09-16
 * 
 */
public class MapsUsedTypeHandlerCallback implements TypeHandlerCallback {

	@Override
	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		if (parameter == null) {
			setter.setString("");
		} else {
			MapsUsedVo mapsUsed = (MapsUsedVo)parameter;
			setter.setString(mapsUsed.toString());
		}
	}

	@Override
	public Object getResult(ResultGetter getter) throws SQLException {
		String mapUsedStr = getter.getString();
		if (StringUtils.isEmpty(mapUsedStr)) return null;
		return new MapsUsedVo(mapUsedStr);
	}

	@Override
	public Object valueOf(String s) {
		return s;
	}
}
