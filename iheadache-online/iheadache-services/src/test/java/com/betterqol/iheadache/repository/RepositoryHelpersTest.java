package com.betterqol.iheadache.repository;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class RepositoryHelpersTest {
	
	@Autowired
	HeadacheRepository repo;
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
	}
	
	@Test
	public void testEnap() {
		
		assertEquals(5,TestHelpers.hm.size());
		assertTrue(TestHelpers.hm.containsKey("TENSION_HEADACHE"));
		
			
	}
	
	@Test
	public void testGetEOD() {
		
		GregorianCalendar c = new GregorianCalendar(2011,8,11,2,30,30);
		Date results = RepositoryHelpers.getEOD(c.getTime());
		
		DateTime r = new DateTime(results);
		assertEquals(9, r.getMonthOfYear());
		assertEquals(11, r.getDayOfMonth());
		assertEquals(23, r.getHourOfDay());
		assertEquals(59, r.getMinuteOfHour());
		
	}
	
	@Test
	public void enmap() throws IOException {
		
		TestHelpers.persistSomeHeadaches(repo, new DateTime(2012,1,1,0,0,0,0),new DateTime(2012,1,1,0,0,0,0),
				new DateTime(2012,1,3,0,0,0,0));
		
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2012,0,1).getTime(),new GregorianCalendar(2012,0,30).getTime());
		assertEquals(3,data.size());
		
		Multimap<BaseDateTime, Map> res = RepositoryHelpers.DayMap(data, HeadacheMap.start.toString());
		assertEquals(2,res.keySet().size());
		List<BaseDateTime> keys = Lists.newArrayList(res.keySet());
		assertEquals(1,keys.get(0).getDayOfMonth());
		assertEquals(3,keys.get(1).getDayOfMonth());
		
		
	}

}
