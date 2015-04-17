/**
 * 
 */
package com.hybris.osh.core.dao;

import de.hybris.platform.core.model.order.CartModel;

import java.util.Date;
import java.util.List;


/**
 * 
 */
public interface CartRemovalDao
{

	List<CartModel> findAbandonedCarts(Date previousYearDate);
}
