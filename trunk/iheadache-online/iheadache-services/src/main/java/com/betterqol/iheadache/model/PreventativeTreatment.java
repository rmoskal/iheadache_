package com.betterqol.iheadache.model;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;


public class PreventativeTreatment extends BaseEntity implements IDescription {
	@TypeDiscriminator
	private String ptname;
	
	public PreventativeTreatment() {
		super();
	}
	
	public PreventativeTreatment(String ptname, String genericName) {
		super();
		this.ptname = ptname;
		this.genericName = genericName;
	}
	
	public PreventativeTreatment(String id,String ptname, String genericName) {
		super();
		this.setId(id);
		this.ptname = ptname;
		this.genericName = genericName;
	}

	private String genericName;
	
	@JsonProperty("ptname")
	@Override
	public String getDescription() {
		return ptname;
	}

	@JsonProperty("ptname")
	public void setDescription(String description) {
		this.ptname = description;
	}
	
	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

}
