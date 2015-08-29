package com.betterqol.iheadache.repository;

import static junit.framework.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.CouchLookup;
import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.UserPrincipal;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class CrossRepsositoryTest extends BaseBigTest {
	
	

	
	
	@Test
	public void testDiscrimination () {
		
		CouchLookup b  = new CouchLookup(CouchLookup.Kind.PAIN_LOCATION);
		lookups.add(b);
		
		repo.add(TestHelpers.createMinHeadache(new Date(), "foo"));
		
		assertEquals(1, repo.getAll().size());
		assertEquals(1, lookups.findByLookupType(b.GetLookupType()).size());
	}
	
	
	@Test
	public void testPrincipalDiscrimination () {
		
		HealthcarePrincipal hp = TestHelpers.makeHcPrincipal("hey", true, "Smith");
		hcp.create(hp);
		UserPrincipal p = TestHelpers.makePrincipal("hey");
		pp.create(p);
		assertEquals(1, hcp.getAll().size());
		assertEquals(1, pp.getAll().size());
		
	}

}
