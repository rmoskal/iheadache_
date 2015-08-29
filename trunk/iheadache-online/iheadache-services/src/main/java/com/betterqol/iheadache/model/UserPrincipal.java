package com.betterqol.iheadache.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;

import com.betterqol.iheadache.extensions.JacksonJoda;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class UserPrincipal  extends BaseEntity implements IPrincipal  {
	
	private String name;
	private String firstName;
	private String lastName;
	@TypeDiscriminator
	private String address;
	private String city;
	private String state;
	private String zipcode;
	private String country;
	private DateTime birthdate;
	private String gender;
	private String password;
	private DateTime created;
	
	
	
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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
	
	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
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
		return "UserPrincipal [name=" + name + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", address=" + address + ", city="
				+ city + ", state=" + state + ", country=" + country
				+ ", birthdate=" + birthdate + ", gender=" + gender
				+ ", password=" + password + "]";
	}





}
