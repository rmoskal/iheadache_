package com.betterqol.iheadache.extensions;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.ektorp.Page;
import org.ektorp.PageRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.YesNoResponse;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class PagingAdapterTest extends BaseBigTest{
	
	@Autowired
	PagingAdapter srvc;
	
	@Test
	public void testConstructFilter(){
		Multimap<String, String> res = PagingAdapter.constructFilter(new int[]{0,1});
		assertEquals(2,res.size());
		assertTrue(res.containsKey("hasTreatments"));	
		List<String> vals = Lists.newArrayList(res.get("hasTreatments"));
		assertEquals("false",vals.get(0));
		assertTrue(res.containsKey("hasSymptoms"));	
		vals = Lists.newArrayList(res.get("hasSymptoms"));
		assertEquals("false",vals.get(0));

	}
	
	@Test
	public void testConstructFilterMulti(){
		Multimap<String, String> res = PagingAdapter.constructFilter(new int[]{6,7});
		assertEquals(2,res.size());
		assertTrue(res.containsKey("kind"));	
		List<String> vals = Lists.newArrayList(res.get("kind"));
		assertEquals(2,vals.size());
		assertEquals("Migraine Headache",vals.get(0));
		assertEquals("Probable Migraine",vals.get(1));
	}
	
	
	@Test
	public void testFilteringPredicate() {
		
		ImmutableMap<String,String> input = ImmutableMap.of("hasTreatments", "false", "hasTriggers", "true");
		boolean results = PagingAdapter.HEADACHE_UI_CRTIRIA(new int[]{}).apply(input);
		assertTrue(results);
		results = PagingAdapter.HEADACHE_UI_CRTIRIA(new int[]{0}).apply(input);
		assertTrue(results);
		input = ImmutableMap.of("hasTreatments", "true", "hasTriggers", "true");
		results = PagingAdapter.HEADACHE_UI_CRTIRIA(new int[]{0}).apply(input);
		assertFalse(results);		
	}
	
	@Test
	public void testFilteringPredicateMulti() {
		
		ImmutableMap<String,String> input = ImmutableMap.of("kind", "Migraine Headache");
		boolean results = PagingAdapter.HEADACHE_UI_CRTIRIA(new int[]{}).apply(input);
		assertTrue(results);
		results = PagingAdapter.HEADACHE_UI_CRTIRIA(new int[]{6}).apply(input);
		assertTrue(results);
		results = PagingAdapter.HEADACHE_UI_CRTIRIA(new int[]{7}).apply(input);
		assertFalse(results);
		results = PagingAdapter.HEADACHE_UI_CRTIRIA(new int[]{6,7}).apply(input);
		assertTrue(results);
	}
	
	@Test
	public void testGET_COMPLETIONINFO() throws JSONException {
		
		Headache o = TestHelpers.createFullHeadache(new GregorianCalendar(), false, "foo", null, null);
		Map results = srvc.checkForCompletion(o);
		assertEquals(true,results.get("hasTreatments"));
		//assertTrue(((String)results.get(HeadacheMap.kind.toString())).startsWith("HEADACHE_TYPE_"));
		assertEquals(true,results.get("hasSymptoms"));
		assertEquals(true,results.get("hasTriggers"));
		assertEquals(true,results.get("hasNote"));
		assertEquals(true,results.get("hasPains"));
		assertEquals(true,results.get("hasDisability")); 
		
	}
	
	
	@Test
	public void testGET_COMPLETIONINFO2() throws JSONException {
		Headache o = TestHelpers.createFullHeadache(new GregorianCalendar(), false, "foo", null, null);
		o.setDisability(null);
		o.getPains().clear();
		o.setNote("Hi");
		o.setSymptoms(null);
		o.getTriggers().clear();
		o.setTreatments(null);
		Map results = srvc.checkForCompletion(o);
		assertEquals(false,results.get("hasTreatments"));
		assertEquals(false,results.get("hasSymptoms"));
		assertEquals(false,results.get("hasTriggers"));
		assertEquals(true,results.get("hasNote"));
		assertEquals(false,results.get("hasPains"));
		assertEquals(false,results.get("hasDisability")); 
		
	}
	
	
	@Test
	public void testCheckForCompletion() throws JSONException {
		Headache o = TestHelpers.createFullHeadache(new GregorianCalendar(), false, "foo", null, null);
		o.getDisability().setCompletelyDisabled(0);
		o.getDisability().setPartiallyDisabled(0);
		System.out.println(o.getDisability().getResponses());
		for (YesNoResponse each:o.getDisability().getResponses()){
			each.setNo(false);
			each.setYes(false);
		}
			
		Map results = srvc.checkForCompletion(o);
		assertEquals(false,results.get("hasDisability")); 
		
	}
	
	@Test
	public void testPagingA_1() throws JSONException {
		List<Headache> hh = makeGetSome(new int[] {0,1,2,3,4,5});
		Map results = srvc.checkForCompletion(hh.get(0));
		assertEquals(false,results.get("hasSymptoms"));
		
		checkReturns1();
			
	}
	
	@Test
	public void testPagingA_2() throws JSONException {
		makeGetSome(new int[] {0,5,7,8,12,13});
		checkReturns1();
			
	}
	
	@Test
	public void testPagingA_3() throws JSONException {
		makeGetSome(new int[] {0,5,7,12,13,14});
		checkReturns1();
			
	}
	
	@Test
	public void testPagingA_4() throws JSONException {
		makeGetSome(new int[] {5,7,10,12,13,14});	
		checkReturns1();
			
	}
	
	
	@Test
	public void testPagingB_1() throws JSONException {
		List<Headache> hh = makeGetSome(new int[] {0,5,7,11,12,13,14});
		Map results = srvc.checkForCompletion(hh.get(0));
		assertEquals(false,results.get("hasSymptoms"));
		
		checkReturns2();
			
	}
	
	@Test
	public void testPagingB_2() throws JSONException {
		List<Headache> hh = makeGetSome(new int[] {0,1,2,3,4,5,6});
		Map results = srvc.checkForCompletion(hh.get(0));
		assertEquals(false,results.get("hasSymptoms"));
		
		checkReturns2();
			
	}
	
	
	@Test
	public void testPagingB_3() throws JSONException {
		List<Headache> hh = makeGetSome(new int[] {0,1,6,7,4,11,14});
		Map results = srvc.checkForCompletion(hh.get(0));
		assertEquals(false,results.get("hasSymptoms"));
		
		checkReturns2();
			
	}
	
	@Test
	public void testPagingB_4() throws JSONException {
		List<Headache> hh = makeGetSome(new int[] {0,1,2,11,12,13,14});
		Map results = srvc.checkForCompletion(hh.get(0));
		assertEquals(false,results.get("hasSymptoms"));
		
		checkReturns2();
			
	}
	

	private void checkReturns1() throws JSONException {

		System.out.println("one");

		Page res1 = srvc.getHeadachePage("foo",  new GregorianCalendar(2010,11,25).getTime(),  new GregorianCalendar(2011,3,31).getTime(),
				PageRequest.firstPage(3), new int[]{1},null);
		
		System.out.println("two");
		Page res2 = srvc.getHeadachePage("foo",  new GregorianCalendar(2010,11,25).getTime(),  new GregorianCalendar(2011,3,31).getTime(), 
				res1.getNextPageRequest(), new int[]{1},null);
		
		
		assertEquals(3,res1.getRows().size());
		assertEquals(3,res2.getRows().size());
		assertFalse (getId(res1.getRows().get(0)).equals(getId(res2.getRows().get(0))));
		assertFalse (getId(res1.getRows().get(1)).equals(getId(res2.getRows().get(1))));
		assertFalse (getId(res1.getRows().get(2)).equals(getId(res2.getRows().get(2))));
		
		if (!res2.isHasNext()) 
			return;
		
		res2 = srvc.getHeadachePage("foo",  new GregorianCalendar(2010,11,25).getTime(),  new GregorianCalendar(2011,3,31).getTime(), 
					res2.getNextPageRequest(),new int[]{1},new int[]{});
		
		//assertTrue (res2.getNextPageRequest()==null);
		assertEquals(0,res2.getRows().size());
	}
	
	private void checkReturns2() throws JSONException {

		System.out.println("one");

		PageRequest firstPage = PageRequest.firstPage(3);
		Page res1 = srvc.getHeadachePage("foo",  new GregorianCalendar(2010,11,25).getTime(),  new GregorianCalendar(2011,3,31).getTime(), 
				firstPage, new int[]{1},null);
		
		System.out.println("two");
		Page res2 = srvc.getHeadachePage("foo",  new GregorianCalendar(2010,11,25).getTime(),  new GregorianCalendar(2011,3,31).getTime(), 
				res1.getNextPageRequest(), new int[]{1},null);
		
		System.out.println("three");
		Page res3 = srvc.getHeadachePage("foo",  new GregorianCalendar(2010,11,25).getTime(),  new GregorianCalendar(2011,3,31).getTime(), 
				res2.getNextPageRequest(), new int[]{1},null);

		//assertTrue (res3.getNextLink()==null);
		
		assertEquals(3,res1.getRows().size());
		assertEquals(3,res2.getRows().size());
		assertEquals(1,res3.getRows().size());
		assertFalse (getId(res1.getRows().get(0)).equals(getId(res2.getRows().get(0))));
		assertFalse (getId(res1.getRows().get(1)).equals(getId(res2.getRows().get(1))));
		assertFalse (getId(res1.getRows().get(2)).equals(getId(res2.getRows().get(2))));
		
		assertTrue (res3.getNextPageRequest()==null);
	}

	private List<Headache> makeGetSome(int[] which) {
		TestHelpers.createLookups(db);
		TestHelpers.persistSomeHeadaches(repo, "foo",lookups,treatments,
				 new GregorianCalendar(2010,11,26),
				 new GregorianCalendar(2011,0,28),
				 new GregorianCalendar(2011,1,28),
				 new GregorianCalendar(2011,2,31));
		
		List<Headache> hh = repo.getPagedDateRange("foo",  new GregorianCalendar(2010,11,25).getTime(),  
					new GregorianCalendar(2011,3,31).getTime(), PageRequest.firstPage(100), "by_date").getRows();
		
		for (Headache h : hh){
			System.out.println(h.getId());
		}
		
		for (int each: which) {
			hh.get(each).getSymptoms().clear();
			repo.update(hh.get(each));
		 }
		
		return hh;
	}
	
	private static String getId(Object o) {
		Map _o = (Map)o;
		return _o.get("id").toString();
	}
}
