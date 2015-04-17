/**
 * 
 */
package com.hybris.osh.core.discount.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.order.strategies.calculation.impl.FindPricingWithCurrentPriceFactoryStrategy;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.util.DiscountValue;

import java.util.ArrayList;
import java.util.List;

import com.hybris.osh.core.promotion.model.OshTaxPromotionModel;


public class OshFindPricingWithCurrentPriceFactoryStrategy extends FindPricingWithCurrentPriceFactoryStrategy
{

	@Override
	public List<DiscountValue> findDiscountValues(final AbstractOrderModel order) throws CalculationException
	{
		final AbstractOrderModel cartModel = order;
		final AbstractOrder orderItem = getModelService().getSource(order);
		final List<DiscountValue> discountList = new ArrayList<DiscountValue>();
		/*
		 * final AbstractOrder orderItem = getModelService().getSource(order); try { return
		 * getCurrentPriceFactory().getDiscountValues(orderItem); } catch (final JaloPriceFactoryException e) { throw new
		 * CalculationException(e); }
		 */
		if (cartModel.getAllPromotionResults() != null && !(cartModel.getAllPromotionResults().isEmpty()))
		{
			for (final PromotionResultModel promoResult : cartModel.getAllPromotionResults())
			{
				if (promoResult.getPromotion() instanceof OshTaxPromotionModel)
				{

					final double taxValue = cartModel.getTaxAmount() != null ? cartModel.getTaxAmount().doubleValue() : 0.0;
					int discountCount = cartModel.getGlobalDiscountValues().size();
					final List<DiscountValue> discountValueList = cartModel.getGlobalDiscountValues();
					for (final DiscountValue dicountValue : discountValueList)
					{
						if (dicountValue.getAppliedValue() == 0.0)
						{
							final DiscountValue discount = new DiscountValue(cartModel.getPk().toString() + "_" + ++discountCount,
									taxValue, true, taxValue, cartModel.getCurrency().getIsocode());
							discountList.add(discount);
							break;
						}
						else
						{
							discountList.add(dicountValue);
						}

					}
					/*
					 * cartModel.setGlobalDiscountValues(discountList); getModelService().save(cartModel);
					 */
				}
			}
		}

		List<DiscountValue> newDiscountList = null;
		try
		{
			newDiscountList = getCurrentPriceFactory().getDiscountValues(orderItem);
			if (newDiscountList.isEmpty() && discountList.isEmpty() && !order.getGlobalDiscountValues().isEmpty())
			{
				return order.getGlobalDiscountValues();
			}
		}
		catch (final JaloPriceFactoryException e)
		{
			// YTODO Auto-generated catch block
			e.printStackTrace();
		}
		return discountList;
	}
}
