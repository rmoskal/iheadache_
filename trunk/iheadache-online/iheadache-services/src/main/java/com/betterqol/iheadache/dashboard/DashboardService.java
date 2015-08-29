package com.betterqol.iheadache.dashboard;


import static com.betterqol.iheadache.dashboard.DashboardHelpers.GET_DISABILITY_COUNT;
import static com.betterqol.iheadache.dashboard.DashboardHelpers.GET_PAINLOCATION_COUNT;
import static com.betterqol.iheadache.dashboard.DashboardHelpers.GET_PAINTYPE_COUNT;
import static com.betterqol.iheadache.dashboard.DashboardHelpers.GET_SYMPTOMS;
import static com.betterqol.iheadache.dashboard.DashboardHelpers.GET_TREATMENT_COUNT;
import static com.betterqol.iheadache.dashboard.DashboardHelpers.GET_TRIGGERS;
import static com.betterqol.iheadache.dashboard.DashboardHelpers.chunk;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.AppHelpers;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.accumulators.Accumulator2;
import com.betterqol.iheadache.accumulators.AccumulatorBank;
import com.betterqol.iheadache.calendar.CalendarMaker;
import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.dashboard.HeadacheAccumulator.Accumulater;
import com.betterqol.iheadache.model.AppSettings.DrugRules;
import com.betterqol.iheadache.model.CouchLookup;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.Treatment;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.dto.CalendarItem;
import com.betterqol.iheadache.model.dto.MidasGraphItem;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.betterqol.iheadache.repository.TreatmentRepository;
import com.betterqol.iheadache.repository.UserInformationRepository;
import com.betterqol.iheadache.resource.DashboardResource;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Component
@Path(DashboardService.URL)
@RolesAllowed(value = { "ROLE_USER" })
public class DashboardService implements DashboardResource{
	
	public static final String URL = "/app/service/dashboard";
	
	@Autowired
	HeadacheRepository repo;
	
	@Autowired
	LookupRepository lookups;
	
	@Autowired
	TreatmentRepository treatments;
	
	@Autowired
	UserInformationRepository users;
	
	@Autowired
	AccumulatorBank aBank;

	@Override
	public String test() {
		return this.getClass().getName();
	}
	
	
	public String getTriggersStacked(Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		return getTriggersStacked(repo.getUser().getUserPk(), startDate, endDate, chunk);
	}

	public String getTriggersStacked(String userId, Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		
		HeadacheAccumulator ac = aBank.getTriggers(userId, startDate, endDate, chunk);
		return ac.postProcessJson(false, true, AppHelpers.EMPTY_LIST).toString();
		 
	}
	
	@Override
	public String getTriggers(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		return getTriggers(repo.getUser().getUserPk(), startDate, endDate);
	}
	
	public String getTriggers(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		Iterable<Map> res = Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_TRIGGERS);
		HeadacheAccumulator a = aBank.getTriggers(DashboardHelpers.getRealHeadaches(res),userId, startDate, endDate, SliceType.ALL);
		return a.postProcessJson(true, "axis","count").toString();
	}
	
	@Override
	public String getTriggersNoOnly(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		return getTriggersNoOnly(repo.getUser().getUserPk(), startDate, endDate);
	}
	
	public String getTriggersNoOnly(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		Iterable<Map> res = Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_TRIGGERS);
		HeadacheAccumulator a = aBank.getTriggers(DashboardHelpers.getNoHeadaches(res),userId, startDate, endDate, SliceType.ALL);
		return a.postProcessJson(true, "axis","count").toString();
	}
	
	
	public String getAcuteTreatmentsStacked(Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		return getAcuteTreatmentsStacked(repo.getUser().getUserPk(), startDate, endDate, chunk);
	}
	
	public String getAcuteTreatmentsStacked(String userId, Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		
		
		 DrugRules drugRule = users.getCurrent().getAppSettings().getDrugRule();
		 if (drugRule.equals(DrugRules.BOTH))
			 drugRule = DrugRules.TRADE;
		 HeadacheAccumulator a = aBank.getRawTreatments(userId, drugRule ,startDate, endDate,
				chunk, Accumulater.byTest);
		 String _res =  a.postProcessJson(false, true, AppHelpers.EMPTY_LIST).toString();
		 
		 System.out.println(_res);
		 return _res;

	}
	
	@Override
	public byte[] getAcuteTreatments(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		return getAcuteTreatments(repo.getUser().getUserPk(), startDate, endDate).getBytes("UTF8");
	}
	
	public String getAcuteTreatments(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		 Iterable<Map> res =  Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_TREATMENT_COUNT);
		 HeadacheAccumulator a = aBank.getRawTreatments(DashboardHelpers.getRealHeadaches(res),users.getCurrent().getAppSettings().getDrugRule(),
				 userId, startDate, endDate,
				 SliceType.ALL, Accumulater.byTest);
		 String _res = a.postProcessJson(true, "axis","count").toString();
		 System.out.println(_res);
		 return _res;
	}


	
	@Override
	public String getAcuteTreatmentsNoOnly(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		return getAcuteTreatmentsNoOnly(repo.getUser().getUserPk(), startDate, endDate);
	}
	
	public String getAcuteTreatmentsNoOnly(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		 Iterable<Map> res =  Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_TREATMENT_COUNT);
		 HeadacheAccumulator a = aBank.getRawTreatments(DashboardHelpers.getNoHeadaches(res), users.getCurrent().getAppSettings().getDrugRule(),
				 userId, startDate, endDate,SliceType.ALL, Accumulater.byTest);
		 return a.postProcessJson(true, "axis","count").toString();
	}


	
	

	public String getSymptomsStacked(Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		return getSymptomsStacked(repo.getUser().getUserPk(), startDate, endDate, chunk);
	}
	
	public String getSymptomsStacked(String userId, Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		
		List<Map> in =  Lists.newArrayList(Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_SYMPTOMS));
		 HeadacheAccumulator a = aBank._getSymptoms(userId, startDate, endDate,chunk, in,DashboardHelpers.SHORT_SYMPTOMS);
		 return a.postProcessJson(false, true, AppHelpers.EMPTY_LIST).toString();
	}
	
	public String getCustomSymptomsStacked(Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		return getCustomSymptomsStacked(repo.getUser().getUserPk(), startDate, endDate, chunk);
	}
	
	public String getCustomSymptomsStacked(String userId, Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		
		 List<Map> in =  Lists.newArrayList(Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_SYMPTOMS));
		 UserInformation currentUser = users.getCurrent(); //add the custom symptoms
		 HeadacheAccumulator a = aBank._getSymptoms(userId, startDate, endDate,chunk, in,currentUser.getCustomSymptoms());
		 return a.postProcessJson(false, true, AppHelpers.EMPTY_LIST).toString();
	}
	
	@Override
	public String getSymptoms(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		return getSymptoms(repo.getUser().getUserPk(), startDate, endDate).toString();
	}
	
	
	public JSONArray getSymptoms(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		 Iterable<Map> res = Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_SYMPTOMS);
		 List<LookupDTO>  h = RepositoryHelpers.enList(lookups.findByLookupType(Kind.SYMPTOM));
		 UserInformation currentUser = users.getCurrent(); //add the custom symptoms
		 h.addAll(currentUser.getCustomSymptoms());
		 HeadacheAccumulator a = aBank. _getSymptoms(userId,startDate,endDate,SliceType.ALL,DashboardHelpers.getRealHeadaches(res),h);
		 return a.postProcessJson(true, "axis","count");
	}


	@Override
	public String getSymptomsNoOnly(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		return getSymptomsNoOnly(repo.getUser().getUserPk(), startDate, endDate).toString();
	}
	
	public JSONArray getSymptomsNoOnly(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		 Iterable<Map> res = Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_SYMPTOMS);
		 List<LookupDTO>  h = RepositoryHelpers.enList(lookups.findByLookupType(Kind.SYMPTOM));
		 UserInformation currentUser = users.getCurrent(); //add the custom symptoms
		 h.addAll(currentUser.getCustomSymptoms());
		 HeadacheAccumulator a = aBank. _getSymptoms(userId,startDate,endDate,SliceType.ALL,DashboardHelpers.getNoHeadaches(res),h);
		 return a.postProcessJson(true, "axis","count");
	}
	
	
	
	public String getTreatmentTable(Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		return getTreatmentTable(repo.getUser().getUserPk(), startDate, endDate, chunk);
	}
	
	public String getTreatmentTable(String userId, Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		HeadacheAccumulator a = aBank.getRawTreatments(userId, users.getCurrent().getAppSettings().getDrugRule(), startDate, endDate,
				chunk, Accumulater.byValue);
		return a.postProcessJson(false, false, AppHelpers.EMPTY_LIST).toString();
	}
	
	public String getHeadacheTypeStacked(Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {

		return getHeadacheTypeStacked(repo.getUser().getUserPk(), startDate, endDate, chunk);
	}
	
	public String getHeadacheTypeStacked(String userId, Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException, JSONException {
		
		 HeadacheAccumulator a = aBank.getHeadacheTypes(userId, startDate, endDate, chunk);
		 return a.postProcessJson(false, false, "count").toString();
	}
	
	public String getHeadacheDays(Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException {
		return getHeadacheDays(repo.getUser().getUserPk(), startDate, endDate, chunk);
	}
	
	
	public String getHeadacheDays(String userId, Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException {
		
		 List<Map> in =  repo.getDateRangeDTO(userId, startDate, endDate);

		 
		 List<DateTime> chunks = DashboardHelpers.chunk(startDate, endDate, chunk);
		 
		 HeadacheAccumulator a = new HeadacheAccumulator(TestHelpers.h,  Accumulater.byCount);
		 a.accumulate(chunks,in, new String[]{HeadacheMap.kind.toString()});
		 JSONArray out = new JSONArray();
		 
		 for (Map o: a.postProcess(false, AppHelpers.EMPTY_LIST)) {
			 DateTime t = new DateTime(o.get("axis"));
			 Integer count = 0;
			 if (o.containsKey("count")) {
				 count =  Integer.decode(o.get("count").toString()); 
				 count++;
			 }
			 o.remove("count");
			 o.remove("No Headache");
			 o.put("Headache Free", chunk.getDuration(new DateTime(t))-count);
			 out.put(o);
		 }
			 
		 return out.toString();
	}
	
	
	public String getPercentDisablilityStacked(Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException {
		return getPercentDisablilityStacked(repo.getUser().getUserPk(), startDate, endDate, chunk);
	}
	
	public String getPercentDisablilityStacked(String userId, Date startDate, Date endDate, SliceType chunk) throws IOException, ParseException {
		
		 return aBank.getDisability(userId, startDate, endDate, chunk) .toString();
	}
	
	
	
	@Override
	public String getDisabilityCount(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		return getDisabilityCount(repo.getUser().getUserPk(), startDate, endDate).toString();
	}
	
	public JSONArray getDisabilityCount(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		 Iterable<Map> res = Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_DISABILITY_COUNT);
		 HeadacheAccumulator a = aBank.getDisability(DashboardHelpers.getRealHeadaches(res),userId,startDate,endDate);
		 return a.postProcessJson(false, "axis","count");
	}
	
	

	@Override
	public String getDisabilityCountNoHeadache(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		return getDisabilityCountNoHeadache(repo.getUser().getUserPk(), startDate, endDate).toString();
	}
	
	public JSONArray getDisabilityCountNoHeadache(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		 Iterable<Map> res = Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_DISABILITY_COUNT);
		 HeadacheAccumulator a = aBank.getDisability(DashboardHelpers.getNoHeadaches(res),userId,startDate,endDate);
		 return a.postProcessJson(true, "axis","count");
	}
	
	
	
	
	
	
	public List<CalendarItem> getCalendarTest(Date startDate) {
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(startDate);
		return CalendarMaker.makeForMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
		
	}
	
	
	public List<CalendarItem> getCalendar(Date startDate) throws IOException {

		return getCalendar(repo.getUser().getUserPk(), startDate);
	}
	
	public List<CalendarItem> getCalendar(String userId, Date startDate) throws IOException {
		Calendar c = GregorianCalendar.getInstance();
		c.setTime(startDate);
		
		DateTime endMonth = SliceType.MONTH.roll(new DateTime(c));
		DateTime startMonth = endMonth.withDayOfMonth(1);
		
		List<Map> in =  repo.getDateRangeDTO(userId, startMonth.toDate(), endMonth.toDate());
		
		return CalendarMaker.makeForMonth(c.get(Calendar.YEAR), c.get(Calendar.MONTH), in);
		
	}
	
	
	public String getDisablilityStacked(Date startDate, Date endDate, DashboardHelpers.SliceType chunk) throws IOException, ParseException {
		return getDisablilityStacked(repo.getUser().getUserPk(), startDate, endDate, chunk);
	}
	
	
	public String getDisablilityStacked(String userId, Date startDate, Date endDate, DashboardHelpers.SliceType chunk) throws IOException, ParseException {
			 
		 List<Map<String, Object>> in = aBank.getDisability(userId, startDate, endDate, chunk);
		 JSONArray out = new JSONArray();
		 for (Map o:in)
			 out.put(o);
		 
		 return out.toString();
	}
	
	public String getMidasScoreChart(Date startDate, Date endDate, DashboardHelpers.SliceType chunk) throws IOException, ParseException {
		return getMidasScoreChart(repo.getUser().getUserPk(), startDate, endDate, chunk);
	}
	
	
	public String getMidasScoreChart(String userId, Date startDate, Date endDate, DashboardHelpers.SliceType chunk) throws IOException, ParseException {
		 Accumulator2 ac = aBank.getMidasScore(userId,startDate,endDate,chunk);
		 return ac.getJson().toString();
	}
	
	public String getheadacheDT0(Date startDate, Date endDate) throws IOException {
		return getheadacheDT0(repo.getUser().getUserPk(), startDate, endDate);
	}

	
	public String getheadacheDT0(String userId, Date startDate, Date endDate) throws IOException {
		
		 List<Map> in = repo.getDateRangeDTO(userId, startDate, endDate);
		 JSONArray out = new JSONArray();
		 for (Map o:in)
			 out.put(o);
		 
		 return out.toString();	
	}
	
	

	@Override
	public String getPainLocations(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		return getPainLocations(repo.getUser().getUserPk(), startDate, endDate).toString();
	}
	
	public JSONArray getPainLocations(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		 Iterable<Map> res = Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_PAINLOCATION_COUNT);
		 List<CouchLookup> h = lookups.findByLookupType(Kind.PAIN_LOCATION);
		 List<DateTime> chunks = chunk(startDate, endDate, SliceType.ALL);
		 HeadacheAccumulator a = new HeadacheAccumulator(h, Accumulater.byTest);
		 a.accumulate(chunks,Lists.newArrayList(res), Iterables.toArray(Iterables.transform(h, RepositoryHelpers.GET_IDS), String.class));
		 return a.postProcessJson(true, "axis","count");
	}
	

	@Override
	public String getPainLocationTable(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		return getPainLocationTable(repo.getUser().getUserPk(), startDate, endDate).toString();
	}
	
	public JSONArray getPainLocationTable(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		 
		List<Map> in =  Lists.newArrayList(Iterables.transform(repo.getDateRange(
					userId, startDate, endDate), DashboardHelpers.GET_PAINLOC_SEVERITY));
		List<CouchLookup> h = lookups.findByLookupType(Kind.PAIN_LOCATION);
		List<DateTime> chunks = chunk(startDate, endDate, SliceType.ALL);
		HeadacheAccumulator a = new HeadacheAccumulator(Accumulater.byfloatTest, 
				Iterables.toArray(Iterables.transform(h, RepositoryHelpers.GET_IDS), String.class));
	 
		a.accumulate(chunks, in, Iterables.toArray(Iterables.transform(h, RepositoryHelpers.GET_IDS), String.class)); 
		Map<String, String> lu = RepositoryHelpers.enMap(h);
		JSONArray results = new JSONArray();

		Map src = a.postProcess(true).get(0);
		
		for (String key: lu.keySet()) {
			
			JSONObject item = new JSONObject();
			if (!src.containsKey(key)) {
				continue;
			}
			item.put("name", lu.get(key));
			item.put("count", a.totalCounter.get(key));
			float total = Float.parseFloat(src.get(key).toString());
			DecimalFormat df = new DecimalFormat("###.###");
			item.put("avg", df.format(total/ Float.parseFloat(a.totalCounter.get(key).toString())));
			results.put(item);
			  
		 }
		  
		 return results;
	}
	
	@Override
	public String getPainTypes(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		return getPainTypes(repo.getUser().getUserPk(), startDate, endDate).toString();
	}
	
	public JSONArray getPainTypes(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		 Iterable<Map> res = Iterables.transform(repo.getDateRange(userId, startDate, endDate), GET_PAINTYPE_COUNT);
		 List<CouchLookup> h = lookups.findByLookupType(Kind.PAIN_TYPE);
		 List<DateTime> chunks = chunk(startDate, endDate, SliceType.ALL);
		 
		 HeadacheAccumulator a = new HeadacheAccumulator(h, Accumulater.byTest);
		 a.accumulate(chunks,Lists.newArrayList(res), Iterables.toArray(Iterables.transform(h, RepositoryHelpers.GET_IDS), String.class));
		 return a.postProcessJson(true, "axis","count");
	}
	
	@Override
	public String getPainTypeTable(Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		return getPainTypeTable(repo.getUser().getUserPk(), startDate, endDate).toString();
	}
	
	public JSONArray getPainTypeTable(String userId, Date startDate, Date endDate) throws IOException, ParseException, JSONException {
		
		 
		List<Map> in =  Lists.newArrayList(Iterables.transform(repo.getDateRange(
					userId, startDate, endDate), DashboardHelpers.GET_PAINTYPE_SEVERITY));
		List<CouchLookup> h = lookups.findByLookupType(Kind.PAIN_TYPE);
		List<DateTime> chunks = chunk(startDate, endDate, SliceType.ALL);
		HeadacheAccumulator a = new HeadacheAccumulator(Accumulater.byfloatTest, 
				Iterables.toArray(Iterables.transform(h, RepositoryHelpers.GET_IDS), String.class));
	 
		a.accumulate(chunks, in, Iterables.toArray(Iterables.transform(h, RepositoryHelpers.GET_IDS), String.class)); 
		Map<String, String> lu = RepositoryHelpers.enMap(h);
		JSONArray results = new JSONArray();

		Map src = a.postProcess(true).get(0);

		for (String key: lu.keySet()) {
			
			JSONObject item = new JSONObject();
			if (!src.containsKey(key)) {
				continue;
			}
			item.put("name", lu.get(key));
			item.put("count", a.totalCounter.get(key));
			float total = Float.parseFloat(src.get(key).toString());
			DecimalFormat df = new DecimalFormat("###.###");
			item.put("avg",df.format(total/ Float.parseFloat(a.totalCounter.get(key).toString())));
			results.put(item);
			  
		 }
		  
		 return results;
	}
	
	
	/***************************************************************************/
	/** Test methods follow.  These are used by the jasmine client side tests **/
	
	public String testStackedBarChart(String userId, Date startDate, Date endDate)  {
		
		 ArrayList<GregorianCalendar> months = Lists.newArrayList(new GregorianCalendar(2011,0,31),
				 new GregorianCalendar(2011,1,28),
				 new GregorianCalendar(2011,2,31),
				 new GregorianCalendar(2011,3,30),
				 new GregorianCalendar(2011,4,31),
				 new GregorianCalendar(2011,5,30),
				 new GregorianCalendar(2011,6,31),
				 new GregorianCalendar(2011,7,31),
				 new GregorianCalendar(2011,8,30),
				 new GregorianCalendar(2011,9,30),
				 new GregorianCalendar(2011,10,30),
				 new GregorianCalendar(2011,11,31)
				 
		 );
				 


		 JSONArray out = new JSONArray();
		 Random randomGenerator = new Random();
		 for(Calendar m :months){
			 Map <String,Object> item = new LinkedHashMap<String,Object>();
			 DateTime dt = new DateTime(m);
			 item.put("axis",dt.getMillis());
			 item.put("Probable Migraine", randomGenerator.nextInt(5)+1);
			 item.put("Migraine Headache", randomGenerator.nextInt(5)+1);
			 item.put("Tension Headache", randomGenerator.nextInt(5)+1);
			 item.put("Unclassified Headache", randomGenerator.nextInt(5)+1);
			 out.put(item); 
		 }
		 
		 return out.toString();	
	}
	
	
	public String testMidasGraph(String userId, Date startDate, Date endDate) {
		
		JSONArray results = new JSONArray();
		
				MidasGraphItem.create (results,100,false, new GregorianCalendar(2011,0,31));
				MidasGraphItem.create (results,80,false, new GregorianCalendar(2011,1,28));
				MidasGraphItem.create (results,100,false, new GregorianCalendar(2011,2,31));
				MidasGraphItem.create (results,0,false, new GregorianCalendar(2011,3,30));
				MidasGraphItem.create (results,70,false, new GregorianCalendar(2011,4,31));
				MidasGraphItem.create (results,100,false, new GregorianCalendar(2011,5,30));
				MidasGraphItem.create (results,100,false, new GregorianCalendar(2011,6,31));
				MidasGraphItem.create (results,65,false, new GregorianCalendar(2011,7,31));
				MidasGraphItem.create (results,100,false, new GregorianCalendar(2011,8,30));
				MidasGraphItem.create (results,22,false, new GregorianCalendar(2011,9,30));
				MidasGraphItem.create (results,6,false, new GregorianCalendar(2011,10,30));
				MidasGraphItem.create (results,12,false, new GregorianCalendar(2011,11,31));
		
		return results.toString();
			
	}
	
	
	public String testAcuteTreatmentGraph(String userId, Date startDate, Date endDate, SliceType type) throws ParseException, JSONException {
		
		 ArrayList<Treatment> h = Lists.newArrayList(Treatment.minimal("one", "first"),
				 Treatment.minimal("two", "Second"),Treatment.minimal("three", "Third"),Treatment.minimal("four", "Fourth"), Treatment.minimal("five", "Fifth") );
		 
		 HeadacheAccumulator a = TestHelpers.contructAcuteTreatment(h, new GregorianCalendar(2011,7,31), new GregorianCalendar(2011,10,31),
				 new GregorianCalendar(2011,8,10), new GregorianCalendar(2011,8,11),new GregorianCalendar(2011,9,10),new GregorianCalendar(2011,10,10),
				 new GregorianCalendar(2011,9,11));
		 return a.postProcessJson(false, false).toString();

	}
}
