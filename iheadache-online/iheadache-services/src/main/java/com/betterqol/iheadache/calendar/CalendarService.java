package com.betterqol.iheadache.calendar;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.accumulators.AccumulatorBank;
import com.betterqol.iheadache.accumulators.PTUsageAccumulator;
import com.betterqol.iheadache.dashboard.DashboardHelpers.CalendarMap;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.HeadachePain;
import com.betterqol.iheadache.model.HeadacheTreatment;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.resource.CalendarResource;
import com.betterqol.iheadache.resource.MiniHeadacheRepo;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Component
@Path(CalendarService.URL)
@RolesAllowed(value = { "ROLE_USER" })
public class CalendarService implements CalendarResource {
	
	public static final String URL = "/app/service/calendar";
	
	@Autowired
	MiniHeadacheRepo repo;
	
	@Autowired
	LookupRepository lookups;
	
	@Autowired
	AccumulatorBank accumulators;

	@Override
	public String test() {
		return this.getClass().getName();
	}
	
	private ImmutableMap<String,Integer> map = ImmutableMap.of("TENSION_HEADACHE",1,"PROBABLE_MIGRAINE",2,"MIGRAINE",3,
			"UNCLASSIFIED_HEADACHE",4,"NO_HEADACHE",5);
	
	public String getCalendarEntry(Date startDate, Date endDate) throws IOException, JSONException {
		
		return getCalendarEntry(repo.getUser().getUserPk(), startDate, endDate);
	
	}

	public String getCalendarEntry(String userId, Date startDate, Date endDate) throws IOException, JSONException {
		 
		
		 JSONObject results = new JSONObject();
		 JSONArray out = new JSONArray();
		 List<Headache> headaches = repo.getDateRange(userId, startDate, endDate);
		 List<Map> in = Lists.newArrayList(Iterables.transform(headaches, CalendarHelpers.MAKE_Calendar));
		
		 for (Map o:in)
		 {
			int calendarId = map.get(o.get(CalendarMap.title.toString()));
			o.put(CalendarMap.cid.toString(), calendarId);
			o.put(CalendarMap.ad.toString(), true);
			o.put(CalendarMap.title.toString(), TestHelpers.hm.get(o.get(CalendarMap.title.toString())));
			 
			 out.put(o);
			 
			 //o.put(CalendarMap.cid.toString(), 5);
			 o.put(CalendarMap.id.toString(), "start_"+ o.get(CalendarMap.id.toString()));
			 o.remove(CalendarMap.ad.toString());
			 String end = o.get(CalendarMap.end.toString()).toString();
			 o.put(CalendarMap.end.toString(), end);
			 o.put(CalendarMap.title.toString(), "Start");
			 o.put(CalendarMap.notes.toString(), "Hey ho, lets go");

			 out.put(o);
			 
			 
		 }
		 
		 
		 for (Headache o: headaches) {
			 for (HeadachePain t: o.getPains()){
				 Map res = CalendarHelpers.MAKE_PainCalendar.apply(t);
	
				 out.put(res);
			 }
		 }
		 
		 
		 for (Headache o1: headaches) {
			 for (HeadacheTreatment t: o1.getTreatments()){
				 Map res = CalendarHelpers.MAKE_TreatmentCalendar.apply(t);
				 //res.put(CalendarMap.cid.toString(), 5);
				 out.put(res);
			 } 
			 
		 } 
			 
		 
		 results.put("evts", out);
		 return results.toString();
		
	}
	
	
	public String getPTreatmentEntry(Date startDate, Date endDate) throws IOException, JSONException {
		
		return getPTreatmentEntry(repo.getUser().getUserPk(), startDate, endDate);
	
	}
	
	
	public String getPTreatmentEntry(String userId, Date startDate, Date endDate) throws IOException, JSONException {
		 
		
		 JSONObject results = new JSONObject();
		 JSONArray out = new JSONArray();
		 List<Headache> headaches = repo.getDateRange(userId, startDate, endDate);
		 List<Map> in = Lists.newArrayList(Iterables.transform(headaches, CalendarHelpers.MAKE_Calendar));
		 
		
		 for (Map o:in)
		 {
			int calendarId = map.get(o.get(CalendarMap.title.toString()));
			o.put(CalendarMap.cid.toString(), calendarId);
			o.put(CalendarMap.ad.toString(), true);
			o.put(CalendarMap.title.toString(), TestHelpers.hm.get(o.get(CalendarMap.title.toString())));
			out.put(o);		 
			 
		 }
		 
		 
		 PTUsageAccumulator ac = accumulators.getPTUsages(userId, startDate, endDate, SliceType.ALL);
		 for (Map o: ac.started_results.get(0)) {
			 o.put(CalendarMap.cid.toString(), 6);
			 o.put(CalendarMap.title.toString(), o.get("treatmentDescription"));
			 o.put(CalendarMap.ad.toString(), true);
			 if (o.get("end")==null)
				 o.put("end", endDate.getTime());
			 out.put(o);
		 }
		 
		 for (Map o: ac.ongoing_results.get(0)) {
			 o.put(CalendarMap.cid.toString(), 6);
			 o.put(CalendarMap.title.toString(), o.get("treatmentDescription"));
			 o.put(CalendarMap.ad.toString(), true);
			 o.put("end", endDate.getTime());
			 if (new DateTime(o.get("start")).isAfter(new DateTime(startDate)))
				 	continue;
			// System.out.println(o);
			 out.put(o);
		 }
		 
		 for (Map o: ac.ended_results.get(0)) {
			 o.put(CalendarMap.cid.toString(), 6);
			 o.put(CalendarMap.title.toString(), o.get("treatmentDescription"));
			 o.put(CalendarMap.ad.toString(), true);
			 if (new DateTime(o.get("start")).isAfter(new DateTime(startDate)))
			 	continue;
			 out.put(o);
		 }
		 
		 
		 results.put("evts", out);
		 return results.toString();
		
	}
	
	public String getNavCalendar(Date startDate, Date endDate) throws IOException, JSONException {
		
		return getNavCalendar (repo.getUser().getUserPk(), startDate, endDate);
	}
	
	public String getNavCalendar(String userId, Date startDate, Date endDate) throws IOException, JSONException {
		 

		 JSONObject results = new JSONObject();
		 JSONArray out = new JSONArray();
		 List<Map> in = Lists.newArrayList(Iterables.transform(repo.getDateRange(userId, startDate, endDate), CalendarHelpers.MAKE_Calendar));
		
		 for (Map o:in)
		 {
			 o.put(CalendarMap.cid.toString(), map.get(o.get(CalendarMap.title.toString())));
			 o.put(CalendarMap.end.toString(),  o.get(CalendarMap.start.toString()));
			 o.put(CalendarMap.ad.toString(), true);
			 o.put(CalendarMap.title.toString(), TestHelpers.hm.get(o.get(CalendarMap.title.toString())));
			 out.put(o);
			 
		 }
			 
		 
		 results.put("evts", out);
		 return results.toString();
		
	}
	

}
