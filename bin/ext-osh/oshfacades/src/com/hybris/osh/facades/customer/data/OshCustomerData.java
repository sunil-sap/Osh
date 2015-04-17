/**
 * 
 */
package com.hybris.osh.facades.customer.data;

import de.hybris.platform.acceleratorfacades.user.data.AcceleratorCustomerData;


/**
 * 
 * 
 */
public class OshCustomerData extends AcceleratorCustomerData
{
	private Long phone;
	private String day;
	private String month;
	private boolean storenewletter;
	private boolean cluborchardinfo;
	private boolean specialoffer;
	private String loyaltyNumber;


	/**
	 * @return the loyaltyNumber
	 */
	public String getLoyaltyNumber()
	{
		return loyaltyNumber;
	}

	/**
	 * @param loyaltyNumber
	 *           the loyaltyNumber to set
	 */
	public void setLoyaltyNumber(final String loyaltyNumber)
	{
		this.loyaltyNumber = loyaltyNumber;
	}

	/**
	 * @return the phone
	 */
	public Long getPhone()
	{
		return phone;
	}

	/**
	 * @param phone
	 *           the phone to set
	 */
	public void setPhone(final Long phone)
	{
		this.phone = phone;
	}


	/**
	 * @return the storenewletter
	 */
	public boolean isStorenewletter()
	{
		return storenewletter;
	}

	/**
	 * @param storenewletter
	 *           the storenewletter to set
	 */
	public void setStorenewletter(final boolean storenewletter)
	{
		this.storenewletter = storenewletter;
	}

	/**
	 * @return the cluborchardinfo
	 */
	public boolean isCluborchardinfo()
	{
		return cluborchardinfo;
	}

	/**
	 * @param cluborchardinfo
	 *           the cluborchardinfo to set
	 */
	public void setCluborchardinfo(final boolean cluborchardinfo)
	{
		this.cluborchardinfo = cluborchardinfo;
	}

	/**
	 * @return the specialoffer
	 */
	public boolean isSpecialoffer()
	{
		return specialoffer;
	}

	/**
	 * @param specialoffer
	 *           the specialoffer to set
	 */
	public void setSpecialoffer(final boolean specialoffer)
	{
		this.specialoffer = specialoffer;
	}


	/**
	 * @return the day
	 */
	public String getDay()
	{
		return day;
	}

	/**
	 * @param day
	 *           the day to set
	 */
	public void setDay(final String day)
	{
		this.day = day;
	}

	/**
	 * @return the month
	 */
	public String getMonth()
	{
		return month;
	}

	/**
	 * @param month
	 *           the month to set
	 */
	public void setMonth(final String month)
	{
		this.month = month;
	}

}
