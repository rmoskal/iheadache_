package com.betterqol.iheadache.model.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.betterqol.iheadache.model.Treatment;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TreatmentProfileItem extends Treatment  {
	
	private boolean in;
	private boolean custom;
	
	
	
	/**
	 * Constructor for test cases only
	 * @param in
	 * @param custom
	 * @param all
	 */
	public TreatmentProfileItem(boolean in, boolean custom, String all) {
		super(all, all, all, all,all, false);
		this.in = in;
		this.custom = custom;
	}
	

	public TreatmentProfileItem() {
		
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
		return "TreatmentProfileItem [in=" + in + ", custom=" + custom
				+ ", getDescription()=" + getDescription() + ", getForm()="
				+ getForm() + ", getUom()=" + getUom()
				+ ", isMigraineTreatment()=" + isMigraineTreatment()
				+ ", getGenericName()=" + getGenericName() + ", getId()="
				+ getId() + "]";
	}
	

}
