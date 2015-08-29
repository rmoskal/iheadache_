package com.betterqol.iheadache.resource;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonNode;

import com.betterqol.iheadache.extensions.DateFormat;
import com.betterqol.iheadache.model.CouchLookup;
import com.betterqol.iheadache.model.Headache;

@Produces({ MediaType.APPLICATION_JSON })
@Consumes({ MediaType.APPLICATION_JSON })
public interface DiaryResource {

	@GET
    @Produces("application/vnd.headache+json")
	Headache getFirst(@QueryParam("date")  @DateFormat("yyyy-MM-dd") Date date) throws IOException;

	@GET
	@Path("day")
	List<JsonNode> get(@QueryParam("date")  @DateFormat("yyyy-MM-dd") Date date) throws IOException;
	
	@GET
	@Path("empty")
	Headache getEmpty() throws IOException;
	
	@POST
    @Consumes("application/vnd.headache+json")
	String create (Headache o);
	
	@PUT
	@Path("update")
	@Consumes("application/vnd.headache+json")
	void update (Headache o);

	@GET
	@Path("standard_symptoms")
	public  List<CouchLookup> getStandardSymptoms();
	
	@GET
	@Path("ptusage")
	public String getPTreaments(@QueryParam("year") int year,@QueryParam("month")int month) throws IOException;

}
