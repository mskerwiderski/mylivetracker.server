package de.msk.mylivetracker.web.frontend.tracksoverview.command;

import java.io.Serializable;

import de.msk.mylivetracker.domain.sender.SenderVo;
import de.msk.mylivetracker.web.util.FmtUtils;

/**
 * SenderEntry.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public class SenderEntry implements Serializable {

	private static final long serialVersionUID = 4459904924545546422L;
	
	private String label;
	private SenderVo sender;
	
	
	/**
	 * @param label
	 * @param sender
	 */
	public SenderEntry(SenderVo sender) {
		this.label =FmtUtils.getSenderLabel(sender, false, FmtUtils.noValue);			
		this.sender = sender;
	}
	
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return the sender
	 */
	public SenderVo getSender() {
		return sender;
	}
	/**
	 * @param sender the sender to set
	 */
	public void setSender(SenderVo sender) {
		this.sender = sender;
	}	
}
