package com.betterqol.iheadache.resource;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;

import com.betterqol.iheadache.dashboard.DashboardHelpers.SliceType;
import com.betterqol.iheadache.extensions.DateFormat;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface ReportResource {
	
	public static final String URL = "/app/service/report";
	
	@GET
	String getReport(@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd")Date endDate, 
			@QueryParam("chunk")SliceType chunk) throws IOException, ParseException, JSONException ;
	

}
