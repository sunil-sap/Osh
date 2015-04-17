/**
 * 
 */
package com.hybris.osh.core.search.solrfacetsearch.querybuilder.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder.impl.AbstractFreeTextQueryBuilder;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 *
 */
public class DefaultOshFreeTextQueryBuilder extends AbstractFreeTextQueryBuilder
{
	private static final Logger LOG = Logger.getLogger(DefaultOshFreeTextQueryBuilder.class);

	private String propertyName;
	private int boost;

	protected String getPropertyName()
	{
		return propertyName;
	}

	@Required
	public void setPropertyName(final String propertyName)
	{
		this.propertyName = propertyName;
	}

	protected int getBoost()
	{
		return boost;
	}

	@Required
	public void setBoost(final int boost)
	{
		this.boost = boost;
	}

	@Override
	protected void addFreeTextQuery(final SearchQuery searchQuery, final IndexedProperty indexedProperty, final String value,
			final double boost)
	{
		final String field = indexedProperty.getName();
		if (!indexedProperty.isFacet())
		{
			if ("text".equalsIgnoreCase(indexedProperty.getType()))
			{
				addFreeTextQuery(searchQuery, field, value.toLowerCase(), "", boost);
				addFreeTextQuery(searchQuery, field, value.toLowerCase(), "*", boost / 2.0d);
				//addFreeTextQuery(searchQuery, field, value.toLowerCase(), "~", boost / 4.0d);
			}
			else
			{
				addFreeTextQuery(searchQuery, field, value, "", boost);
				addFreeTextQuery(searchQuery, field, value, "*", boost / 2.0d);
			}
		}
		else
		{
			LOG.warn("Not searching " + indexedProperty
					+ ". Free text search not available in facet property. Configure an additional text property for searching.");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.commerceservices.search.solrfacetsearch.querybuilder.FreeTextQueryBuilder#addFreeTextQuery(
	 * de.hybris.platform.solrfacetsearch.search.SearchQuery, java.lang.String, java.lang.String[])
	 */
	@Override
	public void addFreeTextQuery(final SearchQuery searchQuery, final String fullText, final String[] textWords)
	{
		final IndexedProperty indexedProperty = searchQuery.getIndexedType().getIndexedProperty(getPropertyName());
		if (indexedProperty != null)
		{
			addFreeTextQuery(searchQuery, indexedProperty, fullText, textWords, getBoost());
		}
	}


}
