package com.betterqol.iheadache.security;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.repository.HealthcarePrincipalRepository;
import com.google.common.collect.Lists;

@Component (value= "hcp_userService")
public class HcpUserService implements UserDetailsService {
	
	
	@Autowired
	private HealthcarePrincipalRepository hcprepo;

	//@Override
	public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException, DataAccessException {
		

		HealthcarePrincipal h_user = hcprepo.findByName(username.toLowerCase(Locale.ENGLISH));
		
		 if(h_user==null)
			 h_user = hcprepo.findByName(username);
		
		if (h_user==null) 
			throw new UsernameNotFoundException(String.format("%s not found", username));
		if (!h_user.getApproved())
			throw new UsernameNotFoundException(String.format("%s not not yet approved", username));
		return new HeadacheSecurityPrincipal(h_user.getId(), h_user.getName(), h_user.getPassword(), 
					true, true, true, true, Lists.newArrayList(new GrantedAuthorityImpl("ROLE_USER")));
		
	}

}
