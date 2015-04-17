/**
 *
 */
package com.hybris.osh.payment.service;

import de.hybris.platform.payment.AdapterException;
import de.hybris.platform.payment.commands.request.StandaloneRefundRequest;
import de.hybris.platform.payment.commands.result.RefundResult;
import de.hybris.platform.payment.methods.CardPaymentService;

import com.hybris.osh.command.request.FullReversalRequest;
import com.hybris.osh.command.result.FullReversalResult;


public interface OshCardPaymentService extends CardPaymentService
{

	public FullReversalResult FullReversal(final FullReversalRequest request);


	public abstract RefundResult oshRefundStandalone(StandaloneRefundRequest paramStandaloneRefundRequest,
			final String paymentProvider) throws AdapterException;



}
