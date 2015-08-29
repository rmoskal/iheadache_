package com.betterqol.iheadache.resource;


import java.util.Map;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONException;


@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface ClientStorageResource {
	

	public static final String URL = "/app/service/storage";

	@GET
	@Path("test")
	public  String test();
	
	@GET
	@Path("owner/")
	public  String findByUser();

	@PUT
	@Path("{key}")
	public  void insert(@PathParam("key") String key,  String items);



	@DELETE
	@Path ("{key}")
	public  void delete(@PathParam("key") String key);

	@GET
	@Path("{key}")
	public  String fetch(@PathParam("key") String key) throws JSONException;
	
	
	@POST
	@Path("multi/{key}")
	void insertMulti(@PathParam("key")String key,String items);
	
	
	@GET
	@Path("multi/{key}")
	public  String fetchMulti(@PathParam("key") String key) throws JSONException;
	


}
