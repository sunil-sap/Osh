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
package com.hybris.osh.facades.flow.impl;

import de.hybris.platform.acceleratorfacades.order.impl.AcceleratorCheckoutFacade;
import de.hybris.platform.commercefacades.order.impl.DefaultCheckoutFacade;
import com.hybris.osh.core.checkout.flow.CheckoutFlowStrategy;
import com.hybris.osh.core.checkout.pci.CheckoutPciStrategy;
import com.hybris.osh.core.enums.CheckoutFlowEnum;
import com.hybris.osh.core.enums.CheckoutPciOptionEnum;
import com.hybris.osh.facades.flow.CheckoutFlowFacade;

import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link CheckoutFlowFacade}. Delegates resolving the checkout flow to an injected
 * {@link CheckoutFlowStrategy}.
 * 
 * @since 4.6
 * @spring.bean checkoutFlowFacade
 */
public class DefaultCheckoutFlowFacade extends AcceleratorCheckoutFacade implements CheckoutFlowFacade
{
	private CheckoutFlowStrategy checkoutFlowStrategy;
	private CheckoutPciStrategy checkoutPciStrategy;

	@Override
	public CheckoutFlowEnum getCheckoutFlow()
	{
		return getCheckoutFlowStrategy().getCheckoutFlow();
	}

	@Override
	public CheckoutPciOptionEnum getSubscriptionPciOption()
	{
		return getCheckoutPciStrategy().getSubscriptionPciOption();
	}

	protected CheckoutFlowStrategy getCheckoutFlowStrategy()
	{
		return checkoutFlowStrategy;
	}

	@Required
	public void setCheckoutFlowStrategy(final CheckoutFlowStrategy strategy)
	{
		this.checkoutFlowStrategy = strategy;
	}

	protected CheckoutPciStrategy getCheckoutPciStrategy()
	{
		return this.checkoutPciStrategy;
	}

	@Required
	public void setCheckoutPciStrategy(final CheckoutPciStrategy strategy)
	{
		this.checkoutPciStrategy = strategy;
	}
}
