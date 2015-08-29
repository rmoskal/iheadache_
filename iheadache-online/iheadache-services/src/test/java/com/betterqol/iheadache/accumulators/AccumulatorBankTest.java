package com.betterqol.iheadache.accumulators;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class AccumulatorBankTest  extends BaseBigTest {
	
	private String id;
	@Test
	public void testgetOnemonth() throws IOException {
		id = up.create(TestHelpers.makeUser("foo"));
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2012,4,18),31);	
		TestHelpers.addPTUsages(ptRepo,id, "tea", new GregorianCalendar(2012,5,01),null);	
		TestHelpers.addPTUsages(ptRepo,id, "maryjane", new GregorianCalendar(2012,5,18),null);
		
		 PTUsageAccumulator accumulator = accumulators.getOneMonthPT("", 2012, 5);
		 assertEquals(1,accumulator.started_results.size());
		 assertEquals(1,accumulator.ended_results.size());
		 assertEquals(1,accumulator.ongoing_results.size());

	}
	
	
	@Test
	public void testOneMonthRegress() throws IOException{
		
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2012,4,18),null);	
		TestHelpers.addPTUsages(ptRepo,id, "spirits", new GregorianCalendar(2012,4,1),10);	
		GregorianCalendar start_date = new GregorianCalendar(2012,4,01);
		GregorianCalendar end_date = new GregorianCalendar(2012,4,31);

		
		List<Map> full_res  = ptRepo.getDateRangeDTO(id, start_date.getTime(), end_date.getTime());
		List<Map> inactive = ptRepo.getInactiveDTO(id, start_date.getTime(), end_date.getTime());
		List<Map> active_res = ptRepo.getUntilDTO(id, end_date.getTime());
		assertEquals(2,full_res.size());
		
		
		PTUsageAccumulator acc = new PTUsageAccumulator(full_res,inactive,active_res);
		acc.accumulate(DashboardHelpers.chunk(start_date.getTime(), 
				end_date.getTime(), SliceType.MONTH),SliceType.MONTH);
		
		assertEquals(1,acc.started_results.size());
		assertEquals(1,acc.ended_results.size());
		assertEquals(1,acc.ongoing_results.size());
		System.out.println(acc.started_results.get(0));
		assertEquals(2,acc.started_results.get(0).size()); //started
		assertEquals(1,acc.ended_results.get(0).size());//ended
		assertEquals(0,acc.ongoing_results.get(0).size());
		
	}
	
	@Test
	public void testOutputComparison2() throws IOException {
		
		id = up.create(TestHelpers.makeUser("foo"));
		
		TestHelpers.addPTUsages(ptRepo,id, "cofee", new GregorianCalendar(2012,4,18),20);	
		TestHelpers.addPTUsages(ptRepo,id, "tea", new GregorianCalendar(2012,5,1),null);	
		TestHelpers.addPTUsages(ptRepo,id, "maryjane", new GregorianCalendar(2012,5,18),null);

		PTUsageAccumulator acc = accumulators.getOneMonthPT(id, 2012, 6);
		
		assertEquals(2,acc.started_results.get(0).size()); //started
		assertEquals(1,acc.ended_results.get(0).size());
		assertEquals(0,acc.ongoing_results.get(0).size());
		
	}
	
	

}
