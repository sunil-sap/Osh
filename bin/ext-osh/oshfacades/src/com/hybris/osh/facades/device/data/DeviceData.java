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
package com.hybris.osh.facades.device.data;


import java.util.Map;

/**
 */
public class DeviceData
{
	private String id;
	private String userAgent;
	private Map<String, String> capabilities;

	private boolean desktopBrowser;
	private boolean mobileBrowser;
	private boolean tabletBrowser;


	public String getId()
	{
		return id;
	}

	public void setId(final String id)
	{
		this.id = id;
	}

	public String getUserAgent()
	{
		return userAgent;
	}

	public void setUserAgent(final String userAgent)
	{
		this.userAgent = userAgent;
	}

	public Map<String, String> getCapabilities()
	{
		return capabilities;
	}

	public void setCapabilities(final Map<String, String> capabilities)
	{
		this.capabilities = capabilities;
	}

	public boolean isDesktopBrowser()
	{
		return desktopBrowser;
	}

	public void setDesktopBrowser(final boolean desktopBrowser)
	{
		this.desktopBrowser = desktopBrowser;
	}

	public boolean isMobileBrowser()
	{
		return mobileBrowser;
	}

	public void setMobileBrowser(final boolean mobileBrowser)
	{
		this.mobileBrowser = mobileBrowser;
	}

	public boolean isTabletBrowser()
	{
		return tabletBrowser;
	}

	public void setTabletBrowser(final boolean tabletBrowser)
	{
		this.tabletBrowser = tabletBrowser;
	}
}
