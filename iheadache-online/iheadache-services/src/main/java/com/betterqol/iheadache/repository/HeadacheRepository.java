package com.betterqol.iheadache.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.codehaus.jackson.JsonNode;
import org.ektorp.BulkDeleteDocument;
import org.ektorp.CouchDbConnector;
import org.ektorp.Page;
import org.ektorp.PageRequest;
import org.ektorp.ViewQuery;
import org.ektorp.support.GenerateView;
import org.ektorp.support.View;
import org.ektorp.support.Views;
import org.ektorp.util.Assert;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.UserAwareRepository;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.HeadacheTreatment;
import com.betterqol.iheadache.model.Treatment;
import com.betterqol.iheadache.resource.HeadacheResource;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.ByteStreams;

@Component(value="headacheRepository")
@Path(HeadacheResource.URL)
@RolesAllowed(value = { "ROLE_USER" })
public class HeadacheRepository extends UserAwareRepository<Headache>
		implements HeadacheResource {



	@Autowired
	public HeadacheRepository(@Qualifier("headacheDatabase") CouchDbConnector db) {
		super(Headache.class, db);
		initStandardDesignDocument();
	}
	
	
	/** View Documents  **/
	@Views({

	@View(name = "by_date", map = "function(doc) { if(doc.kind) {emit([doc.user,Number(doc.start)], doc); } } "),
	@View( name = "headachedto_byDate", file = "headachedto_byDate.json")
	})

	public String test() {
		return "test";
	}

	public Headache getPrototype() {
		return TestHelpers.createFullHeadache(false, "test", null, null);
	}

	public String create(Headache h) {

		Assert.notNull(h.getStart());
		Assert.notNull(h.getEnd());
		Assert.notNull(h.getUser());
		h.setId(UUID.randomUUID().toString());
		super.add(h);
		return h.getId();
	}

	public void deleteAll() {
		RepositoryHelpers.bulkDelete(db, getAll());

	}

	@GenerateView
	public List<Headache> findByUser(String user) {
		return queryView("by_user", user);
	}

	public void update(Headache o) {
		Assert.notNull(o.getStart());
		Assert.notNull(o.getEnd());
		super.update(o);
	}

	public void delete(String pk) {

		Headache o = super.get(pk);
		if (o == null)
			throw new RuntimeException();
		super.remove(o);
	}
	
	
	public void deleteForUser(String userId) {

		List<BulkDeleteDocument> bulkDocs = Lists.newArrayList();
		for (Headache h:findByUser(userId)){
			bulkDocs.add(BulkDeleteDocument.of(h));
		}
		db.executeBulk(bulkDocs);
	}
	
	
	public void createBulk(List<Headache> headaches) {

		db.executeBulk(headaches);
		
	}



	@GenerateView
	@Override
	public List<Headache> getAll() {
		ViewQuery q = createQuery("all").descending(true).includeDocs(true)
				.designDocId("_design/Headache");
		return db.queryView(q, Headache.class);
	}


	public StreamingOutput getDateRange2(String userId, Date startDate,
			Date endDate) {

		final InputStream results = db.queryForStream(buildDateQuery(userId,
				startDate, endDate, "by_date"));
		return new StreamingOutput() {

			@Override
			public void write(OutputStream output) throws IOException,
					WebApplicationException {

				ByteStreams.copy(results, output);
				results.close();

			}

		};

	}
	
	public List<Headache> getDateRange(String userId, Date startDate,
			Date endDate) {
		
		return db.queryView(buildDateQuery(userId, startDate, endDate,"by_date"),
				Headache.class);
	}
	
	
	/*public List<Headache> getDateRangeDescending(String userId, Date startDate,
			Date endDate, int limit) {
		
		ViewQuery query = buildDateQuery(userId, endDate,startDate,"by_date");
		query.descending(true);
		query.limit(limit);
		return db.queryView(query,
				Headache.class);
	} */
	

	
	
	public Page<Headache> getPagedDateRange(String userId, Date startDate,
			Date endDate, PageRequest pageRequest, String queryName) {
		DateTime c = new DateTime(endDate);
		endDate = c.plusDays(1).toDate();
		ViewQuery query = buildDateQuery(userId, endDate,startDate,queryName);
		query.descending(true);
		query.includeDocs(true);
		return db.queryForPage(query,
				pageRequest,
				Headache.class);
	}
	

	
	public List<Map>  getDateRangeDTO(String userId, Date startDate,
			Date endDate) throws IOException {

		   return db.queryView(buildDateQuery(userId,
				startDate, endDate, "headachedto_byDate"),Map.class);
		
	}
	
	public List<JsonNode>  getDateRangeDTO2(String userId, Date startDate,
			Date endDate) throws IOException {

		   return db.queryView(buildDateQuery(userId,
				startDate, endDate, "headachedto_byDate"),JsonNode.class);
		
	}

	public ViewQuery buildDateQuery(String userId, Date startDate, Date endDate, String viewName) {
		
	
	    endDate = RepositoryHelpers.getEOD(endDate);
		
		return new ViewQuery().designDocId("_design/Headache")
				.viewName(viewName).startKey(RepositoryHelpers.createDateKey(userId, startDate))
				.endKey(RepositoryHelpers.createDateKey(userId, endDate));
	}
	
	
	public static  List<Treatment> getActualSymptoms(List<Headache>in) {
		HashMap<String, Treatment> res = Maps.newHashMap();
		
		for (Headache h: in)
			for (HeadacheTreatment t: h.getTreatments())
				res.put(t.getTreatment().getId(), t.getTreatment());			
		
		return new ArrayList(res.values());
	}

/** Added a bunch of functions for the API to ensure the user is logged in
 * 
 */
	@Override
	public String createForApp(Headache o) {
		
		if (!o.getUser().equals(getUser().getUserPk()))
			return null;
		
		return create(o);	
		
	}


	@Override
	public Headache getForApp(@PathParam("pk") String pk) {
	
		Headache h = get(pk);
		
		if (!h.getUser().equals(getUser().getUserPk()))
			return null;
		
		return h;
	}


	@Override
	public void deleteForApp(@PathParam("pk") String pk) {
		
		Headache o = get(pk);

		if (o == null)
			throw new RuntimeException();
		
		if (!o.getUser().equals(getUser().getUserPk()))
			return;
		
		remove(o);	
		
	}


	@Override
	public void updateForApp(Headache o) {
		
		if (!o.getUser().equals(getUser().getUserPk()))
			return;
		
		update(o);
		
	}

}
