package com.betterqol.iheadache.evaluations.impl;

import java.util.Map;

import com.betterqol.iheadache.evaluations.HeadacheTransformer;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.YesNoResponse;
import com.google.common.collect.Maps;


public class MidasScorer implements HeadacheTransformer{

	public enum QuestionMap {
		DISABILITY_QUESTION_1, DISABILITY_QUESTION_2, DISABILITY_QUESTION_3, DISABILITY_QUESTION_4, DISABILITY_QUESTION_5
	}

	
	@Override
	public void transform(Headache h, Map context) {
		
		Map<String, Boolean> hits = Maps.newHashMap();
		int score = 0;
		
		for (YesNoResponse o :h.getDisability().getResponses()){
			
			if (o.isNo()) continue;
			
			if ((!o.isNo()) & (!o.isYes())) continue;
			
			if (o.isYes()) hits.put(o.getId(), true);
			
			if (o.getId().equals(QuestionMap.DISABILITY_QUESTION_2.toString()))
				if (hits.containsKey(QuestionMap.DISABILITY_QUESTION_1.toString()))
					continue;
			
			if (o.getId().equals(QuestionMap.DISABILITY_QUESTION_4.toString()))
				if (hits.containsKey(QuestionMap.DISABILITY_QUESTION_3.toString()))
					continue;
			
			score++;
			
		}
		
		h.setMIDAS(score);
		
	}

}
