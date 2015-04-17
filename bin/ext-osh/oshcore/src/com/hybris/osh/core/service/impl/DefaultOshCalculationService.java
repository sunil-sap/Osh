/**
 * 
 */
package com.hybris.osh.core.service.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.impl.DefaultCalculationService;
import de.hybris.platform.order.strategies.calculation.OrderRequiresCalculationStrategy;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.util.PriceValue;
import de.hybris.platform.util.TaxValue;

import java.util.Collection;
import java.util.List;
import java.util.Map;


/**
 * 
 */
public class DefaultOshCalculationService extends DefaultCalculationService
{

	private OrderRequiresCalculationStrategy orderOshRequiresCalculationStrategy;


	public OrderRequiresCalculationStrategy getOrderOshRequiresCalculationStrategy()
	{
		return orderOshRequiresCalculationStrategy;
	}


	public void setOrderOshRequiresCalculationStrategy(final OrderRequiresCalculationStrategy orderOshRequiresCalculationStrategy)
	{
		this.orderOshRequiresCalculationStrategy = orderOshRequiresCalculationStrategy;
	}

	@Override
	protected double calculateTotalTaxValues(final AbstractOrderModel order, final boolean recalculate, final int digits,
			final double taxAdjustmentFactor, final Map taxValueMap)
	{
		validateParameterNotNull(order, "Abstract Order model can not be null");
		validateParameterNotNull(taxValueMap, "Tax Value Map can not be null");

		return order.getTotalTax().doubleValue();

	}

	@Override
	public void calculate(final AbstractOrderModel order) throws CalculationException
	{
		if (getOrderOshRequiresCalculationStrategy().requiresCalculation(order))
		{
			// -----------------------------
			// first calc all entries
			calculateEntries(order, false);
			// -----------------------------
			// reset own values
			final Map taxValueMap = resetAllValues(order);
			// -----------------------------
			// now calculate all totals
			calculateTotals(order, false, taxValueMap);
			// notify manual discouns - needed?
			//notifyDiscountsAboutCalculation();
		}
	}

	@Override
	public void calculateEntries(final AbstractOrderModel order, final boolean forceRecalculate) throws CalculationException
	{
		double subtotal = 0.0;
		for (final AbstractOrderEntryModel e : order.getEntries())
		{
			if (forceRecalculate || getOrderOshRequiresCalculationStrategy().requiresCalculation(e))
			{
				recalculate(e);
			}
			subtotal += e.getTotalPrice().doubleValue();
		}
		order.setTotalPrice(Double.valueOf(subtotal));

	}

	private void recalculate(final AbstractOrderEntryModel entry) throws CalculationException
	{
		resetAllValues(entry);
		calculateTotals(entry, true);
	}

	@Override
	protected void resetAllValues(final AbstractOrderEntryModel entry) throws CalculationException
	{
		// taxes
		final Collection<TaxValue> entryTaxes = findTaxValues(entry);
		//entry.setTaxValues(entryTaxes);
		final PriceValue pv = findBasePrice(entry);
		final AbstractOrderModel order = entry.getOrder();
		final PriceValue basePrice = convertPriceIfNecessary(pv, order.getNet().booleanValue(), order.getCurrency(), entryTaxes);
		entry.setBasePrice(Double.valueOf(basePrice.getValue()));
		final List<DiscountValue> entryDiscounts = findDiscountValues(entry);
		entry.setDiscountValues(entryDiscounts);
	}

	@Override
	public void recalculate(final AbstractOrderModel order) throws CalculationException
	{
		// -----------------------------
		// first force calc entries
		calculateEntries(order, true);
		// -----------------------------
		// reset own values
		final Map taxValueMap = resetAllValues(order);
		// -----------------------------
		// now recalculate totals
		calculateTotals(order, true, taxValueMap);
		// notify discounts -- needed?
		//			notifyDiscountsAboutCalculation();

	}




}
