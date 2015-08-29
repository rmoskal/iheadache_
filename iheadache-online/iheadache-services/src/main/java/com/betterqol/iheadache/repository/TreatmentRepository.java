package com.betterqol.iheadache.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.model.Treatment;
import com.betterqol.iheadache.resource.TreatmentResource;

@Component(value="treatmentRepository")
@Path(TreatmentRepository.URL)
@RolesAllowed(value = { "ROLE_USER" })
public class TreatmentRepository extends CouchDbRepositorySupport<Treatment> implements TreatmentResource {
	
	@Autowired
	public TreatmentRepository(@Qualifier("headacheDatabase") CouchDbConnector db) {
		
		super(Treatment.class, db);
		initStandardDesignDocument();
	}
	
	public String test() {
		
		return "test";
	}
	
	public  Treatment getPrototype() {
		
		return new Treatment("The name", "Pill", "350 mg", true);
	}
	
	@GenerateView
	@Override
	public List<Treatment> getAll() {
		
		ViewQuery q = createQuery("all").descending(true).
				designDocId("_design/Treatment")
				.includeDocs(true);
		return db.queryView(q, Treatment.class);
	}
	
	
	
	@Override
	public String create(Treatment o) {
		
		super.add(o);
		return o.getId();
	}


	@Override
	public void delete(String id) {
		
		super.remove(get(id));
	}


	@Override
	public void update(Treatment o) {
		
		super.update(o);
	}

	public void deleteAll() {

		List<Treatment> docs = getAll();
		List<BulkDeleteDocument> deleteList = new ArrayList<BulkDeleteDocument>();
		for (Treatment o: docs) {
			deleteList.add(BulkDeleteDocument.of(o));
		}
		db.executeBulk(deleteList);
	}


}
