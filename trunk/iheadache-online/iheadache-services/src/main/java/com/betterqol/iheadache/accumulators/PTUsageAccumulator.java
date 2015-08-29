package com.betterqol.iheadache.accumulators;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.model.AppSettings.DrugRules;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/***
 * A special accumulator for preventative treatments. 
 * @author rob
 *
 */
public class PTUsageAccumulator {
	
	List<Map> started;
	List<Map> finshes;
	List<Map> full;
	public List<List<Map>> started_results;
	public List<List<Map>> ended_results;
	public List<List<Map>> ongoing_results;
	
	public PTUsageAccumulator (List<Map>  all, List<Map> completed, List<Map>starts) {
		started = starts;
		full = all;
		finshes = completed;
		
	}
	
	public void accumulate(List<DateTime> chunks,SliceType slice) {
		
		started_results = accumulate(chunks,started,Accumulater.startend,"start");
		ended_results = accumulate(chunks,finshes,Accumulater.startend,"end");
		ongoing_results = accumulateOngoing(chunks,full,Accumulater.ongoing,"_____");
	}
	

	public List<List<Map>> accumulate(List<DateTime> chunks, List<Map> ptData, Accumulater accum, String field) {
		
		List<List<Map>> results = Lists.newArrayList();
		ListIterator<Map> iterator = ptData.listIterator(); //we pass around the iterator so we can keep our place in the list
	  	for (DateTime chunk :chunks ) 
	  		results.add(accum.execute(chunk,iterator,field));  	
	  	
	  	return results;
	}
	
	public List<List<Map>> accumulateOngoing(List<DateTime> chunks, List<Map> ptData, Accumulater accum, String field) {
		
		List<List<Map>> results = Lists.newArrayList();
	  	for (DateTime chunk :chunks )  {
	  		ListIterator<Map> iterator = ptData.listIterator();
	  		results.add(accum.execute(chunk,iterator,field)); 
	  	}
	  	
	  	return results;
	}
	
	
	public enum Accumulater {
		
		startend	
		{
			@Override
			List<Map> execute(DateTime chunk, ListIterator<Map> iter,String field) {
		
				List<Map>acumulator = Lists.newArrayList();
				while (iter.hasNext()){
					Map in = iter.next();
					DateMidnight startTime = new DateMidnight(in.get(field));
					if (startTime.compareTo(chunk) >0){
						iter.previous();
						return acumulator;
					}
					//System.out.println("started " + in.get("treatmentDescription"));
					acumulator.add(in);
				}
				return acumulator;
			}
		},
		
		ongoing	
		{
			@Override
			List<Map> execute(DateTime chunk, ListIterator<Map> iter,String field) {;
				List<Map>acumulator = Lists.newArrayList();
				DateMidnight nchunk = new DateMidnight(chunk).withDayOfMonth(1);
				while (iter.hasNext()){
					Map in = iter.next();
					//System.out.println("onging " + in.get("treatmentDescription"));
					//System.out.println("onging " + in);
					DateMidnight startTime = new DateMidnight(in.get("start"));
					if (startTime.compareTo(nchunk)>=0) {
						if (!iter.hasNext()){
							iter.previous();
							return acumulator;
							
						}
						continue;
					}
					if (in.get("end") != null){	
						if (new DateMidnight(in.get("end")).isBefore(chunk))
							continue;	
					}
					acumulator.add(in);
				}
				return acumulator;	
			}
		};
		
		 abstract List<Map>  execute (DateTime chunk, ListIterator<Map> iter, String field) ;	
	}
	
	
	public static Map<DateTime,List<Map>> zip(List<List<Map>> in,List<DateTime> chunks) {
		
		Map<DateTime,List<Map>> results = Maps.newLinkedHashMap();
		for(int i= 0; i < chunks.size(); i++){
			results.put(chunks.get(i), in.get(i));
			}
		return results;
		
	}
	// DrugRules drugRule = users.getCurrent().getAppSettings().getDrugRule();
	// CEC 2014 June 7 changed treatmentDescription to drugRuleDescription and then changed template in report1.js
	// fixed problem of adding generic description to name multiple times in report.
	public static JSONArray getJson(List<Map>in, DrugRules rule){
		JSONArray out = new JSONArray();
		for (Map each: in){
			switch (rule) {
			//case BOTH : each.put("treatmentDescription", each.get("treatmentDescription") + " (" + each.get("genericDescription")+ ")");
			case BOTH : each.put("drugRuleDescription", each.get("treatmentDescription") + " (" + each.get("genericDescription")+ ")");
						break;
			case GENERIC:  each.put("drugRuleDescription", each.get("genericDescription"));
			case TRADE: each.put("drugRuleDescription", each.get("treatmentDescription"));
			}
			//System.out.println("pTreatment getJson " + each);
			out.put(each);
		}
		return out;
	}

	public static void getMap(Map each, DrugRules rule){
		//List<Map> ruled = List.newArrayList
		//for (Map each: in){
			switch (rule) {
			//case BOTH : each.put("treatmentDescription", each.get("treatmentDescription") + " (" + each.get("genericDescription")+ ")");
			case BOTH : each.put("treatmentDescription", each.get("treatmentDescription") + " (" + each.get("genericDescription")+ ")");
						break;
			case GENERIC:  each.put("treatmentDescription", each.get("genericDescription"));
			}
			System.out.println("pTreatment getMap " + each);
			//out.put(each);
		//}
		return;
	}
	


}
