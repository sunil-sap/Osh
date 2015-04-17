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
package com.hybris.osh.storefront.controllers.pages.checkout;

import de.hybris.platform.commercefacades.order.data.CartData;
import com.hybris.osh.facades.flow.CheckoutFlowFacade;
import com.hybris.osh.storefront.controllers.pages.AbstractPageController;

import javax.annotation.Resource;

/**
 * Base controller for all page controllers. Provides common functionality for all page controllers.
 */
public abstract class AbstractCheckoutController extends AbstractPageController
{
	@Resource(name = "checkoutFlowFacade")
	private CheckoutFlowFacade checkoutFlowFacade;

	protected CheckoutFlowFacade getCheckoutFlowFacade()
	{
		return checkoutFlowFacade;
	}

	/**
	 * Checks if there are any items in the cart.
	 * 
	 * @return returns true if items found in cart.
	 */
	protected boolean hasItemsInCart()
	{
		final CartData cartData = getCheckoutFlowFacade().getCheckoutCart();

		return (cartData.getEntries() != null && !cartData.getEntries().isEmpty());
	}
}
