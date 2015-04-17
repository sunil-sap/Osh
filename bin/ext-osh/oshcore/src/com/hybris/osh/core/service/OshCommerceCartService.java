/**
 * 
 */
package com.hybris.osh.core.service;

import de.hybris.platform.commerceservices.order.CommerceCartModification;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;
import de.hybris.platform.commerceservices.order.CommerceCartService;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;


/**
 * This interface is used for the custom implementation for splitting the cart entry for a single product and for
 * merging the split cart entry
 */
public interface OshCommerceCartService extends CommerceCartService
{
	CommerceCartModification addToCartOrder(CartModel cartModel, ProductModel productModel, long quantity, UnitModel unit,
			boolean forceNewEntry, String orderType) throws CommerceCartModificationException;

	CommerceCartModification updateForMergeCartEntry(CartModel cartModel, long entryNumber, long quantity, String orderType)
			throws CommerceCartModificationException;


}
