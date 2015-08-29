package com.betterqol.iheadache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.ektorp.CouchDbConnector;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.betterqol.iheadache.dashboard.DashboardHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.dashboard.HeadacheAccumulator;
import com.betterqol.iheadache.dashboard.HeadacheAccumulator.Accumulater;
import com.betterqol.iheadache.model.CouchLookup;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.Disability;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.HeadachePain;
import com.betterqol.iheadache.model.HeadacheTreatment;
import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.PTUsage;
import com.betterqol.iheadache.model.PreventativeTreatment;
import com.betterqol.iheadache.model.Treatment;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.model.YesNoResponse;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.PTRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.betterqol.iheadache.repository.TreatmentRepository;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public abstract class TestHelpers {

	public static Headache createTest1Headache(Date date, String userId, LookupRepository lookup) {
		Random randomGenerator = new Random();
		Headache o = new Headache();
		o.setStart(new DateTime(date));
		o.setEnd(new DateTime(date).plusHours(3));
		o.setKind(new LookupDTO("UNCLASSIFIED_HEADACHE", "Unclassified Headache"));
		o.setUser(userId);

		o.setPains(Lists.newArrayList(new HeadachePain(5, new DateTime(date),
				"THROBBING", "PAIN_LOCATION_RIGHT_SIDE_OF_H",
				"PAIN_LOCATION_LEFT_TEMPLE"), new HeadachePain(5, new DateTime(date),
				"THROBBING", "PAIN_LOCATION_LEFT_TEMPLE",
				"PAIN_LOCATION_RIGHT_SIDE_OF_H")));
		
		o.setSymptoms(Lists.newArrayList(
				new YesNoResponse("HAS_AURA", false, true),
				new YesNoResponse("IS_ANILATERAL", false, true), 
				new YesNoResponse("IS_MOVEMENT", true, false), 
				new YesNoResponse( "IS_THROBBING", true, false),
				new YesNoResponse("HAS_NAUSEA", true, false),
				new YesNoResponse("HAS_PHOTOPHOBIA", false, true),
				new YesNoResponse("HAS_PHONOBIA", false, true),
				new YesNoResponse("IS_NECK", false, true),
				new YesNoResponse("IS_SINUS", false, true),
				new YesNoResponse("HAS_OLFACTOPHOBIA", false, true)
				));

//		for (YesNoResponse res : o.getSymptoms()) {
//			boolean selected = randomGenerator.nextBoolean();
//			if (selected)
//				res.setYes(true);
//			else
//				res.setNo(true);
//		}

		Disability d = new Disability();
		d.setCompletelyDisabled(randomGenerator.nextInt(8));
		d.setPartiallyDisabled(randomGenerator.nextInt(12));
		d.setResponses(Lists.newArrayList(
				new YesNoResponse("DISABILITY_QUESTION_1", true, false), 
				new YesNoResponse("DISABILITY_QUESTION_2", true, false), 
				new YesNoResponse("DISABILITY_QUESTION_3", true, false), 
				new YesNoResponse("DISABILITY_QUESTION_4", true, false), 
				new YesNoResponse("DISABILITY_QUESTION_5", false, true)));

		o.setNote("Test 1");

		o.setTreatments(Lists.newArrayList(new HeadacheTreatment(
				new DateTime(date), new Treatment("STND_TR_10")) 
				));		

		o.setMIDAS(randomGenerator.nextInt(90) + 10);




		o.setDisability(d);
		return o;
	}

	public static void persistSomeHeadaches(HeadacheRepository repo, String userId, LookupRepository lookup) {
		Calendar c =  new GregorianCalendar(2012,2,5);
		repo.create(createTest1Headache(c.getTime(), userId, lookup));
		c.add(Calendar.DAY_OF_MONTH, 1);
		repo.create(createTest1Headache(c.getTime(), userId, lookup));
		c.add(Calendar.DAY_OF_MONTH, 1);
		repo.create(createTest1Headache(c.getTime(), userId, lookup));
	}
	

	
	public static Headache createMinHeadache(Date date, String userId) {
		Random randomGenerator = new Random();
		Headache o = new Headache();
		o.setStart(new DateTime(date));
		o.setEnd(new DateTime(date).plusHours(4));
		o.setKind(new LookupDTO("one", "one"));
		o.setMIDAS(randomGenerator.nextInt(90) + 10);
		o.setUser(userId);
		Disability d = new Disability();
		d.setCompletelyDisabled(randomGenerator.nextInt(8));
		d.setPartiallyDisabled(randomGenerator.nextInt(12));
		d.setResponses(Lists.newArrayList(new YesNoResponse(
				"DISABILITY_QUESTION_1", true, false), new YesNoResponse(
				"DISABILITY_QUESTION_1", false, true)));

		o.setPains(Lists.newArrayList(new HeadachePain(10, new DateTime(date),
				"THROBBING", "PAIN_LOCATION_RIGHT_SIDE_OF_H",
				"PAIN_LOCATION_LEFT_TEMPLE"), new HeadachePain(10, new DateTime(date),
				"THROBBING", "PAIN_LOCATION_LEFT_TEMPLE",
				"PAIN_LOCATION_RIGHT_SIDE_OF_H")));
		
		o.setSymptoms(Lists.newArrayList(
				new YesNoResponse( "IS_THROBBING", true, false)
				));

		o.setDisability(d);
		return o;
	}

	public static Headache createFullHeadache(boolean isRandom, String user,
			LookupRepository lookup, TreatmentRepository treatmments) {

		return createFullHeadache(new GregorianCalendar(2011, 8, 10), false,
				"test", lookup, null);
	}

	public static Headache createFullHeadache(Calendar c, boolean isRandom,
			String user, LookupRepository lookup, TreatmentRepository treatments) {

		String[] headacheTypes = { "TENSION_HEADACHE", "PROBABLE_MIGRAINE",
				"MIGRAINE", "UNCLASSIFIED_HEADACHE" };
		String[] triggers = { "TRIGGER_01", "TRIGGER_02", "TRIGGER_03",
				"TRIGGER_04" };
		Integer[] minutes = { 0, 15, 30, 45 };

		Headache o = new Headache();
		Random randomGenerator = new Random();
		DateTime startTime = new DateTime(c.getTime())
				.plusHours(randomGenerator.nextInt(19));
		o.setStart(startTime);
		o.setEnd(startTime.plusHours(randomGenerator.nextInt(4)));
		o.setMIDAS(randomGenerator.nextInt(3) + 1);
		o.setUser(user);

		o.setTreatments(Lists.newArrayList(new HeadacheTreatment(
				new DateTime(c), new Treatment("one")), new HeadacheTreatment(
				new DateTime(c), new Treatment("two"))));

		if (isRandom)
			o.setTreatments(buildTreatmentList(startTime.plusHours(1),
					randomGenerator));

		o.setTriggers(Lists.newArrayList(new LookupDTO("TRIGGER_ONE",
				"TRIGGER_ONE"), new LookupDTO("TRIGGER_TWO", "TRIGGER_TWO")));
		if (isRandom) {

			o.getTriggers().clear();
			for (int i = 0; i <= randomGenerator.nextInt(3); i++)
				o.getTriggers().add(new LookupDTO(triggers[i], triggers[i]));
		}

		o.setPains(Lists.newArrayList(new HeadachePain(10, new DateTime(c),
				"THROBBING", "HEAD_RIGHT",
				"TEMPLE_LEFT"), new HeadachePain(10, new DateTime(c),
				"THROBBING", "TEMPLE_LEFT",
				"HEAD_RIGHT")));
		o.setSymptoms(Lists.newArrayList(new YesNoResponse(
				"IS_MOVEMENT", true, false), new YesNoResponse(
				"IS_ANILATERAL", true, false), new YesNoResponse(
				"HAS_AURA", false, true)));

		Disability d = new Disability();
		d.setCompletelyDisabled(randomGenerator.nextInt(8) * 60
				+ minutes[randomGenerator.nextInt(3)]);
		d.setPartiallyDisabled(randomGenerator.nextInt(8) * 60
				+ minutes[randomGenerator.nextInt(3)]);
		d.setResponses(Lists.newArrayList(
				new YesNoResponse("DISABILITY_QUESTION_1", true, false), 
				new YesNoResponse("DISABILITY_QUESTION_2", false, true)
				));

		o.setDisability(d);
		String hId = headacheTypes[randomGenerator.nextInt(3)];
		o.setKind(new LookupDTO(hId, hId));
		o.setNote("Adding some note text just for fun.");
		if (lookup != null) {
			o.setSymptoms(Lists.newArrayList(Iterables.transform(
					lookup.findByLookupType(Kind.SYMPTOM),
					RepositoryHelpers.MAKE_QUESTION)));
			for (YesNoResponse res : o.getSymptoms()) {
				boolean selected = randomGenerator.nextBoolean();
				if (selected)
					res.setYes(true);
				else
					res.setNo(true);
			}
			d.setResponses(Lists.newArrayList(Iterables.transform(
					lookup.findByLookupType(Kind.DISABILITY_QUESTION),
					RepositoryHelpers.MAKE_QUESTION)));
			for (YesNoResponse res : d.getResponses()) {
				boolean selected = randomGenerator.nextBoolean();
				if (selected)
					res.setYes(true);
				else
					res.setNo(true);
			}

			for (HeadacheTreatment t : o.getTreatments())
				t.setTreatment(treatments.get(t.getTreatment().getId()));

			o.getKind().setDescription(
					lookup.get(o.getKind().getId()).getDescription());

			for (LookupDTO t : o.getTriggers())
				t.setDescription(lookup.get(t.getId()).getDescription());

			for (HeadachePain p : o.getPains()) {
				for (LookupDTO t : p.getPainLocation())
					t.setDescription(lookup.get(t.getId()).getDescription());
				for (LookupDTO t : p.getPainType())
					t.setDescription(lookup.get(t.getId()).getDescription());
			}

		}

		return o;

	}

	public static List<HeadacheTreatment> buildTreatmentList(DateTime c,
			Random randomGenerator) {

		List<String> treatments = Lists.newArrayList("STND_TR_ADVIL",
				"STND_TR_ALEVE", "STND_TR_AMERGE_1_MG",
				"STND_TR_AMERGE_2_5_MG", "STND_TR_ASPIRIN",
				"STND_TR_AXERT", "STND_TR_CAMBIA",
				"STND_TR_CAMBIA", "STND_TR_DHE-45");
		List<HeadacheTreatment> results = new ArrayList<HeadacheTreatment>();
		int i = randomGenerator.nextInt(8);
		results.add(new HeadacheTreatment(c, new Treatment(treatments.get(i))));

		if (i < 3)
			results.add(new HeadacheTreatment(c.minusHours(2), new Treatment(
					treatments.get(i + 1))));

		return results;

	}
	


	public static void persistSomeHeadaches(HeadacheRepository repo,
			String user, LookupRepository lookup,
			TreatmentRepository treatments, Calendar... calendars) {
		for (Calendar c : calendars) {
			repo.create(createFullHeadache(c, true, user,
					lookup, treatments));
			repo.create(createFullHeadache(c, true, user,
					lookup, treatments));
			c.roll(Calendar.DAY_OF_MONTH, 5);
			repo.create(createFullHeadache(c, true, user, lookup, treatments));
			c.roll(Calendar.WEEK_OF_YEAR, 2);
			repo.create(createFullHeadache(c, true, user, lookup, treatments));

		}
	}

	public static List<CouchLookup> makeSomeLookups(CouchLookup.Kind kind) {
		return Lists.newArrayList(new CouchLookup(kind, "one thing", "one"),
				new CouchLookup(kind, "Second thing", "two"), new CouchLookup(
						kind, "Third Thing", "three"), new CouchLookup(kind,
						"Fourth Thing", "four"));

	}

	public static void persistSomePTUsages(PTRepository repo,  String id,
			Calendar start) {

		String[] items = { "Topomax 100mg per day", "Topomax 150mg per day",
				"Topomax 200mg per day", "Topomax 100mg per day",
				"Topomax 200mg per day", "Xanthan Gum 500mg per day",
				"Cymbaline 500mg per day", "Xanthan Gum 1000mg per day" };

		// String[] treats = {"one", "two", "three",
		// "two", "one", "four", "five", "one"};

		Random randomGenerator = new Random();
		Calendar current = (Calendar) start.clone();
		current.add(Calendar.DATE, randomGenerator.nextInt(15));

		for (String item : items) {
			addPTUsages(repo, id, item, start, randomGenerator);
		}

	}

	public static void addPTUsages(PTRepository repo, String id, String name,
			Calendar start, Random randomGenerator) {
		Date s = start.getTime();
		PTUsage p = null;
		if (randomGenerator != null) {
			start.add(Calendar.DATE, randomGenerator.nextInt(40) + 5);
			p = new PTUsage(id, s, start.getTime(), "I was tired", "50", name, name+ "_generic");
		} else
			p = new PTUsage(id, s,  "50", new PreventativeTreatment(name, name+ "_generic"));
		repo.add(p);

	}
	
	public static void addPTUsages(PTRepository repo, String id, String name,Calendar start, int days) {
		Date s = start.getTime();
		start.add(Calendar.DATE, days);
		PTUsage p = new PTUsage(id, s, start.getTime(), "I was tired", "50", name, name+ "_generic");
		repo.add(p);
	}
	
	

	public static UserInformation makeUser(String uName) {

		UserInformation o = new UserInformation();
		o.setName(uName);
		//o.setPassword(uName);
		o.setTriggerProfile(Lists.newArrayList(new LookupDTO("TRIGGER_01",
				"TRIGGER_01"), new LookupDTO("TRIGGER_02", "TRIGGER_02"), new LookupDTO("XXX",
						"Flourescent Light")));
		o.setCustomTriggers(Lists.newArrayList(new LookupDTO("XXX",
				"Flourescent Light"), new LookupDTO("XX", "Slim Jims")));
		o.setCustomTreatments(Lists.newArrayList(new Treatment(UUID
				.randomUUID().toString(), "Test treatment", "generic","Pill", "200 mg",
				true), new Treatment(UUID.randomUUID().toString(), "Zombisol","Undead tablets",
				"Injection", "2000 cc", true)));
		o.setTreatmentProfile(Lists.newArrayList(new Treatment(
				"STND_TR_CAMBIA"), new Treatment("STND_TR_ASPIRIN"),
				new Treatment("STND_TR_AMERGE_1_MG"), new Treatment(
						"STND_TR_ADVIL")));
		//o.setCustomTriggers(Lists.newArrayList(new LookupDTO("smoke", "smoke"), new LookupDTO("trees", "trees")));
		o.getTreatmentProfile().addAll(o.getCustomTreatments());
		o.setActiveCustomSymptoms(Lists.newArrayList(new LookupDTO("yyy",
				"Butterflies in stomach")));
		o.setCustomSymptoms(Lists.newArrayList(new LookupDTO("yyy",
				"Butterflies in stomach"), new LookupDTO("yy", "Palipitations")));
		
		o.setUserAssociations(Lists.newArrayList("JohnSmith"));
		return o;

	}

	public static UserInformation simpleMakeUser(String name) {

		UserInformation o = new UserInformation();
		o.setName(name);
		return o;

	}
	
	public static UserPrincipal makePrincipal(String name) {

		UserPrincipal o = new UserPrincipal();
		o.setName(name);
		o.setAddress(name);
		PasswordEncoder encoder = new Md5PasswordEncoder();
		o.setPassword(encoder.encodePassword(name,null));

		return o;

	}
	
	public static HealthcarePrincipal makeHcPrincipal(String name, boolean approved, String last) {

		HealthcarePrincipal o = new HealthcarePrincipal();
		o.setName(StringUtils.deleteAny((name+last), " ").toLowerCase());
		o.setFirstName(name);
		o.setLastName(last);
		o.setId(UUID.randomUUID().toString());
		PasswordEncoder encoder = new Md5PasswordEncoder();
		o.setPassword(encoder.encodePassword(StringUtils.deleteAny(name, " ").toLowerCase(),null));
		o.setInstitution(last + " Hospital");
		o.setApproved(approved);
		o.setState("NY");
		o.setAddress1("20 Central Ave.");
		o.setZipcode("11542");

		return o;

	}

	public static HeadacheAccumulator contructAcuteTreatment(
			ArrayList<Treatment> h, GregorianCalendar start,
			GregorianCalendar end, GregorianCalendar... createDates)
			throws ParseException {
		List<Map> data = new ArrayList<Map>();

		for (GregorianCalendar c : createDates)
			data.add(DashboardHelpers.GET_TREATMENTS.apply(createFullHeadache(
					c, false, "test", null, null)));

		List<DateTime> chunks = DashboardHelpers.chunk(start, end,
				SliceType.MONTH);

		HeadacheAccumulator a = new HeadacheAccumulator(h, Accumulater.byTest);
		a.accumulate(chunks, data, new String[] { "one", "two", "three",
				"four", "five" });
		return a;
	}
	
	public static HeadacheAccumulator contructAcuteTreatmentNovel(
			ArrayList<Treatment> h, GregorianCalendar start,
			GregorianCalendar end, GregorianCalendar... createDates)
			throws ParseException {
		List<Map> data = new ArrayList<Map>();

		for (GregorianCalendar c : createDates){
			Headache hh = createFullHeadache(
					c, false, "test", null, null);
			hh.setTreatments(Lists.newArrayList(new HeadacheTreatment(
					new DateTime(c), new Treatment("unknown")), new HeadacheTreatment(
					new DateTime(c), new Treatment("unkown"))));
			data.add(DashboardHelpers.GET_TREATMENTS.apply(hh));
		};

		List<DateTime> chunks = DashboardHelpers.chunk(start, end,
				SliceType.MONTH);

		HeadacheAccumulator a = new HeadacheAccumulator(h, Accumulater.byTest);
		a.accumulate(chunks, data, new String[] { "one", "two", "three",
				"four", "five" });
		return a;
	}

	public static final ArrayList<CouchLookup> h = Lists.newArrayList(
			new CouchLookup(Kind.HEADACHE_TYPE,"Migraine", "MIGRAINE"),
			new CouchLookup(Kind.HEADACHE_TYPE, "Probable Migraine","PROBABLE_MIGRAINE"),
			new CouchLookup(Kind.HEADACHE_TYPE, "Tension Headache", "TENSION_HEADACHE"),
			new CouchLookup(Kind.HEADACHE_TYPE, "Unclassified Headache","UNCLASSIFIED_HEADACHE"),
			new CouchLookup(Kind.HEADACHE_TYPE, "No Headache","NO_HEADACHE"));

	public static final Map<String, String> hm = RepositoryHelpers.enMap(h);

	public static void createLookups(CouchDbConnector db)  {
		 Resource r = new ClassPathResource("init_lookup_data_production.json");
		 IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		 loader.setDataPath(r);
		 
		 try {
			loader.init();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void persistSomeHeadaches(HeadacheRepository repo, DateTime... dates) {
		int i = 0;
		for (DateTime dt: dates) {
			Headache o = createMinHeadache(dt.toDate(), "foo");
			o.setKind(new LookupDTO("MIGRAINE","MIGRAINE"));
			o.setMIDAS(2);
			o.setNote("This is a note " + i);
			i++;
			repo.create(o);
		}
	}
	
	public static void persistManyHeadaches(HeadacheRepository repo, DateMidnight start, DateMidnight stop) {
		while (start.isBefore(stop)){
			Headache o = createMinHeadache(start.toDate(), "foo");
			o.setKind(new LookupDTO("MIGRAINE","MIGRAINE"));
			o.setMIDAS(2);
			repo.create(o);
			start = start.plusDays(1);
			
		}

	}
}
