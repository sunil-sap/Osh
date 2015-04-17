/**
 * 
 */
package com.hybris.osh.payment.command.impl;

import de.hybris.platform.cybersource.adapter.CybersourceDTOFactory;
import de.hybris.platform.cybersource.adapter.CybersourceExecutor;
import de.hybris.platform.cybersource.api.v1_49.BillTo;
import de.hybris.platform.cybersource.api.v1_49.PaySubscriptionUpdateService;
import de.hybris.platform.cybersource.api.v1_49.RecurringSubscriptionInfo;
import de.hybris.platform.cybersource.api.v1_49.ReplyMessage;
import de.hybris.platform.cybersource.api.v1_49.RequestMessage;
import de.hybris.platform.cybersource.commands.AbstractCommand;
import de.hybris.platform.cybersource.commands.CybersourceCodeTranslator;
import de.hybris.platform.payment.commands.UpdateSubscriptionCommand;
import de.hybris.platform.payment.commands.request.UpdateSubscriptionRequest;
import de.hybris.platform.payment.commands.result.SubscriptionResult;
import de.hybris.platform.payment.dto.BillingInfo;

import com.google.common.base.Preconditions;


/**
 * 
 */
public class UpdateSubscriptionCommandImpl extends AbstractCommand<UpdateSubscriptionRequest, SubscriptionResult> implements
		UpdateSubscriptionCommand
{

	@Override
	protected RequestMessage translateRequest(final UpdateSubscriptionRequest yRequest,
			final CybersourceExecutor.ExecutionContext context)
	{
		final CybersourceDTOFactory dtoFactory = context.getCybersourceDTOFactory();
		final RequestMessage request = dtoFactory.createRequestMessage();

		request.setMerchantReferenceCode(yRequest.getMerchantTransactionCode());
		request.setMerchantID(getMerchantID());

		final PaySubscriptionUpdateService paySubscriptionUpdateService = dtoFactory.createPaySubscriptionUpdateService();
		paySubscriptionUpdateService.setRun("true");
		request.setPaySubscriptionUpdateService(paySubscriptionUpdateService);

		final RecurringSubscriptionInfo subscription = dtoFactory.createRecurringSubscriptionInfo();
		subscription.setSubscriptionID(yRequest.getSubscriptionID());
		request.setRecurringSubscriptionInfo(subscription);

		if (yRequest.getBillingInfo() != null)
		{
			final BillTo billTo = dtoFactory.createBillTo();
			request.setBillTo(billTo);
			final BillingInfo billInfo = yRequest.getCard().getBillingInfo();
			Preconditions.checkArgument(billInfo != null);

			billTo.setFirstName(billInfo.getFirstName());
			billTo.setLastName(billInfo.getLastName());
			billTo.setStreet1(billInfo.getStreet1());
			billTo.setStreet2(billInfo.getStreet2());
			billTo.setCity(billInfo.getCity());
			billTo.setState(billInfo.getState());
			billTo.setPostalCode(billInfo.getPostalCode());
			billTo.setCountry(billInfo.getCountry());
			billTo.setPhoneNumber(billInfo.getPhoneNumber());
			billTo.setEmail(billInfo.getEmail());
			billTo.setIpAddress(billInfo.getIpAddress());
		}

		/*
		 * if (yRequest.getCard() != null) { final Card card = dtoFactory.createCard(); request.setCard(card); final
		 * CardInfo cardInfo = yRequest.getCard(); Preconditions.checkArgument(cardInfo != null);
		 * 
		 * card.setCardType(context.getCardRegistry().getCardTypeByCode(cardInfo.getCardType()).getCybersourceCardCode());
		 * card.setAccountNumber(cardInfo.getCardNumber());
		 * card.setExpirationMonth(BigInteger.valueOf(cardInfo.getExpirationMonth().longValue()));
		 * card.setExpirationYear(BigInteger.valueOf(cardInfo.getExpirationYear().longValue()));
		 * card.setCvNumber(cardInfo.getCv2Number()); }
		 */
		return request;
	}

	@Override
	protected SubscriptionResult translateResponse(final ReplyMessage reply, final CybersourceExecutor.ExecutionContext context)
	{
		final CybersourceCodeTranslator codeTranslator = context.getCybersourceCodeTranslator();
		final SubscriptionResult result = new SubscriptionResult();

		if (reply.getPaySubscriptionCreateReply() != null)
		{
			result.setSubscriptionID(reply.getPaySubscriptionUpdateReply().getSubscriptionID());
			result.setTransactionStatusDetails(codeTranslator.translateReasonCode(reply.getPaySubscriptionUpdateReply()
					.getReasonCode()));
		}
		result.setMerchantTransactionCode(reply.getMerchantReferenceCode());
		result.setRequestId(reply.getRequestID());
		result.setRequestToken(reply.getRequestToken());
		result.setTransactionStatus(codeTranslator.translateDecision(reply.getDecision()));
		result.setTransactionStatusDetails(codeTranslator.translateReasonCode(reply.getReasonCode()));

		return result;
	}
}
