package com.betterqol.iheadache.repository;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.ektorp.BulkDeleteDocument;
import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.base.BaseDateTime;

import com.betterqol.iheadache.model.IDescription;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.YesNoResponse;
import com.google.common.base.Function;
import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

public abstract class RepositoryHelpers {

	/**
	 * A helper that lets us delete bulk delete a list of documents in the
	 * database.
	 * 
	 * @param db
	 * @param entities
	 */
	public static void bulkDelete(CouchDbConnector db,
			Collection<? extends Object> entities) {

		List<BulkDeleteDocument> deleteList = Lists.newArrayList();
		for (Object post : entities) {
			deleteList.add(BulkDeleteDocument.of(post));
		}
		db.executeBulk(deleteList);
	}
	



	/**
	 * Create the key we use for the bulk of our views. basically:
	 * 
	 * [user,year,month,day of month]
	 * 
	 * @param userId
	 * @param date
	 * @return
	 */
	public static ComplexKey createDateKey(String userId, Date date) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);

		return ComplexKey.of(userId, c.getTime().getTime());

	}
	
	/**
	 * returns one millisecond before midnight of the current day
	 * @return
	 */
	public static Date getEOD(Date day) {
		
		DateMidnight e = new DateMidnight(day);
		e = e.plusDays(1);
		
		DateTime e2 = new DateTime(e);
		e2 = e2.minusMillis(1);
		return e2.toDate();
	}

	/**
	 * A little helper for returning the ids from a list of entities
	 */
	public static final Function<IDescription, String> GET_IDS = new Function<IDescription, String>() {
		public String apply(IDescription hasId) {
			return hasId.getId();
		}
	};

	public static final Function<IDescription, YesNoResponse> MAKE_QUESTION = new Function<IDescription, YesNoResponse>() {
		public YesNoResponse apply(IDescription hasId) {
			return new YesNoResponse (hasId);
		}
	};

	public static final Map<String, String> enMap(
			Iterable<? extends IDescription> i) {

		Map<String, String> results = Maps.newHashMap();
		for (IDescription each : i)
			results.put(each.getId(), each.getDescription());

		return results;
	}
	
	public static final Function<String, IDescription> MAKE_IDESCRIPTION = new Function<String,IDescription>() {
		public IDescription apply(String isId) {
			return new LookupDTO(isId,isId);
		}
	};
	
	public static final Map<String, LookupDTO> enMap2(
			Iterable<? extends IDescription> i) {

		Map<String, LookupDTO> results = Maps.newHashMap();
		for (IDescription each : i)
			results.put(each.getId(), new LookupDTO(each) );

		return results;

	}
	
	public static final Multimap<BaseDateTime, Map> DayMap(
			Iterable<Map> in, String property) {

		Multimap<BaseDateTime, Map> results = LinkedHashMultimap.create();
		DayMap(results,in,property);
		return results;

	}
	
	public static final void DayMap(Multimap<BaseDateTime, Map> results,
			Iterable<Map> in, String property) {

		for (Map i : in){
			DateTime t = new DateTime((i.get(property)));
			results.put(new DateMidnight (t), i);
		};
	}
	
	public static final Map<Object, Map> MapToMap(List<Map> in,String property) {
		Map<Object, Map> results = Maps.newLinkedHashMap();
		for (Map i : in)
			results.put(i.get(property), i);
		return results;
	}
	
	public static final List<LookupDTO> enList(
			Iterable<? extends IDescription> i) {

		List<LookupDTO>  results = Lists.newArrayList();
		for (IDescription each : i)
			results.add(new LookupDTO(each) );

		return results;
	}
	


}
