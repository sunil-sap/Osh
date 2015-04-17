package com.hybris.osh.facades.payment.impl;

import de.hybris.platform.commercefacades.order.data.CCPaymentInfoData;
import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;

import com.hybris.osh.facades.payment.OshPaymentFacade;


/**
 * implementation class to update the payment Details
 * 
 */
public class DefaultOshPaymentFacade implements OshPaymentFacade
{
	private EnumerationService enumerationService;



	@Override
	public BillingInfo createBillingInfo(final AddressModel address)
	{
		if (address == null)
		{
			return null;
		}

		final BillingInfo billingInfo = new BillingInfo();

		billingInfo.setCity(address.getTown());
		if (address.getCountry() != null)
		{
			billingInfo.setCountry(address.getCountry().getIsocode());
		}
		billingInfo.setEmail(address.getEmail());
		billingInfo.setFirstName(address.getFirstname());
		billingInfo.setLastName(address.getLastname());
		billingInfo.setPhoneNumber(address.getPhone1());
		billingInfo.setPostalCode(address.getPostalcode());
		if (address.getRegion() != null)
		{
			billingInfo.setState(address.getRegion().getName());
		}
		billingInfo.setStreet1(address.getStreetname());
		billingInfo.setStreet2(address.getStreetnumber());

		return billingInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.osh.facades.payment.paymentfacades.OshPaymentFacade#createCCDetails(de.hybris.platform.commercefacades
	 * .order.data.CCPaymentInfoData)
	 */
	@Override
	public CardInfo createCCDetails(final CCPaymentInfoData paymentInfo)
	{
		final CardInfo cardInfo = new CardInfo();

		cardInfo.setCardHolderFullName(paymentInfo.getAccountHolderName());
		cardInfo.setCardNumber(paymentInfo.getCardNumber());
		final CreditCardType cardType = (CreditCardType) getEnumerationService().getEnumerationValue(
				CreditCardType.class.getSimpleName(), paymentInfo.getCardType());
		cardInfo.setCardType(cardType);
		cardInfo.setExpirationMonth(Integer.valueOf(paymentInfo.getExpiryMonth()));
		cardInfo.setExpirationYear(Integer.valueOf(paymentInfo.getExpiryYear()));
		cardInfo.setIssueNumber(paymentInfo.getIssueNumber());

		return cardInfo;
	}

	/**
	 * @return the enumerationService
	 */
	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	/**
	 * @param enumerationService
	 *           the enumerationService to set
	 */
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}
}
