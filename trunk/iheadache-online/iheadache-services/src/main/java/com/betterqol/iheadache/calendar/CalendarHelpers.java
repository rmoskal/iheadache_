package com.betterqol.iheadache.calendar;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.codehaus.jettison.json.JSONArray;
import org.joda.time.DateTime;

import com.betterqol.iheadache.dashboard.DashboardHelpers.CalendarMap;
import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.HeadachePain;
import com.betterqol.iheadache.model.HeadacheTreatment;
import com.google.common.base.Function;

/***
 * A variety of helpers that put various headache properties on a map 
 * to construct the various calendars used in the application
 * @author rob
 *
 */
public abstract class CalendarHelpers {
	
	public static final Function<Headache, Map> MAKE_TREATMENTS_CALENDAR = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			Map results = new LinkedHashMap();
			results.put(HeadacheMap.start.toString(), h.getStart().toString());
			for (HeadacheTreatment t : h.getTreatments())
				results.put(t.getTreatment(), t.getTreatment());
			return results;
		}
	};

	public static final Function<Headache, Map> MAKE_Calendar = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			Map results = new LinkedHashMap();
			results.put(CalendarMap.start.toString(), h.getStart().getMillis());
			results.put(CalendarMap.end.toString(),h.getEnd().getMillis());
			results.put(CalendarMap.title.toString(), h.getKind().getId());
			//results.put(CalendarMap.notes.toString(), h.getMIDAS());
			results.put(CalendarMap.cid.toString(), 1);
			results.put(CalendarMap.id.toString(), h.getId());
			return results;
		}
	};
	
	
	public static final Function<HeadacheTreatment, Map> MAKE_TreatmentCalendar = new Function<HeadacheTreatment, Map>() {
		public Map apply(HeadacheTreatment h) {

			Map results = new LinkedHashMap();
			results.put(CalendarMap.start.toString(),
					h.getDose().getMillis());
			results.put(CalendarMap.end.toString(),
					h.getDose().getMillis());
			results.put(CalendarMap.title.toString(),  h.getTreatment().getDescription());
			results.put(CalendarMap.cid.toString(), 6);
			results.put(CalendarMap.id.toString(), "none_" + UUID.randomUUID());
			return results;
		}
	};
	
	
	public static final Function<HeadachePain, Map> MAKE_PainCalendar = new Function<HeadachePain, Map>() {
		public Map apply(HeadachePain h) {

			Map results = new LinkedHashMap();
			results.put(CalendarMap.start.toString(),
					h.getTime().getMillis());
			results.put(CalendarMap.end.toString(),
					h.getTime().getMillis());
			results.put(CalendarMap.title.toString(), "Pain level: " + h.getLevel() );
			results.put(CalendarMap.cid.toString(), 7);
			results.put(CalendarMap.id.toString(), "none_" + UUID.randomUUID());
			return results;
		}
	};
	

	
	
	
	
	
	
	public static void foo(List<Map> started, List<Map> ongoing, List<Map> ended ) {
	
		JSONArray results = new JSONArray();
		//results.put(PTUsageAccumulator.getJson(acc.started_results.get(0)));
		//results.put(PTUsageAccumulator.getJson(acc.ended_results.get(0)));
		//results.put(PTUsageAccumulator.getJson(acc.ongoing_results.get(0)));
	}
	

	
	
	
	
	

}
