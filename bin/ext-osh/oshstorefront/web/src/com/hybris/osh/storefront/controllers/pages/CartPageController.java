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
package com.hybris.osh.storefront.controllers.pages;


import de.hybris.platform.acceleratorfacades.user.data.AcceleratorCustomerData;
import de.hybris.platform.acceleratorservices.config.SiteConfigService;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceData.PriceType;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commerceservices.delivery.DeliveryService;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.deliveryzone.model.ZoneDeliveryModeModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.exception.StockLevelNotFoundException;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.enums.CheckoutFlowEnum;
import com.hybris.osh.core.enums.CheckoutPciOptionEnum;
import com.hybris.osh.core.promotion.model.OshPromotionVoucherModel;
import com.hybris.osh.core.voucher.OshVoucherService;
import com.hybris.osh.facades.cart.OshCartFacade;
import com.hybris.osh.facades.checkout.OshCheckoutFacade;
import com.hybris.osh.facades.customer.data.OshCustomerData;
import com.hybris.osh.facades.flow.impl.SessionOverrideCheckoutFlowFacade;
import com.hybris.osh.facades.inventoryvalidation.InventoryValidation;
import com.hybris.osh.facades.order.data.OshOrderEntryData;
import com.hybris.osh.facades.tax.OshTaxFacade;
import com.hybris.osh.facades.voucher.impl.OshVoucherFacade;
import com.hybris.osh.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.hybris.osh.storefront.constants.WebConstants;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.util.GlobalMessages;
import com.hybris.osh.storefront.forms.UpdateQuantityForm;


/**
 * Controller for cart page
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/cart")
public class CartPageController extends AbstractPageController
{
	protected static final Logger LOG = Logger.getLogger(CartPageController.class);

	public static final String SHOW_CHECKOUT_STRATEGY_OPTIONS = "storefront.show.checkout.flows";

	private static final String CART_CMS_PAGE_LABEL = "cart";
	private static final String CONTINUE_URL = "continueUrl";
	private static final String LOYALTY_VOUCHER = "loyalty.voucher";


	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "commonI18NService")
	private CommonI18NService commonI18NService;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;

	@Resource(name = "productService")
	private ProductService productService;

	@Resource(name = "cartFacade")
	private OshCartFacade cartFacade;

	@Resource(name = "inventoryValidation")
	private InventoryValidation inventoryValidation;

	@Resource(name = "cartService")
	private CartService cartService;

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "siteConfigService")
	private SiteConfigService siteConfigService;

	@Resource(name = "sessionService")
	private SessionService sessionService;

	@Resource(name = "checkoutFacade")
	private OshCheckoutFacade checkoutFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "deliveryService")
	private DeliveryService deliveryService;

	@Resource(name = "oshVoucherFacade")
	private OshVoucherFacade oshVoucherFacade;

	@Resource(name = "simpleBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder resourceBreadcrumbBuilder;

	@Resource(name = "oshTaxFacade")
	private OshTaxFacade oshTaxFacade;


	@Resource(name = "warehouseService")
	private WarehouseService warehouseService;

	@Resource(name = "stockService")
	private StockService stockService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "oshVoucherService")
	private OshVoucherService oshVoucherService;

	@Resource(name = "customerFacade")
	protected CustomerFacade<AcceleratorCustomerData> customerFacade;

	//boolean updateEntry = false;

	final Map<Integer, ArrayList<String>> promotionData = new HashMap<Integer, ArrayList<String>>();


	// Public getter used in a test
	@Override
	public SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@ModelAttribute("showCheckoutStrategies")
	public boolean isCheckoutStrategyVisible()
	{
		final String property = getSiteConfigService().getProperty(SHOW_CHECKOUT_STRATEGY_OPTIONS);
		return !StringUtils.isBlank(property) && Boolean.parseBoolean(property);
	}

	/*
	 * Display the cart page
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String showCart(final Model model, final HttpServletRequest request, final HttpSession session)
			throws CMSItemNotFoundException
	{
		final String previousPage = request.getHeader("Referer");
		if (StringUtils.isBlank(previousPage))
		{
			session.setAttribute("previousPage", ROOT);
		}
		else if (!previousPage.contains("/cart"))
		{
			session.setAttribute("previousPage", previousPage);
		}
		prepareDataForPage(model, session);
		return ControllerConstants.Views.Pages.Cart.CartPage;
	}

	/**
	 * Handle the '/cart/checkout' request url. This method checks to see if the cart is valid before allowing the
	 * checkout to begin. Note that this method does not require the user to be authenticated and therefore allows us to
	 * validate that the cart is valid without first forcing the user to login. The cart will be checked again once the
	 * user has logged in.
	 * 
	 * @return The page to redirect to
	 */
	@RequestMapping(value = "/checkout", method = RequestMethod.GET)
	public String cartCheck()
	{
		SessionOverrideCheckoutFlowFacade.resetSessionOverrides();

		if (!cartFacade.hasSessionCart() || cartFacade.getSessionCart().getEntries().isEmpty())
		{
			LOG.info("Missing or empty cart");

			// No session cart or empty session cart. Bounce back to the cart page.
			return REDIRECT_PREFIX + "/cart";
		}

		if (userService.getCurrentUser().getName().equalsIgnoreCase("Guest User"))
		{
			return REDIRECT_PREFIX + "/login/checkout";
		}

		// Redirect to the start of the checkout flow to begin the checkout process
		// We just redirect to the generic '/checkout' page which will actually select the checkout flow
		// to use. The customer is not necessarily logged in on this request, but will be forced to login
		// when they arrive on the '/checkout' page.
		return REDIRECT_PREFIX + "/checkout";
	}

	// This controller method is used to allow the site to force the visitor through a specified checkout flow.
	// If you only have a static configured checkout flow then you can remove this method.
	@RequestMapping(value = "/checkout/select-flow", method = RequestMethod.GET)
	public String initCheck(@RequestParam(value = "flow", required = false) final CheckoutFlowEnum checkoutFlow,
			@RequestParam(value = "pci", required = false) final CheckoutPciOptionEnum checkoutPci)
	{
		SessionOverrideCheckoutFlowFacade.resetSessionOverrides();

		if (!cartFacade.hasSessionCart() || cartFacade.getSessionCart().getEntries().isEmpty())
		{
			LOG.info("Missing or empty cart");

			// No session cart or empty session cart. Bounce back to the cart page.
			return REDIRECT_PREFIX + "/cart";
		}

		// Override the Checkout Flow setting in the session
		if (checkoutFlow != null && StringUtils.isNotBlank(checkoutFlow.getCode()))
		{
			SessionOverrideCheckoutFlowFacade.setSessionOverrideCheckoutFlow(checkoutFlow);
		}

		// Override the Checkout PCI setting in the session
		if (checkoutPci != null && StringUtils.isNotBlank(checkoutPci.getCode()))
		{
			SessionOverrideCheckoutFlowFacade.setSessionOverrideSubscriptionPciOption(checkoutPci);
		}


		// Redirect to the start of the checkout flow to begin the checkout process
		// We just redirect to the generic '/checkout' page which will actually select the checkout flow
		// to use. The customer is not necessarily logged in on this request, but will be forced to login
		// when they arrive on the '/checkout' page.
		return REDIRECT_PREFIX + "/checkout";
	}


	@RequestMapping(value = "/clearSession", method = RequestMethod.GET)
	@ResponseBody
	public void clearSession(final HttpSession session)
	{
		session.removeAttribute("previousPage");
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public String updateCartQuantities(@RequestParam("entryNumber") final long entryNumber, final Model model,
			@Valid final UpdateQuantityForm form, final BindingResult bindingResult, final HttpSession session,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		session.setAttribute("itemUpdateFlag", true);


		if (bindingResult.hasErrors())
		{
			for (final ObjectError error : bindingResult.getAllErrors())
			{
				if (error.getCode().equals("typeMismatch"))
				{
					GlobalMessages.addErrorMessage(model, "basket.error.quantity.invalid");
				}
				else
				{
					GlobalMessages.addErrorMessage(model, error.getDefaultMessage());
				}
			}
		}
		else if (cartFacade.getSessionCart().getEntries() != null)
		{
			final CartEntryModel cartEntryModel = cartService.getEntryForNumber(cartService.getSessionCart(), (int) entryNumber);
			if (cartEntryModel != null && cartEntryModel.getProduct() != null
					&& cartEntryModel.getOrderType().equalsIgnoreCase(OshCoreConstants.WAREHOUSE))
			{
				final ProductModel productModel = cartEntryModel.getProduct();
				final ProductData productData = productFacade.getProductForCodeAndOptions(productModel.getCode(), Arrays.asList(
						ProductOption.BASIC, ProductOption.PRICE, ProductOption.SUMMARY, ProductOption.DESCRIPTION,
						ProductOption.GALLERY, ProductOption.CATEGORIES, ProductOption.REVIEW, ProductOption.PROMOTIONS,
						ProductOption.CLASSIFICATION, ProductOption.VARIANT_FULL, ProductOption.STOCK));
				if (form.getQuantity().longValue() > productData.getStockLevel().longValue())
				{
					redirectModel.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
							Collections.singletonList("requested.quantity.not.available"));
					return REDIRECT_PREFIX + "/cart";
				}
			}

			if (cartEntryModel != null && cartEntryModel.getProduct() != null
					&& cartEntryModel.getOrderType().equalsIgnoreCase("bopis"))
			{
				String myStore = null;
				final ProductModel productModel = cartEntryModel.getProduct();
				int storeStock = 0;
				WarehouseModel wareHouseModel = null;
				final CustomerModel custModel = (CustomerModel) userService.getCurrentUser();
				if (custModel.getMyStore() != null)
				{
					myStore = custModel.getMyStore().getName();
				}
				if (myStore != null)
				{
					wareHouseModel = warehouseService.getWarehouseForCode(myStore);
				}
				try
				{
					if (wareHouseModel != null)
					{
						storeStock = stockService.getStockLevelAmount(productModel, wareHouseModel);

					}
				}
				catch (final StockLevelNotFoundException e)
				{
					storeStock = 0;
				}

				if (form.getQuantity().longValue() > storeStock)
				{
					redirectModel.addFlashAttribute(GlobalMessages.ERROR_MESSAGES_HOLDER,
							Collections.singletonList("requested.quantity.not.available"));
					return REDIRECT_PREFIX + "/cart";
				}
			}

			try
			{

				final CartModificationData cartModification = cartFacade.updateCartEntry(entryNumber, form.getQuantity().longValue());
				if (cartModification.getQuantity() == form.getQuantity().longValue())
				{
					// Success

					if (cartModification.getQuantity() == 0)
					{
						// Success in removing entry
						promotionData.remove(entryNumber);
						redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
								Collections.singletonList("basket.page.message.remove"));
					}
					else
					{
						// Success in update quantity
						redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
								Collections.singletonList("basket.page.message.update"));
					}
				}
				else
				{
					// Less than successful

					if (form.getQuantity().longValue() == 0)
					{
						// Failed to remove entry
						redirectModel.addFlashAttribute(
								GlobalMessages.ERROR_MESSAGES_HOLDER,
								Collections.singletonList("basket.information.quantity.reducedNumberOfItemsAdded."
										+ cartModification.getStatusCode()));
					}
					else
					{
						// Failed to update quantity
						redirectModel.addFlashAttribute(
								GlobalMessages.ERROR_MESSAGES_HOLDER,
								Collections.singletonList("basket.information.quantity.reducedNumberOfItemsAdded."
										+ cartModification.getStatusCode()));
					}
				}
				session.setAttribute("taxCalculated", false);
				// Redirect to the cart page on update success so that the browser doesn't re-post again
				return REDIRECT_PREFIX + "/cart";



			}
			catch (final CommerceCartModificationException ex)
			{
				LOG.warn("Couldn't update product with the entry number: " + entryNumber + ".", ex);
			}
		}

		prepareDataForPage(model, session);
		return ControllerConstants.Views.Pages.Cart.CartPage;
	}

	@SuppressWarnings("boxing")
	@RequestMapping(value = "/applyVoucher", method = RequestMethod.GET)
	public String applyVoucher(@RequestParam(required = false) final String voucherCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response, final RedirectAttributes redirectModel)
			throws CMSItemNotFoundException, CommerceCartModificationException
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
				return REDIRECT_PREFIX + "/cart";
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
				}
				else
				{
					redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
							Collections.singletonList(OshCoreConstants.VOUCHER_RESTRICTION_FAIL));
				}
			}
		}
		request.getSession().setAttribute("taxCalculated", false);
		return REDIRECT_PREFIX + "/cart";
	}

	@SuppressWarnings("boxing")
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
		return REDIRECT_PREFIX + "/cart";
	}

	public String getVoucherCode(final PromotionVoucherModel voucherModel)
	{
		return voucherModel.getVoucherCode();
	}

	public String getVoucherCode(final OshPromotionVoucherModel voucherModel)
	{
		return voucherModel.getVoucherCode();
	}

	protected void createProductList(final Model model, final HttpSession session) throws CMSItemNotFoundException
	{
		CartData cartData = cartFacade.getSessionCart();
		boolean storepickup = false;
		boolean shipping = false;
		String myStore = null;
		BigDecimal sub = new BigDecimal(0);
		if (cartData.getEntries() != null && !cartData.getEntries().isEmpty())
		{
			final int index = 0;
			for (final OrderEntryData entry : cartData.getEntries())
			{

				getPromotionDetails(cartData, entry, index);
				if (((OshOrderEntryData) entry).getOrderType().equalsIgnoreCase(OshCoreConstants.BOPIS))
				{
					storepickup = true;
				}
				else
				{
					shipping = true;
				}

				final UpdateQuantityForm uqf = new UpdateQuantityForm();
				uqf.setQuantity(entry.getQuantity());
				model.addAttribute("updateQuantityForm" + entry.getProduct().getCode(), uqf);
				sub = sub.add(entry.getTotalPrice().getValue());
				final PriceData subTotal = priceDataFactory.create(PriceType.BUY, sub, OshCoreConstants.DEFAULT_CURRENCY);
				cartData.setSubTotal(subTotal);
				final ProductModel productModel = productService.getProductForCode(entry.getProduct().getCode());
				if (inventoryValidation.getMyStore() != null)
				{
					myStore = inventoryValidation.getMyStore().getName();

				}
				else
				{
					//myStore = (String) JaloSession.getCurrentSession().getAttribute("storeName");
					//final Session session = sessionService.getCurrentSession();
					myStore = (String) session.getAttribute("storeName");
				}

				final StockLevelStatus stockStatus = inventoryValidation.storeStock(myStore, productModel);
				((OshOrderEntryData) entry).setStockLevelStatus(stockStatus);

				model.addAttribute("quantities", getNumberRange(0, 10));
			}
			Boolean taxCalculated = (Boolean) session.getAttribute("taxCalculated");

			if (taxCalculated == null)
			{
				session.setAttribute("taxCalculated", false);
				taxCalculated = false;
			}
			cartData = checkoutFacade.getCheckoutCart();
			if (cartData.getPaymentInfo() != null && cartData.getDeliveryAddress() != null && !taxCalculated
					&& cartData.getPaymentInfo().getBillingAddress() != null)
			{

				oshTaxFacade.calculateTax(cartData);
				checkoutFacade.getCheckoutCart();
				cartData = checkoutFacade.getCheckoutCart();
				session.setAttribute("taxCalculated", true);
			}
		}
		if (cartData.getDeliveryMode() == null && cartData.getDeliveryCost() == null)
		{
			final DeliveryModeModel supportedDeliveryModes = deliveryService.getDeliveryModeForCode("ground-gross");
			PriceData totalPrice = cartData.getTotalPrice();
			final ZoneDeliveryModeModel zoneModel = (ZoneDeliveryModeModel) supportedDeliveryModes;
			final Double deliveryCost = zoneModel.getValues().iterator().next().getValue();
			if ((storepickup && shipping) || shipping)
			{
				final PriceData shippingPrice = priceDataFactory.create(PriceType.BUY,
						BigDecimal.valueOf(deliveryCost.doubleValue()), commonI18NService.getCurrentCurrency().getIsocode());
				//cartData.setDeliveryCost(shippingPrice);
				if (shippingPrice != null && totalPrice != null)
				{
					totalPrice = priceDataFactory.create(PriceType.BUY,
							BigDecimal.valueOf(totalPrice.getValue().doubleValue() + shippingPrice.getValue().doubleValue()),
							commonI18NService.getCurrentCurrency().getIsocode());
				}
				//cartData.setTotalPrice(totalPrice);
			}

			else
			{
				cartData.setDeliveryCost(null);
				checkoutFacade.removeDeliveryMode();

			}
		}
		else
		{
			final PriceData deliveryCost = cartData.getDeliveryCost();
			PriceData totalPrice = cartData.getTotalPrice();
			if (deliveryCost != null && totalPrice != null)
			{
				totalPrice = priceDataFactory.create(PriceType.BUY,
						BigDecimal.valueOf(totalPrice.getValue().doubleValue() - deliveryCost.getValue().doubleValue()),
						commonI18NService.getCurrentCurrency().getIsocode());
			}
			cartData.setTotalPrice(totalPrice);
		}

		model.addAttribute("cartData", cartData);

		storeCmsPageInModel(model, getContentPageForLabelOrId(CART_CMS_PAGE_LABEL));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(CART_CMS_PAGE_LABEL));
	}

	protected void prepareDataForPage(final Model model, final HttpSession session) throws CMSItemNotFoundException
	{
		final String continueUrl = (String) sessionService.getAttribute(WebConstants.CONTINUE_URL);
		model.addAttribute(CONTINUE_URL, (continueUrl != null && !continueUrl.isEmpty()) ? continueUrl : ROOT);

		createProductList(model, session);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, resourceBreadcrumbBuilder.getBreadcrumbs("breadcrumb.cart"));
		model.addAttribute("pageType", PageType.Cart);
	}

	/**
	 * TODO:Need to move out of controller and place in facade with business logic to populate max number
	 * 
	 */
	protected List<String> getNumberRange(final int startNumber, final int endNumber)
	{
		final List<String> numbers = new ArrayList<String>();
		for (int number = startNumber; number <= endNumber; number++)
		{
			numbers.add(String.valueOf(number));
		}
		return numbers;
	}

	@ResponseBody
	@RequestMapping(value = "/mergeCart", method = RequestMethod.POST, produces = "application/json")
	public String addToCart(@RequestParam("entryNumber") final long entryNumber, final Model model,
			@RequestParam(value = "qty", required = false) final long qty,
			@RequestParam(value = "orderType", required = false) final String orderType, final HttpSession session)
			throws CMSItemNotFoundException
	{

		try
		{
			final CartModificationData cartModification = cartFacade.mergeCartEntry(entryNumber, qty, orderType);
			model.addAttribute("quantity", Long.valueOf(cartModification.getQuantityAdded()));
			model.addAttribute("entry", cartModification.getEntry());
			model.addAttribute("cartData", cartFacade.getSessionCart());
		}

		catch (final CommerceCartModificationException ex)
		{
			model.addAttribute("errorMsg", "basket.error.occurred");
			model.addAttribute("quantity", Long.valueOf(0L));

		}

		prepareDataForPage(model, session);
		return ControllerConstants.Views.Pages.Cart.CartPage;
	}
}
