/**
 * 
 */
package com.hybris.osh.core.stock.impl;

import de.hybris.platform.basecommerce.enums.InStockStatus;
import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commerceservices.stock.CommerceStockLevelStatusStrategy;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;

import javax.annotation.Resource;


public class OshCommerceStockLevelStatusStrategy extends CommerceStockLevelStatusStrategy
{
	@Resource(name = "configurationService")
	private ConfigurationService configurationService;

	@Override
	public StockLevelStatus checkStatus(final StockLevelModel stockLevel)
	{
		StockLevelStatus resultStatus = StockLevelStatus.OUTOFSTOCK;
		if (stockLevel == null)
		{
			resultStatus = StockLevelStatus.OUTOFSTOCK;
		}
		else if (InStockStatus.FORCEINSTOCK == stockLevel.getInStockStatus())
		{
			resultStatus = StockLevelStatus.INSTOCK;
		}
		else if (stockLevel.getReserved() == 0)
		{
			final int result = stockLevel.getAvailable() - stockLevel.getReserved() + stockLevel.getOverSelling();
			if (result <= 0)
			{
				resultStatus = StockLevelStatus.OUTOFSTOCK;
			}
			else if (result > getLowStockThresholdForStockLevel(stockLevel))
			{
				resultStatus = StockLevelStatus.INSTOCK;
			}
			else
			{
				resultStatus = StockLevelStatus.LOWSTOCK;
			}
		}
		else
		{
			if (stockLevel.getAvailable() <= 0)
			{
				resultStatus = StockLevelStatus.OUTOFSTOCK;
			}
			else if (stockLevel.getAvailable() > 0 && stockLevel.getAvailable() <= stockLevel.getReserved())
			{
				resultStatus = StockLevelStatus.LOWSTOCK;
			}
			else
			{
				resultStatus = StockLevelStatus.INSTOCK;
			}
		}

		return resultStatus;
	}






}
