package com.betterqol.iheadache.accumulators;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.accumulators.PTUsageAccumulator.Accumulater;
import com.betterqol.iheadache.dashboard.DashboardHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class PTUsageAccumulatorTest extends BaseBigTest {
	
	private Date start_time;
	private Date end_time;
	private String id;
	private PTUsageAccumulator accumulator;
	private List<Map> full_res;
	private List<Map> inactive;
	private List<Map> active_res;

	@Before
	public void setUp() throws IOException {
		id = up.create(TestHelpers.makeUser("foo"));
		start_time = new GregorianCalendar(2012,4,18).getTime();
		end_time = new GregorianCalendar(2012,5,18).getTime();
		
	}
	
	public void runIt() throws IOException {
		full_res = ptRepo.getDateRangeDTO(id, start_time, end_time);
		inactive = ptRepo.getInactiveDTO(id, start_time,end_time);
		active_res = ptRepo.getActiveUntilDTO(id, end_time);
		
		accumulator = new PTUsageAccumulator(full_res,inactive,active_res);
	}
	
	public void setUp1() throws IOException {
		id = up.create(TestHelpers.makeUser("foo"));
		start_time = new GregorianCalendar(2012,4,18).getTime();
		end_time = new GregorianCalendar(2012,5,18).getTime();
		
		
		full_res = ptRepo.getDateRangeDTO(id, start_time, end_time);
		inactive = ptRepo.getInactiveDTO(id, start_time,end_time);
		active_res = ptRepo.getUntilDTO(id, end_time);
		
		accumulator = new PTUsageAccumulator(full_res,inactive,active_res);
	}
	
	public void setUp2() throws IOException {
		id = up.create(TestHelpers.makeUser("foo"));
		start_time = new GregorianCalendar(2012,4,18).getTime();
		end_time = new GregorianCalendar(2012,5,18).getTime();
		TestHelpers.addPTUsages(ptRepo,id, "tea", new GregorianCalendar(2012,5,01),15);	

		full_res = ptRepo.getDateRangeDTO(id, start_time, end_time);
		inactive = ptRepo.getInactiveDTO(id, start_time,end_time);
		active_res = ptRepo.getUntilDTO(id, end_time);
		
		accumulator = new PTUsageAccumulator(full_res,inactive,active_res);
	}

	@Test
	public void testStartEnd() throws IOException {	
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2012,4,18),31);	
		TestHelpers.addPTUsages(ptRepo,id, "tea", new GregorianCalendar(2012,5,01),null);	
		TestHelpers.addPTUsages(ptRepo,id, "maryjane", new GregorianCalendar(2012,5,18),null);
		runIt();
		assertEquals(3,full_res.size());
		
		List<List<Map>> res = accumulator.accumulate(DashboardHelpers.chunk(start_time, end_time, SliceType.MONTH),active_res, Accumulater.startend,"start");
		assertEquals(2,res.size());
		assertEquals(0,res.get(0).size());
		assertEquals(2,res.get(1).size());
			
		res = accumulator.accumulate(DashboardHelpers.chunk(start_time, end_time, SliceType.MONTH),inactive,Accumulater.startend,"end");
		assertEquals(2,res.size());
		int total = res.get(0).size() + res.get(1).size();
		assertEquals(1,total);
		
	}
	

	@Test
	public void testOngoing() throws IOException {
		TestHelpers.addPTUsages(ptRepo,id, "tea", new GregorianCalendar(2012,5,01),null);	
		runIt();
		active_res = ptRepo.getUntilDTO(id, end_time);
		List<List<Map>> res = accumulator.accumulateOngoing(DashboardHelpers.chunk(start_time, 
				new GregorianCalendar(2012,7,18).getTime(), SliceType.MONTH),active_res,Accumulater.ongoing,"foo");
		assertEquals(4,res.size());
		assertEquals(0,res.get(0).size());
		assertEquals(0,res.get(1).size());
		assertEquals(1,res.get(2).size());
		assertEquals(1,res.get(3).size()); 
	}
	
	@Test
	public void testOngoingEnded() throws IOException {
		TestHelpers.addPTUsages(ptRepo,id, "tea", new GregorianCalendar(2012,5,01),15);	
		runIt();
		active_res = ptRepo.getUntilDTO(id, new GregorianCalendar(2012,6,18).getTime());
		accumulator = new PTUsageAccumulator(full_res,inactive,active_res);
		List<List<Map>> res = accumulator.accumulateOngoing(DashboardHelpers.chunk(start_time, 
				new GregorianCalendar(2012,6,18).getTime(), SliceType.MONTH),active_res,Accumulater.ongoing,"foo");
		assertEquals(3,res.size());
		assertEquals(0,res.get(0).size());
		assertEquals(0,res.get(1).size());
		assertEquals(0,res.get(2).size());
	}
	
	
	@Test
	public void testOngoingEnded2() throws IOException {
		TestHelpers.addPTUsages(ptRepo,id, "tea", new GregorianCalendar(2012,5,01),90);	
		runIt();
		active_res = ptRepo.getUntilDTO(id, new GregorianCalendar(2012,8,18).getTime());
		accumulator = new PTUsageAccumulator(full_res,inactive,active_res);
		List<List<Map>> res = accumulator.accumulateOngoing(DashboardHelpers.chunk(start_time, 
				new GregorianCalendar(2012,8,18).getTime(), SliceType.MONTH),active_res,Accumulater.ongoing,"foo");
		assertEquals(5,res.size());
		assertEquals(0,res.get(0).size());
		assertEquals(0,res.get(1).size());
		assertEquals(1,res.get(2).size());
		assertEquals(0,res.get(3).size());
	}
	
	@Test
	
	public void testOngoing2() throws IOException {

		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2012,4,18),31);	
		TestHelpers.addPTUsages(ptRepo,id, "tea", new GregorianCalendar(2012,5,01),null);	
		TestHelpers.addPTUsages(ptRepo,id, "maryjane", new GregorianCalendar(2012,5,18),null);
		runIt();
		TestHelpers.addPTUsages(ptRepo,id, "maryjane", new GregorianCalendar(2012,6,18),31);	
		TestHelpers.addPTUsages(ptRepo,id, "maryjane", new GregorianCalendar(2012,5,1),10);	
		
		List<Map> realres = ptRepo.getDateRangeDTO(id, start_time, end_time);
			
		List<List<Map>> res = accumulator.accumulate(DashboardHelpers.chunk(start_time, 
				new GregorianCalendar(2012,8,18).getTime(), SliceType.MONTH),realres,Accumulater.ongoing,"foo");
		//assertEquals(5,res.size());
		//assertEquals(1,res.get(0).size());
		//assertEquals(1,res.get(1).size());
		//assertEquals(2,res.get(2).size());
		//assertEquals(0,res.get(3).size());
		//assertEquals(0,res.get(4).size());
	}
	
	@Test
	public void testComplete() throws IOException{

		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2012,4,18),31);	
		TestHelpers.addPTUsages(ptRepo,id, "tea", new GregorianCalendar(2012,5,01),null);	
		TestHelpers.addPTUsages(ptRepo,id, "maryjane", new GregorianCalendar(2012,5,18),null);
		runIt();
		accumulator.accumulate(DashboardHelpers.chunk(start_time, 
				new GregorianCalendar(2012,6,18).getTime(), SliceType.MONTH),SliceType.MONTH);
		
		assertEquals(3,accumulator.started_results.size());
		assertEquals(3,accumulator.ended_results.size());
		assertEquals(3,accumulator.ongoing_results.size());
		//System.out.println(accumulator.started_results.get(0).get(0));
		
		/*DateTime[] keys = accumulator.started_results.keySet().toArray(new DateTime[0]);		
		assertEquals(new DateMidnight(2012,5,31),keys[0]);
		assertEquals(new DateMidnight(2012,6,30),keys[1]);
		assertEquals(new DateMidnight(2012,7,31),keys[2]); */
		
		List<List<Map>> res = lilHelper(accumulator.started_results);
		//assertEquals(1,res.get(0).size());
		//assertEquals(2,res.get(1).size());
		//assertEquals(0,res.get(2).size());
		
		res = lilHelper(accumulator.ended_results);
		//assertEquals(0,res.get(0).size());
		//assertEquals(1,res.get(1).size());
		//assertEquals(0,res.get(2).size());
		
		res = lilHelper(accumulator.ongoing_results);
		//assertEquals(1,res.get(0).size());
		//assertEquals(2,res.get(1).size());
		//assertEquals(2,res.get(2).size());
		
	}
	
	public static List<List<Map>> lilHelper(List<List<Map>> items) {
		
		List<List<Map>> results = Lists.newArrayList();
		results.add(items.get(0));
		results.add(items.get(1));
		results.add(items.get(2));
		return results;
		
		
	}
}
