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

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.CategoryPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.data.CategoryData;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.ProductSearchFacade;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetRefinement;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.facetdata.ProductCategorySearchPageData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.url.UrlResolver;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hybris.osh.storefront.breadcrumb.impl.SearchBreadcrumbBuilder;
import com.hybris.osh.storefront.constants.WebConstants;
import com.hybris.osh.storefront.controllers.ControllerConstants;
import com.hybris.osh.storefront.controllers.util.CategoryDataHelper;
import com.hybris.osh.storefront.util.MetaSanitizerUtil;


/**
 * Controller for a category page
 */
@Controller
@Scope("tenant")
@RequestMapping(value = "/**/c/{categoryCode}")
public class CategoryPageController extends AbstractSearchPageController
{
	protected static final Logger LOG = Logger.getLogger(CategoryPageController.class);

	private static final String PRODUCT_GRID_PAGE = "category/productGridPage";
	private static final String SALE_LINK = "::Sale:Sale";

	@Resource(name = "productFacade")
	private ProductFacade productFacade;

	@Resource(name = "modelService")
	private ModelService modelService;

	@Resource(name = "productService")
	private ProductService productService;


	@Resource(name = "productSearchFacade")
	private ProductSearchFacade<ProductData> productSearchFacade;

	@Resource(name = "cmsPageService")
	private CMSPageService cmsPageService;

	@Resource(name = "commerceCategoryService")
	private CommerceCategoryService commerceCategoryService;

	@Resource(name = "searchBreadcrumbBuilder")
	private SearchBreadcrumbBuilder searchBreadcrumbBuilder;

	@Resource(name = "categoryModelUrlResolver")
	private UrlResolver<CategoryModel> categoryModelUrlResolver;

	@Resource(name = "priceDataFactory")
	private PriceDataFactory priceDataFactory;




	@SuppressWarnings(
	{ "boxing", "deprecation" })
	@RequestMapping(method = RequestMethod.GET)
	public String category(@PathVariable("categoryCode") final String categoryCode,
			@RequestParam(value = "q", required = false) String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showItem", defaultValue = "0") int showItem,
			@RequestParam(value = "sort", required = false) final String sortCode, final Model model,
			final HttpServletRequest request, final HttpServletResponse response) throws UnsupportedEncodingException
	{
		String catCode = null;
		if (request.getRequestURI().contains("Brands") && categoryCode.contains("-"))
		{
			final String catCodeVar[] = categoryCode.split("-");
			catCode = catCodeVar[1];
			request.getSession().setAttribute("brandCategory", catCodeVar[0]);
		}
		CategoryModel category = null;
		if (categoryCode.contains("-"))
		{
			category = commerceCategoryService.getCategoryForCode(catCode);
		}
		else
		{
			category = commerceCategoryService.getCategoryForCode(categoryCode);

		}

		final String redirection = checkRequestUrl(request, response, categoryModelUrlResolver.resolve(category));
		if (StringUtils.isNotEmpty(redirection))
		{
			return redirection;
		}
		if (categoryCode.equalsIgnoreCase("oshCategories") && searchQuery == null)
		{
			searchQuery = SALE_LINK;
		}
		final CategoryPageModel categoryPage = getCategoryPage(category);
		if (searchQuery != null && searchQuery.contains("&showItem"))
		{
			final String[] stringQueries = searchQuery.split("&showItem=");
			searchQuery = stringQueries[0];
			showItem = Integer.parseInt(stringQueries[1]);
		}

		if (request.getSession().getAttribute("brandCategory") != null && searchQuery == null)
		{
			searchQuery = "::brand:" + request.getSession().getAttribute("brandCategory") + ":prodlist:true";
			//categoryCode = (String) request.getSession().getAttribute("brandCategory");
		}
		final CategorySearchEvaluator categorySearch = new CategorySearchEvaluator(categoryCode, searchQuery, page, showMode,
				sortCode, categoryPage, showItem);
		categorySearch.doSearch();

		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = categorySearch
				.getSearchPageData();
		request.getSession().removeAttribute("brandCategory");
		final boolean showCategoriesOnly = categorySearch.isShowCategoriesOnly();
		if (category.getPicture() != null)
		{
			model.addAttribute("categoryUrl", category.getPicture().getURL());
		}

		storeCmsPageInModel(model, categorySearch.categoryPage);
		storeContinueUrl(request);

		//Code for pagination
		final Map<Integer, Integer> pagination = getPagination(searchPageData, page);
		model.addAttribute("pagination", pagination);
		final String pageUrl;
		if (showItem != 0)
		{
			pageUrl = searchPageData.getCurrentQuery().getUrl() + ":prodlist:true" + "&showItem=" + showItem;
		}
		else
		{
			pageUrl = searchPageData.getCurrentQuery().getUrl() + ":prodlist:true";
		}
		model.addAttribute("pageUrl", pageUrl);

		populateModel(model, searchPageData, showMode);
		model.addAttribute(WebConstants.BREADCRUMBS_KEY, searchBreadcrumbBuilder.getBreadcrumbs(categoryCode, searchPageData));
		model.addAttribute("showCategoriesOnly", Boolean.valueOf(showCategoriesOnly));
		model.addAttribute("categoryName", category.getName());

		model.addAttribute("pageType", PageType.Category);
		model.addAttribute("allSubCategory", category.getCategories());
		updatePageTitle(category, searchPageData.getBreadcrumbs(), model);
		CategoryDataHelper.setCurrentCategory(request, categoryCode);
		model.addAttribute("defaultPageSize", Config.getInt("defaultPageSize", 24));

		if (searchQuery != null)
		{
			model.addAttribute("metaRobots", "no-index,follow");

		}

		final String metaKeywords = MetaSanitizerUtil.sanitizeKeywords(category.getKeywords());
		final String metaDescription = MetaSanitizerUtil.sanitizeDescription(category.getDescription());
		setUpMetaData(model, metaKeywords, metaDescription);
		if (categoryCode.equalsIgnoreCase("oshCategories"))
		{
			return ControllerConstants.Views.Pages.SubCategory.SubCategoryPage;
		}
		for (final CategoryModel brandcategory : category.getAllSupercategories())
		{
			if (brandcategory.getCode().equalsIgnoreCase("brands"))
			{
				return ControllerConstants.Views.Pages.SubCategory.SubCategoryPage;
			}
		}

		if (category.getAllSupercategories().size() > 2 || searchQuery != null && searchQuery.contains("prodlist:true"))
		{
			return ControllerConstants.Views.Pages.SubCategory.SubCategoryPage;
		}
		else
		{
			final List<CategoryModel> brands = new ArrayList<CategoryModel>();

			for (final FacetData<SearchStateData> facets : searchPageData.getFacets())
			{
				if (facets.getName().equalsIgnoreCase("Brand"))
				{
					for (final FacetValueData<SearchStateData> facetValue : facets.getValues())
					{
						final Map<String, Long> unsortMap = new HashMap<String, Long>();
						unsortMap.put(facetValue.getCode(), Long.valueOf(facetValue.getCount()));
						final Map<Object, Long> sortedMap = sortByComparator(unsortMap);
						for (final Map.Entry entry : sortedMap.entrySet())
						{
							final CategoryModel brandCategory = commerceCategoryService.getCategoryForCode((String) entry.getKey());
							brands.add(brandCategory);
						}
					}
				}
			}
			model.addAttribute("brands", brands);
			return getViewPage(categorySearch.categoryPage);
		}

	}

	/**
	 * @param unsortMap
	 * @return Map
	 */
	private static Map sortByComparator(final Map unsortMap)
	{
		final List list = new LinkedList(unsortMap.entrySet());
		// sort list based on comparator
		Collections.sort(list, new Comparator()
		{
			@Override
			public int compare(final Object obj1, final Object obj2)
			{
				final Long val1 = (Long) ((Map.Entry) obj1).getValue();
				final Long val2 = (Long) ((Map.Entry) obj2).getValue();
				if (val1.longValue() > val2.longValue())
				{
					return -1;
				}
				else if (val1.longValue() > val2.longValue())
				{
					return 1;
				}
				return 0;
			}
		});
		// put sorted list into map again
		// LinkedHashMap make sure order in which keys were inserted
		final Map sortedMap = new LinkedHashMap();
		for (final Iterator it = list.iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();
			if (sortedMap.size() > 12)
			{
				break;
			}
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	@ResponseBody
	@RequestMapping(value = "/facets", method = RequestMethod.GET)
	public FacetRefinement<SearchStateData> getFacets(@PathVariable("categoryCode") final String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showItem", defaultValue = "0") final int showItem,
			@RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException
	{
		final CategoryModel category = commerceCategoryService.getCategoryForCode(categoryCode);
		final CategoryPageModel categoryPage = getCategoryPage(category);
		final CategorySearchEvaluator categorySearch = new CategorySearchEvaluator(categoryCode, searchQuery, page, showMode,
				sortCode, categoryPage, showItem);
		categorySearch.doSearch();

		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = categorySearch
				.getSearchPageData();

		final List<FacetData<SearchStateData>> facets = refineFacets(searchPageData.getFacets(),
				convertBreadcrumbsToFacets(searchPageData.getBreadcrumbs()));
		final FacetRefinement<SearchStateData> refinement = new FacetRefinement<SearchStateData>();
		refinement.setFacets(facets);
		refinement.setCount(searchPageData.getPagination().getTotalNumberOfResults());
		refinement.setBreadcrumbs(searchPageData.getBreadcrumbs());
		return refinement;
	}


	@ResponseBody
	@RequestMapping(value = "/results", method = RequestMethod.GET)
	public SearchResultsData<ProductData> getResults(@PathVariable("categoryCode") final String categoryCode,
			@RequestParam(value = "q", required = false) final String searchQuery,
			@RequestParam(value = "page", defaultValue = "0") final int page,
			@RequestParam(value = "show", defaultValue = "Page") final ShowMode showMode,
			@RequestParam(value = "showItem", defaultValue = "0") final int showItem,
			@RequestParam(value = "sort", required = false) final String sortCode) throws UnsupportedEncodingException
	{
		final CategoryModel category = commerceCategoryService.getCategoryForCode(categoryCode);
		final CategoryPageModel categoryPage = getCategoryPage(category);
		final CategorySearchEvaluator categorySearch = new CategorySearchEvaluator(categoryCode, searchQuery, page, showMode,
				sortCode, categoryPage, showItem);
		categorySearch.doSearch();

		final ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData = categorySearch
				.getSearchPageData();

		final SearchResultsData<ProductData> searchResultsData = new SearchResultsData<ProductData>();
		searchResultsData.setResults(searchPageData.getResults());
		searchResultsData.setPagination(searchPageData.getPagination());
		return searchResultsData;
	}


	protected boolean categoryHasDefaultPage(final CategoryPageModel categoryPage)
	{
		return Boolean.TRUE.equals(categoryPage.getDefaultPage());
	}

	protected CategoryPageModel getCategoryPage(final CategoryModel category)
	{
		try
		{
			return cmsPageService.getPageForCategory(category);
		}
		catch (final CMSItemNotFoundException ignore)
		{
			// Ignore
		}
		return null;
	}

	protected CategoryPageModel getDefaultCategoryPage()
	{
		try
		{
			return cmsPageService.getPageForCategory(null);
		}
		catch (final CMSItemNotFoundException ignore)
		{
			// Ignore
		}
		return null;
	}


	protected <QUERY> void updatePageTitle(final CategoryModel category, final List<BreadcrumbData<QUERY>> appliedFacets,
			final Model model)
	{
		storeContentPageTitleInModel(model, getPageTitleResolver().resolveCategoryPageTitle(category));
	}

	protected String getViewPage(final CategoryPageModel categoryPage)
	{
		if (categoryPage != null)
		{
			final String targetPage = getViewForPage(categoryPage);
			if (targetPage != null && !targetPage.isEmpty())
			{
				return targetPage;
			}
		}
		return PAGE_ROOT + PRODUCT_GRID_PAGE;
	}

	@ExceptionHandler(UnknownIdentifierException.class)
	public String handleUnknownIdentifierException(final UnknownIdentifierException exception, final HttpServletRequest request)
	{
		request.setAttribute("message", exception.getMessage());
		return FORWARD_PREFIX + "/404";
	}

	private class CategorySearchEvaluator
	{
		private final String categoryCode;
		private final String searchQuery;
		private final int page;
		private final ShowMode showMode;
		private final String sortCode;
		private CategoryPageModel categoryPage;
		private boolean showCategoriesOnly;
		private final int showItem;

		private ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> searchPageData;

		public CategorySearchEvaluator(final String categoryCode, final String searchQuery, final int page,
				final ShowMode showMode, final String sortCode, final CategoryPageModel categoryPage, final int showItem)
		{
			this.categoryCode = categoryCode;
			this.searchQuery = searchQuery;
			this.page = page;
			this.showMode = showMode;
			this.sortCode = sortCode;
			this.categoryPage = categoryPage;
			this.showItem = showItem;
		}

		public boolean isShowCategoriesOnly()
		{
			return showCategoriesOnly;
		}

		public ProductCategorySearchPageData<SearchStateData, ProductData, CategoryData> getSearchPageData()
		{
			return searchPageData;
		}

		public void doSearch()
		{
			showCategoriesOnly = false;
			if (searchQuery == null)
			{
				// Direct category link without filtering
				searchPageData = productSearchFacade.categorySearch(categoryCode);
				if (categoryPage != null)
				{
					showCategoriesOnly = !categoryHasDefaultPage(categoryPage)
							&& CollectionUtils.isNotEmpty(searchPageData.getSubCategories());
				}
			}
			else
			{
				// We have some search filtering
				if (categoryPage == null || !categoryHasDefaultPage(categoryPage))
				{
					// Load the default category page
					categoryPage = getDefaultCategoryPage();
				}

				final SearchStateData searchState = new SearchStateData();
				searchState.setQuery(searchQuery);

				final PageableData pageableData = createPageableData(page, getSearchPageSize(), sortCode, showMode);
				if (showItem != 0)
				{
					pageableData.setPageSize(showItem);
				}
				searchPageData = productSearchFacade.categorySearch(categoryCode, searchState, pageableData);
			}
		}
	}
}
