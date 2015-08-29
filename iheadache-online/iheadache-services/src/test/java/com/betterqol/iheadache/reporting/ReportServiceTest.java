package com.betterqol.iheadache.reporting;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.codehaus.jettison.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.BaseBigTest;
import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class ReportServiceTest extends BaseBigTest {
	
	@Autowired
	ReportService rptSvc;
	
	@Test
	public void testFirst() throws IOException, ParseException, JSONException {
		
		TestHelpers.persistSomeHeadaches(repo,"test",null,null,new GregorianCalendar(2011,7,8), new GregorianCalendar(2011,7,14),
				new GregorianCalendar(2011,7,29), new GregorianCalendar(2011,8,29));
		

		Calendar c =  new GregorianCalendar(2011,7,8);
		Calendar c2 =  new GregorianCalendar(2011,9,30);
		
		//Object results = rptSvc.getReport("test", c.getTime(), c2.getTime(), SliceType.MONTH);

		//System.out.print(results);
		
		
		
	}

}
