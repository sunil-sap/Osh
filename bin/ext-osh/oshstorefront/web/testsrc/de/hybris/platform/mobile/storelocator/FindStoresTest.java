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
package de.hybris.platform.mobile.storelocator;

import de.hybris.bootstrap.annotations.UnitTest;

import java.util.List;

import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.iphone.IPhoneDriver;


/**
 * The unit test for the mobile Store Finder page.
 */
@UnitTest
public class FindStoresTest
{
	/**
	 * The Store Finder page's URL.
	 */
	private static final String STORE_FINDER_URL = "http://localhost:9001/oshstorefront/store-finder?clear=true&site=electronics";

	/**
	 * Tests the Store Finder page using the iOS and Android drivers.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testStoreFinderPage() throws Exception
	{

		final WebDriver driver = new IPhoneDriver();

		testStoreFinder("iOS", driver);

		//final WebDriver driver = new AndroidDriver();

		//testStoreFinder("Android", driver);
	}

	/**
	 * Tests the Store Finder page.
	 * 
	 * @param label
	 * @param driver
	 */
	private void testStoreFinder(final String label, final WebDriver driver)
	{
		// Visit the StoreFinder page...
		driver.get(STORE_FINDER_URL);

		// Find the search text field by its name...
		final WebElement element = driver.findElement(By.name("q"));

		// Enter a search term...
		element.sendKeys("Tokio");

		// Submit the form... WebDriver finds the element's form
		element.submit();

		Assert.assertEquals("StoreFinder | Electronics Site", driver.getTitle());

		final List<WebElement> elements = driver.findElements(By.className("storeName"));

		Assert.assertEquals(5, elements.size());

		final List<WebElement> goButtons = driver.findElements(By.className("goButton"));

		Assert.assertEquals(5, goButtons.size());

		final WebElement firstGoButton = goButtons.get(0);

		firstGoButton.click();

		final WebElement storeName = driver.findElement(By.id("storeName"));

		Assert.assertEquals("Hybris Electronics - Misato", storeName.getText());
	}
}
