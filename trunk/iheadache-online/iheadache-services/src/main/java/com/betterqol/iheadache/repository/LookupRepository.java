package com.betterqol.iheadache.repository;

import java.util.List;

import javax.ws.rs.Path;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.GenerateView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.model.CouchLookup;
import com.betterqol.iheadache.resource.LookupResource;


@Component(value="lookupRepository")
@Path(LookupRepository.URL)
public class LookupRepository extends CouchDbRepositorySupport<CouchLookup> implements LookupResource{
	
	public static final String URL = "/app/service/lookups";
	
	@Autowired
	public LookupRepository(@Qualifier("headacheDatabase") CouchDbConnector db) {
		super(CouchLookup.class, db);
		initStandardDesignDocument();
	}
	
	@GenerateView
	public List<CouchLookup> findByLookupType(CouchLookup.Kind type) {
	    return queryView("by_lookupType", type.toString());
	}
	
	
	public String  findByLookupType2(CouchLookup.Kind type) throws JSONException {
		JSONArray results = new JSONArray();
		List<CouchLookup> items = findByLookupType(type);
		for (CouchLookup item : items) {
			
			JSONObject o  = new JSONObject();
			o.put("id", item.getId());
			o.put("description", item.getDescription());
			o.put("lookupType", item.GetLookupType());
			results.put(o);
			
			
		}
		return results.toString();
	}
	
	

	
	public void deleteAllByType(CouchLookup.Kind type) {
		RepositoryHelpers.bulkDelete(db, findByLookupType(type));
	}
	
	
	@GenerateView @Override
	public List<CouchLookup> getAll() {
		ViewQuery q = createQuery("all").designDocId("_design/CouchLookup")
						.descending(true)
						.includeDocs(true);
		return db.queryView(q, CouchLookup.class);
	}
	
	
	public void deleteAll() {
		RepositoryHelpers.bulkDelete(db,  getAll());
	}

	@Override
	public String test() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String create(CouchLookup o) {
		super.add(o);
		return o.getId();
	}

	@Override
	public void delete(String pk) {
		super.remove(this.get(pk));
		
	}

	@Override
	public void update(CouchLookup o) {
		super.update(o);
		
	}

	@Override
	public CouchLookup getPrototype() {
		CouchLookup a = new CouchLookup(CouchLookup.Kind.TRIGGER);
		a.setDescription("This is the description");
		a.setId("pk");
		return a;
	}
	
	
	



}
