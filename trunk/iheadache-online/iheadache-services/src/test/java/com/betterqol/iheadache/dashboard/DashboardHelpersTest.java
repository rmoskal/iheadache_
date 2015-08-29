package com.betterqol.iheadache.dashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.joda.time.DateTime;
import org.junit.Test;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.HeadacheTreatment;
import com.betterqol.iheadache.model.PreventativeTreatment;
import com.betterqol.iheadache.model.Treatment;
import com.betterqol.iheadache.model.dto.Counter;
import com.google.common.collect.Lists;


public class DashboardHelpersTest {
	
	@Test
	public void testConvertHeadacheTreatmentsToMap() {
		
		Map res = DashboardHelpers.GET_TREATMENTS.apply(TestHelpers.createFullHeadache(false, "test", null, null));
		assertEquals(4, res.size());
	}
	
	@Test
	public void testConvertHeadacheTreatmentCountToMap() {
		
		Headache h = TestHelpers.createFullHeadache(false, "test", null, null);
		h.getTreatments().add(new HeadacheTreatment(new DateTime(), new Treatment("one")));
		assertEquals("one",h.getTreatments().get(2).getTreatment().getId());
		Map res = DashboardHelpers.GET_TREATMENT_COUNT.apply(h);
		assertEquals(4, res.size());
		assertEquals(2,res.get("one"));
	}
	
	@Test
	public void testConvertHeadachePainLocationCountToMap() {
		
		Headache h = TestHelpers.createFullHeadache(false, "test", null, null);
		h.getTreatments().add(new HeadacheTreatment(new DateTime(), new Treatment("one")));
		assertEquals("one",h.getTreatments().get(2).getTreatment().getId());
		Map res = DashboardHelpers.GET_PAINLOCATION_COUNT.apply(h);
		assertEquals(4, res.size());
		assertEquals(2,res.get("TEMPLE_LEFT"));
		assertEquals(2,res.get("HEAD_RIGHT"));	

	}
	
	
	@Test
	public void testConvertHeadachePainLocationSeverityToMap() {
		
		Headache h = TestHelpers.createFullHeadache(false, "test", null, null);
		h.getTreatments().add(new HeadacheTreatment(new DateTime(), new Treatment("one")));
		assertEquals("one",h.getTreatments().get(2).getTreatment().getId());
		Map res = DashboardHelpers.GET_PAINLOC_SEVERITY.apply(h);
		assertEquals(4, res.size());
		assertEquals(10f,res.get("TEMPLE_LEFT"));
		assertEquals(10f,res.get("HEAD_RIGHT"));	
	}
	
	@Test
	public void testConvertHeadachePainTypeCountToMap() {
		
		Headache h = TestHelpers.createFullHeadache(false, "test", null, null);
		h.getTreatments().add(new HeadacheTreatment(new DateTime(), new Treatment("one")));
		assertEquals("one",h.getTreatments().get(2).getTreatment().getId());
		Map res = DashboardHelpers.GET_PAINTYPE_COUNT.apply(h);
		assertEquals(3, res.size());
		assertEquals(2,res.get("THROBBING"));
	}
	
	@Test
	public void testConvertHeadachePainTypeSeverityToMap() {
		
		Headache h = TestHelpers.createFullHeadache(false, "test", null, null);
		h.getTreatments().add(new HeadacheTreatment(new DateTime(), new Treatment("one")));
		assertEquals("one",h.getTreatments().get(2).getTreatment().getId());
		Map res = DashboardHelpers.GET_PAINTYPE_SEVERITY.apply(h);
		assertEquals(3, res.size());
		assertEquals(10f,res.get("THROBBING"));
	}
	
	
	@Test
	public void testConvertDisabilityToMap() {
		
		Headache h = TestHelpers.createFullHeadache(false, "test", null, null);
		assertEquals(2,h.getDisability().getResponses().size());
		Map res = DashboardHelpers.GET_DISABILITY_COUNT.apply(h);
		assertEquals(3, res.size());
		assertEquals(1,res.get("DISABILITY_QUESTION_1"));
	}
	
		
	@Test
	public void testConvertHeadacheTreatmentsToMapDateHandling() {
		
		Map res = DashboardHelpers.GET_TREATMENTS.apply(TestHelpers.createFullHeadache(false, "test", null, null));
		assertTrue(res.containsKey(HeadacheMap.start.toString()));
		
	}
	
	@Test
	public void testGatherKeys() {
		
		Collection<Counter> aCount = Lists.newArrayList(new Counter("one", 1),new Counter("one", 0));
		assertEquals(.5f,DashboardHelpers.gatherKeys(aCount),0);
		
	}
	
	@Test
	public void testAddSuffix() {
		
		List<String> res = DashboardHelpers.addSuffix(Lists.newArrayList("one", "two"),"_avg");
		assertEquals(4,res.size());
		assertEquals("one_avg",res.get(1));
	}
	
	@Test
	public void testMakeSorted() throws JSONException {
		
		/*JSONObject o = new JSONObject();
		o.put("one", 9);
		o.put("two", 1);
		HashMap<String, String> lookupValues = new HashMap<String, String>();
		lookupValues.put("two", "WTF");
		JSONArray res = DashboardHelpers.makeSorted(o, lookupValues );
		JSONObject oo = res.getJSONObject(0);
		assertEquals("WTF",oo.get("name"));	*/
	}
	
	@Test
	public void testEnMapTreatments() {
		
		List<Treatment> teats = Lists.newArrayList(new Treatment("1","heyho", "generichey", "","", false));
		Map<String, String> t2 = DashboardHelpers.enMapGenericName(teats);
		assertEquals("generichey",t2.get("1"));	
			
	}
	
	@Test
	public void testEnMapPTreatments() {
		
		List<PreventativeTreatment> teats = Lists.newArrayList(new PreventativeTreatment("1","heyho", "generichey"));
		Map<String, String> t2 = DashboardHelpers.enMapPGenericName(teats);
		assertEquals("generichey",t2.get("1"));	
			
	}

}
