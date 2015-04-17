package com.hybris.osh.core.price.facade;

import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.europe1.jalo.PriceRow;

import java.util.List;


/**
 * Interface for Osh price
 * 
 */
public interface OshPriceFacade
{
	/**
	 * Method to get all the price rows available on the product
	 * 
	 * @param product
	 * @return priceRows
	 */
	List<PriceRow> getPriceInfo(ProductModel product);
}
