package com.betterqol.iheadache.email;

import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.google.common.collect.ImmutableMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml" })
public class EmailSenderTest {
	
	 @Autowired
	 private JavaMailSender sender;
	 @Autowired
	 private VelocityEngine ve;
	 

	 
	 @Test
	 public void sendDemo() throws MessagingException {
		 
		 MimeMessage message = sender.createMimeMessage();
	     MimeMessageHelper helper = new MimeMessageHelper(message, false);
	     helper.setTo("rmoskal@mostmedia.com");
	     helper.setSubject("Demo");
	     Map<String, String> vars = ImmutableMap.of("test","hey ho");
	     String body = VelocityEngineUtils.mergeTemplateIntoString(ve, "/test.vm", vars);
	     assertTrue(body.contains("hey ho"));
	     helper.setText(body,true);
	     sender.send(message);
		 
	 }

}
