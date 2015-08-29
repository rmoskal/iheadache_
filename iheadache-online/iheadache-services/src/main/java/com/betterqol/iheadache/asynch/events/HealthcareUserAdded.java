package com.betterqol.iheadache.asynch.events;

import org.springframework.context.ApplicationEvent;
import com.betterqol.iheadache.model.HealthcarePrincipal;

public class HealthcareUserAdded extends ApplicationEvent {

	private HealthcarePrincipal principal;

	
	public HealthcareUserAdded(Object source, HealthcarePrincipal p) {
		super(source);
		principal = p;
	}
	
	public HealthcarePrincipal getPrincipal() {
		return principal;
	}

}
