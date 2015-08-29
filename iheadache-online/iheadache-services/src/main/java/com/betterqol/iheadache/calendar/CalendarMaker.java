package com.betterqol.iheadache.calendar;


import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.model.dto.CalendarItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class CalendarMaker {
	
	
	public static List<CalendarItem> makeForMonth(int year, int month, List<Map> headaches) {
		
		List<CalendarItem> results = Lists.newArrayList(); 
		Calendar cal = new GregorianCalendar(year, month,1);
		Map<Integer, Map> hMap = enmapHeadaches(headaches);
		
		Integer[] _cal = makeRawCalendar(cal);
		for (Integer i : _cal){	
			if (hMap.containsKey(i)){
				Map h = hMap.get(i);
				results.add(new CalendarItem(i,h.get(HeadacheMap.kind.toString()).toString(),
						h.get(HeadacheMap.id.toString()).toString()));
			}
			else {
				results.add(new CalendarItem(i,"","test"));
			}
			
		}		
		return results;
	}
	

	public static List<CalendarItem> makeForMonth(int year, int month) {
		
		List<CalendarItem> results = Lists.newArrayList(); 
		Calendar cal = new GregorianCalendar(year, month,1);
		
		Integer[] _cal = makeRawCalendar(cal);
		for (Integer i : _cal) {
			if (i==null){
				
				results.add(new CalendarItem(i,"","test"));
				continue;
			}
				
			results.add(new CalendarItem(i,"UNCLASSIFIED_HEADACHE","test"));
		}
		
		return results;
	}
	
	public static Integer[] makeRawCalendar(Calendar c) {
		
		Integer [] results = new Integer[42];
		int offset =  getFirstOfMonth(c);
		for (int i = 1; i <= c.getMaximum(Calendar.DAY_OF_MONTH); i = i + 1) 
			results[offset + i] = i;
		
		return results;
	}
	
	public static int getFirstOfMonth(Calendar c) {
		
		return c.get(Calendar.DAY_OF_WEEK)-2; //The days of the week are 1 based! WTF?
	}
	
	public static Map<Integer,Map> enmapHeadaches(List<Map> headaches) {
		Map<Integer,Map> results = Maps.newHashMap();
		for (Map in: headaches){
			DateTime startDay = new DateTime(in.get((HeadacheMap.start.toString())));
			results.put(startDay.getDayOfMonth(), in);
		}
		return results;
	}

}
