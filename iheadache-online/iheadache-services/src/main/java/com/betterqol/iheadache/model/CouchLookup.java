package com.betterqol.iheadache.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.ektorp.support.TypeDiscriminator;

/**
 * Represents a name value pair in the couch db
 * @author rob
 *
 */

@XmlRootElement
public  class CouchLookup extends BaseEntity implements IDescription {
	
	public static enum Kind{
		DISABILITY_QUESTION,
		HEADACHE_TYPE,
		PAIN_LOCATION,
		PAIN_TYPE,
		SYMPTOM,
		TRIGGER		
	}

	
	private String description;
	@TypeDiscriminator
	public  Kind lookupType;

	public CouchLookup(Kind type) {
		
		lookupType = type;
	}
	
	public CouchLookup() {
	}
	
	public CouchLookup(Kind type,String description) {
		lookupType = type;
		this.description = description;
	}
	
	public CouchLookup(Kind type,String description, String id) {
		lookupType = type;
		this.description = description;
		super.setId(id);
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public void setLookupType(Kind type) {
		lookupType = type;
	}
	
	public  Kind GetLookupType(){
		return lookupType;
	}

}
