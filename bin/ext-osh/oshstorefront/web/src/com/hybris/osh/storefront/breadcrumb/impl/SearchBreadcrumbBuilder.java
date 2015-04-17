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
package com.hybris.osh.storefront.breadcrumb.impl;

import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.commercefacades.search.data.SearchStateData;
import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.commerceservices.search.facetdata.BreadcrumbData;
import de.hybris.platform.commerceservices.search.facetdata.ProductSearchPageData;
import de.hybris.platform.commerceservices.url.UrlResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.osh.storefront.breadcrumb.Breadcrumb;


/**
 * SearchBreadcrumbBuilder implementation for
 * {@link de.hybris.platform.commerceservices.search.facetdata.FacetSearchPageData}
 */
public class SearchBreadcrumbBuilder
{
	private static final String LAST_LINK_CLASS = "active";
	private static final String OSH_CAT = "OSH Categories";
	private static final String BRAND = "Brands";

	private CommerceCategoryService commerceCategoryService;
	private UrlResolver<CategoryModel> categoryModelUrlResolver;

	public List<Breadcrumb> getBreadcrumbs(final String categoryCode,
			final ProductSearchPageData<SearchStateData, ProductData> searchPageData) throws IllegalArgumentException
	{
		final List<Breadcrumb> breadcrumbs = new ArrayList<Breadcrumb>();

		final boolean emptyBreadcrumbs = CollectionUtils.isEmpty(searchPageData.getBreadcrumbs());
		Breadcrumb breadcrumb;
		if (categoryCode == null)
		{
			breadcrumb = new Breadcrumb("/search?text=" + searchPageData.getFreeTextSearch(),
					StringEscapeUtils.escapeHtml(searchPageData.getFreeTextSearch()), (emptyBreadcrumbs ? LAST_LINK_CLASS : ""));
			breadcrumbs.add(breadcrumb);
		}
		else
		{
			// Create category hierarchy path for breadcrumb
			final List<Breadcrumb> categoryBreadcrumbs = new ArrayList<Breadcrumb>();
			final Collection<CategoryModel> categoryModels = new ArrayList<CategoryModel>();
			final CategoryModel lastCategoryModel = getCommerceCategoryService().getCategoryForCode(categoryCode);
			categoryModels.addAll(lastCategoryModel.getSupercategories());
			categoryBreadcrumbs.add(getCategoryBreadcrumb(lastCategoryModel));

			while (!categoryModels.isEmpty())
			{
				final CategoryModel categoryModel = categoryModels.iterator().next();
				if (!(categoryModel instanceof ClassificationClassModel))
				{
					if (categoryModel != null)
					{
						categoryBreadcrumbs.add(getCategoryBreadcrumb(categoryModel));
						categoryModels.clear();
						categoryModels.addAll(categoryModel.getSupercategories());
					}
				}
			}
			Collections.reverse(categoryBreadcrumbs);
			if (!categoryBreadcrumbs.isEmpty()
					&& (categoryBreadcrumbs.get(0).getName().equalsIgnoreCase(OSH_CAT) || categoryBreadcrumbs.get(0).getName()
							.equalsIgnoreCase(BRAND)))
			{
				categoryBreadcrumbs.remove(0);
			}
			breadcrumbs.addAll(categoryBreadcrumbs);
		}

		if (!emptyBreadcrumbs)
		{
			final Iterator<BreadcrumbData<SearchStateData>> iterator = searchPageData.getBreadcrumbs().iterator();
			while (iterator.hasNext())
			{
				final BreadcrumbData<SearchStateData> breadcrumbData = iterator.next();
				final boolean last = !iterator.hasNext();
				if (breadcrumbData.getTruncateQuery() == null && Boolean.parseBoolean(breadcrumbData.getFacetValueName()))
				{

					breadcrumb = new Breadcrumb("#", StringEscapeUtils.escapeHtml(breadcrumbData.getFacetName()),
							(last ? LAST_LINK_CLASS : ""));
				}
				else
				{
					if (Boolean.parseBoolean(breadcrumbData.getFacetValueName()))
					{
						breadcrumb = new Breadcrumb("#", StringEscapeUtils.escapeHtml(breadcrumbData.getFacetName()),
								(last ? LAST_LINK_CLASS : ""));
					}
					else
					{
						breadcrumb = new Breadcrumb("#", StringEscapeUtils.escapeHtml(breadcrumbData.getFacetValueName()),
								(last ? LAST_LINK_CLASS : ""));
					}
				}
				breadcrumbs.add(breadcrumb);
			}
		}

		return breadcrumbs;
	}


	protected Breadcrumb getCategoryBreadcrumb(final CategoryModel category)
	{
		final String categoryUrl = getCategoryModelUrlResolver().resolve(category);
		return new Breadcrumb(categoryUrl, category.getName(), null);
	}

	protected CommerceCategoryService getCommerceCategoryService()
	{
		return commerceCategoryService;
	}

	@Required
	public void setCommerceCategoryService(final CommerceCategoryService commerceCategoryService)
	{
		this.commerceCategoryService = commerceCategoryService;
	}

	protected UrlResolver<CategoryModel> getCategoryModelUrlResolver()
	{
		return categoryModelUrlResolver;
	}

	@Required
	public void setCategoryModelUrlResolver(final UrlResolver<CategoryModel> categoryModelUrlResolver)
	{
		this.categoryModelUrlResolver = categoryModelUrlResolver;
	}
}
