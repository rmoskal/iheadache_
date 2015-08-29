package com.betterqol.iheadache.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.UUID;

import org.ektorp.spring.HttpClientFactoryBean;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.UserPrincipal;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class UserRepositoryTest {
	
	
	@Autowired
	UserInformationRepository repo;
	
	@Autowired
	PTRepository ptRepo;
	
	@Autowired
	HttpClientFactoryBean db;
	
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
		 ptRepo.deletAll();
	 }
	
	
	@Test
	public void testCRUD() {
		
		//assertEquals("localhost",db.host);
		String id =repo.create(TestHelpers.makeUser("foo"));
		UserInformation u = repo.get(id);
		assertEquals("foo",u.getName());
		
		u.setName("boo");
		assertEquals("boo",u.getName());
		assertEquals(1,repo.getAll().size());
		repo.delete(id);
		assertEquals(0,repo.getAll().size());
	}
	
	
	@Test
	public void testCheckRelation() {
		String pk = UUID.randomUUID().toString();
		UserPrincipal p = TestHelpers.makePrincipal("boo");
		p.setId(pk);
		repo.createForParent(p.getId(),p.getName());
		assertNotNull(repo.get("u_" + pk));	
	}
	
	
	@Test
	public void testGetByName() {
		
		repo.create(TestHelpers.makeUser("foo"));
		assertNotNull(repo.findByName("foo"));
	}
	
	
	@Test
	public void testGetByFails() {
		
		repo.create(TestHelpers.makeUser("foo"));
		assertNull(repo.findByName("hey"));
	}
	
	
	@Test
	public void testdeleteAll() {
		
		repo.create(TestHelpers.makeUser("foo"));
		repo.deleteAll();
		assertEquals(0,repo.getAll().size());
	}
	
	
	@Test
	public void testUserPKExtraction () {
	
		UserInformation o = new UserInformation();
		o.setId("u_foo");
		assertEquals("foo", o.getUserId());
	}
	
}
