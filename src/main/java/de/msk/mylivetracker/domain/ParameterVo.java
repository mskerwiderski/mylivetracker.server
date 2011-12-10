package de.msk.mylivetracker.domain;

import java.io.Serializable;

/**
 * ParameterVo.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class ParameterVo implements Serializable {

	private static final long serialVersionUID = 4548450905859675892L;
	
	private String name;
	private String value;
		
	public ParameterVo() {
	}
	
	/**
	 * @param name
	 * @param value
	 */
	public ParameterVo(String name, String value) {
		this.name = name;
		this.value = value;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}	
}
