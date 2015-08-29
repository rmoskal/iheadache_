package com.betterqol.iheadache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.ektorp.CouchDbConnector;
import org.ektorp.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.google.common.io.Files;

/***
 * Loads the couch database with data
 * @author rob
 *
 */
public class IHeadacheDataLoader  {
	
	private Resource dataPath;


	private final static Logger LOG = LoggerFactory.getLogger(IHeadacheDataLoader.class);
	
	private final ObjectMapper objectMapper;
	protected final CouchDbConnector db;


	public IHeadacheDataLoader( CouchDbConnector db) {
		this(db, new ObjectMapper());
	}
	
	public IHeadacheDataLoader( CouchDbConnector db, ObjectMapper objectMapper) {
		this.db = db;
		this.objectMapper = objectMapper;
	}
	
	public void init() throws FileNotFoundException, IOException{
		
		load(Files.newReader(dataPath.getFile(), Charset.defaultCharset()));
	}
	/**
	 * Reads documents from the reader and stores them in the database.
	 * @param in
	 */
	public void load(Reader in) {
		try {
			doLoad(in);
		} catch (Exception e) {
			throw Exceptions.propagate(e);
		}
	}

	private void doLoad(Reader in) throws IOException, JsonParseException,
			JsonMappingException {
		Set<String> allIds = new HashSet<String>(db.getAllDocIds());
		JsonNode jn = objectMapper.readValue(in, JsonNode.class);
		System.out.println(jn);

		for (Iterator<JsonNode> i = jn.getElements(); i.hasNext();) {
			JsonNode n = i.next();
			String id = null;
			if (n.has("_id"))
				id = n.get("_id").getTextValue();
			
			if (id==null) {
				
				id = UUID.randomUUID().toString();
				if (n.has("lookupType")) {
					
					id = n.get("lookupType").getTextValue().toUpperCase();
					id = id + "_" + n.get("description").getTextValue().replace(" ", "_").toUpperCase();
					if (id.length()>30)
						id = id.substring(0, 29);
				}
					
			}
			
			if (!allIds.contains(id)) {
				LOG.info("adding {} to database", id);
				allIds.add(id);
				createDocument(n, id);
			}
		}
	}
	/**
	 * Can be overidden in order to customize document creation.
	 * @param n
	 * @param id
	 */
	protected void createDocument(JsonNode n, String id) {
		db.create(id, n);
	}
	
	
    public static String makeId(JsonNode n, String id){
    	
    	return null;
    	
    }
    
	public void setDataPath(Resource dataPath) {
		this.dataPath = dataPath;
	}


}
