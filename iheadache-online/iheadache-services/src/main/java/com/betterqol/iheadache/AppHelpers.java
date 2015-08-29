package com.betterqol.iheadache;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.betterqol.iheadache.model.CouchLookup;
import com.betterqol.iheadache.model.CouchLookup.Kind;
import com.betterqol.iheadache.model.IDescription;
import com.betterqol.iheadache.model.LookupDTO;
import com.betterqol.iheadache.model.YesNoResponse;
import com.betterqol.iheadache.model.dto.ProfileItem;
import com.betterqol.iheadache.repository.LookupRepository;
import com.betterqol.iheadache.repository.RepositoryHelpers;
import com.google.common.collect.Lists;

public abstract class AppHelpers {
	
	public static ObjectMapper mapper = new ObjectMapper();
	
	public final static double  SEVERITY_MODERATE = 3.5;
	public final static double  SEVERITY_SEVERE = 6.6;
	
	public final static String[]  EMPTY_LIST = {};
	
	
	public static enum HeadacheTypes {TENSION_HEADACHE,PROBABLE_MIGRAINE,MIGRAINE,UNCLASSIFIED_HEADACHE,NO_HEADACHE};
	public static enum StandardSymptoms {HAS_AURA,IS_ANILATERAL,IS_MOVEMENT,IS_THROBBING,HAS_NAUSEA,HAS_PHOTOPHOBIA,HAS_PHONOBIA,
		HAS_OLFACTOPHOBIA,IS_NECK,IS_SINUS};

	public final static String  MESSAGE_BUS_LOG = "MESSAGE_BUS_LOG";  // We use one log for all message events
    
	public static List<CouchLookup> getStandardSymptoms(LookupRepository lookups) {
		
		List <CouchLookup> results = Lists.newArrayList();
		
		for (StandardSymptoms s :AppHelpers.StandardSymptoms.values())
			results.add(lookups.get(s.name()));
				
		return results;
	}
	
	/**
	 * Just takes two JSONArrays and adds the second to the first
	 * @param one
	 * @param two
	 * @throws JSONException
	 */
	public static JSONArray mergeJSONArrays(JSONArray one, JSONArray two) throws JSONException {
		
		for (int i = 0; i < two.length(); i = i + 1) {
			one.put( two.getJSONObject(i));
			
		}
		return one;
	}
	
	

	public static JSONArray constructProfile (List<? extends IDescription> selections, List<? extends IDescription> fullSet, boolean isCustom, String ... toRemove) throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		
		Map<String, String> selectionMap = RepositoryHelpers.enMap(selections);
		JSONArray results = new JSONArray();
		AppHelpers.mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		if (selections==null)
			selections = Lists.newArrayList();
		
		for (IDescription each: fullSet)
		{
			JSONObject o = new JSONObject(AppHelpers.mapper.writeValueAsString(each));
			o.put("in", selectionMap.containsKey(each.getId()));
			o.put("custom", isCustom);
			if (o.has("_id")) {
				o.put("id", o.getString("_id"));
				o.remove("_id");
				o.remove("_rev");
				o.remove("lookupType");
			}
			
			for (String prop: toRemove)
				o.remove(prop);
			results.put(o);
		}
		
		return results;
	
	}
	

	public static JSONArray simpleJson(List<? extends Object> items, String ... toRemove) throws JsonGenerationException, JsonMappingException, JSONException, IOException {
		
		JSONArray results = new JSONArray();
		AppHelpers.mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		for (Object each: items)
		{
			JSONObject o = new JSONObject(AppHelpers.mapper.writeValueAsString(each));
			if (o.has("_id")) {
				o.put("id", o.getString("_id"));
				o.remove("_id");
				o.remove("_rev");
				o.remove("lookupType");
			}
			
			for (String prop: toRemove)
				o.remove(prop);
			
			results.put(o);
			
		}
		
		return results;
	}

	public static List<YesNoResponse> constructSymptoms (List<? extends IDescription> selections, List<? extends IDescription> fullSet)  {
		
		Map<String, String> selectionMap = RepositoryHelpers.enMap(selections);
		List<YesNoResponse>  results = Lists.newArrayList();
	
		if (selections==null)
			selections = Lists.newArrayList();
		
		for (IDescription each: fullSet)
			if (selectionMap.containsKey(each.getId()))
				results.add(new YesNoResponse(each));
	
		return results;
	
	}
	
	public static void splitResponse(List <ProfileItem> inputList,List<LookupDTO>active, 
			List<LookupDTO> others, boolean onlycustom) {
		
		for (ProfileItem item : inputList ) {
			if (item.isIn())
				active.add(new LookupDTO(item));
			if (onlycustom) {
				if (item.isCustom())
					others.add(new LookupDTO(item));
			}
			else
				others.add(new LookupDTO(item));
						
		}
		
	}
	
	public static  List <CouchLookup> upcastLookupDTO(List<? extends IDescription> in, Kind lookupKind) {
		
		List <CouchLookup> res = Lists.newArrayList();
		for (IDescription each:in)
			res.add(new CouchLookup());
		return res;
	
	}
	

    
    
}
