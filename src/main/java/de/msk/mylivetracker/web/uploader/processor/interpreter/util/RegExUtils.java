package de.msk.mylivetracker.web.uploader.processor.interpreter.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * RegExUtils.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public final class RegExUtils {
	private RegExUtils() {		
	}
	
	public static String getDataItemIfRegExMatches(
		String regex, String dataStr, boolean throwExceptionIfNotFound)
		throws InterpreterException {
		return getDataItemIfRegExAux(regex, 
			dataStr, true, throwExceptionIfNotFound);
	}
	
	public static String getDataItemIfRegExFind(
		String regex, String dataStr, boolean throwExceptionIfNotFound)
		throws InterpreterException {
		return getDataItemIfRegExAux(regex, 
			dataStr, false, throwExceptionIfNotFound);
	}
		
	public static String[] getDataItemsIfRegExMatches(
		String regex, String dataStr, boolean throwExceptionIfNotFound)
		throws InterpreterException {
		return getDataItemsIfRegExAux(regex, dataStr, 
			true, throwExceptionIfNotFound);
	}
	
	public static String[] getDataItemsIfRegExFind(
		String regex, String dataStr, boolean throwExceptionIfNotFound)
		throws InterpreterException {
		return getDataItemsIfRegExAux(regex, dataStr, 
			false, throwExceptionIfNotFound);
	}
	
	private static String getDataItemIfRegExAux(
		String regex, String dataStr, 
		boolean onlyMatches, boolean throwExceptionIfNotFound) 
		throws InterpreterException {
		String item = null;
		String[] items = getDataItemsIfRegExAux(regex, 
			dataStr, onlyMatches, throwExceptionIfNotFound);
		if (items != null) {
			if (items.length != 1) {
				throw new InterpreterException(
					"found more than one item.");
			} else {
				item = items[0];
			}
		}
		return item;
	}
	
	private static String[] getDataItemsIfRegExAux(
		String regex, String dataStr, 
		boolean onlyMatches, boolean throwExceptionIfNotFound) 
		throws InterpreterException {
		if (StringUtils.isEmpty(regex)) {
			throw new IllegalArgumentException("regex must not be null.");
		}
		if (StringUtils.isEmpty(dataStr)) {
			throw new IllegalArgumentException("dataStr must not be null.");
		}
		String[] dataItems = null;
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(dataStr);
		boolean regExFound = onlyMatches ? matcher.matches() : matcher.find();  
		if (!regExFound && throwExceptionIfNotFound) {
			throw new InterpreterException("data string '" + dataStr + 
				"' does not match to regular expression '" + regex + 
				"' using '" + (onlyMatches ? "matches()" : "find()") + "'.");
		}
		if (regExFound && (matcher.groupCount() <= 0)) {
			throw new InterpreterException(
				"no data items found while matching data string '" + dataStr +
				"' to regular expression '" + regex + "'.");
		}				
		if (regExFound) {
			dataItems = new String[matcher.groupCount()];
		
			for (int i=1; i <= matcher.groupCount(); i++) {
				String val = matcher.group(i);				
				dataItems[i-1] = StringUtils.isEmpty(val) ? null : val;
			}
		}
		return dataItems;
	}
}
