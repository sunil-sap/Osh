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
package com.hybris.osh.facades.converters.populator;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator;
import de.hybris.platform.commercefacades.product.data.ProductData;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.stock.strategy.StockLevelStatusStrategy;

import java.util.Collection;
import java.util.Collections;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * Populate the product data with stock information
 */
public class OshProductStockPopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends
		AbstractProductPopulator<SOURCE, TARGET>
{
	private StockService stockService;
	private WarehouseService warehouseService;


	private StockLevelStatusStrategy stockLevelStatusStrategy;

	protected WarehouseService getWarehouseService()
	{
		return warehouseService;
	}

	@Required
	public void setWarehouseService(final WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
	}

	protected StockService getStockService()
	{
		return stockService;
	}

	@Required
	public void setStockService(final StockService stockService)
	{
		this.stockService = stockService;
	}

	public StockLevelStatusStrategy getStockLevelStatusStrategy()
	{
		return stockLevelStatusStrategy;
	}

	@Required
	public void setStockLevelStatusStrategy(final StockLevelStatusStrategy stockLevelStatusStrategy)
	{
		this.stockLevelStatusStrategy = stockLevelStatusStrategy;
	}

	@Override
	public void populate(final SOURCE productModel, final TARGET productData) throws ConversionException
	{
		if (!isStockSystemEnabled())
		{
			productData.setStockLevelStatus(StockLevelStatus.INSTOCK);
			productData.setStockLevel(Integer.valueOf(0));
		}
		else if (productModel.getCode().startsWith("8"))
		{
			final Collection<StockLevelModel> stockLevels = getStockService().getStockLevels(productModel,
					Collections.singletonList(warehouseService.getWarehouseForCode("dropship")));
			productData.setStockLevel(Integer.valueOf(calculateTotalActualAmount(stockLevels)));
			productData.setStockLevelStatus(getStockLevelStatusStrategy().checkStatus(stockLevels));
		}
		else
		{
			final Collection<StockLevelModel> stockLevels = getStockService().getStockLevels(productModel,
					warehouseService.getDefWarehouse());
			productData.setStockLevel(Integer.valueOf(calculateTotalActualAmount(stockLevels)));
			productData.setStockLevelStatus(getStockLevelStatusStrategy().checkStatus(stockLevels));
		}
	}

	/**
	 * Default implementation is to check if any Warehouses have 'default' set to true.
	 * 
	 * @return true if there is a warehouse with default set to true.
	 */
	protected boolean isStockSystemEnabled()
	{
		return !CollectionUtils.isEmpty(getWarehouseService().getDefWarehouse());
	}

	protected int calculateTotalActualAmount(final Collection<StockLevelModel> stockLevels)
	{
		int totalActualAmount = 0;
		for (final StockLevelModel stockLevel : stockLevels)
		{
			final int actualAmount = stockLevel.getAvailable() - stockLevel.getReserved();
			if (((actualAmount <= 0) && (stockLevel.isTreatNegativeAsZero()))
					|| InStockStatus.FORCEOUTOFSTOCK.equals(stockLevel.getInStockStatus()))
			{
				continue;
			}
			totalActualAmount += actualAmount;
		}

		return totalActualAmount;
	}
}
