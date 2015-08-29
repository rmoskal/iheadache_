package com.betterqol.iheadache.model.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.betterqol.iheadache.model.LookupDTO;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ProfileItem extends LookupDTO  {
	
	private boolean in;
	private boolean custom;
	


	public ProfileItem(String id, String description,boolean in, boolean custom) {
		super(id, description);
		this.in = in;
		this.custom = custom;
	}

	public ProfileItem () {
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
		return "ProfileItem [in=" + in + ", custom=" + custom + ", getId()="
				+ getId() + ", getDescription()=" + getDescription() + "]";
	}

}
