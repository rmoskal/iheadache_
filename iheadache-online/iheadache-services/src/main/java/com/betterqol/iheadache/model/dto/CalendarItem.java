package com.betterqol.iheadache.model.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

@JsonSerialize(include = Inclusion.ALWAYS)
/***
 * A litle DTO that supports the calendar widget
 * @author rob
 *
 */
public class CalendarItem {
	
	public Integer Date;
	public String HeadacheType;
	public String HeadacheId;
	
	public CalendarItem() {
		
	}
	
	public CalendarItem(Integer date, String headacheType, String headacheId) {
		super();
		Date = date;
		HeadacheType = headacheType;
		HeadacheId = headacheId;
	}

}
