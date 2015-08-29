package com.betterqol.iheadache.repository;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Path;

import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.util.Assert;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.UserAwareRepository;
import com.betterqol.iheadache.model.PTUsage;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.resource.UserResource;

@Component(value="userRepository")
@Path(UserResource.URL)
public class UserInformationRepository extends UserAwareRepository<UserInformation>
implements UserResource {
	
	@Autowired
	public UserInformationRepository(@Qualifier("headacheDatabase") CouchDbConnector db) {
		super(UserInformation.class, db);
		initStandardDesignDocument();
	}
	
	@Autowired
	PTRepository ptRepo;

	@Override
	public String test() {
		return this.getClass().getName();
	}

	@Override
	public String create(UserInformation o) {
		
		String pk = UUID.randomUUID().toString();
		o.setId(pk);
		super.add(o);
		return pk;
	}
	
	public void createForParent(String pk, String name) {
			
		UserInformation ui = new UserInformation();
		ui.setId("u_" + pk);
		ui.setName(name);
		super.add(ui);

	}
	
	public void createTestUser(UserInformation o) {
		super.add(o);
	}
	
	public void update(UserInformation o) {
		super.update(o);
	}

	@Override
	public void delete(String pk) {
		
		UserInformation o = super.get(pk);
		if (o == null)
			throw new RuntimeException();
		
		RepositoryHelpers.bulkDelete(db, ptRepo.findByUser(o.getId()));
		super.remove(o);
	}
	
	@GenerateView
	@Override
	public List<UserInformation> getAll() {
		ViewQuery q = createQuery("all").descending(true).includeDocs(true);
		return db.queryView(q, UserInformation.class);
	}
	
	public void deleteAll() {
		RepositoryHelpers.bulkDelete(db,  getAll());

	}
	
	public PTUsage addPTUsage(PTUsage o) {
		Assert.notNull(o, "PTUsage may not be null");
		Assert.hasText(o.getUser(), "PTUsage must have a user");
		db.create(o);
		return o;
	}
	

	@GenerateView
	public UserInformation findByName(String user) {
		List<UserInformation> res = queryView("by_name", user);
		return res.size() >0 ? res.get(0) : null;
	}
	
	public UserInformation getCurrent() {
		return this.get("u_"+ this.getUser().getUserPk());
	}


}
