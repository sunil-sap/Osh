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
package com.hybris.osh.facades.device.converter;

import de.hybris.platform.commerceservices.converter.impl.AbstractConverter;
import com.hybris.osh.facades.device.data.DeviceData;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import net.sourceforge.wurfl.core.Device;
import net.sourceforge.wurfl.core.WURFLManager;
import net.sourceforge.wurfl.core.utils.UserAgentUtils;

/**
 * Converter that uses the Wurfl device detection library to convert an HttpServletRequest into a DeviceData instance.
 */
public class WurflRequestDeviceDataConverter extends AbstractConverter<HttpServletRequest, DeviceData>
{
	private static final Logger LOG = Logger.getLogger(WurflRequestDeviceDataConverter.class);

	private WURFLManager wurflManager;

	protected WURFLManager getWurflManager()
	{
		return wurflManager;
	}

	@Required
	public void setWurflManager(final WURFLManager wurflManager)
	{
		this.wurflManager = wurflManager;
	}

	@Override
	protected DeviceData createTarget()
	{
		return new DeviceData();
	}

	@Override
	public void populate(final HttpServletRequest source, final DeviceData target)
	{
		// Resolve the device information for the source request
		final Device device = getWurflManager().getDeviceForRequest(source);

		target.setId(device.getId());
		target.setUserAgent(device.getUserAgent());
		target.setCapabilities(new HashMap<String, String>(device.getCapabilities()));

		target.setDesktopBrowser(isDesktopBrowser(device));
		target.setMobileBrowser(isMobileBrowser(device));
		target.setTabletBrowser(isTabletBrowser(device));

		if (LOG.isDebugEnabled())
		{
			LOG.debug(toStringDeviceData(target));
		}
	}

	protected boolean isDesktopBrowser(final Device device)
	{
		return UserAgentUtils.isDesktopBrowser(device.getUserAgent());
	}

	protected boolean isMobileBrowser(final Device device)
	{
		return UserAgentUtils.isMobileBrowser(device.getUserAgent()) && !isTabletBrowser(device);
	}

	public boolean isTabletBrowser(final Device device)
	{
		final String isTabletCapability = device.getCapability("is_tablet");
		return isTabletCapability != null && "true".equals(isTabletCapability);
	}

	public String toStringDeviceData(final DeviceData device)
	{
		final StringBuilder builder = new StringBuilder();
		builder.append("[DeviceData ");
		builder.append("id").append("=").append(device.getId()).append(", ");
		builder.append("userAgent").append("=").append(device.getUserAgent()).append(", ");
		builder.append("capabilities").append("=").append(device.getCapabilities()).append(", ");
		builder.append("desktop").append("=").append(device.isDesktopBrowser()).append(", ");
		builder.append("mobile").append("=").append(device.isMobileBrowser()).append(", ");
		builder.append("tablet").append("=").append(device.isTabletBrowser()).append(", ");
		builder.append("]");
		return builder.toString();
	}
}
