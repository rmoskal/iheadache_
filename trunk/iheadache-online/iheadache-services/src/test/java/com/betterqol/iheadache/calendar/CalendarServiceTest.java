package com.betterqol.iheadache.calendar;

import java.io.IOException;

import org.codehaus.jettison.json.JSONException;
import org.ektorp.CouchDbConnector;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.TreatmentRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml" })
public class CalendarServiceTest {

	@Autowired
	HeadacheRepository repo;

	@Autowired
	LookupRepository lookups;

	@Autowired
	TreatmentRepository treatments;

	@Autowired
	CalendarService serviceToTest;

	@Autowired
	@Qualifier("headacheDatabase")
	CouchDbConnector db;

	@After
	public void cleanup() {
		repo.deleteAll();
		lookups.deleteAll();
		treatments.deleteAll();
	}

	@Test
	public void testDateFormattingAAARGH() throws IOException, JSONException {
		
		//DateTime dt = new DateTime(Long.parseLong("1317661200000"));
		//assertEquals(13, dt.getHourOfDay());
		//dt = new DateTime(Long.parseLong("1317664800000"));
		//assertEquals(14, dt.getHourOfDay());
		


	}

}
