/**
 * @(#) PTUsage.java
 */

package com.betterqol.iheadache.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;

import com.betterqol.iheadache.extensions.JacksonJoda;
import com.betterqol.iheadache.extensions.JacksonJoda2;
import com.betterqol.iheadache.extensions.JodaTimeAdapter;


/**
 * Represents the usage of a preventative treatment
 * @author rob
 *
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PTUsage extends BaseEntity
{
	
	private String user;
	@XmlJavaTypeAdapter(JodaTimeAdapter.class)
	private DateTime start;
	@XmlJavaTypeAdapter(JodaTimeAdapter.class)
	private DateTime end;
	private String stopReason;
	private String dose;
	private String treatmentId;
	@TypeDiscriminator
	private String treatmentDescription;
	private String genericDescription;
	//private String bothDescription;
	
	public PTUsage(){
		
	}
	
	public PTUsage(String user, Date start, 
			String dose, PreventativeTreatment treatment) {
		super();
		this.user = user;
		this.start = new DateTime(start);
		this.dose = dose;
		this.setTreatmentId(treatment.getId());
		this.setTreatmentDescription(treatment.getDescription());
		this.setGenericDescription(treatment.getDescription());
	}
	
	/**
	 * Really should only be used for testing as we seldom want to create a treatement and end it 
	 * right away.
	 * @param user
	 * @param start
	 * @param end
	 * @param stopReason
	 * @param dose
	 * @param treatment
	 */
	public PTUsage(String user, Date start, Date end, String stopReason,
			String dose, String treatment, String genericTreatment) {
		super();
		this.user = user;
		this.start = new DateTime(start);
		this.end = (end != null) ?new DateTime(end): null;
		this.stopReason = stopReason;
		this.dose = dose;
		this.setTreatmentId(treatment);
		this.setTreatmentDescription(treatment);
		this.setGenericDescription(genericTreatment);
	}
	
	
	
	@JsonSerialize(using = JacksonJoda.class)
	public DateTime getStart() {
		return start;
	}
	@JsonDeserialize(using = JacksonJoda2.class)
	public void setStart(DateTime start) {
		this.start = start;
	}
	@JsonSerialize(using = JacksonJoda.class)
	public DateTime getEnd() {
		return end;
	}
	@JsonDeserialize(using = JacksonJoda2.class)
	public void setEnd(DateTime end) {
		this.end = end;
	}
	public String getStopReason() {
		return stopReason;
	}
	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}
	public String getDose() {
		return dose;
	}
	public void setDose(String dose) {
		this.dose = dose;
	}

	public void setPtreatment(CouchLookup treatment) {
		this.setTreatmentId(treatment.getId());
		this.setTreatmentDescription(treatment.getDescription());
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}

	public String getTreatmentId() {
		return treatmentId;
	}

	public void setTreatmentId(String treatmentId) {
		this.treatmentId = treatmentId;
	}

	public String getTreatmentDescription() {
		return treatmentDescription;
	}

	public void setTreatmentDescription(String treatmentDescription) {
		this.treatmentDescription = treatmentDescription;
	}

	public String getGenericDescription() {
		return genericDescription;
	}

	public void setGenericDescription(String genericDescription) {
		this.genericDescription = genericDescription;
	}
		
	


	
	
}
