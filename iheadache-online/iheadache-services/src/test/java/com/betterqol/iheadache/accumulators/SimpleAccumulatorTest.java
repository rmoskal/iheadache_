package com.betterqol.iheadache.accumulators;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers;
import com.betterqol.iheadache.model.Headache;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class SimpleAccumulatorTest extends BaseBigTest {
	
	
	@Test
	public void testBasic() throws IOException, ParseException {
		
		TestHelpers.persistSomeHeadaches(repo, new DateTime(2011, 12, 30, 0, 0,
				0, 0), new DateTime(2012, 1, 1, 0, 0, 0, 0), new DateTime(2012,
				1, 1, 0, 0, 0, 0), new DateTime(2012, 1, 3, 0, 0, 0, 0));
		
		List<Headache> data = repo.getDateRange("foo", new GregorianCalendar(
				2011, 11, 30).getTime(), new GregorianCalendar(2012, 0, 30)
				.getTime());
		assertEquals(4, data.size());
		List<Map> in = Lists.newArrayList(Iterables.transform(data,DashboardHelpers.GET_NOTES));
		SimpleAccumulator a = new SimpleAccumulator();
		List<DateTime> chunks = Lists.newArrayList(
				new DateTime(2011,12,31,0,0,0,0),
				new DateTime(2012,1,30,0,0,0,0));
		a.accumulate(chunks, in);
		assertEquals(2, a.results.size());
		assertEquals(1, a.results.get(0).size());
		assertEquals(3, a.results.get(1).size());
		System.out.println(a.results.get(1));
			
	}
	

	@Test
	public void testToJSON() throws IOException, ParseException {
		TestHelpers.persistSomeHeadaches(repo, new DateTime(2011, 12, 30, 0, 0,
				0, 0), new DateTime(2012, 1, 1, 0, 0, 0, 0), new DateTime(2012,
				1, 1, 0, 0, 0, 0), new DateTime(2012, 1, 3, 0, 0, 0, 0));
		
		List<Headache> data = repo.getDateRange("foo", new GregorianCalendar(
				2011, 11, 30).getTime(), new GregorianCalendar(2012, 0, 30)
				.getTime());
		List<Map> in = Lists.newArrayList(Iterables.transform(data,DashboardHelpers.GET_NOTES));
		SimpleAccumulator a = new SimpleAccumulator();
		List<DateTime> chunks = Lists.newArrayList(
				new DateTime(2011,12,31,0,0,0,0),
				new DateTime(2012,1,30,0,0,0,0));
		a.accumulate(chunks, in);
		List<List<JSONObject>> res = a.getJson();
		assertEquals(2, res.size());
		assertEquals(1, res.get(0).size());
		assertEquals(3, res.get(1).size());
		System.out.println(a.results.get(1));
	}
	
	
	

}
