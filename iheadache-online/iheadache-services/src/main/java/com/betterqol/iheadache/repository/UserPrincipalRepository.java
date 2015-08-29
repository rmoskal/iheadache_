package com.betterqol.iheadache.repository;

import java.util.List;
import java.util.UUID;

import org.ektorp.CouchDbConnector;
import org.ektorp.Page;
import org.ektorp.PageRequest;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import org.springframework.stereotype.Component;


import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.security.DuplicateUsernameException;

@Component(value="userPrincipalRepository")
public class UserPrincipalRepository extends CouchDbRepositorySupport<UserPrincipal>{

	@Autowired
	public UserPrincipalRepository(@Qualifier("headacheDatabase") CouchDbConnector db) {
		super(UserPrincipal.class, db);
		initStandardDesignDocument();
	}
	
	@Autowired
	private PasswordEncoder encoder;
	
	
	/**
	 * This function should be called bby all clients, it ensures no duplicate user names
	 * @param o
	 * @return
	 */
	public String create(UserPrincipal o) {
		
		if (null != findByName(o.getName()))
				throw new DuplicateUsernameException(String.format("UserInformation with name %s already exists", o.getName()));
			
		String pk = UUID.randomUUID().toString();
	    o.setPassword(encoder.encodePassword(o.getPassword(), null));
		o.setCreated(new DateTime());
		o.setId(pk);
		super.add(o);
		return pk;
	}
	
	@Override
	public void update(UserPrincipal o) {
		
		UserPrincipal existing = findByName(o.getName());
		if ((existing != null)&&(!existing.getId().equals(o.getId())))
			throw new DuplicateUsernameException(String.format("UserInformation with name %s already exists", o.getName()));
		
		super.update(o);
		
	}
	
	@GenerateView
	public UserPrincipal findByName(String user) {
		List<UserPrincipal> res = queryView("by_name", user);
		return res.size() >0 ? res.get(0) : null;
	}
	
	public void deleteAll() {
		RepositoryHelpers.bulkDelete(db,  getAll());

	}
	
	@GenerateView
	@Override
	public List<UserPrincipal> getAll() {
		ViewQuery q = createQuery("all").descending(true).includeDocs(true);
		return db.queryView(q, UserPrincipal.class);
	}
	
	
	
	public Page<UserPrincipal> getPaged(PageRequest pageRequest) {

		ViewQuery q = createQuery("all").descending(true).includeDocs(true);
		return db.queryForPage(q,pageRequest,UserPrincipal.class);
	}
	
	
	



}
