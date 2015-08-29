package com.betterqol.iheadache.web;

import java.text.ParseException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

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
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.repository.UserPrincipalRepository;
import com.betterqol.iheadache.security.PasswordResetException;
import com.betterqol.iheadache.security.PasswordResetUrlHelper;
import com.google.common.collect.Maps;

@Controller
public class LostPasswordController {
	
	@Autowired
	UserPrincipalRepository pr;
	
	@Autowired
	PasswordResetUrlHelper pwHelper;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	private MessageDigestPasswordEncoder encoder;
	
	
	final Logger logger = LoggerFactory.getLogger(LostPasswordController.class);

	
	
	@ResponseBody
    @RequestMapping(value="/recoverpw", method = RequestMethod.POST)
    public String recoverPw( HttpServletRequest request) throws ParseException {
		
        if (! checkCaptcha(request))
        	return "[\"CAPTCHA\", \"\"]";
        
        String email = request.getParameter("email").toLowerCase(Locale.ENGLISH);
		UserPrincipal u = pr.findByName(email);
        if (u == null)
        	return String.format("[\"UNKNOWN_USER\", \"%s\"]",email);
        
		Map<Object, Object> model = Maps.newHashMap();
		model.put("fancyUrl", pwHelper.encodeEmail(u.getName()));
		model.put("firstname", u.getFirstName());
        
        emailService.sendMessage("/lost_pw.vm", "Iheadache Password Change Request", u.getName(), model);
        
        return String.format("[\"OK\",\"%s\"]", email);
		
	}
	
	@ResponseBody
    @RequestMapping(value="/recoverpw2", method = RequestMethod.POST)
    public String recoverPw2( HttpServletRequest request) throws ParseException {
		
        
        String email = (String) request.getSession().getAttribute("_principal");
		UserPrincipal u = pr.findByName(email);
        if (u == null)
        	return String.format("[\"UNKNOWN_USER\", \"%s\"]",email);
        
		Map<Object, Object> model = Maps.newHashMap();
		model.put("fancyUrl", pwHelper.encodeEmail(u.getName()));
		model.put("firstname", u.getFirstName());
        emailService.sendMessage("/lost_pw.vm", "Iheadache Password Change Request", u.getName(), model);
        return String.format("[\"OK\",\"%s\"]", email);
		
	}
	
    @RequestMapping(value="/changepw", method = RequestMethod.GET)
	public ModelAndView recoverPwForm(HttpServletRequest request) {
    	
    	ModelAndView modelAndView = new ModelAndView("reg-newpass.html");
    	UserPrincipal principal = null;
		try {
			principal = pwHelper.checkUrl(request.getParameter("hash"));
		} catch (PasswordResetException e) {
			request.getSession().setAttribute("_principal", e.getMessage());
			return new ModelAndView("reg-expired.html", "principal", e.getMessage());
		}
		catch (IllegalArgumentException e) {
			return new ModelAndView("reg-error.html", "APP_Exception_message",  e.getMessage());
		}

		request.getSession().setAttribute("_principal", principal.getName());
		modelAndView.addObject("principal", principal);
		modelAndView.addObject("hash", request.getParameter("hash"));
		return modelAndView;
	}
    
	@ResponseBody
    @RequestMapping(value="/changepw", method = RequestMethod.POST)
	public String changePw(HttpServletRequest request)  {
		
		String hash = request.getParameter("hash");
		UserPrincipal principal = null;
		try {
			principal = pwHelper.checkUrl(hash);
		} catch (PasswordResetException e) {
			 request.getSession().setAttribute("_principal", e.getMessage());
			 return String.format("[\"EXPIRED\",\"%s\"]",e.getMessage() );
		}
		catch (IllegalArgumentException e) {
			
			return String.format("[\"ERROR\",\"%s\"hcp.signup]",  "");
			
		}
		String password = request.getParameter("password");
		principal.setPassword(encoder.encodePassword(password, null));
		pr.update(principal);
		logger.info("Changed pw for " + principal.getName());
		
		
	    return String.format("[\"OK\",\"%s\"]",  principal.getName());
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
