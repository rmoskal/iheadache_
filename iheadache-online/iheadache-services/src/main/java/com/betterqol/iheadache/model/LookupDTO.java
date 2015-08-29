package com.betterqol.iheadache.model;

import java.util.List;
import java.util.ListIterator;

public class LookupDTO implements IDescription{

	private String id;
	private String description;
	

	public LookupDTO(){
		
	}
	
	
	public LookupDTO(String id, String description) {
		super();
		this.id = id;
		this.description = description;
	}	
	
	
	public LookupDTO(IDescription o) {
		super();
		this.id = o.getId();
		this.description = o.getDescription();
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
	
	
	public static void enlist(StringBuffer b, List<LookupDTO> items) {
		
		ListIterator <LookupDTO>itype = items.listIterator();
		while (itype.hasNext()) {	
			LookupDTO ptype = itype.next();
			b.append(ptype.getDescription());
			if (itype.hasNext())
				b.append(", ");
		}
	}

}
