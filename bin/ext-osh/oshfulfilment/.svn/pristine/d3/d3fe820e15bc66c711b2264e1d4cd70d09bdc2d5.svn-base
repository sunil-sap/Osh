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
import de.hybris.platform.core.model.order.OrderModel;
import com.hybris.osh.CheckOrderService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import java.util.Set;

import org.apache.log4j.Logger;


/**
 * This example action checks the order for required data in the business process. Skipping this action may result in
 * failure in one of the subsequent steps of the process. The relation between the order and the business process is
 * defined in basecommerce extension through item OrderProcess. Therefore if your business process has to access the
 * order (a typical case), it is recommended to use the OrderProcess as a parentClass instead of the plain
 * BusinessProcess.
 */
public class CheckOrder extends AbstractAction
{

	private CheckOrderService checkOrderService;

	private static final String STATUS_NOK = "NOK";
	private static final String STATUS_OK = "OK";

	private static Set<String> transitions = createTransitions(STATUS_OK, STATUS_NOK);


	@Override
	public String execute(final BusinessProcessModel process)
	{
		final OrderModel order = ((OrderProcessModel) process).getOrder();

		if (order == null)
		{
			Logger.getLogger(this.getClass()).error("Missing the order, exiting the process");
			return STATUS_NOK;
		}

		if (checkOrderService.isValid(order))
		{
			setOrderStatus(order, OrderStatus.CHECKED_VALID);
			return STATUS_OK;
		}
		else
		{
			setOrderStatus(order, OrderStatus.CHECKED_INVALID);
			return STATUS_NOK;
		}
	}

	@Override
	public Set<String> getTransitions()
	{
		return transitions;
	}

	public void setCheckOrderService(final CheckOrderService checkOrderService)
	{
		this.checkOrderService = checkOrderService;
	}

}
