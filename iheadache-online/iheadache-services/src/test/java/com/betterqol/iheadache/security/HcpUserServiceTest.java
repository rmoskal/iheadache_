package com.betterqol.iheadache.security;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.repository.HealthcarePrincipalRepository;
import com.betterqol.iheadache.repository.PTRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class HcpUserServiceTest {
	
	@Autowired
	HcpUserService userService;
	@Autowired
	HealthcarePrincipalRepository repo;
	@Autowired
	PTRepository ptRepo;

		
	@After
	 public void cleanup() {
		 repo.deleteAll();
		 ptRepo.deletAll();
	 }
	
	@Test
	public void testGet() {
		
		repo.add(TestHelpers.makeHcPrincipal("foo", true, "Smith"));
		UserDetails user = userService.loadUserByUsername("foosmith");
		assertEquals("acbd18db4cc2f85cedef654fccc4a4d8",user.getPassword());
		assertEquals("foosmith",user.getUsername());
		
		ArrayList auth = new ArrayList(user.getAuthorities());
		
		assertEquals("ROLE_USER",auth.get(0).toString());

	}
	
	@Test (expected= UsernameNotFoundException.class)
	public void testGetNotApproved() {
		
		repo.add(TestHelpers.makeHcPrincipal("foo", false, "Smith"));
		userService.loadUserByUsername("foo");

	}
	
	
	@Test (expected= UsernameNotFoundException.class)
	public void testGetFails() {
		
		userService.loadUserByUsername("hey");

	}
	

	
	

	

	


	

}
