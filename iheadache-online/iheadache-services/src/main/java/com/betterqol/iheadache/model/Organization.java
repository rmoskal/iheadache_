/**
 * @(#) Organization.java
 */

package com.betterqol.iheadache.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Organization extends BaseEntity 
{
	
	private UserInformation users;
	
	/**
	 * @param o
	 */
	public void setUsers( UserInformation o )
	{
		users= o;
	}
	
	/**
	 * @return 
	 */
	public UserInformation getUsers( )
	{
		return users;
	}
	
	
}
