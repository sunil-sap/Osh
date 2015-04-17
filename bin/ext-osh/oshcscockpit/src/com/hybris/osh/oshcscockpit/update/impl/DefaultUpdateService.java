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
package com.hybris.osh.oshcscockpit.update.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hybris.osh.oshcscockpit.update.UpdateService;
import com.hybris.osh.oshcscockpit.update.strategy.UpdatableCheck;


public class DefaultUpdateService extends AbstractBusinessService implements UpdateService
{

	private List<UpdatableCheck> updatableCheck = new LinkedList<UpdatableCheck>();


	public List<UpdatableCheck> getUpdatableCheck()
	{
		return updatableCheck;
	}


	public void setUpdatableCheck(final List<UpdatableCheck> updatableCheck)
	{
		this.updatableCheck = updatableCheck;
	}

	public boolean isUpdatable(final OrderModel order, final AbstractOrderEntryModel entry, final long updateQuantity)
	{
		// process assigned strategies
		boolean isUpdatable = (getUpdatableCheck() == null || getUpdatableCheck().isEmpty()) ? true : false;
		for (final UpdatableCheck strategy : getUpdatableCheck())
		{
			isUpdatable = strategy.perform(order, entry, updateQuantity);

			if (!isUpdatable)
			{
				return false;
			}
		}
		return isUpdatable;
	}

	@Override
	public Map<AbstractOrderEntryModel, Long> getAllUpdatableEntries(final OrderModel order, final PrincipalModel requestor)
	{
		final Map<AbstractOrderEntryModel, Long> updatable = new HashMap<AbstractOrderEntryModel, Long>();

		for (final AbstractOrderEntryModel entry : order.getEntries())
		{

			long updatableQuantity = 0;
			for (long i = entry.getQuantity().longValue(); i > 0; i--)
			{
				if (isUpdatable(order, entry, i))
				{
					updatableQuantity = i;
					break;
				}
			}
			if (updatableQuantity > 0)
			{
				updatable.put(entry, Long.valueOf(updatableQuantity));
			}
		}
		return updatable;
	}
}
