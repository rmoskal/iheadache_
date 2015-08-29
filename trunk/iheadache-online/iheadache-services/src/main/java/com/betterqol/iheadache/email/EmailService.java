package com.betterqol.iheadache.email;

import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.google.common.collect.ImmutableMap;

@Component
public class EmailService {

	final Logger log = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private JavaMailSender sender;
	@Autowired
	private VelocityEngine ve;
	@Autowired
	private Properties appProperties;
	
	private String baseTemplate = "base_template.vm";

	@Async
	public void sendMessage(String template, String subject, String to,
			Map<Object, Object> model) {
		try {

			MimeMessage message = createMessage(template, subject, to, model);
			sender.send(message);
			log.info(String.format("Sent %s message", subject));
		} catch (MessagingException e) {
			log.error("Unable to send email!", e);
		}
		

	}

	public MimeMessage createMessage(String template, String subject,
			String to, Map<Object, Object> model) throws MessagingException {

		model.putAll(appProperties);
		
		MimeMessage message = sender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, false);
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setFrom(appProperties.getProperty("fromEmail"));
		try {
			String body = VelocityEngineUtils.mergeTemplateIntoString(ve, template,
				model);
			String wrapper = VelocityEngineUtils.mergeTemplateIntoString(ve, 
					baseTemplate,ImmutableMap.of("content",body));
			helper.setText(wrapper, true);
		}catch(ResourceNotFoundException e) {
			throw new MessagingException(String.format("Unable to find template named %s",template),e);
		}
		
		return message;

	}
	

	public Properties getAppProperties() {
		return appProperties;
	}

}
