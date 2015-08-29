package com.betterqol.iheadache.resource;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;

import com.betterqol.iheadache.model.CouchLookup;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed(value = { "ROLE_USER" })
public interface LookupResource {
	
	@GET
	@Path("test")
	String test ();
	
	@GET
	List<CouchLookup> getAll();
	
	@POST
	String create (CouchLookup o);
	
	@GET
	@Path("prototype")
	CouchLookup getPrototype();
	
	@GET
	@Path("{pk}")
	CouchLookup get(@PathParam("pk")String pk);
	
	@DELETE
	@Path("{pk}")
	void delete (@PathParam("pk")String pk);
	
	@POST
	@Path("update")
	void update (CouchLookup o);
	

	@GET
	@Path("for/{kind}")
	String  findByLookupType2(@PathParam("kind")CouchLookup.Kind type) throws JSONException;


}
