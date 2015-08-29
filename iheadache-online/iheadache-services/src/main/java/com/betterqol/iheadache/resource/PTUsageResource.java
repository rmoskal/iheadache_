package com.betterqol.iheadache.resource;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.betterqol.iheadache.extensions.DateFormat;
import com.betterqol.iheadache.model.PTUsage;



@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed(value = { "ROLE_USER" })
public interface PTUsageResource {
	
	public static final String URL = "/app/service/ptusage";
	
	@POST
	@Path("/")
	void add(PTUsage o);
	
	@POST
	@Path("/active")
	PTUsage add2(PTUsage o);
	
	
	@GET
	@Path("/active")
	List<PTUsage> getActiveandFuture();
	
	
	@PUT
	@Path("/active/{id}")
	PTUsage update2(PTUsage item);
	
	@DELETE
	@Path("/active/{id}")
	PTUsage deletePTUsage(@PathParam("id")String id);
	
	
	@GET
	@Path("/inactive")
	List<PTUsage> getInactive();
	
	@DELETE
	@Path("/inactive/{id}")
	PTUsage deletePTUsage2(@PathParam("id")String id);
	
	@PUT
	@Path("/inactive/{id}")
	PTUsage reactivate(PTUsage item);
	
	@GET
	@Path("/all")
	 List<PTUsage>  getDateRange(
			@QueryParam("startDate") @DateFormat("yyyy-MM-dd")Date startDate, 
			@QueryParam("endDate")  @DateFormat("yyyy-MM-dd") Date endDate);

}
