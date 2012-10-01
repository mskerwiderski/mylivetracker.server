package de.msk.mylivetracker.dao.typehandler;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

import de.msk.mylivetracker.commons.util.datetime.DateTime;

/**
 * TimeZoneTypeHandlerCallback.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-12
 * 
 */
public class DateTimeAsInternalDateTimeFormatTypeHandlerCallback implements TypeHandlerCallback {

	@Override
	public void setParameter(ParameterSetter setter, Object parameter) throws SQLException {
		if (parameter == null) {
			setter.setString("");
		} else {
			setter.setString(((DateTime)parameter).getAsStr(
				TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC),
				DateTime.INTERNAL_DATE_TIME_FMT));
		}
	}

	@Override
	public Object getResult(ResultGetter getter) throws SQLException {
		String str = getter.getString();
		if (StringUtils.isEmpty(str)) return null;
		DateTime obj = null;
		try {
			obj = new DateTime(str, 
			DateTime.INTERNAL_DATE_TIME_FMT, 
			TimeZone.getTimeZone(DateTime.TIME_ZONE_UTC));
		} catch (ParseException e) {
			throw new SQLException(e);
		}
		return obj;
	}

	@Override
	public Object valueOf(String s) {
		return s;
	}
}
