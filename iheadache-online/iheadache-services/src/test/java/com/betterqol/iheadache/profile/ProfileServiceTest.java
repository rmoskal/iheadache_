package com.betterqol.iheadache.profile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.IHeadacheDataLoader;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.PreventativeTreatment;
import com.betterqol.iheadache.model.Treatment;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.model.dto.PreventativeTreatmentProfileItem;
import com.betterqol.iheadache.model.dto.TreatmentProfileItem;
import com.betterqol.iheadache.security.DuplicateUsernameException;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml" })
public class ProfileServiceTest extends BaseBigTest {

	@Autowired
	private MessageDigestPasswordEncoder encoder;

	@Test
	public void testTriggerProfile() throws JsonGenerationException,
			JsonMappingException, JSONException, IOException {

		Resource r = new ClassPathResource("init_lookup_data_production.json");
		IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		loader.setDataPath(r);

		loader.init();

		UserInformation u = TestHelpers.makeUser("foo");
		u.setId("u_f65762e9-11b1-4965-a3c5-913c13af078b");
		up.createTestUser(u);

		JSONArray res = serviceToTest
				._getTriggers("f65762e9-11b1-4965-a3c5-913c13af078b");
		assertEquals(10, res.length());

		for (int i = 0; i < res.length(); i = i + 1) {

			JSONObject o = res.getJSONObject(i);
			System.out.println(o.toString());
			if (o.getString("id").equals("XX")) {
				assertEquals(true, o.getBoolean("custom"));
				assertEquals(false, o.getBoolean("in"));
			}

			if (o.getString("id").equals("XXX")) {
				assertEquals(true, o.getBoolean("custom"));
				assertEquals(true, o.getBoolean("in"));
			}

			if (o.getString("id").equals("TRIGGER_01")) {
				assertEquals(false, o.getBoolean("custom"));
				assertEquals(true, o.getBoolean("in"));
			}

		}
	}

	@Test
	public void testSymptomProfile() throws JsonGenerationException,
			JsonMappingException, JSONException, IOException {

		Resource r = new ClassPathResource("init_lookup_data_production.json");
		IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		loader.setDataPath(r);

		loader.init();

		UserInformation u = TestHelpers.makeUser("foo");
		u.setId("u_f65762e9-11b1-4965-a3c5-913c13af078b");
		up.createTestUser(u);

		JSONArray res = serviceToTest
				._getCustomSymptoms("f65762e9-11b1-4965-a3c5-913c13af078b");
		assertEquals(2, res.length());

		for (int i = 0; i < res.length(); i = i + 1) {

			JSONObject o = res.getJSONObject(i);
			System.out.println(o.toString());
			if (o.getString("id").equals("yy")) {
				assertEquals(false, o.getBoolean("in"));
			}

			if (o.getString("id").equals("yyy")) {
				assertEquals(true, o.getBoolean("in"));
			}

		}
	}

	@Test
	public void testAssociatedUserProfile() throws FileNotFoundException,
			IOException, JSONException {

		Resource r = new ClassPathResource("init_lookup_data_production.json");
		IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		loader.setDataPath(r);

		loader.init();

		UserInformation u = TestHelpers.makeUser("foo");
		u.setId("u_f65762e9-11b1-4965-a3c5-913c13af078b");
		up.createTestUser(u);

		hcp.add(TestHelpers.makeHcPrincipal("Doctor One", true, "Smith"));
		hcp.add(TestHelpers.makeHcPrincipal("John Two", true, "Smith"));
		hcp.add(TestHelpers.makeHcPrincipal("Harry One", true, "Smith"));
		hcp.add(TestHelpers.makeHcPrincipal("Sammy Six", true, "Smith"));
		hcp.add(TestHelpers.makeHcPrincipal("Suzy Eight", true, "Smith"));

		assertEquals(5, hcp.findByApproved1(true).size());

		JSONArray res = serviceToTest
				._getAssociationProfile("f65762e9-11b1-4965-a3c5-913c13af078b");
		JSONObject res1 = res.getJSONObject(0);
		assertEquals(5, res.length());
		System.out.println(res1);
		assertFalse(res1.has("password"));

	}
	
	@Test
	public void testGetApproved() throws FileNotFoundException,
			IOException, JSONException {

		Resource r = new ClassPathResource("init_lookup_data_production.json");
		IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		loader.setDataPath(r);

		loader.init();

		hcp.add(TestHelpers.makeHcPrincipal("Doctor One", false, "Smith"));
		hcp.add(TestHelpers.makeHcPrincipal("John Two", false, "Smith"));
		hcp.add(TestHelpers.makeHcPrincipal("Harry One", false, "Smith"));
		hcp.add(TestHelpers.makeHcPrincipal("Sammy Six", true, "Smith"));
		hcp.add(TestHelpers.makeHcPrincipal("Suzy Eight", true, "Smith"));

		assertEquals(2, hcp.findByApproved1(true).size());


	}
	

	@Test
	public void testUpdateCredentials() {

		UserPrincipal p = TestHelpers.makePrincipal("foo");
		pp.create(p);
		up.createForParent(p.getId(), p.getName());

		serviceToTest.updateCredentials(p.getId(), "r@r.com", "one");

		p = pp.get(p.getId());
		assertEquals("r@r.com", p.getName());
		assertTrue(encoder.isPasswordValid(p.getPassword(), "one", null));

		UserInformation ui = up.get("u_" + p.getId());
		assertEquals("r@r.com", ui.getName());
	}

	@Test(expected = DuplicateUsernameException.class)
	public void testUpdateCredentialsDuplicate() {

		UserPrincipal p = TestHelpers.makePrincipal("r@r.com");
		pp.create(p);

		p = TestHelpers.makePrincipal("foo");
		pp.create(p);
		up.createForParent(p.getId(), p.getName());

		serviceToTest.updateCredentials(p.getId(), "r@r.com", "one");

		p = pp.get(p.getId());
		assertEquals("r@r.com", p.getName());
		assertTrue(encoder.isPasswordValid(p.getPassword(), "one", null));

		UserInformation ui = up.get("u_" + p.getId());
		assertEquals("r@r.com", ui.getName());
	}

	@Test
	public void testUpdatePrincipal() {

		UserPrincipal p = TestHelpers.makePrincipal("foo");
		DateTime bdate = new DateTime(1962, 8, 29, 0, 0, 0, 0);
		p.setBirthdate(bdate);
		p.setCity("Brooklyn");
		pp.create(p);

		p.setCity("Latham");
		serviceToTest.updateUserInformation(p.getId(), p);
		p = pp.get(p.getId());
		assertEquals("Latham", p.getCity());

	}

	@Test
	public void testSplitTreatmentResponse() {

		ArrayList<TreatmentProfileItem> in = Lists.newArrayList(
				new TreatmentProfileItem(true, false, "One"),
				new TreatmentProfileItem(true, true, "Two"),
				new TreatmentProfileItem(true, true, "Three"));

		List<Treatment> active = Lists.newArrayList();
		List<Treatment> others = Lists.newArrayList();
		ProfileService.splitTreatmentResponse(in, active, others);
		assertEquals(3, active.size());
		assertEquals(2, others.size());

	}

	@Test
	public void testSplitPreventativeTreatmentResponse() {

		ArrayList<PreventativeTreatmentProfileItem> in = Lists.newArrayList(
				new PreventativeTreatmentProfileItem(true, false, "One"),
				new PreventativeTreatmentProfileItem(true, true, "Two"),
				new PreventativeTreatmentProfileItem(true, true, "Three"));

		List<PreventativeTreatment> active = Lists.newArrayList();
		List<PreventativeTreatment> others = Lists.newArrayList();
		ProfileService.splitPreventativeTreatmentResponse(in, active, others);
		assertEquals(3, active.size());
		assertEquals(2, others.size());

	}

	@Test
	public void testAssociations() {
		UserPrincipal p = TestHelpers.makePrincipal("foo");
		pp.create(p);
		up.createForParent(p.getId(), p.getName());
		serviceToTest._updateUserAssociations(p.getId(),
				Lists.newArrayList("one"));
		assertEquals(1, associationRepo.findByHcpId("one").size());
	}

	@Test
	public void testAssociations_cleanup() {
		UserPrincipal p = TestHelpers.makePrincipal("foo");
		pp.create(p);
		up.createForParent(p.getId(), p.getName());
		serviceToTest._updateUserAssociations(p.getId(),
				Lists.newArrayList("one"));
		serviceToTest._updateUserAssociations(p.getId(),
				Lists.newArrayList("one"));
		assertEquals(1, associationRepo.findByHcpId("one").size());
	}

}
