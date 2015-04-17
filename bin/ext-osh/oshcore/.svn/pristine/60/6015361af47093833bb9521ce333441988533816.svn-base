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
package com.hybris.osh.core.valueproviders;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.impl.ProductStockLevelStatusValueProvider;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.stock.exception.StockLevelNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.hybris.osh.core.constants.OshCoreConstants;


/**
 * This ValueProvider will provide the product stock level status. The stock level count changes so frequently that it
 * is not sensible to index the count directly, but rather to map the count to a status (or band) and then index the
 * status.
 */
public class OshProductStockLevelValueProvider extends ProductStockLevelStatusValueProvider
{
	final static Logger LOG = Logger.getLogger(OshProductStockLevelValueProvider.class);

	private static final Integer ZERO = Integer.valueOf(0);

	@Override
	protected List<FieldValue> createFieldValue(final ProductModel product, final IndexedProperty indexedProperty)
	{
		final List<FieldValue> fieldValues = new ArrayList<FieldValue>();

		Integer stockLevel = ZERO;
		try
		{
			final WarehouseModel warehouseModel = getWarehouseService().getWarehouseForCode(OshCoreConstants.WAREHOUSE);
			stockLevel = Integer.valueOf(getProductStockLevel(product, warehouseModel));
			if (stockLevel < ZERO)
			{
				stockLevel = ZERO;
			}
		}
		catch (final StockLevelNotFoundException slnfe)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format("Could not find stock level for product %s", product));
			}
		}

		addFieldValues(fieldValues, indexedProperty, stockLevel);

		return fieldValues;
	}

	/**
	 * Default implementation will search warehouses that have 'default' set to true.
	 * 
	 * @param product
	 * @return the stock level status for the product
	 */
	private int getProductStockLevel(final ProductModel product, final WarehouseModel warehouseModel)
	{
		return getStockService().getStockLevelAmount(product, warehouseModel);
	}

}
