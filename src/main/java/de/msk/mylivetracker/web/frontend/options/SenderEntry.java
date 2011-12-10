package de.msk.mylivetracker.web.frontend.options;

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
public class SenderEntry {

	private SenderVo sender;
	private String label;
	
	/**
	 * @param sender
	 * @param label
	 */
	public SenderEntry(SenderVo sender, String label) {
		this.sender = sender;
		if (sender == null) {
			this.label = label;
		} else {
			this.label = FmtUtils.getSenderLabel(sender, true, FmtUtils.noValue);
		}
	}
	
	/**
	 * @return the sender
	 */
	public SenderVo getSender() {
		return sender;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}	
}
