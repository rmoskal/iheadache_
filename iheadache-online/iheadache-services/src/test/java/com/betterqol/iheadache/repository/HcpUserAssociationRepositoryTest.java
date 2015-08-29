package com.betterqol.iheadache.repository;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.model.HcpUserAssociation;
import com.google.common.collect.Lists;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class HcpUserAssociationRepositoryTest {
	
	@Autowired
	HcpUserAssociationRepository repo;
	
	@After
	 public void cleanup() {
		repo.deleteAll();
		 
	 }
	

	@Test
	public void testFind () {
		
		HcpUserAssociation	o  = new HcpUserAssociation("one", "two");
		HcpUserAssociation	o1  = new HcpUserAssociation("one", "three");
		repo.add(o);
		repo.add(o1);
		assertEquals(2,repo.findByUserId("one").size());
		assertEquals(1,repo.findByHcpId("three").size());
	}
	
	
	@Test
	public void testDeletionsUser () {
		
		makeSomeRelations();
		assertEquals(4,repo.getAll().size());
		repo.deleteForUserId("a");
		assertEquals(2,repo.getAll().size());
	}
	
	@Test
	public void testDeletionsHcp () {
		
		makeSomeRelations();
		repo.deleteForHcpId("three");
		assertEquals(1,repo.getAll().size());
	}
	
	
	@Test
	public void testCreation() {
		
		makeSomeRelations();
		repo.create("a", Lists.newArrayList("one", "two"));
		assertEquals(4,repo.getAll().size());
		assertEquals(2,repo.findByUserId("a").size());
	}


	private void makeSomeRelations() {
		HcpUserAssociation	o  = new HcpUserAssociation("a", "two");
		repo.add(o);
		o  = new HcpUserAssociation("a", "three");
		repo.add(o);
		o  = new HcpUserAssociation("b", "three");
		repo.add(o);
		o  = new HcpUserAssociation("c", "three");
		repo.add(o);
	}
	
	
}
