package com.hybris.osh.core.price.services;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;
import de.hybris.platform.jalo.order.price.PriceInformation;

import java.util.List;


/**
 * Service for Getting oshPrice
 * 
 */
public interface OshPriceService
{
	List<PriceRow> getPriceInfo(ProductModel product);

	PriceInformation getPriceInfoForSolrFacets(ProductModel productModel);
}
