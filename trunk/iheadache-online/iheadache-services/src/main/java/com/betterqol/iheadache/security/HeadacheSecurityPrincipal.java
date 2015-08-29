package com.betterqol.iheadache.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class HeadacheSecurityPrincipal extends User {
	
	private String userPk;
	
	public HeadacheSecurityPrincipal(String userPk,String username, String password,
			boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		this.userPk = userPk;
	}

	public String getUserPk() {
		return userPk;
	}

	public void setUserPk(String userPk) {
		this.userPk = userPk;
	}



}
