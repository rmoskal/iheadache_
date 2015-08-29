package com.betterqol.iheadache.repository;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.model.PreventativeTreatment;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class PreventativeTreatmentRepositoryTest extends BaseBigTest{
	
	@Test
	public void testWiring() {
		assertNotNull(preventTRepo);
	}
	
	@Test
	public void testGetAll() {
		
		PreventativeTreatment t = new PreventativeTreatment("some", "some_generic");
		preventTRepo.add(t);
		List<PreventativeTreatment> res = preventTRepo.getAll();
		assertEquals(1,res.size());
		
	}

}
