package com.betterqol.iheadache.hcp;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.GregorianCalendar;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.IHeadacheDataLoader;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.HcpUserAssociation;
import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.UserPrincipal;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class HcpServiceTest extends BaseBigTest  {
	

	
	@Autowired
	HcpService svc;
	
	@Before
	public void makeSomeData() throws FileNotFoundException, IOException{
		Resource r = new ClassPathResource("init_lookup_data_production.json");
		IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		loader.setDataPath(r);

		loader.init();
	}
	
	@Test
	public void testHeadaches() {
		
		UserPrincipal p = TestHelpers.makePrincipal("foo");
		pp.create(p);
		up.createForParent(p.getId(), p.getName());
		
		TestHelpers.persistSomeHeadaches(repo, p.getId(),lookups,treatments,
				 new GregorianCalendar(2010,11,26),
				 new GregorianCalendar(2011,0,28),
				 new GregorianCalendar(2011,1,28));
		
		HealthcarePrincipal hp = TestHelpers.makeHcPrincipal("dr", true, "Smith");
		hcp.create(hp);
		up.createForParent(hp.getId(), hp.getName());
		
		int uCount = repo.getAll().size();
		UserInformation hpui = up.get("u_"+ hp.getId());
		
		svc.switchUser(p.getId(), hp.getId(),hpui);
		
		assertEquals(2 * uCount,repo.getAll().size()); //we should have doubled the repo
		assertEquals(uCount, repo.findByUser(hp.getId()).size());  //we should have the same for the dr
		assertEquals(uCount, repo.findByUser(p.getId()).size()); // and for the first
		
		p = TestHelpers.makePrincipal("another");
		pp.create(p);
		up.createForParent(p.getId(), p.getName());
		
		TestHelpers.persistSomeHeadaches(repo, p.getId(),lookups,treatments,
				 new GregorianCalendar(2010,11,26),
				 new GregorianCalendar(2011,0,28),
				 new GregorianCalendar(2011,1,28)); 
		
		assertEquals(3 * uCount,repo.getAll().size());
		
		svc.switchUser(p.getId(), hp.getId(),hpui);
		assertEquals(3 * uCount,repo.getAll().size());
		
		hp = hcp.get(hp.getId());
		assertEquals(p.getId(), hp.getCurrentUser());
		
	}
	
	@Test
	public void testTreatments() {
		
		UserPrincipal p = TestHelpers.makePrincipal("foo");
		pp.create(p);
		up.createForParent(p.getId(), p.getName());
		
		TestHelpers.persistSomePTUsages(ptRepo, p.getId(), new GregorianCalendar(2010,11,26));
		int uCount = ptRepo.getAll().size();
		HealthcarePrincipal hp = TestHelpers.makeHcPrincipal("dr", true, "Smith");
		hcp.create(hp);
		up.createForParent(hp.getId(), hp.getName());
		UserInformation hpui = up.get("u_"+ hp.getId());
		
		svc.switchUser(p.getId(), hp.getId(),hpui);
		
		assertEquals(2 * uCount,ptRepo.getAll().size()); //we should have doubled the repo
		assertEquals(uCount, ptRepo.findByUser(hp.getId()).size());  //we should have the same for the dr
		assertEquals(uCount, ptRepo.findByUser(p.getId()).size()); // and for the first
		
		p = TestHelpers.makePrincipal("another");
		pp.create(p);
		up.createForParent(p.getId(), p.getName());
		TestHelpers.persistSomePTUsages(ptRepo, p.getId(), new GregorianCalendar(2010,11,26));
		
		assertEquals(3 * uCount,ptRepo.getAll().size());
		
		svc.switchUser(p.getId(), hp.getId(),hpui);
		assertEquals(3 * uCount,ptRepo.getAll().size());
		
	}
	
	@Test
	public void testTreatments_regress1() throws IOException {
		
		UserPrincipal p = TestHelpers.makePrincipal("foo");
		pp.create(p);
		up.createForParent(p.getId(), p.getName());
		
		TestHelpers.addPTUsages(ptRepo,p.getId(),"Some thing",new GregorianCalendar(2010,11,26),null);
		int uCount = 1;
		HealthcarePrincipal hp = TestHelpers.makeHcPrincipal("dr", true, "Smith");
		hcp.create(hp);
		up.createForParent(hp.getId(), hp.getName());
		UserInformation hpui = up.get("u_"+ hp.getId());
		
		svc.switchUser(p.getId(), hp.getId(),hpui);
		
		assertEquals(2 * uCount,ptRepo.getAll().size()); //we should have doubled the repo
		assertEquals(uCount, ptRepo.findByUser(hp.getId()).size());  //we should have the same for the dr
		assertEquals(uCount, ptRepo.findByUser(p.getId()).size()); // and for the first
		
		//assertEquals(uCount,ptRepo.getUntilDTO(p.getId(), new GregorianCalendar(2010,11,26).getTime()).size());
		
	}
	
	@Test
	public void testAssociations() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		
		UserPrincipal u = TestHelpers.makePrincipal("foo");
		pp.create(u);
		UserPrincipal u2 = TestHelpers.makePrincipal("bar");
		pp.create(u2);
		
		HcpUserAssociation	o  = new HcpUserAssociation(u.getId(), "two");
		HcpUserAssociation	o1  = new HcpUserAssociation(u2.getId(), "two");
		associationRepo.add(o);
		associationRepo.add(o1);
		
		JSONArray res = svc.getAssociations("two");
		assertEquals(2, res.length());
		
	}
	
	
	@Test
	public void testswitchUserInformation() {
		
		UserPrincipal p = TestHelpers.makePrincipal("foo");
		pp.create(p);
		up.createForParent(p.getId(), p.getName());
		UserInformation pui = up.get("u_"+ p.getId());
		pui.setActiveCustomSymptoms(Lists.newArrayList(new LookupDTO("one", "one"), new LookupDTO("two", "two")));
		
	
		HealthcarePrincipal hp = TestHelpers.makeHcPrincipal("dr", true, "Smith");
		hcp.create(hp);
		up.createForParent(hp.getId(), hp.getName());
		UserInformation hpui = up.get("u_"+ hp.getId());
		
		svc.switchUserInformation(hpui, pui);
		
		hpui = up.get("u_"+ hp.getId());
		
		assertEquals(2, hpui.getActiveCustomSymptoms().size());
		
	}

}
