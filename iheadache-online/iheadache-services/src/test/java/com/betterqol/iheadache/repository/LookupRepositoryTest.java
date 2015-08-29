package com.betterqol.iheadache.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.model.CouchLookup;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class LookupRepositoryTest {
	
	@Autowired
	LookupRepository repo;
	
	@After
	 public void cleanup() {
		repo.deleteAll();
		 
	 }
	
	@Test
	public void testWiring() {
		
		assertNotNull(repo);
	}
	
	@Test
	public void testCrud() {
		
		CouchLookup a = new CouchLookup(CouchLookup.Kind.TRIGGER);
		a.setDescription("foo");
		String id = repo.create(a);
		a = repo.get(id);
		assertEquals("foo", a.getDescription());
		a.setDescription("hey");
		repo.update(a);
		a = repo.get(id);
		assertEquals("hey", a.getDescription());
		repo.delete(a.getId());
		assertEquals(0,repo.getAll().size());
		
	}
	
	@Test
	public void testDeleteAll() {
		
		CouchLookup a = new CouchLookup(CouchLookup.Kind.TRIGGER);
		CouchLookup a1 = new CouchLookup(CouchLookup.Kind.TRIGGER);
		
		repo.add(a);
		repo.add(a1);
		
		repo.deleteAll();
		assertEquals(0,repo.getAll().size());
	}
	
	
	@Test
	public void testDeleteAllByType() {
		
		CouchLookup a = new CouchLookup(CouchLookup.Kind.TRIGGER);
		CouchLookup a1 = new CouchLookup(CouchLookup.Kind.TRIGGER);
		
		repo.add(a);
		repo.add(a1);
		
		repo.deleteAllByType(CouchLookup.Kind.TRIGGER);
		assertEquals(0,repo.getAll().size());
	}
	
	
	@Test
	public void testDiscrimination() {
		
		
		CouchLookup a = new CouchLookup(CouchLookup.Kind.TRIGGER,"one");
		CouchLookup a1 = new CouchLookup(CouchLookup.Kind.TRIGGER,"two");
		CouchLookup b  = new CouchLookup(CouchLookup.Kind.PAIN_LOCATION, "a");
		
		repo.add(a);
		repo.add(a1);
		repo.add(b);
		assertEquals(3, repo.getAll().size());
		
		
		List<CouchLookup> results = repo.findByLookupType(CouchLookup.Kind.TRIGGER);
		assertEquals(2, results.size());
		
		results = repo.findByLookupType(CouchLookup.Kind.PAIN_LOCATION);
		assertEquals(1, results.size());
		
		
		repo.deleteAllByType(CouchLookup.Kind.TRIGGER);
		assertEquals(0, repo.findByLookupType(CouchLookup.Kind.TRIGGER).size());
		assertEquals(1, repo.findByLookupType(CouchLookup.Kind.PAIN_LOCATION).size());
		

	}
	

}
