package com.hybris.osh.core.valueproviders;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractPropertyFieldValueProvider;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.hybris.osh.core.price.services.OshPriceService;


public class OshProductPriceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider, Serializable
{

	private FieldNameProvider fieldNameProvider;
	private OshPriceService oshPriceService;

	@Override
	public Collection getFieldValues(final IndexConfig indexConfig, final IndexedProperty indexedProperty, final Object model)
			throws FieldValueProviderException
	{
		final Collection fieldValues = new ArrayList();
		ProductModel product = null;
		String rangeName = null;

		if (model instanceof ProductModel)
		{
			product = (ProductModel) model;
		}
		else
		{
			throw new FieldValueProviderException("Cannot evaluate price of non-product item");
		}

		final Iterator iterator = indexConfig.getCurrencies().iterator();
		CurrencyModel currency;
		CurrencyModel sessionCurrency;
		currency = (CurrencyModel) iterator.next();
		sessionCurrency = i18nService.getCurrentCurrency();
		i18nService.setCurrentCurrency(currency);

		//final Collection<VariantProductModel> vpm = product.getVariants();
		final PriceInformation price = oshPriceService.getPriceInfoForSolrFacets(product);

		if (price != null)
		{
			final Double value = Double.valueOf(price.getPriceValue().getValue());
			rangeName = getRangeName(indexedProperty, value, currency.getIsocode());
			final Collection fieldNames = fieldNameProvider.getFieldNames(indexedProperty, price.getPriceValue().getCurrencyIso());
			String fieldName;
			for (final Iterator iterator1 = fieldNames.iterator(); iterator1.hasNext(); fieldValues.add(new FieldValue(fieldName,
					rangeName != null ? ((Object) (rangeName)) : ((Object) (value)))))
			{
				fieldName = (String) iterator1.next();
			}

		}

		i18nService.setCurrentCurrency(sessionCurrency);
		i18nService.setCurrentCurrency(sessionCurrency);
		return fieldValues;
	}

	public void setFieldNameProvider(final FieldNameProvider fieldNameProvider)
	{
		this.fieldNameProvider = fieldNameProvider;
	}


	public void setOshPriceService(final OshPriceService oshPriceService)
	{
		this.oshPriceService = oshPriceService;
	}

}
