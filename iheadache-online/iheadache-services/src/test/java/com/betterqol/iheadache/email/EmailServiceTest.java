package com.betterqol.iheadache.email;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import javax.mail.MessagingException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Maps;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml" })
public class EmailServiceTest {
	
	final Logger log = LoggerFactory.getLogger(EmailService.class);

	@Autowired
	private EmailService emailService;

	@Test
	public void testMin() {
		assertNotNull(emailService);
	}

	@Test
	public void testPropertyCopying() throws MessagingException {
		Map<Object, Object> model = Maps.newHashMap();
		model.put("test", "hey ho");

		emailService.createMessage("/test.vm", "Test",
				"rmoskal@mostmedia.com", model);
		
		assertTrue(model.size()>1);
	}
	
	@Test(expected=MessagingException.class)
	public void testMissingTemplate() throws MessagingException {
		Map<Object, Object> model = Maps.newHashMap();
		model.put("test", "hey ho");

		emailService.createMessage("/foo.vm", "Test",
				"rmoskal@mostmedia.com", model);	
	}
	
	@Test
	public void testSend()  {
		Map<Object, Object> model = Maps.newHashMap();
		model.put("test", "hey ho");
		String to = (String) emailService.getAppProperties().get("notifyEmail");
		
		emailService.sendMessage("/test.vm", "Test",to, model);	
		log.info("Should be first");
	}

}
