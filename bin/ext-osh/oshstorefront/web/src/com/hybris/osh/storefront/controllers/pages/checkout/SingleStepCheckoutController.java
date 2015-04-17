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

import de.hybris.platform.acceleratorfacades.user.data.AcceleratorCustomerData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.DeliveryModeData;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceData.PriceType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hybris.osh.core.constants.GeneratedOshCoreConstants.Enumerations.CheckoutPciOptionEnum;
import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.promotion.model.OshPromotionVoucherModel;
import com.hybris.osh.core.voucher.OshVoucherService;
import com.hybris.osh.facades.checkout.OshCheckoutFacade;
import com.hybris.osh.facades.customer.data.OshCustomerData;
import com.hybris.osh.facades.inventoryvalidation.InventoryValidation;
import com.hybris.osh.facades.order.data.OshCartData;
import com.hybris.osh.facades.order.data.OshOrderEntryData;
import com.hybris.osh.facades.payment.OshPaymentFacade;
import com.hybris.osh.facades.tax.OshTaxFacade;
import com.hybris.osh.facades.user.data.OshAddressData;
import com.hybris.osh.facades.user.data.StateData;
import com.hybris.osh.facades.voucher.impl.OshVoucherFacade;
import com.hybris.osh.payment.services.OshPaymentService;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.util.GlobalMessages;
import com.hybris.osh.storefront.forms.AddressForm;
import com.hybris.osh.storefront.forms.PaymentDetailsForm;
import com.hybris.osh.storefront.forms.PlaceOrderForm;
import com.hybris.osh.storefront.forms.validation.PaymentDetailsValidator;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AuthenticateUser;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AuthenticateUserResponse;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AuthenticationResult;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AuthenticationResultE;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.IdentifierTypes;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.LoyaltyLabAPIStub;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RedeemShopperRewardCerificate;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RedeemShopperRewardCerificateResponse;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.RewardRedemptionStatus;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ShopperIdentifier;
import com.hybris.osh.storefront.util.XSSFilterUtil;


/**
 * SingleStepCheckoutController
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/checkout/single")
public class SingleStepCheckoutController extends AbstractCheckoutController
{
	@SuppressWarnings("unused")
	protected static final Logger LOG = Logger.getLogger(SingleStepCheckoutController.class);

	private static final String SINGLE_STEP_CHECKOUT_SUMMARY_CMS_PAGE = "singleStepCheckoutSummaryPage";
	private static final String WSDL = "loyalty.wsdl.url";
	private static final String API_USER = "loyalty.api.user";
	private static final String API_PASSWORD = "loyalty.api.password";
	private static int loyaltyStatus = 0;
	@Resource(name = "customerFacade")
	protected CustomerFacade<AcceleratorCustomerData> customerFacade;

	@Resource(name = "commerceCartService")
	private CommerceCartService commerceCartService;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "userFacade")
	private UserFacade userFacade;

	@Resource(name = "oshTaxFacade")
	private OshTaxFacade oshTaxFacade;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "oshPaymentService")
	private OshPaymentService oshPaymentService;

	@Resource(name = "oshPaymentFacade")
	private OshPaymentFacade oshPaymentFacade;

	@Resource(name = "addressReversePopulator")
	private AddressReversePopulator addressReversePopulator;

	@Resource(name = "customerAccountService")
	private CustomerAccountService customerAccountService;

	@Resource(name = "commerceCheckoutService")
	private CommerceCheckoutService commerceCheckoutService;

	@Resource(name = "inventoryValidation")
	private InventoryValidation inventoryValidation;

	@Resource(name = "checkoutFacade")
	private OshCheckoutFacade checkoutFacade;

	@Resource(name = "paymentDetailsValidator")
	private PaymentDetailsValidator paymentDetailsValidator;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "oshVoucherFacade")
	private OshVoucherFacade oshVoucherFacade;

	@Resource(name = "paymentService")
	private PaymentService paymentService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Resource(name = "oshVoucherService")
	private OshVoucherService oshVoucherService;

	protected OshCheckoutFacade getOshCheckoutFacade()
	{
		return checkoutFacade;
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

	@ModelAttribute("states")
	public Collection<StateData> getStates()
	{
		return getOshCheckoutFacade().getDefaultDeliveryStates();
	}

	@RequestMapping(method = RequestMethod.GET)
	public String checkoutSummary()
	{
		if (hasItemsInCart())
		{
			return REDIRECT_PREFIX + "/checkout/single/summary";
		}
		return REDIRECT_PREFIX + "/cart";
	}

	@ModelAttribute("countriesStateMap")
	public Map<String, String> getCountriesMap()
	{
		final Map<String, String> countryStateMap = new HashMap<String, String>();
		final List<CountryData> countriesData = checkoutFacade.getDeliveryCountries();
		final Iterator<CountryData> itr = countriesData.listIterator();
		CountryData countryData;

		while (itr.hasNext())
		{
			countryData = itr.next();
			final StringBuilder stateBuilder = new StringBuilder();
			final List<StateData> statesModel = getOshCheckoutFacade().getStates(countryData.getIsocode());
			for (final StateData stateModel : statesModel)
			{
				stateBuilder.append(stateModel.getIsocode()).append("|").append(stateModel.getName()).append("#");
			}
			if (stateBuilder.length() > 0)
			{
				countryStateMap.put(countryData.getIsocode(), stateBuilder.substring(0, stateBuilder.length() - 1));
			}
		}
		return countryStateMap;
	}


	@RequestMapping(value = "/applyVoucher", method = RequestMethod.GET)
	public String applyVoucher(@RequestParam(required = false) final String voucherCode, final Model model,
			final HttpServletRequest request, final RedirectAttributes redirectModel) throws CMSItemNotFoundException,
			CommerceCartModificationException
	{

		final OshCustomerData customerData = (OshCustomerData) customerFacade.getCurrentCustomer();
		if (voucherCode == null)
		{
			model.addAttribute("invalidvoucher", Boolean.TRUE);
		}

		else
		{
			if (voucherCode.startsWith("441") && (voucherCode.length() == 13))
			{
				if (StringUtils.isEmpty(customerData.getLoyaltyNumber()))
				{
					redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
							Collections.singletonList(OshCoreConstants.VALID_LOYALTY_VOUCHER_INVALID_CUSTOMER));
					return REDIRECT_PREFIX + "/cart";
				}

			}
			if (!oshVoucherService.isValidVoucherCode(voucherCode))
			{
				redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
						Collections.singletonList(OshCoreConstants.VALID_VOUCHER_CODE));
				return REDIRECT_PREFIX + "/checkout/single/summary";
			}
			else
			{
				if (oshVoucherFacade.redeemVoucher(voucherCode))
				{
					if (voucherCode.startsWith("441") && voucherCode.length() == 13)
					{
						request.getSession().setAttribute("loyaltyVoucher", voucherCode);
						redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
								Collections.singletonList(OshCoreConstants.LOYALITY_VOUCHER_SUCCESS));
					}
					else
					{
						redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
								Collections.singletonList(OshCoreConstants.VOUCHER_SUCCESS));
					}

					/*
					 * CartData cartData = checkoutFacade.getCheckoutCart(); cartData = checkoutFacade.getCheckoutCart();
					 * oshTaxFacade.calculateTax(cartData);
					 */
				}
				else
				{
					redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
							Collections.singletonList(OshCoreConstants.VOUCHER_RESTRICTION_FAIL));
				}
			}
		}
		request.getSession().setAttribute("taxCalculated", false);
		return REDIRECT_PREFIX + "/checkout/single/summary";
	}

	@RequestMapping(value = "/releaseVoucher", method = RequestMethod.GET)
	public String releaseVoucher(final HttpServletRequest request) throws CMSItemNotFoundException,
			CommerceCartModificationException
	{
		final CartModel cartModel = cartService.getSessionCart();
		String voucherCode = null;
		final List<DiscountModel> discounts = cartModel.getDiscounts();

		if (discounts != null && !discounts.isEmpty())
		{
			final DiscountModel voucherModel = discounts.get(0);
			if (voucherModel instanceof PromotionVoucherModel)
			{
				voucherCode = ((PromotionVoucherModel) voucherModel).getVoucherCode();
			}
			else if (voucherModel instanceof OshPromotionVoucherModel)
			{
				voucherCode = ((OshPromotionVoucherModel) voucherModel).getVoucherCode();
			}

			oshVoucherService.removeVoucher(voucherCode, cartModel);
			request.getSession().setAttribute("taxCalculated", false);
		}
		return REDIRECT_PREFIX + "/checkout/single/summary";

		/*
		 * CartData cartData = checkoutFacade.getCheckoutCart(); cartData = checkoutFacade.getCheckoutCart();
		 * oshTaxFacade.calculateTax(cartData); return REDIRECT_PREFIX + "/checkout/single/summary";
		 */
	}





	@SuppressWarnings("boxing")
	@RequestMapping(value = "/summary", method = RequestMethod.GET)
	public String checkoutSummary(final Model model, final HttpServletRequest request) throws CMSItemNotFoundException
	{
		boolean storepickupPrice = false;
		boolean shippingPrice = false;
		boolean outOfStock = false;
		if ((request.getSession().getAttribute("cartMerged") != null && request.getSession().getAttribute("cartMerged")
				.equals("YES"))
				&& request.getSession().getAttribute("cartMergeFlagged") == null)
		{

			GlobalMessages.addErrorMessage(model, "basket.information.merge.successful");
			request.getSession().setAttribute("cartMergeFlagged", true);
		}



		if (!hasItemsInCart())
		{
			// no items in the cart
			return FORWARD_PREFIX + "/cart";
		}
		// Try to set default delivery address and delivery mode
		checkoutFacade.setDeliveryAddressIfAvailable();
		checkoutFacade.setPaymentDetailsIfAvailable();
		checkoutFacade.setDeliveryModeIfAvailable();


		/*
		 * if (request.getSession().getAttribute(OshCoreConstants.ITEM_UPDATE_FLAG) != null) {
		 * oshVoucherFacade.updateVoucher(); request.getSession().removeAttribute(OshCoreConstants.ITEM_UPDATE_FLAG); }
		 */

		final PointOfServiceData pos = inventoryValidation.getMyStore();
		if (pos != null)
		{
			model.addAttribute("posData", pos);
		}

		CartData cartData = checkoutFacade.getCheckoutCart();


		BigDecimal sub = new BigDecimal(0);

		Boolean taxCalculated = (Boolean) request.getSession().getAttribute("taxCalculated");

		//final CustomerModel guest = (CustomerModel) userService.getCurrentUser();
		if (taxCalculated == null)
		{
			request.getSession().setAttribute("taxCalculated", false);
			taxCalculated = false;
		}

		final HttpSession taxSession = request.getSession();
		taxSession.getAttribute("cartTotal");
		if (taxSession.getAttribute("cartTotal") == null)
		{
			taxSession.setAttribute("cartTotal", cartData.getSubTotal().getValue().doubleValue());
		}


		if (!taxSession.getAttribute("cartTotal").equals(cartData.getSubTotal().getValue().doubleValue()))
		{
			checkoutFacade.getSupportedDeliveryModes();
			if (checkoutFacade.isWeightForHomeDirect())
			{
				GlobalMessages.addErrorMessage(model, "checkout.homeDirectdeliveryMode.Selected");
			}
			oshTaxFacade.calculateTax(cartData);
			cartData = checkoutFacade.getCheckoutCart();
			taxSession.setAttribute("cartTotal", cartData.getSubTotal().getValue().doubleValue());
			request.getSession().setAttribute("taxCalculated", true);
		}

		/*
		 * if (request.getSession().getAttribute("cartMergeFlagged") != null &&
		 * request.getSession().getAttribute("cartMergeFlagged").equals(true)) { oshVoucherFacade.updateVoucher();
		 * taxCalculated = false; System.out.println("-------------------------------------------------------");
		 * //request.getSession().setAttribute("cartMergeFlagged", null); }
		 */

		if (cartData.getPaymentInfo() != null && cartData.getDeliveryAddress() != null && !taxCalculated
				&& cartData.getPaymentInfo().getBillingAddress() != null)
		{

			oshTaxFacade.calculateTax(cartData);
			cartData = checkoutFacade.getCheckoutCart();
			request.getSession().setAttribute("taxCalculated", true);
		}

		cartData = checkoutFacade.getCheckoutCart();
		System.out.println(cartData.getEntries().size());


		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			final int index = 0;
			for (final OrderEntryData entry : cartData.getEntries())
			{

				if (!((OshOrderEntryData) entry).getOrderType().equalsIgnoreCase(OshCoreConstants.WAREHOUSE)
						&& !((OshOrderEntryData) entry).getOrderType().equalsIgnoreCase(OshCoreConstants.DROPSHIP))
				{

					outOfStock = checkoutFacade.checkStoreStock(entry.getProduct().getCode(), pos.getName());
					storepickupPrice = true;
					model.addAttribute("storepickup", Boolean.valueOf(storepickupPrice));

				}
				else
				{
					shippingPrice = true;
					model.addAttribute("shipping", Boolean.valueOf(shippingPrice));
				}

				if (storepickupPrice && !shippingPrice)
				{
					final BigDecimal totalPrice = ((OshCartData) cartData).getTotalAmount().getValue();
					if (cartData.getDeliveryCost() != null)
					{
						final BigDecimal actualPrice = totalPrice.subtract(cartData.getDeliveryCost().getValue());
						final PriceData price = priceDataFactory.create(PriceType.BUY, BigDecimal.valueOf(actualPrice.doubleValue()),
								commonI18NService.getCurrentCurrency().getIsocode());
						//	cartData.setTotalPrice(price);
						((OshCartData) cartData).setTotalAmount(price);
					}
					checkoutFacade.removeDeliveryMode();
					cartData.setDeliveryCost(null);

				}

				final String productCode = entry.getProduct().getCode();
				final ProductData product = productFacade.getProductForCodeAndOptions(productCode,
						Arrays.asList(ProductOption.BASIC, ProductOption.PRICE));
				entry.setProduct(product);
				getPromotionDetails(cartData, entry, index);
				sub = sub.add(entry.getTotalPrice().getValue());
				final PriceData subTotal = priceDataFactory.create(PriceType.BUY, sub, OshCoreConstants.DEFAULT_CURRENCY);
				cartData.setSubTotal(subTotal);

			}
		}

		if (outOfStock)
		{
			GlobalMessages.addErrorMessage(model, "product.outOfStock");
		}
		model.addAttribute("allItems", cartData.getEntries());
		model.addAttribute("deliveryAddress", cartData.getDeliveryAddress());
		model.addAttribute("deliveryMode", cartData.getDeliveryMode());
		model.addAttribute("paymentInfo", cartData.getPaymentInfo());
		model.addAttribute("outOfStock", Boolean.valueOf(outOfStock));
		model.addAttribute(new AddressForm());
		model.addAttribute(new PaymentDetailsForm());
		model.addAttribute(new PlaceOrderForm());
		model.addAttribute("surcharge", ((OshCartData) cartData).getShippingSurchargeStatus());
		storeCmsPageInModel(model, getContentPageForLabelOrId(SINGLE_STEP_CHECKOUT_SUMMARY_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(SINGLE_STEP_CHECKOUT_SUMMARY_CMS_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		model.addAttribute("cartData", cartData);

		return ControllerConstants.Views.Pages.SingleStepCheckout.CheckoutSummaryPage;
	}

	@ResponseBody
	@RequestMapping(value = "/summary/getCheckoutCart.json", method = RequestMethod.GET)
	public CartData getCheckoutCart()
	{

		return checkoutFacade.getCheckoutCart();
	}


	@ResponseBody
	@RequestMapping(value = "/summary/getDeliveryAddresses.json", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public List<? extends AddressData> getDeliveryAddresses()
	{
		final List<? extends AddressData> deliveryAddresses = checkoutFacade.getSupportedDeliveryAddresses(true);
		return deliveryAddresses == null ? Collections.<AddressData> emptyList() : deliveryAddresses;
	}

	@SuppressWarnings("boxing")
	@ResponseBody
	@RequestMapping(value = "/summary/setDeliveryAddress.json", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public CartData setDeliveryAddress(@RequestParam(value = "addressId") final String addressId,
			final HttpServletRequest request, final Model model)
	{
		AddressData addressData = null;
		final List<? extends AddressData> deliveryAddresses = checkoutFacade.getSupportedDeliveryAddresses(false);
		for (final AddressData deliveryAddress : deliveryAddresses)
		{
			if (deliveryAddress.getId().equals(addressId))
			{
				addressData = deliveryAddress;
				break;
			}
		}

		if (addressData != null && checkoutFacade.setDeliveryAddress(addressData))
		{
			CartData cartData = checkoutFacade.getCheckoutCart();
			oshTaxFacade.calculateTax(cartData);
			cartData = checkoutFacade.getCheckoutCart();
			model.addAttribute("cartData", cartData);
			model.addAttribute("surcharge", ((OshCartData) cartData).getShippingSurchargeStatus());
			return cartData;
		}

		return null;
	}

	@ResponseBody
	@RequestMapping(value = "/summary/getDeliveryModes.json", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public List<? extends DeliveryModeData> getDeliveryModes(final Model model)
	{
		final List<? extends DeliveryModeData> deliveryModes = checkoutFacade.getSupportedDeliveryModes();
		if (checkoutFacade.getCheckoutCart().getDeliveryMode() != null)
		{
			model.addAttribute("deliveryModeCode", checkoutFacade.getCheckoutCart().getDeliveryMode().getCode());
		}
		return deliveryModes == null ? Collections.<ZoneDeliveryModeData> emptyList() : deliveryModes;
	}

	@ResponseBody
	@RequestMapping(value = "/summary/setDeliveryMode.json", method = RequestMethod.POST)
	public CartData setDeliveryMode(@RequestParam(value = "modeCode") final String modeCode)
	{
		DeliveryModeData selectedDeliveryMode = null;

		final List<? extends DeliveryModeData> deliveryModes = checkoutFacade.getSupportedDeliveryModes();
		for (final DeliveryModeData deliveryMode : deliveryModes)
		{
			if (deliveryMode.getCode().equals(modeCode))
			{
				selectedDeliveryMode = deliveryMode;
				break;
			}
		}

		if (selectedDeliveryMode != null && checkoutFacade.setDeliveryMode(selectedDeliveryMode))
		{
			CartData cartData = checkoutFacade.getCheckoutCart();
			if (cartData.getPaymentInfo() != null && cartData.getDeliveryAddress() != null
					&& cartData.getPaymentInfo().getBillingAddress() != null && oshVoucherFacade.updateVoucher())
			{
				oshTaxFacade.calculateTax(cartData);
				checkoutFacade.getCheckoutCart();
			}
			cartData = checkoutFacade.getCheckoutCart();
			return cartData;
		}
		return null;
	}

	@SuppressWarnings("boxing")
	@RequestMapping(value = "/summary/getDeliveryAddressForm.json", method = RequestMethod.GET)
	public String getDeliveryAddressForm(final Model model, @RequestParam(value = "addressId") final String addressId,
			@RequestParam(value = "createUpdateStatus") final String createUpdateStatus)
	{
		OshAddressData addressData = null;
		if (addressId != null && !addressId.isEmpty())
		{
			addressData = (OshAddressData) checkoutFacade.getDeliveryAddressForCode(addressId);
		}

		final AddressForm addressForm = new AddressForm();

		final boolean hasAddressData = addressData != null;
		if (hasAddressData)
		{
			addressForm.setEmail(addressData.getEmail());
			addressForm.setAddressId(addressData.getId());
			//addressForm.setTitleCode(addressData.getTitleCode());
			addressForm.setFirstName(addressData.getFirstName());
			addressForm.setLastName(addressData.getLastName());
			addressForm.setLine1(addressData.getLine1());
			addressForm.setLine2(addressData.getLine2());
			addressForm.setTownCity(addressData.getTown());
			addressForm.setPostcode(addressData.getPostalCode());
			addressForm.setPhoneNo(addressData.getPhone());
			addressForm.setCountryIso(addressData.getCountry().getIsocode());
			addressForm.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
			addressForm.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
			addressForm.setStateIso(addressData.getState().getIsocode());
			model.addAttribute(addressForm);
		}
		else
		{
			addressForm.setCountryIso(OshCoreConstants.DEFAULT_COUNTRY_CODE);
			addressForm.setStateIso(OshCoreConstants.DEFAULT_STATE_CODE);
			model.addAttribute(addressForm);
		}
		final CartData cartData = checkoutFacade.getCheckoutCart();
		model.addAttribute("edit", Boolean.valueOf(hasAddressData));
		model.addAttribute("createUpdateStatus", createUpdateStatus);
		model.addAttribute("surcharge", ((OshCartData) cartData).getShippingSurchargeStatus());
		return ControllerConstants.Views.Fragments.SingleStepCheckout.DeliveryAddressFormPopup;
	}

	@SuppressWarnings("boxing")
	@RequestMapping(value = "/summary/createUpdateDeliveryAddress.json", method = RequestMethod.POST)
	public String createUpdateDeliveryAddress(final Model model, @Valid final AddressForm form, final HttpServletRequest request,
			final BindingResult bindingResult)
	{
		if (bindingResult.hasErrors())
		{
			model.addAttribute("edit", Boolean.valueOf(StringUtils.isNotBlank(form.getAddressId())));

			return ControllerConstants.Views.Fragments.SingleStepCheckout.DeliveryAddressFormPopup;
		}
		CartData cartData = checkoutFacade.getCheckoutCart();
		final OshAddressData oldAddressData = (OshAddressData) cartData.getDeliveryAddress();

		String shippingAddress = null;
		if (oldAddressData != null)
		{
			shippingAddress = oldAddressData.getState().getIsocode() + oldAddressData.getCountry().getIsocode()
					+ oldAddressData.getTown() + oldAddressData.getPostalCode();

		}
		//create delivery address and set it on cart
		final OshAddressData addressData = new OshAddressData();
		addressData.setEmail(form.getEmail());
		addressData.setId(form.getAddressId());
		//addressData.setTitleCode(form.getTitleCode());
		addressData.setFirstName(form.getFirstName());
		addressData.setLastName(form.getLastName());
		addressData.setLine1(form.getLine1());
		addressData.setLine2(form.getLine2());
		addressData.setTown(form.getTownCity());
		addressData.setPostalCode(form.getPostcode());
		addressData.setPhone(form.getPhoneNo());
		addressData.setCountry(checkoutFacade.getCountryForIsocode(form.getCountryIso()));
		final StateData stateData = new StateData();
		stateData.setIsocode(form.getStateIso());
		addressData.setState(stateData);
		if (!checkoutFacade.isValidZipcode(form.getStateIso(), form.getPostcode()))
		{
			model.addAttribute("zipCodeErrorMsg", Boolean.TRUE);
			GlobalMessages.addErrorMessage(model, "form.global.error");
			return ControllerConstants.Views.Fragments.SingleStepCheckout.DeliveryAddressFormPopup;
		}
		addressData.setShippingAddress(Boolean.TRUE.equals(form.getShippingAddress())
				|| Boolean.TRUE.equals(form.getSaveInAddressBook()));
		addressData.setBillingAddress(Boolean.TRUE.equals(form.getBillingAddress()));

		if (StringUtils.isBlank(form.getAddressId()))
		{
			userFacade.addAddress(addressData);
		}
		else
		{
			userFacade.editAddress(addressData);
			final String editShippingAddress = addressData.getState().getIsocode() + addressData.getCountry().getIsocode()
					+ addressData.getTown() + addressData.getPostalCode();
			cartData = checkoutFacade.getCheckoutCart();
			if (cartData.getPaymentInfo() != null && cartData.getPaymentInfo().getBillingAddress() != null
					&& !shippingAddress.equals(editShippingAddress))
			{

				oshVoucherFacade.updateVoucher();
				oshTaxFacade.calculateTax(cartData);
				cartData = checkoutFacade.getCheckoutCart();
				model.addAttribute("cartData", cartData);

			}
		}

		checkoutFacade.setDeliveryAddress(addressData);

		if (checkoutFacade.getCheckoutCart().getDeliveryMode() == null)
		{
			checkoutFacade.setDeliveryModeIfAvailable();
		}

		model.addAttribute("createUpdateStatus", "Success");
		model.addAttribute("addressId", addressData.getId());
		model.addAttribute("surcharge", ((OshCartData) cartData).getShippingSurchargeStatus());
		return REDIRECT_PREFIX + "/checkout/single/summary/getDeliveryAddressForm.json?addressId=" + addressData.getId()
				+ "&createUpdateStatus=Success";

	}

	@ResponseBody
	@RequestMapping(value = "/summary/getSavedCards.json", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public List<CCPaymentInfoData> getSavedCards()
	{
		final List<CCPaymentInfoData> paymentInfos = userFacade.getCCPaymentInfos(true);
		return paymentInfos == null ? Collections.<CCPaymentInfoData> emptyList() : paymentInfos;
	}


	@ResponseBody
	@RequestMapping(value = "/summary/removeSavedAddress.json", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String removeSavedAddress(@RequestParam(value = "addressId") final String addressId)
	{
		userFacade.removeAddress(userFacade.getAddressForCode(addressId));
		return "success";

	}

	@ResponseBody
	@RequestMapping(value = "/summary/removeSavedCards.json", method = RequestMethod.POST)
	public String removeSavedCards(@RequestParam(value = "paymentId") final String paymentId)
	{
		userFacade.removeCCPaymentInfo(paymentId);
		return "success";
	}


	@ResponseBody
	@RequestMapping(value = "/summary/setPaymentDetails.json", method = RequestMethod.POST)
	public CartData setPaymentDetails(@RequestParam(value = "paymentId") final String paymentId)
	{
		if (StringUtils.isNotBlank(paymentId))
		{
			if (checkoutFacade.setPaymentDetails(paymentId))
			{
				return checkoutFacade.getCheckoutCart();
			}
		}

		return null;
	}

	@RequestMapping(value = "/summary/getPaymentDetailsForm.json", method = RequestMethod.GET)
	public String getPaymentDetailsForm(final Model model,
			@RequestParam(value = "createUpdateStatus") final String createUpdateStatus)
	{
		final PaymentDetailsForm paymentDetailsForm = new PaymentDetailsForm();
		final List<CCPaymentInfoData> paymentInfos = userFacade.getCCPaymentInfos(true);
		final OshAddressData addressData = (OshAddressData) getCheckoutCart().getDeliveryAddress();
		final CartData cartData = getCheckoutCart();
		final CartEntryModel cartEntryModel = cartService.getEntryForNumber(cartService.getSessionCart(), cartData.getEntries()
				.iterator().next().getEntryNumber());
		boolean storepickup = false;

		if (cartEntryModel != null && !cartEntryModel.getOrderType().equalsIgnoreCase(OshCoreConstants.ONLINE))
		{
			storepickup = true;
		}
		model.addAttribute("storepickup", storepickup);

		OshAddressData billingAddressData = null;
		if (checkoutFacade.getCheckoutCart().getPaymentInfo() != null)
		{
			billingAddressData = (OshAddressData) checkoutFacade.getCheckoutCart().getPaymentInfo().getBillingAddress();
		}
		final AddressForm addressForm = new AddressForm();
		if (addressData != null || billingAddressData != null)
		{
			if (checkoutFacade.getCheckoutCart().getPaymentInfo() != null)
			{
				//final CCPaymentInfoData ccInfo = checkoutFacade.getCheckoutCart().getPaymentInfo();
				//paymentDetailsForm.setCardNumber(ccInfo.getCardNumber());
				//paymentDetailsForm.setCardTypeCode(ccInfo.getCardType());
				//paymentDetailsForm.setExpiryMonth(ccInfo.getExpiryMonth());
				//paymentDetailsForm.setExpiryYear(ccInfo.getExpiryYear());
				//paymentDetailsForm.setSecurityCode("123");
				model.addAttribute("paymentId", checkoutFacade.getCheckoutCart().getPaymentInfo().getId());
				addressForm.setEmail(billingAddressData.getEmail());
				addressForm.setAddressId(billingAddressData.getId());
				//addressForm.setTitleCode(addressData.getTitleCode());
				addressForm.setFirstName(billingAddressData.getFirstName());
				addressForm.setLastName(billingAddressData.getLastName());
				addressForm.setLine1(billingAddressData.getLine1());
				addressForm.setLine2(billingAddressData.getLine2());
				addressForm.setTownCity(billingAddressData.getTown());
				addressForm.setPostcode(billingAddressData.getPostalCode());
				addressForm.setPhoneNo(billingAddressData.getPhone());
				addressForm.setCountryIso(billingAddressData.getCountry().getIsocode());
				addressForm.setShippingAddress(Boolean.valueOf(billingAddressData.isShippingAddress()));
				addressForm.setBillingAddress(Boolean.valueOf(billingAddressData.isBillingAddress()));
				addressForm.setStateIso(billingAddressData.getState().getIsocode());

			}
			else
			{
				addressForm.setEmail(addressData.getEmail());
				addressForm.setAddressId(addressData.getId());
				//addressForm.setTitleCode(addressData.getTitleCode());
				addressForm.setFirstName(addressData.getFirstName());
				addressForm.setLastName(addressData.getLastName());
				addressForm.setLine1(addressData.getLine1());
				addressForm.setLine2(addressData.getLine2());
				addressForm.setTownCity(addressData.getTown());
				addressForm.setPostcode(addressData.getPostalCode());
				addressForm.setPhoneNo(addressData.getPhone());
				addressForm.setCountryIso(addressData.getCountry().getIsocode());
				addressForm.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
				addressForm.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
				addressForm.setStateIso(addressData.getState().getIsocode());
				paymentDetailsForm.setNewBillingAddress(Boolean.FALSE);

			}
		}
		else
		{

			paymentDetailsForm.setNewBillingAddress(Boolean.FALSE);
			addressForm.setCountryIso(OshCoreConstants.DEFAULT_COUNTRY_CODE);
			addressForm.setStateIso(OshCoreConstants.DEFAULT_STATE_CODE);
		}
		paymentDetailsForm.setBillingAddress(addressForm);
		if (paymentInfos != null)
		{
			model.addAttribute("paymentInfos", paymentInfos);
		}
		model.addAttribute("paymentInfoData", userFacade.getCCPaymentInfos(true));
		model.addAttribute(paymentDetailsForm);
		model.addAttribute("createUpdateStatus", createUpdateStatus);
		return ControllerConstants.Views.Fragments.SingleStepCheckout.PaymentDetailsFormPopup;
	}

	@ResponseBody
	@RequestMapping(value = "/summary/updatePaymentDetailsForm.json", method = RequestMethod.GET, produces = "Application/json")
	public PaymentDetailsForm updatePaymentDetailsForm(final Model model, @RequestParam(value = "paymentId") final String paymentId)
	{
		final PaymentDetailsForm paymentDetailsForm = new PaymentDetailsForm();

		final OshAddressData addressData = (OshAddressData) userFacade.getCCPaymentInfoForCode(paymentId).getBillingAddress();
		final CCPaymentInfoData paymentInfoData = userFacade.getCCPaymentInfoForCode(paymentId);
		final AddressForm addressForm = new AddressForm();
		addressForm.setEmail(addressData.getEmail());
		addressForm.setAddressId(addressData.getId());
		//addressForm.setTitleCode(addressData.getTitleCode());
		addressForm.setFirstName(addressData.getFirstName());
		addressForm.setLastName(addressData.getLastName());
		addressForm.setLine1(addressData.getLine1());
		addressForm.setLine2(addressData.getLine2());
		addressForm.setTownCity(addressData.getTown());
		addressForm.setPostcode(addressData.getPostalCode());
		addressForm.setPhoneNo(addressData.getPhone());
		addressForm.setCountryIso(addressData.getCountry().getIsocode());
		addressForm.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
		addressForm.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
		addressForm.setStateIso(addressData.getState().getIsocode());
		paymentDetailsForm.setBillingAddress(addressForm);
		paymentDetailsForm.setPaymentId(paymentId);
		paymentDetailsForm.setCardTypeCode(paymentInfoData.getCardType());
		paymentDetailsForm.setNameOnCard(paymentInfoData.getAccountHolderName());
		paymentDetailsForm.setCardNumber(paymentInfoData.getCardNumber());
		paymentDetailsForm.setStartMonth(paymentInfoData.getStartMonth());
		paymentDetailsForm.setStartYear(paymentInfoData.getStartYear());
		paymentDetailsForm.setExpiryMonth(paymentInfoData.getExpiryMonth());
		paymentDetailsForm.setExpiryYear(paymentInfoData.getExpiryYear());
		paymentDetailsForm.setSaveInAccount(paymentInfoData.isSaved());
		paymentDetailsForm.setIssueNumber(paymentInfoData.getIssueNumber());
		final CartData cartData = checkoutFacade.getCheckoutCart();
		model.addAttribute("cartData", cartData);
		return paymentDetailsForm;
	}

	@RequestMapping(value = "/summary/createPaymentDetails.json", method = RequestMethod.POST)
	public String createUpdatePaymentDetails(final Model model, @Valid final PaymentDetailsForm form,
			final BindingResult bindingResult, final HttpServletRequest request)
	{
		paymentDetailsValidator.validate(form, bindingResult);

		if (form.getPaymentId().equals("") && bindingResult.hasErrors())
		{
			final CartData cartData = getCheckoutCart();
			final CartEntryModel cartEntryModel = cartService.getEntryForNumber(cartService.getSessionCart(), cartData.getEntries()
					.iterator().next().getEntryNumber());
			boolean storepickup = false;
			if (cartEntryModel != null && !cartEntryModel.getOrderType().equalsIgnoreCase(OshCoreConstants.ONLINE))
			{
				storepickup = true;
			}
			model.addAttribute("storepickup", storepickup);
			if (form.getBillingAddress() == null)
			{
				final AddressForm addressForm = new AddressForm();
				final OshAddressData addressData = (OshAddressData) checkoutFacade.getCheckoutCart().getDeliveryAddress();
				if (addressData != null)
				{
					addressForm.setEmail(addressData.getEmail());
					addressForm.setAddressId(addressData.getId());
					//addressForm.setTitleCode(addressData.getTitleCode());
					addressForm.setPhoneNo(addressData.getPhone());
					addressForm.setFirstName(addressData.getFirstName());
					addressForm.setLastName(addressData.getLastName());
					addressForm.setLine1(addressData.getLine1());
					addressForm.setLine2(addressData.getLine2());
					addressForm.setTownCity(addressData.getTown());
					addressForm.setPostcode(addressData.getPostalCode());
					addressForm.setCountryIso(addressData.getCountry().getIsocode());
					addressForm.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
					addressForm.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
					addressForm.setStateIso(addressData.getState().getIsocode());
					//final PaymentDetailsForm paymentDetailForm = new PaymentDetailsForm();
					form.setBillingAddress(addressForm);
				}
				model.addAttribute("paymentDetailsForm", form);
				return ControllerConstants.Views.Fragments.SingleStepCheckout.PaymentDetailsFormPopup;
			}
			else
			{
				model.addAttribute("paymentDetailsForm", form);
				return ControllerConstants.Views.Fragments.SingleStepCheckout.PaymentDetailsFormPopup;
			}
		}
		CCPaymentInfoData paymentInfoData = new CCPaymentInfoData();
		if (!form.getPaymentId().isEmpty())
		{

			paymentInfoData = userFacade.getCCPaymentInfoForCode(form.getPaymentId());

		}
		else
		{
			paymentInfoData.setId(form.getPaymentId());
			paymentInfoData.setCardType(form.getCardTypeCode());
			paymentInfoData.setAccountHolderName(form.getNameOnCard());
			paymentInfoData.setCardNumber(form.getCardNumber());
			paymentInfoData.setStartMonth(form.getStartMonth());
			paymentInfoData.setStartYear(form.getStartYear());
			paymentInfoData.setExpiryMonth(form.getExpiryMonth());
			paymentInfoData.setExpiryYear(form.getExpiryYear());
			paymentInfoData.setSaved(Boolean.TRUE.equals(form.getSaveInAccount()));
			paymentInfoData.setIssueNumber(form.getSecurityCode());
		}

		OshAddressData addressData;
		final AddressModel newAddress = modelService.create(AddressModel.class);
		if (Boolean.FALSE.equals(form.getNewBillingAddress()))
		{
			try
			{
				addressData = (OshAddressData) checkoutFacade.getCheckoutCart().getPaymentInfo().getBillingAddress();
			}
			catch (final NullPointerException e)
			{
				addressData = (OshAddressData) getCheckoutCart().getDeliveryAddress();
			}
			final AddressForm addressForm = new AddressForm();
			if (addressData == null)
			{
				GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.createSubscription.billingAddress.noneSelected");
				return ControllerConstants.Views.Fragments.SingleStepCheckout.PaymentDetailsFormPopup;
			}
			else
			{
				addressForm.setEmail(addressData.getEmail());
				addressForm.setAddressId(addressData.getId());
				//addressForm.setTitleCode(addressData.getTitleCode());
				addressForm.setFirstName(addressData.getFirstName());
				addressForm.setLastName(addressData.getLastName());
				addressForm.setLine1(addressData.getLine1());
				addressForm.setLine2(addressData.getLine2());
				addressForm.setTownCity(addressData.getTown());
				addressForm.setPostcode(addressData.getPostalCode());
				addressForm.setPhoneNo(addressData.getPhone());
				addressForm.setCountryIso(addressData.getCountry().getIsocode());
				addressForm.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
				addressForm.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
				addressForm.setStateIso(addressData.getState().getIsocode());
				//final PaymentDetailsForm paymentDetailForm = new PaymentDetailsForm();
				form.setBillingAddress(addressForm);
			}
			addressData.setBillingAddress(true); // mark this as billing address
		}
		else
		{
			final AddressForm addressForm = form.getBillingAddress();

			addressData = new OshAddressData();
			if (addressForm != null)
			{
				//addressData.setTitleCode(addressForm.getTitleCode());
				addressData.setEmail(addressForm.getEmail());
				addressData.setFirstName(addressForm.getFirstName());
				addressData.setLastName(addressForm.getLastName());
				addressData.setLine1(addressForm.getLine1());
				addressData.setLine2(addressForm.getLine2());
				addressData.setTown(addressForm.getTownCity());
				addressData.setPostalCode(addressForm.getPostcode());
				addressData.setPhone(addressForm.getPhoneNo());
				addressData.setCountry(checkoutFacade.getCountryForIsocode(addressForm.getCountryIso()));
				addressData.setShippingAddress(Boolean.TRUE.equals(addressForm.getShippingAddress()));
				final StateData stateData = new StateData();
				stateData.setIsocode(addressForm.getStateIso());
				addressData.setState(stateData);
				addressData.setBillingAddress(true);
				addressReversePopulator.populate(addressData, newAddress);
				// Store the address against the user
				customerAccountService.saveAddressEntry((CustomerModel) userService.getCurrentUser(), newAddress);
				addressData.setId(newAddress.getPk().toString());
			}
		}
		String paymentSubscription;
		paymentInfoData.setBillingAddress(addressData);
		if (!form.getPaymentId().isEmpty())
		{
			final CardInfo cardInfo = oshPaymentFacade.createCCDetails(paymentInfoData);
			final String merchantTransactionCode = userService.getCurrentUser().getUid() + "-" + UUID.randomUUID();
			paymentSubscription = paymentInfoData.getSubscriptionId();
			cardInfo.setBillingInfo(oshPaymentFacade.createBillingInfo(newAddress));
			userFacade.updateCCPaymentInfo(paymentInfoData);
			final PaymentTransactionEntryModel entryModel = oshPaymentService.updateSubscription(merchantTransactionCode,
					paymentInfoData.getSubscriptionId(), commerceCheckoutService.getPaymentProvider(), newAddress, cardInfo);
			if (entryModel.getSubscriptionID() != null)
			{
				userFacade.getCCPaymentInfoForCode(form.getPaymentId()).setBillingAddress(addressData);
				checkoutFacade.setPaymentDetails(paymentInfoData.getId());
			}
			else
			{
				GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.createSubscription.failed");
				return ControllerConstants.Views.Fragments.SingleStepCheckout.PaymentDetailsFormPopup;
			}
			model.addAttribute("createUpdateStatus", "Success");
			model.addAttribute("paymentId", paymentInfoData.getId());
		}
		else
		{
			final CCPaymentInfoData newPaymentSubscription = checkoutFacade.createPaymentSubscription(paymentInfoData);
			if (newPaymentSubscription != null && StringUtils.isNotBlank(newPaymentSubscription.getSubscriptionId()))
			{
				checkoutFacade.setPaymentDetails(newPaymentSubscription.getId());
				paymentSubscription = newPaymentSubscription.getId();
			}
			else
			{
				final List<CCPaymentInfoData> paymentInfos = userFacade.getCCPaymentInfos(true);
				if (paymentInfos != null)
				{
					model.addAttribute("paymentInfos", paymentInfos);
				}
				model.addAttribute("paymentInfoData", userFacade.getCCPaymentInfos(true));
				model.addAttribute("createUpdateStatus", "Failed");
				boolean storepickup = false;
				final CartData cartData = getCheckoutCart();
				final CartEntryModel cartEntryModel = cartService.getEntryForNumber(cartService.getSessionCart(), cartData
						.getEntries().iterator().next().getEntryNumber());
				if (cartEntryModel != null && !cartEntryModel.getOrderType().equalsIgnoreCase(OshCoreConstants.ONLINE))
				{
					storepickup = true;
				}
				model.addAttribute("storepickup", storepickup);
				model.addAttribute("paymentDetailsForm", form);
				final CartData cartData1 = checkoutFacade.getCheckoutCart();
				if (cartData1.getPaymentInfo() != null)
				{
					model.addAttribute("paymentId", checkoutFacade.getCheckoutCart().getPaymentInfo().getId());
				}
				final Calendar cal = Calendar.getInstance();
				if (Integer.parseInt(form.getExpiryYear()) <= cal.get(Calendar.YEAR)
						&& Integer.parseInt(form.getExpiryMonth()) < cal.get(Calendar.MONTH) + 1)
				{
					GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.createSubscription.failed.forDate");
				}
				else
				{
					GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.createSubscription.failed.forCardNo");
				}
				return ControllerConstants.Views.Fragments.SingleStepCheckout.PaymentDetailsFormPopup;
			}

			model.addAttribute("createUpdateStatus", "Success");
			model.addAttribute("paymentId", newPaymentSubscription.getId());
		}

		if (form.getBillingAddress() != null && !paymentSubscription.isEmpty())
		{
			CartData cartData = checkoutFacade.getCheckoutCart();
			oshTaxFacade.calculateTax(cartData);
			cartData = checkoutFacade.getCheckoutCart();
			request.getSession().setAttribute("taxCalculated", true);
		}

		return REDIRECT_PREFIX + "/checkout/single/summary/getPaymentDetailsForm.json?paymentId=" + paymentSubscription
				+ "&createUpdateStatus=Success";
	}

	@RequestMapping(value = "/placeOrder")
	public String placeOrder(@ModelAttribute("placeOrderForm") final PlaceOrderForm placeOrderForm, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException, InvalidCartException
	{
		// validate the cart
		boolean invalid = false;
		boolean storepickupPrice = false;
		boolean shippingPrice = false;
		OrderData orderData = null;
		removeSessionAttribute(request);
		final String securityCode = placeOrderForm.getSecurityCode();
		final CartData cartData = checkoutFacade.getCheckoutCart();
		final PointOfServiceData pos = inventoryValidation.getMyStore();
		final String loyaltyVoucher = cartService.getSessionCart().getLoyaltyVoucher();
		for (final OrderEntryData orderEntryData : cartData.getEntries())
		{
			final CartEntryModel cartEntryModel = cartService.getEntryForNumber(cartService.getSessionCart(), orderEntryData
					.getEntryNumber().intValue());

			if (cartEntryModel != null && !cartEntryModel.getOrderType().equalsIgnoreCase(OshCoreConstants.ONLINE))
			{
				getOshCheckoutFacade().setStoreName(cartEntryModel, pos.getName());
				storepickupPrice = true;
			}
			else
			{
				shippingPrice = true;
			}

		}

		if (placeOrderForm.getGiftMessage() != null && !placeOrderForm.getGiftMessage().isEmpty())
		{
			final String giftMessage = XSSFilterUtil.filter(placeOrderForm.getGiftMessage());
			placeOrderForm.setGiftMessage(giftMessage);
			getOshCheckoutFacade().setGiftOrder(placeOrderForm.isGift(), giftMessage);
		}

		if (storepickupPrice)
		{
			getOshCheckoutFacade().setStoreAddressIfAvailable();
		}


		if (shippingPrice && cartData.getDeliveryAddress() == null)
		{
			GlobalMessages.addErrorMessage(model, "checkout.deliveryAddress.notSelected");
			invalid = true;
		}
		if (cartData.getDeliveryAddress() != null)
		{
			if (cartData.getDeliveryAddress().getPhone() == null)
			{
				GlobalMessages.addErrorMessage(model, "checkout.deliveryAddress.phoneNo.notSelected");
				invalid = true;
			}

		}
		if (shippingPrice && cartData.getDeliveryMode() == null)
		{
			GlobalMessages.addErrorMessage(model, "checkout.deliveryMethod.notSelected");
			invalid = true;
		}
		if (cartData.getPaymentInfo() != null)
		{
			if (cartData.getPaymentInfo().getBillingAddress() != null
					&& cartData.getPaymentInfo().getBillingAddress().getPhone() == null)
			{
				GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.phoneNo.notSelected");
				invalid = true;
			}

		}

		if (cartData.getPaymentInfo() == null)
		{
			GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.notSelected");
			invalid = true;
		}
		else
		{
			if (CheckoutPciOptionEnum.DEFAULT.equals(getCheckoutFlowFacade().getSubscriptionPciOption())
					&& (cartData.getPaymentInfo() != null) && cartData.getPaymentInfo().getSubscriptionId() == null)
			{
				if (StringUtils.isBlank(securityCode))
				{
					GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.noSecurityCode");
					invalid = true;
				}
			}
		}

		if (invalid)
		{
			return checkoutSummary(model, request);
		}

		/*
		 * if (!checkoutFacade.authorizePayment(securityCode)) { GlobalMessages.addErrorMessage(model,
		 * "checkout.placeOrder.failed"); return checkoutSummary(model); }
		 */

		final CartModel cart = cartService.getSessionCart();
		if (cartData.getDeliveryCost() != null)
		{
			cart.setNewDeliveryCost(Double.valueOf(cartData.getDeliveryCost().getValue().doubleValue()));
		}

		modelService.save(cart);
		if (checkoutFacade.authorizePayment(securityCode))
		{
			checkoutFacade.setCashRegisterNo(cart);
			checkoutFacade.setCustomerID(cart);
			if (loyaltyVoucher != null)
			{
				final String status = redeemLoyaltyCertificate(loyaltyVoucher, model);
				LOG.info("***status*** " + status);
				if (!status.equalsIgnoreCase("success"))
				{
					try
					{
						final String transactionStatus = getOshCheckoutFacade().reverseAuthorization();
						oshVoucherService.releaseVoucher("LOYALTYVOUCHER", cart);
						cart.setLoyaltyVoucher(null);
						modelService.save(cart);
						LOG.info("Auth Reversal Done ?   " + transactionStatus);
					}
					catch (final JaloPriceFactoryException e)
					{
						e.printStackTrace();
					}

					return checkoutSummary(model, request);
				}
				else
				{
					orderData = checkoutFacade.placeOrder();

					if (orderData == null)
					{
						GlobalMessages.addErrorMessage(model, "checkout.placeOrder.failed");
						return checkoutSummary(model, request);
					}
					return REDIRECT_PREFIX + "/checkout/orderConfirmation/" + orderData.getCode();
				}
			}
			else
			{
				orderData = checkoutFacade.placeOrder();

				if (orderData == null)
				{
					GlobalMessages.addErrorMessage(model, "checkout.placeOrder.failed");
					return checkoutSummary(model, request);
				}
				return REDIRECT_PREFIX + "/checkout/orderConfirmation/" + orderData.getCode();
			}
		}
		else
		{
			GlobalMessages.addErrorMessage(model, "checkout.placeOrder.failed");
			return checkoutSummary(model, request);
		}
		//return "";
		//return REDIRECT_PREFIX + "/checkout/orderConfirmation/" + orderData.getCode();
	}

	/**
	 * @param request
	 */
	public void removeSessionAttribute(final HttpServletRequest request)
	{
		final HttpSession taxSession = request.getSession();
		taxSession.removeAttribute("taxCalculated");
		taxSession.removeAttribute("cartTotal");
	}

	public String redeemLoyaltyCertificate(final String loyaltyVoucher, final Model model)
	{
		String status = "";
		final String url = configurationService.getConfiguration().getString(WSDL);
		final String apiUser = configurationService.getConfiguration().getString(API_USER);
		final String apiPassword = configurationService.getConfiguration().getString(API_PASSWORD);
		try
		{
			final LoyaltyLabAPIStub api = new LoyaltyLabAPIStub(url);
			final AuthenticateUser user = new AuthenticateUser();
			user.setUsername(apiUser);
			user.setPassword(apiPassword);
			final AuthenticationResultE authE = new AuthenticationResultE();
			AuthenticateUserResponse response;
			response = api.authenticateUser(user, null);
			final AuthenticationResult result = response.getAuthenticateUserResult();
			authE.setAuthenticationResult(result);
			final RedeemShopperRewardCerificate certificate = new RedeemShopperRewardCerificate();
			certificate.setRewardNumber(loyaltyVoucher);
			final OshCustomerData customerData = (OshCustomerData) customerFacade.getCurrentCustomer();
			final ShopperIdentifier identifier = new ShopperIdentifier();
			final IdentifierTypes types = new IdentifierTypes("RetailerShopperId", true);
			identifier.setIdentifierType(types);
			identifier.setIdentifierValue(customerData.getLoyaltyNumber());
			certificate.setShopperIdentifier(identifier);
			final RedeemShopperRewardCerificateResponse redeemResponse = api.redeemShopperRewardCerificate(certificate, authE);
			final RewardRedemptionStatus rewardStatus = redeemResponse.getRedeemShopperRewardCerificateResult();

			status = rewardStatus.getValue();
			if (!status.equalsIgnoreCase("Success"))
			{
				GlobalMessages.addErrorMessage(model, configurationService.getConfiguration().getString(status));
			}

			LOG.info("***status*** " + status);

		}

		catch (final RemoteException e)
		{
			LOG.error(e.getMessage());
			GlobalMessages.addErrorMessage(model, "checkout.placeOrder.loyalty.down");
		}

		return status;
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


}
