package com.betterqol.iheadache.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.betterqol.iheadache.model.Treatment;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface TreatmentResource {
	
	public static final String URL = "/app/service/treatment";
	
	@GET
	@Path("test")
	String test ();
	
	@GET
	@Path("prototype")
	Treatment getPrototype();
	
	@GET
	@Path("all")
	List<Treatment> getAll();
	
	@GET
	@Path("{pk}")
	Treatment get(@PathParam("pk")String pk);
	
	@POST
	String create(Treatment o);

	@DELETE
	@Path("{pk}")
	void delete(String id);

	@POST
	@Path("update")
	void update(Treatment o);

}