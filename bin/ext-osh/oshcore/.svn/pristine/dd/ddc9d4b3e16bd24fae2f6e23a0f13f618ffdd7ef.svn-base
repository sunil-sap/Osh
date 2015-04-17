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
package com.hybris.osh.core.service.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.strategies.CartValidator;
import de.hybris.platform.order.strategies.CreateOrderFromCartStrategy;
import de.hybris.platform.order.strategies.ordercloning.CloneAbstractOrderStrategy;

import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class DefaultOshCreateOrderFromCartStrategy implements CreateOrderFromCartStrategy
{

	private CartValidator cartValidator;
	private CloneAbstractOrderStrategy cloneAbstractOrderStrategy;


	@Override
	public OrderModel createOrderFromCart(final CartModel cart) throws InvalidCartException
	{
		if (cartValidator != null)
		{
			cartValidator.validateCart(cart);
		}
		final OrderModel res = cloneAbstractOrderStrategy.clone(null, null, cart, cart.getMerchantReferenceCode(),
				OrderModel.class, OrderEntryModel.class);

		return res;
	}



	@Required
	public void setCartValidator(final CartValidator cartValidator)
	{
		this.cartValidator = cartValidator;
	}

	@Required
	public void setCloneAbstractOrderStrategy(final CloneAbstractOrderStrategy cloneAbstractOrderStrategy)
	{
		this.cloneAbstractOrderStrategy = cloneAbstractOrderStrategy;
	}



}
