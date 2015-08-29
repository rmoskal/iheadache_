package com.betterqol.iheadache.diary;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jettison.json.JSONException;
import org.ektorp.CouchDbConnector;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.TreatmentRepository;
import com.betterqol.iheadache.repository.UserInformationRepository;
import com.betterqol.iheadache.resource.DiaryResource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml" })
public class DiaryServiceTest {
	
	@Autowired
	HeadacheRepository repo;

	@Autowired
	LookupRepository lookups;

	@Autowired
	TreatmentRepository treatments;

	@Autowired
	DiaryResource serviceToTest;
	
	@Autowired
	UserInformationRepository up;

	@Autowired
	@Qualifier("headacheDatabase")
	CouchDbConnector db;

	@After
	public void cleanup() {
		repo.deleteAll();
		lookups.deleteAll();
		treatments.deleteAll();
		up.deleteAll();
	}
	
	@Test
	public void testGetFirst() throws IOException, JSONException {

		GregorianCalendar c = new GregorianCalendar(2011,7,8);
		TestHelpers.persistSomeHeadaches(repo,"test",null,null,c);
		Headache o = serviceToTest.getFirst(c.getTime());
		assertEquals("test", o.getUser());

	}
	
	@Test
	public void testGetFirstNoExist() throws IOException, JSONException {

		/* Resource r = new ClassPathResource("init_lookup_data_production.json");
		 IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		 loader.setDataPath(r);
		 
		 loader.init();
		 UserInformation u = TestHelpers.makeUser("test");
		 u.setId("test");
		 up.createTestUser(u);

		
		GregorianCalendar c = new GregorianCalendar(2011,7,8);
		TestHelpers.persistSomeHeadaches(repo,"test",null,null,c);
		c.roll(Calendar.MONTH, -1);
		Headache o = serviceToTest.getFirst(c.getTime());
		assertEquals("test", o.getUser());
		assertEquals("new", o.getId());
		assertEquals(5, o.getDisability().getResponses().size());
		assertEquals(11, o.getSymptoms().size()); */

	}
	
	
	@Test
	public void testGetForDay() throws IOException, JSONException {

		GregorianCalendar c = new GregorianCalendar(2011,7,8);
		TestHelpers.persistSomeHeadaches(repo,"test",null,null,c);
		c = new GregorianCalendar(2011,7,8);
		List<JsonNode> o = serviceToTest.get(c.getTime());
		assertEquals(2, o.size());

	}
	
	
	@Test
	public void testGetForDayNone() throws IOException, JSONException {

		GregorianCalendar c = new GregorianCalendar(2011,7,8);
		List<JsonNode> o = serviceToTest.get(c.getTime());
		assertEquals(0, o.size());

	}
	

}
