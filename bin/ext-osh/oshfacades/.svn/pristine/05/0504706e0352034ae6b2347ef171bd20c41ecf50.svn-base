package com.hybris.osh.facades.checkout;

import de.hybris.platform.commercefacades.order.CheckoutFacade;
import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.commercefacades.order.data.CardTypeData;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.order.CartEntryModel;
import de.hybris.platform.core.model.order.CartModel;

import java.util.List;

import com.hybris.osh.facades.user.data.StateData;


public interface OshCheckoutFacade extends CheckoutFacade
{
	/**
	 * Gets delivery states of the default country
	 * 
	 * @return List<StateData>
	 */
	List<StateData> getDefaultDeliveryStates();

	/**
	 * Gets states for country model
	 * 
	 * @param countryModel
	 * @return List<StateData>
	 */
	List<StateData> getStates(final CountryModel countryModel);

	/**
	 * Gets states for country code
	 * 
	 * @param countryCode
	 * @return List<StateData>
	 */
	List<StateData> getStates(final String countryCode);

	/**
	 * this method will set gift message on the order and set order as gift order by placing true in gift attribute on
	 * abstractOrder
	 * 
	 * 
	 */
	public void setGiftOrder(final boolean gift, final String giftMessage);

	/**
	 * This method will store the store address on customer and orderModel
	 * 
	 */
	public void setStoreAddressIfAvailable();

	/**
	 * This method will store the store name on each order entry
	 * 
	 */
	public void setStoreName(final CartEntryModel cartEntryModel, final String storeName);

	/**
	 * This method will override the method createPaymentSubscriptionWithoutCart to change the implement
	 * 
	 */
	public CCPaymentInfoData createPaymentSubscriptionWithoutCart(final CCPaymentInfoData paymentInfoData);

	/**
	 * This method will cancel the authorize amount
	 * 
	 */
	public String reverseAuthorization();

	@Override
	public List<CardTypeData> getSupportedCardTypes();

	/**
	 * This method will validate the zipcode as per the postal code and state
	 * 
	 * @return boolean value
	 * @param state
	 * @param postalcode
	 */
	public boolean isValidZipcode(final String state, final String postalcode);

	/**
	 * This method will set the cashRegisterNo that is to be used for Tlog
	 * 
	 * @return boolean value
	 * @param cartModel
	 */
	public boolean setCashRegisterNo(final CartModel cart);

	public boolean checkStoreStock(final String productCode, final String storeName);

	public boolean setCustomerID(final CartModel cart);

	/**
	 * @param state
	 * @param zipcode
	 */
	public boolean isWeightForHomeDirect();
}
