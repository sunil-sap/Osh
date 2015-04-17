/**
 * 
 */
package com.hybris.osh.facades.tax;

import de.hybris.platform.commercefacades.order.data.CartData;


public interface OshTaxFacade
{

	public double calculateTax(final CartData cart);


}
