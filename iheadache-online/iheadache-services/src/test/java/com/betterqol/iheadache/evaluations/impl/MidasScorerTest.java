package com.betterqol.iheadache.evaluations.impl;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.YesNoResponse;
import com.google.common.collect.Lists;


public class MidasScorerTest {
	
	@Test
	public void testScoring1() {
		
		Headache h = TestHelpers.createMinHeadache(new Date(), "foo");
		List<YesNoResponse> responses = Lists.newArrayList(
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_1.toString(),true,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_2.toString(),true,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_3.toString(),true,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_4.toString(),true,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_5.toString(),true,false));
		
		h.getDisability().setResponses(responses);
		new MidasScorer().transform(h, null);
		assertEquals(3, h.getMIDAS());
		
		
	}
	
	@Test
	public void testScoring2() {
		
		Headache h = TestHelpers.createMinHeadache(new Date(), "foo");
		List<YesNoResponse> responses = Lists.newArrayList(
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_1.toString(),false,true),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_2.toString(),false,true),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_3.toString(),false,true),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_4.toString(),false,true),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_5.toString(),false,true));
		
		h.getDisability().setResponses(responses);
		new MidasScorer().transform(h, null);
		assertEquals(0, h.getMIDAS());
		
		
	}
	
	
	@Test
	public void testScoring3() {
		
		Headache h = TestHelpers.createMinHeadache(new Date(), "foo");
		List<YesNoResponse> responses = Lists.newArrayList(
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_1.toString(),false,true),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_2.toString(),true,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_3.toString(),false,true),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_4.toString(),true,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_5.toString(),true,false));
		
		h.getDisability().setResponses(responses);
		new MidasScorer().transform(h, null);
		assertEquals(3, h.getMIDAS());
		
		
	}
	
	
	@Test
	public void testScoring4() {
		
		Headache h = TestHelpers.createMinHeadache(new Date(), "foo");
		List<YesNoResponse> responses = Lists.newArrayList(
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_1.toString(),false,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_2.toString(),true,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_3.toString(),false,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_4.toString(),true,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_5.toString(),true,false));
		
		h.getDisability().setResponses(responses);
		new MidasScorer().transform(h, null);
		assertEquals(3, h.getMIDAS());
		
		
	}
	

	
	@Test
	public void testScoringNone() {
		
		Headache h = TestHelpers.createMinHeadache(new Date(), "foo");
		List<YesNoResponse> responses = Lists.newArrayList(
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_1.toString(),false,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_2.toString(),false,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_3.toString(),false,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_4.toString(),false,false),
		new YesNoResponse(MidasScorer.QuestionMap.DISABILITY_QUESTION_5.toString(),false,false));
		
		h.getDisability().setResponses(responses);
		new MidasScorer().transform(h, null);
		assertEquals(0, h.getMIDAS());
		
		
	}

}
