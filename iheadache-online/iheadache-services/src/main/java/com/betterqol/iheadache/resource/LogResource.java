package com.betterqol.iheadache.resource;

import java.util.Date;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;

import com.betterqol.iheadache.extensions.DateFormat;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface LogResource {
	
	@GET
	@Path("paged")
	Map getLog(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd") Date startDate,
			@QueryParam("endDate") @DateFormat("yyyy-MM-dd") Date endDate,
			@QueryParam("pageLink") String pageLink,
			@QueryParam("criteria")int[]  criteria,
			@QueryParam("types")int[] headacheTypes) throws JSONException;

}