package com.betterqol.iheadache.extensions;

import java.util.AbstractMap.SimpleEntry;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jettison.json.JSONException;
import org.ektorp.Page;
import org.ektorp.PageRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.log.LogService;
import com.betterqol.iheadache.model.CouchLookup;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.YesNoResponse;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/***
 * We need a paging adapter that allows for complex sorting/filtering after the data comes out of couch.  This class extends the couch 
 * paged request support to work client side. Basically we fetch a page of data at a time and filter, if we don't have enough to 
 * make a page we get the next etc.
 * @author rob
 *
 */
@Component
public class PagingAdapter {

	@Autowired
	HeadacheRepository repo;
	
	@Autowired
	LookupRepository lookups;
	
	
	 List<CouchLookup> standardSymptoms;

	public int pageSize = 12;
	private static final Logger logger = LoggerFactory.getLogger(PagingAdapter.class);
	
	public List<CouchLookup> getStandardSymptoms() {
		if (standardSymptoms != null)
			return standardSymptoms;
		standardSymptoms = lookups.findByLookupType(Kind.SYMPTOM);
		return standardSymptoms;
		
	}


	public Page getHeadachePage(String userId, Date startDate, Date endDate,
			PageRequest pr, int[]criteria, int[] types ) throws JSONException {

		List<Map> contents = Lists.newArrayList();
		pageSize = pr.getPageSize();
		PageRequest prev = null;
		PageRequest next = pr;
		int totalRecords = 0;
		while (contents.size() < pageSize){

			if (next == null)
				break;

			if (logger.isDebugEnabled())
				logger.debug("Grabbing more starting with " + next.asJson());

			Page<Headache> in = repo.getPagedDateRange(userId, startDate, endDate,
					next, "by_date");
			totalRecords = in.getTotalSize();

			if (prev == null)
				prev = in.getPreviousPageRequest();

			next= in.getNextPageRequest();

			for (Headache h : in.getRows()) {
				Map hMap = this.checkForCompletion(h);
				if (logger.isDebugEnabled())
					logger.debug("Candidate " + h.getId() + " at " + h.getStart());

				if (contents.size()>= pageSize){
					next = prev.getNextPageRequest(RepositoryHelpers.createDateKey(userId, h.getStart().toDate()).toJson(), h.getId());
					break;
				} 

				if (HEADACHE_UI_CRTIRIA(types).apply(hMap))
					if (HEADACHE_UI_CRTIRIA(criteria).apply(hMap))
					{
						if (logger.isDebugEnabled())
							logger.debug("Adding " + h.getId());
						contents.add(hMap);
					}	

			}
		}
		return new Page(contents,totalRecords,pageSize,prev,next);
	}



	/***
	 * A few functions that help in filtering the log by complete/incomplete items
	 * @param criteria
	 * @return
	 */
	public static Predicate HEADACHE_UI_CRTIRIA(final int[] criteria) {
		
		final  Multimap<String,String> innerCriteria = constructFilter(criteria);
		Predicate p = new Predicate<Map>() {
			@Override
			public boolean apply(Map input) {
				if (innerCriteria.size() ==0)
					return true;
				for (Entry<String, String> e: innerCriteria.entries()) {
					if (input.get(e.getKey()).toString().equals(e.getValue()))
						return true;
				}
				return false;
			}

		};
		return p;
	}

	/***
	 * Constructs the filtering criteria based on the identifiers passed from the ui
	 * @param criteria
	 * @return
	 */
	public static Multimap<String,String> constructFilter(final int[] criteria) {
		SimpleEntry[] labels = {new SimpleEntry("hasTreatments","false"),new SimpleEntry("hasSymptoms","false"),
				new SimpleEntry("hasTriggers","false"),new  SimpleEntry("hasDisability","false"),
				new SimpleEntry("hasPains","false"),new SimpleEntry("hasNote","false"),
				new SimpleEntry("kind","Migraine Headache"),new SimpleEntry("kind","Probable Migraine"),
				new SimpleEntry("kind","Tension Headache"),new SimpleEntry("kind","Unclassified Headache"),
				new SimpleEntry("kind","No Headache")};
		Multimap<String,String> results =  ArrayListMultimap.create();
		if (criteria==null)
			return results;
		for (int each: criteria)
			results.put(labels[each].getKey().toString(), labels[each].getValue().toString()); 	  	
		return results;	
	}

	
	/***
	 * A function that checks a headache for completion.  The same logic is mirrored client side in the
	 * diary controller, we may or may not want to do something about this.
	 */
	public Map checkForCompletion(Headache h) {

		Map results = new HashMap();
		results.put(HeadacheMap.id.toString(), h.getId());
		results.put(HeadacheMap.start.toString(), LogService.fmt.print(h.getStart()));
		results.put(HeadacheMap.end.toString(), LogService.fmt.print(h.getEnd()));
		results.put(HeadacheMap.kind.toString(), h.getKind()
				.getDescription());
		results.put(HeadacheMap.midas.toString(), h.getMIDAS());
		if (h.getDisability() != null)
			results.put(
					"disabled",
					LogService.createDisabiltySLabel(h.getDisability()
							.getPartiallyDisabled(), h.getDisability()
							.getCompletelyDisabled()));
		results.put("hasTreatments", ((h.getTreatments() != null) && (h
				.getTreatments().size() > 0)));
		
		
		
		results.put("hasSymptoms",checkSymptomCompletion(h,this.getStandardSymptoms()) );
		results.put("hasTriggers", ((h.getTriggers() != null) && (h
				.getTriggers().size() > 0)));
		results.put("hasNote", StringUtils.hasText(h.getNote()));
		
		boolean disabledComplete = false;
	
		if (h.getDisability()!= null) {
			disabledComplete = (h.getDisability().getPartiallyDisabled() >0) | (h.getDisability().getCompletelyDisabled() >0);
			for (YesNoResponse r :h.getDisability().getResponses()) {
				if (r.isYes() ==true) disabledComplete = true;
				if (r.isNo() ==true) disabledComplete= true;
			}
		}
			
		results.put("hasDisability", disabledComplete );
		results.put("hasPains", ((h.getPains() != null) && (h.getPains()
				.size() > 0)));


		return results;
	}
	
	
	public static boolean checkSymptomCompletion(Headache h, List<CouchLookup> standardSymptoms) {
		if ((h.getSymptoms() == null) || (h.getSymptoms().size()==0)) return false;
		for (CouchLookup s: standardSymptoms) 
			for (YesNoResponse response : h.getSymptoms()) {
				if (response.getId().equals(s.getId()))
					if ((response.isNo()==false) && (response.isYes()==false))
						return false;
			}
		return true;
	}

}
