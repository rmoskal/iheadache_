package com.betterqol.iheadache.accumulators;

import java.text.ParseException;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;

import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.google.common.collect.Lists;

/**]
 * Simple accumulator for chunking and accumlating some properties
 * @author rob
 *
 */
public class SimpleAccumulator {
	
	public List<List<Map>> results = Lists.newArrayList();
	
	public SimpleAccumulator () {
		
		
	}
	
	public void accumulate(List<DateTime> chunks, List<Map> headacheData ) throws ParseException {
		ListIterator<Map> iterator = headacheData.listIterator(); //we pass around the iterator so we can keep our place in the list
	  	for (DateTime chunk :chunks ) {
	  		results.add(accumulate(chunk,iterator));
	  	}
	}
	
	
	public List<Map> accumulate (DateTime chunk, ListIterator<Map> iter) throws ParseException {
		
		List<Map> acumulator = Lists.newArrayList(); 
		while (iter.hasNext()){
			Map in = iter.next();
			DateMidnight startTime = new DateMidnight(Long.parseLong(in.get(HeadacheMap.start.toString()).toString()));
			if (startTime.compareTo(chunk) >0){
				iter.previous();
				return acumulator;
			}
		    			
			acumulator.add(in);
		}
		return acumulator;
	}	
	
	
	public  List<List<JSONObject>> getJson(){
		 List<List<JSONObject>> res = Lists.newArrayList();
		 for (List<Map> l :this.results){
			 List<JSONObject> each = Lists.newArrayList();
			 for (Map o:l ){
				 each.add(new JSONObject(o));
			 }
			 res.add(each);
		 }
		 return res;
		 
	}

	

}
