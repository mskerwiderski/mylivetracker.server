package de.msk.mylivetracker.web.frontend.util;

import java.beans.PropertyEditorSupport;

import de.msk.mylivetracker.domain.sender.SenderSymbol;

/**
 * CustomSenderSymbolEditor.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-09-08
 * 
 */
public class CustomSenderSymbolEditor extends PropertyEditorSupport {
	
	public CustomSenderSymbolEditor() {
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(SenderSymbol.getById(text));				
	}

	@Override
	public String getAsText() {
		return ((SenderSymbol)getValue()).getId();
	}
}
