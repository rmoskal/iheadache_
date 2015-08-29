package com.betterqol.iheadache.accumulators;

import static com.betterqol.iheadache.dashboard.DashboardHelpers.GET_TREATMENT_COUNT;
import static com.betterqol.iheadache.dashboard.DashboardHelpers.GET_TRIGGERS;
import static com.betterqol.iheadache.dashboard.DashboardHelpers.chunk;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.dashboard.HeadacheAccumulator;
import com.betterqol.iheadache.dashboard.HeadacheAccumulator.Accumulater;
import com.betterqol.iheadache.model.AppSettings.DrugRules;
import com.betterqol.iheadache.model.CouchLookup;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.Treatment;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.PTRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.betterqol.iheadache.repository.TreatmentRepository;
import com.betterqol.iheadache.repository.UserInformationRepository;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

/***
 * There are certain queries reused among the dashboard and the reports. We move
 * the common ones here.
 * 
 * @author rob
 * 
 */
@Component
public class AccumulatorBank {

	@Autowired
	HeadacheRepository repo;

	@Autowired
	LookupRepository lookups;

	@Autowired
	PTRepository ptRepo;

	@Autowired
	UserInformationRepository users;

	@Autowired
	TreatmentRepository treatments;
	
	public SimpleAccumulator getNotes(String userId, Date startDate,
			Date endDate, SliceType chunk) throws IOException, ParseException {
		
		SimpleAccumulator ac = new SimpleAccumulator();
		List<DateTime> chunks = chunk(startDate, endDate, chunk);
		List<Headache> data = repo.getDateRange(userId, startDate, endDate);
		List<Map> in = Lists.newArrayList(Iterables.transform(data,DashboardHelpers.GET_NOTES));
		ac.accumulate(chunks,in);
		return ac;
	}

	public HeadacheAccumulator getHeadacheTypes(String userId, Date startDate,
			Date endDate, SliceType chunk) throws IOException, ParseException {

		List<Map> in = repo.getDateRangeDTO(userId, startDate, endDate);
		List<DateTime> chunks = chunk(startDate, endDate, chunk);

		HeadacheAccumulator a = new HeadacheAccumulator(TestHelpers.h,
				Accumulater.byCount);
		a.accumulate(chunks, in, new String[] { HeadacheMap.kind.toString() });
		return a;
	}

	public HeadacheAccumulator getTrueHeadacheTypes(String userId,
			Date startDate, Date endDate, SliceType chunk) throws IOException,
			ParseException {

		List<Map> in = DashboardHelpers.getRealHeadaches(repo.getDateRangeDTO(
				userId, startDate, endDate));
		List<DateTime> chunks = chunk(startDate, endDate, chunk);

		HeadacheAccumulator a = new HeadacheAccumulator(TestHelpers.h,
				Accumulater.byCount);
		a.accumulate(chunks, in, new String[] { HeadacheMap.kind.toString() });
		return a;
	}

	public List<Map<String, Object>> getDisability(String userId,
			Date startDate, Date endDate, DashboardHelpers.SliceType chunk)
			throws ParseException, IOException {
		List<Map> in = repo.getDateRangeDTO(userId, startDate, endDate);

		List<DateTime> chunks = DashboardHelpers.chunk(startDate, endDate,
				chunk);

		HeadacheAccumulator a = new HeadacheAccumulator(Accumulater.byValue,
				"completelyDisabled", "partiallyDisabled");
		a.accumulate(chunks, in, new String[] { "completelyDisabled",
				"partiallyDisabled" });
		List<Map<String, Object>> out = Lists.newArrayList();
		for (Map o : a.results) {
			double disablity = Double.parseDouble(o.get("completelyDisabled")
					.toString()) / 60;
			double partialDisability = Double.parseDouble(o.get("partiallyDisabled")
					.toString()) / 60;
//			double partialDisability = Integer.parseInt(o.get("partiallyDisabled")
//					.toString()) / 60;
			o.remove("count");
			o.put("completelyDisabled", disablity);
			o.put("partiallyDisabled", partialDisability);
			swapLabel(o, "completelyDisabled", "Completely Disabled");
			swapLabel(o, "partiallyDisabled", "Partially Disabled");
			;
			out.add(o);
		}
		return out;
	}

	public Accumulator2 getMidasScore(String userId, Date startDate,
			Date endDate, DashboardHelpers.SliceType chunk) throws IOException {

		List<Map> in = repo.getDateRangeDTO(userId, new Date(0), endDate);
		MidasAccumulator midasAccumulator = new MidasAccumulator(in);
		Accumulator2 ac = new Accumulator2(midasAccumulator, midasAccumulator,
				in);
		ac.accumulate(DashboardHelpers.chunk(startDate, endDate, chunk));

		return ac;

	}

	public HeadacheAccumulator getRawTreatments(String userId, DrugRules rule,
			Date startDate, Date endDate, SliceType chunk, Accumulater accum)
			throws ParseException {
		List<Map> in = Lists.newArrayList(Iterables.transform(
				repo.getDateRange(userId, startDate, endDate),
				GET_TREATMENT_COUNT));
		return getRawTreatments(in, rule, userId, startDate, endDate, chunk,
				accum);
	}

	public HeadacheAccumulator getRawTreatments(List<Map> in, DrugRules rule,
			String userId, Date startDate, Date endDate, SliceType chunk,
			Accumulater accum) throws ParseException {

		List<Treatment> h = HeadacheRepository.getActualSymptoms(repo.getDateRange(userId, startDate, endDate));

		Map<String, String> names = DashboardHelpers.getTreatmentNamer(rule, h);
		List<String> hid = Lists.newArrayList(Iterables.transform(h,
				RepositoryHelpers.GET_IDS));

		List<DateTime> chunks = chunk(startDate, endDate, chunk);

		HeadacheAccumulator a = new HeadacheAccumulator(names, h, accum);
		a.accumulate(chunks, in, hid.toArray(new String[hid.size()]));
		return a;
	}

	public HeadacheAccumulator getTriggers(String userId, Date startDate,
			Date endDate, SliceType chunk) throws IOException, ParseException {

		List<Map> in = Lists.newArrayList(Iterables.transform(
				repo.getDateRange(userId, startDate, endDate), GET_TRIGGERS));
		return getTriggers(in, userId, startDate, endDate, chunk);

	}

	public HeadacheAccumulator getTriggers(List<Map> in, String userId,
			Date startDate, Date endDate, SliceType chunk) throws IOException,
			ParseException {

		List<LookupDTO> h = RepositoryHelpers.enList(lookups
				.findByLookupType(Kind.TRIGGER));
		UserInformation currentUser = users.getCurrent(); // add the custom
															// triggers
		h.addAll(currentUser.getCustomTriggers());

		List<DateTime> chunks = chunk(startDate, endDate, chunk);

		HeadacheAccumulator a = new HeadacheAccumulator(h, Accumulater.byTest);
		a.accumulate(chunks, in,
				Iterables.toArray(
						Iterables.transform(h, RepositoryHelpers.GET_IDS),
						String.class));
		return a;

	}

	public HeadacheAccumulator getDisability(List<Map> in, String userId,
			Date startDate, Date endDate) throws IOException, ParseException {

		List<CouchLookup> h = lookups
				.findByLookupType(Kind.DISABILITY_QUESTION);
		
		for (CouchLookup o: h) 
			o.setDescription(o.getDescription().replace("?", ""));
			

		List<DateTime> chunks = chunk(startDate, endDate, SliceType.ALL);

		HeadacheAccumulator a = new HeadacheAccumulator(h, Accumulater.byTest);
		a.accumulate(chunks, in,
				Iterables.toArray(
						Iterables.transform(h, RepositoryHelpers.GET_IDS),
						String.class));
		return a;

	}

	public HeadacheAccumulator _getSymptoms(String userId, Date startDate,
			Date endDate, SliceType chunk, List<Map> in, List<LookupDTO> targets)
			throws IOException, ParseException {

		List<DateTime> chunks = chunk(startDate, endDate, chunk);
		HeadacheAccumulator a = new HeadacheAccumulator(targets,
				Accumulater.byTest);
		a.accumulate(chunks, in, Iterables.toArray(
				Iterables.transform(targets, RepositoryHelpers.GET_IDS),
				String.class));
		return a;
	}

	public Accumulator2 getPainTypeTable(String userId, Date startDate,
			Date endDate) throws IOException, ParseException {

		List<Map> in = Lists.newArrayList(Iterables.transform(
				repo.getDateRange(userId, startDate, endDate),
				DashboardHelpers.GET_PAINTYPE_COUNT));

		List<CouchLookup> h = lookups.findByLookupType(Kind.PAIN_TYPE);
		List<DateTime> chunks = chunk(startDate, endDate, SliceType.ALL);
		ValueAccumulator ac = new ValueAccumulator(Lists.newArrayList(Iterables
				.transform(h, RepositoryHelpers.GET_IDS)));
		Accumulator2 ac2 = new Accumulator2(ac, ac, in);

		ac2.accumulate(chunks);
		return ac2;
	}

	public PTUsageAccumulator getPTUsages(String userId, Date startDate,
			Date endDate, SliceType chunk) throws IOException {

		List<Map> active_res = ptRepo.getDateRangeDTO(userId, startDate, endDate);
		List<Map> full_res = ptRepo.getDateRangeDTO(userId, new Date(0), new Date());
		List<Map> inactive = ptRepo.getInactiveDTO(userId, startDate, endDate);
		PTUsageAccumulator a = new PTUsageAccumulator(full_res, inactive,
				active_res);
		a.accumulate(chunk(startDate, endDate, chunk), chunk);
		return a;
	}

	public PTUsageAccumulator getOneMonthPT(String userId, int year, int month)
			throws IOException {

		DateTime startDate = new DateTime(year, month, 1, 0, 0, 0, 0);
		DateTime endDate = SliceType.MONTH.roll(startDate);
		return getPTUsages(userId, startDate.toDate(), endDate.toDate(),
				SliceType.MONTH);
	}

	public static void swapLabel(Map m, String oldLabel, String newLabel) {

		Object part = m.get(oldLabel);
		m.remove(oldLabel);
		m.put(newLabel, part);

	}

}
