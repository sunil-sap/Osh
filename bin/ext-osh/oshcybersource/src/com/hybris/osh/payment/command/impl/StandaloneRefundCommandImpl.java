package com.hybris.osh.payment.command.impl;

import de.hybris.platform.cybersource.adapter.CybersourceDTOFactory;
import de.hybris.platform.cybersource.adapter.CybersourceExecutor.ExecutionContext;
import de.hybris.platform.cybersource.api.v1_49.BillTo;
import de.hybris.platform.cybersource.api.v1_49.CCCreditService;
import de.hybris.platform.cybersource.api.v1_49.PurchaseTotals;
import de.hybris.platform.cybersource.api.v1_49.RecurringSubscriptionInfo;
import de.hybris.platform.cybersource.api.v1_49.RequestMessage;
import de.hybris.platform.cybersource.commands.AbstractRefundCommand;
import de.hybris.platform.payment.commands.StandaloneRefundCommand;
import de.hybris.platform.payment.commands.request.StandaloneRefundRequest;
import de.hybris.platform.payment.dto.BillingInfo;

import com.google.common.base.Preconditions;


/**
 * Implementation for {@link StandaloneRefundCommand} In OshCybersource documentation it is described as
 * "Stand-Alone Credits"
 *
 * @author michal.flasinski
 *
 */
public class StandaloneRefundCommandImpl extends AbstractRefundCommand<StandaloneRefundRequest> implements
		StandaloneRefundCommand<StandaloneRefundRequest>
{

	@Override
	protected RequestMessage translateRequest(final StandaloneRefundRequest yRequest, final ExecutionContext context)
	{
		final CybersourceDTOFactory dtoFactory = context.getCybersourceDTOFactory();
		final RequestMessage request = dtoFactory.createRequestMessage();
		request.setMerchantReferenceCode(yRequest.getMerchantTransactionCode());

		if ((!yRequest.isTokenizedRequest()) || yRequest.getBillTo() != null)
		{
			final BillTo billTo = dtoFactory.createBillTo();
			final BillingInfo billingInfo = yRequest.getBillTo();
			Preconditions.checkArgument(billingInfo != null);

			billTo.setCity(billingInfo.getCity());
			billTo.setCountry(billingInfo.getCountry());
			billTo.setEmail(billingInfo.getEmail());
			billTo.setFirstName(billingInfo.getFirstName());
			billTo.setLastName(billingInfo.getLastName());
			billTo.setPostalCode(billingInfo.getPostalCode());
			billTo.setState(billingInfo.getState());
			billTo.setStreet1(billingInfo.getStreet1());
			request.setBillTo(billTo);
		}


		/*
		 * if ((!yRequest.isTokenizedRequest()) || yRequest.getCard() != null) { final Card card =
		 * dtoFactory.createCard(); final CardInfo cardInfo = yRequest.getCard(); Preconditions.checkArgument(cardInfo !=
		 * null); card.setAccountNumber(cardInfo.getCardNumber());
		 * card.setCardType(context.getCardRegistry().getCardTypeByCode(cardInfo.getCardType()).getCybersourceCardCode());
		 * card.setExpirationMonth(new BigInteger(cardInfo.getExpirationMonth().toString())); card.setExpirationYear(new
		 * BigInteger(cardInfo.getExpirationYear().toString())); request.setCard(card); }
		 */

		/*
		 * if (yRequest.isTokenizedRequest()) {
		 */
		final RecurringSubscriptionInfo subscription = dtoFactory.createRecurringSubscriptionInfo();
		subscription.setSubscriptionID(yRequest.getSubscriptionID());
		request.setRecurringSubscriptionInfo(subscription);
		/* } */

		final PurchaseTotals purchaseTotals = dtoFactory.createPurchaseTotals();
		purchaseTotals.setCurrency(yRequest.getCurrency().getCurrencyCode());
		purchaseTotals.setGrandTotalAmount(yRequest.getTotalAmount().toString());
		request.setPurchaseTotals(purchaseTotals);

		final CCCreditService creditService = dtoFactory.createCCCreditService();
		request.setCcCreditService(creditService);
		creditService.setRun("true");

		return request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.payment.commands.StandaloneRefundCommand#perform(de.hybris.platform.payment.commands.request
	 * .AbstractRequest)
	 */
	/*
	 * @Override public RefundResult perform(final StandaloneRefundRequest request) { // return null; }
	 */
}
