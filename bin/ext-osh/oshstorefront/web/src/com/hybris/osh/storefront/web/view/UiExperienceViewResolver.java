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
package com.hybris.osh.storefront.web.view;

import de.hybris.platform.acceleratorservices.enums.UiExperienceLevel;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;


/**
 * A view resolver that detects the device a request is coming from and directs it to the appropriate view. This view
 * resolver extends Spring's org.springframework.web.servlet.view.InternalResourceViewResolver.
 */
public class UiExperienceViewResolver extends InternalResourceViewResolver
{
	private static final Logger LOG = Logger.getLogger(UiExperienceViewResolver.class);

	private UiExperienceService uiExperienceService;
	private Map<UiExperienceLevel, String> uiExperienceViewPrefix;
	private String unknownUiExperiencePrefix;

	protected UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	@Required
	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}

	protected Map<UiExperienceLevel, String> getUiExperienceViewPrefix()
	{
		return uiExperienceViewPrefix;
	}

	@Required
	public void setUiExperienceViewPrefix(final Map<UiExperienceLevel, String> uiExperienceViewPrefix)
	{
		this.uiExperienceViewPrefix = uiExperienceViewPrefix;
	}

	protected String getUnknownUiExperiencePrefix()
	{
		return unknownUiExperiencePrefix;
	}

	@Required
	public void setUnknownUiExperiencePrefix(final String unknownUiExperiencePrefix)
	{
		this.unknownUiExperiencePrefix = unknownUiExperiencePrefix;
	}

	@Override
	protected AbstractUrlBasedView buildView(final String viewName) throws Exception
	{
		final UiExperienceLevel uiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		final String expandedViewName = getViewName(uiExperienceLevel, viewName);

		if (LOG.isDebugEnabled())
		{
			LOG.debug("Expanded View Name [" + viewName + "] into [" + expandedViewName + "]");
		}

		final InternalResourceView view = (InternalResourceView) super.buildView(expandedViewName);
		view.setAlwaysInclude(false);
		return view;
	}

	public String getViewName(final UiExperienceLevel uiExperienceLevel, final String viewName)
	{
		final String prefix = getUiExperienceViewPrefix().get(uiExperienceLevel);
		if (prefix != null)
		{
			return prefix + viewName;
		}

		return getUnknownUiExperiencePrefix() + viewName;
	}
}
