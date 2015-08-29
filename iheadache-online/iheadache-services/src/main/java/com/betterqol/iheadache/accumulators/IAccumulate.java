package com.betterqol.iheadache.accumulators;

import java.util.List;
import java.util.Map;

import org.joda.time.base.BaseDateTime;

public interface IAccumulate {

	Map<String,Object>  accumulate(List<Map> source, BaseDateTime chunk);
}
