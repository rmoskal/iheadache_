package com.betterqol.iheadache.resource;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.betterqol.iheadache.model.UserInformation;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@RolesAllowed(value = { "ROLE_USER" })
public interface UserResource {
	
	public static final String URL = "/app/service/user";
	
	@GET
	@Path("--test")
	String test ();
	
	@POST
	String create (UserInformation o);
	
	@GET
	@Path("/{pk}")
	UserInformation get(@PathParam("pk")String pk);
	
	@GET
	@Path("/by-name/{user}")
	public UserInformation findByName(@PathParam("user")String user);
	
	
	@DELETE
	@Path("/{pk}")
	void delete (@PathParam("pk")String pk);
	
	@POST
	@Path("/update")
	void update (UserInformation o);
	


	

}
