package com.betterqol.iheadache.calendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.model.dto.CalendarItem;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.resource.DashboardResource;

@SuppressWarnings("rawtypes")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class CalendarMakerTest {
	
	@Autowired
	HeadacheRepository repo;
	
	@Autowired
	DashboardResource svc;
	
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
	 }
	
	@Test
	public void testGetFirstOfMonth() {
		Calendar c = new GregorianCalendar(2012,1,1);
		int isSun = CalendarMaker.getFirstOfMonth(c);
		assertEquals(2,isSun); 
	}
	

	@Test
	public void testMakeRawCalendar() {
		
		Calendar c = new GregorianCalendar(2012,1,1);
		Integer[] res = CalendarMaker.makeRawCalendar(c);
		assertEquals(1, res[3].intValue());
		assertEquals(2, res[4].intValue());
		assertEquals(31, res[33].intValue());
		assertNull(res[34]); 
	}

	@Test
	public void testCalendar() {
		
		List<CalendarItem> results = CalendarMaker.makeForMonth(2012, 0);
		assertEquals(42, results.size());

	}
	
	@Test
	public void testEnmap() throws IOException {
		
		TestHelpers.persistSomeHeadaches(repo, new DateTime(2012,1,1,0,0,0,0),new DateTime(2012,1,2,0,0,0,0),
				new DateTime(2012,1,3,0,0,0,0));
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2012,0,1).getTime(),new GregorianCalendar(2012,0,30).getTime());
		assertEquals(3,data.size());
		
		Map<Integer, Map> results = CalendarMaker.enmapHeadaches(data);
		assertEquals(3,results.size());
	}
	
	@Test
	public void testNext() throws IOException {
		
		TestHelpers.persistSomeHeadaches(repo, new DateTime(2012,1,1,0,0,0,0),new DateTime(2012,1,2,0,0,0,0),
				new DateTime(2012,1,3,0,0,0,0));
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2012,0,1).getTime(),new GregorianCalendar(2012,0,30).getTime());
		assertEquals(3,data.size());
		
		List<CalendarItem> results = CalendarMaker.makeForMonth(2012, 1, data);
		assertEquals(42,results.size());
		assertEquals("",results.get(0).HeadacheType);
		assertEquals("",results.get(1).HeadacheType);
		assertEquals("MIGRAINE",results.get(3).HeadacheType);
		assertEquals(new Integer(1),results.get(3).Date);
		assertEquals(data.get(0).get(HeadacheMap.id.toString()).toString(),results.get(3).HeadacheId);
		assertEquals("MIGRAINE",results.get(4).HeadacheType);
		assertEquals(new Integer(2),results.get(4).Date);
		assertEquals("MIGRAINE",results.get(5).HeadacheType);
		assertEquals(new Integer(3),results.get(5).Date);
	}
}
