package com.betterqol.iheadache;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;

import com.betterqol.iheadache.security.HeadacheSecurityPrincipal;
import com.google.common.collect.Lists;

/**
 * Subclass of the couchdbrepository that knows about the currently logged in user
 * @author rob
 *
 * @param <T>
 */
public class UserAwareRepository<T> extends CouchDbRepositorySupport<T> {

	public SecurityContext securityContext;

	protected UserAwareRepository(Class<T> type, CouchDbConnector db) {
		super(type, db);
	}

	@Context
	public void setSecurityContext(SecurityContext sc) {
		securityContext = sc;
	}

	public HeadacheSecurityPrincipal getUser() {

		try {
			if (securityContext != null)
				if (securityContext.getUserPrincipal() != null)
					return (HeadacheSecurityPrincipal) SecurityContextHolder
							.getContext().getAuthentication().getPrincipal();

			return new HeadacheSecurityPrincipal("test", "test", "test", true,
					true, true, true,
					Lists.newArrayList(new GrantedAuthorityImpl("ROLE_USER")));
		} catch (Exception e) {

			return new HeadacheSecurityPrincipal("test", "test", "test", true,
					true, true, true,
					Lists.newArrayList(new GrantedAuthorityImpl("ROLE_USER")));
		}

	}

}
