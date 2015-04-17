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
package com.hybris.osh.core.search.populators;

import de.hybris.platform.commerceservices.search.facetdata.FacetData;
import de.hybris.platform.commerceservices.search.facetdata.FacetValueData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.config.CommerceIndexedProperty;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.populators.SearchResponseFacetsPopulator;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.Facet;
import de.hybris.platform.solrfacetsearch.search.FacetValue;
import de.hybris.platform.solrfacetsearch.search.SearchResult;
import de.hybris.platform.util.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 
 * Class to customize the facets
 * 
 * @param <ITEM>
 */
public class OshSearchResponseFacetsPopulator<ITEM> extends SearchResponseFacetsPopulator<ITEM>

{
	@Autowired
	ConfigurationService configurationService;

	protected static final int DEFAULT_PRIORITY = -2000;

	private int topFacetCount = 5;

	@Override
	protected int getTopFacetCount()
	{
		return topFacetCount;
	}


	@Override
	public void setTopFacetCount(final int topFacetCount)
	{
		this.topFacetCount = topFacetCount;
	}

	@Override
	protected List<FacetData<SolrSearchQueryData>> buildFacets(final SearchResult solrSearchResult,
			final SolrSearchQueryData searchQueryData, final IndexedType indexedType)
	{
		final List<Facet> solrSearchResultFacets = solrSearchResult.getFacets();
		final List<FacetData<SolrSearchQueryData>> result = new ArrayList<FacetData<SolrSearchQueryData>>(
				solrSearchResultFacets.size());
		final String value = Config.getString("morewaystoshopflags", "FreeShipping,WhiteGlove,Recall,NewSKU");
		final String[] flagsList = (value.toLowerCase()).split(",");
		final List flagList = Arrays.asList(flagsList);


		for (final Facet facet : solrSearchResultFacets)
		{
			final IndexedProperty indexedProperty = indexedType.getIndexedProperty(facet.getName());
			if (indexedProperty instanceof CommerceIndexedProperty)
			{
				// We know this is a CommerceIndexedProperty
				final CommerceIndexedProperty commerceIndexedProperty = (CommerceIndexedProperty) indexedProperty;
				// Ignore any facets with a priority less than 0 as they are for internal use only
				if (commerceIndexedProperty.getPriority() > 0)
				{
					final FacetData<SolrSearchQueryData> facetData = createFacetData();
					facetData.setCode(facet.getName());
					facetData.setCategory(commerceIndexedProperty.isCategoryField());
					final String displayName = commerceIndexedProperty.getDisplayName();


					facetData.setName(displayName == null ? facet.getName() : displayName);

					facetData.setMultiSelect(commerceIndexedProperty.isMultiSelect());

					facetData.setPriority(commerceIndexedProperty.getPriority());

					Comparator<FacetValue> facetValueComparator = null;

					if (!indexedProperty.isRanged() && indexedProperty.getFacetSortProvider() != null)
					{
						facetValueComparator = indexedProperty.getFacetSortProvider().getComparatorForTypeAndProperty(indexedType,
								commerceIndexedProperty);
					}

					buildFacetValues(facetData, facet, indexedProperty, solrSearchResult, searchQueryData, facetValueComparator);

					// Only add the facet if there are values
					if (facetData.getValues() != null && !facetData.getValues().isEmpty())
					{
						if (flagList.contains(facetData.getName().toLowerCase()))
						{
							boolean isMoreWayPresent = false;
							for (final FacetData<SolrSearchQueryData> facetDataEntry : result)
							{
								if (facetDataEntry.getName().equals("moreWaysToShop"))
								{
									for (final FacetValueData<SolrSearchQueryData> facetValue : facetData.getValues())
									{
										facetValue.setName(facetData.getName());
									}

									facetDataEntry.getValues().addAll(facetData.getValues());
									isMoreWayPresent = true;
								}
							}
							if (!isMoreWayPresent)
							{
								for (final FacetValueData<SolrSearchQueryData> facetValue : facetData.getValues())
								{
									facetValue.setName(facetData.getName());
								}

								facetData.setName("moreWaysToShop");
								result.add(facetData);
							}
						}
						else
						{
							result.add(facetData);
						}
					}
				}
			}
			else
			{
				// We know nothing about this property
				final FacetData<SolrSearchQueryData> facetData = createFacetData();
				facetData.setCode(facet.getName());
				facetData.setName(facet.getName());
				facetData.setPriority(DEFAULT_PRIORITY);
				buildFacetValues(facetData, facet, indexedProperty, solrSearchResult, searchQueryData,
						FacetValueDisplayNameComparator.INSTANCE);

				// Only add the facet if there are values
				if (facetData.getValues() != null && !facetData.getValues().isEmpty())
				{
					result.add(facetData);
				}
			}
		}

		// Sort the FacetData by priority and display name
		Collections.sort(result, FacetDataComparator.INSTANCE);

		return result;
	}

	@Override
	protected void buildFacetValues(final FacetData<SolrSearchQueryData> facetData, final Facet facet,
			final IndexedProperty indexedProperty, final SearchResult solrSearchResult, final SolrSearchQueryData searchQueryData,
			final Comparator<FacetValue> facetValueComparator)
	{
		final List<FacetValue> facetValues = facet.getFacetValues();

		if (supportsTopValues(facetData, indexedProperty))
		{
			final List<FacetValueData<SolrSearchQueryData>> topFacetValues = new ArrayList<FacetValueData<SolrSearchQueryData>>(
					facetValues.size());

			// Go through the facet values in count order
			for (final FacetValue facetValue : facetValues)
			{
				final FacetValueData<SolrSearchQueryData> facetValueData = buildFacetValue(facetData, facet, facetValue,
						solrSearchResult, searchQueryData);
				if (facetValueData != null && facetValueData.getName() != null
						&& !(facetValueData.getName().equalsIgnoreCase("OSH CATEGORIES"))
						&& !(facetValueData.getName().equalsIgnoreCase("BRANDS"))
						&& !(facetValueData.getCode().equalsIgnoreCase(searchQueryData.getCategoryCode())))
				{
					topFacetValues.add(facetValueData);

					if (topFacetValues.size() >= getTopFacetCount())
					{
						// Stop creating top facets when we have enough
						break;
					}
				}
			}

			if (!topFacetValues.isEmpty())
			{
				facetData.setTopValues(topFacetValues);
			}
		}

		final List<FacetValueData<SolrSearchQueryData>> allFacetValues = new ArrayList<FacetValueData<SolrSearchQueryData>>(
				facetValues.size());

		// Sort the facet values
		if (facetValueComparator != null)
		{
			Collections.sort(facetValues, facetValueComparator);
		}

		for (final FacetValue facetValue : facetValues)
		{
			final FacetValueData<SolrSearchQueryData> facetValueData = buildFacetValue(facetData, facet, facetValue,
					solrSearchResult, searchQueryData);
			if (facetValueData != null && facetValueData.getName() != null
					&& !(facetValueData.getName().equalsIgnoreCase("OSH CATEGORIES"))
					&& !(facetValueData.getName().equalsIgnoreCase("BRANDS"))
					&& !(facetValueData.getCode().equalsIgnoreCase(searchQueryData.getCategoryCode()))
					&& !(facetValueData.getName().equalsIgnoreCase("false")))
			{
				allFacetValues.add(facetValueData);
			}
		}

		facetData.setValues(allFacetValues);

		if (facetData.getTopValues() != null && facetData.getTopValues().size() >= facetData.getValues().size())
		{
			// No point showing top facets as we have so few
			facetData.setTopValues(null);
		}
	}

	@Override
	public boolean supportsTopValues(final FacetData<SolrSearchQueryData> facetData, final IndexedProperty indexedProperty)
	{
		return !indexedProperty.isRanged();
	}



}
