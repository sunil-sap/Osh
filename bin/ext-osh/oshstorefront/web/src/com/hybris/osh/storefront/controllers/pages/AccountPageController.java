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

import de.hybris.platform.acceleratorfacades.storefinder.StoreFinderFacade;
import de.hybris.platform.acceleratorfacades.user.data.AcceleratorCustomerData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.OrderFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.order.data.OrderHistoryData;
import de.hybris.platform.commercefacades.order.data.ZoneDeliveryModeData;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commercefacades.user.UserFacade;
import de.hybris.platform.commercefacades.user.converters.populator.AddressReversePopulator;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commercefacades.user.data.CountryData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commercefacades.user.data.TitleData;
import de.hybris.platform.commercefacades.user.exceptions.PasswordMismatchException;
import de.hybris.platform.commerceservices.customer.CustomerAccountService;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.payment.PaymentService;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;

import java.rmi.RemoteException;
import java.util.ArrayList;
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
import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.service.CheetahMailSubscriberService;
import com.hybris.osh.facades.checkout.OshCheckoutFacade;
import com.hybris.osh.facades.customer.OshCustomerFacade;
import com.hybris.osh.facades.customer.data.OshCustomerData;
import com.hybris.osh.facades.order.data.OshOrderData;
import com.hybris.osh.facades.order.data.OshOrderEntryData;
import com.hybris.osh.facades.payment.OshPaymentFacade;
import com.hybris.osh.facades.user.data.OshAddressData;
import com.hybris.osh.facades.user.data.StateData;
import com.hybris.osh.storefront.breadcrumb.Breadcrumb;
import com.hybris.osh.storefront.breadcrumb.ResourceBreadcrumbBuilder;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.util.GlobalMessages;
import com.hybris.osh.storefront.forms.AddressForm;
import com.hybris.osh.storefront.forms.PaymentDetailsForm;
import com.hybris.osh.storefront.forms.UpdateEmailForm;
import com.hybris.osh.storefront.forms.UpdatePasswordForm;
import com.hybris.osh.storefront.forms.UpdateProfileForm;
import com.hybris.osh.storefront.forms.validation.PaymentDetailsValidator;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.ArrayOfRedemption;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AuthenticateUser;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AuthenticateUserResponse;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AuthenticationResult;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.AuthenticationResultE;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperByRetailerID;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperByRetailerIDResponse;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperPointBalance;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperPointBalanceResponse;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperRedemptionsByDate;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.GetShopperRedemptionsByDateResponse;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.LoyaltyLabAPIStub;
import com.hybris.osh.storefront.loyalty.services.client.loyaltyapi.Redemption;


/**
 * Controller for home page
 */
@Controller
@Scope("tenant")
@RequestMapping("/my-account")
public class AccountPageController extends AbstractSearchPageController
{
	// Internal Redirects
	private static final String REDIRECT_MY_ACCOUNT = REDIRECT_PREFIX + "/my-account";
	private static final String REDIRECT_TO_ADDRESS_BOOK_PAGE = REDIRECT_PREFIX + "/my-account/address-book";
	private static final String REDIRECT_TO_PAYMENT_INFO_PAGE = REDIRECT_PREFIX + "/my-account/payment-details";
	private static final String REDIRECT_TO_PROFILE_PAGE = REDIRECT_PREFIX + "/my-account/profile";

	// CMS Pages
	private static final String ACCOUNT_CMS_PAGE = "account";
	private static final String PROFILE_CMS_PAGE = "profile";
	private static final String ADDRESS_BOOK_CMS_PAGE = "address-book";
	private static final String ADD_EDIT_ADDRESS_CMS_PAGE = "add-edit-address";
	private static final String PAYMENT_DETAILS_CMS_PAGE = "payment-details";
	private static final String ORDER_HISTORY_CMS_PAGE = "orders";
	private static final String ORDER_DETAIL_CMS_PAGE = "order";
	private static final String WSDL = "loyalty.wsdl.url";
	private static final String API_USER = "loyalty.api.user";
	private static final String API_PASSWORD = "loyalty.api.password";


	private static final Logger LOG = Logger.getLogger(AccountPageController.class);

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "orderFacade")
	private OrderFacade orderFacade;

	@Resource(name = "checkoutFacade")
	private OshCheckoutFacade checkoutFacade;

	@Resource(name = "userFacade")
	protected UserFacade userFacade;

	@Resource(name = "oshPaymentFacade")
	private OshPaymentFacade oshPaymentFacade;

	@Resource(name = "paymentDetailsValidator")
	private PaymentDetailsValidator paymentDetailsValidator;

	@Resource(name = "commerceCheckoutService")
	private CommerceCheckoutService commerceCheckoutService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "paymentService")
	private PaymentService paymentService;

	@Resource(name = "userService")
	protected UserService userService;

	@Resource(name = "addressReversePopulator")
	private AddressReversePopulator addressReversePopulator;

	@Resource(name = "customerAccountService")
	private CustomerAccountService customerAccountService;

	@Resource(name = "cheetahMailSubscriberService")
	private CheetahMailSubscriberService cheetahMailSubscriberService;

	@Resource(name = "passwordEncoderService")
	private PasswordEncoderService passwordEncoderService;


	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Resource(name = "customerFacade")
	protected OshCustomerFacade oshCustomerFacade;

	@Resource(name = "customerFacade")
	protected CustomerFacade<AcceleratorCustomerData> customerFacade;

	@Resource(name = "accountBreadcrumbBuilder")
	private ResourceBreadcrumbBuilder accountBreadcrumbBuilder;

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
		return checkoutFacade.getDefaultDeliveryStates();
	}

	public CartData getCheckoutCart()
	{

		return checkoutFacade.getCheckoutCart();
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

			final List<StateData> statesModel = checkoutFacade.getStates(countryData.getIsocode());


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


	@RequestMapping(method = RequestMethod.GET)
	public String account(final Model model) throws CMSItemNotFoundException
	{
		storeCmsPageInModel(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ACCOUNT_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs(null));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountHomePage;
	}

	@RequestMapping(value = "/orders", method = RequestMethod.GET)
	public String orders(@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model)
			throws CMSItemNotFoundException
	{
		// Handle paged search results
		final PageableData pageableData = createPageableData(page, 5, sortCode, showMode);
		final SearchPageData<OrderHistoryData> searchPageData = orderFacade.getPagedOrderHistoryForStatuses(pageableData);
		populateModel(model, searchPageData, showMode);

		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_HISTORY_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.orderHistory"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountOrderHistoryPage;
	}

	@RequestMapping(value = "/order/{orderCode}", method = RequestMethod.GET)
	public String order(@PathVariable("orderCode") final String orderCode, final Model model) throws CMSItemNotFoundException
	{
		try
		{
			final OshOrderData orderDetails = (OshOrderData) orderFacade.getOrderDetailsForCode(orderCode);
			if (((ZoneDeliveryModeData) orderDetails.getDeliveryMode()) != null)
			{
				((ZoneDeliveryModeData) orderDetails.getDeliveryMode()).setDeliveryCost(orderDetails.getDeliveryCost());
			}
			model.addAttribute("orderData", orderDetails);

			final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
			breadcrumbs.add(new Breadcrumb("/my-account/orders", getMessageSource().getMessage("text.account.orderHistory", null,
					getI18nService().getCurrentLocale()), null));
			breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.order.orderBreadcrumb", new Object[]
			{ orderDetails.getCode() }, "Order {0}", getI18nService().getCurrentLocale()), null));
			model.addAttribute("breadcrumbs", breadcrumbs);

			for (final OrderEntryData orderEntry : orderDetails.getEntries())
			{
				final String orderType = ((OshOrderEntryData) orderEntry).getOrderType();

				if (!orderType.equalsIgnoreCase(OshCoreConstants.WAREHOUSE) && !orderType.equalsIgnoreCase(OshCoreConstants.DROPSHIP))
				{
					final PointOfServiceData pos = storeFinderFacade.getPointOfServiceForName(orderDetails.getOrderType());
					if (pos != null)
					{
						model.addAttribute("posData", pos);
					}
				}
			}

		}
		catch (final UnknownIdentifierException e)
		{
			LOG.warn("Attempted to load a order that does not exist or is not visible", e);
			return REDIRECT_MY_ACCOUNT;
		}
		storeCmsPageInModel(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		model.addAttribute("metaRobots", "no-index,no-follow");
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ORDER_DETAIL_CMS_PAGE));
		return ControllerConstants.Views.Pages.Account.AccountOrderPage;
	}

	@ModelAttribute("months")
	public List<SelectOption> getMonths()
	{
		final List<SelectOption> months = new ArrayList<SelectOption>();
		months.add(new SelectOption("month", "Month"));
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

	@ModelAttribute("days")
	public List<SelectOption> getDay()
	{
		final List<SelectOption> days = new ArrayList<SelectOption>();
		days.add(new SelectOption("date", "Date"));
		days.add(new SelectOption("1", "01"));
		days.add(new SelectOption("2", "02"));
		days.add(new SelectOption("3", "03"));
		days.add(new SelectOption("4", "04"));
		days.add(new SelectOption("5", "05"));
		days.add(new SelectOption("6", "06"));
		days.add(new SelectOption("7", "07"));
		days.add(new SelectOption("8", "08"));
		days.add(new SelectOption("9", "09"));
		days.add(new SelectOption("10", "10"));
		days.add(new SelectOption("11", "11"));
		days.add(new SelectOption("12", "12"));
		days.add(new SelectOption("13", "13"));
		days.add(new SelectOption("14", "14"));
		days.add(new SelectOption("15", "15"));
		days.add(new SelectOption("16", "16"));
		days.add(new SelectOption("17", "17"));
		days.add(new SelectOption("18", "18"));
		days.add(new SelectOption("19", "19"));
		days.add(new SelectOption("20", "20"));
		days.add(new SelectOption("21", "21"));
		days.add(new SelectOption("22", "22"));
		days.add(new SelectOption("23", "23"));
		days.add(new SelectOption("24", "24"));
		days.add(new SelectOption("25", "25"));
		days.add(new SelectOption("26", "26"));
		days.add(new SelectOption("27", "27"));
		days.add(new SelectOption("28", "28"));
		days.add(new SelectOption("29", "29"));
		days.add(new SelectOption("30", "30"));
		days.add(new SelectOption("31", "31"));

		return days;
	}


	@SuppressWarnings("boxing")
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String profile(final Model model) throws CMSItemNotFoundException
	{
		final List<TitleData> titles = userFacade.getTitles();

		final OshCustomerData customerData = (OshCustomerData) customerFacade.getCurrentCustomer();

		if (customerData.getTitleCode() != null)
		{
			model.addAttribute("title", CollectionUtils.find(titles, new Predicate()
			{
				@Override
				public boolean evaluate(final Object object)
				{
					if (object instanceof TitleData)
					{
						return customerData.getTitleCode().equals(((TitleData) object).getCode());
					}
					return false;
				}
			}));
		}

		final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		if (customerModel.getMyStore() != null)
		{

			final PointOfServiceData pointOfServiceData = storeFinderFacade.getPointOfServiceForName(customerModel.getMyStore()
					.getName());
			model.addAttribute("storeData", pointOfServiceData);

		}
		//Loyalty api call for displaying Customer Data
		if (customerData.getLoyaltyNumber() != null && !customerData.getLoyaltyNumber().isEmpty())
		{
			try
			{
				final String url = configurationService.getConfiguration().getString(WSDL);
				final String apiUser = configurationService.getConfiguration().getString(API_USER);
				final String apiPassword = configurationService.getConfiguration().getString(API_PASSWORD);
				final LoyaltyLabAPIStub api = new LoyaltyLabAPIStub(url);
				final AuthenticateUser user = new AuthenticateUser();
				user.setUsername(apiUser);
				user.setPassword(apiPassword);
				final AuthenticationResultE authE = new AuthenticationResultE();
				final AuthenticateUserResponse response = api.authenticateUser(user, null);
				final AuthenticationResult result = response.getAuthenticateUserResult();

				authE.setAuthenticationResult(result);
				final GetShopperPointBalance shopper = new GetShopperPointBalance();
				final GetShopperByRetailerID shopper2 = new GetShopperByRetailerID();
				shopper2.setRetailerShopperID(customerData.getLoyaltyNumber());
				final GetShopperByRetailerIDResponse retailerResponse = api.getShopperByRetailerID(shopper2, authE);
				if (retailerResponse != null)
				{
					if (retailerResponse.getGetShopperByRetailerIDResult() != null)
					{
						LOG.info("***ShopperId**** " + retailerResponse.getGetShopperByRetailerIDResult().getShopperId());
						shopper.setShopperId(retailerResponse.getGetShopperByRetailerIDResult().getShopperId());
					}
				}

				//get valid points balance (= total-points - not-valid-points)
				final int validPoints = getValidPoints(api, shopper, authE);
				model.addAttribute("customerPoints", validPoints);
				model.addAttribute("remainingPoints", 250 - (validPoints % 250));
			}

			catch (final RemoteException e)
			{
				e.printStackTrace();
			}
		}
		model.addAttribute("customerData", customerData);
		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountProfilePage;
	}

	/**
	 * Get Valid Points: total_points - no_valid_points (30 days from current date)
	 * 
	 * @param api
	 * @param shopper
	 * @param authE
	 * @return
	 */
	private int getValidPoints(final LoyaltyLabAPIStub api, final GetShopperPointBalance shopper, final AuthenticationResultE authE)
	{
		// YTODO Auto-generated method stub

		int totalPoints = 0;
		int redemptionNotValidPoints = 0;

		try
		{
			//1.get total points including not-valid points
			final GetShopperPointBalanceResponse balanceResponse = api.getShopperPointBalance(shopper, authE);

			if (balanceResponse != null)
			{
				totalPoints = balanceResponse.getGetShopperPointBalanceResult();
			}

			//2.get not-valid points for 30 days from current date
			final GetShopperRedemptionsByDate shopperRedemption = new GetShopperRedemptionsByDate();

			//Set To Calendar
			final Calendar calTo = Calendar.getInstance();
			shopperRedemption.setToDate(calTo);

			//Set From Calendar
			final Calendar calFrom = Calendar.getInstance();
			calFrom.add(Calendar.DATE, -30);
			shopperRedemption.setFromDate(calFrom);

			//Set Shopper ID
			shopperRedemption.setShopperId(shopper.getShopperId());

			//get not-valid points for 30 days from current date
			final GetShopperRedemptionsByDateResponse redemptionResponse = api.getShopperRedemptionsByDate(shopperRedemption, authE);

			if (redemptionResponse != null)
			{
				final ArrayOfRedemption redemptionList = redemptionResponse.getGetShopperRedemptionsByDateResult();
				if (redemptionList != null)
				{
					for (final Redemption redemption : redemptionList.getRedemption())
					{
						redemptionNotValidPoints += redemption.getPointsEarned();
					}
				}
			}
		}
		catch (final RemoteException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}

		return (totalPoints - redemptionNotValidPoints);
	}

	@RequestMapping(value = "/update-email", method = RequestMethod.GET)
	public String editEmail(final Model model) throws CMSItemNotFoundException
	{
		final AcceleratorCustomerData customerData = customerFacade.getCurrentCustomer();
		final UpdateEmailForm updateEmailForm = new UpdateEmailForm();

		updateEmailForm.setEmail(customerData.getDisplayUid());

		model.addAttribute("updateEmailForm", updateEmailForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountProfileEmailEditPage;
	}

	@RequestMapping(value = "/update-email", method = RequestMethod.POST)
	public String updateEmail(@Valid final UpdateEmailForm updateEmailForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		String returnAction = REDIRECT_TO_PROFILE_PAGE;
		final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		final String encodedCurrentPassword = passwordEncoderService.encode(customerModel, updateEmailForm.getPassword(),
				customerModel.getPasswordEncoding());
		if (!encodedCurrentPassword.equals(customerModel.getEncodedPassword()))
		{
			bindingResult.rejectValue("password", "profile.currentPassword.invalid");
		}
		if (!updateEmailForm.getChkEmail().equals(oshCustomerFacade.getCurrentCustomer().getUid()))
		{
			bindingResult.rejectValue("chkEmail", "validation.checkEmail.equals", new Object[] {}, "validation.checkEmail.equals");
		}

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
			model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
			returnAction = ControllerConstants.Views.Pages.Account.AccountProfileEmailEditPage;
		}
		else
		{
			try
			{
				customerFacade.changeUid(updateEmailForm.getEmail(), updateEmailForm.getPassword());
				redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
						Collections.singletonList("text.account.profile.confirmationUpdated"));

				// Replace the spring security authentication with the new UID
				final String newUid = customerFacade.getCurrentCustomer().getUid().toLowerCase();
				final Authentication oldAuthentication = SecurityContextHolder.getContext().getAuthentication();
				final UsernamePasswordAuthenticationToken newAuthentication = new UsernamePasswordAuthenticationToken(newUid, null,
						oldAuthentication.getAuthorities());
				newAuthentication.setDetails(oldAuthentication.getDetails());
				SecurityContextHolder.getContext().setAuthentication(newAuthentication);
			}
			catch (final DuplicateUidException e)
			{
				redirectAttributes.addFlashAttribute(GlobalMessages.INFO_MESSAGES_HOLDER,
						Collections.singletonList("text.account.profile.emailNotChanged"));
			}
			catch (final PasswordMismatchException passwordMismatchException)
			{
				bindingResult.rejectValue("password", "profile.currentPassword.invalid");
				GlobalMessages.addErrorMessage(model, "form.global.error");
				storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
				model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
				returnAction = ControllerConstants.Views.Pages.Account.AccountProfileEmailEditPage;
			}
		}

		return returnAction;
	}

	@SuppressWarnings("boxing")
	@RequestMapping(value = "/update-profile", method = RequestMethod.GET)
	public String editProfile(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("titleData", userFacade.getTitles());
		model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());

		final OshCustomerData customerData = oshCustomerFacade.getCurrentCustomer();
		//final CustomerData customerData = customerFacade.getCurrentCustomer();
		final UpdateProfileForm updateProfileForm = new UpdateProfileForm();
		final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		if (customerModel.getMyStore() != null)
		{

			final PointOfServiceData pointOfServiceData = storeFinderFacade.getPointOfServiceForName(customerModel.getMyStore()
					.getName());
			model.addAttribute("storeData", pointOfServiceData);

		}

		//Loyalty api call for displaying Customer Data
		if (customerData.getLoyaltyNumber() != null && !customerData.getLoyaltyNumber().isEmpty())
		{
			try
			{
				final String url = configurationService.getConfiguration().getString(WSDL);
				final String apiUser = configurationService.getConfiguration().getString(API_USER);
				final String apiPassword = configurationService.getConfiguration().getString(API_PASSWORD);
				final LoyaltyLabAPIStub api = new LoyaltyLabAPIStub(url);
				final AuthenticateUser user = new AuthenticateUser();
				user.setUsername(apiUser);
				user.setPassword(apiPassword);
				final AuthenticationResultE authE = new AuthenticationResultE();
				final AuthenticateUserResponse response = api.authenticateUser(user, null);
				final AuthenticationResult result = response.getAuthenticateUserResult();

				authE.setAuthenticationResult(result);
				final GetShopperPointBalance shopper = new GetShopperPointBalance();
				final GetShopperByRetailerID shopper2 = new GetShopperByRetailerID();
				shopper2.setRetailerShopperID(customerData.getLoyaltyNumber());
				final GetShopperByRetailerIDResponse retailerResponse = api.getShopperByRetailerID(shopper2, authE);
				if (retailerResponse != null)
				{
					if (retailerResponse.getGetShopperByRetailerIDResult() != null)
					{
						LOG.info("***ShopperId**** " + retailerResponse.getGetShopperByRetailerIDResult().getShopperId());
						shopper.setShopperId(retailerResponse.getGetShopperByRetailerIDResult().getShopperId());
					}
				}
				//get valid points balance (= total-points - not-valid-points)
				final int validPoints = getValidPoints(api, shopper, authE);
				model.addAttribute("customerPoints", validPoints);
				model.addAttribute("remainingPoints", 250 - (validPoints % 250));
				model.addAttribute("customerData", customerData);

			}

			catch (final RemoteException e)
			{
				e.printStackTrace();
			}
		}


		updateProfileForm.setTitleCode(customerData.getTitleCode());

		updateProfileForm.setFirstName(customerData.getFirstName());
		updateProfileForm.setLastName(customerData.getLastName());
		updateProfileForm.setPhone(customerData.getPhone());
		updateProfileForm.setMonth(customerData.getMonth());
		/*
		 * updateProfileForm.setCluborchardinfo(customerData.isCluborchardinfo());
		 * updateProfileForm.setSpecialoffer(customerData.isSpecialoffer());
		 */
		updateProfileForm.setStorenewletter(customerData.isStorenewletter());

		updateProfileForm.setDay(customerData.getDay());
		updateProfileForm.setMonth(customerData.getMonth());
		updateProfileForm.setLoyaltyNumber(customerData.getLoyaltyNumber());

		//updateProfileForm.setAddressForm(customerData.getDefaultShippingAddress());
		final OshAddressData addressData = (OshAddressData) customerData.getDefaultShippingAddress();
		final AddressForm addressFrom = new AddressForm();
		addressFrom.setCountryIso(OshCoreConstants.DEFAULT_COUNTRY_CODE);
		addressFrom.setStateIso(OshCoreConstants.DEFAULT_STATE_CODE);
		addressFrom.setEmail(customerData.getUid());
		if (addressData != null)
		{
			addressFrom.setAddressId(addressData.getId());
			addressFrom.setTitleCode(addressData.getTitleCode());
			addressFrom.setFirstName(addressData.getFirstName());
			addressFrom.setLastName(addressData.getLastName());
			addressFrom.setLine1(addressData.getLine1());
			addressFrom.setLine2(addressData.getLine2());
			addressFrom.setPostcode(addressData.getPostalCode());
			//addressFrom.setEmail(addressData.getEmail());
			addressFrom.setTownCity(addressData.getTown());
			addressFrom.setCountryIso(addressData.getCountry().getIsocode());
			addressFrom.setStateIso(addressData.getState().getIsocode());

			//updateProfileForm.getAddressForm().setCountryIso(addressData.getCountry().getIsocode());
		}
		updateProfileForm.setAddressForm(addressFrom);
		model.addAttribute("updateProfileForm", updateProfileForm);
		model.addAttribute("addressForm", new AddressForm());


		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));

		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountProfileEditPage;
	}

	@RequestMapping(value = "/update-profile", method = RequestMethod.POST)
	public String updateProfile(@Valid final UpdateProfileForm updateProfileForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		String returnAction = ControllerConstants.Views.Pages.Account.AccountProfileEditPage;
		final CustomerData currentCustomerData = oshCustomerFacade.getCurrentCustomer();
		final OshCustomerData oshCustomerData = new OshCustomerData();
		//oshCustomerData.setTitleCode(updateProfileForm.getTitleCode());
		oshCustomerData.setFirstName(updateProfileForm.getFirstName());
		oshCustomerData.setLastName(updateProfileForm.getLastName());
		oshCustomerData.setLoyaltyNumber(updateProfileForm.getLoyaltyNumber());
		oshCustomerData.setPhone(updateProfileForm.getPhone());
		oshCustomerData.setMonth(updateProfileForm.getMonth());
		oshCustomerData.setDay(updateProfileForm.getDay());
		/*
		 * oshCustomerData.setSpecialoffer(updateProfileForm.isSpecialoffer());
		 * oshCustomerData.setCluborchardinfo(updateProfileForm.isCluborchardinfo());
		 */
		oshCustomerData.setStorenewletter(updateProfileForm.isStorenewletter());
		oshCustomerData.setUid(currentCustomerData.getUid());

		/**
		 * This code is for temporary purpose for cheetah mail subscriber
		 */
		cheetahMailSubscriberService.setSubscriber(currentCustomerData.getUid(), updateProfileForm.isStorenewletter());

		final OshAddressData newAddress = new OshAddressData();

		if (currentCustomerData.getDefaultShippingAddress() != null)
		{
			newAddress.setId(currentCustomerData.getDefaultShippingAddress().getId());
		}

		//newAddress.setTitleCode(updateProfileForm.getAddressForm().getTitleCode());
		newAddress.setEmail(currentCustomerData.getUid());
		newAddress.setFirstName(updateProfileForm.getFirstName());
		newAddress.setLastName(updateProfileForm.getLastName());
		newAddress.setLine1(updateProfileForm.getAddressForm().getLine1());
		newAddress.setLine2(updateProfileForm.getAddressForm().getLine2());
		newAddress.setTown(updateProfileForm.getAddressForm().getTownCity());
		newAddress.setPostalCode(updateProfileForm.getAddressForm().getPostcode());
		final CountryData countryData = new CountryData();
		countryData.setIsocode(updateProfileForm.getAddressForm().getCountryIso());
		newAddress.setCountry(countryData);
		final StateData stateData = new StateData();
		stateData.setIsocode(updateProfileForm.getAddressForm().getStateIso());
		newAddress.setState(stateData);

		final boolean isValidCode = checkoutFacade.isValidZipcode(updateProfileForm.getAddressForm().getStateIso(),
				updateProfileForm.getAddressForm().getPostcode());
		if (!isValidCode)
		{
			final AddressForm addressFrom = new AddressForm();

			addressFrom.setAddressId(newAddress.getId());
			addressFrom.setFirstName(newAddress.getFirstName());
			addressFrom.setLine1(newAddress.getLine1());
			addressFrom.setLine2(newAddress.getLine2());
			addressFrom.setPostcode(newAddress.getPostalCode());
			addressFrom.setEmail(newAddress.getEmail());
			addressFrom.setTownCity(newAddress.getTown());
			addressFrom.setCountryIso(newAddress.getCountry().getIsocode());
			addressFrom.setStateIso(newAddress.getState().getIsocode());
			updateProfileForm.setAddressForm(addressFrom);

			model.addAttribute("updateProfileForm", updateProfileForm);
			bindingResult.rejectValue("addressForm.Postcode", "address.postcode.state.invalid", new Object[] {},
					"address.postcode.state.invalid");
			storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
			model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
			return returnAction;
		}

		newAddress.setBillingAddress(false);
		newAddress.setDefaultAddress(true);
		newAddress.setShippingAddress(true);
		oshCustomerData.setDefaultShippingAddress(newAddress);

		model.addAttribute("titleData", userFacade.getTitles());

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
		}
		else
		{
			try
			{
				oshCustomerFacade.oshUpdateProfile(oshCustomerData);
				if (userFacade.isAddressBookEmpty() || currentCustomerData.getDefaultShippingAddress() == null)
				{
					newAddress.setDefaultAddress(true);
					userFacade.addAddress(newAddress);
				}
				else
				{
					userFacade.editAddress(newAddress);
				}

				redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
						Collections.singletonList("text.account.profile.confirmationUpdated"));
				returnAction = REDIRECT_TO_PROFILE_PAGE;
			}
			catch (final DuplicateUidException e)
			{
				bindingResult.rejectValue("email", "registration.error.account.exists.title");
				GlobalMessages.addErrorMessage(model, "form.global.error");
			}
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile"));
		return returnAction;
	}

	@RequestMapping(value = "/update-password", method = RequestMethod.GET)
	public String updatePassword(final Model model) throws CMSItemNotFoundException
	{
		final UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm();

		model.addAttribute("updatePasswordForm", updatePasswordForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));

		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.updatePasswordForm"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountChangePasswordPage;
	}

	@RequestMapping(value = "/update-password", method = RequestMethod.POST)
	public String updatePassword(@Valid final UpdatePasswordForm updatePasswordForm, final BindingResult bindingResult,
			final Model model, final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		if (updatePasswordForm.getNewPassword().equals(updatePasswordForm.getCheckNewPassword()))
		{
			try
			{
				customerFacade.changePassword(updatePasswordForm.getCurrentPassword(), updatePasswordForm.getNewPassword());
			}
			catch (final PasswordMismatchException localException)
			{
				bindingResult.rejectValue("currentPassword", "profile.currentPassword.invalid", new Object[] {},
						"profile.currentPassword.invalid");
			}
		}
		else
		{
			bindingResult.rejectValue("checkNewPassword", "validation.checkPwd.equals", new Object[] {},
					"validation.checkPwd.equals");
		}

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.error.password");
			storeCmsPageInModel(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PROFILE_CMS_PAGE));

			model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.profile.updatePasswordForm"));
			return ControllerConstants.Views.Pages.Account.AccountChangePasswordPage;
		}
		else
		{
			redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
					Collections.singletonList("text.account.confirmation.password.updated"));
			return REDIRECT_TO_PROFILE_PAGE;
		}
	}

	@RequestMapping(value = "/address-book", method = RequestMethod.GET)
	public String getAddressBook(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("addressData", userFacade.getAddressBook());
		storeCmsPageInModel(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADDRESS_BOOK_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.addressBook"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountAddressBookPage;
	}

	@RequestMapping(value = "/add-address", method = RequestMethod.GET)
	public String addAddress(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
		model.addAttribute("titleData", userFacade.getTitles());
		final AddressForm addressForm = new AddressForm();
		addressForm.setCountryIso(OshCoreConstants.DEFAULT_COUNTRY_CODE);
		addressForm.setStateIso(OshCoreConstants.DEFAULT_STATE_CODE);
		model.addAttribute("addressForm", addressForm);

		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/address-book", getMessageSource().getMessage("text.account.addressBook", null,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.addressBook.addEditAddress", null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountEditAddressPage;
	}

	@RequestMapping(value = "/add-paymentdetail", method = RequestMethod.GET)
	public String addPaymentDetails(final Model model) throws CMSItemNotFoundException
	{
		final PaymentDetailsForm paymentDetailsForm = new PaymentDetailsForm();
		paymentDetailsForm.setIsAccountPaymentForm(Boolean.TRUE);
		paymentDetailsForm.setSaveInAccount(Boolean.TRUE);
		final AddressForm addressForm = new AddressForm();
		addressForm.setCountryIso(OshCoreConstants.DEFAULT_COUNTRY_CODE);
		addressForm.setStateIso(OshCoreConstants.DEFAULT_STATE_CODE);
		OshAddressData addressData = (OshAddressData) userFacade.getDefaultAddress();

		if (addressData != null)
		{
			if (checkoutFacade.getCheckoutCart().getPaymentInfo() != null)
			{
				addressData = (OshAddressData) checkoutFacade.getCheckoutCart().getPaymentInfo().getBillingAddress();
				model.addAttribute("paymentId", checkoutFacade.getCheckoutCart().getPaymentInfo().getId());
				addressForm.setEmail(addressData.getEmail());
				addressForm.setAddressId(addressData.getId());
				//addressForm.setTitleCode(addressData.getTitleCode());
				addressForm.setFirstName(addressData.getFirstName());
				addressForm.setLastName(addressData.getLastName());
				addressForm.setLine1(addressData.getLine1());
				addressForm.setLine2(addressData.getLine2());
				addressForm.setTownCity(addressData.getTown());
				addressForm.setPhoneNo(addressData.getPhone());
				addressForm.setPostcode(addressData.getPostalCode());
				addressForm.setCountryIso(addressData.getCountry().getIsocode());
				addressForm.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
				addressForm.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
				addressForm.setStateIso(addressData.getState().getIsocode());
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
				addressForm.setPhoneNo(addressData.getPhone());
				addressForm.setPostcode(addressData.getPostalCode());
				addressForm.setCountryIso(addressData.getCountry().getIsocode());
				addressForm.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
				addressForm.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
				addressForm.setStateIso(addressData.getState().getIsocode());
			}
		}
		paymentDetailsForm.setBillingAddress(addressForm);
		storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
		model.addAttribute(paymentDetailsForm);
		//model.addAttribute("createUpdateStatus", "");
		return ControllerConstants.Views.Pages.Account.AccountAddPaymentDetailPage;
	}

	@RequestMapping(value = "/add-paymentdetail.json", method = RequestMethod.POST, produces = "application/json")
	public String createUpdatePaymentDetails(final Model model, @Valid final PaymentDetailsForm form,
			final BindingResult bindingResult) throws CMSItemNotFoundException
	{
		paymentDetailsValidator.validate(form, bindingResult);
		if (form.getPaymentId() == "" && bindingResult.hasErrors())
		{
			final AddressForm addressForm = new AddressForm();
			OshAddressData addressData = new OshAddressData();
			if (Boolean.FALSE.equals(form.getNewBillingAddress()))
			{
				addressData = (OshAddressData) userFacade.getDefaultAddress();
			}
			else
			{
				addressData = (OshAddressData) checkoutFacade.getCheckoutCart().getDeliveryAddress();
			}
			if (addressData != null)
			{
				addressForm.setEmail(addressData.getEmail());
				addressForm.setAddressId(addressData.getId());
				//addressForm.setTitleCode(addressData.getTitleCode());
				addressForm.setFirstName(addressData.getFirstName());
				addressForm.setLastName(addressData.getLastName());
				addressForm.setLine1(addressData.getLine1());
				addressForm.setLine2(addressData.getLine2());
				addressForm.setTownCity(addressData.getTown());
				addressForm.setPhoneNo(addressData.getPhone());
				addressForm.setPostcode(addressData.getPostalCode());
				addressForm.setCountryIso(addressData.getCountry().getIsocode());
				addressForm.setShippingAddress(Boolean.valueOf(addressData.isShippingAddress()));
				addressForm.setBillingAddress(Boolean.valueOf(addressData.isBillingAddress()));
				addressForm.setStateIso(addressData.getState().getIsocode());
				//final PaymentDetailsForm paymentDetailForm = new PaymentDetailsForm();
				form.setBillingAddress(addressForm);
				model.addAttribute("paymentDetailsForm", form);
				return ControllerConstants.Views.Pages.Account.AccountAddPaymentDetailPopup;
			}
			else
			{
				model.addAttribute("paymentDetailsForm", form);
				return ControllerConstants.Views.Pages.Account.AccountAddPaymentDetailPopup;
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
			paymentInfoData.setIssueNumber(form.getIssueNumber());

		}
		final OshAddressData addressData;
		final AddressModel newAddress = modelService.create(AddressModel.class);
		if (Boolean.FALSE.equals(form.getNewBillingAddress()))
		{
			addressData = (OshAddressData) userFacade.getDefaultAddress();
			final AddressForm addressForm = new AddressForm();
			if (addressData == null)
			{
				GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.createSubscription.billingAddress.noneSelected");
				return ControllerConstants.Views.Pages.Account.AccountAddPaymentDetailPopup;

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
				addressForm.setPhoneNo(addressData.getPhone());
				addressForm.setPostcode(addressData.getPostalCode());
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
				addressData.setPhone(addressForm.getPhoneNo());
				addressData.setPostalCode(addressForm.getPostcode());
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
		//String paymentSubscription;
		paymentInfoData.setBillingAddress(addressData);
		/*
		 * if (userFacade.getCCPaymentInfos(true).isEmpty()) { userFacade.setDefaultPaymentInfo(paymentInfoData); }
		 */

		if (!form.getPaymentId().isEmpty())
		{
			final CardInfo cardInfo = oshPaymentFacade.createCCDetails(paymentInfoData);
			final String merchantTransactionCode = userService.getCurrentUser().getUid() + "-" + UUID.randomUUID();
			//paymentSubscription = paymentInfoData.getSubscriptionId();
			cardInfo.setBillingInfo(oshPaymentFacade.createBillingInfo(newAddress));
			userFacade.updateCCPaymentInfo(paymentInfoData);
			final PaymentTransactionEntryModel entryModel = paymentService.updateSubscription(merchantTransactionCode,
					paymentInfoData.getSubscriptionId(), commerceCheckoutService.getPaymentProvider(), newAddress, cardInfo);
			if (entryModel.getSubscriptionID() != null)
			{
				userFacade.getCCPaymentInfoForCode(form.getPaymentId()).setBillingAddress(addressData);
				checkoutFacade.setPaymentDetails(paymentInfoData.getId());
				if (userFacade.getCCPaymentInfos(true).size() == 1)
				{
					userFacade.setDefaultPaymentInfo(paymentInfoData);
				}

			}
			else
			{
				GlobalMessages.addErrorMessage(model, "checkout.paymentMethod.createSubscription.failed");
				return ControllerConstants.Views.Pages.Account.AccountAddPaymentDetailPopup;
			}
			model.addAttribute("createUpdateStatus", "Success");
			model.addAttribute("paymentId", paymentInfoData.getId());
		}
		else
		{
			final CCPaymentInfoData newPaymentSubscription = checkoutFacade.createPaymentSubscriptionWithoutCart(paymentInfoData);

			if (newPaymentSubscription != null && StringUtils.isNotBlank(newPaymentSubscription.getSubscriptionId()))
			{
				model.addAttribute("paymentDetailsForm", form);
				model.addAttribute("paymentId", newPaymentSubscription.getId());
				checkoutFacade.setPaymentDetails(newPaymentSubscription.getId());
				if (userFacade.getCCPaymentInfos(true).size() == 1)
				{
					userFacade.setDefaultPaymentInfo(newPaymentSubscription);
				}
				//paymentSubscription = newPaymentSubscription.getId();
			}
			else
			{
				model.addAttribute("paymentDetailsForm", form);
				model.addAttribute("createUpdateStatus", "Failed");
				model.addAttribute("paymentId", "");
				storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
				setUpMetaDataForContentPage(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
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
				return ControllerConstants.Views.Pages.Account.AccountAddPaymentDetailPage;
			}
		}

		model.addAttribute("createUpdateStatus", "success");
		return ControllerConstants.Views.Pages.Account.AccountAddPaymentDetailPage;
	}




	@RequestMapping(value = "/add-address", method = RequestMethod.POST, produces = "application/json")
	public String addAddress(@Valid final AddressForm addressForm, final BindingResult bindingResult, final Model model,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{

		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
			model.addAttribute("titleData", userFacade.getTitles());
			return ControllerConstants.Views.Pages.Account.AccountEditAddressPage;
		}

		final OshAddressData newAddress = new OshAddressData();
		/* newAddress.setTitleCode(addressForm.getTitleCode()); */
		newAddress.setEmail(addressForm.getEmail());
		newAddress.setFirstName(addressForm.getFirstName());
		newAddress.setLastName(addressForm.getLastName());
		newAddress.setLine1(addressForm.getLine1());
		newAddress.setLine2(addressForm.getLine2());
		newAddress.setTown(addressForm.getTownCity());
		newAddress.setPhone(addressForm.getPhoneNo());
		newAddress.setPostalCode(addressForm.getPostcode());
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(true);
		final CountryData countryData = new CountryData();
		countryData.setIsocode(addressForm.getCountryIso());
		newAddress.setCountry(countryData);
		final StateData stateData = new StateData();
		stateData.setIsocode(addressForm.getStateIso());
		newAddress.setState(stateData);
		final boolean isValidCode = checkoutFacade.isValidZipcode(addressForm.getStateIso(), addressForm.getPostcode());

		if (!isValidCode)
		{
			model.addAttribute("zipCodeErrorMsg", Boolean.TRUE);
			GlobalMessages.addErrorMessage(model, "form.global.error");
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
			model.addAttribute("titleData", userFacade.getTitles());
			return ControllerConstants.Views.Pages.Account.AccountEditAddressPage;
		}
		if (userFacade.isAddressBookEmpty())
		{
			newAddress.setDefaultAddress(true);
		}
		else
		{
			newAddress.setDefaultAddress(addressForm.getDefaultAddress().booleanValue());
		}
		userFacade.addAddress(newAddress);
		final Map<String, Object> currentFlashScope = RequestContextUtils.getOutputFlashMap(request);
		currentFlashScope.put(GlobalMessages.CONF_MESSAGES_HOLDER, Collections.singletonList("account.confirmation.address.added"));

		return ControllerConstants.Views.Pages.Account.EmptyPage;
	}


	@RequestMapping(value = "/edit-address/{addressCode}", method = RequestMethod.GET)
	public String editAddress(@PathVariable("addressCode") final String addressCode, final Model model)
			throws CMSItemNotFoundException
	{
		final AddressForm addressForm = new AddressForm();
		model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
		model.addAttribute("titleData", userFacade.getTitles());
		model.addAttribute("addressForm", addressForm);
		model.addAttribute("zipCodeErrorMsg", Boolean.FALSE);
		/*
		 * final List<OshAddressData> addresses = new ArrayList<OshAddressData>(); userFacade.getAddressBook();
		 */

		for (final AddressData addressData : userFacade.getAddressBook())
		{
			if (addressData instanceof OshAddressData)
			{
				if (addressData.getId() != null && addressData.getId().equals(addressCode))
				{
					model.addAttribute("addressData", addressData);
					addressForm.setAddressId(addressData.getId());
					/* addressForm.setTitleCode(addressData.getTitleCode()); */
					addressForm.setFirstName(addressData.getFirstName());
					addressForm.setLastName(addressData.getLastName());
					addressForm.setLine1(addressData.getLine1());
					addressForm.setLine2(addressData.getLine2());
					addressForm.setTownCity(addressData.getTown());
					addressForm.setPostcode(addressData.getPostalCode());
					addressForm.setEmail(addressData.getEmail());
					addressForm.setPhoneNo(addressData.getPhone());
					addressForm.setCountryIso(addressData.getCountry().getIsocode());
					if (((OshAddressData) addressData).getState() != null)
					{
						addressForm.setStateIso(((OshAddressData) addressData).getState().getIsocode());
					}

					if (userFacade.getDefaultAddress() != null && userFacade.getDefaultAddress().getId() != null
							&& userFacade.getDefaultAddress().getId().equals(addressData.getId()))
					{
						addressForm.setDefaultAddress(Boolean.TRUE);
					}
					break;
				}
			}
		}

		storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));

		final List<Breadcrumb> breadcrumbs = accountBreadcrumbBuilder.getBreadcrumbs(null);
		breadcrumbs.add(new Breadcrumb("/my-account/address-book", getMessageSource().getMessage("text.account.addressBook", null,
				getI18nService().getCurrentLocale()), null));
		breadcrumbs.add(new Breadcrumb("#", getMessageSource().getMessage("text.account.addressBook.addEditAddress", null,
				getI18nService().getCurrentLocale()), null));
		model.addAttribute("breadcrumbs", breadcrumbs);
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountEditPage;
	}


	@RequestMapping(value = "/edit-address/{addressCode}", method = RequestMethod.POST, produces = "application/json")
	public String editAddress(@Valid final AddressForm addressForm, final BindingResult bindingResult, final Model model,
			final RedirectAttributes redirectModel) throws CMSItemNotFoundException
	{
		model.addAttribute("metaRobots", "no-index,no-follow");
		if (bindingResult.hasErrors())
		{
			GlobalMessages.addErrorMessage(model, "form.global.error");
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
			model.addAttribute("titleData", userFacade.getTitles());
			return ControllerConstants.Views.Pages.Account.AccountEditPage;
		}
		final boolean isValidCode = checkoutFacade.isValidZipcode(addressForm.getStateIso(), addressForm.getPostcode());
		if (!isValidCode)
		{
			model.addAttribute("zipCodeErrorMsg", Boolean.TRUE);
			GlobalMessages.addErrorMessage(model, "form.address.error.zipcode");
			storeCmsPageInModel(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
			model.addAttribute("countryData", checkoutFacade.getDeliveryCountries());
			model.addAttribute("titleData", userFacade.getTitles());
			return ControllerConstants.Views.Pages.Account.AccountEditPage;
		}
		final OshAddressData newAddress = new OshAddressData();
		newAddress.setId(addressForm.getAddressId());
		/* newAddress.setTitleCode(addressForm.getTitleCode()); */
		newAddress.setFirstName(addressForm.getFirstName());
		newAddress.setLastName(addressForm.getLastName());
		newAddress.setLine1(addressForm.getLine1());
		newAddress.setLine2(addressForm.getLine2());
		newAddress.setTown(addressForm.getTownCity());
		newAddress.setPostalCode(addressForm.getPostcode());
		newAddress.setEmail(addressForm.getEmail());
		newAddress.setPhone(addressForm.getPhoneNo());
		final StateData stateData = new StateData();
		stateData.setIsocode(addressForm.getStateIso());
		newAddress.setState(stateData);
		newAddress.setBillingAddress(false);
		newAddress.setShippingAddress(true);
		final CountryData countryData = new CountryData();
		countryData.setIsocode(addressForm.getCountryIso());
		newAddress.setCountry(countryData);


		if (Boolean.TRUE.equals(addressForm.getDefaultAddress()))
		{
			newAddress.setDefaultAddress(true);
			checkoutFacade.setDeliveryAddress(newAddress);
		}
		userFacade.editAddress(newAddress);

		redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("text.account.addressBook.confirmationUpdated"));
		return ControllerConstants.Views.Pages.Account.EmptyPage;
	}

	@RequestMapping(value = "/remove-address/{addressCode}", method = RequestMethod.GET)
	public String removeAddress(@PathVariable("addressCode") final String addressCode, final RedirectAttributes redirectModel)
	{
		final AddressData addressData = new AddressData();
		addressData.setId(addressCode);
		userFacade.removeAddress(addressData);
		redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("account.confirmation.address.removed"));
		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/set-default-address/{addressCode}", method = RequestMethod.GET)
	public String setDefaultAddress(@PathVariable("addressCode") final String addressCode, final RedirectAttributes redirectModel)
	{
		final AddressData addressData = new AddressData();
		addressData.setDefaultAddress(true);
		addressData.setId(addressCode);
		userFacade.setDefaultAddress(addressData);
		checkoutFacade.setDeliveryAddress(addressData);
		redirectModel.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("account.confirmation.default.address.changed"));
		return REDIRECT_TO_ADDRESS_BOOK_PAGE;
	}

	@RequestMapping(value = "/payment-details", method = RequestMethod.GET)
	public String paymentDetails(final Model model) throws CMSItemNotFoundException
	{
		model.addAttribute("customerData", customerFacade.getCurrentCustomer());
		final List<CCPaymentInfoData> oldCCPaymentInfos = userFacade.getCCPaymentInfos(true);
		final List<CCPaymentInfoData> newCCPaymentInfos = new ArrayList<CCPaymentInfoData>();
		if (!oldCCPaymentInfos.isEmpty())
		{
			for (final CCPaymentInfoData ccPaymentInfoData : oldCCPaymentInfos)
			{
				if (ccPaymentInfoData.isDefaultPaymentInfo())
				{
					newCCPaymentInfos.add(0, ccPaymentInfoData);
				}
				else
				{
					newCCPaymentInfos.add(ccPaymentInfoData);
				}
			}
		}
		model.addAttribute("paymentInfoData", newCCPaymentInfos);
		storeCmsPageInModel(model, getContentPageForLabelOrId(PAYMENT_DETAILS_CMS_PAGE));
		setUpMetaDataForContentPage(model, getContentPageForLabelOrId(ADD_EDIT_ADDRESS_CMS_PAGE));
		model.addAttribute("breadcrumbs", accountBreadcrumbBuilder.getBreadcrumbs("text.account.paymentDetails"));
		model.addAttribute("metaRobots", "no-index,no-follow");
		return ControllerConstants.Views.Pages.Account.AccountPaymentInfoPage;
	}

	@RequestMapping(value = "/set-default-payment-details", method = RequestMethod.POST)
	public String setDefaultPaymentDetails(@RequestParam final String paymentInfoId)
	{
		CCPaymentInfoData paymentInfoData = null;
		if (StringUtils.isNotBlank(paymentInfoId))
		{
			paymentInfoData = userFacade.getCCPaymentInfoForCode(paymentInfoId);
			checkoutFacade.setPaymentDetails(paymentInfoId);
		}
		userFacade.setDefaultPaymentInfo(paymentInfoData);
		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}

	@RequestMapping(value = "/remove-payment-method", method = RequestMethod.POST)
	public String removePaymentMethod(final Model model, @RequestParam(value = "paymentInfoId") final String paymentMethodId,
			final RedirectAttributes redirectAttributes) throws CMSItemNotFoundException
	{
		userFacade.unlinkCCPaymentInfo(paymentMethodId);
		final List<CCPaymentInfoData> oldCCPaymentInfos = userFacade.getCCPaymentInfos(true);

		if (!oldCCPaymentInfos.isEmpty())
		{
			for (final CCPaymentInfoData ccPaymentInfoData : oldCCPaymentInfos)
			{
				if (ccPaymentInfoData != null)
				{
					userFacade.setDefaultPaymentInfo(ccPaymentInfoData);
					break;
				}
			}
		}
		redirectAttributes.addFlashAttribute(GlobalMessages.CONF_MESSAGES_HOLDER,
				Collections.singletonList("text.account.profile.paymentCart.removed"));
		return REDIRECT_TO_PAYMENT_INFO_PAGE;
	}

	@ModelAttribute("cardTypes")
	public Collection<CardTypeData> getCardTypes()
	{
		return checkoutFacade.getSupportedCardTypes();
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
