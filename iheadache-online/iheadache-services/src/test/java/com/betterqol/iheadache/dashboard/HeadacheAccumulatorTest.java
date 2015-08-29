package com.betterqol.iheadache.dashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.AppHelpers;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.dashboard.HeadacheAccumulator.Accumulater;
import com.betterqol.iheadache.model.CouchLookup;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.betterqol.iheadache.resource.DashboardResource;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@SuppressWarnings("rawtypes")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class HeadacheAccumulatorTest {
	
	@Autowired
	HeadacheRepository repo;
	
	@Autowired
	DashboardResource svc;
	
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
	 }
	
	@Test
	public void testStringSplit(){
		String[] res = "2011-08-08T04:00:00.000+0000".split("T");
		assertEquals("2011-08-08",res[0]);
	}
	
	@Test
	public void testLoadMap() {
		 List<String> in = Lists.newArrayList(Iterables.transform(TestHelpers.makeSomeLookups(CouchLookup.Kind.HEADACHE_TYPE),RepositoryHelpers.GET_IDS));
		 Map<String, Object> res = DashboardHelpers.loadMap(in);
		 assertEquals(5,res.keySet().size());
		 Object[] res2 = res.keySet().toArray();
		 assertEquals("axis", res2[0].toString());
		 assertEquals("one", res2[1].toString());
		 assertEquals("four", res2[4].toString());
		
	}
	

	@Test
	public void testAccumulation() throws IOException, ParseException {
		
		persistSomeHeadaches();
		
		
		HeadacheAccumulator accummulator = new HeadacheAccumulator(TestHelpers.makeSomeLookups(CouchLookup.Kind.HEADACHE_TYPE), Accumulater.byCount);
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2011,7,8).getTime(),new GregorianCalendar(2011,8,29).getTime());
		assertEquals(3,data.size());
		
		Map<String, Object> results = accummulator.accumulate(new DateTime(2011,8,31,0,0,0,0),
				data.listIterator(), new String[] {HeadacheMap.kind.toString()});
		
		assertEquals("1",results.get("one").toString());
		assertEquals("1",results.get("two").toString());
		assertEquals("0",results.get("three").toString()); 
		assertEquals("2",results.get("count").toString()); 
		
	}
	
	@Test
	public void testMinimalAccumulation() throws IOException, ParseException {
		
		Calendar c =  new GregorianCalendar(2012,0,31);
		Headache o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		o.setKind(new LookupDTO("one","one"));
		repo.create(o);
		
		List<DateTime> chunks = DashboardHelpers.chunk(new GregorianCalendar(2011,11,1), 
				new GregorianCalendar(2012,2,31), SliceType.MONTH);
				

		
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2011,11,31).getTime(),new GregorianCalendar(2012,2,31).getTime());
		assertEquals(1,data.size());
		
		HeadacheAccumulator accummulator = new HeadacheAccumulator(TestHelpers.makeSomeLookups(CouchLookup.Kind.HEADACHE_TYPE), Accumulater.byCount);
		
		accummulator.accumulate(chunks,data, new String[]{HeadacheMap.kind.toString()});
		Map<String, Object> results = accummulator.results.get(0);
		//assertEquals("1",results.get("one").toString());
		assertEquals(new DateTime(2011,12,31,0,0,0,0).toDate(),((DateTime)results.get("axis")).toDate()); 
		
		results = accummulator.results.get(1);
		assertEquals("1",results.get("one").toString());
		assertEquals(new DateTime(2012,1,31,0,0,0,0).toDate(),((DateTime)results.get("axis")).toDate()); 
	}
	
	
	
	
	@Test
	public void testAcculumationFull() throws IOException, ParseException {
		
		persistSomeHeadaches();
		
		
		HeadacheAccumulator accummulator = new HeadacheAccumulator(TestHelpers.makeSomeLookups(CouchLookup.Kind.HEADACHE_TYPE), Accumulater.byCount);
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2011,7,8).getTime(),new GregorianCalendar(2011,8,29).getTime());
		assertEquals(3,data.size());
		
		List<DateTime> chunks = Lists.newArrayList(
				new DateTime(2011,8,31,0,0,0,0),
				new DateTime(2011,9,30,0,0,0,0));
		
		accummulator.accumulate(chunks,data, new String[]{HeadacheMap.kind.toString()});
		
		Map<String, Object> results = accummulator.results.get(0);
		
		assertEquals("1",results.get("one").toString());
		assertEquals("1",results.get("two").toString());
		assertEquals("0",results.get("three").toString());
		assertEquals(new DateTime(2011,8,31,0,0,0,0),results.get("axis")); 
		
		results = accummulator.results.get(1);
		
		assertEquals("1",results.get("one").toString());
		assertEquals("0",results.get("two").toString());
		assertEquals("0",results.get("three").toString());
		assertEquals(new DateTime(2011,9,30,0,0,0,0),results.get("axis")); 
		
	}
	
	
	
	@Test
	public void testAcculumation2() throws IOException, ParseException {
		
		persistSomeHeadaches();
		
		
		HeadacheAccumulator accummulator = new HeadacheAccumulator(TestHelpers.makeSomeLookups(CouchLookup.Kind.HEADACHE_TYPE), Accumulater.byCount);
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2011,7,8).getTime(),new GregorianCalendar(2011,8,29).getTime());
		assertEquals(3,data.size());
		
		List<DateTime> chunks = Lists.newArrayList(
				new DateTime(2011,8,31,0,0,0,0),
				new DateTime(2011,9,30,0,0,0,0));
		
		accummulator.accumulate(chunks,data, new String[]{HeadacheMap.kind.toString()});
		
		assertEquals(2, accummulator.postProcess(false, AppHelpers.EMPTY_LIST).size());
		
		Map<String, Object> results = accummulator.postProcess(false, AppHelpers.EMPTY_LIST).get(0);
		
		assertEquals("1",results.get("one").toString());
		assertEquals("1",results.get("two").toString());
		assertEquals("0",results.get("three").toString());
		assertEquals("2",results.get("count").toString());
		System.out.println(results);
		assertEquals(new DateTime(2011,8,31,0,0,0,0),results.get("axis")); 
		
		results = accummulator.postProcess(false, AppHelpers.EMPTY_LIST).get(1);
		
		assertEquals("1",results.get("one").toString());
		assertEquals("0",results.get("two").toString());
		assertEquals("0",results.get("three").toString());
		assertEquals("1",results.get("count").toString());
		System.out.println(results);
		assertEquals(new DateTime(2011,9,30,0,0,0,0),results.get("axis")); 
		
		results = accummulator.postProcess(false, "count").get(1);
		assertFalse(results.containsKey("count"));
		
	}
	
	
	@Test
	/**
	 * Tests the midas style accumulation
	 * @throws IOException
	 * @throws ParseException
	 */
	public void testAccumulationByValue() throws IOException, ParseException {
		
		persistSomeHeadaches();
		
		
		HeadacheAccumulator accummulator = new HeadacheAccumulator(Accumulater.byValue, HeadacheMap.midas.toString());
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2011,7,8).getTime(),new GregorianCalendar(2011,8,29).getTime());
		assertEquals(3,data.size());
		
		Map<String, Object> results = accummulator.accumulate(new DateTime(2011,7,31,0,0,0,0),
				data.listIterator(), new String[] {HeadacheMap.midas.toString()});
		System.out.print(results.get("midas").toString());
	//	assertTrue(Integer.valueOf(results.get("midas").toString()).compareTo(20) == 1);
	}	
	
	@Test
	/**
	 * Suitable for disablility
	 * @throws IOException
	 * @throws ParseException
	 */
	public void testAccumulationByValue2() throws IOException, ParseException {
		
		persistSomeHeadaches();
		
		
		HeadacheAccumulator accummulator = new HeadacheAccumulator(Accumulater.byValue, HeadacheMap.completelyDisabled.toString(),HeadacheMap.partiallyDisabled.toString());
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2011,7,8).getTime(),new GregorianCalendar(2011,8,29).getTime());
		assertEquals(3,data.size());
		
		Map<String, Object> results = accummulator.accumulate(new DateTime(2011,7,31,0,0,0,0),
				data.listIterator(), new String[] {HeadacheMap.completelyDisabled.toString(),HeadacheMap.partiallyDisabled.toString() });
		assertTrue(Integer.valueOf(results.get("completelyDisabled").toString()).compareTo(-1) == 1);
		assert(Integer.valueOf(results.get("partiallyDisabled").toString()).compareTo(-1) == 1);
	
	}
	
	
	
	
	
	
	
	
	@Test
	/**
	 * Suitable for new dashboard graphs
	 * @throws IOException
	 * @throws ParseException
	 */
	public void testAccumulationForTotalGraphs() throws IOException, ParseException {
		
		persistSomeHeadaches();
		HeadacheAccumulator accummulator = new HeadacheAccumulator(TestHelpers.makeSomeLookups(CouchLookup.Kind.HEADACHE_TYPE), Accumulater.byCount);
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2011,7,8).getTime(),new GregorianCalendar(2011,8,29).getTime());
		assertEquals(3,data.size());
		
		List<DateTime> chunks = Lists.newArrayList(
				new DateTime(2011,9,30,0,0,0,0));
		
		accummulator.accumulate(chunks,data, new String[]{HeadacheMap.kind.toString()});
		
		assertEquals(1, accummulator.postProcess(false, AppHelpers.EMPTY_LIST).size());
		
		Map<String, Object> results = accummulator.postProcess(false, AppHelpers.EMPTY_LIST).get(0);
		
		assertEquals("2",results.get("one").toString());
		assertEquals("1",results.get("two").toString());
		assertEquals("0",results.get("three").toString());
		assertEquals("3",results.get("count").toString());
		System.out.println(results);
		assertEquals(new DateTime(2011,9,30,0,0,0,0),results.get("axis")); 
	
	}
	
	
	@Test
	/**
	 * Suitable for new dashboard graphs
	 * @throws IOException
	 * @throws ParseException
	 */
	public void testUnwindingTotalGraphs() throws IOException, ParseException, JSONException {
		
		persistSomeHeadaches();
		HeadacheAccumulator accummulator = new HeadacheAccumulator(TestHelpers.makeSomeLookups(CouchLookup.Kind.HEADACHE_TYPE), Accumulater.byCount);
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2011,7,8).getTime(),new GregorianCalendar(2011,8,29).getTime());
		assertEquals(3,data.size());
		
		List<DateTime> chunks = Lists.newArrayList(
				new DateTime(2011,9,30,0,0,0,0));
		
		accummulator.accumulate(chunks,data, new String[]{HeadacheMap.kind.toString()});

		
		JSONArray results = accummulator.postProcessJson(false, "axis","count");
		assertEquals(4,results.length());
		assertTrue(((JSONObject)results.get(0)).has("name"));
		
	}
	
	
	@Test
	public void testAcculumation9() throws IOException, ParseException, JSONException {
		
		persistSomeHeadaches();
		
		
		HeadacheAccumulator accummulator = new HeadacheAccumulator(TestHelpers.makeSomeLookups(CouchLookup.Kind.HEADACHE_TYPE), Accumulater.byCount);
		List<Map> data = repo.getDateRangeDTO("foo",new GregorianCalendar(2011,7,8).getTime(),new GregorianCalendar(2011,8,29).getTime());
		assertEquals(3,data.size());
		
		List<DateTime> chunks = Lists.newArrayList(
				new DateTime(2011,8,31,0,0,0,0),
				new DateTime(2011,9,30,0,0,0,0));
		
		accummulator.accumulate(chunks,data, new String[]{HeadacheMap.kind.toString()});
		
		assertEquals(2, accummulator.postProcessJson2(false, "axis","count").size());
	}
		
	
	
	
	
	
	

	private void persistSomeHeadaches() {
		Calendar c =  new GregorianCalendar(2011,7,8);
		Headache o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		o.setKind(new LookupDTO("one","one"));
		o.setNote("one");
		repo.create(o);
		c.add(Calendar.DAY_OF_MONTH, 3);
	    o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		o.setKind(new LookupDTO("two", "two"));
		o.setNote("two");
		repo.create(o);
		c.add(Calendar.MONTH, 1);
		o = TestHelpers.createMinHeadache(c.getTime(), "foo");
		o.setKind(new LookupDTO("one","one"));
		o.setNote("three");
		repo.create(o);
	}
	
	

	
	

	

}
