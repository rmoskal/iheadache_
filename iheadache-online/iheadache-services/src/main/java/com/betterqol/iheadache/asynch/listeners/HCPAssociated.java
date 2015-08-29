package com.betterqol.iheadache.asynch.listeners;

import java.util.Map;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.AppHelpers;
import com.betterqol.iheadache.asynch.events.HealthcareUserAdded;
import com.betterqol.iheadache.email.EmailService;
import com.google.common.collect.Maps;

@Component
public class HCPAssociated implements ApplicationListener <HealthcareUserAdded>{
	
	@Autowired
	EmailService emailService;
	
	private static final Logger logger = LoggerFactory.getLogger(AppHelpers.MESSAGE_BUS_LOG);
	
	@Override
	@Async
	public void onApplicationEvent(HealthcareUserAdded event) {
		logger.info("Recieved new hcp registration " + event.getPrincipal().getName());
		Map<Object, Object> model = Maps.newHashMap();
		model.put("user", event.getPrincipal());
		model.put("now", new DateTime());
		String sendTo = (String) emailService.getAppProperties().get("notifyEmail");
		emailService.sendMessage("/notify_mgt.vm", "", sendTo,model);
		
	}

}
