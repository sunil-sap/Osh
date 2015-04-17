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
package com.hybris.osh.storefront.breadcrumb.impl;

import de.hybris.platform.servicelayer.i18n.I18NService;
import com.hybris.osh.storefront.breadcrumb.Breadcrumb;
import com.hybris.osh.storefront.breadcrumb.ResourceBreadcrumbBuilder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.MessageSource;


/**
 * BasicBreadcrumbBuilder implementation
 */
public class DefaultResourceBreadcrumbBuilder implements ResourceBreadcrumbBuilder
{
	private static final String LAST_LINK_CLASS = "active";

	private MessageSource messageSource;
	private I18NService i18nService;

	private String parentBreadcrumbResourceKey;
	private String parentBreadcrumbLinkPath;

	protected I18NService getI18nService()
	{
		return i18nService;
	}

	@Required
	public void setI18nService(final I18NService i18nService)
	{
		this.i18nService = i18nService;
	}

	protected MessageSource getMessageSource()
	{
		return messageSource;
	}

	@Required
	public void setMessageSource(final MessageSource messageSource)
	{
		this.messageSource = messageSource;
	}

	protected String getParentBreadcrumbResourceKey()
	{
		return parentBreadcrumbResourceKey;
	}

	// Optional
	public void setParentBreadcrumbResourceKey(final String parentBreadcrumbResourceKey)
	{
		this.parentBreadcrumbResourceKey = parentBreadcrumbResourceKey;
	}

	protected String getParentBreadcrumbLinkPath()
	{
		return parentBreadcrumbLinkPath;
	}

	// Optional
	public void setParentBreadcrumbLinkPath(final String parentBreadcrumbLinkPath)
	{
		this.parentBreadcrumbLinkPath = parentBreadcrumbLinkPath;
	}

	@Override
	public List<Breadcrumb> getBreadcrumbs(final String resourceKey) throws IllegalArgumentException
	{
		final List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();

		if (getParentBreadcrumbResourceKey() != null && !getParentBreadcrumbResourceKey().isEmpty())
		{
			final String name = getMessageSource().getMessage(getParentBreadcrumbResourceKey(), null, getI18nService().getCurrentLocale());
			final String breadcrumbLinkPath = getParentBreadcrumbLinkPath();
			final String link = breadcrumbLinkPath != null && !breadcrumbLinkPath.isEmpty() ? breadcrumbLinkPath : "#";
			breadcrumbs.add(new Breadcrumb(link, name, null));
		}

		if (StringUtils.isNotBlank(resourceKey))
		{
			final String name = getMessageSource().getMessage(resourceKey, null, getI18nService().getCurrentLocale());
			breadcrumbs.add(new Breadcrumb("#", name, null));
		}

		if (!breadcrumbs.isEmpty())
		{
			breadcrumbs.get(breadcrumbs.size() - 1).setLinkClass(LAST_LINK_CLASS);
		}

		return breadcrumbs;
	}
}
