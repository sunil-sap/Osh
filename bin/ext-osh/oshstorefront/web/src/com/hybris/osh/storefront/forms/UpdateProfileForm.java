/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.hybris.osh.storefront.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;


/**
 * Form object for updating profile.
 */
public class UpdateProfileForm
{

	private String titleCode;
	private String firstName;
	private String lastName;
	private AddressForm addressForm;
	private String countryData;
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
	 * @return the titleCode
	 */
	public String getTitleCode()
	{
		return titleCode;
	}

	/**
	 * @param titleCode
	 *           the titleCode to set
	 */
	public void setTitleCode(final String titleCode)
	{
		this.titleCode = titleCode;
	}

	/**
	 * @return the firstName
	 */
	@NotNull(message = "{profile.firstName.invalid}")
	@Size(min = 1, max = 255, message = "{profile.firstName.invalid}")
	@NotBlank(message = "{profile.firstName.invalid}")
	public String getFirstName()
	{
		return firstName;
	}

	/**
	 * @param firstName
	 *           the firstName to set
	 */
	public void setFirstName(final String firstName)
	{
		this.firstName = firstName;
	}

	/**
	 * @return the lastName
	 */
	@NotNull(message = "{profile.lastName.invalid}")
	@Size(min = 1, max = 255, message = "{profile.lastName.invalid}")
	@NotBlank(message = "{profile.lastName.invalid}")
	public String getLastName()
	{
		return lastName;
	}


	/**
	 * @param lastName
	 *           the lastName to set
	 */
	public void setLastName(final String lastName)
	{
		this.lastName = lastName;
	}

	/**
	 * @return the addressForm
	 */
	public AddressForm getAddressForm()
	{
		return addressForm;
	}

	/**
	 * @param addressForm
	 *           the addressForm to set
	 */
	public void setAddressForm(final AddressForm addressForm)
	{
		this.addressForm = addressForm;
	}

	/**
	 * @return the countryData
	 */
	public String getCountryData()
	{
		return countryData;
	}

	/**
	 * @param countryData
	 *           the countryData to set
	 */
	public void setCountryData(final String countryData)
	{
		this.countryData = countryData;
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



}
