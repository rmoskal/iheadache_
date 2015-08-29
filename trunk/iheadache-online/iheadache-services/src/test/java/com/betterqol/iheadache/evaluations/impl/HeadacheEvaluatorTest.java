package com.betterqol.iheadache.evaluations.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.AppHelpers.HeadacheTypes;
import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.HeadacheTreatment;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.YesNoResponse;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.google.common.collect.Lists;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml" })
public class HeadacheEvaluatorTest extends BaseBigTest {
	
	private static final ArrayList<YesNoResponse> COMPLETE_SYMPTOM_LIST = Lists.newArrayList(
			new YesNoResponse("IS_MOVEMENT", true, false), 
			new YesNoResponse("IS_ANILATERAL", true, false), 
			new YesNoResponse("HAS_AURA", true, false),
			new YesNoResponse("HAS_NAUSEA", true, false),
			new YesNoResponse("HAS_PHOTOPHOBIA", true, false),
			new YesNoResponse("HAS_PHONOBIA", true, false),
			new YesNoResponse("HAS_OLFACTOPHOBIA", true, false),
			new YesNoResponse("IS_NECK", true, false),
			new YesNoResponse("IS_SINUS", true, false),
			new YesNoResponse("IS_THROBBING", true, false)
			);
	private static final ArrayList<YesNoResponse> COMPLETE_SYMPTOM_LIST_PROBABLE = Lists.newArrayList(
			new YesNoResponse("IS_MOVEMENT", false, true), 
			new YesNoResponse("IS_THROBBING", true, false),
			new YesNoResponse("IS_ANILATERAL", false, true), 
			new YesNoResponse("HAS_AURA", false, true),
			new YesNoResponse("HAS_NAUSEA", false, true),
			new YesNoResponse("HAS_PHOTOPHOBIA", false, true),
			new YesNoResponse("HAS_PHONOBIA", false, true),
			new YesNoResponse("HAS_OLFACTOPHOBIA", false, true),
			new YesNoResponse("IS_NECK", false, true),
			new YesNoResponse("IS_SINUS", false, true)
			);

	
	@Test
	public void testMaxSeverity() {
		
		Headache h = new Headache();
		assertEquals(0,HeadacheEvaluator.getMaxSeverity(h),0.1); // null test
		h = TestHelpers.createMinHeadache(new Date(), "foo");
		assertEquals(10,HeadacheEvaluator.getMaxSeverity(h),0.1);	
	}
	
	@Test
	public void testGetDuration() {
		
		Headache h = new Headache();
		assertEquals(new Duration(0),HeadacheEvaluator.getDuration(h)); // null test
		h = TestHelpers.createMinHeadache(new Date(), "foo");
		assertEquals(Duration.standardHours(4),HeadacheEvaluator.getDuration(h)); 	
	}
	
	@Test
	public void testGetPaintType() {
		
		Headache h = new Headache();
		assertFalse(HeadacheEvaluator.hasPainType(h, "THROBBING")); // null test
		h = TestHelpers.createMinHeadache(new Date(), "foo");
		assertTrue(HeadacheEvaluator.hasPainType(h, "THROBBING"));	
		assertFalse(HeadacheEvaluator.hasPainType(h, "FOOOOO"));	
	}
	
	@Test
	public void testGetSymptoms() {
		
		Headache h = new Headache();
		assertFalse(HeadacheEvaluator.getSymptom(h, "FOOOOO"));
		h.setSymptoms(Lists.newArrayList(new YesNoResponse(
				"IS_MOVEMENT", true, false), new YesNoResponse(
				"IS_ANILATERAL", true, false), new YesNoResponse(
				"HAS_AURA", false, true)));
		assertFalse(HeadacheEvaluator.getSymptom(h, "FOOOOO"));	
		assertTrue(HeadacheEvaluator.getSymptom(h, "IS_ANILATERAL"));
		assertFalse(HeadacheEvaluator.getSymptom(h, "HAS_AURA"));
		
	}
	
	
	@Test
	public void testHasCompleteSymptomsNormal() {
		
		Headache h = new Headache();
		h.setSymptoms(Lists.newArrayList(new YesNoResponse(
				"IS_MOVEMENT", true, false), new YesNoResponse(
				"IS_ANILATERAL", true, false), new YesNoResponse(
				"HAS_AURA", false, true)));
		assertFalse(HeadacheEvaluator.hasCompleteSymtoms(h));	
		h.setSymptoms(COMPLETE_SYMPTOM_LIST);
		assertTrue(HeadacheEvaluator.hasCompleteSymtoms(h));	
		
	}
	
	
	@Test
	public void testHasCompleteSymptomsNormal2() {
		
		Headache h = new Headache();
		h.setSymptoms(Lists.newArrayList(
				new YesNoResponse("IS_MOVEMENT", true, false), 
				new YesNoResponse("IS_ANILATERAL", true, false), 
				new YesNoResponse("HAS_AURA", true, false),
				new YesNoResponse("HAS_NAUSEA", true, false),
				new YesNoResponse("HAS_PHOTOPHOBIA", true, false),
				new YesNoResponse("HAS_PHONOBIA", true, false),
				new YesNoResponse("HAS_OLFACTOPHOBIA", true, false),
				new YesNoResponse("IS_NECK", true, false),
				new YesNoResponse("IS_SINUS", false, true)
				));
		assertFalse(HeadacheEvaluator.hasCompleteSymtoms(h));	
		
	}
	
	@Test
	public void testHasCompleteSymptomsCustom() {
		
		Headache h = new Headache();
		h.setSymptoms(COMPLETE_SYMPTOM_LIST);
		assertTrue(HeadacheEvaluator.hasCompleteSymtoms(h));	
				
	}
	
	@Test
	public void testHasCompleteSymptomsCustom2() {
		
		Headache h = new Headache();
		h.setSymptoms(Lists.newArrayList(
				new YesNoResponse("IS_MOVEMENT", true, false), 
				new YesNoResponse("IS_ANILATERAL", true, false), 
				new YesNoResponse("HAS_AURA", true, false),
				new YesNoResponse("HAS_NAUSEA", true, false),
				new YesNoResponse("HAS_PHOTOPHOBIA", true, false),
				new YesNoResponse("HAS_PHONOBIA", true, false),
				new YesNoResponse("HAS_OLFACTOPHOBIA", true, false),
				new YesNoResponse("IS_NECK", true, false),
				new YesNoResponse("IS_SINUS", true, false),
				//new YesNoResponse("IS_THROBBING", true, false),
				new YesNoResponse("FOOO!!!", true, false)
				));
		assertFalse(HeadacheEvaluator.hasCompleteSymtoms(h));	
			
	}
	
	@Test
	public void testHasCompleteSymptomsCustom3() {
		
		Headache h = new Headache();
		h.setSymptoms(Lists.newArrayList(
				new YesNoResponse("IS_MOVEMENT", true, false), 
				new YesNoResponse("IS_ANILATERAL", true, false), 
				new YesNoResponse("HAS_AURA", true, false),
				new YesNoResponse("HAS_NAUSEA", true, false),
				new YesNoResponse("HAS_PHOTOPHOBIA", true, false),
				new YesNoResponse("HAS_PHONOBIA", true, false),
				new YesNoResponse("HAS_OLFACTOPHOBIA", true, false),
				new YesNoResponse("IS_NECK", true, false),
				new YesNoResponse("IS_SINUS", true, false),
				new YesNoResponse("IS_THROBBING", true, false),
				new YesNoResponse("FOOO!!!", true, false)
				));
		assertTrue(HeadacheEvaluator.hasCompleteSymtoms(h));	
			
	}
	
	@Test
	public void testMigrainMedication() {
		
		Headache h = new Headache();
		assertFalse(HeadacheEvaluator.hasMigraineMedication(h));	
		
		TestHelpers.createLookups(db);
		h.setTreatments(Lists.newArrayList( new HeadacheTreatment(new DateTime(),treatments.get("STND_TR_AXERT"))));
		assertTrue(HeadacheEvaluator.hasMigraineMedication(h));	
		
	}
	
	
	@Test
	public void testHasSymptoms() {
		
		Headache h = new Headache();
		assertFalse(HeadacheEvaluator.hasCompleteSymtoms(h));		
	}
	
	@Test
	public void testClassificationMinimimal() {
		
		Headache h = new Headache();
		_rucClassTest(h);
		assertEquals(HeadacheTypes.UNCLASSIFIED_HEADACHE.name(), h.getKind().getId());
	}
	
	@Test
	public void testClassificationProbaleMigraine() {
		
		Headache h = TestHelpers.createMinHeadache(new Date(), "foo");
		h.setSymptoms(COMPLETE_SYMPTOM_LIST_PROBABLE);
		_rucClassTest(h);
		assertEquals(HeadacheTypes.PROBABLE_MIGRAINE.name(), h.getKind().getId());
	}
	
	@Test
	public void testClassificationMigraine() {
		
		Headache h = TestHelpers.createMinHeadache(new Date(), "foo");
		h.setSymptoms(COMPLETE_SYMPTOM_LIST);
		_rucClassTest(h);
		assertEquals(HeadacheTypes.MIGRAINE.name(), h.getKind().getId());
	}
	
	
	@Test
	public void testClassification30Minutes() {
		
		Headache h = TestHelpers.createMinHeadache(new Date(), "foo");
		h.setEnd(h.getStart().plusMinutes(30));
		_rucClassTest(h);
		assertEquals(HeadacheTypes.UNCLASSIFIED_HEADACHE.name(), h.getKind().getId());
	}
	
	@Test
	public void testClassification30Minutes2() {
		
		Headache h = TestHelpers.createMinHeadache(new Date(), "foo");
		h.setEnd(h.getStart().plusMinutes(30));
		h.setSymptoms(COMPLETE_SYMPTOM_LIST);
		_rucClassTest(h);
		assertEquals(HeadacheTypes.UNCLASSIFIED_HEADACHE.name(), h.getKind().getId());
	}
	
	@Test
	public void testClassificationNoHeadaches() {
		
		Headache h = TestHelpers.createMinHeadache(new Date(), "foo");
		h.setEnd(h.getStart().plusMinutes(30));
		h.setSymptoms(COMPLETE_SYMPTOM_LIST);
		h.setNoHeadache(true);
		_rucClassTest(h);
		assertEquals(HeadacheTypes.NO_HEADACHE.name(), h.getKind().getId());
	}
	
	public void _rucClassTest(Headache h) {
	
		TestHelpers.createLookups(db);
		HeadacheEvaluator eval = new HeadacheEvaluator();
		Map context = new HashMap();
		UserInformation user = new UserInformation();
		context.put("USER", user);
		context.put("HEADCAHE_TYPES", RepositoryHelpers.enMap2(lookups.findByLookupType(Kind.HEADACHE_TYPE)));

		eval.transform(h, context);
		
	}
	
}
