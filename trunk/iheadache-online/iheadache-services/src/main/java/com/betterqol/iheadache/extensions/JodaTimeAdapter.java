package com.betterqol.iheadache.extensions;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.joda.time.DateTime;

public class JodaTimeAdapter extends  XmlAdapter<String, DateTime> {

	//static DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
	
	@Override
	public DateTime unmarshal(String v) throws Exception {
		return new DateTime(Long.parseLong(v));

	}

	@Override
	public String marshal(DateTime v) throws Exception {
		return String.valueOf(v.getMillis());
		
	}

}
