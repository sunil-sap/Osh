/**
 * 
 */
package com.hybris.osh.facades.converters;

import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryData;
import de.hybris.platform.commerceservices.search.solrfacetsearch.data.SolrSearchQueryTermData;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.List;

import org.apache.commons.lang.StringUtils;


/**
 * Implemented the class to add the custom field in the url for the facets
 */
public class OshSearchQueryEncoder implements Converter<SolrSearchQueryData, String>
{
	@Override
	public String convert(final SolrSearchQueryData source) throws ConversionException
	{
		final StringBuilder builder = new StringBuilder();
		if (source != null)
		{
			if (StringUtils.isNotBlank(source.getFreeTextSearch()))
			{
				builder.append(source.getFreeTextSearch());
			}

			builder.append(':');

			if (StringUtils.isNotBlank(source.getSort()))
			{
				builder.append(source.getSort());
			}

			final List<SolrSearchQueryTermData> terms = source.getFilterTerms();
			if (terms != null && !terms.isEmpty())
			{
				for (final SolrSearchQueryTermData term : terms)
				{
					if (StringUtils.isNotBlank(term.getKey()) && StringUtils.isNotBlank(term.getValue()))
					{
						builder.append(':').append(term.getKey()).append(':').append(term.getValue());
					}
				}
				builder.append(':');
				builder.append("prodlist:true");
			}
		}

		final String result = builder.toString();

		// Special case for empty query
		if (":".equals(result))
		{
			return "";
		}

		return result;
	}

	@Override
	public String convert(final SolrSearchQueryData source, final String prototype) throws ConversionException
	{
		return convert(source);
	}
}