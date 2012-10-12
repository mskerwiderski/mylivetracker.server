package de.msk.mylivetracker.dao.typehandler;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import de.msk.mylivetracker.domain.sender.SenderRadiusUnit;

/**
 * SenderSymbolTypeHandlerCallback.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-09-01
 * 
 */
public class SenderRadiusUnitTypeHandlerCallback implements TypeHandlerCallback {

	@Override
	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		setter.setString(((SenderRadiusUnit)parameter).name());
	}

	@Override
	public Object getResult(ResultGetter getter) throws SQLException {
		String str = getter.getString();
		return SenderRadiusUnit.valueOf(str);
	}

	@Override
	public Object valueOf(String s) {
		return s;
	}
}
