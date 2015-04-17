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
import de.hybris.platform.acceleratorservices.storefinder.StoreFinderService;
import de.hybris.platform.acceleratorservices.storefinder.data.StoreFinderSearchPageData;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.store.services.BaseStoreService;
import de.hybris.platform.storelocator.exception.GeoLocatorException;
import de.hybris.platform.storelocator.exception.MapServiceException;
import de.hybris.platform.storelocator.model.PointOfServiceModel;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.hybris.osh.facades.storelocator.data.OshPointOfServiceData;
import com.hybris.osh.storefront.breadcrumb.impl.StorefinderBreadcrumbBuilder;
import com.hybris.osh.storefront.constants.WebConstants;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.util.GlobalMessages;
import com.hybris.osh.storefront.forms.StoreFinderForm;
import com.hybris.osh.storefront.forms.StorePositionForm;
import com.hybris.osh.storefront.util.MetaSanitizerUtil;



/**
 * Controller for store locator search and detail pages. Provides display data for these two pages. Search result amount
 * is limited to the {@link StoreLocatorPageController#INITIAL_LOCATIONS_TO_DISPLAY} value. Increasing number of
 * displayed stores is possible by giving proper argument for
 * {@link StoreLocatorPageController#findSores(String, int, com.hybris.osh.storefront.forms.StoreFinderForm, org.springframework.ui.Model, org.springframework.validation.BindingResult)}
 * method but is limited to max value of {@link StoreLocatorPageController#MAX_LOCATIONS_TO_DISPLAY} stores.
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/store-finder")
public class StoreLocatorPageController extends AbstractPageController
{
	protected static final Logger LOG = Logger.getLogger(StoreLocatorPageController.class);

	private static final String STORE_FINDER_CMS_PAGE_LABEL = "storefinder";
	private static final String GOOGLE_API_KEY_ID = "googleApiKey";
	private static final String GOOGLE_API_VERSION = "googleApiVersion";
	private static final String STOREURL = "storeUrl";
	private static final String STORENAME = "storeName";
	private static final String ZIPCODE = "zipCode";
	private static final String NOSTORE = "noStore";
	private static final String LOCATION = "location";
	private static final int INITIAL_LOCATIONS_TO_DISPLAY = 50;
	private static final int LOCATIONS_INCREMENT = 5;
	private static final int MAX_LOCATIONS_TO_DISPLAY = 100;
	private static final String INITIAL_LOCATIONS_TO_DISPLAY_TEXT = "" + INITIAL_LOCATIONS_TO_DISPLAY; //NOPMD
	private static final String CLIENT_ID = "clientId";
	private static final String POSRESULT = "posResults";

	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Resource(name = "storefinderBreadcrumbBuilder")
	private StorefinderBreadcrumbBuilder storefinderBreadcrumbBuilder;

	@Resource(name = "storeFinderFacade")
	private StoreFinderFacade storeFinderFacade;

	@Resource(name = "userService")
	private UserService userService;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "baseStoreService")
	private BaseStoreService baseStoreService;

	@Resource(name = "storeFinderService")
	private StoreFinderService storeFinderService;


	@ModelAttribute("googleApiVersion")
	public String getGoogleApiVersion()
	{
		return configurationService.getConfiguration().getString(GOOGLE_API_VERSION);
	}

	@ModelAttribute("clientId")
	public String getClientId()
	{
		return configurationService.getConfiguration().getString(CLIENT_ID);
	}

	@ModelAttribute("googleApiKey")
	public String getGoogleApiKey(final HttpServletRequest request)
	{
		final String googleApiKey = getHostConfigService().getProperty(GOOGLE_API_KEY_ID, request.getServerName());
		if (StringUtils.isEmpty(googleApiKey))
		{
			LOG.warn("No Google API key found for server: " + request.getServerName());
		}
		return googleApiKey;
	}

	@ResponseBody
	@RequestMapping(value = "/myStore", method = RequestMethod.GET, produces = "application/json")
	public String setMyStore(@RequestParam(value = "q", required = false, defaultValue = "") final String query,
			@RequestParam("storeName") final String storeName, final Model model, final HttpServletRequest request,
			final HttpServletResponse response) throws CMSItemNotFoundException
	{
		boolean notGoogleBot = true;

		final String userAgent = request.getHeader("user-agent");

		if (userAgent != null && !StringUtils.isEmpty(userAgent) && userAgent.toLowerCase().contains("bot"))
		{
			notGoogleBot = false;
		}

		final HttpSession httpSession = request.getSession(true);
		setUpPageForms(model);
		final CustomerModel customerModel = (CustomerModel) userService.getCurrentUser();
		final BaseStoreModel currentBaseStore = baseStoreService.getCurrentBaseStore();
		final PointOfServiceModel pointOfService = storeFinderService.getPointOfServiceForName(currentBaseStore, storeName);
		final PointOfServiceData pointOfServiceData = storeFinderFacade.getPointOfServiceForName(storeName);

		final String postalCode = pointOfServiceData.getAddress().getPostalCode();
		final int pageSize = 5;
		final PageableData pageableData = preparePage(pageSize);
		if (notGoogleBot)
		{
			final StoreFinderSearchPageData<PointOfServiceData> searchResult = storeFinderFacade.locationSearch(postalCode,
					pageableData);
			request.getSession().setAttribute(POSRESULT, searchResult);
			LOG.info("Google Map Call in during make this my store");
		}
		if (pointOfService != null)
		{
			if (!customerModel.getUid().equals("anonymous") && customerModel.getName() != null
					&& !(StringUtils.contains(customerModel.getName(), "Guest User")))
			{
				customerModel.setMyStore(pointOfService);
				modelService.save(customerModel);
				httpSession.setAttribute(STOREURL, request.getContextPath() + pointOfServiceData.getUrl());
				//httpSession.setAttribute(STORENAME, pointOfService.getName());
				//httpSession.setAttribute("zipCode", pointOfServiceData.getAddress().getPostalCode());
				httpSession.setAttribute("storeChanged", true);
				setCookie(request, response, pointOfService.getName(), pointOfServiceData.getAddress().getPostalCode());
			}
			else
			{

				httpSession.setAttribute(STOREURL, request.getContextPath() + pointOfServiceData.getUrl());
				//httpSession.setAttribute(STORENAME, pointOfService.getName());
				//httpSession.setAttribute("zipCode", pointOfServiceData.getAddress().getPostalCode());
				httpSession.setAttribute("storeChanged", true);
				setCookie(request, response, pointOfService.getName(), pointOfServiceData.getAddress().getPostalCode());
			}
		}
		storeCmsPageInModel(model, getStoreFinderPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getStoreFinderPage());

		return findStores(query, INITIAL_LOCATIONS_TO_DISPLAY, null, model, null, request);
	}

	protected void setCookie(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse, final String storeName,
			final String zipCode)
	{

		final Cookie[] cookie = httpRequest.getCookies();
		boolean storeZipCodeCookieExist = false;//flag for zipCode

		if (cookie != null)
		{
			for (Cookie storeCookie : cookie)
			{

				if (storeCookie.getName().equals("store"))
				{
					storeCookie.setValue(storeName);
					storeCookie.setMaxAge(Integer.MAX_VALUE);
					storeCookie.setPath("/");
					httpResponse.addCookie(storeCookie);
					httpRequest.getSession().setAttribute(STORENAME, storeName);
					httpRequest.getSession().setAttribute(NOSTORE, false);
				}
				else
				{
					storeCookie = new Cookie("store", storeName);
					storeCookie.setMaxAge(Integer.MAX_VALUE);
					storeCookie.setPath("/");
					httpResponse.addCookie(storeCookie);
					httpRequest.getSession().setAttribute(STORENAME, storeName);
					httpRequest.getSession().setAttribute(NOSTORE, false);
				}

				//set Zip Code Cookie
				if (storeCookie.getName().equals("storeZipCode"))
				{
					storeCookie.setValue(zipCode);
					storeCookie.setMaxAge(Integer.MAX_VALUE);
					storeCookie.setPath("/");
					httpResponse.addCookie(storeCookie);
					httpRequest.getSession().setAttribute(ZIPCODE, zipCode);
					storeZipCodeCookieExist = true;
				}
			}
		}

		//set new Cookie for Zip Code
		if (!storeZipCodeCookieExist)
		{
			Cookie storeZipCodeCookie = null;
			storeZipCodeCookie = new Cookie("storeZipCode", zipCode);
			storeZipCodeCookie.setMaxAge(Integer.MAX_VALUE);
			storeZipCodeCookie.setPath("/");
			httpResponse.addCookie(storeZipCodeCookie);
			httpRequest.getSession().setAttribute(ZIPCODE, zipCode);
		}

	}

	@RequestMapping(value = "/clearSession", method = RequestMethod.GET)
	@ResponseBody
	public void clearSession(final HttpSession session)
	{
		session.removeAttribute("previousPage");
	}


	@RequestMapping(method = RequestMethod.GET)
	public String getStoreFinderPage(final Model model, final HttpServletRequest request, final HttpSession session)
			throws CMSItemNotFoundException
	{
		final String previousPage = request.getHeader("Referer");
		if (StringUtils.isBlank(previousPage))
		{
			session.setAttribute("previousPage", ROOT);
		}
		else if (!previousPage.contains("/store-finder"))
		{
			session.setAttribute("previousPage", previousPage);
		}


		setUpPageForms(model);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, storefinderBreadcrumbBuilder.getBreadcrumbs());
		storeCmsPageInModel(model, getStoreFinderPage());
		setUpMetaDataForContentPage(model, (ContentPageModel) getStoreFinderPage());
		return ControllerConstants.Views.Pages.StoreFinder.StoreFinderSearchPage;
	}

	@RequestMapping(method = RequestMethod.GET, params = "q")
	public String findStores(
			@RequestParam(value = "q") final String locationQuery,
			@RequestParam(value = "more", defaultValue = INITIAL_LOCATIONS_TO_DISPLAY_TEXT, required = false) final int requestedPageSize,
			final StoreFinderForm storeFinderForm, final Model model, final BindingResult bindingResult,
			final HttpServletRequest request) throws GeoLocatorException, MapServiceException, CMSItemNotFoundException
	{
		if (StringUtils.isBlank(locationQuery))
		{
			setUpPageForms(model);
			GlobalMessages.addErrorMessage(model, "storelocator.error.no.results.subtitle");
			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					storefinderBreadcrumbBuilder.getBreadcrumbsForLocationSearch(locationQuery));
			storeCmsPageInModel(model, getStoreFinderPage());
			return ControllerConstants.Views.Pages.StoreFinder.StoreFinderSearchPage;
		}

		final int pageSize = Math.min(MAX_LOCATIONS_TO_DISPLAY, requestedPageSize);

		// Run the location search & populate the model
		runLocationSearch(locationQuery, model, pageSize, request);
		model.addAttribute("metaRobots", "no-index,follow");
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(locationQuery);
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getSiteName() + " "
				+ getMessageSource().getMessage("storeFinder.meta.description.results", null, getI18nService().getCurrentLocale())
				+ " " + locationQuery);
		setUpMetaData(model, metaKeywords, metaDescription);
		updatePageTitle(locationQuery, model);
		return ControllerConstants.Views.Pages.StoreFinder.StoreFinderSearchPage;
	}

	private void runLocationSearch(final String locationQuery, final Model model, final int pageSize,
			final HttpServletRequest request) throws CMSItemNotFoundException
	{
		final String userAgent = request.getHeader("user-agent");

		if (!(userAgent != null && !StringUtils.isEmpty(userAgent) && userAgent.toLowerCase().contains("bot")))
		{
			// Run the location search & populate the model
			final PageableData pageableData = preparePage(pageSize);

			final StoreFinderSearchPageData<PointOfServiceData> searchResult = storeFinderFacade.locationSearch(locationQuery,
					pageableData);
			LOG.info("Google Map Call ...........to find a store");
			final List<PointOfServiceData> posList = new ArrayList<PointOfServiceData>();
			for (final PointOfServiceData pos : searchResult.getResults())
			{
				String str = pos.getFormattedDistance();
				str = str.substring(0, str.indexOf("Miles")).replaceAll(",", "");
				if (Float.parseFloat(str) < 50 && ((OshPointOfServiceData) pos).isActive())
				{
					posList.add(pos);
				}
			}
			searchResult.setResults(posList);

			model.addAttribute("storeSearchPageData", searchResult);
			model.addAttribute("locationQuery", StringEscapeUtils.escapeHtml(searchResult.getLocationText()));
			if (searchResult.getResults().isEmpty())
			{
				GlobalMessages.addErrorMessage(model, "storelocator.error.no.results.subtitle");
			}

			model.addAttribute(WebConstants.BREADCRUMBS_KEY,
					storefinderBreadcrumbBuilder.getBreadcrumbsForLocationSearch(locationQuery));
			storeCmsPageInModel(model, getStoreFinderPage());

			if (isLimitNotExceeded(searchResult))
			{
				final int nextMoreSize = searchResult.getPagination().getPageSize() + LOCATIONS_INCREMENT;
				final String showMoreUrl = UriComponentsBuilder.fromPath("/store-finder").queryParam("q", locationQuery)
						.queryParam("more", Integer.valueOf(nextMoreSize)).build().toString();
				model.addAttribute("showMoreUrl", showMoreUrl);
			}
		}
	}

	private void setUpPageForms(final Model model)
	{
		final StoreFinderForm storeFinderForm = new StoreFinderForm();
		model.addAttribute("storeFinderForm", storeFinderForm);
		final StorePositionForm storePositionForm = new StorePositionForm();
		model.addAttribute("storePositionForm", storePositionForm);
	}

	@RequestMapping(value = "/position", method =
	{ RequestMethod.GET, RequestMethod.POST })
	public String searchByCurrentPosition(
			final StorePositionForm storePositionForm,
			@RequestParam(value = "more", defaultValue = INITIAL_LOCATIONS_TO_DISPLAY_TEXT, required = false) final int requestedPageSize,
			final Model model) throws GeoLocatorException, MapServiceException, CMSItemNotFoundException
	{
		final int pageSize = Math.min(MAX_LOCATIONS_TO_DISPLAY, requestedPageSize);

		// Run the location search & populate the model
		final PageableData pageableData = preparePage(pageSize);
		final StoreFinderSearchPageData<PointOfServiceData> searchResult = storeFinderFacade.positionSearch(
				storePositionForm.getLatitude(), storePositionForm.getLongitude(), pageableData);
		model.addAttribute("storeSearchPageData", searchResult);
		model.addAttribute("locationQuery", StringEscapeUtils.escapeHtml(searchResult.getLocationText()));

		model.addAttribute(WebConstants.BREADCRUMBS_KEY, storefinderBreadcrumbBuilder.getBreadcrumbsForCurrentPositionSearch());
		storeCmsPageInModel(model, getStoreFinderPage());
		setUpPageForms(model);


		if (isLimitNotExceeded(searchResult))
		{
			final int nextMoreSize = searchResult.getPagination().getPageSize() + LOCATIONS_INCREMENT;
			final String showMoreUrl = UriComponentsBuilder.fromPath("/store-finder/position")
					.queryParam("lat", Double.valueOf(storePositionForm.getLongitude()))
					.queryParam("long", Double.valueOf(storePositionForm.getLatitude()))
					.queryParam("more", Integer.valueOf(nextMoreSize)).build().toString();
			model.addAttribute("showMoreUrl", showMoreUrl);
		}

		return ControllerConstants.Views.Pages.StoreFinder.StoreFinderSearchPage;
	}

	protected PageableData preparePage(final int pageSize)
	{
		final PageableData pageableData = new PageableData();
		pageableData.setCurrentPage(0); // Always on the first page
		pageableData.setPageSize(pageSize); // Adjust pageSize to see more
		return pageableData;
	}

	private boolean isLimitNotExceeded(final StoreFinderSearchPageData<PointOfServiceData> searchResult)
	{
		return searchResult.getPagination().getNumberOfPages() > 1
				&& searchResult.getPagination().getPageSize() < MAX_LOCATIONS_TO_DISPLAY;
	}

	protected AbstractPageModel getStoreFinderPage() throws CMSItemNotFoundException
	{
		return getContentPageForLabelOrId(STORE_FINDER_CMS_PAGE_LABEL);
	}

	protected void updatePageTitle(final String searchText, final Model model)
	{
		storeContentPageTitleInModel(
				model,
				getPageTitleResolver().resolveContentPageTitle(
						getMessageSource().getMessage("storeFinder.meta.title", null, getI18nService().getCurrentLocale()) + " "
								+ searchText));
	}
}
