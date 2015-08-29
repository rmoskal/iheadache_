package com.betterqol.iheadache.model.dto;

import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

import org.codehaus.jettison.json.JSONArray;
import org.joda.time.DateTime;


public abstract class MidasGraphItem {
		


	public static void create(JSONArray l, int midasScore,
			boolean estimated, GregorianCalendar date) {
		LinkedHashMap<String, Object> r = new LinkedHashMap<String, Object>();
		r.put("Score",midasScore);
		r.put("Est",estimated);
		r.put("Date",new DateTime(date).toString());
		
		l.put(r);
		
	}
	

}
