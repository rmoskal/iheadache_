package com.betterqol.iheadache.resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONException;

import com.betterqol.iheadache.model.AppSettings;
import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.model.dto.PreventativeTreatmentProfileItem;
import com.betterqol.iheadache.model.dto.ProfileItem;
import com.betterqol.iheadache.model.dto.TreatmentProfileItem;

@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public interface ProfileResource {
	
	
	@GET
	@Path("test")
	String test(); 
	
	@GET
	@Path("triggers")
	String getTriggerProfile() throws JsonGenerationException, JsonMappingException, 
	JSONException, IOException;
	
	
	@GET
	@Path("treatments")
	String getTreatmentProfile()throws JsonGenerationException, JsonMappingException, 
	JSONException, IOException;
	
	
	@GET
	@Path("custom_symptoms")
	String getCustomSymptoms() throws JsonGenerationException, 
		JsonMappingException, JSONException, IOException;
	
	@GET
	@Path("associations")
	String getAssociationProfile() throws JsonGenerationException, JsonMappingException, 
	JSONException, IOException;
	
	@PUT
	@Path("associations")
	void updateUserAssociations(List<String>items);
	
	@PUT
	@Path("custom_symptoms")
	void updateUserSymptoms(List <ProfileItem> items);
	
	@PUT
	@Path("triggers")
	void updateUserTriggers (List <ProfileItem> items);
	
	@PUT
	@Path("settings")
	void updatePreferences (AppSettings appSettings);
	
	@PUT
	@Path("treatments")
	void updateAcuteTreatments(List<TreatmentProfileItem> items);
	
	
	@GET
	@Path("user")
	UserInformation getSafeUser();
	
	@GET
	@Path("principal")
	public UserPrincipal getSafePrincipal();
	
	@GET
	@Path("principal/id/{id}")
	public UserPrincipal getPrincipal(@PathParam("id")String id);
	
	@GET
	@Path("principal/email/{email}")
	public UserPrincipal findPrincipal(@PathParam("email")String email);
	
	@PUT
	@Path("principal")
	void updateUserInformation(UserPrincipal ui);
	
	@GET
	@Path("hcp-principal")
	public HealthcarePrincipal getSafeHealthcarePrincipal();
	
	@GET
	@Path("hcp-principal/id/{id}")
	public HealthcarePrincipal getHcpPrincipal(@PathParam("id")String id);
	
	@GET
	@Path("hcp-principal/email/{email}")
	public HealthcarePrincipal findHcpPrincipal(@PathParam("email")String email);
	
	@PUT
	@Path("hcp-principal")
	void updateUserInformation(HealthcarePrincipal ui) ;
	
	
	@POST
	@Path("credentials")
	void updateCredentials(@FormParam("email") String email, @FormParam("password") String pw);
	
	
	
	@PUT
	@Path("logout")
	void logOut();

	@PUT
	@Path("ptreatments")
	void updateUserPreventatives(List <PreventativeTreatmentProfileItem> items);


	@GET
	@Path("ptreatments")
	String getPreventativeTreatmentProfile()
			throws JsonGenerationException, JsonMappingException,
			JSONException, IOException;
}
