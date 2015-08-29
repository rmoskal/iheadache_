package com.betterqol.iheadache.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.AppHelpers;
import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.IHeadacheDataLoader;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.model.dto.ProfileItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml" })
public class ProfileHelperTest extends BaseBigTest {

	static ArrayList<ProfileItem> in = Lists.newArrayList(
			new ProfileItem("one","Item One",true,false),
			new ProfileItem("two","Item Two",false,false),
			new ProfileItem("three","Item Three",true,true)
			);



	@Test
	public void testTriggerProfile() throws JsonGenerationException, JsonMappingException, JSONException, IOException {

		Resource r = new ClassPathResource("init_lookup_data_production.json");
		IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		loader.setDataPath(r);

		loader.init();

		UserInformation u = TestHelpers.makeUser("foo");
		u.setId("f65762e9-11b1-4965-a3c5-913c13af078b");
		up.createTestUser(u);

		JSONArray res = AppHelpers.constructProfile(u.getTriggerProfile(), lookups.findByLookupType(Kind.TRIGGER), false);
		JSONObject res2 = res.getJSONObject(0);
		System.out.println(res2.toString());
		assertEquals("TRIGGER_01",res2.get("id"));
		assertEquals(true,res2.get("in"));
		res2 = res.getJSONObject(2);
		assertEquals("TRIGGER_03",res2.get("id"));
		assertEquals(false,res2.get("in"));
		
	}
	
	@Test
	public void testSimpleJson() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		
		UserPrincipal u = TestHelpers.makePrincipal("foo");
		pp.create(u);
		u = TestHelpers.makePrincipal("bar");
		pp.create(u);
		JSONArray res = AppHelpers.simpleJson(pp.getAll(), "password");
		assertEquals(2, res.length());
		assertFalse(res.getJSONObject(0).has("password"));
	}
	
	@Test
	public void testListJoining() throws JSONException {
		
		Map <String,String> hey = Maps.newHashMap();
		hey.put("one", "one");
		
		JSONObject o = new JSONObject(hey);
		JSONArray res = new JSONArray();
		res.put(o);
		
		assertEquals(1, res.length());
		
		JSONArray res2 = new JSONArray();
		res2.put(o);
		res2.put(o);
	
		res.put(res2);
		assertEquals(2, res.length()); //Does not join lists !!!!!!!!!!!!!
	}
	
	@Test
	public void testMergeJSONArrays() throws JSONException {
		
		Map <String,String> hey = Maps.newHashMap();
		hey.put("one", "one");
		
		JSONObject o = new JSONObject(hey);
		JSONArray res = new JSONArray();
		res.put(o);
		
		assertEquals(1, res.length());
		
		JSONArray res2 = new JSONArray();
		res2.put(o);
		res2.put(o);
		
		res = AppHelpers.mergeJSONArrays(res, res2);
	
		assertEquals(3, res.length()); 
	}
	
	
	@Test
	public void testSplitResponse() {
		
		List<LookupDTO>active =  Lists.newArrayList();
		List<LookupDTO>others =  Lists.newArrayList();
		
		AppHelpers.splitResponse(in, active, others, false);
		
		assertEquals(2,active.size());
		assertEquals(3,others.size());	
	}
	
	@Test
	public void testSplitResponseCustom() {
		
		List<LookupDTO>active =  Lists.newArrayList();
		List<LookupDTO>others =  Lists.newArrayList();
		
		AppHelpers.splitResponse(in, active, others, true);
		
		assertEquals(2,active.size());
		assertEquals(1,others.size());	
	}


}
