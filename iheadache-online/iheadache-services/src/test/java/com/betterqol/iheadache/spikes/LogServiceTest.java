package com.betterqol.iheadache.spikes;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class LogServiceTest {
	
	@Autowired
	HeadacheRepository repo;
	
	
	@Autowired
	LookupRepository lookups;
	
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
		 lookups.deleteAll();
	 }
	
	
	
	@Test
	public void testCreateDisabiltySLabel() {
		
	String hey = "@.^&**";
	hey.toLowerCase(Locale.ENGLISH);
		
	}
	

	

	

	

}
