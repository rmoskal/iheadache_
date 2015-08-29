/**
 * @(#) Headache.java
 */

package com.betterqol.iheadache.model;


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;

import com.betterqol.iheadache.extensions.JacksonJoda;
import com.betterqol.iheadache.extensions.JacksonJoda2;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Headache extends BaseEntity
{
	private String user;
	private DateTime start;
	private DateTime end;
	private List<HeadachePain> pains;
	private List<LookupDTO> triggers;
	private List<HeadacheTreatment>treatments;
	private Disability disability;
	private List<YesNoResponse> symptoms;
	@TypeDiscriminator
	private LookupDTO kind;
	private boolean fromYesterday;
	private boolean noHeadache;
	private boolean atBedtime;
	private boolean recordComplete;
	private DateTime latEdited;
	private int MIDAS;
	private String note;
	private boolean diaryHeadacheComplete;
	
	public DateTime getLatEdited() {
		return latEdited;
	}
	public void setLatEdited(DateTime latEdited) {
		this.latEdited = latEdited;
	}
	/**
	 * @return 
	 */
	public String getUser() {
		return user;
	}
	/**
	 * @param user
	 */
	public void setUser(String user) {
		this.user = user;
	}
	/**
	 * @return 
	 */
	public List<HeadachePain> getPains() {
		return pains;
	}
	/**
	 * @param pains
	 */
	
	public void setPains(List<HeadachePain> pains) {
		this.pains = pains;
	}
	/**
	 * @return 
	 */

	/**
	 * @return 
	 */
	public Disability getDisability() {
		return disability;
	}
	/**
	 * @param disability
	 */
	public void setDisability(Disability disability) {
		this.disability = disability;
	}
	/**
	 * @return 
	 */
	public List<YesNoResponse> getSymptoms() {
		return symptoms;
	}
	/**
	 * @param oo
	 */
	public void setSymptoms(List<YesNoResponse>oo) {
		symptoms = oo;
	}
	/**
	 * @return 
	 */
	public LookupDTO getKind() {
		return kind;
	}
	/**
	 * @param kind
	 */
	public void setKind(LookupDTO kind) {
		this.kind = kind;
	}
	/**
	 * @return 
	 */
	@JsonSerialize(using = JacksonJoda.class)
	public DateTime getStart() {
		return start;
	}
	/**
	 * @param start
	 */
	@JsonDeserialize(using = JacksonJoda2.class)
	public void setStart(DateTime start) {
		this.start = start;
	}
	/**
	 * @return 
	 */
	@JsonSerialize(using = JacksonJoda.class)
	public DateTime getEnd() {
		return end;
	}
	/**
	 * @param end
	 */
	@JsonDeserialize(using = JacksonJoda2.class)
	public void setEnd(DateTime end) {
		this.end = end;
	}
	/**
	 * @return 
	 */
	public boolean isFromYesterday() {
		return fromYesterday;
	}
	/**
	 * @param fromYesterday
	 */
	public void setFromYesterday(boolean fromYesterday) {
		this.fromYesterday = fromYesterday;
	}
	/**
	 * @return 
	 */
	public boolean isNoHeadache() {
		return noHeadache;
	}
	/**
	 * @param noHeadache
	 */
	public void setNoHeadache(boolean noHeadache) {
		this.noHeadache = noHeadache;
	}
	/**
	 * @return 
	 */
	public boolean isAtBedtime() {
		return atBedtime;
	}
	
	/**
	 * @return
	 */
	public boolean isDiaryHeadacheComplete(){
		return diaryHeadacheComplete;
	}
	/**
	 * @param atBedtime
	 */
	public void setAtBedtime(boolean atBedtime) {
		this.atBedtime = atBedtime;
	}
	public List<LookupDTO> getTriggers() {
		return triggers;
	}
	public void setTriggers(List<LookupDTO> triggers) {
		this.triggers = triggers;
	}
	public List<HeadacheTreatment> getTreatments() {
		return treatments;
	}
	public void setTreatments(List<HeadacheTreatment> treatments) {
		this.treatments = treatments;
	}
	public void setRecordComplete(boolean recordComplete) {
		this.recordComplete = recordComplete;
	}
	public boolean isRecordComplete() {
		return recordComplete;
	}
	public int getMIDAS() {
		return MIDAS;
	}
	public void setMIDAS(int midasScore) {
		this.MIDAS = midasScore;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
	public void setDiaryHeadacheComplete(boolean diaryHeadacheComplete){
		this.diaryHeadacheComplete = diaryHeadacheComplete;
	}

}
