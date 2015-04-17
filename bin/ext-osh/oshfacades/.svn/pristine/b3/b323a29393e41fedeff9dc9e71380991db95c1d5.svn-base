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
package com.hybris.osh.facades.flow.impl;

import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
import com.hybris.osh.core.enums.CheckoutFlowEnum;
import com.hybris.osh.facades.device.DeviceDetectionFacade;
import com.hybris.osh.facades.flow.CheckoutFlowFacade;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;



/**
 * Test case when user agent is 'a mobile'
 * 
 */
public class DefaultMobileCheckoutFlowFacadeTest extends DefaultCheckoutFlowFacadeTest
{
	@Resource
	private CheckoutFlowFacade checkoutFlowFacade;

	@Resource
	private CMSSiteService cmsSiteService;

	@Resource
	private DeviceDetectionFacade deviceDetectionFacade;

	@Mock
	protected HttpServletRequest request;


	@Override
	@Before
	public void prepareRequest()
	{
		request = Mockito.mock(HttpServletRequest.class);
		BDDMockito
				.given(request.getHeader("User-Agent"))
				.willReturn(
						"Mozilla/5.0 (iPhone; U; CPU iPhone OS 3_0 like Mac OS X; en-us) AppleWebKit/528.18 (KHTML, like Gecko) Version/4.0 Mobile/7A341 Safari/528.16");
		deviceDetectionFacade.initializeRequest(request);
	}

	/**
	 * fallback to default
	 */
	@Override
	@Test
	public void testGetDefaultFlowForUndefinedSiteUid()
	{
		new CMSSiteAwareTestExecutor("dummy", cmsSiteService)
		{
			@Override
			protected void performTest()
			{
				Assert.assertEquals(CheckoutFlowEnum.MULTISTEP, checkoutFlowFacade.getCheckoutFlow());
			}
		}.run();
	}


	@Override
	@Test
	public void testGetDefaultFlowForApparelUk()
	{
		new CMSSiteAwareTestExecutor("apparel-uk", cmsSiteService)
		{
			@Override
			protected void performTest()
			{
				Assert.assertEquals(CheckoutFlowEnum.MULTISTEP, checkoutFlowFacade.getCheckoutFlow());
			}
		}.run();
	}


	@Override
	@Test
	public void testGetDefaultFlowForApparelDe()
	{
		new CMSSiteAwareTestExecutor("apparel-de", cmsSiteService)
		{
			@Override
			protected void performTest()
			{
				Assert.assertEquals(CheckoutFlowEnum.MULTISTEP, checkoutFlowFacade.getCheckoutFlow());
			}
		}.run();
	}


	/**
	 * decision is based on : multi step on new customer (i.e. customer does not have full data required for checkout),
	 * single step otherwise
	 * 
	 */
	@Override
	@Test
	public void testGetDefaultFlowForElectronics()
	{
		new CMSSiteAwareTestExecutor("electronics", cmsSiteService)
		{
			@Override
			protected void performTest()
			{
				Assert.assertEquals(CheckoutFlowEnum.MULTISTEP, checkoutFlowFacade.getCheckoutFlow());
			}
		}.run();
	}
}
