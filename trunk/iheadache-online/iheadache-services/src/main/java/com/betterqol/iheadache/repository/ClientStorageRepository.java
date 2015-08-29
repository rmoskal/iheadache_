package com.betterqol.iheadache.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Path;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.View;
import org.ektorp.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.UserAwareRepository;
import com.betterqol.iheadache.resource.ClientStorageResource;


@SuppressWarnings("rawtypes")
@Path(ClientStorageRepository.URL)
@Component
public class ClientStorageRepository extends UserAwareRepository<Map> implements ClientStorageResource {

	public static final String URL = "/app/service/storage";	
	public static final String KEY = "key";
	public static final String OWNER = "owner";

	
@Autowired
public ClientStorageRepository(CouchDbConnector db) {
	super(Map.class, db);
	initStandardDesignDocument();
}

public String test () {
	
	return this.getClass().getName();
}


@Override
public void insert(String key,String items)  {

	insertSingleton( getUser().getUserPk(), key,items);
	
}

@Override
public void delete(String key) {
	delete(getUser().getUserPk(), key );
	
}

@Override
public String fetch(String key) throws JSONException {

	return fetch(getUser().getUserPk(),key).toString();
}

public void insertSingleton(String owner, String key,
		String items) {
	
	Assert.isTrue(owner != null,"Map needs an owner");
	Assert.isTrue(key != null,"Map needs a key");
	_insert(owner, key, items, makeId(owner,key));
}

public void insertMulti(String key,String items)  {

	insertMulti(getUser().getUserPk(), key, items);
}

public void insertMulti(String owner, String key,
		String items) {
	
	Assert.isTrue(owner != null,"Map needs an owner");
	Assert.isTrue(key != null,"Map needs a key");
	_insert(owner, key, items, UUID.randomUUID().toString());
}

public void _insert(String owner, String key,
		String items, String pk) {
	

	Map<String, Object> doc = new  HashMap<String, Object>();
	doc.put(OWNER, owner);
	doc.put(KEY, key);;

		//JSONObject payload = new JSONObject(items);
    	//log.debug(payload.toString());
		doc.put("data", items);

	doc.put("_id",  pk);
	Map data = null;
	if (! contains(pk))
		db.update(doc);	
	else {
		data = get(pk);
		data.putAll(doc);
		db.update(data);
	}
}


/* (non-Javadoc)
 * @see com.betterqol.iheadache.resources.impl.ClientStorage#delete(java.lang.String, java.lang.String)
 */

public void delete(String owner, String key ) {
	
	Map o = db.get(Map.class, makeId(owner,key));
	if (o != null)
		db.delete(o);
}



public JSONObject fetch(String owner,String key) throws JSONException {
	return new JSONObject(get(makeId(owner,key)).get("data").toString());
	
}

@View(name = "fetch_multi", map = "function(doc) { if(doc.owner) {emit([doc.owner,doc.key], doc); } } ")
public  List<Map> fetchMulti(String owner,String key) {	

	return this.queryView(("fetch_multi"), ComplexKey.of(owner,key));
	
}


@View(name = "fetch_state_user", map = "function(doc) { if(doc.owner) {emit(doc.owner, doc); } }")
public String findByUser() {
	List<Map> source = queryView("fetch_state_user", getUser().getUserPk());
	JSONArray results = new JSONArray();
	for (Map each: source){
		JSONObject p = new JSONObject(each);
		results.put(p);
	}
	
	return results.toString();	
}

public  String fetchMulti(String key) throws JSONException {
	
	List<Map> source = fetchMulti(getUser().getUserPk(),key);
	JSONArray results = new JSONArray();
	for (Map each: source){
		JSONObject p = new JSONObject(each.get("data").toString());
		results.put(p);
	}
	
	return results.toString();		
	
}



private static String makeId(String owner, String key) {
	return owner + "_" + key;
}



public void deleteAll() {

	RepositoryHelpers.bulkDelete(db,  getAll());

}


}
