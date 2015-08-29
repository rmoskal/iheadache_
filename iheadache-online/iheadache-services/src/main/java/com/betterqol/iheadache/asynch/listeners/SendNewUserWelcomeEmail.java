package com.betterqol.iheadache.asynch.listeners;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.AppHelpers;
import com.betterqol.iheadache.asynch.events.UserAdded;
import com.betterqol.iheadache.email.EmailService;
import com.google.common.collect.Maps;

@Component
public class SendNewUserWelcomeEmail  implements ApplicationListener <UserAdded> {
	
	@Autowired
	EmailService emailService;
	
	private static final Logger logger = LoggerFactory.getLogger(AppHelpers.MESSAGE_BUS_LOG);

	@Override
	public void onApplicationEvent(UserAdded event) {
	    logger.info("Recieved new user registration" + event.getPrincipal().getName());
		Map<Object, Object> model = Maps.newHashMap();
		model.put("user", event.getPrincipal());
        emailService.sendMessage("/welcome.vm", "Welcome to IHeadache", event.getPrincipal().getName(), model);
        logger.info("Sent welcome email" + event.getPrincipal().getName());
		
	}

}
