package com.betterqol.iheadache.accumulators;

import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;

import com.betterqol.iheadache.dashboard.DashboardHelpers;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

/**
 * An accumulator that is good at aggregating values, for example
 * headache duration or pain intensity.
 * @author rob
 *
 */
public class ValueAccumulator implements IAggregate, IAccumulate {
	
	private List<String> field;
	private IAggregate aggregator;
	
	public ValueAccumulator (IAggregate aggregator, String...field){
		this.field = Lists.newArrayList(field);
		this.aggregator = aggregator;
	}
	
	public ValueAccumulator (IAggregate aggregator, List<String> fields){
		this.field = fields;
		this.aggregator = aggregator;
	}
	
	public ValueAccumulator ( List<String> fields){
		this.field = fields;
		this.aggregator = new IAggregate(){

			@Override
			public List<Map> aggregate(Multimap<BaseDateTime, Map> source,
					BaseDateTime chunk) {
				DateTime start = new DateTime(chunk);
				return Accumulator2.getForRange(source, start.minusDays(30), chunk);

			}};
	}

	@Override
	public Map<String, Object> accumulate(List<Map> source,
			BaseDateTime chunk) {
		
		Map<String, Object> results = DashboardHelpers.loadMap(field);
		results.put("axis", new DateTime(chunk));
		for (Map each : source) {
			for (String p :field){	
				Integer value = Integer.parseInt(each.get(p).toString());
				Integer total = Integer.decode(results.get(p).toString());
				results.put(p, total + value);	
				
			}
		}
		results.put("count", source.size());
		return results;
	}

	@Override
	public List<Map> aggregate(Multimap<BaseDateTime, Map> source,
			BaseDateTime chunk) {	
		return aggregator.aggregate(source, chunk);
	}
}
