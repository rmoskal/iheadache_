/**
 * @(#) Disability.java
 */

package com.betterqol.iheadache.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Disability
{

	private int completelyDisabled;
	private List<YesNoResponse> responses;
	private int partiallyDisabled;

	

	public void setCompletelyDisabled( int o )
	{
		completelyDisabled=o;
	}
	

	public int getCompletelyDisabled( )
	{
		return completelyDisabled;
	}
	


	public void setPartiallyDisabled( int o )
	{
		partiallyDisabled=o;
	}
	

	public int getPartiallyDisabled( )
	{
		return partiallyDisabled;
	}


	public List<YesNoResponse> getResponses() {
		return responses;
	}


	public void setResponses(List<YesNoResponse> responses) {
		this.responses = responses;
	}
	


	
	
}
