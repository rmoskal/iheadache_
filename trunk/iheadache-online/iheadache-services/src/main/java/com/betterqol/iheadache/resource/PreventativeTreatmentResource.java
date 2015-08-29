package com.betterqol.iheadache.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.betterqol.iheadache.model.PreventativeTreatment;


@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
@Path("/app/service/preventative-treatments")
public interface PreventativeTreatmentResource {

	@GET
	List<PreventativeTreatment> getAll();

}
