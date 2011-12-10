package de.msk.mylivetracker.web.options;

import java.io.Serializable;

/**
 * IntOptionDsc.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class IntOptionDsc implements Comparable<IntOptionDsc>, Serializable {

	private static final long serialVersionUID = 8903690148384284441L;
	
	private Integer value;
	private String label;	
	
	/**
	 * @param label
	 * @param value
	 */
	public IntOptionDsc(Integer value, String label) {		
		this.value = value;
		this.label = label;
	}
	
	/**
	 * @return the value
	 */
	public Integer getValue() {
		return value;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntOptionDsc other = (IntOptionDsc) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	public int compareTo(IntOptionDsc o) {
		return this.label.compareTo(o.label);
	}	
}
