package com.betterqol.iheadache.security;


import static org.junit.Assert.assertNotNull;

import java.security.GeneralSecurityException;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.repository.UserPrincipalRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class PasswordResetUrlHelperTest {
	
	
	@Autowired
	PasswordResetUrlHelper o_to_test;
	
	@Autowired
	UserPrincipalRepository repo;
	
	@Before
	public void setup() {
		repo.add(TestHelpers.makePrincipal("foo"));
		repo.add(TestHelpers.makePrincipal("r_foo@foo.com"));
		
	}
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
	 }
	
	@Test
	public void testFull() throws PasswordResetException, GeneralSecurityException {
		
		String res = o_to_test.encodeEmail("foo");
		UserPrincipal u = o_to_test.checkUrl(res);
		assertNotNull(u);	
	}
	
	@Test
	public void testRegressName() throws PasswordResetException, GeneralSecurityException {
		
		String res = o_to_test.encodeEmail("r_foo@foo.com");
		UserPrincipal u = o_to_test.checkUrl(res);
		assertNotNull(u);	
	}
	

	public void testExpiry() throws PasswordResetException, GeneralSecurityException {	
		String res = o_to_test.encodeEmail("foo");
		DateTime expires = new DateTime();
		expires = expires.plusHours(23);
		UserPrincipal u = o_to_test.checkUrl(res,expires);
		assertNotNull(u);
	}
	
	@Test(expected= IllegalArgumentException.class)
	public void testFail() throws PasswordResetException, GeneralSecurityException {	
		o_to_test.checkUrl("POOO");
	}
	
	@Test(expected= PasswordResetException.class)
	public void testExpires() throws PasswordResetException, GeneralSecurityException {	
		String res = o_to_test.encodeEmail("foo");
		DateTime expires = new DateTime();
		expires = expires.plusHours(25);
		o_to_test.checkUrl(res,expires);
	}
	
	@Test(expected= PasswordResetException.class)
	public void testExpires1() throws PasswordResetException, GeneralSecurityException {	
		String res = o_to_test.encodeEmail("foo");
		DateTime expires = new DateTime();
		expires = expires.plusDays(2);
		o_to_test.checkUrl(res,expires);
	}
	
	
	@Test
	public void regressHCPName(){
		
		String hash = "c332d80b621119773cf3b903edb46bf73f805ac73a75c6e76d816b30082f88672f52afcfd4b65fd6";
		String res = o_to_test.decryptBlowfish(hash, "secret_salt");
		System.out.println(res);
		String[] p = res.split("\\\\--#-\\\\");
		DateTime t = new DateTime(Long.parseLong(p[1]));
		System.out.println(t);
		
		
	}
	
	

}
