package com.betterqol.iheadache.model.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.betterqol.iheadache.model.PreventativeTreatment;

@JsonIgnoreProperties(ignoreUnknown=true)
public class PreventativeTreatmentProfileItem extends  PreventativeTreatment {
	
	
	private boolean in;
	private boolean custom;
	/**
	 * Constructor for test cases only
	 * @param in
	 * @param custom
	 * @param all
	 */
	public PreventativeTreatmentProfileItem(boolean in, boolean custom, String all) {
		super(all, all, all);
		this.in = in;
		this.custom = custom;
	}
	
	public PreventativeTreatmentProfileItem() {
		super();
	}
	
	public boolean isIn() {
		return in;
	}
	public void setIn(boolean in) {
		this.in = in;
	}
	public boolean isCustom() {
		return custom;
	}
	public void setCustom(boolean custom) {
		this.custom = custom;
	}
	
	@Override
	public String toString() {
		return "PreventativeTreatmentProfileItem [in=" + in + ", custom="
				+ custom + ", getDescription()=" + getDescription()
				+ ", getGenericName()=" + getGenericName() + ", getId()="
				+ getId() + ", getRevision()=" + getRevision()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}

}
