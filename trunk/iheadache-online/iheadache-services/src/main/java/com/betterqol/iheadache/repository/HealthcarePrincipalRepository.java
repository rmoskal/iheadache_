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
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.security.DuplicateUsernameException;

@Component(value="healthcarePrincipalRepository")
public class HealthcarePrincipalRepository extends CouchDbRepositorySupport<HealthcarePrincipal>{
	
	
	@Autowired
	public HealthcarePrincipalRepository(@Qualifier("headacheDatabase") CouchDbConnector db) {
		super(HealthcarePrincipal.class, db);
		initStandardDesignDocument();
	}
	
	@Autowired
	private MessageDigestPasswordEncoder encoder;


	/**
	 * This function should be called by all clients, it ensures no duplicate user names
	 * @param o
	 * @return
	 */
	public String create(HealthcarePrincipal o) {
		
		if (null != findByName(o.getName()))
				throw new DuplicateUsernameException(String.format("UserInformation with name %s already exists", o.getName()));
			
		String pk = UUID.randomUUID().toString();
		o.setCreated(new DateTime());
	    o.setPassword(encoder.encodePassword(o.getPassword(), null));
		o.setId(pk);
		super.add(o);
		return pk;
	}
	
	

	
	
	@GenerateView
	public HealthcarePrincipal findByName(String user) {
		List<HealthcarePrincipal> res = queryView("by_name", user);
		return res.size() >0 ? res.get(0) : null;
	}
	
	
	@GenerateView
	public List<HealthcarePrincipal> findByApproved1(boolean state) {
		return queryView("by_approved1", String.valueOf(state));
	}
	
	public void deleteAll() {
		RepositoryHelpers.bulkDelete(db,  getAll());

	}
	
	@GenerateView
	@Override
	public List<HealthcarePrincipal> getAll() {
		ViewQuery q = createQuery("all").descending(true).includeDocs(true);
		return db.queryView(q, HealthcarePrincipal.class);
	}
	
	

	public Page<HealthcarePrincipal> getPaged(PageRequest pageRequest, boolean state) {

		ViewQuery q = createQuery("by_approved1").descending(true).
				key(String.valueOf(state)).includeDocs(true);
		return db.queryForPage(q,pageRequest,HealthcarePrincipal.class);
	}


}
