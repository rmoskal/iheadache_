package com.betterqol.iheadache;

import java.util.GregorianCalendar;
import java.util.UUID;

import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.betterqol.iheadache.model.HealthcarePrincipal;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.model.UserPrincipal;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.HealthcarePrincipalRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.PTRepository;
import com.betterqol.iheadache.repository.TreatmentRepository;
import com.betterqol.iheadache.repository.UserInformationRepository;
import com.betterqol.iheadache.repository.UserPrincipalRepository;

/***
 * Loads the couch database with sample data
 * @author rob
 *
 */
public class SampleDataLoader {
	
	private  HeadacheRepository repo;
	private  LookupRepository lookups;
	private  TreatmentRepository treatments;
	private  UserInformationRepository up;
	private  UserPrincipalRepository pp;
	private  HealthcarePrincipalRepository hp;
	private  PTRepository ptRepo;
	
	public SampleDataLoader(HeadacheRepository repo,UserInformationRepository up, LookupRepository lookups,
			TreatmentRepository treatments, PTRepository ptRepo, UserPrincipalRepository pp, 
			HealthcarePrincipalRepository hp) {
		super();
		this.repo = repo;
		this.up = up;
		this.lookups = lookups;
		this.treatments = treatments;
		this.ptRepo = ptRepo;
		this.pp = pp;
		this.hp = hp;
	}

	public void init() throws Throwable {	

	try{
		
		UserPrincipal p = TestHelpers.makePrincipal("foo@foo.com");
		p.setId("f65762e9-11b1-4965-a3c5-913c13af078b");
		pp.add(p);
		
		
		UserInformation u = TestHelpers.makeUser("foo@foo.com");
		u.setId("u_"+"f65762e9-11b1-4965-a3c5-913c13af078b");
		up.createTestUser(u);
		
		HealthcarePrincipal hh = TestHelpers.makeHcPrincipal("John", true, "Smith");
		p.setId("f65762e9-11b1-4965-a3c5-000c13af078b");
		hp.add(hh);
		up.createForParent(hh.getId(), hh.getName());
	

		
	}catch(Throwable e){ 
		System.out.println(e.getMessage());
	};
		
	}
	
	public void quickCreate(String email, String first, String last) {
		
		UserPrincipal p = TestHelpers.makePrincipal(email);
		p.setFirstName(first);
		p.setLastName(last);
		p.setId(UUID.randomUUID().toString());
		pp.add(p);
		UserInformation u = TestHelpers.makeUser(email);
		u.setId("u_"+ p.getId());
		System.out.println("-"+p.getId()+"-");
		up.createTestUser(u);
		
	}
	
	public void quickHCPCreate(String email, String first, String last, String institution, boolean approved) {
		
		HealthcarePrincipal o = new HealthcarePrincipal();
		o.setName(email);
		o.setFirstName(first);
		o.setLastName(last);
		o.setId(UUID.randomUUID().toString());
		PasswordEncoder en = new Md5PasswordEncoder();
		o.setInstitution(institution);
		o.setApproved(approved);
		o.setPassword(en.encodePassword(email, null));
		hp.add(o);
		up.createForParent(o.getId(), o.getName());
		
	}
	
	
}
