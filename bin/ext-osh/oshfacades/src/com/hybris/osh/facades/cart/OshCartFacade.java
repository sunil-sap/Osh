/**
 * 
 */
package com.hybris.osh.facades.cart;

import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartModificationData;
import de.hybris.platform.commerceservices.order.CommerceCartModificationException;

import javax.servlet.http.HttpServletRequest;


/**
 * Extends the Cart Facade for the
 */
public interface OshCartFacade extends CartFacade
{

	/**
	 * Appends users currentCart with previous cart if available
	 */
	void appendCartForCurrentUser(HttpServletRequest request);

	/**
	 * This method is used to add the cart entry for different Product and OrderType
	 * 
	 * @param code
	 * @param quantity
	 * @param orderType
	 * @return CartModificationData
	 */
	CartModificationData addToCartOrder(String code, long quantity, String orderType) throws CommerceCartModificationException;

	/**
	 * This method is used to merge the cart entry for the split cart entry for a single product
	 * 
	 * @param entryNumber
	 * @param quantity
	 * @param orderType
	 * @return CartModificationData
	 */
	CartModificationData mergeCartEntry(long entryNumber, long quantity, String orderType)
			throws CommerceCartModificationException;

	/**
	 * @param qty
	 * @param code
	 * @param stockValue
	 * @return boolean
	 */
	boolean isQuantityAvailableOnAdd(long qty, String code, long stockValue);


}
