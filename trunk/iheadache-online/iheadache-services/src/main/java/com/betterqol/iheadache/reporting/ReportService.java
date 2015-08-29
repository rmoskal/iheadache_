package com.betterqol.iheadache.reporting;

import static com.betterqol.iheadache.dashboard.DashboardHelpers.GET_TREATMENT_COUNT;
import static com.betterqol.iheadache.dashboard.DashboardHelpers.chunk;
import static com.betterqol.iheadache.dashboard.DashboardHelpers.getQuarter;

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;

import org.apache.commons.httpclient.util.HttpURLConnection;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.accumulators.AccumulatorBank;
import com.betterqol.iheadache.accumulators.PTUsageAccumulator;
import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.dashboard.HeadacheAccumulator;
import com.betterqol.iheadache.dashboard.HeadacheAccumulator.Accumulater;
import com.betterqol.iheadache.model.AppSettings.DrugRules;
import com.betterqol.iheadache.model.Treatment;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.betterqol.iheadache.repository.TreatmentRepository;
import com.betterqol.iheadache.repository.UserInformationRepository;
import com.betterqol.iheadache.resource.ReportResource;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Path(ReportResource.URL)
@Component
@RolesAllowed(value = { "ROLE_USER" })
public class ReportService implements ReportResource {
	
	private static final String DATE_FORMAT = "yyyy-MMM-dd";
	private static final String DATE_FORMAT_MONTH = "yyyy-MMMM";
	private static final String DATE_FORMAT_QUARTER = "yyyy";

	@Autowired
	HeadacheRepository repo;
	
	@Autowired
	LookupRepository lookups;
	
	@Autowired
	TreatmentRepository treatments;
	
	@Autowired
	UserInformationRepository users;
	
	@Autowired
	AccumulatorBank ab;
	
	public HeadacheAccumulator getRawTreatments(String userId, DrugRules rule,
			Date startDate, Date endDate, SliceType chunk, Accumulater accum)
			throws ParseException {
		List<Map> in = Lists.newArrayList(Iterables.transform(
				repo.getDateRange(userId, startDate, endDate),
				GET_TREATMENT_COUNT));
		return getRawTreatments(in, rule, userId, startDate, endDate, chunk,
				accum);
	}

	public HeadacheAccumulator getRawTreatments(List<Map> in, DrugRules rule,
			String userId, Date startDate, Date endDate, SliceType chunk,
			Accumulater accum) throws ParseException {

		List<Treatment> h = treatments.getAll();
		UserInformation currentUser = users.getCurrent(); // add the custom
															// treatments
		h.addAll(currentUser.getCustomTreatments());

		Map<String, String> names = getTreatmentNamer(rule, h);
		List<String> hid = Lists.newArrayList(Iterables.transform(h,
				RepositoryHelpers.GET_IDS));

		List<DateTime> chunks = chunk(startDate, endDate, chunk);

		HeadacheAccumulator a = new HeadacheAccumulator(names, h, accum);
		a.accumulate(chunks, in, hid.toArray(new String[hid.size()]));
		return a;
	}
	
	public static Map<String, String> enMapTradeName(Iterable<Treatment>teats) {
		
		Map<String, String> results = Maps.newHashMap();
		for (Treatment each: teats)
			results.put(each.getId(), each.getDescription()+"/" + each.getForm() + " " + each.getUom());
		
		return results;
	}
	
	public static Map<String, String> enMapGenericName(Iterable<Treatment>teats) {
		
		Map<String, String> results = Maps.newHashMap();
		for (Treatment each: teats)
			results.put(each.getId(), each.getGenericName()+"/" + each.getForm() + " " + each.getUom());
		
		return results;
	}
	
	public static Map<String, String> enMapBothNames(Iterable<Treatment>teats) {
		
		Map<String, String> results = Maps.newHashMap();
		for (Treatment each: teats)
			results.put(each.getId(), each.getDescription() + "/"+ each.getGenericName()+"/" + each.getForm() + " " + each.getUom());
		
		return results;
	}
	
	public static Map<String,String> getTreatmentNamer(DrugRules rule,Iterable<Treatment>teats ){
		
		switch(rule) {
		
			case GENERIC : return enMapGenericName(teats);
			case BOTH: 	return enMapBothNames(teats);
		    default: return RepositoryHelpers.enMap(teats);
		}
	}
	
	public String getReport(Date start, Date end, SliceType chunk) throws IOException, ParseException, JSONException {
		
		return getReport(repo.getUser().getUserPk(), start, end, chunk);
	}
	
	public String getReport(String userId, Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		
		
		try {
		List<JSONArray> headacheTypes = ab.getHeadacheTypes(userId, startDate, endDate, chunk).
				postProcessJson2(true, "axis", "count");
		System.out.println(headacheTypes);
		List<Map> midasScores = ab.getMidasScore(userId, startDate, endDate, chunk).getResults();
		List<JSONArray> triggers = ab.getTriggers(userId, startDate, endDate, chunk).
				 postProcessJson2(true, "axis", "count");
		List<JSONArray>  treats = getRawTreatments(userId, users.getCurrent().getAppSettings().getDrugRule(), 
				startDate, endDate, chunk, Accumulater.byTest).postProcessJson2(true, "axis", "count");
		List<Map<String, Object>>  disability = ab.getDisability(userId, startDate, endDate, chunk);
		PTUsageAccumulator pTreats = ab.getPTUsages(userId, startDate, endDate, chunk);
		List<List<JSONObject>> notes = ab.getNotes(userId, startDate, endDate, chunk).getJson();

		List<DateTime> chunks = chunk(startDate, endDate, chunk);
		
		Collections.reverse(pTreats.started_results);
		Collections.reverse(pTreats.ended_results);
		Collections.reverse(pTreats.ongoing_results);
				
		Collections.reverse(headacheTypes);
		Collections.reverse(midasScores);
		Collections.reverse(triggers);
		Collections.reverse(treats);
		Collections.reverse(disability);
		Collections.reverse(chunks);
		Collections.reverse(notes);
		

		
		
		DrugRules drugRule = users.getCurrent().getAppSettings().getDrugRule();
		
		JSONArray res = new JSONArray();	
		//Now we merge them all together
		 for(int i=0; i< midasScores.size(); i++){
			 JSONObject o = new JSONObject();
			 o.put("headache_types", headacheTypes.get(i));
			 o.put("midas_scores", midasScores.get(i));
			 o.put("notes", notes.get(i));
			 o.put("triggers", triggers.get(i));
			 o.put("treats", treats.get(i));
			 o.put("header", getHeader(chunks.get(i),chunk));
			 o.put("plus", getHeader2(chunks.get(i),chunk));
			 o.put("disability", cleanMap(disability.get(i), "axis"));
			 o.put("pt_start", PTUsageAccumulator.getJson(pTreats.started_results.get(i),drugRule));
			 o.put("pt_end", PTUsageAccumulator.getJson(pTreats.ended_results.get(i),drugRule));
			 o.put("pt_ongoing", PTUsageAccumulator.getJson(pTreats.ongoing_results.get(i),drugRule));
			 res.put(o);
		 }
		 
		return res.toString();
		}
	catch (IndexOutOfBoundsException e){
		throw new WebApplicationException(HttpURLConnection.HTTP_NO_CONTENT);
	}
		
	}
	
	public JSONArray getHeadacheTypeSection(String userId, Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		 
		List<Map> in =  repo.getDateRangeDTO(userId, startDate, endDate);
		 List<DateTime> chunks = chunk(startDate, endDate, chunk);

		 HeadacheAccumulator a = new HeadacheAccumulator(TestHelpers.h,  Accumulater.byCount);
		 a.accumulate(chunks,in, new String[]{HeadacheMap.kind.toString()});
		 
		 return a.postProcessJson(false, false);
	}
	
	
	public String getHeader(DateTime date, SliceType chunk) {

		StringBuilder sb = new StringBuilder();
		switch (chunk) {
		case MONTH:
			sb.append(date.toString(DATE_FORMAT_MONTH));
			return sb.toString();
		case QUARTER:
			sb.append(getQuarter(date));
			sb.append("Q ");
			sb.append(date.toString(DATE_FORMAT_QUARTER));
			return sb.toString();
		default:
			sb.append(chunk.decrement(date).plusDays(1).toString(DATE_FORMAT));
			sb.append(" to ");
			sb.append(date.toString(DATE_FORMAT));
		return sb.toString();
		}
	}
	
	public JSONObject getHeader2(DateTime date, SliceType chunk) throws JSONException {
		
		JSONObject res = new JSONObject();
		res.put("title", getHeader(date,chunk));
		res.put("start", chunk.decrement(date).plusDays(1).toString(DATE_FORMAT));
		res.put("end", date.toString(DATE_FORMAT));
		res.put("chunk", chunk.getLabel());
		return res;
	}
	
	
	public List<JSONObject>  cleanMap(Map<String, Object> in, String... keys) throws JSONException {
		
		List<JSONObject> results = Lists.newArrayList();
		
		for (String each: keys)
			in.remove(each);
		
		Iterator<String> i = in.keySet().iterator();
		while (i.hasNext()) {
			String each = i.next();
			if (in.get(each).toString().trim().equals("0"))
				i.remove();
		}		
		
		for (String each : in.keySet()) {
			JSONObject o = new JSONObject();
			o.put("description", each);
			o.put("value", in.get(each).toString());
			results.add(o);
		}
		return results;
	}

}
