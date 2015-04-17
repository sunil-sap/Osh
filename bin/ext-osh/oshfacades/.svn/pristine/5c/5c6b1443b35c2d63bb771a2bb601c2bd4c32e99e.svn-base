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

import de.hybris.platform.acceleratorservices.enums.UiExperienceLevel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import com.hybris.osh.facades.device.data.DeviceData;

/**
 * Converter that converts from a DeviceData to a UiExperienceLevel enum value.
 * The purpose of this converter is to map from the DeviceData settings into one
 * of the UiExperienceLevel enum values.
 */
public class DeviceDataUiExperienceLevelConverter implements Converter<DeviceData, UiExperienceLevel>
{
	@Override
	public UiExperienceLevel convert(final DeviceData deviceData) throws ConversionException
	{
		if (deviceData.isDesktopBrowser())
		{
			return UiExperienceLevel.DESKTOP;
		}
		else if (deviceData.isMobileBrowser())
		{
			return UiExperienceLevel.MOBILE;
		}
		else if (deviceData.isTabletBrowser())
		{
			return UiExperienceLevel.DESKTOP;
		}
		// Default to the DESKTOP
		return UiExperienceLevel.DESKTOP;
	}

	@Override
	public UiExperienceLevel convert(final DeviceData deviceData, final UiExperienceLevel prototype) throws ConversionException
	{
		return convert(deviceData);
	}
}
