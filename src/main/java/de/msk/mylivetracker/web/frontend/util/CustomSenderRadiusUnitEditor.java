package de.msk.mylivetracker.web.frontend.util;

import java.beans.PropertyEditorSupport;

import de.msk.mylivetracker.domain.sender.SenderRadiusUnit;

/**
 * CustomSenderRadiusUnitEditor.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-10-12
 * 
 */
public class CustomSenderRadiusUnitEditor extends PropertyEditorSupport {
	
	public CustomSenderRadiusUnitEditor() {
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(SenderRadiusUnit.valueOf(text));				
	}

	@Override
	public String getAsText() {
		return ((SenderRadiusUnit)getValue()).name();
	}
}
