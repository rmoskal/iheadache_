package com.betterqol.iheadache.hcp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.AppHelpers;
import com.betterqol.iheadache.evaluations.impl.HeadacheEvaluator;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.HcpUserAssociation;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.PTUsage;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.repository.HcpUserAssociationRepository;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.HealthcarePrincipalRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.PTRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.betterqol.iheadache.repository.UserInformationRepository;
import com.betterqol.iheadache.repository.UserPrincipalRepository;
import com.betterqol.iheadache.resource.HcpResource;
import com.google.common.collect.Lists;

@Component
@Path(HcpService.URL)
@RolesAllowed(value = { "ROLE_USER" })
public class HcpService implements HcpResource {
	
	public static final String URL = "/app/service/hcp";
	
	private static final Logger logger = LoggerFactory.getLogger(HcpService.class);
	
	@Autowired
	HeadacheRepository hrepo;
	
	@Autowired
	UserInformationRepository users;
	
	@Autowired
	PTRepository ptRepo;
	
	@Autowired
	HealthcarePrincipalRepository hcprepo;
	
	@Autowired
	UserPrincipalRepository urepo;
	
	@Autowired
	HcpUserAssociationRepository associationRepo;
	
	@Autowired
	LookupRepository lookups;
	
	@Autowired
	@Qualifier("headacheDatabase")
	CouchDbConnector db;
	
	@Override
	public String getCurrentPatient() throws JSONException{
		JSONObject res = new JSONObject();
		res.put("id", getCurrentPatient(hrepo.getUser().getUserPk()));
		return res.toString();
	}
	
	public String getCurrentPatient(String userId){
		HealthcarePrincipal hp = hcprepo.get(userId);
		return hp.getCurrentUser();
	}
	
	@Override
	public String switchUser(String userId) {
		// Get a list of users that have picked this HCP 
		switchUserInformation(users.getCurrent(), users.get("u_"+ userId));
		return switchUser(userId, hrepo.getUser().getUserPk(),users.getCurrent());	
	}
	
	public String switchUser(String source, String target, UserInformation targetUserInfo ) {
		
		long startTime = System.currentTimeMillis();
		ptRepo.deleteForUser(target);
		hrepo.deleteForUser(target);
		List<Headache> headaches = hrepo.findByUser(source);
		
		HealthcarePrincipal hp = hcprepo.get(target);
		//System.out.print("3 setCurrentUser is next\n");
		hp.setCurrentUser(source);
		//System.out.print("4 setCurrentUser is complete\n");
		hcprepo.update(hp);
		//System.out.print("5 update is complete\n");
		
		HeadacheEvaluator eval = new HeadacheEvaluator();
		Map context = new HashMap();
		
		context.put("USER",targetUserInfo);
		context.put("HEADCAHE_TYPES", RepositoryHelpers.enMap2(lookups.findByLookupType(Kind.HEADACHE_TYPE)));
		
		long startForHeadacheTime = System.currentTimeMillis();
		for (Headache h: headaches) {
			eval.transform(h, context);
			h.setRevision(null);
			h.setId(UUID.randomUUID().toString());
			h.setUser(target);
		}
		long endForHeadacheTime = System.currentTimeMillis();
		
		hrepo.createBulk(headaches);
		
		List<PTUsage> usages = ptRepo.findByUser(source);
		long startPTUTime = System.currentTimeMillis();
		for (PTUsage usage : usages) {
			
			usage.setRevision(null);
			usage.setId(UUID.randomUUID().toString());
			usage.setUser(target);
		}
		
		long endPTUTime = System.currentTimeMillis();
		ptRepo.createBulk(usages);
		long endTime = System.currentTimeMillis();
		System.out.println("HcpService / setup took " + (startForHeadacheTime - startTime) + " milliseconds");
		System.out.println("HcpService / ForHeadache took " + (endForHeadacheTime - startForHeadacheTime) + " milliseconds");
		System.out.println("HcpService / createBulk1 took " + (startPTUTime - endForHeadacheTime) + " milliseconds");
		System.out.println("HcpService / PTU took " + (endPTUTime - startPTUTime) + " milliseconds");
		System.out.println("HcpService / createBulk2 took " + (endTime - endPTUTime) + " milliseconds");
		System.out.println("HcpService / switchUser took " + (endTime - endTime) + " milliseconds");
		
		return "ready";
	}
	
	/**
	 * Copy over all the ancillary information for a patient like custom treatments, etc.
	 * @param target
	 * @param source
	 */
	public void switchUserInformation(UserInformation target, UserInformation source) {
		
		long startTime = System.currentTimeMillis();
		target.setActiveCustomSymptoms(source.getActiveCustomSymptoms());
		target.setCustomPTreatments(source.getCustomPTreatments());
		target.setCustomSymptoms(source.getCustomSymptoms());
		target.setCustomTreatments(source.getCustomTreatments());
		target.setCustomTriggers(source.getCustomTriggers());
		target.setPreventativeProfile(source.getPreventativeProfile());
		//System.out.print("1 update user info next\n");
		users.update(target);
		//System.out.print("2 updated user info\n");
		long endTime = System.currentTimeMillis();
		System.out.println("HcpService / switchUserInformation took " + (endTime - startTime) + " milliseconds");
		
	};
	
	
	@Override
	public String getAssociations() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		return getAssociations(hrepo.getUser().getUserPk()).toString();
	}
	
	
	public JSONArray getAssociations(String userId) throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		long startTime = System.currentTimeMillis();
		List<String> userIds = Lists.newArrayList();
		
		for (HcpUserAssociation a : associationRepo.findByHcpId(userId)) 
			userIds.add(a.getUserId());
		long midTime = System.currentTimeMillis();

		ViewQuery q = new ViewQuery()
                      .allDocs()
                      .includeDocs(true)
                      .keys(userIds);
		
		List<UserPrincipal> users = db.queryView(q, UserPrincipal.class);
		long endTime = System.currentTimeMillis();
		//logger.error("End query");
		System.out.println("HcpService / getAssociation List and For took " + (midTime - startTime) + " milliseconds");
		System.out.println("HcpService / getAssociation Query  took " + (endTime - midTime) + " milliseconds");		
		System.out.println("HcpService / getAssociation All  took " + (endTime - startTime) + " milliseconds");
				
		return AppHelpers.simpleJson(users, "password");
		
	}
	
	
	

}
