package com.hybris.osh.core.price.facade.impl;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;

import java.util.List;

import com.hybris.osh.core.price.facade.OshPriceFacade;
import com.hybris.osh.core.price.services.OshPriceService;


/**
 * Implementation class for OshPriceFacade
 * 
 */
public class DefaultOshPriceFacade implements OshPriceFacade
{
	OshPriceService oshPriceService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.price.facade.OshPriceFacade#getPriceInfo(de.hybris.platform.core.model.product.ProductModel)
	 */
	@Override
	public List<PriceRow> getPriceInfo(final ProductModel product)
	{

		return getOshPriceService().getPriceInfo(product);
	}

	/**
	 * @return the oshPriceService
	 */
	public OshPriceService getOshPriceService()
	{
		return oshPriceService;
	}

	/**
	 * @param oshPriceService
	 *           the oshPriceService to set
	 */
	public void setOshPriceService(final OshPriceService oshPriceService)
	{
		this.oshPriceService = oshPriceService;
	}

}
