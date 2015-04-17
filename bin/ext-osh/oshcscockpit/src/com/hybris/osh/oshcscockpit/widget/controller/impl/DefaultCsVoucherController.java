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
package com.hybris.osh.oshcscockpit.widget.controller.impl;


import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.cscockpit.widgets.controllers.BasketController;
import de.hybris.platform.cscockpit.widgets.controllers.impl.AbstractCsWidgetController;
import de.hybris.platform.cscockpit.widgets.events.CheckoutEvent;

import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hybris.osh.core.voucher.OshVoucherService;


/**
 *
 */
public class DefaultCsVoucherController extends AbstractCsWidgetController
{
	private OshVoucherService voucherService;
	private BasketController basketController;
	private static final Logger LOG = Logger.getLogger(DefaultCsVoucherController.class);

	public BasketController getBasketController()
	{
		return basketController;
	}

	public void setBasketController(final BasketController basketController)
	{
		this.basketController = basketController;
	}


	protected CartModel getCartModel()
	{
		final TypedObject cart = getBasketController().getCart();
		if (cart != null && (cart.getObject() instanceof CartModel))
		{
			return (CartModel) cart.getObject();
		}
		else
		{
			return null;
		}
	}

	@Override
	public void dispatchEvent(final String arg0, final Object arg1, final Map<String, Object> arg2)
	{
		dispatchEvent("csCtx", (new CheckoutEvent(arg1, arg0)));
	}

	public boolean isVoucherApplied()
	{
		final CartModel cart = getCartModel();
		if (cart != null)
		{
			final Collection<DiscountModel> collection = voucherService.getAppliedVouchers(cart);
			if (collection != null && !collection.isEmpty())
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * @return the voucherService
	 */
	public OshVoucherService getVoucherService()
	{
		return voucherService;
	}

	/**
	 * @param voucherService
	 *           the voucherService to set
	 */
	public void setVoucherService(final OshVoucherService voucherService)
	{
		this.voucherService = voucherService;
	}


}
