package com.betterqol.iheadache.resource;

import java.util.Date;
import java.util.List;

import org.ektorp.Page;
import org.ektorp.PageRequest;

import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.security.HeadacheSecurityPrincipal;

/***
 * Fucking java makes me do this
 * @author rob
 *
 */
public interface MiniHeadacheRepo {
	
	List<Headache> getDateRange(String userId, Date startDate,Date endDate);
	HeadacheSecurityPrincipal getUser();
	Page<Headache> getPagedDateRange(String userId, Date startDate,
			Date endDate, PageRequest pageRequest);


}
