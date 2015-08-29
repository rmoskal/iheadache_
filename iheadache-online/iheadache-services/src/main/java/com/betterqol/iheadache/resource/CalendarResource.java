package com.betterqol.iheadache.resource;

import java.io.IOException;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.codehaus.jettison.json.JSONException;

import com.betterqol.iheadache.extensions.DateFormat;


public interface CalendarResource {
	
	
	@GET
	@Path("test")
	String test ();
	
	@GET
	@Path("big")
	String getCalendarEntry( 
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate) throws IOException, JSONException;
	
	@GET
	@Path("ptreatments")
	String getPTreatmentEntry( 
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate) throws IOException, JSONException;
	
	@GET
	@Path("nav")
	String getNavCalendar( 
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate) throws IOException, JSONException;
	
	

}
