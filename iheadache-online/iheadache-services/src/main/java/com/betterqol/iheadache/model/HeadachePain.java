/**
 * @(#) HeadachePain.java
 */
package com.betterqol.iheadache.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import com.betterqol.iheadache.extensions.JacksonJoda;
import com.google.common.collect.Lists;

@XmlRootElement
public class HeadachePain 
{
	
	private double level;
	private DateTime time;
	private List<LookupDTO> painType;
	private List <LookupDTO >painLocation;
	
	public HeadachePain() {
		
	}
	
	public HeadachePain(double level, DateTime time, String painType,
			String... painLocation) {
		super();
		this.level = level;
		this.time = time;
		this.painType = Lists.newArrayList(new LookupDTO (painType, painType));
		this.painLocation = Lists.newArrayList();
		for (String p : painLocation)
			this.painLocation.add(new LookupDTO(p, p));
	}


	public void setLevel( double o )
	{
		level=o;
	}
	

	public double getLevel( )
	{
		return level;
	}
	
	@JsonSerialize(using = JacksonJoda.class)
	public void setTime( DateTime o )
	{
		time=o;
	}
	
	@JsonSerialize(using = JacksonJoda.class)
	public DateTime getTime( )
	{
		return time;
	}
	

	public void setPainType( List<LookupDTO> o )
	{
		painType=o;
	}
	

	public List<LookupDTO> getPainType( )
	{
		return painType;
	}
	

	public void setPainLocation( List<LookupDTO> o )
	{
		painLocation=o;
	}
	
	/**
	 * Refers to a CouchLookup
	 */
	public List<LookupDTO> getPainLocation( )
	{
		return painLocation;
	}
	
	@Override
	public  String toString() 
	{	
		StringBuffer b = new StringBuffer();
		LookupDTO.enlist(b, this.painType);
		b.append(" Pain in ");
		LookupDTO.enlist(b, this.painLocation);
		return b.toString();
	}
	
	
}
