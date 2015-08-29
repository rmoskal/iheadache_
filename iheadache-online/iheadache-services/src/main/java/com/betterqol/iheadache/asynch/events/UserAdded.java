package com.betterqol.iheadache.asynch.events;

import org.springframework.context.ApplicationEvent;
import com.betterqol.iheadache.model.UserPrincipal;

public class UserAdded extends ApplicationEvent{
	
	private UserPrincipal principal;

	public UserAdded(Object source, UserPrincipal p) {
		super(source);
		this.principal = p;
	}

	public UserPrincipal getPrincipal() {
		return principal;
	}


	

}
