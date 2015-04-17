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
package com.hybris.osh.events;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 *
 */
public class PaymentFailedEvent extends AbstractEvent
{
	private final OrderProcessModel process;
	private final OrderModel order;

	/**
	 * @param process
	 */
	public PaymentFailedEvent(final OrderProcessModel process, final OrderModel order)
	{
		this.process = process;
		this.order = order;
	}


	/**
	 * @return
	 */
	public OrderProcessModel getProcess()
	{
		return process;
	}

	/**
	 * @return
	 */
	public OrderModel getOrder()
	{
		return order;
	}



}
