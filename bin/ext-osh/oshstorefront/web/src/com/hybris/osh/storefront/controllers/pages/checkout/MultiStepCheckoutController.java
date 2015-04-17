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

import de.hybris.platform.acceleratorfacades.hostedorderpage.HostedOrderPageFacade;
import de.hybris.platform.acceleratorservices.hostedorderpage.data.HostedOrderPageData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.order.InvalidCartException;
import com.hybris.osh.core.enums.CheckoutPciOptionEnum;
import com.hybris.osh.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.hybris.osh.storefront.constants.WebConstants;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.util.GlobalMessages;
import com.hybris.osh.storefront.forms.AddressForm;
import com.hybris.osh.storefront.forms.PaymentDetailsForm;
import com.hybris.osh.storefront.forms.PlaceOrderForm;
import com.hybris.osh.storefront.forms.validation.PaymentDetailsValidator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;


/**
 * MultiStepCheckoutController
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/checkout/multi")
public class MultiStepCheckoutController extends AbstractCheckoutController
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(MultiStepCheckoutController.class);

	private static final String MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL = "multiStepCheckoutSummary";
	private static final String REDIRECT_URL_CHOOSE_DELIVERY_ADDRESS = REDIRECT_PREFIX + "/checkout/multi/choose-delivery-address";
	private static final String REDIRECT_URL_ADD_DELIVERY_ADDRESS = REDIRECT_PREFIX + "/checkout/multi/add-delivery-address";
	private static final String REDIRECT_URL_CHOOSE_DELIVERY_METHOD = REDIRECT_PREFIX + "/checkout/multi/choose-delivery-method";
	private static final String REDIRECT_URL_CHOOSE_PAYMENT_METHOD = REDIRECT_PREFIX + "/checkout/multi/choose-payment-method";
	private static final String REDIRECT_URL_ADD_PAYMENT_METHOD = REDIRECT_PREFIX + "/checkout/multi/add-payment-method";
	private static final String REDIRECT_URL_SUMMARY = REDIRECT_PREFIX + "/checkout/multi/summary";
	private static final String REDIRECT_URL_CART = REDIRECT_PREFIX + "/cart";
	private static final String REDIRECT_URL_ERROR = REDIRECT_PREFIX + "/checkout/multi/hop-error";

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "acceleratorCheckoutFacade")
	private CheckoutFacade checkoutFacade;

	@Resource(name = "paymentDetailsValidator")
	private PaymentDetailsValidator paymentDetailsValidator;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "multiStepCheckoutBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Resource(name = "hostedOrderPageFacade")
	private HostedOrderPageFacade hostedOrderPageFacade;

	protected CheckoutFacade getCheckoutFacade()
	{
		return checkoutFacade;
	}

	protected UserFacade getUserFacade()
	{
		return userFacade;
	}

	protected ProductFacade getProductFacade()
	{
		return productFacade;
	}

	protected PaymentDetailsValidator getPaymentDetailsValidator()
	{
		return paymentDetailsValidator;
	}

	protected ResourceBreadcrumbBuilder getResourceBreadcrumbBuilder()
	{
		return resourceBreadcrumbBuilder;
	}

	protected HostedOrderPageFacade getHostedOrderPageFacade()
	{
		return hostedOrderPageFacade;
	}

	@ModelAttribute("titles")
	public Collection<TitleData> getTitles()
	{
		return userFacade.getTitles();
	}

	@ModelAttribute("countries")
	public Collection<CountryData> getCountries()
	{
		return checkoutFacade.getDeliveryCountries();
	}

	@ModelAttribute("billingCountries")
	public Collection<CountryData> getBillingCountries()
	{
		return checkoutFacade.getBillingCountries();
	}

	@ModelAttribute("cardTypes")
	public Collection<CardTypeData> getCardTypes()
	{
		return checkoutFacade.getSupportedCardTypes();
	}

	@ModelAttribute("months")
	public List<SelectOption> getMonths()
	{
		final List<SelectOption> months = new ArrayList<SelectOption>();

		months.add(new SelectOption("1", "01"));
		months.add(new SelectOption("2", "02"));
		months.add(new SelectOption("3", "03"));
		months.add(new SelectOption("4", "04"));
		months.add(new SelectOption("5", "05"));
		months.add(new SelectOption("6", "06"));
		months.add(new SelectOption("7", "07"));
		months.add(new SelectOption("8", "08"));
		months.add(new SelectOption("9", "09"));
		months.add(new SelectOption("10", "10"));
		months.add(new SelectOption("11", "11"));
		months.add(new SelectOption("12", "12"));

		return months;
	}

	@ModelAttribute("startYears")
	public List<SelectOption> getStartYears()
	{
		final List<SelectOption> startYears = new ArrayList<SelectOption>();
		final Calendar calender = new GregorianCalendar();

		for (int i = calender.get(Calendar.YEAR); i > (calender.get(Calendar.YEAR) - 6); i--)
		{
			startYears.add(new SelectOption(String.valueOf(i), String.valueOf(i)));
		}

		return startYears;
	}

	@ModelAttribute("expiryYears")
	public List<SelectOption> getExpiryYears()
	{
		final List<SelectOption> expiryYears = new ArrayList<SelectOption>();
		final Calendar calender = new GregorianCalendar();

		for (int i = calender.get(Calendar.YEAR); i < (calender.get(Calendar.YEAR) + 11); i++)
		{
			expiryYears.add(new SelectOption(String.valueOf(i), String.valueOf(i)));
		}

		return expiryYears;
	}

	@ModelAttribute("checkoutSteps")
	public List<CheckoutSteps> addCheckoutStepsToModel(final HttpServletRequest request)
	{
		final String baseUrl = UriComponentsBuilder.fromHttpUrl(request.getRequestURL().toString())
				.replacePath(request.getContextPath()).build().toUriString();

		final List<CheckoutSteps> checkoutSteps = new ArrayList<CheckoutSteps>();
		checkoutSteps.add(new CheckoutSteps("deliveryAddress", baseUrl + "/checkout/multi/choose-delivery-address"));
		checkoutSteps.add(new CheckoutSteps("deliveryMethod", baseUrl + "/checkout/multi/choose-delivery-method"));
		checkoutSteps.add(new CheckoutSteps("paymentMethod", baseUrl + "/checkout/multi/choose-payment-method"));
		checkoutSteps.add(new CheckoutSteps("confirmOrder", baseUrl + "/checkout/multi/summary"));

		return checkoutSteps;
	}

	@RequestMapping(method = RequestMethod.GET)
	public String gotoFirstStep()
	{
		if (hasItemsInCart())
		{
			return REDIRECT_URL_CHOOSE_DELIVERY_ADDRESS;
		}
		LOG.info("Missing or empty cart");
		return REDIRECT_URL_CART;
	}


	/**
	 * This is the entry point (first page) for the the multi-step checkout process. The page returned by this call will
	 * show a list of customer addresses. If there is a default address, this will be selected in the view. If there are
	 * no address then we redirect to the create new delivery address page.
	 * 
	 * @param model
	 *           - the model for the view.
	 * @return - the deliver address step page.
	 * @throws CMSItemNotFoundException
	 *            - when a CMS page is not found
	 */
	@RequestMapping(value = "/choose-delivery-address", method = RequestMethod.GET)
	public String doChooseDeliveryAddress(final Model model) throws CMSItemNotFoundException
	{
		if (!hasItemsInCart())
		{
			LOG.info("Missing or empty cart");
			return REDIRECT_URL_CART;
		}

		if (hasNoDeliveryAddress())
		{
			return REDIRECT_URL_ADD_DELIVERY_ADDRESS;
		}

		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		model.addAttribute("cartData", cartData);
		model.addAttribute("selectedDeliveryAddressId", cartData.getDeliveryAddress().getId());
		model.addAttribute("deliveryAddresses", getDeliveryAddresses());

		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));

		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryAddress.breadcrumb"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryAddressPage;
	}

	/**
	 * This method gets called when the "Use this Address" button is clicked. It sets the selected delivery address on
	 * the checkout facade - if it has changed, and reloads the page highlighting the selected delivery address.
	 * 
	 * @param selectedAddressCode
	 *           - the id of the delivery address.
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = "/select-delivery-address", method = RequestMethod.GET)
	public String doSelectDeliveryAddress(@RequestParam("selectedAddressCode") final String selectedAddressCode)
	{
		if (!hasItemsInCart())
		{
			LOG.info("Missing or empty cart");
			return REDIRECT_URL_CART;
		}

		if (StringUtils.isNotBlank(selectedAddressCode))
		{
			final AddressData selectedAddressData = getCheckoutFacade().getDeliveryAddressForCode(selectedAddressCode);
			final boolean hasSelectedAddressData = selectedAddressData != null;
			if (hasSelectedAddressData)
			{
				final AddressData cartCheckoutDeliveryAddress = getCheckoutFacade().getCheckoutCart().getDeliveryAddress();
				if (isAddressIdChanged(cartCheckoutDeliveryAddress, selectedAddressData))
				{
					getCheckoutFacade().setDeliveryAddress(selectedAddressData);
				}
			}
		}
		return REDIRECT_URL_CHOOSE_DELIVERY_METHOD;
	}

	protected boolean isAddressIdChanged(final AddressData cartCheckoutDeliveryAddress, final AddressData selectedAddressData)
	{
		return (cartCheckoutDeliveryAddress != null && !selectedAddressData.getId().equals(cartCheckoutDeliveryAddress.getId()));
	}

	@RequestMapping(value = "/add-delivery-address", method = RequestMethod.GET)
	public String addDeliveryAddress(final Model model) throws CMSItemNotFoundException
	{
		if (!hasItemsInCart())
		{
			LOG.info("Missing or empty cart");
			return REDIRECT_URL_CART;
		}

		model.addAttribute("noAddress", Boolean.valueOf(hasNoDeliveryAddress()));
		model.addAttribute("addressForm", new AddressForm());
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryAddress.breadcrumb"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MultiStepCheckout.AddEditDeliveryAddressPage;
	}

	@RequestMapping(value = "/add-delivery-address", method = RequestMethod.POST)
	public String addDeliveryAddress(@Valid final AddressForm addressForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		model.addAttribute("noAddress", Boolean.valueOf(hasNoDeliveryAddress()));

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "address.error.formentry.invalid");
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			return ControllerConstants.Views.Pages.MultiStepCheckout.AddEditDeliveryAddressPage;
		}

		final AddressData newAddress = new AddressData();
		newAddress.setTitleCode(addressForm.getTitleCode());
		newAddress.setFirstName(addressForm.getFirstName());
		newAddress.setLastName(addressForm.getLastName());
		newAddress.setLine1(addressForm.getLine1());
		newAddress.setLine2(addressForm.getLine2());
		newAddress.setTown(addressForm.getTownCity());
		newAddress.setPostalCode(addressForm.getPostcode());
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(true);
		final CountryData countryData = new CountryData();
		countryData.setIsocode(addressForm.getCountryIso());
		newAddress.setCountry(countryData);
		if (userFacade.isAddressBookEmpty())
		{
			newAddress.setDefaultAddress(true);
		}
		else
		{
			newAddress.setDefaultAddress(addressForm.getDefaultAddress().booleanValue());
		}
		userFacade.addAddress(newAddress);

		// Set the new address as the selected checkout delivery address
		getCheckoutFacade().setDeliveryAddress(newAddress);
		redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("checkout.multi.address.added"));

		return REDIRECT_URL_CHOOSE_DELIVERY_METHOD;
	}

	@RequestMapping(value = "/edit-delivery-address", method = RequestMethod.POST)
	public String editDeliveryAddress(@Valid final AddressForm addressForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		model.addAttribute("noAddress", Boolean.valueOf(hasNoDeliveryAddress()));

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "address.error.formentry.invalid");
			storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
			return ControllerConstants.Views.Pages.MultiStepCheckout.AddEditDeliveryAddressPage;
		}

		final AddressData newAddress = new AddressData();
		newAddress.setId(addressForm.getAddressId());
		newAddress.setTitleCode(addressForm.getTitleCode());
		newAddress.setFirstName(addressForm.getFirstName());
		newAddress.setLastName(addressForm.getLastName());
		newAddress.setLine1(addressForm.getLine1());
		newAddress.setLine2(addressForm.getLine2());
		newAddress.setTown(addressForm.getTownCity());
		newAddress.setPostalCode(addressForm.getPostcode());
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(true);
		final CountryData countryData = new CountryData();
		countryData.setIsocode(addressForm.getCountryIso());
		newAddress.setCountry(countryData);
		if (Boolean.TRUE.equals(addressForm.getDefaultAddress()))
		{
			newAddress.setDefaultAddress(true);
		}
		userFacade.editAddress(newAddress);
		getCheckoutFacade().setDeliveryModeIfAvailable();
		redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("checkout.multi.address.updated"));

		return REDIRECT_URL_CHOOSE_DELIVERY_METHOD;
	}

	@RequestMapping(value = "/edit-delivery-address", method = RequestMethod.GET)
	public String editDeliveryAddress(@RequestParam("editAddressCode") final String editAddressCode, final Model model)
			throws CMSItemNotFoundException
	{
		if (!hasItemsInCart())
		{
			LOG.info("Missing or empty cart");
			return REDIRECT_URL_CART;
		}

		AddressData addressData = null;
		if (StringUtils.isNotEmpty(editAddressCode))
		{
			addressData = getCheckoutFacade().getDeliveryAddressForCode(editAddressCode);
		}

		final AddressForm addressForm = new AddressForm();

		final boolean hasAddressData = addressData != null;
		if (hasAddressData)
		{
			addressForm.setAddressId(addressData.getId());
			addressForm.setTitleCode(addressData.getTitleCode());
			addressForm.setFirstName(addressData.getFirstName());
			addressForm.setLastName(addressData.getLastName());
			addressForm.setLine1(addressData.getLine1());
			addressForm.setLine2(addressData.getLine2());
			addressForm.setTownCity(addressData.getTown());
			addressForm.setPostcode(addressData.getPostalCode());
			addressForm.setCountryIso(addressData.getCountry().getIsocode());
			addressForm.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
			addressForm.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
		}

		model.addAttribute("noAddress", Boolean.valueOf(hasNoDeliveryAddress()));
		model.addAttribute("edit", Boolean.valueOf(hasAddressData));
		model.addAttribute("addressForm", addressForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryAddress.breadcrumb"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MultiStepCheckout.AddEditDeliveryAddressPage;
	}


	@RequestMapping(value = "/choose-delivery-method", method = RequestMethod.GET)
	public String doChooseDeliveryModes(final Model model) throws CMSItemNotFoundException
	{
		if (!hasItemsInCart())
		{
			LOG.info("Missing or empty cart");
			return REDIRECT_URL_CART;
		}

		if (hasNoDeliveryAddress())
		{
			return REDIRECT_URL_ADD_DELIVERY_ADDRESS;
		}

		// Try to set default delivery mode
		getCheckoutFacade().setDeliveryModeIfAvailable();

		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		model.addAttribute("cartData", cartData);
		model.addAttribute("deliveryAddress", getCheckoutFacade().getCheckoutCart().getDeliveryAddress());
		model.addAttribute("selectedDeliveryMethodId", (cartData.getDeliveryMode() != null ? cartData.getDeliveryMode().getCode()
				: null));
		model.addAttribute("deliveryMethods", getCheckoutFacade().getSupportedDeliveryModes());

		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.deliveryMethod.breadcrumb"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MultiStepCheckout.ChooseDeliveryMethodPage;
	}

	/**
	 * This method gets called when the "Use Selected Delivery Method" button is clicked. It sets the selected delivery
	 * mode on the checkout facade and reloads the page highlighting the selected delivery Mode.
	 * 
	 * @param selectedDeliveryMethod
	 *           - the id of the delivery mode.
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = "/select-delivery-method", method = RequestMethod.GET)
	public String doSelectDeliveryMode(@RequestParam("delivery_method") final String selectedDeliveryMethod)
	{
		if (!hasItemsInCart())
		{
			LOG.info("Missing or empty cart");
			return REDIRECT_URL_CART;
		}

		DeliveryModeData deliveryModeData = null;

		if (StringUtils.isNotEmpty(selectedDeliveryMethod))
		{
			final List<? extends DeliveryModeData> deliveryModes = getCheckoutFacade().getSupportedDeliveryModes();
			for (final DeliveryModeData deliveryMode : deliveryModes)
			{
				if (deliveryMode.getCode().equals(selectedDeliveryMethod))
				{
					deliveryModeData = deliveryMode;
					break;
				}
			}
		}
		final boolean hasDeliveryModeData = deliveryModeData != null;
		if (hasDeliveryModeData)
		{
			getCheckoutFacade().setDeliveryMode(deliveryModeData);
		}
		return REDIRECT_URL_CHOOSE_PAYMENT_METHOD;
	}

	@RequestMapping(value = "/choose-payment-method", method = RequestMethod.GET)
	public String doChoosePaymentMethod(final Model model) throws CMSItemNotFoundException
	{
		if (!hasItemsInCart())
		{
			LOG.info("Missing or empty cart");
			return REDIRECT_URL_CART;
		}

		if (hasNoDeliveryAddress())
		{
			return REDIRECT_URL_CHOOSE_DELIVERY_ADDRESS;
		}
		if (hasNoDeliveryMode())
		{
			return REDIRECT_URL_CHOOSE_DELIVERY_METHOD;
		}

		// Try to set default payment method
		getCheckoutFacade().setPaymentDetailsIfAvailable();

		if (hasNoPaymentInfo())
		{
			return REDIRECT_URL_ADD_PAYMENT_METHOD;
		}

		final List<CCPaymentInfoData> paymentMethods = getUserFacade().getCCPaymentInfos(true);
		final CCPaymentInfoData selectedPaymentInfo = getCheckoutFacade().getCheckoutCart().getPaymentInfo();

		if (selectedPaymentInfo != null)
		{
			model.addAttribute("selectedPaymentMethodId", selectedPaymentInfo.getId());

			if (!selectedPaymentInfo.isSaved())
			{
				paymentMethods.add(selectedPaymentInfo);
			}
		}

		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		model.addAttribute("cartData", cartData);
		model.addAttribute("deliveryAddress", getCheckoutFacade().getCheckoutCart().getDeliveryAddress());
		model.addAttribute("deliveryMethod", getCheckoutFacade().getCheckoutCart().getDeliveryMode());
		model.addAttribute("paymentMethods", paymentMethods);

		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.paymentMethod.breadcrumb"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MultiStepCheckout.ChoosePaymentMethodPage;
	}

	/**
	 * This method gets called when the "Use These Payment Details" button is clicked. It sets the selected payment
	 * method on the checkout facade and reloads the page highlighting the selected payment method.
	 * 
	 * @param selectedPaymentMethodId
	 *           - the id of the payment method to use.
	 * @return - a URL to the page to load.
	 */
	@RequestMapping(value = "/select-payment-method", method = RequestMethod.GET)
	public String doSelectPaymentMethod(@RequestParam("selectedPaymentMethodId") final String selectedPaymentMethodId)
	{
		if (!hasItemsInCart())
		{
			LOG.info("Missing or empty cart");
			return REDIRECT_URL_CART;
		}

		if (StringUtils.isNotBlank(selectedPaymentMethodId))
		{
			getCheckoutFacade().setPaymentDetails(selectedPaymentMethodId);
		}
		return REDIRECT_URL_SUMMARY;
	}

	@RequestMapping(value = "/add-payment-method", method = RequestMethod.GET)
	public String doAddPaymentMethod(final Model model) throws CMSItemNotFoundException
	{
		if (!hasItemsInCart())
		{
			LOG.info("Missing or empty cart");
			return REDIRECT_URL_CART;
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute("metaRobots", "no-index,no-follow");

		// Use the checkout PCI strategy for getting the URL for creating new subscriptions.
		final CheckoutPciOptionEnum subscriptionPciOption = getCheckoutFlowFacade().getSubscriptionPciOption();
		if (CheckoutPciOptionEnum.HOP.equals(subscriptionPciOption))
		{
			// Redirect the customer to the HOP page or show error message if it fails (e.g. no HOP configurations).
			try
			{
				final HostedOrderPageData hostedOrderPageData = getHostedOrderPageFacade().beginCreateSubscription(
						"/checkout/multi/hop-response");
				model.addAttribute("hostedOrderPageData", hostedOrderPageData);
				return ControllerConstants.Views.Pages.MultiStepCheckout.HostedOrderPostPage;
			}
			catch (final Exception e)
			{
				LOG.error("Failed to build beginCreateSubscription request", e);
				GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.addPaymentDetails.generalError");
				return doChoosePaymentMethod(model);
			}
		}

		// If not using HOP we need to build up the payment details form
		final PaymentDetailsForm paymentDetailsForm = new PaymentDetailsForm();
		final AddressForm addressForm = new AddressForm();
		paymentDetailsForm.setBillingAddress(addressForm);
		model.addAttribute("hasNoPaymentInfo", Boolean.valueOf(hasNoPaymentInfo()));
		model.addAttribute(paymentDetailsForm);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.paymentMethod.breadcrumb"));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		return ControllerConstants.Views.Pages.MultiStepCheckout.AddPaymentMethodPage;
	}

	@RequestMapping(value =
	{ "/add-payment-method" }, method = RequestMethod.POST)
	public String doSavePaymentMethod(final Model model, @Valid final PaymentDetailsForm form, final BindingResult bindingResult)
			throws CMSItemNotFoundException
	{
		getPaymentDetailsValidator().validate(form, bindingResult);

		model.addAttribute("hasNoPaymentInfo", Boolean.valueOf(hasNoPaymentInfo()));
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.paymentMethod.breadcrumb"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "checkout.error.paymentethod.formentry.invalid");
			return ControllerConstants.Views.Pages.MultiStepCheckout.AddPaymentMethodPage;
		}

		final CCPaymentInfoData paymentInfoData = new CCPaymentInfoData();
		paymentInfoData.setId(form.getPaymentId());
		paymentInfoData.setCardType(form.getCardTypeCode());
		paymentInfoData.setAccountHolderName(form.getNameOnCard());
		paymentInfoData.setCardNumber(form.getCardNumber());
		paymentInfoData.setStartMonth(form.getStartMonth());
		paymentInfoData.setStartYear(form.getStartYear());
		paymentInfoData.setExpiryMonth(form.getExpiryMonth());
		paymentInfoData.setExpiryYear(form.getExpiryYear());
		paymentInfoData.setSaved(Boolean.TRUE.equals(form.getSaveInAccount()));
		paymentInfoData.setIssueNumber(form.getIssueNumber());

		final AddressData addressData;
		if (Boolean.FALSE.equals(form.getNewBillingAddress()))
		{
			addressData = getCheckoutFacade().getCheckoutCart().getDeliveryAddress();
			if (addressData == null)
			{
				GlobalMessages.addErrorMessage(model,
						"checkout.multi.paymentMethod.createSubscription.billingAddress.noneSelectedMsg");
				return ControllerConstants.Views.Pages.MultiStepCheckout.AddPaymentMethodPage;
			}

			addressData.setBillingAddress(true); // mark this as billing address
		}
		else
		{
			final AddressForm addressForm = form.getBillingAddress();

			addressData = new AddressData();
			if (addressForm != null)
			{
				addressData.setId(addressForm.getAddressId());
				addressData.setTitleCode(addressForm.getTitleCode());
				addressData.setFirstName(addressForm.getFirstName());
				addressData.setLastName(addressForm.getLastName());
				addressData.setLine1(addressForm.getLine1());
				addressData.setLine2(addressForm.getLine2());
				addressData.setTown(addressForm.getTownCity());
				addressData.setPostalCode(addressForm.getPostcode());
				addressData.setCountry(getCheckoutFacade().getCountryForIsocode(addressForm.getCountryIso()));
				addressData.setShippingAddress(Boolean.TRUE.equals(addressForm.getShippingAddress()));
				addressData.setBillingAddress(Boolean.TRUE.equals(addressForm.getBillingAddress()));
			}
		}

		paymentInfoData.setBillingAddress(addressData);

		final CCPaymentInfoData newPaymentSubscription = getCheckoutFacade().createPaymentSubscription(paymentInfoData);
		if (newPaymentSubscription != null && StringUtils.isNotBlank(newPaymentSubscription.getSubscriptionId()))
		{
			getCheckoutFacade().setPaymentDetails(newPaymentSubscription.getId());
		}
		else
		{
			GlobalMessages.addErrorMessage(model, "checkout.multi.paymentMethod.createSubscription.failedMsg");
			return ControllerConstants.Views.Pages.MultiStepCheckout.AddPaymentMethodPage;
		}

		model.addAttribute("paymentId", newPaymentSubscription.getId());

		return REDIRECT_URL_SUMMARY;
	}

	@RequestMapping(value = "/hop-response", method = RequestMethod.POST)
	public String doHandleHopResponse(final HttpServletRequest request)
	{
		final Map<String, String> resultMap = getRequestParameterMap(request);

		final CCPaymentInfoData newPaymentSubscription = getHostedOrderPageFacade().completeCreateSubscription(resultMap);
		if (newPaymentSubscription != null && StringUtils.isNotBlank(newPaymentSubscription.getSubscriptionId()))
		{
			getCheckoutFacade().setPaymentDetails(newPaymentSubscription.getId());
		}
		else
		{
			// HOP ERROR!
			LOG.error("Failed to create subscription.  Please check the log files for more information");
			return REDIRECT_URL_ERROR + "/?decision=" + resultMap.get("decision") + "&reasonCode=" + resultMap.get("reasonCode");
		}

		return REDIRECT_URL_SUMMARY;
	}

	@RequestMapping(value = "/hop-error", method = RequestMethod.GET)
	public String doHostedOrderPageError(@RequestParam(required = true) final String decision,
			@RequestParam(required = true) final String reasonCode, final Model model) throws CMSItemNotFoundException
	{

		String redirectUrl = REDIRECT_URL_CHOOSE_PAYMENT_METHOD;
		if (!hasItemsInCart())
		{
			redirectUrl = REDIRECT_URL_CART;
		}
		if (StringUtils.isBlank(redirectUrl) && hasNoDeliveryAddress())
		{
			redirectUrl = REDIRECT_URL_CHOOSE_DELIVERY_ADDRESS;
		}
		if (StringUtils.isBlank(redirectUrl) && hasNoDeliveryMode())
		{
			redirectUrl = REDIRECT_URL_CHOOSE_DELIVERY_METHOD;
		}
		model.addAttribute("decision", decision);
		model.addAttribute("reasonCode", reasonCode);
		model.addAttribute("redirectUrl", redirectUrl.replace(REDIRECT_PREFIX, ""));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.hostedOrderPageError.breadcrumb"));
		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));

		GlobalMessages.addErrorMessage(model, "checkout.multi.hostedOrderPageError.globalError");

		return ControllerConstants.Views.Pages.MultiStepCheckout.HostedOrderPageErrorPage;
	}


	protected Map<String, String> getRequestParameterMap(final HttpServletRequest request)
	{
		final Map<String, String> map = new HashMap<String, String>();

		final Enumeration myEnum = request.getParameterNames();
		while (myEnum.hasMoreElements())
		{
			final String paramName = (String) myEnum.nextElement();
			final String paramValue = request.getParameter(paramName);
			map.put(paramName, paramValue);
		}

		return map;
	}

	@RequestMapping(value = "/summary", method = RequestMethod.GET)
	public String checkoutSummary(final Model model) throws CMSItemNotFoundException
	{
		if (!hasItemsInCart())
		{
			LOG.info("Missing or empty cart");
			return REDIRECT_URL_CART;
		}

		// Try to set default delivery address and delivery mode
		getCheckoutFacade().setDeliveryAddressIfAvailable();
		getCheckoutFacade().setDeliveryModeIfAvailable();
		getCheckoutFacade().setPaymentDetailsIfAvailable();

		if (hasNoDeliveryAddress())
		{
			return REDIRECT_URL_CHOOSE_DELIVERY_ADDRESS;
		}
		if (hasNoDeliveryMode())
		{
			return REDIRECT_URL_CHOOSE_DELIVERY_METHOD;
		}
		if (hasNoPaymentInfo())
		{
			return REDIRECT_URL_CHOOSE_PAYMENT_METHOD;
		}

		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			for (final OrderEntryData entry : cartData.getEntries())
			{
				final String productCode = entry.getProduct().getCode();
				final ProductData product = getProductFacade().getProductForCodeAndOptions(productCode,
						Arrays.asList(ProductOption.BASIC, ProductOption.PRICE));
				entry.setProduct(product);
			}
		}

		model.addAttribute("cartData", cartData);
		model.addAttribute("allItems", cartData.getEntries());
		model.addAttribute("deliveryAddress", cartData.getDeliveryAddress());
		model.addAttribute("deliveryMode", cartData.getDeliveryMode());
		model.addAttribute("paymentInfo", cartData.getPaymentInfo());

		// Only request the security code if the SubscriptionPciOption is set to Default.
		final boolean requestSecurityCode = (CheckoutPciOptionEnum.DEFAULT.equals(getCheckoutFlowFacade()
				.getSubscriptionPciOption()));
		model.addAttribute("requestSecurityCode", Boolean.valueOf(requestSecurityCode));

		model.addAttribute(new PlaceOrderForm());

		storeCmsPageInModel(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(MULTI_CHECKOUT_SUMMARY_CMS_PAGE_LABEL));
		model.addAttribute(WebConstants.BREADCRUMBS_KEY,
				getResourceBreadcrumbBuilder().getBreadcrumbs("checkout.multi.summary.breadcrumb"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.MultiStepCheckout.CheckoutSummaryPage;
	}

	@RequestMapping(value = "/placeOrder")
	public String placeOrder(@ModelAttribute("placeOrderForm") final PlaceOrderForm placeOrderForm, final Model model)
			throws CMSItemNotFoundException, InvalidCartException
	{
		// validate the cart
		boolean invalid = false;
		final String securityCode = placeOrderForm.getSecurityCode();
		final CartData cartData = checkoutFacade.getCheckoutCart();
		if (cartData.getDeliveryAddress() == null)
		{
			GlobalMessages.addErrorMessage(model, "checkout.deliveryAddress.notSelected");
			invalid = true;
		}

		if (cartData.getDeliveryMode() == null)
		{
			GlobalMessages.addErrorMessage(model, "checkout.deliveryMethod.notSelected");
			invalid = true;
		}

		if (cartData.getPaymentInfo() == null)
		{
			GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.notSelected");
			invalid = true;
		}
		else
		{
			// Only require the Security Code to be entered on the summary page if the SubscriptionPciOption is set to Default.
			if (CheckoutPciOptionEnum.DEFAULT.equals(getCheckoutFlowFacade().getSubscriptionPciOption()))
			{
				if (StringUtils.isBlank(securityCode))
				{
					GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.noSecurityCode");
					invalid = true;
				}
			}
		}

		if (!placeOrderForm.isTermsCheck())
		{
			GlobalMessages.addErrorMessage(model, "checkout.error.terms.not.accepted");
			invalid = true;
		}

		if (invalid)
		{
			return checkoutSummary(model);
		}

		if (!checkoutFacade.authorizePayment(securityCode))
		{
			return checkoutSummary(model);
		}

		final OrderData orderData = checkoutFacade.placeOrder();
		if (orderData == null)
		{
			GlobalMessages.addErrorMessage(model, "checkout.placeOrder.failed");
			return checkoutSummary(model);
		}

		return REDIRECT_PREFIX + "/checkout/orderConfirmation/" + orderData.getCode();
	}


	protected boolean hasNoDeliveryAddress()
	{
		getCheckoutFacade().setDeliveryAddressIfAvailable();
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		return (cartData == null || cartData.getDeliveryAddress() == null);
	}

	protected boolean hasNoDeliveryMode()
	{
		getCheckoutFacade().setDeliveryAddressIfAvailable();
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		return (cartData == null || cartData.getDeliveryMode() == null);
	}

	protected boolean hasNoPaymentInfo()
	{
		getCheckoutFacade().setPaymentDetailsIfAvailable();
		final CartData cartData = getCheckoutFacade().getCheckoutCart();
		return (cartData == null || cartData.getPaymentInfo() == null);
	}

	public List<? extends AddressData> getDeliveryAddresses()
	{
		final List<? extends AddressData> deliveryAddresses = getCheckoutFacade().getSupportedDeliveryAddresses(true);
		return deliveryAddresses == null ? Collections.<AddressData> emptyList() : deliveryAddresses;
	}

	/**
	 * Data class used to hold a drop down select option value. Holds the code identifier as well as the display name.
	 */
	public static class SelectOption
	{
		private final String code;
		private final String name;

		public SelectOption(final String code, final String name)
		{
			this.code = code;
			this.name = name;
		}

		public String getCode()
		{
			return code;
		}

		public String getName()
		{
			return name;
		}
	}

	public static class CheckoutSteps
	{
		private final String stepName;
		private final String url;

		public CheckoutSteps(final String stepName, final String url)
		{
			this.stepName = stepName;
			this.url = url;
		}

		/**
		 * @return the stepName
		 */
		public String getStepName()
		{
			return stepName;
		}

		/**
		 * @return the URL
		 */
		public String getUrl()
		{
			return url;
		}
	}
}
