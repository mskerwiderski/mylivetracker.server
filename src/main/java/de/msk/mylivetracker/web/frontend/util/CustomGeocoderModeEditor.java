package de.msk.mylivetracker.web.frontend.util;

import java.beans.PropertyEditorSupport;

import de.msk.mylivetracker.domain.user.GeocoderMode;

/**
 * CustomGeocoderModeEditor.
 * 
 * @author michael skerwiderski, (c)2012
 * 
 * @version 000
 * 
 * history
 * 000 initial 2012-09-23
 * 
 */
public class CustomGeocoderModeEditor extends PropertyEditorSupport {
	
	public CustomGeocoderModeEditor() {
	}
	
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		setValue(GeocoderMode.valueOf(text));				
	}

	@Override
	public String getAsText() {
		return ((GeocoderMode)getValue()).name();
	}
}
