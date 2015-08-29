package com.betterqol.iheadache.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;

import com.betterqol.iheadache.extensions.JacksonJoda;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class HealthcarePrincipal  extends BaseEntity implements IPrincipal, IDescription{

	private String name;
	private String password;
	@TypeDiscriminator
	private String institution;
	private String firstName;
	private String lastName;
	private String address1;
	private String address2;
	private String city;
	private String state;
	private String zipcode;
	private String country;
	private String phone;
	private String fax;
	private String website;
	
	private DateTime birthdate;
	private String gender;
	private String degree;
	private String specialty;
	private boolean ucns_certified_in_Headache;
	private boolean include_in_physician_finder;
	private boolean american_academy_of_neurology;
	private boolean american_headache_society;
	private boolean headache_cooperative_of_new_england;
	private boolean headache_cooperative_of_the_pacific;
	private boolean national_headache_foundation;
	private boolean southern_headache_society;
	
	private String american_academy_of_neurology_meeting;
	private String american_headache_society_meeting;
	private String headache_cooperative_of_new_england_meeting;
	private String headache_cooperative_of_the_pacific_meeting;
	private String national_headache_foundation_meeting;
	private String southern_headache_society_meeting;

	private String patient_brochures;
	private String brochure_holders;
	private String patient_cards;
	private String card_holders;
	
	private boolean approved;
	private String currentUser;
	private DateTime created;
	
	
	

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean getApproved() {
		return approved;
	}
	
	//We add this to support filtering/ ektorp 
	public String getApproved1() {
		return String.valueOf(approved);
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	@Override
	public String getDescription() {
		
		return this.name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address) {
		this.address1 = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public DateTime getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(DateTime birthdate) {
		this.birthdate = birthdate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getSpecialty() {
		return specialty;
	}

	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}

	public boolean isUcns_certified_in_Headache() {
		return ucns_certified_in_Headache;
	}

	public void setUcns_certified_in_Headache(boolean ucns_certified_in_Headache) {
		this.ucns_certified_in_Headache = ucns_certified_in_Headache;
	}

	public boolean isInclude_in_physician_finder() {
		return include_in_physician_finder;
	}

	public void setInclude_in_physician_finder(boolean include_in_physician_finder) {
		this.include_in_physician_finder = include_in_physician_finder;
	}

	public boolean isAmerican_academy_of_neurology() {
		return american_academy_of_neurology;
	}

	public void setAmerican_academy_of_neurology(
			boolean american_academy_of_neurology) {
		this.american_academy_of_neurology = american_academy_of_neurology;
	}

	public boolean isAmerican_headache_society() {
		return american_headache_society;
	}

	public void setAmerican_headache_society(boolean american_headache_society) {
		this.american_headache_society = american_headache_society;
	}

	public boolean isHeadache_cooperative_of_new_england() {
		return headache_cooperative_of_new_england;
	}

	public void setHeadache_cooperative_of_new_england(
			boolean headache_cooperative_of_new_england) {
		this.headache_cooperative_of_new_england = headache_cooperative_of_new_england;
	}

	public boolean isHeadache_cooperative_of_the_pacific() {
		return headache_cooperative_of_the_pacific;
	}

	public void setHeadache_cooperative_of_the_pacific(
			boolean headache_cooperative_of_the_pacific) {
		this.headache_cooperative_of_the_pacific = headache_cooperative_of_the_pacific;
	}

	public boolean isNational_headache_foundation() {
		return national_headache_foundation;
	}

	public void setNational_headache_foundation(boolean national_headache_foundation) {
		this.national_headache_foundation = national_headache_foundation;
	}

	public boolean isSouthern_headache_society() {
		return southern_headache_society;
	}

	public void setSouthern_headache_society(boolean southern_headache_society) {
		this.southern_headache_society = southern_headache_society;
	}

	public String getAmerican_academy_of_neurology_meeting() {
		return american_academy_of_neurology_meeting;
	}

	public void setAmerican_academy_of_neurology_meeting(
			String american_academy_of_neurology_meeting) {
		this.american_academy_of_neurology_meeting = american_academy_of_neurology_meeting;
	}

	public String getAmerican_headache_society_meeting() {
		return american_headache_society_meeting;
	}

	public void setAmerican_headache_society_meeting(
			String american_headache_society_meeting) {
		this.american_headache_society_meeting = american_headache_society_meeting;
	}

	public String getHeadache_cooperative_of_new_england_meeting() {
		return headache_cooperative_of_new_england_meeting;
	}

	public void setHeadache_cooperative_of_new_england_meeting(
			String headache_cooperative_of_new_england_meeting) {
		this.headache_cooperative_of_new_england_meeting = headache_cooperative_of_new_england_meeting;
	}

	public String getHeadache_cooperative_of_the_pacific_meeting() {
		return headache_cooperative_of_the_pacific_meeting;
	}

	public void setHeadache_cooperative_of_the_pacific_meeting(
			String headache_cooperative_of_the_pacific_meeting) {
		this.headache_cooperative_of_the_pacific_meeting = headache_cooperative_of_the_pacific_meeting;
	}

	public String getNational_headache_foundation_meeting() {
		return national_headache_foundation_meeting;
	}

	public void setNational_headache_foundation_meeting(
			String national_headache_foundation_meeting) {
		this.national_headache_foundation_meeting = national_headache_foundation_meeting;
	}

	public String getSouthern_headache_society_meeting() {
		return southern_headache_society_meeting;
	}

	public void setSouthern_headache_society_meeting(
			String southern_headache_society_meeting) {
		this.southern_headache_society_meeting = southern_headache_society_meeting;
	}

	public String getPatient_brochures() {
		return patient_brochures;
	}

	public void setPatient_brochures(
			String patient_brochures) {
		this.patient_brochures = patient_brochures;
	}

	public String getBrochure_holders() {
		return brochure_holders;
	}

	public void setBrochure_holders(
			String brochure_holders) {
		this.brochure_holders = brochure_holders;
	}

	public String getPatient_cards() {
		return patient_cards;
	}

	public void setPatient_cards(
			String patient_cards) {
		this.patient_cards = patient_cards;
	}

	public String getCard_holders() {
		return card_holders;
	}

	public void setCard_holders(
			String card_holders) {
		this.card_holders = card_holders;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}
	
	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}


	@JsonSerialize(using = JacksonJoda.class)
	public DateTime getCreated() {
		return created;
	}
	@JsonSerialize(using = JacksonJoda.class)
	public void setCreated(DateTime created) {
		this.created = created;
	}

	@Override
	public String toString() {
		return "HealthcarePrincipal [name=" + name + ", password=" + password
				+ ", institution=" + institution + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", address=" + address1
				+ ", address2=" + address2 + ", city=" + city + ", state="
				+ state + ", zipcode=" + zipcode + ", country=" + country
				+ ", phone=" + phone + ", fax=" + fax + ", website=" + website
				+ ", birthdate=" + birthdate + ", gender=" + gender
				+ ", degree=" + degree + ", specialty=" + specialty
				+ ", ucns_certified_in_Headache=" + ucns_certified_in_Headache
				+ ", include_in_physician_finder="
				+ include_in_physician_finder
				+ ", american_academy_of_neurology="
				+ american_academy_of_neurology
				+ ", american_headache_society=" + american_headache_society
				+ ", headache_cooperative_of_new_england="
				+ headache_cooperative_of_new_england
				+ ", headache_cooperative_of_the_pacific="
				+ headache_cooperative_of_the_pacific
				+ ", national_headache_foundation="
				+ national_headache_foundation + ", southern_headache_society="
				+ southern_headache_society
				+ ", american_academy_of_neurology_meeting="
				+ american_academy_of_neurology_meeting
				+ ", american_headache_society_meeting="
				+ american_headache_society_meeting
				+ ", headache_cooperative_of_new_england_meeting="
				+ headache_cooperative_of_new_england_meeting
				+ ", headache_cooperative_of_the_pacific_meeting="
				+ headache_cooperative_of_the_pacific_meeting
				+ ", national_headache_foundation_meeting="
				+ national_headache_foundation_meeting
				+ ", southern_headache_society_meeting="
				+ southern_headache_society_meeting + ", approved=" + approved
				+ "]";
	}
	
	

}
