package com.betterqol.iheadache.resource;

import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface AdminResource {
	
	@GET
	@Path("users")
	Map getUsers(@QueryParam("pageLink") String pageLink);
	
	@GET
	@Path("hcp-users")
	Map getHcpUsers(@QueryParam("pageLink") String pageLink);
	
	
	@GET
	@Path("hcp-unapproved")
	Map getUnApprovedHcpUsers(@QueryParam("pageLink") String pageLink);

}
