package com.betterqol.iheadache.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.IHeadacheDataLoader;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.Headache;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class HeadacheRepositoryTest {
	
	@Autowired
	HeadacheRepository repo;
	
	@Autowired
	LookupRepository lookups;
	
	@Autowired
	TreatmentRepository treatments;
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
		 lookups.deleteAll();
		 treatments.deleteAll();
	 }
	
	@Autowired
	@Qualifier("headacheDatabase")CouchDbConnector db;
	

	
	@Test
	public void testWiring() {
		
		assertNotNull(repo);
	}
	
	@Test (expected=NullPointerException.class)
	public void testStartMissing(){
		Headache o = new Headache();
		repo.create(o);
	}
	
	@Test (expected=NullPointerException.class)
	public void testEndMissing(){
		Headache o = new Headache();
		o.setStart(new DateTime(new Date()));
		repo.create(o);
	}
	
	@Test
	public void testMinimum(){
		Date start = new Date();
		Headache o = TestHelpers.createMinHeadache(start, "foo");
		String pk = repo.create(o);
		
		Calendar c =  Calendar.getInstance();
		c.setTime(o.getStart().toDate());
		
		assertEquals(1,repo.getAll().size());
		Headache object = repo.get(pk);
		assertNotNull(object);
		assertEquals(o.getStart().toDate(), start);
	}
	
	

	@Test
	public void testFindByUser() {
		
		Date start = new Date();
		Headache o = TestHelpers.createMinHeadache(start, "foo");
		repo.create(o);
		
		o = TestHelpers.createMinHeadache(start, "hey");
		repo.create(o);
		
		assertEquals(2,repo.getAll().size());
				
		assertEquals(1,repo.findByUser("hey").size());
		
	}
	
	@Test
	public void testDeleteByUser() {
		
		Date start = new Date();
		Headache o = TestHelpers.createMinHeadache(start, "foo");
		repo.create(o);
		
		repo.deleteForUser("foo");
				
		assertEquals(0,repo.findByUser("foo").size());
		
	}
	
	@Test
	public void testCreateBulk() {
		
		Date start = new Date();
		ArrayList<Headache> hh = Lists.newArrayList(TestHelpers.createMinHeadache(start, "foo"),
				TestHelpers.createMinHeadache(start, "foo"));
		repo.createBulk(hh);
		assertEquals(2,repo.getAll().size());
		
	}

	/**
	 * Not really a test, more of a means for generating test data
	 */
	@Test
	public void createFull(){
		
		Headache o = TestHelpers.createFullHeadache(false, "test", null, null);
		String pk = repo.create(o);
		assertEquals(o.getId(),repo.get(pk).getId());
		
	}
	
	@Test
	public void testKeyCreation() {
		
		Calendar c =  new GregorianCalendar(2011,6,9);
		ComplexKey r1 = RepositoryHelpers.createDateKey("foo", c.getTime());
		assertEquals("foo",r1.toJson().get(0).toString());
		//assertEquals("1310184000000",r1.toJson().get(1).toString());
		
		
	}
	
	@Test
	public void testGetDateRange() {
		Calendar c =  new GregorianCalendar(2011,7,8);
		Headache o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		c.add(Calendar.DAY_OF_MONTH, 3);
	    o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		c.add(Calendar.MONTH, 1);
		o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		o = TestHelpers.createMinHeadache(new Date(), "another");
		repo.create(o);
		
		assertEquals(4,repo.getAll().size());
		 c =  new GregorianCalendar(2011,7,8);
		
		assertEquals (1,repo.getDateRange("foo",c.getTime(), c.getTime()).size());

	}
	
	@Test
	public void testGetDateRangeFrom0() {
		Calendar c =  new GregorianCalendar(2011,7,8);
		Headache o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		c.add(Calendar.DAY_OF_MONTH, 3);
	    o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		c.add(Calendar.MONTH, 1);
		o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		o = TestHelpers.createMinHeadache(new Date(), "foo");
		repo.create(o);

		assertEquals (4,repo.getDateRange("foo",new Date(0), new Date()).size());

	}
	
	
	@Test
	public void testGetDateRangeOrdinals() {
		Calendar c =  new GregorianCalendar(2011,7,8);
		Headache o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		c.add(Calendar.DAY_OF_MONTH, 3);
	    o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		c.add(Calendar.MONTH, 1);
		o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		
		List<Headache> results = repo.getDateRange("foo", new GregorianCalendar(2011,7,8).getTime(), c.getTime());
		assertEquals (3,results.size());
		assertTrue(results.get(0).getStart().isBefore(results.get(1).getStart()));
		

	}
/*	
	@Test
	public void testGetDateRangeReverse() {
		Calendar c =  new GregorianCalendar(2011,7,8);
		Headache o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		o.getSymptoms().clear();
		repo.create(o);
		c.add(Calendar.DAY_OF_MONTH, 3);
	    o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		c.add(Calendar.MONTH, 1);
		o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		assertEquals(3,repo.getAll().size());
		
		PageRequest pr = PageRequest.firstPage(12);
		
		Page<Headache> results = repo.getPagedDateRange("foo", new GregorianCalendar(2011,7,7).getTime(),c.getTime(),
				pr,"headache_no_symptoms");
		assertEquals (1,results.getRows().size());
		
		
	}
	
	@Test
	public void testGetDateRangeReverse2() {
		Calendar c =  new GregorianCalendar(2011,7,8);
		Headache o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		o.setSymptoms(null);
		repo.create(o);
		PageRequest pr = PageRequest.firstPage(12);
		
		Page<Headache> results = repo.getPagedDateRange("foo", new GregorianCalendar(2011,7,7).getTime(),c.getTime(),
				pr,"headache_no_symptoms");
		assertEquals (1,results.getRows().size());
		
	}
*/	
	
	@Test
	/**
	 * This is an important test.  Since we now use the millisecond value for 
	 * the headache index, we need a way to get all the headaches within a single day.
	 */
	public void testGetDateRange2() throws FileNotFoundException, IOException {
		
		Resource r = new ClassPathResource("init_lookup_data_production.json");
		IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		loader.setDataPath(r);

		loader.init();
		
		Calendar c =  new GregorianCalendar(2011,7,8);
		Headache o = TestHelpers.createFullHeadache(c, true, "foo", lookups,
				treatments);
		repo.create(o);

		
		assertEquals (1,repo.getDateRange("foo",c.getTime(), c.getTime()).size());

	}

	
	@Test
	public void testGetDateRangeDTO() throws IOException {
		Calendar c =  new GregorianCalendar(2011,7,8);
		Headache o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		c.add(Calendar.DAY_OF_MONTH, 3);
	    o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		c.add(Calendar.MONTH, 1);
		o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		repo.create(o);
		o = TestHelpers.createMinHeadache(new Date(), "another");
		repo.create(o);
		
		assertEquals(4,repo.getAll().size());
		 Calendar c2 =  new GregorianCalendar(2011,7,8);
		
		List<Map> res = repo.getDateRangeDTO("foo",c2.getTime(), c.getTime());
		assertEquals(3,res.size());

	}
	
	@Test
	public void testGetDateRangeDTO_2() throws IOException {
		GregorianCalendar c = new GregorianCalendar(2011,7,8);
		TestHelpers.persistSomeHeadaches(repo,"test",null,null,c);
		c = new GregorianCalendar(2011,7,8); //The calendar gets rolled in the helper
		List<Map> o = repo.getDateRangeDTO("test", c.getTime(), c.getTime());
		assertEquals(2,o.size());
		assertTrue(o.get(0).containsKey("id"));
		assertTrue(o.get(0).containsKey("start"));
		assertTrue(o.get(0).containsKey("end"));
		assertTrue(o.get(0).containsKey("kind"));
		assertTrue(o.get(0).containsKey("description"));

		//o.get(0).containsKey("description");
	
	}
	
	@Test
	public void testGetDateRangeDTO2() throws IOException {
		GregorianCalendar c = new GregorianCalendar(2011,7,8);
		TestHelpers.persistSomeHeadaches(repo,"test",null,null,c);
		c = new GregorianCalendar(2011,7,8); //The calendar gets rolled in the helper
		List<JsonNode> o = repo.getDateRangeDTO2("test", c.getTime(), c.getTime());
		assertEquals(2,o.size());
		assertTrue(o.get(0).has("id"));
		assertTrue(o.get(0).has("start"));
		assertTrue(o.get(0).has("end"));
		assertTrue(o.get(0).has("kind"));
		assertTrue(o.get(0).has("description"));

		//o.get(0).containsKey("description");
	
	}
	
	
	
	
	@Test
	public void testPersistSomeHeadaches() {
		
		TestHelpers.persistSomeHeadaches(repo, "test",null,null,new GregorianCalendar(2011,1,15));
		List<Headache> res = repo.getAll();
		assertEquals(4,res.size());
		assertEquals("IS_MOVEMENT",res.get(0).getSymptoms().get(0).getId());
		assertEquals("DISABILITY_QUESTION_1",res.get(0).getDisability().getResponses().get(0).getId());
		
	}
	
	@Test
	public void testLookupFilling() throws FileNotFoundException, IOException{
		
		 Resource r = new ClassPathResource("init_lookup_data_production.json");
		 IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		 loader.setDataPath(r);
		 
		 loader.init();
		
		TestHelpers.persistSomeHeadaches(repo, "test",lookups,treatments,new GregorianCalendar(2011,1,15));
		List<Headache> res = repo.getAll();
		assertEquals(4,res.size());
		
		assertEquals("HAS_AURA",res.get(0).getSymptoms().get(0).getId());
		assertEquals("DISABILITY_QUESTION_1",res.get(0).getDisability().getResponses().get(0).getId()); 
		
		
		
	}
	

}
