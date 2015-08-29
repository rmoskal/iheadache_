package com.betterqol.iheadache.log;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Path;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.ektorp.CouchDbConnector;
import org.ektorp.Page;
import org.ektorp.PageRequest;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.betterqol.iheadache.extensions.PagingAdapter;
import com.betterqol.iheadache.model.Headache;
import com.betterqol.iheadache.repository.HeadacheRepository;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.resource.LogResource;

@Component
@Path(LogService.URL)
@SuppressWarnings({ "rawtypes", "unchecked" })
@RolesAllowed(value = { "ROLE_USER" })
public class LogService implements LogResource {

	public static final String URL = "/app/service/log";
	private static final Logger logger = LoggerFactory.getLogger(LogService.class);
	public static DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

	@Autowired
	HeadacheRepository repo;

	@Autowired
	LookupRepository lookups;
	
	@Autowired
	PagingAdapter adapter;

	@Autowired
	@Qualifier("headacheDatabase")
	CouchDbConnector db;
	
	



	public Map getLog(Date startDate, Date endDate, String pageLink,int[] criteria, int[] headacheTypes)
			throws JSONException {
	
		return getLog(repo.getUser().getUserPk(), startDate, endDate, pageLink,criteria,headacheTypes);
	}

	public Map getLog(String userId, Date startDate, Date endDate,
			String pageLink, int[] criteria, int[] headacheTypes) throws JSONException {

		PageRequest pr = pageLink != null ? PageRequest.fromLink(pageLink)
				: PageRequest.firstPage(12);

		Page<Headache> in = adapter.getHeadachePage(userId, startDate, endDate,
				pr, criteria,headacheTypes);

		Map results = new HashMap();
		results.put("payload", in.getRows());
		if (in.isHasNext())
			results.put("nextLink", in.getNextLink());
		if (in.isHasPrevious())
			results.put("previousLink", in.getPreviousLink());
		results.put("total", in.getTotalSize());
		return results;
	}


	public static int[] parseParameters(String params) {
		
		int [] criteria = new int[]{};
		if (! StringUtils.isEmpty(params)) {
			String[] items = params.split(",");
			 criteria = new int[items.length];
			 for (int i = 0; i < items.length; i++) 
				 criteria[i] =  Integer.parseInt(items[i]);
		
		}
		return criteria;
	}
	
	public static String createDisabiltySLabel(int full, int partial) {

		int total = full + partial;
		int hr = total / 60;
		int min = total % 60;
		return String.format("%s hr: %s min", hr, min);
	}

}

