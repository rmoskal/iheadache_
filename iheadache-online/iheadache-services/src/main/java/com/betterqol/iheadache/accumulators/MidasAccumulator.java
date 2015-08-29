package com.betterqol.iheadache.accumulators;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.base.BaseDateTime;

import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.google.common.collect.Multimap;

/**
 * The midas accumulator has to check on values from 90 days ago.
 * @author rob
 *
 */
public class MidasAccumulator extends ValueAccumulator{
	
	private DateTime firstDate;


	public MidasAccumulator(List<Map> items) {
		super(null, HeadacheMap.midas.toString());	
		firstDate = new DateTime(items.get(0).get("start"));
	}
	
	
	@Override
	public List<Map> aggregate(Multimap<BaseDateTime, Map> source,
			BaseDateTime chunk) {	
		DateTime start = new DateTime(chunk);
		List<Map> temp = Accumulator2.getForRange(source, start.minusDays(91), chunk);
		return temp;
	}
	
	@Override
	public Map<String, Object> accumulate(List<Map> source,
			BaseDateTime chunk) {
		Map<String, Object> res = super.accumulate(source, chunk);
		res.put("Estimated",-100);
		res.put("Actual",-100);
		res.put("None", -100);

		Days days = Days.daysBetween(firstDate, chunk);
		if (days.getDays() < 30 ) {
			res.put(HeadacheMap.midas.toString(),0);
			res.put("None", 0);
			return res;
		}
		
		if (days.getDays() < 90 ) {
			Integer currentPoints = new Integer(res.get(HeadacheMap.midas.toString()).toString());
			res.put(HeadacheMap.midas.toString(),90 * currentPoints/days.getDays() );
			res.put("Estimated",90 * currentPoints/days.getDays());
			return res;
		}
		
		res.put("Actual",new Integer(res.get(HeadacheMap.midas.toString()).toString()) );
		return res;
			
	}
}
