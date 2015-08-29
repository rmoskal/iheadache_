package com.betterqol.iheadache.evaluations;

import java.util.Map;

import com.betterqol.iheadache.model.Headache;

public interface HeadacheTransformer {
	
	public void transform (Headache h, Map context);

}
