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
package com.hybris.osh.core.checkout.pci.impl;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.acceleratorservices.hostedorderpage.constants.HostedOrderPageConstants;
import com.hybris.osh.core.enums.CheckoutPciOptionEnum;

import org.apache.commons.configuration.PropertyConverter;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class ConfiguredCheckoutPciStrategy extends AbstractCheckoutPciStrategy
{
	private SiteConfigService siteConfigService;

	protected SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	@Override
	public CheckoutPciOptionEnum getSubscriptionPciOption()
	{
		final CheckoutPciOptionEnum checkoutPciOption = getSiteConfiguredSubscriptionPciOption();
		if (checkoutPciOption != null)
		{
			return checkoutPciOption;
		}

		return getDefaultCheckoutPciStrategy().getSubscriptionPciOption();
	}

	protected CheckoutPciOptionEnum getSiteConfiguredSubscriptionPciOption()
	{
		// Check if there is any HOP configuration
		final String enabledProperty = getSiteConfigService().getProperty(HostedOrderPageConstants.HopProperties.HOP_PCI_STRATEGY_ENABLED);
		final boolean enabled = enabledProperty != null && PropertyConverter.toBoolean(enabledProperty).booleanValue();
		if (enabled)
		{
			return CheckoutPciOptionEnum.HOP;
		}
		return null;
	}
}
