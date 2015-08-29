package com.betterqol.iheadache.controller;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ektorp.CouchDbConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJacksonJsonView;

import com.betterqol.iheadache.IHeadacheDataLoader;
import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.profile.ProfileService;
import com.betterqol.iheadache.repository.HealthcarePrincipalRepository;

@Controller
public class AdminController {
	
	@Autowired
	@Qualifier("headacheDatabase")
	CouchDbConnector db;
	
	@Autowired
	ProfileService pService;
	
	@Autowired
	HealthcarePrincipalRepository hcepo;
	
	final Logger logger = LoggerFactory.getLogger(AdminController.class);
	

	@RequestMapping(value="/")
	public ModelAndView test(HttpServletResponse response) throws IOException{
		return new ModelAndView("home");
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value="/deleteUser.svc",method = RequestMethod.POST)
	public Map deleteUser(HttpServletRequest request) throws IOException{
		
		String userId = request.getParameter("userId");
		pService.deleteUserCompletely(userId);
		
		Map o = new HashMap();
		o.put("success", true);
		o.put("result", "Gone!");
		return o;
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value="/deleteHcpUser.svc",method = RequestMethod.POST)
	public Map deleteHcpUser(HttpServletRequest request) throws IOException{
		
		String userId = request.getParameter("userId");
		pService.deleteHCPCompletely(userId);
		
		Map o = new HashMap();
		o.put("success", true);
		o.put("result", "Gone!");
		return o;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value="/approveHcpUser.svc",method = RequestMethod.POST)
	public Map approveHcpUser(HttpServletRequest request) throws IOException{
		
		String userId = request.getParameter("userId");
		HealthcarePrincipal hcp = hcepo.get(userId);
		hcp.setApproved(true);
		hcepo.update(hcp);
		
		Map o = new HashMap();
		o.put("success", true);
		o.put("result", "Approved!");
		return o;
		
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ResponseBody
	@RequestMapping(value="/import.svc")
	public Map importLookups(HttpServletRequest request) throws IOException{
		
		String in = request.getParameter("json");
		
		IHeadacheDataLoader loader = new IHeadacheDataLoader(db);
		loader.load(new StringReader(in));
		
		Map o = new HashMap();
		o.put("success", true);
		o.put("result", "Import Successful!");
		return o;
	}
}