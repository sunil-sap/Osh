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
package com.hybris.osh.storefront.controllers.pages.checkout;

import de.hybris.platform.acceleratorfacades.storefinder.StoreFinderFacade;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceData.PriceType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.enums.CheckoutFlowEnum;
import com.hybris.osh.facades.flow.impl.SessionOverrideCheckoutFlowFacade;
import com.hybris.osh.facades.order.data.OshOrderData;
import com.hybris.osh.facades.order.data.OshOrderEntryData;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.forms.RegisterForm;


/**
 * CheckoutController
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/checkout")
public class CheckoutController extends AbstractCheckoutController
{
	protected static final Logger LOG = Logger.getLogger(CheckoutController.class);

	private static final String CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL = "orderConfirmation";

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;



	@RequestMapping(method = RequestMethod.GET)
	public String checkout()
	{
		if (hasItemsInCart())
		{
			return getCheckoutRedirectUrl();
		}
		LOG.info("Missing or empty cart");

		// No session cart or empty session cart. Bounce back to the cart page.
		return REDIRECT_PREFIX + "/cart";
	}

	@SuppressWarnings("static-access")
	@RequestMapping(value = "/orderConfirmation/{orderCode}", method = RequestMethod.GET)
	public String orderConfirmation(@PathVariable("orderCode") final String orderCode, final Model model)
			throws CMSItemNotFoundException
	{
		SessionOverrideCheckoutFlowFacade.resetSessionOverrides();
		try
		{
			final OshOrderData orderDetails = (OshOrderData) orderFacade.getOrderDetailsForCode(orderCode);
			BigDecimal sub = new BigDecimal(0);
			boolean storepickup = false;
			boolean shipping = false;
			if (orderDetails.getEntries() != null && !orderDetails.getEntries().isEmpty())
			{
				final int index = 0;
				for (final OrderEntryData entry : orderDetails.getEntries())
				{
					if (!((OshOrderEntryData) entry).getOrderType().equalsIgnoreCase(OshCoreConstants.WAREHOUSE)
							&& !((OshOrderEntryData) entry).getOrderType().equalsIgnoreCase(OshCoreConstants.DROPSHIP))
					{
						storepickup = true;
						model.addAttribute("storepickup", Boolean.valueOf(storepickup));

					}
					else
					{
						shipping = true;
						model.addAttribute("shipping", Boolean.valueOf(shipping));
					}
					final String productCode = entry.getProduct().getCode();
					final ProductData product = productFacade.getProductForCodeAndOptions(productCode,
							Arrays.asList(ProductOption.BASIC, ProductOption.PRICE, ProductOption.CATEGORIES));
					entry.setProduct(product);

					getPromotionDetails(orderDetails, entry, index);
					sub = sub.add(entry.getTotalPrice().getValue());
					final PriceData subTotal = priceDataFactory.create(PriceType.BUY, sub, OshCoreConstants.DEFAULT_CURRENCY);
					orderDetails.setSubTotal(subTotal);
				}
			}

			if (!orderDetails.getOrderType().equalsIgnoreCase(OshCoreConstants.WAREHOUSE))
			{
				final PointOfServiceData pos = storeFinderFacade.getPointOfServiceForName(orderDetails.getOrderType());
				if (pos != null)
				{
					model.addAttribute("posData", pos);
				}
			}
			model.addAttribute("orderCode", orderCode);
			model.addAttribute("orderData", orderDetails);
			model.addAttribute("allItems", orderDetails.getEntries());
			model.addAttribute("deliveryAddress", orderDetails.getDeliveryAddress());
			model.addAttribute("deliveryMode", orderDetails.getDeliveryMode());
			model.addAttribute("paymentInfo", orderDetails.getPaymentInfo());
			model.addAttribute("pageType", PageType.OrderConfirmation);
			if (shipping)
			{
				final AddressData shippingAddressData = orderFacade.getOrderDetailsForCode(orderCode).getDeliveryAddress();
				final RegisterForm registerForm = new RegisterForm();
				registerForm.setEmail(shippingAddressData.getEmail());
				registerForm.setFirstName(shippingAddressData.getFirstName());
				registerForm.setLastName(shippingAddressData.getLastName());
				registerForm.setTitleCode(shippingAddressData.getTitleCode());
				model.addAttribute("email", shippingAddressData.getEmail());
				model.addAttribute("registerForm", registerForm);
			}
			if (storepickup && !shipping)
			{
				final AddressData shippingAddressData = orderFacade.getOrderDetailsForCode(orderCode).getPaymentInfo()
						.getBillingAddress();
				final RegisterForm registerForm = new RegisterForm();
				registerForm.setEmail(shippingAddressData.getEmail());
				registerForm.setFirstName(shippingAddressData.getFirstName());
				registerForm.setLastName(shippingAddressData.getLastName());
				registerForm.setTitleCode(shippingAddressData.getTitleCode());
				model.addAttribute("email", shippingAddressData.getEmail());
				model.addAttribute("registerForm", registerForm);
			}

			final AbstractPageModel cmsPage = getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL);
			storeCmsPageInModel(model, cmsPage);
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CHECKOUT_ORDER_CONFIRMATION_CMS_PAGE_LABEL));
			model.addAttribute("metaRobots", "no-index,no-follow");
			return ControllerConstants.Views.Pages.Checkout.CheckoutConfirmationPage;
		}
		catch (final ModelNotFoundException e)
		{
			LOG.getLogger(e.getMessage());
			return "redirect:/";
		}

	}

	@RequestMapping(value = "/orderConfirmation/{orderCode}", method = RequestMethod.POST)
	public String guestRegister(@PathVariable("orderCode") final String orderCode, final Model model,
			final RegisterForm registerForm) throws CMSItemNotFoundException
	{
		return "forward:/login/register";
	}

	/**
	 * Method used to determine the checkout redirect URL that will handle the checkout process.
	 * 
	 * @return A <code>String</code> object of the URL to redirect to.
	 */
	protected String getCheckoutRedirectUrl()
	{
		final CheckoutFlowEnum checkoutFlow = getCheckoutFlowFacade().getCheckoutFlow();
		if (CheckoutFlowEnum.SINGLE.equals(checkoutFlow))
		{
			return REDIRECT_PREFIX + "/checkout/single";
		}

		// Default to the multi-step checkout
		return REDIRECT_PREFIX + "/checkout/multi";
	}

	@ModelAttribute("titles")
	public Collection<TitleData> getTitles()
	{
		return userFacade.getTitles();
	}
}
