package com.betterqol.iheadache.extensions;

import java.lang.annotation.Annotation;
import java.util.Date;

import org.jboss.resteasy.spi.StringParameterUnmarshaller;
import org.jboss.resteasy.util.FindAnnotation;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DateFormatter implements StringParameterUnmarshaller<Date> {
	DateTimeFormatter fmt;

	@Override
	public void setAnnotations(Annotation[] annotations) {
		DateFormat format = FindAnnotation.findAnnotation(annotations, DateFormat.class);
	    fmt = DateTimeFormat.forPattern(format.value());
	}

	@Override
	public Date fromString(String str) {
	
		return fmt.parseDateTime(str).toDate();

	}
	
}
