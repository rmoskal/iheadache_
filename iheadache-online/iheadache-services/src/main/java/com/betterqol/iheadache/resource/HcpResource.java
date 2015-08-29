package com.betterqol.iheadache.resource;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface HcpResource {
	
	@GET
	@Path("currentPatient")
	String getCurrentPatient() throws JSONException;
	
	@GET
	@Path("associations")
	String getAssociations() throws JsonGenerationException,
			JsonMappingException, JSONException, IOException;

	@PUT
	@Path("switch/{user}")
	String switchUser(@PathParam("user")String userId);
	
}
