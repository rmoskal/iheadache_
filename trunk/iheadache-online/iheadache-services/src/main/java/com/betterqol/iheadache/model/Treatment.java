/**
 * @(#) Treatment.java
 */

package com.betterqol.iheadache.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.ektorp.support.TypeDiscriminator;

@XmlRootElement
public class Treatment extends BaseEntity implements IDescription 
{

	private String description;
	private String genericName;
	private String form;
	@TypeDiscriminator
	private String uom;
	private boolean migraineTreatment;
	
	public Treatment(){
		
	}
	
	public Treatment(String id) {
		super();
		this.setId(id);
		this.description = id;

	}
	
	public Treatment(String description, String form, String uom,
			boolean migraineTratment) {
		super();
		this.description = description;
		this.form = form;
		this.uom = uom;
		this.migraineTreatment = migraineTratment;
	}
	
	
	public Treatment(String id, String description, String genericName, String form, String uom,
			boolean migraineTratment) {
		super();
		this.setId(id);
		this.description = description;
		this.form = form;
		this.uom = uom;
		this.migraineTreatment = migraineTratment;
		this.genericName = genericName;
	}
	

	/**
	 * @return 
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return 
	 */
	public String getForm() {
		return form;
	}
	/**
	 * @param form
	 */
	public void setForm(String form) {
		this.form = form;
	}
	/**
	 * @return 
	 */
	public String getUom() {
		return uom;
	}
	/**
	 * @param uom
	 */
	public void setUom(String uom) {
		this.uom = uom;
	}
	/**
	 * @return 
	 */
	public boolean isMigraineTreatment() {
		return migraineTreatment;
	}
	/**
	 * @param migraineTratment
	 */
	public void setMigraineTreatment(boolean migraineTratment) {
		this.migraineTreatment = migraineTratment;
	}
	

	public String getGenericName() {
		return genericName;
	}

	public void setGenericName(String genericName) {
		this.genericName = genericName;
	}

	public static Treatment minimal(String id, String description) {
		Treatment t = new Treatment();
		t.setId(id);
		t.setDescription(description);
		return t;
	}
	
}
