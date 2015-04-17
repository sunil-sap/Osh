/**
 * 
 */
package com.hybris.osh.facades.user.data;

import de.hybris.platform.commercefacades.user.data.RegisterData;

/**
 * POJO to add the field email which is extending the existing RegisterData 
 */
public class OshRegisterData extends RegisterData
{

	private String email;

	/**
	 * @return the email
	 */
	public String getEmail()
	{
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(final String email)
	{
		this.email = email;
	}
}
