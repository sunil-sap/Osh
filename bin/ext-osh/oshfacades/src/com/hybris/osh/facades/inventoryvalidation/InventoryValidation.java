/**
 * 
 */
package com.hybris.osh.facades.inventoryvalidation;

import de.hybris.platform.basecommerce.enums.StockLevelStatus;
import de.hybris.platform.commercefacades.storelocator.data.PointOfServiceData;
import de.hybris.platform.core.model.product.ProductModel;

import javax.servlet.http.HttpServletRequest;


/**
 * 
 */
public interface InventoryValidation
{

	StockLevelStatus storeStock(String storeName, ProductModel productModel);

	PointOfServiceData getMyStore();

	String getUserLocation(final HttpServletRequest request);
}
