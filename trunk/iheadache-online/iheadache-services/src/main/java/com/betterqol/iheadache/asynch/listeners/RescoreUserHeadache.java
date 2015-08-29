package com.betterqol.iheadache.asynch.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.AppHelpers;
import com.betterqol.iheadache.asynch.events.AppSettingsChanged;
import com.betterqol.iheadache.evaluations.impl.HeadacheEvaluator;
import com.betterqol.iheadache.model.AppSettings;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.model.UserInformation;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;

@Component
public class RescoreUserHeadache implements ApplicationListener <AppSettingsChanged>{
	
	private static final Logger logger = LoggerFactory.getLogger(AppHelpers.MESSAGE_BUS_LOG);
	
	@Autowired
	HeadacheRepository hrepo;
	
	@Autowired
	LookupRepository lookups;

	@Override
	@Async
	public void onApplicationEvent(AppSettingsChanged event) {
		logger.info("Recieved app settings changed for " + event.getNewSettings().getName());
		if (reevaluateHeadache(event.getOldSettings(), event.getNewSettings().getAppSettings()))
			reescoreHeadaches(hrepo.findByUser(event.getNewSettings().getUserId()), event.getNewSettings());	
	}
	
	 void reescoreHeadaches(List<Headache>headaches, UserInformation u ) {
		HeadacheEvaluator eval = new HeadacheEvaluator();
		Map context = new HashMap();
		
		context.put("USER",u);
		context.put("HEADCAHE_TYPES", RepositoryHelpers.enMap2(lookups.findByLookupType(Kind.HEADACHE_TYPE)));
		
		for (Headache h: headaches) {
			eval.transform(h, context);
			hrepo.update(h);
		}
		logger.info("Rescored  headache " + headaches.size());
	}
	
	public static boolean reevaluateHeadache(AppSettings oldSettings, AppSettings newSettings) {
		
		if (!oldSettings.getDurationRule().equals(newSettings.getDurationRule()))
			return true;
		
		if (!oldSettings.getSortRule().equals(newSettings.getSortRule()))
			return true;
		
		return false;
	}
}
