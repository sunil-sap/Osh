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
package com.hybris.osh.oshcscockpit.widgets.controllers.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.cscockpit.widgets.controllers.impl.DefaultReturnsController;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.hybris.osh.core.enums.ConsignmentEntryStatus;



/**
 * Override for customize returnable entries
 */
public class DefaultOshReturnsController extends DefaultReturnsController
{

	@Override
	public Map getReturnableOrderEntries()
	{
		final OrderModel orderModel = getOrderModel();
		if (orderModel != null)
		{
			final Map returnableEntries = getReturnService().getAllReturnableEntries(orderModel);

			if (returnableEntries != null)
			{
				final Map newReturnableEntries = new HashMap();
				for (final Iterator iterator = returnableEntries.entrySet().iterator(); iterator.hasNext();)
				{
					//Retrieve those entry which have status complete or shipped
					final Map.Entry entry = (Map.Entry) iterator.next();
					final AbstractOrderEntryModel entryModel = (AbstractOrderEntryModel) entry.getKey();
					for (final ConsignmentEntryModel consignmentEntryModel : entryModel.getConsignmentEntries())
					{
						if (consignmentEntryModel.getEntryLevelStatus().getCode()
								.equals(ConsignmentEntryStatus.PICKEDUP_AT_STORE.toString())
								|| consignmentEntryModel.getEntryLevelStatus().getCode()
										.equals(ConsignmentEntryStatus.SHIPPED.toString()))
						{
							newReturnableEntries.put(entry.getKey(), entry.getValue());
						}
					}

				}

				final Map result = new HashMap();
				java.util.Map.Entry entry;
				for (final Iterator iterator = newReturnableEntries.entrySet().iterator(); iterator.hasNext(); result.put(
						getCockpitTypeService().wrapItem(entry.getKey()), entry.getValue()))
				{
					entry = (java.util.Map.Entry) iterator.next();
				}

				return result;
			}
		}

		return null;
	}
}
