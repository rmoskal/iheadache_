package com.betterqol.iheadache.repository;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Path;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.ektorp.support.Views;
import org.ektorp.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.UserAwareRepository;
import com.betterqol.iheadache.model.PTUsage;
import com.betterqol.iheadache.resource.PTUsageResource;
import com.google.common.collect.Lists;


@Component(value="ptRepository")
@Path(PTUsageResource.URL)
public class PTRepository  extends UserAwareRepository<PTUsage> implements PTUsageResource {
	
	@Autowired
	public PTRepository(@Qualifier("headacheDatabase") CouchDbConnector db) {
		super(PTUsage.class, db);
		initStandardDesignDocument();
	}
	
	@Views({
		@View( name = "ptusage_byDate", file = "ptusage_byDate.json"),
		@View( name = "ptusage_active_byDate", file = "ptusage_active_byDate.json"),
		@View( name = "ptusage_inactive_byDate", file = "ptusage_inactive_byDate.json"),
		@View( name = "ptusage_active_until_Date", file = "ptusage_active_until_Date.json")
		
		})
	
	
	public PTUsage add2(PTUsage o) {
		Assert.notNull(o, "PTUsage may not be null");
		o.setUser(this.getUser().getUserPk());
		o.setRevision(null);
		o.setId(null);
		db.create(o);
		return o;
	}
	
	public void add(PTUsage o) {
		Assert.notNull(o, "PTUsage may not be null");

		o.setId(null);
		db.create(o);

	}
	
	public void deleteForUser(String userId) {

		List<BulkDeleteDocument> bulkDocs = Lists.newArrayList();
		for (PTUsage h:findByUser(userId)){
			bulkDocs.add(BulkDeleteDocument.of(h));
		}
		db.executeBulk(bulkDocs);
	}
	
	public void createBulk(List<PTUsage> usages) {

		db.executeBulk(usages);
	}
	
	
	/**
	 * Extjs needs to update the revision number when updating a record so we send it back
	 */
	public PTUsage update2(PTUsage o) {
		
		Assert.notNull(o, "PTUsage may not be null");
		Assert.hasText(o.getUser(), "PTUsage must have a user");
		db.update(o);
		return o;
	}
	
	public PTUsage reactivate(PTUsage o) {
		PTUsage res = this.get(o.getId());
		res.setEnd(null);
		res.setStopReason(null);
		db.update(res);
		return res;
	}
	
	
	@GenerateView
	public List<PTUsage> findByUser(String user) {
		return queryView("by_user", user);
	}
	
	public PTUsage deletePTUsage(String id) {

		PTUsage o = db.get(PTUsage.class, id);
		db.delete(o);
		return o;
	}
	
	public PTUsage deletePTUsage2(String id) {

		PTUsage o = db.get(PTUsage.class, id);
		db.delete(o);
		return o;
	}

	@GenerateView
	@Override
	public List<PTUsage> getAll() {
		
		ViewQuery q = createQuery("all").descending(true).includeDocs(true)
				.designDocId("_design/PTUsage");
		return db.queryView(q, PTUsage.class);
	}
	
	public void deletAll(){
		
		RepositoryHelpers.bulkDelete(db, getAll());
	}
	
	public List<PTUsage> getActive() {
		return getActive(this.getUser().getUserPk());
	}
	
	public List<PTUsage> getActive(String userId) {
		
		return db.queryView(_constructQuery(userId, new Date(0), new Date(), "ptusage_active_byDate"),
				PTUsage.class);
	}
	
	public List<PTUsage> getActiveandFuture() {
		return getActiveandFuture(this.getUser().getUserPk());
	}
	
	
	public List<PTUsage> getActiveandFuture(String userId) {
		Calendar c = Calendar.getInstance(); 
		c.add( Calendar.YEAR, 1 );  
		return db.queryView(_constructQuery(userId, new Date(0), c.getTime(), "ptusage_active_until_Date"),
				PTUsage.class);
	}
	
	public List<PTUsage> getInactive() {
		return getInactive(this.getUser().getUserPk());
	}
	
	public List<PTUsage> getInactive(String userId) {
		
		return db.queryView(_constructQuery(userId, new Date(0), new Date(), "ptusage_inactive_byDate"),
				PTUsage.class);		
	}

	public List<PTUsage> getDateRange(Date startDate,
			Date endDate) {
		
		return getDateRange(this.getUser().getUserPk(), startDate, endDate);
	}
	
	public List<PTUsage> getDateRange(String userId, Date startDate,
			Date endDate) {

		return db.queryView(_constructQuery(userId, startDate, endDate, "ptusage_byDate"),
				PTUsage.class);
	}
	
	public List<Map>  getDateRangeDTO(String userId, Date startDate,
			Date endDate) throws IOException {

		   return db.queryView(_constructQuery(userId,
				startDate, endDate, "ptusage_byDate"),Map.class);
	}
	
	public List<Map>  getActiveUntilDTO(String userId,
			Date endDate) throws IOException {

		   return db.queryView(_constructQuery(userId,
				 endDate, "ptusage_active_byDate"),Map.class);
	}
	
	public List<Map> getInactiveDTO(String userId,Date startDate,
			Date endDate) {
		
		return db.queryView(_constructQuery(userId, startDate, endDate, "ptusage_inactive_byDate"),
				Map.class);		
	}
	
	
	public List<Map>  getUntilDTO(String userId,
			Date endDate) throws IOException {

		   return db.queryView(_constructQuery(userId,
				 endDate, "ptusage_byDate"),Map.class);
	}

	private static ViewQuery _constructQuery(String userId, Date start, Date end, String index) {
		return new ViewQuery().designDocId("_design/PTUsage")
				.viewName(index).includeDocs(true).
				startKey(RepositoryHelpers.createDateKey(userId, start))
				.endKey(RepositoryHelpers.createDateKey(userId, end));
	}
	
	
	private static ViewQuery _constructQuery(String userId, Date end, String index) {
		return new ViewQuery().designDocId("_design/PTUsage")
				.viewName(index).includeDocs(true)
				.endKey(RepositoryHelpers.createDateKey(userId, end));
	}


}
