package com.betterqol.iheadache.dashboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.model.IDescription;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.SortedSetMultimap;
import com.google.common.collect.TreeMultimap;

/**
 * Class that chunks a results set and tabulates the frequency of various attributes.  For example you can get a count of 
 * all the headache types within a given period. The actual accumulation work is done by the Accumulater class which implements the 
 * command pattern.
 * @author rob
 *
 */
@SuppressWarnings("rawtypes")
public class HeadacheAccumulator {
	
	Accumulater accumulator;
	List<String> categories;
	Map<String, Object> usageTracker = new HashMap<String, Object> ();
	public Map<String, Object> totalCounter = new HashMap<String, Object> ();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public List<Map<String, Object>> results = new ArrayList <Map<String, Object>>();
	Map <String,String> lookupValues;

	
	/**
	 * This is normally a complete set of lookup values to count that comes straight out of the database
	 * @param categories
	 */
	public HeadacheAccumulator(List<? extends IDescription> categories,Accumulater a) {	
		
		this.categories = Lists.newArrayList(Iterables.transform(categories, RepositoryHelpers.GET_IDS));
		
		lookupValues = new HashMap<String, String>();
		for (IDescription o: categories)
			lookupValues.put(o.getId(), o.getDescription());
			
		accumulator = a;
	}
	
	public HeadacheAccumulator(Map<String,String> nameTransformer, List<? extends IDescription> categories,Accumulater a) {	
		
		this.categories = Lists.newArrayList(Iterables.transform(categories, RepositoryHelpers.GET_IDS));
		lookupValues = nameTransformer;
		accumulator = a;
	}
	
	public HeadacheAccumulator(Accumulater a, String... categories) {	
		
		this.categories = Lists.newArrayList(categories);
		accumulator = a;
	}

	public void accumulate(List<DateTime> chunks, List<Map> headacheData,String[]field ) throws ParseException {
		
			usageTracker= DashboardHelpers.loadMap(categories);
			usageTracker.remove("axis");
			totalCounter = DashboardHelpers.loadMap(categories);
			totalCounter.remove("axis");
			ListIterator<Map> iterator = headacheData.listIterator(); //we pass around the iterator so we can keep our place in the list
		  	for (DateTime chunk :chunks ) {
		  		results.add(accumulate(chunk,iterator,field));
		  	}
	}
	
	
	public  List<Map<String, Object>> postProcess(boolean removeUnused, String... squelch ) {
		
			List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
			for (Map each: results){
				Map<String, Object> o = new LinkedHashMap<String,Object>();
				
				if (squelch != null) 
					for (String sq:squelch)
						each.remove(sq);
				
				if (removeUnused)
					for (String key:usageTracker.keySet()) 
						each.remove(key);
						
				if (lookupValues == null) {
					o.putAll(each);
					res.add(o);
					continue;
				}

			    for (Object pk: each.keySet()) 
			    		o.put(pk.toString(), each.get(pk));		
			    	
			   
			    res.add(o);
			}		
			return res;
	}
	
	public  JSONArray postProcessJson(boolean removeUnused, boolean take5, String... squelch) throws JSONException {
		
		List<String> top5 = null;
		take5 = false;
		if (take5==true)
			top5 = take5(this.totalCounter);
			
		JSONArray res = new JSONArray();
		for (Map each: results){
			
			JSONObject o = new JSONObject();
			
			if (removeUnused)
				for (String key:usageTracker.keySet()) 
					each.remove(key);
			
			if (squelch != null) 
				for (String sq:squelch)
					each.remove(sq);
			
			if (take5==true)
				consolidateTop5(top5,each);

		    for (Object pk: each.keySet()) {
		    	JSONObject _inner = new JSONObject();
		    	_inner.put("value",each.get(pk));
		    	_inner.put("name",this.lookupValues.get(pk));
		    	_inner.put("id",pk);
		    	o.put(pk.toString(), _inner);	
		    }
		    

		    res.put(o);
		}
		
		return res;
	}
	
	/**
	 * For certain new graphs we need to unroll the map and provide an individual 
	 * entry for each property
	 * @param removeUnused
	 * @param squelch
	 * @return
	 * @throws JSONException 
	 */
	public  JSONArray postProcessJson(boolean removeUnused, String... squelch) throws JSONException {
		JSONArray res = postProcessJson(removeUnused, false,squelch);
		JSONObject first = res.getJSONObject(0);
		JSONArray a = DashboardHelpers.makeSorted(first, this.lookupValues);

		return a;
	}
	
	
	public  List<JSONArray> postProcessJson2(boolean removeUnused, String... squelch) throws JSONException {
		JSONArray in = postProcessJson(false, false,squelch);
		List<JSONArray> res = Lists.newArrayList();
		for (int i = 0; i< in.length(); i++) {
			JSONArray inner = new JSONArray();
			JSONObject o = in.getJSONObject(i);
			Iterator itr = o.keys();
			while(itr.hasNext()) {
				
				String next = itr.next().toString();
				if (o.getJSONObject(next).has("value"))
					if (removeUnused) 
						if (o.getJSONObject(next).getInt("value")==0)
							continue;
				inner.put(o.getJSONObject(next));
			}
			res.add(inner);
			
		}
		
		return res;
	}
	
	/**
	 * Track only the top 5 items and aggregate the rest
	 * @param usageTracker
	 * @return
	 */
	public static List<String> take5 (Map<String, Object> usageTracker) {
		
		 List<String> results = Lists.newArrayList();
		 SortedSetMultimap<Integer, String> map =  TreeMultimap.create();
		 for (Entry<String, Object> entry: usageTracker.entrySet()) 
			 map.put( Integer.parseInt(entry.getValue().toString()), entry.getKey());
		 int total = 0;
		 Map<Integer, Collection<String>> _map = map.asMap();
		 List<Integer> _sortedKeys = Lists.newArrayList(map.keySet());
		 Collections.reverse(_sortedKeys);
		 
		 for (Integer key: _sortedKeys)
			 for (String name : _map.get(key)) {
				 results.add(name);
				 total++;
				 if (total>=5)
					 return results;
			 }
		 
		 return results; 
	}
	
	public static void consolidateTop5(List<String> top5, Map<String, Object> item) {
		int total = 0;
		Iterator<Entry<String, Object>> itr = item.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Object> each = itr.next();
			
			if ((!top5.contains(each.getKey()) &! (each.getKey().equals("axis")))){
				total += Integer.parseInt(each.getValue().toString());
				itr.remove();
			}
		}
		item.put("Other", total);
	}
	
	public Map<String,Object>  accumulate (DateTime chunk, ListIterator<Map> iter, String[] field ) throws ParseException {
		
		Map<String,Object> acumulator = DashboardHelpers.loadMap(categories);
		
		while (iter.hasNext()){
			Map in = iter.next();
			DateMidnight startTime = new DateMidnight(Long.parseLong(in.get(HeadacheMap.start.toString()).toString()));
			if (startTime.compareTo(chunk) >0){
				iter.previous();
				acumulator.put("axis", new DateTime(chunk));
				return acumulator;
			}
			accumulator.execute(acumulator, in, field,usageTracker, totalCounter);
		}
		acumulator.put("axis", new DateTime(chunk));
		return acumulator;
	}	
	
	public enum Accumulater {

		byCount	
		{
			@Override
			public void execute(Map<String, Object> acumulator, Map in,
					String[] field, Map <String, Object> scoreboard, Map<String, Object> totals) {
				
				Integer total = 0;
				Integer count = 0;
				if (acumulator.containsKey("count"))
					total = Integer.decode(acumulator.get("count").toString());
				
				for (String each: field){
					String value = in.get(each).toString();
					if (!acumulator.containsKey(value)) continue;
					count = Integer.decode(acumulator.get(value).toString());
					acumulator.put(value, ++count);	
					acumulator.put("count", ++total);
				}
			}
		},
		
		byValue	
		{
			@Override
			public void execute(Map<String, Object> acumulator, Map in,
					String[] field, Map <String, Object> scoreboard, Map<String, Object> totals) {
				
				Integer count = 0;
				if (acumulator.containsKey("count"))
					count = Integer.decode(acumulator.get("count").toString());
				
				for (String each: field){
					Integer value = Integer.parseInt(in.get(each).toString());
					Integer total = Integer.decode(acumulator.get(each).toString());
					acumulator.put(each, total + value);
					count += value;	
				}
				acumulator.put("count", count);
			}
		},
		
			
		byTest	
		{
			@Override
			public void execute(Map<String, Object> acumulator, Map in,
					String[] field, Map <String, Object> scoreboard, Map<String, Object> totals) {
				for (String each: field){
						if (in.containsKey(StringUtils.trim(each))){
							Integer total = Integer.decode(acumulator.get(each).toString());
							scoreboard.remove(each);
							acumulator.put(each, total + Integer.decode(in.get(each).toString()));	
							Integer grandTotal = Integer.decode(totals.get(each).toString());
							totals.put(each, grandTotal + Integer.decode(in.get(each).toString()));	
						}
							
				}
				}
			
			},
			byfloatTest	
			{
				@Override
				public void execute(Map<String, Object> acumulator, Map in,
						String[] field, Map <String, Object> scoreboard, Map<String, Object> totals) {
					for (String each: field)
							if (in.containsKey(each)){
								Float total = Float.parseFloat(acumulator.get(each).toString());
								scoreboard.remove(each);
								acumulator.put(each, total + Float.parseFloat(in.get(each).toString()));	
								Integer grandTotal = Integer.decode(totals.get(each).toString());
								totals.put(each, grandTotal +1);		
					}
				}
		};		

		public abstract  void execute (Map<String,Object> acumulator, Map in, String[] field, Map <String, Object> scoreboard, Map<String, Object> totals);
	}
}
