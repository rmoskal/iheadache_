package com.betterqol.iheadache.repository;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.model.Treatment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class TreatmentRepositoryTest {
	
	@Autowired
	TreatmentRepository repo;
	
	@After
	 public void cleanup() {
		repo.deleteAll();
		 
	 }
	
	@Test
	public void testCrud() {
		
		Treatment t = new Treatment("description","liquid","quart",false);
		Treatment t2 = new Treatment("other","solid","tonne",false);
		
		repo.create(t);
		String pk = repo.create(t2);
		
		assertEquals(2,repo.getAll().size());
		
		t2 = repo.get(pk);
		assertEquals("other", t2.getDescription());
		
		t2.setDescription("foo");
		repo.update(t2);
		t2 = repo.get(pk);
		assertEquals("foo", t2.getDescription());
		repo.delete(pk);
		
		assertEquals(1,repo.getAll().size());
		
		
		
	}

}
