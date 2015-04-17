/**
 * 
 */
package com.hybris.osh.core.service.impl;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import de.hybris.platform.commerceservices.enums.SalesApplication;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


/**
 * JUnit test suite for{@DefaultOshCommerceCheckoutService}
 * 
 */
public class DefaultOshCommerceCheckoutServiceTest
{
	@InjectMocks
	DefaultOshCommerceCheckoutService commerceCheckoutService = new DefaultOshCommerceCheckoutService();

	@Mock
	CartModel cartModel;

	@Mock
	CartEntryModel cartEntryModel;

	@Mock
	ModelService modelService;

	@Mock
	CustomerModel customerModel;

	@Mock
	UserService userService;

	@Mock
	PointOfServiceModel pos;

	@Mock
	ConfigurationService configurationService;

	boolean gift;

	String giftMessage;

	String securityCode;

	String paymentProvider;

	String storeName;

	BigDecimal amount;

	SalesApplication salesApplication;

	@Before
	public void setUp()
	{
		MockitoAnnotations.initMocks(this);
	}


	@Test(expected = IllegalArgumentException.class)
	public void setStoreAddressNullTest()
	{
		commerceCheckoutService.setStoreAddress(null);
	}

	@Test
	public void setStoreAddressTest()
	{
		given((CustomerModel) userService.getCurrentUser()).willReturn(customerModel);
		given(customerModel.getMyStore()).willReturn(pos);
		verify(modelService, times(0)).save(cartModel);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setGiftOrderNullTest()
	{

		commerceCheckoutService.setGiftOrder(null, gift, giftMessage);
	}

	@Test
	public void setGiftOrderTest()
	{
		verify(modelService, times(0)).save(cartModel);

	}

	@Test(expected = IllegalArgumentException.class)
	public void authorizePaymentNullTest()
	{
		commerceCheckoutService.authorizePayment(null, securityCode, paymentProvider);
	}

	@Test(expected = NullPointerException.class)
	public void setStoreNameNullTest()
	{
		commerceCheckoutService.setStoreName(null, storeName);
	}

	@Test
	public void setStoreNameTest()
	{
		verify(modelService, times(0)).save(cartModel);
	}

	@Test(expected = NullPointerException.class)
	public void authorizePaymentAmountNullTest()
	{
		commerceCheckoutService.authorizePaymentAmount(null, securityCode, paymentProvider, amount);
	}

	@Test(expected = IllegalArgumentException.class)
	public void placeOrderNullTest() throws InvalidCartException
	{
		commerceCheckoutService.placeOrder(null, salesApplication);
	}

	@Test(expected = IllegalArgumentException.class)
	public void placeOrderNullTestForSales() throws InvalidCartException
	{
		commerceCheckoutService.placeOrder(cartModel, null);
	}
}
