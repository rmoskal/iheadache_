package com.betterqol.iheadache.dashboard;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.betterqol.iheadache.AppHelpers;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.Treatment;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class AcuteTreatmentAccumulatorTest {
	/**
	 * This tests the other kind of accumulation.  It works for things like acute 
	 * headache treatments where there are multiple values we have to count
	 * @throws ParseException
	 * 
	 * 
	 */
	@Test
	public void testAcuteTreatmentAccumulation() throws ParseException {
		
		 ArrayList<Treatment> h = Lists.newArrayList(Treatment.minimal("one", "First"),
				 Treatment.minimal("two", "Second"),Treatment.minimal("three", "Third"),Treatment.minimal("four", "Fourth"), Treatment.minimal("five", "Fifth") );
		 
		 
		 HeadacheAccumulator a = TestHelpers.contructAcuteTreatment(h, new GregorianCalendar(2011,7,31), new GregorianCalendar(2011,9,31),
				 new GregorianCalendar(2011,8,10), new GregorianCalendar(2011,8,11),new GregorianCalendar(2011,9,10));
		 assertEquals(3,a.results.size());
		

		// assertEquals("2",a.results.get(0).get("five").toString());
		 System.out.println(a.results.get(0).get("axis").toString());
		 assertTrue(a.results.get(0).get("axis").toString().startsWith("2011-08-31"));
		// assertEquals("2",a.results.get(0).get("two").toString());
		 
		 assertEquals("2",a.results.get(1).get("one").toString());
		 System.out.println(a.results.get(1).get("axis").toString());
		 assertTrue(a.results.get(1).get("axis").toString().startsWith("2011-09-30"));
		 assertEquals("2",a.results.get(1).get("two").toString());
		 
		 assertEquals("1",a.results.get(2).get("one").toString());
		 System.out.println(a.results.get(2).get("axis").toString());
		 assertTrue(a.results.get(2).get("axis").toString().startsWith("2011-10-31"));
		 //assertEquals("2",a.results.get(2).get("two").toString());
	}
	
	@Test
	public void testAcuteTreatmentAccumulationWithNovelty() throws ParseException {
		
		 ArrayList<Treatment> h = Lists.newArrayList(Treatment.minimal("one", "First"),
				 Treatment.minimal("two", "Second"),Treatment.minimal("three", "Third"),Treatment.minimal("four", "Fourth"), Treatment.minimal("five", "Fifth") );
		 
		 
		 HeadacheAccumulator a = TestHelpers.contructAcuteTreatmentNovel(h, new GregorianCalendar(2011,7,31), new GregorianCalendar(2011,9,31),
				 new GregorianCalendar(2011,8,10), new GregorianCalendar(2011,8,11),new GregorianCalendar(2011,9,10));
		 assertEquals(3,a.results.size());
		
		 assertFalse(a.results.get(0).containsKey("unknown"));
		 /*Negative test we must put all possible values to acumulate */
	}
	
	@Test
	public void testAcuteTreatmentAccumulationRemoveDuplicates() throws ParseException {
		
		 ArrayList<Treatment> h = Lists.newArrayList(Treatment.minimal("one", "first"),
				 Treatment.minimal("two", "Second"),Treatment.minimal("three", "Third"),Treatment.minimal("four", "Fourth"), Treatment.minimal("five", "Fifth") );
		 
		 
		 HeadacheAccumulator a = TestHelpers.contructAcuteTreatment(h, new GregorianCalendar(2011,7,31), new GregorianCalendar(2011,9,31),
				 new GregorianCalendar(2011,8,10), new GregorianCalendar(2011,8,11),new GregorianCalendar(2011,9,10));
		
		 List<Map<String, Object>> removeDuplicates = a.postProcess(true, AppHelpers.EMPTY_LIST);
		 assertEquals(3,removeDuplicates.get(0).size());
		 
		 System.out.print(removeDuplicates.get(0).get("axis").toString());
		 assertEquals("1",removeDuplicates.get(2).get("two").toString());
		 System.out.println(a.results.get(0).get("axis").toString());
		 assertTrue(removeDuplicates.get(0).get("axis").toString().startsWith("2011-08-31"));
		 assertEquals("1",removeDuplicates.get(2).get("two").toString());
		 

	}
	
	@Test
	public void testTake5() {
		
		Map<String, Object> in = makeMeAMap();
		
		List<String> res = HeadacheAccumulator.take5(in);
		assertEquals(5, res.size());
		assertEquals("one", res.get(0));
		assertEquals("two", res.get(1));
		assertEquals("seven", res.get(2));
		assertEquals("five", res.get(4));
	
	}
	
	@Test
	public void testConsolidateTop5() {
		
		List<String> tops = Lists.newArrayList("one","two","three");
		Map<String, Object> in = makeMeAMap();
		HeadacheAccumulator.consolidateTop5(tops, in);
		assertEquals(4,in.size());
		assertEquals("5",in.get("one"));
		assertEquals(10,in.get("Other"));

	}


	private static Map<String, Object> makeMeAMap() {
		Map<String,Object> in = Maps.newHashMap();
		in.put("one", "5");
		in.put("two", "5");
		in.put("three", "0");
		in.put("four", "1");
		in.put("five", "2");
		in.put("six", "3");
		in.put("seven", "4");
		return in;
	}
	

}
