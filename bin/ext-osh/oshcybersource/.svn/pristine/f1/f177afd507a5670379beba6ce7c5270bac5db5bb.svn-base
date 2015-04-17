/**
 * 
 */
package com.hybris.osh.payment.command.impl;

import de.hybris.platform.cybersource.adapter.Util;
import de.hybris.platform.cybersource.api.v1_49.CCAuthReversalReply;
import de.hybris.platform.cybersource.api.v1_49.CCAuthReversalService;
import de.hybris.platform.cybersource.api.v1_49.PurchaseTotals;
import de.hybris.platform.cybersource.api.v1_49.ReplyMessage;
import de.hybris.platform.cybersource.api.v1_49.RequestMessage;
import de.hybris.platform.cybersource.commands.AbstractCommand;
import de.hybris.platform.cybersource.commands.CybersourceCodeTranslator;

import java.math.BigDecimal;
import java.util.Currency;

import com.hybris.osh.command.request.FullReversalRequest;
import com.hybris.osh.command.result.FullReversalResult;
import com.hybris.osh.cybersource.adapter.OshCybersourceDTOFactory;
import com.hybris.osh.payment.command.FullReversalCommand;


/**
 * this class will set all the required filed for the full autherization reversal
 * 
 */
public class FullReversalCommandImpl extends AbstractCommand<FullReversalRequest, FullReversalResult> implements
		FullReversalCommand
{


	public FullReversalCommandImpl()
	{
	}


	@Override
	protected RequestMessage translateRequest(final FullReversalRequest fullReversalRequest,
			final de.hybris.platform.cybersource.adapter.CybersourceExecutor.ExecutionContext context)
	{
		final OshCybersourceDTOFactory dtoFactory = (OshCybersourceDTOFactory) context.getCybersourceDTOFactory();
		final RequestMessage request = dtoFactory.createRequestMessage();
		request.setMerchantReferenceCode(fullReversalRequest.getMerchantTransactionCode());
		request.setMerchantID(getMerchantID());

		final CCAuthReversalService ccAuthReversalService = dtoFactory.createCCAuthReversalService();
		final PurchaseTotals purchaseTotal = dtoFactory.createPurchaseTotals();
		ccAuthReversalService.setRun("true");
		if (fullReversalRequest.getRequestId() != null)
		{
			ccAuthReversalService.setAuthRequestID(fullReversalRequest.getRequestId());
		}
		request.setCcAuthReversalService(ccAuthReversalService);
		purchaseTotal.setGrandTotalAmount(String.valueOf(fullReversalRequest.getTotalAmount().doubleValue()));
		purchaseTotal.setCurrency(fullReversalRequest.getCurrency().getCurrencyCode());
		request.setPurchaseTotals(purchaseTotal);
		return request;
	}

	@Override
	protected FullReversalResult translateResponse(final ReplyMessage reply,
			final de.hybris.platform.cybersource.adapter.CybersourceExecutor.ExecutionContext context)
	{
		final CybersourceCodeTranslator codeTranslator = context.getCybersourceCodeTranslator();
		final FullReversalResult result = new FullReversalResult();
		final CCAuthReversalReply reversalReply = reply.getCcAuthReversalReply();
		if (reversalReply != null)
		{
			if (reversalReply.getAmount() != null)
			{
				result.setAmount(BigDecimal.valueOf(Double.parseDouble(reply.getCcAuthReversalReply().getAmount())));
			}
			if (reversalReply.getRequestDateTime() != null)
			{
				result.setRequestTime(Util.parseXmlSchemaDateTime(reversalReply.getRequestDateTime()));
			}
		}
		if (reply.getPurchaseTotals() != null)
		{
			result.setCurrency(Currency.getInstance(reply.getPurchaseTotals().getCurrency()));
		}
		result.setMerchantTransactionCode(reply.getMerchantReferenceCode());
		result.setRequestId(reply.getRequestID());
		result.setRequestToken(reply.getRequestToken());
		result.setTransactionStatus(codeTranslator.translateDecision(reply.getDecision()));
		result.setTransactionStatusDetails(codeTranslator.translateReasonCode(reply.getReasonCode()));
		return result;
	}

}
