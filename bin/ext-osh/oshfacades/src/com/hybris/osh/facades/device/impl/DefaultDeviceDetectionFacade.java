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
package com.hybris.osh.facades.device.impl;

import de.hybris.platform.acceleratorservices.enums.UiExperienceLevel;
import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserConstants;
import com.hybris.osh.facades.device.DeviceDetectionFacade;
import com.hybris.osh.facades.device.data.DeviceData;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the DeviceDetectionFacade
 */
public class DefaultDeviceDetectionFacade implements DeviceDetectionFacade
{
	protected static final String DETECTED_DEVICE = "DeviceDetectionFacade-Detected-Device";

	private static final Logger LOG = Logger.getLogger(DefaultDeviceDetectionFacade.class.getName());
	
	private Converter<HttpServletRequest, DeviceData> requestDeviceDataConverter;
	private Converter<DeviceData, UiExperienceLevel> deviceDataUiExperienceLevelConverter;
	private SessionService sessionService;
	private UiExperienceService uiExperienceService;

	protected Converter<HttpServletRequest, DeviceData> getRequestDeviceDataConverter()
	{
		return requestDeviceDataConverter;
	}

	@Required
	public void setRequestDeviceDataConverter(final Converter<HttpServletRequest, DeviceData> requestDeviceDataConverter)
	{
		this.requestDeviceDataConverter = requestDeviceDataConverter;
	}

	protected Converter<DeviceData, UiExperienceLevel> getDeviceDataUiExperienceLevelConverter()
	{
		return deviceDataUiExperienceLevelConverter;
	}

	@Required
	public void setDeviceDataUiExperienceLevelConverter(final Converter<DeviceData, UiExperienceLevel> deviceDataUiExperienceLevelConverter)
	{
		this.deviceDataUiExperienceLevelConverter = deviceDataUiExperienceLevelConverter;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	@Required
	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}

	@Override
	public void initializeRequest(final HttpServletRequest request)
	{
		// Only initialise the detected device once per session
		if (getCurrentDetectedDevice() == null || "true".equals(request.getParameter("clear")))
		{
			// Detect the device in the current request
			final DeviceData deviceData = getRequestDeviceDataConverter().convert(request);

			// Map the detected device to a UiExperienceLevel
			final UiExperienceLevel uiExperienceLevel = getDeviceDataUiExperienceLevelConverter().convert(deviceData);
			setCurrentDetectedDevice(deviceData);
			getUiExperienceService().setDetectedUiExperienceLevel(uiExperienceLevel);

			if (LOG.isDebugEnabled())
			{
				final UserModel userModel =
					(UserModel) getSessionService().getAttribute(UserConstants.USER_SESSION_ATTR_KEY);
				final String userUid = (userModel != null) ? userModel.getUid() : "<null>";

				LOG.debug("Detected device [" + deviceData.getId() + "] User Agent [" + deviceData.getUserAgent() + "] Mobile [" + deviceData.isMobileBrowser() + "] Session user [" + userUid + "]");
			}
		}
	}

	@Override
	public DeviceData getCurrentDetectedDevice()
	{
		return getSessionService().getAttribute(DETECTED_DEVICE);
	}

	protected void setCurrentDetectedDevice(final DeviceData deviceData)
	{
		getSessionService().setAttribute(DETECTED_DEVICE, deviceData);
	}
}
