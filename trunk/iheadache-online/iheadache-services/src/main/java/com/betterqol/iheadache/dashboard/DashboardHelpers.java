package com.betterqol.iheadache.dashboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import com.betterqol.iheadache.model.AppSettings.DrugRules;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.HeadachePain;
import com.betterqol.iheadache.model.HeadacheTreatment;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.PreventativeTreatment;
import com.betterqol.iheadache.model.Treatment;
import com.betterqol.iheadache.model.YesNoResponse;
import com.betterqol.iheadache.model.dto.Counter;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Ordering;

public abstract class DashboardHelpers {

	public enum HeadacheMap {
		id, start, end, midas, partiallyDisabled, completelyDisabled, kind,note
	}

	public enum CalendarMap {
		id, cid, title, start, end, notes, ad
	}
	
	public static List<LookupDTO> SHORT_SYMPTOMS =  new ImmutableList.Builder<LookupDTO>()
	           .add(new LookupDTO("HAS_AURA", "Aura"))
	           .add(new LookupDTO("IS_ANILATERAL","One-sided"))
	           .add(new LookupDTO("IS_MOVEMENT", "Worse with movement"))
	           .add(new LookupDTO("HAS_NAUSEA", "Nausea"))
	           .add(new LookupDTO("HAS_PHOTOPHOBIA", "Light Sensitivity"))
	           .add(new LookupDTO("HAS_PHONOBIA","Noise Sensitivity"))
	           .add(new LookupDTO("HAS_OLFACTOPHOBIA", "Smell Sensitivity"))
	           .add(new LookupDTO("IS_NECK", "Neck Pain"))
	           .add(new LookupDTO("IS_SINUS", "Sinus Symptoms"))
	           .add(new LookupDTO("IS_THROBBING", "Throbbing"))
	           .build();

	public enum SliceType {

		_7 {
			@Override
			public DateTime roll(DateTime c) {
				
				return c;

			}

			@Override
			public DateTime decrement(DateTime c) {
				return c.minusDays(7);

			}

			@Override
			public int getDuration(DateTime c) {
				return 7;
			}

			@Override
			public String getLabel() {
				return "7 days";
			}
		},

		_28 {
			@Override
			public DateTime roll(DateTime c) {
				return c;
			}
			@Override
			public int getDuration(DateTime c) {
				return 28;
			}

			@Override
			public DateTime decrement(DateTime c) {
				return c.minusDays(28);

			}
			@Override
			public String getLabel() {
				return "28 days";
			}
		},
		_30 {
			@Override
			public DateTime roll(DateTime c) {
				
				return c;

			}

			@Override
			public DateTime decrement(DateTime c) {

				return c.minusDays(30);

			}

			@Override
			public int getDuration(DateTime c) {
				return 30;
			}
			
			@Override
			public String getLabel() {
				return "30 days";
			}
		},
		MONTH {
			@Override
			public DateTime roll(DateTime c) {
				return c.dayOfMonth().withMaximumValue();
				
			}

			@Override
			public DateTime decrement(DateTime c) {
				return c.minusMonths(1).dayOfMonth().withMaximumValue();

			}

			@Override
			public int getDuration(DateTime c) {
				int end = c.dayOfMonth().withMinimumValue().getDayOfMonth();
				int start = c.dayOfMonth().withMaximumValue().getDayOfMonth();
				return Math.abs(end - start);
			}
			
			@Override
			public String getLabel() {
				return "month";
			}
		},
		QUARTER {
			@Override
			public DateTime roll(DateTime date) {
				return getQuarterEnd(date);
			}

			@Override
			public DateTime decrement(DateTime c) {
				return getQuarterPrevious(c);

			}

			@Override
			public int getDuration(DateTime c) {
				Period p = new Period(getQuarterStart(c), getQuarterEnd(c),PeriodType.days());
				return p.getDays();
			}
			@Override
			public String getLabel() {
				return "quarter";
			}
		},
		ALL {
			@Override
			public DateTime roll(DateTime date) {
				return date;
			}

			@Override
			public DateTime decrement(DateTime c) {
				return new DateTime(0);

			}

			@Override
			public int getDuration(DateTime c) {
				return -1;
			}
			
			@Override
			public String getLabel() {
				return "period";
			}
		};

		public abstract DateTime roll(DateTime c);

		public abstract DateTime decrement(DateTime c);
		
		public abstract int getDuration(DateTime c);
		
		public abstract String getLabel();

	}

	public static Date extendDate(Date startDate, SliceType slice) {

		DateTime c = new DateTime(startDate);
		slice.roll(c);
		return c.toDate();

	}
	

	
	public static List<DateTime> chunk(Date startDate, Date end, SliceType type) {

		return chunk(new DateTime(startDate), new DateTime(end), type);

	}

	public static List<DateTime> chunk(GregorianCalendar  startDate, GregorianCalendar end, SliceType type) {

		return chunk(new DateTime(startDate), new DateTime(end), type);
	}

	public static List<DateTime> chunk(DateTime startDate,
			DateTime endDate, SliceType type) {

		List<DateTime> results = new ArrayList<DateTime>();
		endDate = type.roll(endDate);

		while (endDate.getMillis()>= (startDate.getMillis())) {
			results.add(endDate);
			endDate = type.decrement(endDate);
		}

		if (results.size() == 0)
			results.add(endDate);

		Collections.reverse(results);
		return results;

	}

	/*
	 * A little helper for counting triggers
	 */
	public static final Function<Headache, Map> GET_TRIGGERS = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			Map results = new LinkedHashMap();
			results.put(HeadacheMap.start.toString(), h.getStart().getMillis());
			results.put(HeadacheMap.kind.toString(), h.getKind().getId());
			for (LookupDTO t : h.getTriggers())
				results.put(t.getId(), 1);
			return results;
		}
	};
	
	/*
	 * A little helper for gathering notes
	 */
	public static final Function<Headache, Map> GET_NOTES = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			Map results = new LinkedHashMap();
			results.put(HeadacheMap.start.toString(), h.getStart().getMillis());
			results.put(HeadacheMap.kind.toString(), h.getKind().getDescription());
			results.put(HeadacheMap.note.toString(), h.getNote());
			return results;
		}
	};
	
	
	/*
	 * A little helper for counting symptoms
	 */
	public static final Function<Headache, Map> GET_SYMPTOMS = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			Map results = new LinkedHashMap();
			results.put(HeadacheMap.start.toString(), h.getStart().getMillis());
			results.put(HeadacheMap.kind.toString(), h.getKind().getId());
			for (YesNoResponse t : h.getSymptoms())
				if (t.isYes())
					results.put(t.getId(), 1);
			return results;
		}
	};

	/*
	 * A little helper that allows the accumulator to count treatments
	 */
	public static final Function<Headache, Map> GET_TREATMENTS = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			Map results = new LinkedHashMap();
			results.put(HeadacheMap.start.toString(), h.getStart().getMillis());
			results.put(HeadacheMap.kind.toString(), h.getKind().getId());
			for (HeadacheTreatment t : h.getTreatments())
				results.put(t.getTreatment().getId(), 1);
			return results;
		}
	};
	
	
	/*
	 * A little helper that allows the accumulator to count the number of treatments
	 */
	public static final Function<Headache, Map> GET_TREATMENT_COUNT = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			
			Multimap<String,String> interim =  ArrayListMultimap.create();
			for (HeadacheTreatment t : h.getTreatments())
				interim.put(t.getTreatment().getId(), t.getTreatment().getId());
			
			Map results = new LinkedHashMap();
			results.put(HeadacheMap.start.toString(), h.getStart().getMillis());
			results.put(HeadacheMap.kind.toString(), h.getKind().getId());
			for (String key: interim.keySet())
				results.put(key, interim.get(key).size());
			
		
			return results;
		}
	};
	/*
	 * A little helper that allows the accumulator to count the number pain locations
	 */
	public static final Function<Headache, Map> GET_PAINLOCATION_COUNT = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			
			Multimap<String,Counter> interim =  ArrayListMultimap.create();

			for (HeadachePain t : h.getPains()){
				for (LookupDTO loc :t.getPainLocation())
					interim.put(loc.getId(), new Counter(loc.getId(),t.getLevel()));
			}
			
			Map results = new LinkedHashMap();
			results.put(HeadacheMap.start.toString(), h.getStart().getMillis());
			results.put(HeadacheMap.kind.toString(), h.getKind().getId());
			for (String key: interim.keySet()) 
				results.put(key, interim.get(key).size());	
				
				
			return results;
		}
	};
	
	public static final Function<Headache, Map> GET_DISABILITY_COUNT = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			
			Map results = new LinkedHashMap();

			for (YesNoResponse t : h.getDisability().getResponses()){
					if (t.isYes())
						results.put(t.getId(),1);
			}		
			results.put(HeadacheMap.start.toString(), h.getStart().getMillis());
			results.put(HeadacheMap.kind.toString(), h.getKind().getId());	
			return results;
		}
	};
	
	public static final Function<Headache, Map> GET_PAINLOC_SEVERITY = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			
			Multimap<String,Counter> interim =  ArrayListMultimap.create();

			for (HeadachePain t : h.getPains()){
				for (LookupDTO loc :t.getPainLocation())
					interim.put(loc.getId(), new Counter(loc.getId(),t.getLevel()));
			}
			
			Map results = new LinkedHashMap();
			results.put(HeadacheMap.start.toString(), h.getStart().getMillis());
			results.put(HeadacheMap.kind.toString(), h.getKind().getId());
			for (String key: interim.keySet()) 
				results.put(key, gatherKeys(interim.get(key)));	
				
			
			return results;
		}
	};
	
	
	public static Map<String, String> enMapGenericName(Iterable<Treatment>teats) {
		
		Map<String, String> results = Maps.newHashMap();
		for (Treatment each: teats)
			results.put(each.getId(), each.getGenericName());
		
		return results;
	}
	
	public static Map<String, String> enMapBothNames(Iterable<Treatment>teats) {
		
		Map<String, String> results = Maps.newHashMap();
		for (Treatment each: teats)
			results.put(each.getId(), each.getDescription() + "/"+ each.getGenericName());
		
		return results;
	}
	
	public static Map<String, String> enMapPGenericName(Iterable<PreventativeTreatment>teats) {
		
		Map<String, String> results = Maps.newHashMap();
		for (PreventativeTreatment each: teats)
			results.put(each.getId(), each.getGenericName());
		
		return results;
	}
	
	public static Map<String, String> enMapPBothNames(Iterable<PreventativeTreatment>teats) {
		
		Map<String, String> results = Maps.newHashMap();
		for (PreventativeTreatment each: teats)
			results.put(each.getId(),  each.getDescription() + "/"+each.getGenericName());
		
		return results;
	}
	
	
	public static Map<String,String> getTreatmentNamer(DrugRules rule,Iterable<Treatment>teats ){
		
		switch(rule) {
		
			case GENERIC : return enMapGenericName(teats);
			case BOTH: 	return enMapBothNames(teats);
		    default: return RepositoryHelpers.enMap(teats);
		}
	}
	
	public static Map<String,String> getPTreatmentNamer(DrugRules rule,Iterable<PreventativeTreatment>teats ){
		
		switch(rule) {
		
			case GENERIC : return enMapPGenericName(teats);
			case BOTH: 	return enMapPBothNames(teats);
		    default: return RepositoryHelpers.enMap(teats);
		}	
	}

	
	public static float gatherKeys(Collection<Counter> items) {
		
		int results = 0;
		for (Counter each:items)
			results += each.count;
		return results/(float)items.size();
	}
	
	
	/*
	 * A little helper that allows the accumulator to count the number pain locations
	 */
	public static final Function<Headache, Map> GET_PAINTYPE_COUNT = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			
			Multimap<String,Counter> interim =  ArrayListMultimap.create();
			for (HeadachePain t : h.getPains())
				for (LookupDTO loc :t.getPainType())
					interim.put(loc.getId(), new Counter(loc.getId(),t.getLevel()));
			
			Map results = new LinkedHashMap();
			results.put(HeadacheMap.start.toString(), h.getStart().getMillis());
			results.put(HeadacheMap.kind.toString(), h.getKind().getId());
			for (String key: interim.keySet()) 
				results.put(key, interim.get(key).size());	
			
			return results;
		}
	};
	
	public static final Function<Headache, Map> GET_PAINTYPE_SEVERITY = new Function<Headache, Map>() {
		public Map apply(Headache h) {
			
			Multimap<String,Counter> interim =  ArrayListMultimap.create();
			for (HeadachePain t : h.getPains())
				for (LookupDTO loc :t.getPainType())
					interim.put(loc.getId(), new Counter(loc.getId(),t.getLevel()));
			
			Map results = new LinkedHashMap();
			results.put(HeadacheMap.start.toString(), h.getStart().getMillis());
			results.put(HeadacheMap.kind.toString(), h.getKind().getId());
			for (String key: interim.keySet()) {
				results.put(key, gatherKeys(interim.get(key)));	
			}
			return results;
		}
	};


	public static Map<String,Object>  loadMap( Iterable<String> categories) {
		
		Map<String,Object> accumulator = new LinkedHashMap<String,Object>();
		accumulator.put("axis", "waiting");
		for (String item:categories)
			accumulator.put(item, 0);		
		
		return accumulator;	
	}

	public static int getQuarter(DateTime date) {

		int roundUp = 0;
		int month = date.getMonthOfYear();
		if ((month % 3) > 0 ) roundUp = 1;
		return month / 3 + roundUp;
		//if ((date.getMonthOfYear() % 3) > 0 ) roundUp = 1;
		//return date.getMonthOfYear() /3 + roundUp;

	}
	
	public static DateTime getQuarterStart(DateTime date) {
		switch(getQuarter(date)){
			case 1: return date.withMonthOfYear(1).withDayOfMonth(1);
			case 2: return date.withMonthOfYear(4).withDayOfMonth(1);
			case 3: return date.withMonthOfYear(7).withDayOfMonth(1);
			default:  return date.withMonthOfYear(10).withDayOfMonth(1);
		}

	}
	
	public static DateTime getQuarterEnd(DateTime date) {
		switch(getQuarter(date)){
			case 1: return date.withMonthOfYear(4).withDayOfMonth(1).minusDays(1);
			case 2: return date.withMonthOfYear(7).withDayOfMonth(1).minusDays(1);
			case 3: return date.withMonthOfYear(10).withDayOfMonth(1).minusDays(1);
			default:  return date.dayOfYear().withMaximumValue();
		}

	}
	
	public static DateTime getQuarterPrevious(DateTime date) {
		int qtr = getQuarter(date);
		if (qtr >1)
			return getQuarterStart(date).minusDays(1);
	
		return date.minusYears(1).dayOfYear().withMaximumValue();

	}
	

	/**
	 * Filter out all data for actual headache
	 * @param res
	 * @return
	 */
	public static List<Map> getNoHeadaches(Iterable<Map> res) {
		res = Iterables.filter(res, new Predicate <Map>(){
	
			@Override
			public boolean apply(@Nullable Map input) {
				return input.get(HeadacheMap.kind.toString()).equals("NO_HEADACHE");
			}});
		return Lists.newArrayList(res);
	}
	
	


	/**
	 * Filter out all items that are not real headaches (NO_HEADACHE or informational
	 * @param res
	 * @return
	 */
	public static List<Map> getRealHeadaches(Iterable<Map> res) {
		res = Iterables.filter(res, new Predicate <Map>(){
	
			@Override
			public boolean apply(@Nullable Map input) {
				return ! input.get(HeadacheMap.kind.toString()).equals("NO_HEADACHE");
			}});
		return Lists.newArrayList(res);
	}
	
	
	/**
	 * Add a suffix to a list of strings, used for accumulators
	 * @param in
	 * @return
	 */
	static List<String> addSuffix(List<String> in, String suffix) {
		
		List<String> results = Lists.newArrayList();
		for (String s: in) {
			results.add(s);
			results.add(s + suffix);
		}
		
		return results;
		
	}
	

	
	static JSONArray makeSorted(JSONObject in, Map<String, String> lookups) throws JSONException {
		
		List temp = new ArrayList();
		Iterator i = in.keys();
		while (i.hasNext()) {
			Object key = i.next();
			temp.add(in.get(key.toString()));
		}
		
		Collections.sort(temp, Ordering.natural().onResultOf(
			    new Function<JSONObject, Integer>() {
			      public Integer apply(JSONObject from) {
			        try {
						return from.getInt("value");
					} catch (JSONException e) {
						return -1;
					}
			      }
			    }));
		
		return new JSONArray(temp) ;
	}
}


