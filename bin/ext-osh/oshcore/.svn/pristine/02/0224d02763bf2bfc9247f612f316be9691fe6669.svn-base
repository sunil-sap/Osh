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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;


public class OshStateHolidayPromotion extends GeneratedOshStateHolidayPromotion
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(OshStateHolidayPromotion.class.getName());

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
		//get attribute data 
		final String applicableStates = this.getFreeShippingStatesName();
		//check for null

		if (StringUtils.isNotEmpty(applicableStates) && promoContext.getOrder().getDeliveryAddress() != null
				&& promoContext.getOrder().getDeliveryAddress().getRegion() != null)
		{
			//get delivery address state code
			final String orderShippingState = promoContext.getOrder().getDeliveryAddress().getRegion().getIsocode();
			// if attribute data is not null and if delivery state is not specified in the attribute then 
			//do not apply discount
			if (StringUtils.isNotEmpty(orderShippingState))
			{

				if (applicableStates.indexOf(orderShippingState) < 0)
				{

					return promotionResults;
				}
			}
		}

		if (checkRestrictions(ctx, promoContext))
		{

			// Double threshold = getPriceForOrder(ctx, getThresholdTotals(ctx), promoContext.getOrder(), THRESHOLDTOTALS);

			final Double threshold = 0.0;
			//Double discountPriceValue = getPriceForOrder(ctx, getDiscountPrices(ctx), promoContext.getOrder(), DISCOUNTPRICES);
			final Double discountPriceValue = promoContext.getOrder().getDeliveryCost();
			final AbstractOrder order = promoContext.getOrder();
			final double orderSubtotalAfterDiscounts = getOrderSubtotalAfterDiscounts(ctx, order);

			if (orderSubtotalAfterDiscounts >= threshold.doubleValue() && StringUtils.isNotEmpty(applicableStates))
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
	public String getResultDescription(final SessionContext ctx, final PromotionResult arg1, final Locale arg2)
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.promotions.jalo.GeneratedAbstractPromotion#setRestrictions(de.hybris.platform.jalo.SessionContext
	 * , java.util.Collection)
	 */


}
