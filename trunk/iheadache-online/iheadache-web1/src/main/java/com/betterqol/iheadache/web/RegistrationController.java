package com.betterqol.iheadache.web;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.betterqol.iheadache.email.EmailService;
import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.profile.ProfileService;
import com.betterqol.iheadache.repository.UserPrincipalRepository;
import com.betterqol.iheadache.security.DuplicateUsernameException;
import com.betterqol.iheadache.security.PasswordResetException;
import com.betterqol.iheadache.security.PasswordResetUrlHelper;
import com.google.common.collect.Maps;


@Controller
public class RegistrationController {
	
	@Autowired
	ProfileService profileService;
	
	@Autowired
	UserPrincipalRepository pr;
	
	@Autowired
	PasswordResetUrlHelper pwHelper;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	private MessageDigestPasswordEncoder encoder;
	

	
	final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

	@ResponseBody
    @RequestMapping("/test.signup")
    public String helloWorld() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("helloWorld");
        mav.addObject("message", "Hello World!");
        return "hello you";
    }
	
	@ResponseBody
    @RequestMapping(value="/user.signup", method = RequestMethod.POST)
    public String createUser( HttpServletRequest request) throws ParseException {
		
		String strck = request.getParameter("email");
		logger.info("string check " + strck);
		logger.info(Locale.getDefault().toString());
       
        if (! checkCaptcha(request))
        	return "CAPTCHA";
        
        UserPrincipal p = new UserPrincipal();
        p.setFirstName(request.getParameter("firstname"));
        p.setLastName(request.getParameter("lname"));
        p.setName(request.getParameter("email").trim().toLowerCase(Locale.ENGLISH));
        p.setAddress(request.getParameter("address"));
        p.setCity(request.getParameter("city"));
        p.setState(request.getParameter("state"));
        p.setCountry(request.getParameter("country"));
        p.setZipcode(request.getParameter("zip"));
        SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
        p.setBirthdate(new DateTime(formatter.parse(request.getParameter("bdate"))));
        p.setGender(request.getParameter("gender"));
        p.setPassword(request.getParameter("password").trim());
        
    	try {
			profileService.registerRegularUser(p);
			logger.info("Created regular user " + p.getName());
		} catch (DuplicateUsernameException e) {
			return ("DUPLICATE");
		}
        
    	
        return "OK";
    }
	
	@ResponseBody
    @RequestMapping(value="/hcp.signup", method = RequestMethod.POST)
    public String createHCUser( HttpServletRequest request) throws ParseException {
		   
       if (! checkCaptcha(request))
    	   return "CAPTCHA";  
        
        HealthcarePrincipal p = new HealthcarePrincipal();
        p.setFirstName(request.getParameter("firstname"));
        p.setLastName(request.getParameter("lname"));
        p.setName(request.getParameter("email").toLowerCase(Locale.ENGLISH));
        p.setAddress1(request.getParameter("address"));
        p.setAddress2(request.getParameter("address2"));
        p.setCity(request.getParameter("city"));
        p.setState(request.getParameter("state"));
        p.setZipcode(request.getParameter("zip"));
        p.setCountry(request.getParameter("country"));
        SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy");
        p.setBirthdate(new DateTime(formatter.parse(request.getParameter("bdate"))));
        p.setGender(request.getParameter("gender"));
        p.setPassword(request.getParameter("password")); 
        p.setInstitution(request.getParameter("pname"));
        p.setPhone(request.getParameter("phone"));
        p.setFax(request.getParameter("fax"));
        p.setWebsite(request.getParameter("website"));
        p.setDegree(request.getParameter("degree"));
        if (StringUtils.isNotBlank(request.getParameter("other_degree")))
        	p.setDegree(request.getParameter("other_degree"));
        p.setSpecialty(request.getParameter("specialty"));
        if (StringUtils.isNotBlank(request.getParameter("other_specialty")))
        	 p.setSpecialty(request.getParameter("other_specialty"));
        
        p.setInclude_in_physician_finder(Boolean.parseBoolean(request.getParameter("include_in_physician_finder")));
        p.setUcns_certified_in_Headache(Boolean.parseBoolean(request.getParameter("ucns_certified_in_Headache")));
        p.setAmerican_academy_of_neurology(Boolean.parseBoolean(request.getParameter("american_academy_of_neurology")));
        p.setAmerican_headache_society(Boolean.parseBoolean(request.getParameter("american_headache_society")));
        p.setHeadache_cooperative_of_new_england(Boolean.parseBoolean(request.getParameter("headache_cooperative_of_new_england")));
        p.setHeadache_cooperative_of_the_pacific(Boolean.parseBoolean(request.getParameter("headache_cooperative_of_the_pacific")));
        p.setNational_headache_foundation(Boolean.parseBoolean(request.getParameter("national_headache_foundation")));
        p.setSouthern_headache_society(Boolean.parseBoolean(request.getParameter("southern_headache_society")));
        
        p.setAmerican_academy_of_neurology_meeting(request.getParameter("american_academy_of_neurology_meeting"));
        p.setAmerican_headache_society_meeting(request.getParameter("american_headache_society_meeting"));
        p.setHeadache_cooperative_of_new_england_meeting(request.getParameter("headache_cooperative_of_new_england_meeting"));
        p.setHeadache_cooperative_of_the_pacific_meeting(request.getParameter("headache_cooperative_of_the_pacific_meeting"));
        p.setNational_headache_foundation_meeting(request.getParameter("national_headache_foundation_meeting"));
        p.setSouthern_headache_society_meeting(request.getParameter("southern_headache_society_meeting"));

        p.setPatient_brochures(request.getParameter("patient_brochures"));
        p.setBrochure_holders(request.getParameter("brochure_holders"));
        p.setPatient_cards(request.getParameter("patient_cards"));
        p.setCard_holders(request.getParameter("card_holders"));
        
        	
        
    	try {
			profileService.registerHealthcareUser(p);
		} catch (DuplicateUsernameException e) {
			return ("DUPLICATE");
		}
        
    	
        return "OK";
    }
	
	
	private static boolean checkCaptcha( HttpServletRequest request) {
		
		String remoteAddr = request.getRemoteAddr();
	    String challenge = request.getParameter("recaptcha_challenge_field");
	    String uresponse = request.getParameter("recaptcha_response_field");
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
        reCaptcha.setRecaptchaServer("https://google.com/recaptcha/api");  
        reCaptcha.setPrivateKey("6LdG3s0SAAAAABScbTecIUYZaOAWu8i6ooa4WMZp");

        ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

        return reCaptchaResponse.isValid();		
		
	}
	
}