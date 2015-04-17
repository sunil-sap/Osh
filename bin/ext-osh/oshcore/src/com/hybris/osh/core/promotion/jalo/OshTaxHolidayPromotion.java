package com.hybris.osh.core.promotion.jalo;



import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;
import de.hybris.platform.promotions.result.PromotionEvaluationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.log4j.Logger;


public class OshTaxHolidayPromotion extends GeneratedOshTaxHolidayPromotion
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(OshTaxHolidayPromotion.class.getName());

	@Override
	protected Item createItem(final SessionContext ctx, final ComposedType type, final ItemAttributeMap allAttributes)
			throws JaloBusinessException
	{
		// business code placed here will be executed before the item is created
		// then create the item
		final Item item = super.createItem(ctx, type, allAttributes);
		// business code placed here will be executed after the item was created
		// and return the item
		return item;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.promotions.jalo.AbstractPromotion#evaluate(de.hybris.platform.jalo.SessionContext,
	 * de.hybris.platform.promotions.result.PromotionEvaluationContext)
	 */
	@Override
	public List<PromotionResult> evaluate(final SessionContext ctx, final PromotionEvaluationContext promoContext)
	{
		final List promotionResults = new ArrayList();
		if (checkRestrictions(ctx, promoContext))
		{
			// Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), promoContext.getOrder(), THRESHOLDTOTALS);

			final Double threshold = 0.0;
			//Double discountPriceValue = getPriceForOrder(ctx, getDiscountPrices(ctx), promoContext.getOrder(), DISCOUNTPRICES);
			final Double discountPriceValue = promoContext.getOrder().getTotalTax();
			final AbstractOrder order = promoContext.getOrder();
			final double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);

			if (orderSubtotalAfterDiscounts >= threshold.doubleValue())
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug((new StringBuilder("(")).append(getPK()).append(") evaluate: Subtotal ")
							.append(orderSubtotalAfterDiscounts).append(">").append(threshold)
							.append(".  Creating a discount action for value:").append(discountPriceValue).append(".").toString());
				}
				final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
						promoContext.getOrder(), 1.0F);
				result.addAction(ctx,
						PromotionsManager.getInstance().createPromotionOrderAdjustTotalAction(ctx, -discountPriceValue.doubleValue()));
				promotionResults.add(result);
			}
			else
			{
				if (LOG.isDebugEnabled())
				{
					LOG.debug((new StringBuilder("(")).append(getPK()).append(") evaluate: Subtotal ")
							.append(orderSubtotalAfterDiscounts).append("<").append(threshold).append(".  Skipping discount action.")
							.toString());
				}
				final float certainty = (float) (orderSubtotalAfterDiscounts / threshold.doubleValue());
				final PromotionResult result = PromotionsManager.getInstance().createPromotionResult(ctx, this,
						promoContext.getOrder(), certainty);
				promotionResults.add(result);
			}
		}
		return promotionResults;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.promotions.jalo.AbstractPromotion#getResultDescription(de.hybris.platform.jalo.SessionContext,
	 * de.hybris.platform.promotions.jalo.PromotionResult, java.util.Locale)
	 */
	@Override
	public String getResultDescription(final SessionContext ctx, final PromotionResult result, final Locale locale)
	{
		final AbstractOrder order = result.getOrder(ctx);
		if (order != null)
		{
			//final Currency orderCurrency = order.getCurrency(ctx);

			/*
			 * final Double threshold = this.getPriceForOrder(ctx, this.getThresholdTotals(ctx), order,
			 * OrderThresholdDiscountPromotion.THRESHOLDTOTALS);
			 */
			//if (threshold != null)
			/*
			 * { // Discount price for product final Double discountPriceValue = this.getPriceForOrder(ctx,
			 * this.getDiscountPrices(ctx), order, OrderThresholdDiscountPromotion.DISCOUNTPRICES); if (discountPriceValue
			 * != null) { if (result.getFired(ctx)) { // "You saved {3} for spending over {1}" final Object[] args = {
			 * threshold, Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()),
			 * discountPriceValue, Helper.formatCurrencyAmount(ctx, locale, orderCurrency,
			 * discountPriceValue.doubleValue()) }; return formatMessage(this.getMessageFired(ctx), args, locale); } else
			 * if (result.getCouldFire(ctx)) { final double orderSubtotalAfterDiscounts =
			 * getOrderSubtotalAfterDiscounts(ctx, order); final double amountRequired = threshold.doubleValue() -
			 * orderSubtotalAfterDiscounts;
			 * 
			 * // "Spend {1} to get a discount of {3} - Spend another {5} to qualify" final Object[] args = { threshold,
			 * Helper.formatCurrencyAmount(ctx, locale, orderCurrency, threshold.doubleValue()), discountPriceValue,
			 * Helper.formatCurrencyAmount(ctx, locale, orderCurrency, discountPriceValue.doubleValue()),
			 * Double.valueOf(amountRequired), Helper.formatCurrencyAmount(ctx, locale, orderCurrency, amountRequired) };
			 * return formatMessage(this.getMessageCouldHaveFired(ctx), args, locale); } } }
			 */
			return this.getMessageFired();
		}

		return "";
	}



}
