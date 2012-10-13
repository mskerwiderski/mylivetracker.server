package de.msk.mylivetracker.domain.sender;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

/**
 * SenderSwitches.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SenderSwitchesVo {
	
	private static Set<String> switchNamesSet = new HashSet<String>();
	
	public enum Switch {
		IgnoreSenderMileage("IgnoreSenderMileage"),
		IgnoreLocationValidFlag("IgnoreLocationValidFlag"),
		UseTimestampAsReceived("UseTimestampAsReceived"),
		SendNoResponse("SendNoResponse");
		
		private String name;
		
		private Switch(String name) {
			this.name = name;
			switchNamesSet.add(name);
		}
		public String getName() {
			return name;
		}		
		public static boolean exists(String switchValStr) {
			return switchNamesSet.contains(switchValStr); 
		}
	}
		
	private Set<Switch> switchesSet = new HashSet<Switch>();
	
	public static void main(String[] argv) {
		String switches = "IgnoreLocationValidFlag";
		System.out.println(SenderSwitchesVo.isValid(switches));
	}
	
	public SenderSwitchesVo(String switches) {
		if (!checkAndGetSwitches(switches, this.switchesSet)) {
			throw new IllegalArgumentException("invalid switches string: " + switches);
		}
	}
	
	public static boolean isValid(String switches) {
		return checkAndGetSwitches(switches, null);		
	}
	
	
	public static boolean checkAndGetSwitches(String switches, Set<Switch> switchesSet) {
		boolean res = true;
		if (StringUtils.isEmpty(switches)) return res;
		String[] switchValArr = StringUtils.split(switches, ',');
		if (switchValArr != null) {
			for (int i=0; (res == true) && (i < switchValArr.length); i++) {
				String switchValStr = switchValArr[i];
				switchValStr = StringUtils.remove(switchValStr, " ");
				if (StringUtils.isEmpty(switchValStr)) {
					res = false;
				} else {					
					if (!Switch.exists(switchValStr)) {
						res = false;					
					} else if (switchesSet != null) {
						Switch switchVal = Switch.valueOf(switchValStr);
						switchesSet.add(switchVal);
					}
				}
			}
		}		
		return res;
	}
	
	public static String supported() {
		StringBuffer buf = new StringBuffer();
		for (Switch val : Switch.values()) {
			buf.append(val.getName()).append(", ");
		}
		return StringUtils.left(buf.toString(), buf.length()-2);
	}

	public boolean useTimestampAsReceived() {
		return this.switchesSet.contains(Switch.UseTimestampAsReceived);
	}
	
	public boolean ignoreSenderMileage() {
		return this.switchesSet.contains(Switch.IgnoreSenderMileage);
	}
	
	public boolean ignoreLocationValidFlag() {
		return this.switchesSet.contains(Switch.IgnoreLocationValidFlag);
	}
	
	public boolean sendNoResponse() {
		return this.switchesSet.contains(Switch.SendNoResponse);
	}
}
