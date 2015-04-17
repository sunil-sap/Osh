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
package com.hybris.osh.storefront.security;

import de.hybris.platform.commercefacades.customer.CustomerFacade;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.StringUtils;

import com.hybris.osh.facades.cart.OshCartFacade;


/**
 * Success handler initializing user settings and ensuring the cart is handled correctly
 */
public class StorefrontAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
	private CustomerFacade customerFacade;
	@Autowired
	private OshCartFacade oshCartFacade;


	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws IOException, ServletException
	{
		getCustomerFacade().loginSuccess();
		oshCartFacade.appendCartForCurrentUser(request);
		final SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest == null)
		{
			super.onAuthenticationSuccess(request, response, authentication);
			return;
		}
		final String targetUrlParameter = getTargetUrlParameter();
		if (isAlwaysUseDefaultTargetUrl() && !savedRequest.getRedirectUrl().contains("order") || targetUrlParameter != null
				&& StringUtils.hasText(request.getParameter(targetUrlParameter)))
		{
			requestCache.removeRequest(request, response);
			super.onAuthenticationSuccess(request, response, authentication);
			return;
		}
		else
		{
			clearAuthenticationAttributes(request);
			final String targetUrl = savedRequest.getRedirectUrl();
			logger.debug((new StringBuilder()).append("Redirecting to DefaultSavedRequest Url: ").append(targetUrl).toString());
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
			return;
		}
	}

	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	public StorefrontAuthenticationSuccessHandler()
	{
		requestCache = new HttpSessionRequestCache();
	}

	@Required
	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	@Override
	public void setRequestCache(final RequestCache requestCache)
	{
		this.requestCache = requestCache;
	}

	private RequestCache requestCache;
}
