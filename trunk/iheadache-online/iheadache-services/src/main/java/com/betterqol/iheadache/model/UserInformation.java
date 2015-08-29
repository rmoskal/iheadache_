/**
 * @(#) UserInformation.java
 */

package com.betterqol.iheadache.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.ektorp.support.TypeDiscriminator;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserInformation extends BaseEntity 
{
	
	private List<LookupDTO> triggerProfile;
	private List<LookupDTO> customTriggers;
	private List<Treatment> treatmentProfile;
	private List<Treatment> customTreatments;
	private List<PreventativeTreatment> preventativeProfile;
	private List<PreventativeTreatment> customPTreatments;
	private List<LookupDTO> customSymptoms;
	private List<LookupDTO> activeCustomSymptoms;
	private String name;
	private String sponsor;
	@TypeDiscriminator
	private AppSettings appSettings;
	private List<String> userAssociations;


	
	public UserInformation() {
		
		appSettings = new AppSettings();
		treatmentProfile = new ArrayList<Treatment>();
		customTreatments = new ArrayList<Treatment>();
		triggerProfile = new ArrayList<LookupDTO>();
		customTriggers = new ArrayList<LookupDTO>();
		preventativeProfile  = new ArrayList<PreventativeTreatment>();
		customPTreatments = new ArrayList<PreventativeTreatment>();
		customSymptoms  = new ArrayList<LookupDTO>();
		activeCustomSymptoms  = new ArrayList<LookupDTO>();		
		userAssociations  = new ArrayList<String>();			
		
	}
	
	public String getSponsor() {
		return sponsor;
	}
	public void setSponsor(String sponsor) {
		this.sponsor = sponsor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return 
	 */
	public  List<LookupDTO>  getTriggerProfile() {
		return triggerProfile;
	}
	/**
	 * @param triggerProfile
	 */
	public void setTriggerProfile( List<LookupDTO> o) {
		triggerProfile = o;
	}
	/**
	 * @return 
	 */
	public  List<Treatment> getTreatmentProfile() {
		return treatmentProfile;
	}
	/**
	 * @param treatmentProfile
	 */
	public void setTreatmentProfile( List<Treatment> o) {
		this.treatmentProfile = o;
	}
	
	public List<LookupDTO> getCustomTriggers() {
		return customTriggers;
	}

	public void setCustomTriggers(List<LookupDTO> customTriggers) {
		this.customTriggers = customTriggers;
	}

	public List<PreventativeTreatment> getPreventativeProfile() {
		return preventativeProfile;
	}
	public void setPreventativeProfile(
			List<PreventativeTreatment> preventativeProfile) {
		this.preventativeProfile = preventativeProfile;
	}
	
	public List<Treatment> getCustomTreatments() {
		return customTreatments;
	}
	public void setCustomTreatments(List<Treatment> customTreatments) {
		this.customTreatments = customTreatments;
	}
	public AppSettings getAppSettings() {
		return appSettings;
	}
	public void setAppSettings(AppSettings appSettings) {
		this.appSettings = appSettings;
	}

	public List<LookupDTO> getCustomSymptoms() {
		return customSymptoms;
	}

	public void setCustomSymptoms(List<LookupDTO> customSymptoms) {
		this.customSymptoms = customSymptoms;
	}

	public List<String> getUserAssociations() {
		return userAssociations;
	}

	public void setUserAssociations(List<String> userAssociations) {
		this.userAssociations = userAssociations;
	}

	public List<LookupDTO> getActiveCustomSymptoms() {
		return activeCustomSymptoms;
	}

	public void setActiveCustomSymptoms(List<LookupDTO> activeCustomSymptoms) {
		this.activeCustomSymptoms = activeCustomSymptoms;
	}
	
	@JsonIgnore
	public String getUserId() {
		
		return this.getId().split("_")[1];
		
	}

	public List<PreventativeTreatment> getCustomPTreatments() {
		return customPTreatments;
	}

	public void setCustomPTreatments(List<PreventativeTreatment> customPTreatments) {
		this.customPTreatments = customPTreatments;
	}

}
