package com.betterqol.iheadache.diary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jettison.json.JSONArray;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.AppHelpers;
import com.betterqol.iheadache.accumulators.AccumulatorBank;
import com.betterqol.iheadache.accumulators.PTUsageAccumulator;
import com.betterqol.iheadache.evaluations.impl.HeadacheEvaluator;
import com.betterqol.iheadache.evaluations.impl.MidasScorer;
import com.betterqol.iheadache.model.AppSettings.DrugRules;
import com.betterqol.iheadache.model.CouchLookup;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.Disability;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.HeadachePain;
import com.betterqol.iheadache.model.HeadacheTreatment;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.betterqol.iheadache.repository.UserInformationRepository;
import com.betterqol.iheadache.resource.DiaryResource;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

@Component
@Path(DiaryService.URL)
@RolesAllowed(value = { "ROLE_USER" })
/**
 * This is the main service class for the diary and the CRUD operations for a headache
 * @author rob  
 *
 */
public class DiaryService implements DiaryResource{
	
	
	public static final String URL = "/app/service/diary";
	
	@Autowired
	HeadacheRepository repo;
	
	@Autowired
	LookupRepository lookups;
	
	@Autowired
	UserInformationRepository users;
	
	@Autowired
	AccumulatorBank accumulators;
	


	@Override
	public List<JsonNode> get(Date date) throws IOException {
		
		return repo.getDateRangeDTO2(repo.getUser().getUserPk(), date, date);
	}
	

	public List<JsonNode> getFor(String userId, Date date) throws IOException {
		
		return repo.getDateRangeDTO2(userId, date, date);
	}
	
	@Override
	public Headache getFirst(Date date) throws IOException {
		
		String userId = repo.getUser().getUserPk();
		List<Headache> res = repo.getDateRange(userId, date, date);
		return res.size()>0 ? res.get(0): createDecoratedHeadache(userId);
	}
	
	@Override
	public Headache getEmpty() throws IOException {
	
		return createDecoratedHeadache(repo.getUser().getUserPk());
	}
	
	
	public String getPTreaments(int year, int month) throws IOException {
		
		PTUsageAccumulator acc = accumulators.getOneMonthPT(repo.getUser().getUserPk(), year, month);
		DrugRules drugRule = users.getCurrent().getAppSettings().getDrugRule();
		JSONArray results = new JSONArray();
		results.put(PTUsageAccumulator.getJson(acc.started_results.get(0),drugRule));
		results.put(PTUsageAccumulator.getJson(acc.ended_results.get(0),drugRule));
		results.put(PTUsageAccumulator.getJson(acc.ongoing_results.get(0),drugRule));
		return results.toString();
	}
	
	
	
	Headache createDecoratedHeadache(String userId) {
		
		System.out.println(userId);
		
		Headache o = new Headache();
		o.setId("new");
		o.setUser(userId);
		UserInformation user = users.get("u_"+userId);
		o.setStart(new DateTime());
		o.setEnd(new DateTime());
		o.setDisability(new Disability());
		o.setTreatments(new ArrayList<HeadacheTreatment>() );
		o.setPains(new ArrayList<HeadachePain>() );
		
		o.setKind(new LookupDTO("UNCLASSIFIED_HEADACHE", "Unclassified Headache"));
		
		o.setSymptoms(Lists.newArrayList(Iterables.transform(AppHelpers.getStandardSymptoms(lookups), RepositoryHelpers.MAKE_QUESTION)));
		 
		o.getSymptoms().addAll(
				 AppHelpers.constructSymptoms(user.getActiveCustomSymptoms(),user.getCustomSymptoms())
		 );
		 
    	o.getDisability().setResponses(Lists.newArrayList(Iterables.transform(lookups.findByLookupType(Kind.DISABILITY_QUESTION), 
    			RepositoryHelpers.MAKE_QUESTION)));
		
		return o;		
	}
	
	@Override
	public String create(Headache h) {
		
		scoreHeadache(h);
		return repo.create(h);
	}
	
	@Override
	public void update(Headache h) {
		
		scoreHeadache(h);
		repo.update(h);
	}
	
	public void scoreHeadache(Headache h) {
		
		HeadacheEvaluator eval = new HeadacheEvaluator();
		MidasScorer eval2 = new MidasScorer();
		Map context = new HashMap();
		
		context.put("USER",users.getCurrent());
		context.put("HEADCAHE_TYPES", RepositoryHelpers.enMap2(lookups.findByLookupType(Kind.HEADACHE_TYPE)));
		
		eval.transform(h, context);
		eval2.transform(h, context);
	}
	
	@Override
	public  List<CouchLookup> getStandardSymptoms() {
		
		return AppHelpers.getStandardSymptoms(lookups);
	}

}
