package com.betterqol.iheadache.extensions;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.DateTime;

public class JacksonJoda extends JsonSerializer<DateTime> {
	//static DateTimeFormatter fmt = ISODateTimeFormat.dateTime();

	@Override
	public void serialize(DateTime value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
	      jgen.writeNumber(value.getMillis());

		
	}

}
