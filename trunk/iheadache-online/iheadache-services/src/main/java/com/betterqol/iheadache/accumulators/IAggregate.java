package com.betterqol.iheadache.accumulators;

import java.util.List;
import java.util.Map;

import org.joda.time.base.BaseDateTime;

import com.google.common.collect.Multimap;

public interface IAggregate {
	
	List<Map> aggregate(Multimap<BaseDateTime, Map> source, BaseDateTime chunk);

}
