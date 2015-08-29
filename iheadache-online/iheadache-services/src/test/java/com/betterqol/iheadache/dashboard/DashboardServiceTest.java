package com.betterqol.iheadache.dashboard;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.repository.HeadacheRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class DashboardServiceTest {
	
	@Autowired
	HeadacheRepository repo;
	
	@Autowired
	DashboardService svc;
	
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
	 }
	
	@Test
	public void testTestStackedBarChart () throws JSONException {
		
		Calendar c =  new GregorianCalendar(2011,7,8);
		String results = svc.testStackedBarChart("foo",c.getTime(), c.getTime());
		
		JSONArray res = new JSONArray(results);
		assertEquals(12,res.length());
		JSONObject oo = res.getJSONObject(0);
		assertEquals("axis",oo.names().get(0));
		assertEquals("Unclassified Headache",oo.names().get(4));
		
	}
	
	@Test
	public void testGetAcuteTreatmentsStacked() throws IOException, ParseException, JSONException {
		
	/*	TestHelpers.persistSomeHeadaches(repo,"test",null,null,new GregorianCalendar(2011,7,8), new GregorianCalendar(2011,7,14),new GregorianCalendar(2011,7,29), new GregorianCalendar(2011,8,29));
		
		Calendar c =  new GregorianCalendar(2011,7,8);
		Calendar c2 =  new GregorianCalendar(2011,9,30);
		
		String results = svc.getAcuteTreatmentsStacked("test",c.getTime(), c2.getTime(),SliceType.MONTH );
		System.out.print(results);
		JSONArray res = new JSONArray(results);
		assertEquals(3,res.length());
		JSONObject oo = res.getJSONObject(0);
		assertEquals("axis",oo.names().get(0));
		System.out.print(res.toString()); */


	}
	
	@Test
	public void testGetSymptomsStacked() throws IOException, ParseException, JSONException {
		
		TestHelpers.persistSomeHeadaches(repo,"test",null,null,new GregorianCalendar(2011,7,8), new GregorianCalendar(2011,7,14),new GregorianCalendar(2011,7,29), new GregorianCalendar(2011,8,29));
		
		Calendar c =  new GregorianCalendar(2011,7,8);
		Calendar c2 =  new GregorianCalendar(2011,9,30);
		
		String results = svc.getSymptomsStacked("test",c.getTime(), c2.getTime(),SliceType.MONTH );
		System.out.print(results);
		JSONArray res = new JSONArray(results);
		assertEquals(3,res.length());
		JSONObject oo = res.getJSONObject(0);
		assertEquals("axis",oo.names().get(0));
		System.out.print(res.toString());


	}
	

	


}
