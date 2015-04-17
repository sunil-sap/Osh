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
package com.hybris.osh.core.checkout.flow.impl;

import de.hybris.platform.acceleratorservices.enums.UiExperienceLevel;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import com.hybris.osh.core.checkout.flow.CheckoutFlowStrategy;
import com.hybris.osh.core.enums.CheckoutFlowEnum;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Required;

/**
 */
public class UiExperienceCheckoutFlowStrategy extends AbstractCheckoutFlowStrategy
{
	private UiExperienceService uiExperienceService;
	private Map<UiExperienceLevel,CheckoutFlowStrategy> experienceMappings;

	public UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}

	protected Map<UiExperienceLevel, CheckoutFlowStrategy> getExperienceMappings()
	{
		return experienceMappings;
	}

	@Required
	public void setExperienceMappings(final Map<UiExperienceLevel, CheckoutFlowStrategy> experienceMappings)
	{
		this.experienceMappings = experienceMappings;
	}

	@Override
	public CheckoutFlowEnum getCheckoutFlow()
	{
		final UiExperienceLevel uiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		if (uiExperienceLevel != null)
		{
			final CheckoutFlowStrategy checkoutFlowStrategy = getExperienceMappings().get(uiExperienceLevel);
			if (checkoutFlowStrategy != null)
			{
				return checkoutFlowStrategy.getCheckoutFlow();
			}
		}
		return getDefaultStrategy().getCheckoutFlow();
	}
}
