package com.hybris.osh.command.request;

import de.hybris.platform.payment.commands.request.AbstractRequest;

import java.math.BigDecimal;
import java.util.Currency;


/**
 * This class is the request for full authorization reversal
 */

public class FullReversalRequest extends AbstractRequest
{
	private final BigDecimal totalAmount;
	private final Currency currency;
	private final String requestId;
	private final String paymentProvider;

	/**
	 * 
	 * @param merchantTransactionCode
	 * @param requestId
	 * @param totalAmount
	 * @param currency
	 * @param paymentProvider
	 */
	public FullReversalRequest(final String merchantTransactionCode, final String requestId, final BigDecimal totalAmount,
			final Currency currency, final String paymentProvider)
	{
		super(merchantTransactionCode);
		this.requestId = requestId;
		this.totalAmount = totalAmount;
		this.currency = currency;
		this.paymentProvider = paymentProvider;
		// YTODO Auto-generated constructor stub
	}

	/**
	 * @return the totalAmount
	 */
	public BigDecimal getTotalAmount()
	{
		return totalAmount;
	}

	/**
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return currency;
	}

	/**
	 * @return the requestId
	 */
	public String getRequestId()
	{
		return requestId;
	}

	/**
	 * @return the paymentProvider
	 */
	public String getPaymentProvider()
	{
		return paymentProvider;
	}
}
