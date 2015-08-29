package com.betterqol.iheadache;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.betterqol.iheadache.model.HealthcarePrincipal;


public class TestHelpersTest {
	
	@Test
	public void testMakeHcPrincipal() {
		HealthcarePrincipal p = TestHelpers.makeHcPrincipal("John One", true, "Smith");
		assertEquals("johnonesmith", p.getName());
	}
	

}
