/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2012 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 * 
 *  
 */
package com.hybris.osh.core.promotion.jalo;


import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotions.jalo.GeneratedProductPercentageDiscountPromotion;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryAdjustAction;
import de.hybris.platform.promotions.jalo.PromotionOrderEntryConsumed;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;
import de.hybris.platform.promotions.result.PromotionOrderEntry;
import de.hybris.platform.promotions.result.PromotionOrderView;
import de.hybris.platform.promotions.util.Helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;


/**
 * ProductPercentageDiscountPromotion.
 * 
 * Get a percentage off of the price of any of these products. For example: <i>15% off of all garden furniture</i>,
 * <i>10% off of all chart CDs</i>. Each cart product in the set of qualifying products will have its price reduced by
 * the specified percentage.
 * 
 */
public class OshProductPercentageDiscountPromotion extends GeneratedProductPercentageDiscountPromotion
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(OshProductPercentageDiscountPromotion.class);




	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext promoContext)
	{
		final List<PromotionResult> results = new ArrayList<PromotionResult>();
		final Map<AbstractOrderEntry, Double> map = new LinkedHashMap<AbstractOrderEntry, Double>();
		final List<AbstractOrderEntry> entryPro = promoContext.getOrder().getEntries();
		final List<Product> cartProducts = new ArrayList<Product>();
		AbstractOrderEntry orderEntry = null;
		for (final AbstractOrderEntry order : entryPro)
		{
			map.put(order, order.getBasePrice());
		}
		final Map<Object, Double> sortedMap = sortByComparator(map);

		for (final Map.Entry entry : sortedMap.entrySet())
		{
			orderEntry = (AbstractOrderEntry) entry.getKey();
			break;
		}

		cartProducts.add(orderEntry.getProduct());

		// Find all valid products in the cart
		final PromotionsManager.RestrictionSetResult rsr = new PromotionsManager.RestrictionSetResult(cartProducts);

		if (rsr.isAllowedToContinue() && !rsr.getAllowedProducts().isEmpty())
		{
			final PromotionOrderView view = promoContext.createView(ctx, this, rsr.getAllowedProducts());
			final PromotionsManager promotionsManager = PromotionsManager.getInstance();

			// Every product matched on this promotion ends up being a fixed price
			while (view.getTotalQuantity(ctx) > 0)
			{
				promoContext.startLoggingConsumed(this);

				// Get the next order entry
				final PromotionOrderEntry entry = view.peek(ctx);
				final long quantityToDiscount = entry.getQuantity(ctx);
				final long quantityOfOrderEntry = entry.getBaseOrderEntry().getQuantity(ctx).longValue();

				final double percentageDiscount = this.getPercentageDiscount(ctx).doubleValue() / 100D;

				// The adjustment to the order entry
				final double originalUnitPrice = entry.getBasePrice(ctx).doubleValue();
				final double originalEntryPrice = quantityToDiscount * originalUnitPrice;

				final Currency currency = promoContext.getOrder().getCurrency(ctx);

				// Calculate the new entry price and round it to a valid amount
				final BigDecimal adjustedEntryPrice = Helper.roundCurrencyValue(ctx, currency, originalEntryPrice
						- (originalEntryPrice * percentageDiscount));

				// Calculate the unit price and round it
				final BigDecimal adjustedUnitPrice = Helper.roundCurrencyValue(
						ctx,
						currency,
						adjustedEntryPrice.equals(BigDecimal.ZERO) ? BigDecimal.ZERO : adjustedEntryPrice.divide(
								BigDecimal.valueOf(quantityToDiscount), RoundingMode.HALF_EVEN));

				// Work out the fiddle amount that cannot be shared amongst the quantityToDiscount
				final BigDecimal fiddleAmount = adjustedEntryPrice.subtract(adjustedUnitPrice.multiply(BigDecimal
						.valueOf(quantityToDiscount)));

				// Is the fiddleAmount 0 (i.e. can we adjust the unit price for all items)
				if (fiddleAmount.compareTo(BigDecimal.ZERO) == 0)
				{
					for (final PromotionOrderEntryConsumed poec : view.consume(ctx, quantityToDiscount))
					{
						poec.setAdjustedUnitPrice(ctx, adjustedUnitPrice.doubleValue());
					}
				}
				else
				{
					// We have to fiddle the unit price of the last item

					// Apply normal adjusted price to all but 1 products
					for (final PromotionOrderEntryConsumed poec : view.consume(ctx, quantityToDiscount - 1))
					{
						poec.setAdjustedUnitPrice(ctx, adjustedUnitPrice.doubleValue());
					}

					// Adjust the last product with the fiddleAmount
					for (final PromotionOrderEntryConsumed poec : view.consume(ctx, 1))
					{
						poec.setAdjustedUnitPrice(ctx, Helper.roundCurrencyValue(ctx, currency, adjustedUnitPrice.add(fiddleAmount))
								.doubleValue());
					}
				}

				final PromotionResult result = promotionsManager.createPromotionResult(ctx, this, promoContext.getOrder(), 1.0F);
				result.setConsumedEntries(ctx, promoContext.finishLoggingAndGetConsumed(this, true));
				final BigDecimal adjustment = Helper.roundCurrencyValue(ctx, currency,
						adjustedEntryPrice.subtract(BigDecimal.valueOf(originalEntryPrice)));
				final PromotionOrderEntryAdjustAction poeac = promotionsManager.createPromotionOrderEntryAdjustAction(ctx,
						entry.getBaseOrderEntry(), quantityOfOrderEntry, adjustment.doubleValue());
				result.addAction(ctx, poeac);

				results.add(result);
			}

			// There is no partially fired state for this promotion, it either has products to discount or it doesn't.
			return results;
		}

		return results;
	}

	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult promotionResult, final Locale locale)
	{
		final AbstractOrder order = promotionResult.getOrder(ctx);
		if (order != null)
		{
			final Currency orderCurrency = order.getCurrency(ctx);

			if (promotionResult.getFired(ctx))
			{
				final Double totalDiscount = Double.valueOf(promotionResult.getTotalDiscount(ctx));
				final Double percentageDiscount = this.getPercentageDiscount(ctx);

				// "{0} percentage discount - You have saved {2}"
				final Object[] args =
				{ percentageDiscount, totalDiscount,
						Helper.formatCurrencyAmount(ctx, locale, orderCurrency, totalDiscount.doubleValue()) };
				return formatMessage(this.getMessageFired(ctx), args, locale);
			}
			// This promotion does not have a could fire state. There are either qualifying items in the cart or not, so there is no abiguity.
		}

		return "";
	}

	@Override
	protected void buildDataUniqueKey(final SessionContext ctx, final StringBuilder builder)
	{
		super.buildDataUniqueKey(ctx, builder);
		builder.append(getPercentageDiscount(ctx)).append('|');
	}

	private static Map sortByComparator(final Map unsortMap)
	{
		final List list = new LinkedList(unsortMap.entrySet());
		// sort list based on comparator
		Collections.sort(list, new Comparator()
		{
			@Override
			public int compare(final Object obj1, final Object obj2)
			{
				final Double val1 = (Double) ((Map.Entry) obj1).getValue();
				final Double val2 = (Double) ((Map.Entry) obj2).getValue();
				if (val1.doubleValue() > val2.doubleValue())
				{
					return -1;
				}
				else
				{
					return 1;
				}

			}
		});
		// put sorted list into map again
		final Map sortedMap = new LinkedHashMap();
		for (final Iterator it = list.iterator(); it.hasNext();)
		{
			final Map.Entry entry = (Map.Entry) it.next();

			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

}
