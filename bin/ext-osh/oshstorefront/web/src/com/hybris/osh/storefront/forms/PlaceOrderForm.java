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


public class PlaceOrderForm
{
	private String securityCode;
	private boolean termsCheck;
	private boolean gift;
	private String giftMessage;

	public String getSecurityCode()
	{
		return securityCode;
	}

	public void setSecurityCode(final String securityCode)
	{
		this.securityCode = securityCode;
	}

	public boolean isTermsCheck()
	{
		return termsCheck;
	}

	public void setTermsCheck(final boolean termsCheck)
	{
		this.termsCheck = termsCheck;
	}

	/**
	 * @return the gift
	 */
	public boolean isGift()
	{
		return gift;
	}

	/**
	 * @param gift
	 *           the gift to set
	 */
	public void setGift(final boolean gift)
	{
		this.gift = gift;
	}

	/**
	 * @return the giftMessage
	 */
	public String getGiftMessage()
	{
		return giftMessage;
	}

	/**
	 * @param giftMessage
	 *           the giftMessage to set
	 */
	public void setGiftMessage(final String giftMessage)
	{
		this.giftMessage = giftMessage;
	}

}
