package de.msk.mylivetracker.dao.typehandler;

import java.sql.SQLException;

import org.apache.commons.lang.StringUtils;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import de.msk.mylivetracker.domain.sender.SenderSymbolVo;

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
public class SenderSymbolTypeHandlerCallback implements TypeHandlerCallback {

	@Override
	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		if (parameter == null) {
			setter.setString("");
		} else {
			setter.setString(((SenderSymbolVo)parameter).getId());
		}
	}

	@Override
	public Object getResult(ResultGetter getter) throws SQLException {
		String str = getter.getString();
		if (StringUtils.isEmpty(str)) return null;
		return SenderSymbolVo.getById(str);
	}

	@Override
	public Object valueOf(String s) {
		return s;
	}
}
