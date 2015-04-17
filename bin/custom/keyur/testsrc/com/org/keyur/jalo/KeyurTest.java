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
package com.org.keyur.jalo;

import static org.junit.Assert.assertTrue;

import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Test;



/**
 * JUnit Tests for the Keyur extension
 */


public class KeyurTest extends HybrisJUnit4TransactionalTest
{

	@Test
	public void testKeyur()
	{
		final HttpClient client = HttpClientBuilder.create().build();
		final HttpGet request = new HttpGet("abcdefg.com");
		request.setHeader("user-agent", "fake googlebot");
		try
		{
			final HttpResponse response = client.execute(request);
		}
		catch (final Exception e)
		{
			//
		}
		final boolean testTrue = true;
		System.out.println("hello");
		assertTrue("true", testTrue);

	}
}
