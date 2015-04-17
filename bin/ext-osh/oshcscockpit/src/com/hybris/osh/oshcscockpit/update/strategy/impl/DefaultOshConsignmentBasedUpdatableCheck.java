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
package com.hybris.osh.oshcscockpit.update.strategy.impl;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.jalo.BasecommerceManager;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.ordersplitting.jalo.Consignment;
import de.hybris.platform.ordersplitting.jalo.ConsignmentEntry;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.Set;

import javax.annotation.Resource;

import com.hybris.osh.oshcscockpit.update.strategy.UpdatableCheck;


/**
 *
 */
public class DefaultOshConsignmentBasedUpdatableCheck extends AbstractBusinessService implements UpdatableCheck
{

	@Resource
	ModelService modelService;

	@SuppressWarnings("deprecation")
	@Override
	public boolean perform(final OrderModel order, final AbstractOrderEntryModel orderentry, final long updateQuantity)
	{
		// FALSE, in case of invalid quantity
		if (updateQuantity < 1 || orderentry.getQuantity().longValue() < updateQuantity)
		{
			return false;
		}

		// any existing consignments out there?
		final Set<Consignment> consignments = BasecommerceManager.getInstance().getConsignments(
				(Order) getModelService().toPersistenceLayer(order)); // TODO: Avoid accessing the JALO layer

		// initial isUpdatable STATE
		boolean isUpdatable = false;

		if (!consignments.isEmpty())
		{
			// Now, isUpdatable state will only be true, if the status is PENDING and the update quantity is lower or equal than the PENDING quantity
			for (final Consignment consignment : consignments)
			{
				// only PENDING consignments will be examined?
				if (consignment.getStatus().getCode().equals(ConsignmentStatus.SHIPPED.getCode()))
				{
					final Set<ConsignmentEntry> entries = consignment.getConsignmentEntries();
					for (final ConsignmentEntry entry : entries)
					{
						if (modelService.toModelLayer(entry.getOrderEntry()).equals(orderentry))
						{
							// isUpdatable -> TRUE, in case of 'update quantity' is lower or equal than the 'pending quantity'
							isUpdatable = (entry.getShippedQuantityAsPrimitive() >= updateQuantity);
						}
					}
				}
			}
		}
		else
		{
			// ... let'S FAIL if there were no consignments!
			return false;
		}
		return isUpdatable;
	}
}
