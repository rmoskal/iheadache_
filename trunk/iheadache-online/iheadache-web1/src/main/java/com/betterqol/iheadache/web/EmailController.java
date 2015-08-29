package com.betterqol.iheadache.web;

import java.text.ParseException;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.betterqol.iheadache.email.EmailService;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.profile.ProfileService;
import com.betterqol.iheadache.repository.UserPrincipalRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

@Controller
public class EmailController {
	
	@Autowired
	private Properties appProperties;
	
	@Autowired
	UserPrincipalRepository pr;
	
	@Autowired
	private VelocityEngine ve;
	
	private String baseTemplate = "base_template.vm";
	
	
	@ResponseBody
    @RequestMapping(value="/{remainder:.*}.email", method = RequestMethod.GET)
    public String createUser( HttpServletRequest request) throws ParseException {
		
		//return request.getServletPath().substring(1);
		
		String userId = request.getParameter("id");
		
		UserPrincipal user = pr.get(userId);
		
		Map<Object, Object> model = Maps.newHashMap();
		model.putAll(appProperties);
		model.put("user", user);
			
		String body = VelocityEngineUtils.mergeTemplateIntoString(ve, "welcome.vm",
				model);
		String wrapper = VelocityEngineUtils.mergeTemplateIntoString(ve, 
		baseTemplate,ImmutableMap.of("content",body));
		return wrapper;
			
		
	}

}
