package com.hybris.osh.facades.user.data;


public class StateData
{
	private String isocode;
	private String name;
	private String countryIsoCode;


	/**
	 * @return the countryIsoCode
	 */
	public String getCountryIsoCode()
	{
		return countryIsoCode;
	}

	/**
	 * @param countryIsoCode
	 *           the countryIsoCode to set
	 */
	public void setCountryIsoCode(final String countryIsoCode)
	{
		this.countryIsoCode = countryIsoCode;
	}


	/**
	 * @return the name
	 */
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *           the name to set
	 */
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * @return the isocode
	 */
	public String getIsocode()
	{
		return isocode;
	}

	/**
	 * @param isocode
	 *           the isocode to set
	 */
	public void setIsocode(final String isocode)
	{
		this.isocode = isocode;
	}

}
