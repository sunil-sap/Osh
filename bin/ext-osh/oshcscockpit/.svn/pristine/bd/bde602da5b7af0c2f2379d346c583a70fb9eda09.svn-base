/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.hybris.osh.oshcscockpit.cancel.strategy;


import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.OrderCancelState;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordercancel.impl.DefaultOrderCancelStateMappingStrategy;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.Collection;
import java.util.Iterator;



/**
 *
 */
public class DefaultOshOrderCancelStateMappingStrategy extends DefaultOrderCancelStateMappingStrategy
{


	public DefaultOshOrderCancelStateMappingStrategy()
	{
	}

	@Override
	public OrderCancelState getOrderCancelState(final OrderModel order)
	{
		final OrderStatus orderStatus = order.getStatus();
		if (OrderStatus.CANCELLED.equals(orderStatus) || OrderStatus.CANCELLING.equals(orderStatus)
				|| OrderStatus.COMPLETED.equals(orderStatus))
		{
			return OrderCancelState.CANCELIMPOSSIBLE;
		}
		final Collection consignments = order.getConsignments();
		if (consignments == null || consignments.isEmpty())
		{
			return OrderCancelState.PENDINGORHOLDINGAREA;
		}
		else
		{
			return checkConsignments(consignments);
		}
	}

	@Override
	protected OrderCancelState checkConsignments(final Collection consignments)
	{
		boolean oneShipped = false;
		boolean allShipped = true;
		boolean allReady = true;
		for (final Iterator iterator = consignments.iterator(); iterator.hasNext();)
		{
			final ConsignmentModel consignmentModel = (ConsignmentModel) iterator.next();
			final ConsignmentStatus status = consignmentModel.getStatus();
			if (status.equals(ConsignmentStatus.SHIPPED))
			{
				oneShipped = true;
			}
			else
			{
				allShipped = false;
			}
			if (!status.equals(ConsignmentStatus.PENDING))
			{
				allReady = false;
			}
		}

		if (allShipped)
		{
			return OrderCancelState.CANCELIMPOSSIBLE;
		}
		if (oneShipped)
		{
			return OrderCancelState.PARTIALLYSHIPPED;
		}
		if (allReady)
		{
			return OrderCancelState.SENTTOWAREHOUSE;
		}
		else
		{
			return OrderCancelState.SHIPPING;
		}
	}
}
