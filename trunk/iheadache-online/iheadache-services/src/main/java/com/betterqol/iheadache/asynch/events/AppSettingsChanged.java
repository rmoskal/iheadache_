package com.betterqol.iheadache.asynch.events;

import org.springframework.context.ApplicationEvent;

import com.betterqol.iheadache.model.AppSettings;
import com.betterqol.iheadache.model.UserInformation;

/***
 * Event generated when the user preferences change.  Event handlers will wan to 
 * compare the new settings to the current ones to take action. It was created primarily to facilitate 
 * rescoring headaches when a user changes the related preferences.
 * @author rob
 *
 */
public class AppSettingsChanged extends ApplicationEvent{
	
	private UserInformation newSettings;
	private AppSettings oldSettings;
	

	public AppSettingsChanged(Object source, UserInformation newSettings, AppSettings oldSettings) {
		super(source);
		this.newSettings = newSettings;
		this.oldSettings = oldSettings;
	}

	public UserInformation getNewSettings() {
		return newSettings;
	}

	public AppSettings getOldSettings() {
		return oldSettings;
	}
}
