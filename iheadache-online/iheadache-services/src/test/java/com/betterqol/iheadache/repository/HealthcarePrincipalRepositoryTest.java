package com.betterqol.iheadache.repository;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.security.DuplicateUsernameException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class HealthcarePrincipalRepositoryTest {
	
	
	@Autowired
	HealthcarePrincipalRepository repo;
	
	@Autowired
	PTRepository ptRepo;
	
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
		 ptRepo.deletAll();
	 }
	
	
	@Test
	public void testCRUD() {
		
		HealthcarePrincipal p = TestHelpers.makeHcPrincipal("foo", true, "Smith");
		repo.add(p);
		HealthcarePrincipal u = repo.get(p.getId());
		assertEquals("foosmith",u.getName());
		

	}
	
	@Test
	public void testGetByName() {
		
		repo.add(TestHelpers.makeHcPrincipal("foo", true, "Smith"));
		assertNotNull(repo.findByName("foosmith"));
	}
	
	
	@Test
	public void testGetByApproved() {
		
		repo.add(TestHelpers.makeHcPrincipal("foo", true, "Smith"));
		assertEquals(1,repo.findByApproved1(true).size());
	}
	
	
	@Test
	public void testGetByApproved2() {
		
		repo.add(TestHelpers.makeHcPrincipal("foo", false, "Smith"));
		assertEquals(1,repo.findByApproved1(false).size());
	}
	
	
	@Test (expected = DuplicateUsernameException.class)
	public void testInsertDuplicateName() {
		
		repo.create(TestHelpers.makeHcPrincipal("foo", true, "Smith"));
		repo.create(TestHelpers.makeHcPrincipal("foo", true, "Smith"));

	}
	
	
	@Test
	public void testGetByFails() {
		
		repo.add(TestHelpers.makeHcPrincipal("foo", true, "Smith"));
		assertNull(repo.findByName("hey"));
	}
	

	
	
	@Test
	public void testdeleteAll() {
		
		repo.add(TestHelpers.makeHcPrincipal("foo", true, "Smith"));
		repo.deleteAll();
		assertEquals(0,repo.getAll().size());
	}
	

	

	
	

	
	
	

}
