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
package com.hybris.osh.ordercancel;

import de.hybris.platform.basecommerce.enums.CancelReason;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordercancel.OrderCancelEntry;


/**
 *
 */
public class OshOrderCancelEntry extends OrderCancelEntry
{
	//private final double shippingAmt;

	/**
	 * @return the shippingAmt
	 */
	/*
	 * public double getShippingAmt() { return shippingAmt; }
	 */

	public OshOrderCancelEntry(final AbstractOrderEntryModel orderEntry, final long cancelQuantity, final String notes,
			final CancelReason cancelReason)
	{
		super(orderEntry, cancelQuantity, notes, cancelReason);
		if (cancelQuantity < 0L)
		{
			throw new IllegalArgumentException("OrderCancelEntry's cancelQuantity value must be greater than zero");
		}
		if (cancelQuantity > orderEntry.getQuantity().longValue())
		{
			throw new IllegalArgumentException(
					"OrderCancelEntry's cancelQuantity value cannot be greater than actual OrderEntry quantity");
		}
		/*
		 * else { this.shippingAmt = shippingAmt; return; }
		 */
	}
}
