/**
 * 
 */
package com.hybris.osh.core.service;

import de.hybris.platform.commerceservices.order.CommerceCheckoutService;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;


/**
 * 
 */
public interface OshCommerceCheckoutService extends CommerceCheckoutService
{

	void setStoreAddress(final CartModel cartModel);

	void setGiftOrder(final CartModel cartModel, final boolean gift, final String giftMessage);

	void setStoreName(final CartEntryModel cartEntryModel, final String storeName);

	@Override
	String getPaymentProvider();

	String[] collectBCCAddressForEmail();

}
