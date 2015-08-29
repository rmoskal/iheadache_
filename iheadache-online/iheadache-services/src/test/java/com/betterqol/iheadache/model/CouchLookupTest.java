package com.betterqol.iheadache.model;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;



public class CouchLookupTest {
	
	@Test
	public void testDifferentiater() {
		
		CouchLookup o = new CouchLookup(CouchLookup.Kind.TRIGGER);
		assertEquals("TRIGGER", o.GetLookupType().toString());
		
	}
	
	
	@Test
	public void testEnlist() {
		
		List<LookupDTO> oo = Lists.newArrayList(new LookupDTO("one", "one"), new LookupDTO("two", "two"));
		StringBuffer b = new StringBuffer();
		LookupDTO.enlist(b, oo);
		assertEquals("one, two", b.toString());
		
	}

}
