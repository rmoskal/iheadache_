package com.betterqol.iheadache.profile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.AppHelpers;
import com.betterqol.iheadache.asynch.events.AppSettingsChanged;
import com.betterqol.iheadache.asynch.events.HealthcareUserAdded;
import com.betterqol.iheadache.asynch.events.UserAdded;
import com.betterqol.iheadache.model.AppSettings;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.IDescription;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.PreventativeTreatment;
import com.betterqol.iheadache.model.Treatment;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.model.dto.PreventativeTreatmentProfileItem;
import com.betterqol.iheadache.model.dto.ProfileItem;
import com.betterqol.iheadache.model.dto.TreatmentProfileItem;
import com.betterqol.iheadache.repository.HcpUserAssociationRepository;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.HealthcarePrincipalRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.PTRepository;
import com.betterqol.iheadache.repository.PreventativeTreatmentRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.betterqol.iheadache.repository.TreatmentRepository;
import com.betterqol.iheadache.repository.UserInformationRepository;
import com.betterqol.iheadache.repository.UserPrincipalRepository;
import com.betterqol.iheadache.resource.ProfileResource;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;



@Component
@Path(ProfileService.URL)
@RolesAllowed(value = { "ROLE_USER" })
public class ProfileService implements ProfileResource{
	
	private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);
	
	@Autowired
	UserInformationRepository repo;
	
	@Autowired
	UserPrincipalRepository pepo;
	
	@Autowired
	HealthcarePrincipalRepository hcepo;
	
	@Autowired
	LookupRepository lookups;
	
	@Autowired
	TreatmentRepository treatments;
	
	@Autowired
	PreventativeTreatmentRepository ptRepo;
	
	@Autowired
	HeadacheRepository hrepo;
	
	@Autowired
	protected HcpUserAssociationRepository associationRepo;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	PTRepository ptUsageRepo;
	
	@Autowired
	private MessageDigestPasswordEncoder encoder;
	
	
	public static final String URL = "/app/service/profile";
	
	public String getTriggerProfile() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		return getTriggerProfile(repo.getUser().getUserPk());
	}
	
	public String getTriggerProfile(String pk) throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		
		JSONArray results = _getTriggers(pk);
		return results.toString();	
		
	}
	
	public List<LookupDTO> getCustomTriggers(String pk) throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		
		UserInformation o = repo.get("u_"+pk);
		return o.getCustomTriggers();
		
	} 

	 JSONArray _getTriggers(String pk) throws JsonGenerationException,
			JsonMappingException, JSONException, IOException {
		UserInformation o = repo.get("u_"+pk);
		JSONArray results =  AppHelpers.constructProfile(o.getTriggerProfile(), lookups.findByLookupType(Kind.TRIGGER), false);
		JSONArray results2 =  AppHelpers.constructProfile(o.getTriggerProfile(), o.getCustomTriggers(), true);
		
		return AppHelpers.mergeJSONArrays(results, results2);
	}
	 
	 
	 public String getCustomSymptoms() throws JsonGenerationException, 
	 		JsonMappingException, JSONException, IOException {
		 
		return  _getCustomSymptoms(repo.getUser().getUserPk()).toString();
		 
	 }
	
	 JSONArray _getCustomSymptoms(String pk)throws JsonGenerationException,
		JsonMappingException, JSONException, IOException {
		 
			UserInformation o = repo.get("u_"+pk);
			return AppHelpers.constructProfile(o.getActiveCustomSymptoms(), o.getCustomSymptoms(), false);
	 }

	
	 
	@Override
	public String getPreventativeTreatmentProfile() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		
		return getPreventativeTreatmentProfile(repo.getUser().getUserPk());
	}
	
	
	public String getPreventativeTreatmentProfile(String pk) throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		UserInformation o = repo.get("u_"+pk);
		JSONArray results =  AppHelpers.constructProfile(o.getPreventativeProfile(), ptRepo.getAll() , false);
		JSONArray results2 =  AppHelpers.constructProfile(o.getPreventativeProfile(), o.getCustomPTreatments(), true);
		return AppHelpers.mergeJSONArrays(results, results2).toString();			
	}
	public String getTreatmentProfile() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		return getTreatmentProfile(repo.getUser().getUserPk());
	}
	
	public String getTreatmentProfile(String pk) throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		UserInformation o = repo.get("u_"+pk);
		
		JSONArray results = AppHelpers.constructProfile(o.getTreatmentProfile(), treatments.getAll(), false);
		JSONArray results2 = AppHelpers.constructProfile(o.getTreatmentProfile(), o.getCustomTreatments(), true);
		return AppHelpers.mergeJSONArrays(results, results2).toString();	
	}
	public String getAssociationProfile() throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		return  _getAssociationProfile(repo.getUser().getUserPk()).toString();
	}
	
	public JSONArray _getAssociationProfile(String pk) throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		UserInformation o = repo.get("u_"+pk);	
		ArrayList<IDescription> phys = Lists.newArrayList(Iterables.transform(o.getUserAssociations(), 
				RepositoryHelpers.MAKE_IDESCRIPTION));
		return AppHelpers.constructProfile(phys, hcepo.findByApproved1(true), false, "password");	
		
	}
	
	public UserInformation getSafeUser() {
		UserInformation u = repo.getCurrent();
		u.setId(u.getId().split("_")[1]);
		return u;
	}
	
	public UserPrincipal getSafePrincipal() {
		UserInformation u = repo.getCurrent();
		UserPrincipal p  = pepo.get(u.getId().split("_")[1]);
		p.setPassword("_________");
		return p;
	}
	
	public UserPrincipal findPrincipal(String email) {
		UserPrincipal res = pepo.findByName(email);
		res.setPassword("___________");
		return res;
	}
	

	public UserPrincipal getPrincipal(String id) {
		UserPrincipal res = pepo.get(id);
		res.setPassword("___________");
		return res;
	}
	
	public HealthcarePrincipal getSafeHealthcarePrincipal() {
		UserInformation u = repo.getCurrent();
		HealthcarePrincipal p  = hcepo.get(u.getId().split("_")[1]);
		p.setPassword("_________");
		return p;
	}
	
	@Override
	public HealthcarePrincipal getHcpPrincipal(String id) {
		HealthcarePrincipal p  = hcepo.get(id);
	    p.setPassword("_________");
		return p;
	}
	
	public HealthcarePrincipal findHcpPrincipal(String email) {
		HealthcarePrincipal res = hcepo.findByName(email);
		res.setPassword("___________");
		return res;
	}
	
	
	
	public void logOut(){
		
		SecurityContextHolder.clearContext();
	}
	
	public void registerRegularUser (UserPrincipal p) {
		
		pepo.create(p);
		repo.createForParent(p.getId(),p.getName());
		publisher.publishEvent(new UserAdded(this,p));
	}
	
	public void registerHealthcareUser (HealthcarePrincipal p) {
		
		hcepo.create(p);
		repo.createForParent(p.getId(),p.getName());
		publisher.publishEvent(new HealthcareUserAdded(this,p));
	}
	
	public void updateUserAssociations(List<String>items) {
		_updateUserAssociations(repo.getUser().getUserPk(),items);
		
	}
	
	public void _updateUserAssociations(String pk, List<String>items) {
		UserInformation user = repo.get("u_" + pk);
		List<String> old = user.getUserAssociations();
		for (String candidate:items) {
				
		}
		user.setUserAssociations(items);
		repo.update(user);
		//associationRepo.deleteForUserId(pk);
		associationRepo.create(pk, items);
		
	}
	
	
	public void updateUserSymptoms(List <ProfileItem> items) {
		logSome(items);
		_updateUserSymptoms(repo.getUser().getUserPk(),items);
	}

	private static void logSome(List<ProfileItem> items) {
		for (ProfileItem i :items)
			logger.info(i.toString());
	}
	
	public void _updateUserSymptoms(String pk, List<ProfileItem> inputList) {
		UserInformation user = repo.get("u_" + pk);
		
		List<LookupDTO>active =  Lists.newArrayList();
		List<LookupDTO>others =  Lists.newArrayList();
		
		AppHelpers.splitResponse(inputList, active, others, false);
		user.setActiveCustomSymptoms(active);
		user.setCustomSymptoms(others);
		repo.update(user);
	}
	
	public void updateUserTriggers(List <ProfileItem> items) {
		logSome(items);
		_updateUserTriggers(repo.getUser().getUserPk(),items);
	}
	
	public void _updateUserTriggers(String pk, List<ProfileItem> inputList) {
		UserInformation user = repo.get("u_" + pk);
		
		List<LookupDTO>active =  Lists.newArrayList();
		List<LookupDTO>others =  Lists.newArrayList();
		
		AppHelpers.splitResponse(inputList, active, others, true);
		user.setTriggerProfile(active);
		user.setCustomTriggers(others);
		repo.update(user);	
	}
	
	
	@Override
	public void updateUserPreventatives(List <PreventativeTreatmentProfileItem> items) {
		_updateUserPreventatives(repo.getUser().getUserPk(),items);
	}
	
	public void _updateUserPreventatives(String pk, List<PreventativeTreatmentProfileItem> inputList) {
		
		UserInformation user = repo.get("u_" + pk);
		
		List<PreventativeTreatment>active =  Lists.newArrayList();
		List<PreventativeTreatment>others =  Lists.newArrayList();
		
		splitPreventativeTreatmentResponse(inputList, active, others);
		user.setPreventativeProfile(active);
		user.setCustomPTreatments(others);
		repo.update(user);	
	}
	
	public void updatePreferences (AppSettings appSettings) {
		_updatePreferences(repo.getUser().getUserPk(), appSettings);
	}
	
	public void _updatePreferences (String pk, AppSettings appSettings) {
		UserInformation user = repo.get("u_" + pk);
		AppSettings oldSettings = user.getAppSettings();
		user.setAppSettings(appSettings);
		repo.update(user);
		publisher.publishEvent(new AppSettingsChanged(this,user, oldSettings));
		
		
	}
	
	public void updateAcuteTreatments(List<TreatmentProfileItem> items) {
		_updateAcuteTreatments(repo.getUser().getUserPk(), items);
		
	}
	
	public void _updateAcuteTreatments(String pk,List<TreatmentProfileItem> items) {
		UserInformation user = repo.get("u_" + pk);
		List<Treatment>active =  Lists.newArrayList();
		List<Treatment>others =  Lists.newArrayList();
		splitTreatmentResponse(items, active, others);
		
		user.setTreatmentProfile(active);
		user.setCustomTreatments(others);
		repo.update(user);

	}
	
	public void updateCredentials(String email, String pw) {
		
		updateCredentials(repo.getUser().getUserPk(), email, pw);
	}
	
	public void updateUserInformation(UserPrincipal ui) {
		
		updateUserInformation(repo.getUser().getUserPk(),ui);
	}
	
	public void updateUserInformation(HealthcarePrincipal ui) {
		
		updateUserInformation(repo.getUser().getUserPk(),ui);
	}
	
	
	public void updateUserInformation(String pk, UserPrincipal nui) {
		
		UserPrincipal up = pepo.get(pk);
		nui.setId(up.getId());
		nui.setRevision(up.getRevision());
		nui.setName(up.getName());
		nui.setPassword(up.getPassword());
		pepo.update(nui);
	}
	
	
	public void updateUserInformation(String pk, HealthcarePrincipal nui) {
		
		HealthcarePrincipal up = hcepo.get(pk);
		System.out.println("Hcp-principal old " + up);
		System.out.println("Hcp-principal new " + nui);
		nui.setId(up.getId());
		nui.setRevision(up.getRevision());
		nui.setName(up.getName());
		nui.setPassword(up.getPassword());
		hcepo.update(nui);
	}
	
	
	public void updateCredentials(String pk, String email, String pw) {
		
	
		UserInformation ui = repo.get("u_" + pk);
		ui.setName(email);
		UserPrincipal up = pepo.get(pk);
		
		up.setName(email);
		if (StringUtils.isNotBlank(pw))
			up.setPassword(encoder.encodePassword(pw, null));
		pepo.update(up);
		repo.update(ui);
	}

	public String test() {
	
		return "foo";
	}
	
	
	public static void splitTreatmentResponse(List <TreatmentProfileItem> inputList,List<Treatment>active, 
			List<Treatment> others) {
		
		for (TreatmentProfileItem item : inputList ) {
			if (item.isIn())
				active.add(new Treatment(item.getId(),item.getDescription(),item.getGenericName(), item.getForm(),
						item.getUom(), item.isMigraineTreatment()));
		
			if (item.isCustom())
				others.add(new Treatment(item.getId(),item.getDescription(),item.getGenericName(), item.getForm(),
						item.getUom(), item.isMigraineTreatment()));
	
		}
		
	}
	
	
	public static void splitPreventativeTreatmentResponse(List <PreventativeTreatmentProfileItem> inputList,List<PreventativeTreatment>active, 
			List<PreventativeTreatment> others) {
		
		for (PreventativeTreatmentProfileItem item : inputList ) {
			if (item.isIn())
				active.add(new PreventativeTreatment(item.getId(),item.getDescription(),item.getGenericName()));
		
			if (item.isCustom())
				others.add(new PreventativeTreatment(item.getId(),item.getDescription(),item.getGenericName()));
	
		}
		
	}

	
	public void deleteUserCompletely(String userId) {
		
		UserInformation ui = repo.get("u_" + userId);
		UserPrincipal up = pepo.get(userId);
		associationRepo.deleteForUserId(userId); //delete associations
		hrepo.deleteForUser(userId); //headaches
		ptUsageRepo.deleteForUser(userId); //preventative treatment usages
		repo.delete(ui.getId()); //User Information
		pepo.remove(up); // user principal
		logger.debug("Deleted user");
		
		
	}
	
	public void deleteHCPCompletely(String userId) {
		
		UserInformation ui = repo.get("u_" + userId);
		HealthcarePrincipal up = hcepo.get(userId);
		associationRepo.deleteForHcpId(userId); //delete associations
		hrepo.deleteForUser(userId); //headaches
		ptUsageRepo.deleteForUser(userId); //preventative treatment usages
		repo.delete(ui.getId()); //User Information
		hcepo.remove(up); // user principal
		logger.debug("Deleted hcp user");
		
	}








	

}
