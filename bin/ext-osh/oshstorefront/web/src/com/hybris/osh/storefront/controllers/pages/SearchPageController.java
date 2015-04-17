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

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.AutocompleteSuggestionData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hybris.osh.core.logger.OshSearchLogger;
import com.hybris.osh.storefront.breadcrumb.impl.SearchBreadcrumbBuilder;
import com.hybris.osh.storefront.constants.WebConstants;
import com.hybris.osh.storefront.util.MetaSanitizerUtil;
import com.hybris.osh.storefront.util.XSSFilterUtil;


@Controller
@Scope("tenant")
@RequestMapping("/search")
public class SearchPageController extends AbstractSearchPageController
{
	private static final Logger LOG = Logger.getLogger(OshSearchLogger.class);
	private static final String SEARCH_CMS_PAGE_ID = "search";
	private static final String NO_RESULTS_CMS_PAGE_ID = "searchEmpty";

	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "searchBreadcrumbBuilder")
	private SearchBreadcrumbBuilder searchBreadcrumbBuilder;


	@SuppressWarnings("boxing")
	@RequestMapping(method = RequestMethod.GET, params = "!q")
	public String textSearch(@RequestParam(value = "text", defaultValue = "") final String searchText,
			final HttpServletRequest request, final Model model) throws CMSItemNotFoundException
	{
		if (searchText != null && !searchText.equals(""))
		{
			LOG.info(searchText);
		}
		if (StringUtils.isNotBlank(searchText))
		{
			final PageableData pageableData = createPageableData(0, getSearchPageSize(), null, ShowMode.Page);
			final SearchStateData searchState = new SearchStateData();
			searchState.setQuery(XSSFilterUtil.filter(searchText));
			final ProductSearchPageData<SearchStateData, ProductData> searchPageData = productSearchFacade.textSearch(searchState,
					pageableData);
			final Map<Integer, Integer> pagination = new HashMap<Integer, Integer>();
			for (int i = 0; i <= 3; i++)
			{
				if (i < searchPageData.getPagination().getNumberOfPages() - 1)
				{
					int k = 0;
					final int j = i;
					pagination.put(j + 1, i + k);
					k++;
				}
			}
			model.addAttribute("pagination", pagination);

			if (searchPageData == null)
			{
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
			}
			else if (searchPageData.getKeywordRedirectUrl() != null)
			{
				// if the search engine returns a redirect, just
				return "redirect:" + searchPageData.getKeywordRedirectUrl();
			}
			else if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
			{
				model.addAttribute("searchPageData", searchPageData);
				storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
				updatePageTitle(searchPageData.getFreeTextSearch(), model);
			}
			else
			{
				storeContinueUrl(request);

				model.addAttribute("searchPageData", searchPageData);
				model.addAttribute("isShowAllAllowed", Boolean.valueOf(isShowAllAllowed(searchPageData)));
				storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
				updatePageTitle(searchPageData.getFreeTextSearch(), model);
			}
			model.addAttribute(WebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(null, searchPageData));
		}
		else
		{
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
		}
		model.addAttribute("pageType", PageType.ProductSearch);
		model.addAttribute("metaRobots", "no-index,follow");
		model.addAttribute("defaultPageSize", Config.getInt("defaultPageSize", 24));

		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getMessageSource().getMessage(
				"search.meta.description.results", null, getI18nService().getCurrentLocale())
				+ " "
				+ searchText
				+ " "
				+ getMessageSource().getMessage("search.meta.description.on", null, getI18nService().getCurrentLocale())
				+ " "
				+ getSiteName());
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
		setUpMetaData(model, metaKeywords, metaDescription);

		return getViewForPage(model);
	}

	@SuppressWarnings("boxing")
	@RequestMapping(method = RequestMethod.GET, params = "q")
	public String refineSearch(@RequestParam("q") String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode, @RequestParam(value = "sort",
					required = false) final String sortCode, @RequestParam(value = "text", required = false) final String searchText,
			@RequestParam(value = "showItem", defaultValue = "0") int showItem, final HttpServletRequest request, final Model model)
			throws CMSItemNotFoundException
	{

		if (searchQuery != null && searchQuery.contains("&showItem"))
		{
			final String[] stringQueries = searchQuery.split("&showItem=");
			searchQuery = stringQueries[0];
			showItem = Integer.parseInt(stringQueries[1]);
		}
		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = performSearch(searchQuery, page, showMode,
				sortCode, getSearchPageSize(), showItem);

		final Map<Integer, Integer> pagination = getPagination(searchPageData, page);
		model.addAttribute("pagination", pagination);
		final String pageUrl;
		if (showItem != 0)
		{
			pageUrl = searchPageData.getCurrentQuery().getUrl() + "&showItem=" + showItem;
		}
		else
		{
			pageUrl = searchPageData.getCurrentQuery().getUrl();
		}
		model.addAttribute("pageUrl", pageUrl);
		populateModel(model, searchPageData, showMode);
		if (searchPageData.getPagination().getTotalNumberOfResults() == 0)
		{
			updatePageTitle(searchPageData.getFreeTextSearch(), model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(NO_RESULTS_CMS_PAGE_ID));
		}
		else
		{
			storeContinueUrl(request);
			updatePageTitle(searchPageData.getFreeTextSearch(), model);
			storeCmsPageInModel(model, getContentPageForLabelOrId(SEARCH_CMS_PAGE_ID));
		}
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(null, searchPageData));
		model.addAttribute("pageType", PageType.ProductSearch);
		model.addAttribute("defaultPageSize", Config.getInt("defaultPageSize", 24));
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(getMessageSource().getMessage(
				"search.meta.description.results", null, getI18nService().getCurrentLocale())
				+ " "
				+ searchText
				+ " "
				+ getMessageSource().getMessage("search.meta.description.on", null, getI18nService().getCurrentLocale())
				+ " "
				+ getSiteName());
		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(searchText);
		setUpMetaData(model, metaKeywords, metaDescription);

		return getViewForPage(model);
	}

	protected ProductSearchPageData<SearchStateData, ProductData> performSearch(final String searchQuery, final int page,
			final ShowMode showMode, final String sortCode, final int pageSize, final int showItem)
	{

		final PageableData pageableData = createPageableData(page, pageSize, sortCode, showMode);
		if (showItem != 0)
		{
			pageableData.setPageSize(showItem);
		}
		final SearchStateData searchState = new SearchStateData();
		searchState.setQuery(searchQuery);
		return productSearchFacade.textSearch(searchState, pageableData);
	}

	@ResponseBody
	@RequestMapping(value = "/results", method = RequestMethod.GET)
	public SearchResultsData<ProductData> jsonSearchResults(@RequestParam("q") final String searchQuery, @RequestParam(
			value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode, @RequestParam(value = "showItem",
					defaultValue = "0") final int showItem, @RequestParam(value = "sort", required = false) final String sortCode)
			throws CMSItemNotFoundException
	{
		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = performSearch(searchQuery, page, showMode,
				sortCode, getSearchPageSize(), showItem);
		final SearchResultsData<ProductData> searchResultsData = new SearchResultsData<ProductData>();
		searchResultsData.setResults(searchPageData.getResults());
		searchResultsData.setPagination(searchPageData.getPagination());
		return searchResultsData;
	}

	@ResponseBody
	@RequestMapping(value = "/facets", method = RequestMethod.GET)
	public FacetRefinement<SearchStateData> getFacets(@RequestParam("q") final String searchQuery, @RequestParam(value = "page",
			defaultValue = "0") final int page, @RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "sort", required = false) final String sortCode) throws CMSItemNotFoundException
	{
		final SearchStateData searchState = new SearchStateData();
		searchState.setQuery(searchQuery);
		final ProductSearchPageData<SearchStateData, ProductData> searchPageData = productSearchFacade.textSearch(searchState,
				createPageableData(page, getSearchPageSize(), sortCode, showMode));
		final List<FacetData<SearchStateData>> facets = refineFacets(searchPageData.getFacets(),
				convertBreadcrumbsToFacets(searchPageData.getBreadcrumbs()));
		final FacetRefinement<SearchStateData> refinement = new FacetRefinement<SearchStateData>();
		refinement.setFacets(facets);
		refinement.setCount(searchPageData.getPagination().getTotalNumberOfResults());
		refinement.setBreadcrumbs(searchPageData.getBreadcrumbs());
		return refinement;
	}

	@ResponseBody
	@RequestMapping(value = "/autocomplete", method = RequestMethod.GET)
	public List<String> getAutocompleteSuggestions(@RequestParam("term") final String term)
	{
		final List<String> terms = new ArrayList<String>();
		for (final AutocompleteSuggestionData termData : productSearchFacade.getAutocompleteSuggestions(term))
		{
			terms.add(termData.getTerm());
		}
		return terms;
	}

	@ResponseBody
	@RequestMapping(value = "/autocompleteSecure", method = RequestMethod.GET)
	public List<String> getAutocompleteSuggestionsSecure(@RequestParam("term") final String term)
	{
		return getAutocompleteSuggestions(term);
	}


	protected void updatePageTitle(final String searchText, final Model model)
	{
		storeContentPageTitleInModel(
				model,
				getPageTitleResolver().resolveContentPageTitle(
						getMessageSource().getMessage("search.meta.title", null, getI18nService().getCurrentLocale()) + " "
								+ searchText));
	}
}
