package com.betterqol.iheadache.extensions;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import junit.framework.Assert;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.test.BaseResourceTest;
import org.jboss.resteasy.test.TestPortProvider;
import org.junit.BeforeClass;
import org.junit.Test;

public class DateFormatterTest extends BaseResourceTest {
	
	@Path("/datetest")
	   public static class Service
	   {
	      @GET
	      @Produces("text/plain")
	      @Path("/{date}")
	      public String get(@PathParam("date") @DateFormat("yyyy-MM-dd") Date date)
	      {
	         System.out.println(date);
	         Calendar c = GregorianCalendar.getInstance();
	         c.setTime(date);
	         Assert.assertEquals(7, c.get(Calendar.MONTH));
	         Assert.assertEquals(29, c.get(Calendar.DAY_OF_MONTH));
	         Assert.assertEquals(2011, c.get(Calendar.YEAR));
	         return date.toString();
	      }
	   }

	   @BeforeClass
	   public static void setup() throws Exception
	   {
	      addPerRequestResource(Service.class);
	   }

	   @Test
	   public void testMe() throws Exception
	   {
	      ClientRequest request = new ClientRequest(TestPortProvider.generateURL("/datetest/2011-8-29"));
	      System.out.println(request.getTarget(String.class));
	   }
	   


}
