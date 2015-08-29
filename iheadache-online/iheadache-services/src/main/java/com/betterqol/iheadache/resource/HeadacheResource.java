package com.betterqol.iheadache.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.betterqol.iheadache.model.Headache;


@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface HeadacheResource  {
	
	public static final String URL = "/app/service/headache";
	
	@GET
	@Path("test")
	String test ();
	
	@GET
	@Path("prototype")
	Headache getPrototype();
	
	@POST
    @Consumes("application/vnd.headache+json")
	String createForApp (Headache o);
	
	@GET
	@Path("{pk}")
    @Produces("application/vnd.headache+json")
	Headache getForApp(@PathParam("pk")String pk);
	
	@DELETE
	@Path("{pk}")
	void deleteForApp (@PathParam("pk")String pk);
	
	@PUT
	@Path("update")
	@Consumes("application/vnd.headache+json")
	void updateForApp (Headache o);
	
	

}
