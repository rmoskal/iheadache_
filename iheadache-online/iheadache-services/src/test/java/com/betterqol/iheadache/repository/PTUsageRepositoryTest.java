package com.betterqol.iheadache.repository;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.codehaus.jettison.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.PTUsage;
import com.betterqol.iheadache.model.UserInformation;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class PTUsageRepositoryTest extends BaseBigTest  {
	
	@Test
	public void testFindByParent() {
		
		String id = up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,8,18),new Random());	
		
		id =up.create(TestHelpers.simpleMakeUser("ha"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,8,18),new Random());		
		
		assertEquals(1,ptRepo.findByUser(id).size());
	}
	
	@Test
	public void testCascade() {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,8,18),new Random());	
		
		id =up.create(TestHelpers.simpleMakeUser("ha"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,8,18),new Random());
		
		assertEquals(2,ptRepo.getAll().size());
		up.delete(id);
		assertEquals(1,ptRepo.getAll().size());	
	}
	
	
	@Test
	public void testDeletePTUsage() {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,8,18),new Random());
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,8,18),new Random());	
	
		List<PTUsage> res = ptRepo.findByUser(id);
		assertEquals(2,res.size());
		
		ptRepo.deletePTUsage(res.get(0).getId());
		
		res = ptRepo.findByUser(id);
		assertEquals(1,res.size());
	}
	
	@Test
	public void testDirectTreatmentUpdate() throws JSONException {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,8,18),new Random());	
		
		List<PTUsage> pts = ptRepo.getAll();
		assertEquals(1,pts.size());
		
		PTUsage pt = (PTUsage) pts.toArray()[0];
		pt.setEnd(null);	
	}
	
	@Test
	public void testgetActiveInactive () {
	
		PTUsage p = new PTUsage("test", new Date(), null, "I was tired", "50", "Some branded drug", "some generic drug");
		up.addPTUsage(p);
		
		p = new PTUsage("test", new Date(),  new Date(), "I was tired", "100", "Some other branded drug", "Some other generic drug");
		up.addPTUsage(p);
		
		List<PTUsage> results = ptRepo.getActive("test");
		assertEquals(1,results.size());
		assertEquals("Some branded drug",results.get(0).getTreatmentDescription());
		results = ptRepo.getInactive("test");
		assertEquals(1,results.size());
		assertEquals("Some other branded drug",results.get(0).getTreatmentDescription());
	}
	

	
	@Test
	public void testpersistSomePTs () {
		
		UserInformation o = TestHelpers.makeUser("foo");
		o.setId("test");
		up.createTestUser(o);
		TestHelpers.persistSomePTUsages(ptRepo, "test", new GregorianCalendar(2011,0,1));
		List<PTUsage> res = ptRepo.findByUser("test");
		assertEquals(8,res.size()) ;
		ptRepo.deleteForUser("test");
		res = ptRepo.findByUser("test");
		assertEquals(0,res.size()) ;
	}
	
	
	@Test
	public void testGetReallyActive() throws IOException {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,8,18),null);
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,9,18),null);	
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,10,18),null);	
		
		List<Map> res = ptRepo.getActiveUntilDTO(id, new GregorianCalendar(2011,9,18).getTime());
		assertEquals(2,res.size());
		res = ptRepo.getActiveUntilDTO(id, new GregorianCalendar(2011,10,18).getTime());
		assertEquals(3,res.size());
	}
	
	@Test
	public void testGetReallyActive2() throws IOException {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,8,18),new Random());
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,9,18),null);	
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,10,18),null);	
		
		List<Map> res = ptRepo.getActiveUntilDTO(id, new GregorianCalendar(2011,10,18).getTime());
		assertEquals(2,res.size());
	}
	
	@Test
	public void testEndingInTheFuture() throws IOException {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(),30);
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,9,18),null);	
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,10,18),null);	
		
		List<PTUsage> res = ptRepo.getActiveandFuture(id);
		assertEquals(3,res.size());
	}
	
	@Test
	public void testGetUntilDTO() throws IOException {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,8,18),10);
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,9,18),null);	
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,10,18),null);	
		
		List<Map> res = ptRepo.getUntilDTO(id, new GregorianCalendar(2011,10,18).getTime());
		assertEquals(3,res.size());
	}
	
	@Test
	public void testGetActiveUntil() throws IOException {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "spirits", new GregorianCalendar(2012,5,1),62);	
		
		List<Map> res = ptRepo.getUntilDTO(id,
				new GregorianCalendar(2012,5,30).getTime());
		assertEquals(1,res.size());
		
	}
	
	
	@Test
	public void testGetInactiveDTO() throws IOException {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2010,8,18),new Random()); //Excluded!
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,8,18),new Random());
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,9,18),null);	
		TestHelpers.addPTUsages(ptRepo, id, "teas", new GregorianCalendar(2011,10,18),null);	
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,11,18),new Random());//Excluded!
		
		List<Map> res = ptRepo.getInactiveDTO(id, new GregorianCalendar(2011,8,18).getTime(),
				new GregorianCalendar(2011,10,18).getTime());
		assertEquals(1,res.size());
		
	}
	
	
	@Test
	public void testGetInactiveDTO2() throws IOException {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2012,4,18),20);	
		
		List<Map> res = ptRepo.getInactiveDTO(id, new GregorianCalendar(2012,5,1).getTime(),
				new GregorianCalendar(2012,5,30).getTime());
		assertEquals(1,res.size());
		
	}
	
	
	@Test
	public void testGetInactiveDTO3() throws IOException {
		
		String id =up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2011,4,1),10);	
		
		List<Map> res = ptRepo.getInactiveDTO(id, new GregorianCalendar(2011,4,1).getTime(),
				new GregorianCalendar(2011,4,30).getTime());
		assertEquals(1,res.size());
		
	}
	
	
	
	

}
