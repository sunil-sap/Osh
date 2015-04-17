package com.hybris.osh.facades.payment;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;


/**
 * Facade to update the payment Details
 * 
 */
public interface OshPaymentFacade
{
	/**
	 * Used to create the new BillingInfo using Address model
	 * 
	 * @param address
	 * @return
	 */
	public BillingInfo createBillingInfo(final AddressModel address);

	/**
	 * Used to create the new CardInfo using using CCPaymentInfoData
	 * 
	 * @param paymentInfo
	 * @return
	 */
	public CardInfo createCCDetails(final CCPaymentInfoData paymentInfo);
}
