package de.msk.mylivetracker.web.uploader.interpreter.ecotec.yourtracks;

import de.msk.mylivetracker.web.uploader.processor.IPasswordEncoder;

/**
 * PasswordEncoder.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class PasswordEncoder implements IPasswordEncoder {

	/* (non-Javadoc)
	 * @see de.msk.mylivetracker.web.uploader.processor.IPasswordEncoder#encode(java.lang.String)
	 */
	@Override
	public String encode(String plainPassword) {
		StringBuilder sb = null;
		if (plainPassword != null) {
			sb = new StringBuilder();
			int len = plainPassword.length();
			for (int idx=len-1; idx>=0; idx--) {
				char c = plainPassword.charAt(idx);
				if (c >= '0' && c <= '9') {
					sb.append((char)(9 - (c - '0') + '0'));
				} else if (c >= 'a' && c <= 'z') {
					sb.append((char)(('z' - 'a') - (c - 'a') + 'A'));
				} else if (c >= 'A' && c <= 'Z') {
					sb.append((char)(('Z' - 'A') - (c - 'A') + 'a'));
				}
			}
		}
		return (sb != null) ? sb.toString() : null;
	}
}
