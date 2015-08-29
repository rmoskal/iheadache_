package com.betterqol.iheadache.asynch.listeners;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.model.AppSettings;
import com.betterqol.iheadache.model.AppSettings.DurationRules;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml" })
public class RescoreUserHeadacheTest extends BaseBigTest {

	@Test
	public void testReevaluateHeadache() {
		
		assertFalse(RescoreUserHeadache.reevaluateHeadache(new AppSettings(), new AppSettings()));
	}
	
	@Test
	public void testReevaluateHeadache2() {

		AppSettings settings = new AppSettings();
		settings.setDurationRule(DurationRules.REQUIRED);
		assertTrue(RescoreUserHeadache.reevaluateHeadache(new AppSettings(), settings));
		
	} 

}
