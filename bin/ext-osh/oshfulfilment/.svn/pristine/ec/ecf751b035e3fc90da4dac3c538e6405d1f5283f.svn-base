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
package com.hybris.osh.impl;

import de.hybris.platform.core.model.order.OrderModel;
import com.hybris.osh.CheckOrderService;


public class DefaultCheckOrderService implements CheckOrderService
{

	@Override
	public boolean isValid(final OrderModel order)
	{
		if (!order.getCalculated().booleanValue())
		{
			return false;
		}
		if (order.getEntries().isEmpty())
		{
			return false;
		}
		else if (order.getDeliveryAddress() == null)
		{
			return false;
		}
		else if (order.getPaymentInfo() == null)
		{
			return false;
		}
		return true;
	}
}
