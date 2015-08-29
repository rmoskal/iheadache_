package com.betterqol.iheadache.dashboard;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;

public class ChunkingTest {
	
	@Test
	public void testQuarterCalc(){
		assertEquals(1,DashboardHelpers.getQuarter(new DateTime(2012,1,1,0,0,0,0)));
		assertEquals(1,DashboardHelpers.getQuarter(new DateTime(2012,3,31,0,0,0,0)));
		assertEquals(2,DashboardHelpers.getQuarter(new DateTime(2012,4,1,0,0,0,0)));
		assertEquals(2,DashboardHelpers.getQuarter(new DateTime(2012,6,30,0,0,0,0)));
		assertEquals(3,DashboardHelpers.getQuarter(new DateTime(2012,7,1,0,0,0,0)));
		assertEquals(3,DashboardHelpers.getQuarter(new DateTime(2012,9,30,0,0,0,0)));
		assertEquals(4,DashboardHelpers.getQuarter(new DateTime(2012,10,1,0,0,0,0)));
		assertEquals(4,DashboardHelpers.getQuarter(new DateTime(2012,12,31,0,0,0,0)));
	}
	
	//@Test
	/**
	 * THIS IS BROKEN!!!
	 */
	/*public void testDaily() {
		
		
		List<Calendar> results = DashboardHelpers.chunk(new GregorianCalendar(2011,9,1), new GregorianCalendar(2011,9,21), SliceType._7);
		assertEquals(3,results.size());
		assertEquals(new GregorianCalendar(2011,9,7),results.get(0));
		assertEquals(new GregorianCalendar(2011,9,21),results.get(2));
		
	} */
	
	
	
	@Test
	/**
	 * THIS IS BROKEN!!!
	 */
	public void test28Days() {
		
		GregorianCalendar c2 = new GregorianCalendar(2011,9,29);
		
		List<DateTime> results = DashboardHelpers.chunk(new GregorianCalendar(2011,9,1), c2, SliceType._28);
		assertEquals(2,results.size());
	//	System.out.print(results.get(0).getTime().toGMTString());
		//assertEquals(new GregorianCalendar(2011,9,29),results.get(0));
		
	}
	
	@Test
	public void test28DaysLeft() {
		
		
		List<DateTime> results = DashboardHelpers.chunk(new GregorianCalendar(2011,8,24), new GregorianCalendar(2011,9,29), SliceType._28);
		assertEquals(2,results.size());
		assertEquals(new GregorianCalendar(2011,9,1).getTime(),results.get(0).toDate());
		assertEquals(new GregorianCalendar(2011,9,29).getTime(),results.get(1).toDate());

		
	}
	
	@Test
	public void test30Days() {
		
		GregorianCalendar c1 = new GregorianCalendar(2011,9,31);
		GregorianCalendar c2 = new GregorianCalendar(2011,9,1);
		
		List<DateTime> results = DashboardHelpers.chunk(c2, c1, SliceType._30);
		assertEquals(2,results.size());
		//assertEquals(c2.getTime(),results.get(0).getTime());

		
	}
	
	@Test
	public void test30DaysLeft() {
		
		GregorianCalendar c1 = new GregorianCalendar(2011,9,31);
		GregorianCalendar c2 = new GregorianCalendar(2011,8,24);
		
		List<DateTime> results = DashboardHelpers.chunk(c2, c1, SliceType._30);
		assertEquals(2,results.size());
		assertEquals(new GregorianCalendar(2011,9,1).getTime(),results.get(0).toDate());
		assertEquals(new GregorianCalendar(2011,9,31).getTime(),results.get(1).toDate());

		
	}
	
	@Test
	public void test30DaysLeft2() {
		
		GregorianCalendar c1 = new GregorianCalendar(2011,9,1);
		GregorianCalendar c2 = new GregorianCalendar(2011,8,1);
		
		c1.roll(Calendar.DAY_OF_YEAR, -30);
		
		assertEquals(c2.getTime(),c1.getTime());
			
	}
	
	
	@Test
	public void Regress1() {
		
		GregorianCalendar c1 = new GregorianCalendar(2011,11,1);
		GregorianCalendar c2 = new GregorianCalendar(2012,2,22);
		List<DateTime> results = DashboardHelpers.chunk(c1.getTime(), c2.getTime(), SliceType.MONTH);
		assertEquals(4,results.size());
		assertEquals(new GregorianCalendar(2011,11,31).getTime(), results.get(0).toDate());
		assertEquals(new GregorianCalendar(2012,0,31).getTime(), results.get(1).toDate());
		assertEquals(new GregorianCalendar(2012,1,29).getTime(), results.get(2).toDate());
		assertEquals(new GregorianCalendar(2012,2,31).getTime(), results.get(3).toDate());
	}
	
	
	@Test
	public void testMonth() {
		
		GregorianCalendar c1 = new GregorianCalendar(2011,9,27);
		GregorianCalendar c2 = new GregorianCalendar(2011,9,1);
		
		List<DateTime> results = DashboardHelpers.chunk(c2, c1, SliceType.MONTH);
		assertEquals(1,results.size());
		assertEquals(new GregorianCalendar(2011,9,31).getTime(),results.get(0).toDate());
		
	}
	
	
	@Test
	public void testMonthEnd() {
		
		GregorianCalendar c1 = new GregorianCalendar(2012,2,3);
		GregorianCalendar c2 = new GregorianCalendar(2012,2,31);
		
		List<DateTime> results = DashboardHelpers.chunk(c1, c2, SliceType.MONTH);
		assertEquals(1,results.size());
		assertEquals(new GregorianCalendar(2012,2,31).getTime(),results.get(0).toDate());

		
	}
	
	@Test
	public void testMonthDuration() {
		
		
		int res = SliceType.MONTH.getDuration(new DateTime(2012,1,1,0,0,0,0));
		assertEquals(30, res);
		
	}
	
	

	
	@Test
	public void testMonthEnd2() {
		
		GregorianCalendar c1 = new GregorianCalendar(2011,9,31);
		GregorianCalendar c2 = new GregorianCalendar(2011,10,30);
		
		List<DateTime> results = DashboardHelpers.chunk(c1, c2, SliceType.MONTH);
		assertEquals(2,results.size());
		//assertEquals(new GregorianCalendar(2011,9,31).getTime(),results.get(0).getTime());
		//assertEquals(new GregorianCalendar(2011,10,30).getTime(),results.get(1).getTime());


		
	}
	
	
	@Test
	public void testSeveralMonths() {
		
		List<DateTime> results = DashboardHelpers.chunk(new GregorianCalendar(2011,8,1),  new GregorianCalendar(2011,9,27), SliceType.MONTH);
		assertEquals(2,results.size());
		assertEquals(new GregorianCalendar(2011,8,30).getTime(),results.get(0).toDate());
		assertEquals(new GregorianCalendar(2011,9,31).getTime(),results.get(1).toDate());
	}
	
	
	@Test
	public void testMoreMonths() {

		List<DateTime> results = DashboardHelpers.chunk(new GregorianCalendar(2011,0,1),  new GregorianCalendar(2011,11,29), SliceType.MONTH);
		assertEquals(12,results.size());
		assertEquals(1,results.get(0).getMonthOfYear());
		assertEquals(12,results.get(11).getMonthOfYear());

	}
	
	
	@Test
	public void testThisYear() {
		
		List<DateTime> results = DashboardHelpers.chunk(new GregorianCalendar(2011,0,22),  new GregorianCalendar(2012,0,22), SliceType.MONTH);
		assertEquals(13,results.size());
		assertEquals(1,results.get(0).getMonthOfYear());
		assertEquals(2012,results.get(12).getYear());
		assertEquals(1,results.get(12).getMonthOfYear());
	}
	
	
	@Test
	public void testQuarter() {
		
		List<DateTime> results = DashboardHelpers.chunk(new GregorianCalendar(2012,0,15),  new GregorianCalendar(2012,0,22), SliceType.QUARTER);
		assertEquals(1,results.size());
		assertEquals(3,results.get(0).getMonthOfYear());
		assertEquals(2012,results.get(0).getYear());
		assertEquals(31,results.get(0).getDayOfMonth());

	}
	
	@Test
	public void testQuarter2() {
		
		List<DateTime> results = DashboardHelpers.chunk(new GregorianCalendar(2012,0,15),  new GregorianCalendar(2012,4,22), SliceType.QUARTER);
		assertEquals(2,results.size());
		assertEquals(3,results.get(0).getMonthOfYear());
		assertEquals(2012,results.get(0).getYear());
		assertEquals(31,results.get(0).getDayOfMonth());
		
		assertEquals(6,results.get(1).getMonthOfYear());
		assertEquals(2012,results.get(1).getYear());
		assertEquals(30,results.get(1).getDayOfMonth());

	}
	
	@Test
	public void testQuarterAcrossYear() {
		
		List<DateTime> results = DashboardHelpers.chunk(new GregorianCalendar(2011,11,15),  new GregorianCalendar(2012,1,22), SliceType.QUARTER);
		assertEquals(2,results.size());
		assertEquals(12,results.get(0).getMonthOfYear());
		assertEquals(2011,results.get(0).getYear());
		assertEquals(31,results.get(0).getDayOfMonth());
		
		assertEquals(3,results.get(1).getMonthOfYear());
		assertEquals(2012,results.get(1).getYear());
		assertEquals(31,results.get(1).getDayOfMonth());

	}
	
	@Test
	/***
	 * New chunking method added for the latest dashboard parts
	 */
	public void testOneChunk() {
		
		List<DateTime> results = DashboardHelpers.chunk(new GregorianCalendar(2011,11,15),  new GregorianCalendar(2012,1,22), SliceType.ALL);
		assertEquals(1,results.size());
		
	}
	
	
	
	
	
	


}
