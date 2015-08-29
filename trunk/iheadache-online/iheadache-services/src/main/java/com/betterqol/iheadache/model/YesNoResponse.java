package com.betterqol.iheadache.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class YesNoResponse {
	
	private boolean yes;
	private boolean no;
	private String id;
	private String description;
	
	public YesNoResponse() {
		
	}
	
	public YesNoResponse(IDescription item) {
		
		id = item.getId();
		description = item.getDescription();
	}
	
	public YesNoResponse(IDescription item, boolean yes, boolean no) {
		
		id = item.getId();
		description = item.getDescription();
		this.yes = yes;
		this.no = no;	
	}
	
	public YesNoResponse(String id, boolean yes, boolean no) {
		
		this.id = id;
		this.yes = yes;
		this.no = no;
		this.description = id;	
	}
	
	public boolean isYes() {
		return yes;
	}
	public void setYes(boolean yes) {
		this.yes = yes;
	}
	public boolean isNo() {
		return no;
	}
	public void setNo(boolean no) {
		this.no = no;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return "YesNoResponse [yes=" + yes + ", no=" + no + ", description="
				+ description + "]";
	}
	

}
