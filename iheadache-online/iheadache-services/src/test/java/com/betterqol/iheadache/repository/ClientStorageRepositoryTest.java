package com.betterqol.iheadache.repository;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class ClientStorageRepositoryTest {
	
	private static final String SOME_JSON = "{'key':'one','value':[1,2,3,4]}";
	private static final String OTHER_JSON = "{'key':'one','value':[1,2,3,4]}";
	
	@Autowired
	ClientStorageRepository repo;
	
	
	@Test (expected=IllegalArgumentException.class)
	public void testConstraints() {
		
		repo.insertSingleton(null, "test", SOME_JSON);
		
	}
	
	@After
	public void cleanup() {
		repo.deleteAll();

	 }

	
	@Test
	public void testSingleton() throws JSONException {
		

		repo.insertSingleton("rob", "item", SOME_JSON);
		repo.insertSingleton("rob","item", OTHER_JSON);
		
		Assert.assertEquals(1, repo.getAll().size());
		JSONObject o = repo.fetch("rob","item");
		assertTrue(o.has("key"));
		repo.insertSingleton("rob","item2", SOME_JSON);
		Assert.assertEquals(2, repo.getAll().size());
		
		repo.delete("rob", "item2");
		Assert.assertEquals(1, repo.getAll().size());
		
	}
	
	
	
	@Test
	public void testMulti() throws JSONException {
		

		repo.insertMulti("test", "item", SOME_JSON);
		repo.insertMulti("test","item", OTHER_JSON);
		
		Assert.assertEquals(2, repo.getAll().size());
		List<Map> results = repo.fetchMulti("test", "item");
		assertEquals(2,results.size());
		JSONObject o  = new JSONObject( results.get(0).get("data").toString());
		assertEquals("one",o.get("key"));
		
		
	}
	
	

}
