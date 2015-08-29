package com.betterqol.iheadache.accumulators;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.HeadacheMap;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.resource.DashboardResource;
import com.google.common.collect.Lists;

@SuppressWarnings("rawtypes")
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml" })
public class Accumulator2Test {

	@Autowired
	HeadacheRepository repo;

	@Autowired
	DashboardResource svc;

	@After
	public void cleanup() {
		repo.deleteAll();
	}

	@Test
	public void testChunking() throws IOException {

		TestHelpers.persistSomeHeadaches(repo, new DateTime(2011, 12, 30, 0, 0,
				0, 0), new DateTime(2012, 1, 1, 0, 0, 0, 0), new DateTime(2012,
				1, 1, 0, 0, 0, 0), new DateTime(2012, 1, 3, 0, 0, 0, 0));

		List<Map> data = repo.getDateRangeDTO("foo", new GregorianCalendar(
				2011, 11, 30).getTime(), new GregorianCalendar(2012, 0, 30)
				.getTime());
		assertEquals(4, data.size());

		Accumulator2 o = new Accumulator2(null, null, data);
		List<Map> res = Accumulator2.getForRange(o.contents, new DateMidnight(
				2012, 1, 1), new DateMidnight(2012, 1, 1));
		assertEquals(2, res.size());

	}

	@Test
	public void testChunking2() throws IOException {

		TestHelpers.persistSomeHeadaches(repo, new DateTime(2010, 12, 30, 0, 0,
				0, 0), new DateTime(2012, 1, 2, 0, 0, 0, 0), new DateTime(2012,
				2, 1, 0, 0, 0, 0), new DateTime(2012, 3, 1, 0, 0, 0, 0));

		List<Map> data = repo.getDateRangeDTO("foo", new GregorianCalendar(
				2012, 0, 1).getTime(), new GregorianCalendar(2012, 4, 1)
				.getTime());
		assertEquals(3, data.size());

		Accumulator2 o = new Accumulator2(null, null, data);
		List<Map> res = Accumulator2.getForRange(o.contents, new DateMidnight(
				2012, 1, 1), new DateMidnight(2012, 3, 1));
		assertEquals(3, res.size());

	}
	
	@Test
	public void testValueAccumulator() throws IOException {

		TestHelpers.persistSomeHeadaches(repo, new DateTime(2011, 12, 30, 0, 0,
				0, 0), new DateTime(2012, 1, 1, 0, 0, 0, 0), new DateTime(2012,
				1, 1, 0, 0, 0, 0), new DateTime(2012, 1, 3, 0, 0, 0, 0));

		List<Map> data = repo.getDateRangeDTO("foo", new GregorianCalendar(
				2011, 11, 30).getTime(), new GregorianCalendar(2012, 0, 30)
				.getTime());
		assertEquals(4, data.size());

		ValueAccumulator ac = new ValueAccumulator(
				Lists.newArrayList(HeadacheMap.midas.toString()));

		Accumulator2 ac2 = new Accumulator2(ac, ac, data);
		ac2.accumulate(Lists.newArrayList(new DateTime(2012, 1, 1, 0, 0, 0, 0)));
		assertEquals(1, ac2.getResults().size());
		assertEquals(3, ac2.getResults().get(0).get("count"));
		assertEquals(6,
				ac2.getResults().get(0).get(HeadacheMap.midas.toString()));
	}

	@Test
	public void testMidasAccumulatorNone() throws IOException {

		TestHelpers.persistSomeHeadaches(repo, new DateTime(2011, 12, 30, 0, 0,
				0, 0), new DateTime(2012, 1, 1, 0, 0, 0, 0), new DateTime(2012,
				1, 1, 0, 0, 0, 0), new DateTime(2012, 1, 3, 0, 0, 0, 0));

		List<Map> data = repo.getDateRangeDTO("foo", new GregorianCalendar(
				2011, 11, 30).getTime(), new GregorianCalendar(2012, 0, 30)
				.getTime());
		assertEquals(4, data.size());

		ValueAccumulator ac = new MidasAccumulator(data);

		Accumulator2 ac2 = new Accumulator2(ac, ac, data);
		ac2.accumulate(Lists.newArrayList(new DateTime(2012, 1, 1, 0, 0, 0, 0)));
		assertEquals(1, ac2.getResults().size());
		assertEquals(3, ac2.getResults().get(0).get("count"));
		assertEquals(0,
				ac2.getResults().get(0).get(HeadacheMap.midas.toString()));
		assertEquals(-100, ac2.getResults().get(0).get("Actual"));
		assertEquals(-100, ac2.getResults().get(0).get("Estimated"));
	}

	@Test
	public void testMidasAccumulatorEstimated() throws IOException {

		TestHelpers.persistManyHeadaches(repo, new DateMidnight(2012, 11, 01),
				new DateMidnight(2012, 12, 15));

		List<Map> data = repo.getDateRangeDTO("foo", new GregorianCalendar(
				2012, 10, 1).getTime(), new GregorianCalendar(2012, 11, 30)
				.getTime());
		assertEquals(44, data.size());

		ValueAccumulator ac = new MidasAccumulator(data);

		Accumulator2 ac2 = new Accumulator2(ac, ac, data);
		ac2.accumulate(Lists
				.newArrayList(new DateTime(2012, 12, 30, 0, 0, 0, 0)));
		assertEquals(1, ac2.getResults().size());
		assertEquals(44, ac2.getResults().get(0).get("count"));
		assertEquals(134,
				ac2.getResults().get(0).get(HeadacheMap.midas.toString()));
		assertEquals(134, ac2.getResults().get(0).get("Estimated"));
		assertEquals(-100, ac2.getResults().get(0).get("Actual"));
	}

	@Test
	public void testMidasAccumulator_regress_month() throws IOException {
		List<Map> res = testMidasAccumulator_regress(SliceType.MONTH,
				new DateTime(2012, 3, 1, 0, 0, 0, 0), new DateTime(2012, 1, 1,
						0, 0, 0, 0));
		assertEquals(3, res.size());
		assertEquals(1, res.get(0).get("count"));
		assertEquals(2, res.get(0).get(HeadacheMap.midas.toString()));
		assertEquals(2, res.get(0).get("Actual"));
		assertEquals(4, res.get(1).get(HeadacheMap.midas.toString()));
		assertEquals(4, res.get(1).get("Actual"));
		assertEquals(6, res.get(2).get(HeadacheMap.midas.toString()));
		assertEquals(6, res.get(2).get("Actual"));
	}

	@Test
	public void testMidasAccumulator_regress_month_less_1() throws IOException {
		List<Map> res = testMidasAccumulator_regress(SliceType.MONTH,
				new DateTime(2012, 3, 1, 0, 0, 0, 0), new DateTime(2012, 2, 1,
						0, 0, 0, 0));
		assertEquals(2, res.size());
		assertEquals(2, res.get(0).get("count"));
		assertEquals(4, res.get(0).get(HeadacheMap.midas.toString()));
		assertEquals(4, res.get(0).get("Actual"));
		assertEquals(6, res.get(1).get(HeadacheMap.midas.toString()));
		assertEquals(6, res.get(1).get("Actual"));

	}

	@Test
	public void testMidasAccumulator_regress_30() throws IOException {
		List<Map> res = testMidasAccumulator_regress(SliceType._30,
				new DateTime(2012, 3, 1, 0, 0, 0, 0), new DateTime(2012, 1, 1,
						0, 0, 0, 0));
		assertEquals(3, res.size());
		assertEquals(1, res.get(0).get("count"));
		assertEquals(2, res.get(0).get(HeadacheMap.midas.toString()));
		assertEquals(2, res.get(0).get("Actual"));
		assertEquals(2, res.get(1).get(HeadacheMap.midas.toString()));
		assertEquals(2, res.get(1).get("Actual"));
		assertEquals(6, res.get(2).get(HeadacheMap.midas.toString()));
		assertEquals(6, res.get(2).get("Actual"));
	}

	@Test
	public void testMidasAccumulator_regress_28() throws IOException {
		List<Map> res = testMidasAccumulator_regress(SliceType._28,
				new DateTime(2012, 3, 1, 0, 0, 0, 0), new DateTime(2012, 1, 1,
						0, 0, 0, 0));
		assertEquals(3, res.size());
		assertEquals(3, res.size());
		assertEquals(1, res.get(0).get("count"));
		assertEquals(2, res.get(0).get(HeadacheMap.midas.toString()));
		assertEquals(2, res.get(0).get("Actual"));
		assertEquals(4, res.get(1).get(HeadacheMap.midas.toString()));
		assertEquals(4, res.get(1).get("Actual"));
		assertEquals(6, res.get(2).get(HeadacheMap.midas.toString()));
		assertEquals(6, res.get(2).get("Actual"));
	}

	@Test
	public void testMidasAccumulator_regress_week() throws IOException {
		List<Map> res = testMidasAccumulator_regress(SliceType._7,
				new DateTime(2012, 3, 1, 0, 0, 0, 0), new DateTime(2012, 1, 1,
						0, 0, 0, 0));
		assertEquals(9, res.size());
		assertEquals(1, res.get(0).get("count"));
		assertEquals(2, res.get(0).get(HeadacheMap.midas.toString()));
		assertEquals(2, res.get(0).get("Actual"));
		assertEquals(2, res.get(3).get(HeadacheMap.midas.toString()));
		assertEquals(2, res.get(3).get("Actual"));
		assertEquals(4, res.get(6).get(HeadacheMap.midas.toString()));
		assertEquals(4, res.get(6).get("Actual"));
		assertEquals(6, res.get(8).get(HeadacheMap.midas.toString()));
		assertEquals(6, res.get(8).get("Actual"));
	}

	public List<Map> testMidasAccumulator_regress(SliceType sliceType,
			DateTime endDate, DateTime startDate) throws IOException {

		TestHelpers.persistSomeHeadaches(repo, new DateTime(2010, 12, 30, 0, 0,
				0, 0), new DateTime(2012, 1, 1, 0, 0, 0, 0), new DateTime(2012,
				2, 1, 0, 0, 0, 0), new DateTime(2012, 3, 1, 0, 0, 0, 0));

		List<Map> data = repo.getDateRangeDTO("foo", new Date(0),
				endDate.toDate());

		ValueAccumulator ac = new MidasAccumulator(data);

		Accumulator2 ac2 = new Accumulator2(ac, ac, data);
		ac2.accumulate(DashboardHelpers.chunk(startDate, endDate, sliceType));
		return ac2.getResults();

	}

	@Test
	public void testMidasAccumulatorActual() throws IOException {

		TestHelpers.persistManyHeadaches(repo, new DateMidnight(2011, 10, 1),
				new DateMidnight(2012, 1, 30));

		List<Map> data = repo.getDateRangeDTO("foo", new GregorianCalendar(
				2011, 10, 1).getTime(), new GregorianCalendar(2012, 0, 30)
				.getTime());
		assertEquals(90, data.size());

		ValueAccumulator ac = new MidasAccumulator(data);

		Accumulator2 ac2 = new Accumulator2(ac, ac, data);
		ac2.accumulate(Lists
				.newArrayList(new DateTime(2012, 1, 30, 0, 0, 0, 0)));
		assertEquals(1, ac2.getResults().size());
		assertEquals(90, ac2.getResults().get(0).get("count"));
		assertEquals(180,
				ac2.getResults().get(0).get(HeadacheMap.midas.toString()));
		assertEquals(-100, ac2.getResults().get(0).get("Estimated"));
		assertEquals(180, ac2.getResults().get(0).get("Actual"));
	}

	@Test
	public void testMidasAccumulatorRegressOneYear() throws IOException {

		TestHelpers.persistManyHeadaches(repo, new DateMidnight(2011, 3, 1),
				new DateMidnight(2012, 1, 30));

		List<Map> data = repo.getDateRangeDTO("foo", new GregorianCalendar(
				2011, 0, 1).getTime(), new GregorianCalendar(2012, 0, 30)
				.getTime());
		assertEquals(335, data.size());

		ValueAccumulator ac = new MidasAccumulator(data);

		Accumulator2 ac2 = new Accumulator2(ac, ac, data);
		ac2.accumulate(DashboardHelpers.chunk(
				new GregorianCalendar(2011, 0, 1), new GregorianCalendar(2012,
						0, 30), SliceType.MONTH));
		assertEquals(13, ac2.getResults().size());
		assertEquals(0, ac2.getResults().get(0).get("count"));
		assertEquals(31,
				((DateTime) ac2.getResults().get(0).get("axis")).getDayOfYear());
		assertEquals(0,
				ac2.getResults().get(0).get(HeadacheMap.midas.toString()));
		assertEquals(-100, ac2.getResults().get(0).get("Estimated"));
		assertEquals(-100, ac2.getResults().get(0).get("Actual"));
	}

}
