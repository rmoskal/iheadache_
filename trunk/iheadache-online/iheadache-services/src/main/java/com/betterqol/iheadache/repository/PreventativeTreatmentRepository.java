package com.betterqol.iheadache.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.model.PreventativeTreatment;
import com.betterqol.iheadache.resource.PreventativeTreatmentResource;

@Component(value="preventativeTreatmentRepository")
@RolesAllowed(value = { "ROLE_USER" })
public class PreventativeTreatmentRepository extends CouchDbRepositorySupport<PreventativeTreatment>
	implements PreventativeTreatmentResource {
	
	
	@Autowired
	public PreventativeTreatmentRepository(@Qualifier("headacheDatabase") CouchDbConnector db) {
		super( PreventativeTreatment.class, db);
		initStandardDesignDocument();
	}
	

	@GenerateView
	@Override
	public List<PreventativeTreatment> getAll() {
		
		ViewQuery q = createQuery("all").descending(true).
				designDocId("_design/PreventativeTreatment")
				.includeDocs(true);
		return db.queryView(q, PreventativeTreatment.class);
	}
	
	
	public void deleteAll() {

		List<PreventativeTreatment> docs = getAll();
		List<BulkDeleteDocument> deleteList = new ArrayList<BulkDeleteDocument>();
		for (PreventativeTreatment o: docs) {
			deleteList.add(BulkDeleteDocument.of(o));
		}
		db.executeBulk(deleteList);
	}

}
