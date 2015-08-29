package com.betterqol.iheadache.security;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.betterqol.iheadache.TestHelpers;
import com.betterqol.iheadache.repository.HealthcarePrincipalRepository;
import com.betterqol.iheadache.repository.PTRepository;
import com.betterqol.iheadache.repository.UserPrincipalRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:app-config.xml"})
public class UserServiceTest {
	
	@Autowired
	UserService userService;
	@Autowired
	UserPrincipalRepository repo;
	@Autowired
	PTRepository ptRepo;
	@Autowired
	HealthcarePrincipalRepository hcprepo;
	@Autowired
	HcpUserService hcpUserService;
	@Autowired
	private MessageDigestPasswordEncoder encoder;
	
	
	@After
	 public void cleanup() {
		 repo.deleteAll();
		 ptRepo.deletAll();
		 hcprepo.deleteAll();
	 }
	
	@Test
	/**
	 * Test lowercase coercion
	 */
	public void testGet() {
		
		repo.add(TestHelpers.makePrincipal("foo"));
		UserDetails user = userService.loadUserByUsername("Foo");
		assertEquals("acbd18db4cc2f85cedef654fccc4a4d8",user.getPassword());
		assertEquals("foo",user.getUsername());
		
		ArrayList auth = new ArrayList(user.getAuthorities());
		
		assertEquals("ROLE_USER",auth.get(0).toString());

	}
	
	@Test
	/**
	 * Ensure that after the after the coercion we get a second chance
	 */
	public void testGetSecondChance() {
		
		repo.add(TestHelpers.makePrincipal("Foo"));
		UserDetails user = userService.loadUserByUsername("Foo");
		assertEquals("Foo",user.getUsername());
		
		ArrayList auth = new ArrayList(user.getAuthorities());
		
		assertEquals("ROLE_USER",auth.get(0).toString());

	}
	
	@Test
	/**
	 * We test to make sure you can both be a hcp and a regular user 
	 * with the same email
	 */
	public void testIdenticalHCPRegularPrincipal() {
		
		repo.add(TestHelpers.makePrincipal("foo"));
		hcprepo.add(TestHelpers.makeHcPrincipal("fo", true, "o"));
		UserDetails user = userService.loadUserByUsername("foo");
		assertEquals("acbd18db4cc2f85cedef654fccc4a4d8",user.getPassword());
		assertEquals("foo",user.getUsername());
		
		UserDetails res = hcpUserService.loadUserByUsername("foo");
		assertEquals("foo",res.getUsername());
		
		
		ArrayList auth = new ArrayList(user.getAuthorities());
		
		assertEquals("ROLE_USER",auth.get(0).toString());

	}
	
	
	@Test (expected= UsernameNotFoundException.class)
	public void testGetFails() {
		
		userService.loadUserByUsername("hey");

	}
	
	@Test
	public void testSpringEncoder() {
	    String hashedPass = encoder.encodePassword("koala", null);
	    assertEquals("a564de63c2d0da68cf47586ee05984d7", hashedPass);
	}
	
	

	

	


	

}
