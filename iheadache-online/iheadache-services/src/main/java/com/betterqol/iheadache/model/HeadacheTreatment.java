package com.betterqol.iheadache.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import com.betterqol.iheadache.extensions.JacksonJoda;
import com.betterqol.iheadache.extensions.JacksonJoda2;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class HeadacheTreatment {

	
	private DateTime dose;
	private Treatment treatment;
	
	public HeadacheTreatment() {
		
	}
	
	public HeadacheTreatment(DateTime dose, Treatment treatment) {
		super();
		this.dose = dose;
		this.treatment = treatment;
	}
	@JsonSerialize(using = JacksonJoda.class)
	public DateTime getDose() {
		return dose;
	}
	
	@JsonDeserialize(using = JacksonJoda2.class)
	public void setDose(DateTime dose) {
		this.dose = dose;
	}
	public Treatment getTreatment() {
		return treatment;
	}
	public void setTreatment(Treatment treatment) {
		this.treatment = treatment;
	}
	
}
