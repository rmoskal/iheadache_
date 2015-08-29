package com.betterqol.iheadache;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.codehaus.jackson.JsonNode;
import org.ektorp.CouchDbConnector;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.repository.HeadacheRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class IHeadacheDataLoaderTest {
	
	@Autowired
	@Qualifier("headacheDatabase")CouchDbConnector db;
	
	@Test
	public void testWithIdId() {
		
	Reader reader =  new StringReader("[{\"_id\": \"PAIN_LOCATION\",\"description\": \"Right Temple\"}]");
	 IHeadacheDataLoader loader = new IHeadacheDataLoader(db){
		 protected void createDocument(JsonNode n, String id) {
				assertNotNull(n);
			}
	 };
	 
	 loader.load(reader);
		
	}
	
	
	@Test
	public void testWithNullId() {
		
	Reader reader =  new StringReader("[{\"lookupType\": \"PAIN_LOCATION\",\"description\": \"Right Temple\"}]");
	 IHeadacheDataLoader loader = new IHeadacheDataLoader(db){
		 protected void createDocument(JsonNode n, String id) {
				assertEquals("PAIN_LOCATION_RIGHT_TEMPLE",id);
			}
	 };
	 
	 loader.load(reader);
		
	}

	
	@Test
	public void testWithLongNull() {
		
	Reader reader =  new StringReader("[{\"lookupType\": \"PAIN_LOCATION\",\"description\": \"Right Temple pilot here we go and then what foo\"}]");
	 IHeadacheDataLoader loader = new IHeadacheDataLoader(db){
		 protected void createDocument(JsonNode n, String id) {
				assertEquals("PAIN_LOCATION_RIGHT_TEMPLE_PI",id);
			}
	 };
	 
	 loader.load(reader);
		
	}
	public class MutableInteger { public int value; }
	
	@Test
	public void testDuplicateId() {
		
	Reader reader =  new StringReader("[{\"_id\": \"1\",\"description\": \"Right Temple\"},{\"_id\": \"1\",\"description\": \"Left Temple\"}]");
	final MutableInteger counter = new MutableInteger(); 
	IHeadacheDataLoader loader = new IHeadacheDataLoader(db){
		 protected void createDocument(JsonNode n, String id) {
				++ counter.value;
			}
	 };
	 
	 loader.load(reader);
	 assertEquals(1, counter.value);
		
	}
	
	
	@Test
	public void testFromFileSystem() throws IOException {
		
	Resource r = new ClassPathResource("init_lookup_data.json");
	
	final MutableInteger counter = new MutableInteger();
	 IHeadacheDataLoader loader = new IHeadacheDataLoader(db){
		 protected void createDocument(JsonNode n, String id) {
			 ++ counter.value;
			}
	 };
	 

	 loader.setDataPath(r);
	 loader.init();
	 assertEquals(2, counter.value);
	}
	
	
	@Test
	public void testProductionData() throws IOException {
		
	Resource r = new ClassPathResource("init_lookup_data_production.json");
	
	final MutableInteger counter = new MutableInteger();
	 IHeadacheDataLoader loader = new IHeadacheDataLoader(db){
		 protected void createDocument(JsonNode n, String id) {
			 ++ counter.value;
			}
	 };
	 

	 loader.setDataPath(r);
	 loader.init();
	 assertEquals(88, counter.value);  // The number of records in the json file
	}
	
	public static void persistSomeHeadaches(HeadacheRepository repo, DateTime... dates) {
		for (DateTime dt: dates) {
			Headache o = TestHelpers.createMinHeadache(dt.toDate(), "foo");
			o.setKind(new LookupDTO("MIGRAINE","MIGRAINE"));
			repo.create(o);
		}
	}


}
