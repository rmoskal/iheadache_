package com.betterqol.iheadache.admin;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.ektorp.Page;
import org.ektorp.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.repository.HealthcarePrincipalRepository;
import com.betterqol.iheadache.repository.UserPrincipalRepository;
import com.betterqol.iheadache.resource.AdminResource;


@Component
@Path("/app/service/admin")
@SuppressWarnings({ "rawtypes", "unchecked" })
@RolesAllowed(value = { "ROLE_USER" })
public class AdminService implements AdminResource {
	
	@Autowired
	private UserPrincipalRepository userRepo;
	
	@Autowired
	private HealthcarePrincipalRepository hcprepo;

	@Override
	public Map getUsers(@QueryParam("pageLink") String pageLink)  {

			PageRequest pr = pageLink != null ? PageRequest.fromLink(pageLink)
					: PageRequest.firstPage(50);

			Page<UserPrincipal> in = userRepo.getPaged(pr);
			Map results = new HashMap();
			results.put("payload", in.getRows());
			if (in.isHasNext())
				results.put("nextLink", in.getNextLink());
			if (in.isHasPrevious())
				results.put("previousLink", in.getPreviousLink());
			results.put("total", in.getTotalSize());
			return results;
	}

	@Override
	public Map getHcpUsers(@QueryParam("pageLink") String pageLink) {
		
		PageRequest pr = pageLink != null ? PageRequest.fromLink(pageLink)
			: PageRequest.firstPage(50);

		Page<HealthcarePrincipal> in = hcprepo.getPaged(pr, true);
		Map results = new HashMap();
		results.put("payload", in.getRows());
		if (in.isHasNext())
			results.put("nextLink", in.getNextLink());
		if (in.isHasPrevious())
			results.put("previousLink", in.getPreviousLink());
		results.put("total", in.getTotalSize());
		return results;
	}
	
	
	@Override
	public Map getUnApprovedHcpUsers(@QueryParam("pageLink") String pageLink) {
		
		PageRequest pr = pageLink != null ? PageRequest.fromLink(pageLink)
			: PageRequest.firstPage(50);

		Page<HealthcarePrincipal> in = hcprepo.getPaged(pr, false);
		Map results = new HashMap();
		results.put("payload", in.getRows());
		if (in.isHasNext())
			results.put("nextLink", in.getNextLink());
		if (in.isHasPrevious())
			results.put("previousLink", in.getPreviousLink());
		results.put("total", in.getTotalSize());
		return results;
	}

}
