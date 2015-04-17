package com.hybris.osh.storefront.filters;

import de.hybris.platform.acceleratorfacades.storefinder.StoreFinderFacade;
import de.hybris.platform.acceleratorfacades.user.data.AcceleratorCustomerData;
import de.hybris.platform.acceleratorservices.storefinder.StoreFinderService;
import de.hybris.platform.acceleratorservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Calendar;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.GenericFilterBean;

import com.hybris.osh.facades.customer.data.OshCustomerData;
import com.hybris.osh.facades.storelocator.data.OshPointOfServiceData;
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


public class OshStoreLookUpFilter extends GenericFilterBean
{
	private static final Logger LOG = Logger.getLogger(OshStoreLookUpFilter.class.getName());
	private static final String GEOPLUGIN_URL = "geopluginurl";
	private static final String SEARCHPARAMS = "searchparams";
	private static final String STOREURL = "storeUrl";
	private static final String STORENAME = "storeName";
	private static final String ZIPCODE = "zipCode";
	private static final String CUSTOMERPOINTS = "customerPoints";
	private static final String SEARCHRESULT = "searchResult";
	private static final String LOCATION = "location";
	private static final String WSDL = "loyalty.wsdl.url";
	private static final String API_USER = "loyalty.api.user";
	private static final String API_PASSWORD = "loyalty.api.password";
	private static final String NOSTORE = "noStore";
	private static final String POSRESULT = "posResults";


	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "customerFacade")
	protected CustomerFacade<AcceleratorCustomerData> customerFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "storeFinderService")
	private StoreFinderService storeFinderService;

	@Resource(name = "cartService")
	private CartService cartService;

	@Autowired
	private HttpServletRequest request;

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
	 * javax.servlet.FilterChain)
	 */
	@SuppressWarnings("boxing")
	@Override
	public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
			throws IOException, ServletException
	{
		final HttpServletRequest httpRequest = (HttpServletRequest) request;
		boolean notGoogleBot = true;

		final String userAgent = httpRequest.getHeader("user-agent");
		if (userAgent != null && !StringUtils.isEmpty(userAgent) && userAgent.toLowerCase().contains("bot"))
		{
			notGoogleBot = false;
		}

		final HttpServletResponse httpResponse = (HttpServletResponse) response;
		final HttpSession httpSession = httpRequest.getSession(true);
		String userLocation = null;
		Cookie storeCookie = null;
		String cookieStoreValue = (String) httpSession.getAttribute(STORENAME);
		final String cookieZipCodeValue = (String) httpSession.getAttribute(ZIPCODE);
		//Getting cookie information

		final Cookie[] cookie = httpRequest.getCookies();

		if (cookie != null)
		{
			for (final Cookie obj : cookie)
			{
				if (obj.getName().equals("store"))
				{
					storeCookie = obj;
					cookieStoreValue = obj.getValue();
					if (cookieStoreValue != null && !cookieStoreValue.equals("noStore"))
					{
						httpSession.setAttribute(STORENAME, cookieStoreValue);
					}//httpSession.setAttribute(LOCATION, userLocation);

				}
			}

		}
		//Online or Bopis

		CustomerModel customerModel = null;
		if (!userService.getCurrentUser().getUid().equals("anonymous"))
		{
			customerModel = (CustomerModel) userService.getCurrentUser();
		}

		final String orderType = cartService.getSessionCart().getOrderType();
		if (orderType == null || httpSession.getAttribute("orderType") == null)
		{
			httpSession.setAttribute("orderType", "");
		}
		else if (cartService.getSessionCart().getEntries().size() == 0)
		{
			final CartModel currentCart = cartService.getSessionCart();
			currentCart.setOrderType("");
			modelService.save(currentCart);
			httpSession.setAttribute("orderType", "");
		}
		else
		{
			httpSession.setAttribute("orderType", orderType);
		}
		if (httpSession.getAttribute(NOSTORE) == null)
		{
			httpSession.setAttribute(NOSTORE, false);
		}
		if (httpSession.getAttribute(LOCATION) == null && httpSession.getAttribute(STORENAME) == null
				&& httpSession.getAttribute(NOSTORE).equals(false) && cookieStoreValue == null)
		{
			userLocation = getUserLocation(httpRequest);
		}


		final int pageSize = 5;
		final PageableData pageableData = preparePage(pageSize);

		StoreFinderSearchPageData<PointOfServiceData> searchResult = new StoreFinderSearchPageData<PointOfServiceData>();
		if (customerModel != null && customerModel.getMyStore() != null)
		{
			final String storeName = customerModel.getMyStore().getName();
			final PointOfServiceData pointOfServiceData = storeFinderFacade.getPointOfServiceForName(storeName);
			httpSession.setAttribute(STOREURL, httpRequest.getContextPath() + pointOfServiceData.getUrl());
			httpSession.setAttribute(STORENAME, storeName);
			if (cookieStoreValue != null && !(cookieStoreValue.equals(storeName)))
			{
				storeCookie = new Cookie("store", storeName);
				storeCookie.setPath("/");
				storeCookie.setMaxAge(Integer.MAX_VALUE);
				httpResponse.addCookie(storeCookie);
			}
			httpSession.setAttribute(ZIPCODE, pointOfServiceData.getAddress().getPostalCode());
		}
		//location not null and should not be null null
		else if (httpSession.getAttribute(LOCATION) != null && httpSession.getAttribute(NOSTORE).equals(false)
				&& cookieStoreValue == null)
		{
			if (notGoogleBot)
			{
				LOG.info("Google Maps call ");
				LOG.info("Location: " + httpSession.getAttribute(LOCATION) + "...noStore: " + httpSession.getAttribute(NOSTORE)
						+ "....cookieStoreValue: " + cookieStoreValue);
				searchResult = storeFinderFacade.locationSearch((String) httpSession.getAttribute(LOCATION), pageableData);
				httpSession.setAttribute(POSRESULT, searchResult);
			}
		}
		//Set the zipcode if Store is present
		if (cookieZipCodeValue == null && httpSession.getAttribute(STORENAME) != null && cookieStoreValue != null
				&& !cookieStoreValue.isEmpty() && !cookieStoreValue.equals("noStore"))
		{
			if (notGoogleBot)
			{
				final PointOfServiceData pointOfServiceData = storeFinderFacade.getPointOfServiceForName(cookieStoreValue);
				httpSession.setAttribute(ZIPCODE, pointOfServiceData.getAddress().getPostalCode());
			}
		}
		String myStore = null;

		final OshCustomerData customerData = (OshCustomerData) customerFacade.getCurrentCustomer();

		boolean noStore = true;
		if (searchResult.getResults() != null && !searchResult.getResults().isEmpty())
		{
			noStore = false;
			for (final PointOfServiceData nearestStore : searchResult.getResults())
			{
				String str = nearestStore.getFormattedDistance();
				str = str.substring(0, str.indexOf("Miles")).replaceAll(",", "");
				if (Float.parseFloat(str) < 50 && ((OshPointOfServiceData) nearestStore).isActive())
				{

					myStore = nearestStore.getName();
					final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
					final PointOfServiceModel pointOfService = storeFinderService.getPointOfServiceForName(currentBaseStore, myStore);
					if (customerModel != null)
					{
						customerModel.setMyStore(pointOfService);
						modelService.save(customerModel);
					}

					httpSession.setAttribute(STOREURL, httpRequest.getContextPath() + nearestStore.getUrl());
					httpSession.setAttribute(STORENAME, nearestStore.getName());
					if (cookieStoreValue == null)
					{
						storeCookie = new Cookie("store", nearestStore.getName());

						storeCookie.setPath("/");
						storeCookie.setMaxAge(Integer.MAX_VALUE);
						httpResponse.addCookie(storeCookie);
					}

					httpSession.setAttribute(NOSTORE, noStore);
					if (httpSession.getAttribute(LOCATION) == null)
					{
						httpSession.setAttribute(LOCATION, userLocation);
					}
					httpSession.setAttribute(ZIPCODE, nearestStore.getAddress().getPostalCode());
					break;
				}
				else
				{
					noStore = true;
					httpSession.setAttribute(NOSTORE, noStore);
				}
			}
		}
		else
		{
			httpSession.setAttribute(NOSTORE, noStore);
			if (cookieStoreValue == null)
			{
				storeCookie = new Cookie("store", "noStore");

				storeCookie.setPath("/");
				storeCookie.setMaxAge(Integer.MAX_VALUE);
				httpResponse.addCookie(storeCookie);

			}
		}

		if (customerData.getLoyaltyNumber() != null && !customerData.getLoyaltyNumber().isEmpty()
				&& httpSession.getAttribute(CUSTOMERPOINTS) == null)
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
				final AuthenticateUserResponse response1 = api.authenticateUser(user, null);
				final AuthenticationResult result = response1.getAuthenticateUserResult();

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
				//get valid points balance (= total-points - not_valid_points)
				final int validPoints = getValidPoints(api, shopper, authE);
				httpSession.setAttribute(CUSTOMERPOINTS, validPoints);
			}
			catch (final IOException e)
			{
				LOG.error(e.getMessage());
			}
		}
		filterChain.doFilter(request, response);
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

	protected PageableData preparePage(final int pageSize)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0); // Always on the first page
		pageableData.setPageSize(pageSize); // Adjust pageSize to see more
		return pageableData;
	}

	public String getUserLocation(final HttpServletRequest request)
	{
		//Instantiate an HttpClient
		final HttpClient client = new HttpClient();
		//Instantiate a GET HTTP method
		String location = "";
		try
		{
			final String url = configurationService.getConfiguration().getString(GEOPLUGIN_URL) + request.getRemoteAddr();
			final PostMethod method = new PostMethod(url);

			method.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1");
			final int statusCode = client.executeMethod(method);
			LOG.info("statusCode" + statusCode);
			final String geoResponse = method.getResponseBodyAsString();
			final String searchParams = configurationService.getConfiguration().getString(SEARCHPARAMS);
			final String[] paramsArray = searchParams.split(",");
			for (final String params : paramsArray)
			{
				location = location + StringUtils.substringBetween(geoResponse, params, "<") + " ";
			}
			if (request.getSession().getAttribute(LOCATION) == null)
			{
				request.getSession().setAttribute(LOCATION, location);
			}
			LOG.info("***Geo URL of the Shopper*** " + url + "********Geo User Location*** " + location);
			method.releaseConnection();
		}
		catch (final IOException e)
		{
			LOG.error(e.getMessage());
		}
		return location;
	}


}
