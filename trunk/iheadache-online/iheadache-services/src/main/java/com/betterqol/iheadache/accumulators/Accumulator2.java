package com.betterqol.iheadache.accumulators;

import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;

import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * Class for slicing and dicing headache information.  It aggregates an IAccumulate and 
 * an IAggregate to do the actual work.
 * @author rob
 *
 */
public class Accumulator2 {
	

	Multimap<BaseDateTime, Map> contents;
	IAggregate aggregator;
	IAccumulate accumulator;
	
	private List<Map> results;
	
	
	public Accumulator2(IAggregate aggregator, IAccumulate accumulator, List<Map> data) {	
		
		contents = RepositoryHelpers.DayMap(data, HeadacheMap.start.toString());
		this.aggregator = aggregator;
		this.accumulator = accumulator;
	}
	
	public Accumulator2(IAggregate aggregator, IAccumulate accumulator, List<Map> data, String field) {	
		
		contents = RepositoryHelpers.DayMap(data,field);
		this.aggregator = aggregator;
		this.accumulator = accumulator;
	}
	
	
	public void accumulate (List<DateTime> chunks) {
		results =  Lists.newArrayList();
		for (BaseDateTime chunk: chunks) {
			results.add(
					accumulator.accumulate(aggregator.aggregate(contents, chunk),chunk)); 
		}

	}
	

	public static List<Map> getForRange(Multimap<BaseDateTime, Map> contents,BaseDateTime start, BaseDateTime end) {
		
		 List<Map> results = Lists.newArrayList();
		 for (BaseDateTime key: contents.keySet()){ 
			 if (key.isAfter(end))
				 break;
			 
			 if (start.compareTo(key)<1)
				 results.addAll(contents.get(key));
		 }
		 return results;
		
	}


	public List<Map> getResults() {
		return results;
	}
	
	public JSONArray getJson(){
		JSONArray out = new JSONArray();
		for (Map each: results)
			out.put(each);
		return out;
	}
	




}
