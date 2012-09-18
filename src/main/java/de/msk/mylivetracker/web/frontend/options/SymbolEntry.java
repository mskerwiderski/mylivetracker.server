package de.msk.mylivetracker.web.frontend.options;

import javax.servlet.http.HttpServletRequest;

import de.msk.mylivetracker.domain.sender.SenderSymbolVo;
import de.msk.mylivetracker.web.util.WebUtils;

/**
 * SymbolEntry.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-09-01
 * 
 */
public class SymbolEntry {

	private SenderSymbolVo symbol;
	private String label;
	
	public SymbolEntry(HttpServletRequest request, SenderSymbolVo symbol) {
		this.symbol = symbol;
		this.label =  WebUtils.getMessage(
			request, symbol.getMsgCode());
	}
	
	/**
	 * @return the symbol
	 */
	public SenderSymbolVo getSymbol() {
		return symbol;
	}
	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}	
}
