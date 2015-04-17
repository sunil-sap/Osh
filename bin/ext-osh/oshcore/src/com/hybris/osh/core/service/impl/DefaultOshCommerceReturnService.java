/**
 * 
 */
package com.hybris.osh.core.service.impl;

import de.hybris.platform.servicelayer.config.ConfigurationService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

import com.hybris.osh.core.service.OshCommerceReturnService;


/**
 * return order service
 * 
 */
public class DefaultOshCommerceReturnService implements OshCommerceReturnService
{
	protected static final Logger LOG = Logger.getLogger(DefaultOshCommerceReturnService.class);

	private ConfigurationService configurationService;

	private static final String Return_ORDER_BCC_EMAIL = "return_order.notification.bcc.emailid";

	/*
	 * get BCC address for email
	 */
	@Override
	public String[] collectBCCAddressForEmail()
	{
		// YTODO Auto-generated method stub
		final String orderConfBCCEmail = getConfigurationService().getConfiguration().getString(Return_ORDER_BCC_EMAIL);
		Assert.notNull(orderConfBCCEmail, "EmailID For DC Can Not Be Null");

		final String[] emailIds = orderConfBCCEmail.split(",");
		return emailIds;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}

	@Required
	public void setConfigurationService(final ConfigurationService configurationService)
	{
		this.configurationService = configurationService;
	}

}
