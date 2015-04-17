package com.hybris.osh.core.promotion.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Region;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.promotions.jalo.PromotionResult;
import de.hybris.platform.promotions.jalo.PromotionsManager;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;


public class StateRestriction extends GeneratedStateRestriction
{
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(StateRestriction.class.getName());

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
	 * @see de.hybris.platform.voucher.jalo.Restriction#isFulfilledInternal(de.hybris.platform.jalo.order.AbstractOrder)
	 */
	@SuppressWarnings("boxing")
	@Override
	protected boolean isFulfilledInternal(final AbstractOrder order)
	{
		// voucher for state restriction should not be applied if StatePromotion is already applied
		final Set<PromotionResult> result = PromotionsManager.getInstance().getAllPromotionResults(order);
		for (final PromotionResult promoResult : result)
		{
			if (promoResult.getPromotion().getPromotionType().equalsIgnoreCase("StatePromotion"))
			{
				return false;
			}
		}


		// The "value" attribute should have 0.0 value always
		if (this.getVoucher().getValue() != 0.0)
		{
			this.getVoucher().setValue(0.0);
		}

		// freeshipping attribute should always be "true" for state voucher
		if (!this.getVoucher().isFreeShipping())
		{
			this.getVoucher().setFreeShipping(true);
		}

		if (this.getRegion() != null && !(this.getRegion().isEmpty()) && order.getDeliveryAddress() != null
				&& order.getDeliveryAddress().getRegion() != null)
		{
			final List<Region> regions = (List<Region>) this.getRegion();
			for (final Region region : regions)
			{
				// checking if region in delivery address in order matches with region in the list of free shipping regions
				if (region.equals(order.getDeliveryAddress().getRegion()))
				{
					return true;
				}
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.voucher.jalo.Restriction#isFulfilledInternal(de.hybris.platform.jalo.product.Product)
	 */
	@Override
	protected boolean isFulfilledInternal(final Product arg0)
	{
		// YTODO Auto-generated method stub
		return false;
	}

}
