package de.msk.mylivetracker.web.uploader.processor;

/**
 * IPasswordEncoder.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IPasswordEncoder {

	public String encode(String plainPassword);
}
