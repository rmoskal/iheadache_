package com.betterqol.iheadache.repository;

import java.util.Date;
import java.util.List;

import org.ektorp.Page;
import org.ektorp.PageRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.resource.MiniHeadacheRepo;
import com.betterqol.iheadache.security.HeadacheSecurityPrincipal;



@Component(value="miniHeadacheRepository")
public class MiniHeadacheRepository implements MiniHeadacheRepo {
	
	@Autowired
	HeadacheRepository repo;
	
	/**
	 * This is a wrapper around getPagedDateRange(). We are going to apply an AOP wrapper to it, to calculate the headache type
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @param pageRequest
	 * @return
	 */
	public Page<Headache> getPagedDateRange(String userId, Date startDate,
			Date endDate, PageRequest pageRequest) {
		
		return repo.getPagedDateRange(userId, startDate, endDate,pageRequest, "by_date");
	}
	
	
	/***
	 * This is a wrapper around getDateRange(). We will use it when we want to apply an AOP wrapper to it, to calculate the headache type
	 * @param userId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<Headache> getDateRange(String userId, Date startDate,
			Date endDate) {

			
		return repo.getDateRange(userId, startDate, endDate);
	}
	
	
	public HeadacheSecurityPrincipal getUser() {
		return repo.getUser();
	}
	


}
