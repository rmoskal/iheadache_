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
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.security.DuplicateUsernameException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class UserPrincipalRepositoryTest {
	
	
	@Autowired
	UserPrincipalRepository repo;
	
	@Autowired
	PTRepository ptRepo;
	
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
		 ptRepo.deletAll();
	 }
	
	
	@Test
	public void testCRUD() {
		
		UserPrincipal p = TestHelpers.makePrincipal("foo");
		repo.add(p);
		UserPrincipal u = repo.get(p.getId());
		assertEquals("foo",u.getName());
		

	}
	
	@Test (expected = DuplicateUsernameException.class)
	public void testInsertDuplicateName() {
		
		repo.create(TestHelpers.makePrincipal("foo"));
		repo.create(TestHelpers.makePrincipal("foo"));

	}
	
	
	@Test
	public void testGetByName() {
		
		repo.add(TestHelpers.makePrincipal("foo"));
		assertNotNull(repo.findByName("foo"));
	}
	
	
	@Test
	public void testGetByFails() {
		
		repo.add(TestHelpers.makePrincipal("foo"));
		assertNull(repo.findByName("hey"));
	}
	

	
	
	@Test
	public void testdeleteAll() {
		
		repo.add(TestHelpers.makePrincipal("foo"));
		repo.deleteAll();
		assertEquals(0,repo.getAll().size());
	}
	

	

	
	

	
	
	

}
