package com.betterqol.iheadache.asynch.events;

import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.UserInformation;

public class HcpAssociated {
	public UserInformation ui;
	public HealthcarePrincipal hcp;
	
	public HcpAssociated(UserInformation ui,HealthcarePrincipal hcp) {
		this.ui = ui;
		this.hcp = hcp;
	}

}
