package com.betterqol.iheadache.repository;

import java.util.List;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.model.HcpUserAssociation;
import com.google.common.collect.Lists;

@Component
public class HcpUserAssociationRepository extends CouchDbRepositorySupport<HcpUserAssociation> {
	
	@Autowired
	public HcpUserAssociationRepository(@Qualifier("headacheDatabase") CouchDbConnector db) {
		super(HcpUserAssociation.class, db);
		initStandardDesignDocument();
	}
	
	public void deleteAll() {
		RepositoryHelpers.bulkDelete(db,  getAll());
	}
	
	
	
	/***
	 * This should be the only call used to make associations!
	 * @param userId
	 * @param hcpProviders
	 */
	public void create(String userId, List<String> hcpProviders) {
		deleteForUserId(userId);
		HcpUserAssociation o;
		for (String each: hcpProviders) {
			o = new HcpUserAssociation(userId, each);
			this.add(o);
		}
		
	}
	
	
	public void deleteForUserId(String user) {
		
		List<BulkDeleteDocument> bulkDocs = Lists.newArrayList();
		for (HcpUserAssociation h:findByUserId(user)){
			bulkDocs.add(BulkDeleteDocument.of(h));
		}
		db.executeBulk(bulkDocs);
		
	}
	
	public void deleteForHcpId(String user) {
		
		List<BulkDeleteDocument> bulkDocs = Lists.newArrayList();
		for (HcpUserAssociation h:findByHcpId(user)){
			bulkDocs.add(BulkDeleteDocument.of(h));
		}
		db.executeBulk(bulkDocs);
		
	}
	
	@GenerateView
	public List<HcpUserAssociation> findByUserId(String user) {
		return queryView("by_userId", user);
	}
	
	@GenerateView
	public List<HcpUserAssociation> findByHcpId(String user) {
		return queryView("by_hcpId", user);
	}

}
