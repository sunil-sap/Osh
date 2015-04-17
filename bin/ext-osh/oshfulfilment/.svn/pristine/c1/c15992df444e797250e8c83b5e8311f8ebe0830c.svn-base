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
package com.hybris.osh.actions;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;


public class ReserveOrderAmount extends AbstractSimpleOrderDecisionAction
{
	@Override
	public Transition executeAction(final OrderProcessModel process)
	{
		setOrderStatus(process.getOrder(), OrderStatus.PAYMENT_AMOUNT_RESERVED);
		return Transition.OK;
	}
}
