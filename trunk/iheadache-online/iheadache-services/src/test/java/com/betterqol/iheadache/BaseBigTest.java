package com.betterqol.iheadache;

import org.ektorp.CouchDbConnector;
import org.junit.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.betterqol.iheadache.accumulators.AccumulatorBank;
import com.betterqol.iheadache.profile.ProfileService;
import com.betterqol.iheadache.repository.HcpUserAssociationRepository;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.HealthcarePrincipalRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.PTRepository;
import com.betterqol.iheadache.repository.PreventativeTreatmentRepository;
import com.betterqol.iheadache.repository.TreatmentRepository;
import com.betterqol.iheadache.repository.UserInformationRepository;
import com.betterqol.iheadache.repository.UserPrincipalRepository;


public abstract class BaseBigTest {

	@Autowired
	protected HeadacheRepository repo;
	@Autowired
	protected LookupRepository lookups;
	@Autowired
	protected TreatmentRepository treatments;
	@Autowired
	protected ProfileService serviceToTest;
	@Autowired
	protected UserInformationRepository up;
	@Autowired
	protected UserPrincipalRepository pp;
	@Autowired
	protected PTRepository ptRepo;
	@Autowired
	protected PreventativeTreatmentRepository preventTRepo;
	@Autowired
	protected HealthcarePrincipalRepository hcp;
	@Autowired
	protected HcpUserAssociationRepository associationRepo;
	@Autowired
	protected AccumulatorBank accumulators;
	@Autowired
	@Qualifier("headacheDatabase")
	protected CouchDbConnector db;

	public BaseBigTest() {
		super();
	}

	@After
	public void cleanup() {
		repo.deleteAll();
		lookups.deleteAll();
		treatments.deleteAll();
		up.deleteAll();
		pp.deleteAll();
		hcp.deleteAll();
		associationRepo.deleteAll();
		ptRepo.deletAll();
		preventTRepo.deleteAll();
	}

}