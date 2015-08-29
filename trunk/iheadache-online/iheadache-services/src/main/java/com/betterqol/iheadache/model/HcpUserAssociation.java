package com.betterqol.iheadache.model;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.ektorp.support.TypeDiscriminator;

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown=true)
public class HcpUserAssociation extends BaseEntity{
	
	private String userId;
	@TypeDiscriminator
	private String hcpId;
	
	public HcpUserAssociation() {
		
	}
	
	public HcpUserAssociation(String userId, String hcpId) {
		super();
		this.userId = userId;
		this.hcpId = hcpId;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHcpId() {
		return hcpId;
	}
	public void setHcpId(String hcpId) {
		this.hcpId = hcpId;
	}

}
