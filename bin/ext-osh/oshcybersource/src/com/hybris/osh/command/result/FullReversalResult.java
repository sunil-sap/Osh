/**
 * 
 */
package com.hybris.osh.command.result;

import de.hybris.platform.payment.commands.result.AbstractResult;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Date;


/**
 * 
 */
public class FullReversalResult extends AbstractResult
{
	private Currency currency;
	private BigDecimal amount;
	private Date requestTime;

	/**
	 * @return the currency
	 */
	public Currency getCurrency()
	{
		return currency;
	}

	/**
	 * @param currency
	 *           the currency to set
	 */
	public void setCurrency(final Currency currency)
	{
		this.currency = currency;
	}

	/**
	 * @return the amount
	 */
	public BigDecimal getAmount()
	{
		return amount;
	}

	/**
	 * @param amount
	 *           the amount to set
	 */
	public void setAmount(final BigDecimal amount)
	{
		this.amount = amount;
	}

	/**
	 * @return the requestTime
	 */
	public Date getRequestTime()
	{
		return requestTime;
	}

	/**
	 * @param requestTime
	 *           the requestTime to set
	 */
	public void setRequestTime(final Date requestTime)
	{
		this.requestTime = requestTime;
	}


}
