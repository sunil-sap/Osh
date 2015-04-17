/**
 * 
 */
package com.hybris.osh.core.service.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.osh.core.promotion.model.OshStatePromotionModel;
import com.hybris.osh.core.promotion.model.OshTaxPromotionModel;
import com.hybris.osh.core.service.DiscountPriceService;


public class DefaultDiscountPriceService implements DiscountPriceService
{
	@Autowired
	private ModelService modelService;


	@Override
	public void setOrderDiscountPrice(final AbstractOrderModel source)
	{
		double discounts = 0.0d;
		final List<DiscountValue> discountList = source.getGlobalDiscountValues(); // discounts on the cart itself
		if (discountList != null && !discountList.isEmpty())
		{
			for (final DiscountValue discount : discountList)
			{
				final double value = discount.getAppliedValue();
				if (value > 0.0d)
				{
					discounts += value;
				}
			}
		}

		final double discountToRemove = removeDiscount(source);
		double productDiscount = discounts;
		productDiscount = productDiscount - discountToRemove;

		//discounts = discounts - discountToRemove;
		setOrderLevelDiscountPrice(source, discounts, false);
		setOrderLevelDiscountPrice(source, productDiscount, true);

	}

	protected void setOrderLevelDiscountPrice(final AbstractOrderModel source, final double discount,
			final boolean isProductDiscountPrice)
	{
		final double totalOrderPercent = discount / source.getSubtotal().doubleValue();
		//totalOrderPercent = Math.round(totalOrderPercent * 10000) / 10000D;
		final List<AbstractOrderEntryModel> entries = source.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				double discPrice = (entry.getBasePrice().doubleValue()) - (entry.getBasePrice().doubleValue() * totalOrderPercent);
				discPrice = Math.round(discPrice * 10000) / 10000D;
				if (isProductDiscountPrice)
				{

					entry.setProductDiscountPrice(Double.valueOf(discPrice));
				}

				else
				{
					entry.setDiscountPrice(Double.valueOf(discPrice));
				}
				modelService.save(entry);
			}
		}
	}


	/**
	 * @param source
	 */
	@Override
	public void setProductDiscountPrice(final AbstractOrderModel source)
	{
		final List<AbstractOrderEntryModel> entries = source.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				final List<DiscountValue> discountValues = entry.getDiscountValues();
				if (discountValues != null)
				{
					double discPrice = entry.getTotalPrice().doubleValue() / entry.getQuantity().doubleValue();
					discPrice = Math.round(discPrice * 10000) / 10000D;
					entry.setDiscountPrice(Double.valueOf(discPrice));
					entry.setProductDiscountPrice(Double.valueOf(discPrice));
					modelService.save(entry);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.service.DiscountPriceService#setDiscountPriceWithoutPromotion(de.hybris.platform.core.model
	 * .order.AbstractOrderModel)
	 */
	@Override
	public void setDiscountPriceWithoutPromotion(final AbstractOrderModel source)
	{
		final List<AbstractOrderEntryModel> entries = source.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				entry.setDiscountPrice(entry.getBasePrice());
				entry.setProductDiscountPrice(entry.getBasePrice());
				modelService.save(entry);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.core.service.DiscountPriceService#setOrderAndProductDiscountPrice(de.hybris.platform.core.model
	 * .order.AbstractOrderModel)
	 */
	@Override
	public void setOrderAndProductDiscountPrice(final AbstractOrderModel source)
	{

		double discounts = 0.0d;
		final List<DiscountValue> discountList = source.getGlobalDiscountValues(); // discounts on the cart itself
		if (discountList != null && !discountList.isEmpty())
		{
			for (final DiscountValue discount : discountList)
			{
				final double value = discount.getAppliedValue();
				if (value > 0.0d)
				{
					discounts += value;
				}
			}
		}

		final double discountToRemove = removeDiscount(source);
		double productDiscount = discounts;
		productDiscount = productDiscount - discountToRemove;

		//discounts = discounts - discountToRemove;

		setOrderAndProductLevelDiscPrice(source, discounts, false);
		setOrderAndProductLevelDiscPrice(source, productDiscount, true);
	}

	protected void setOrderAndProductLevelDiscPrice(final AbstractOrderModel source, final double discount,
			final boolean isProductDiscountPrice)
	{
		final double totalOrderPercent = discount / source.getSubtotal().doubleValue();
		//totalOrderPercent = Math.round(totalOrderPercent * 10000) / 10000D;
		final List<AbstractOrderEntryModel> entries = source.getEntries();
		if (entries != null)
		{
			for (final AbstractOrderEntryModel entry : entries)
			{
				double productdisc = entry.getTotalPrice().doubleValue() / entry.getQuantity().doubleValue();
				productdisc = Math.round(productdisc * 10000) / 10000D;
				final double discPrice = productdisc - (productdisc * totalOrderPercent);
				final double discountPrice = (discPrice * 10000) / 10000D;
				if (isProductDiscountPrice)
				{

					entry.setProductDiscountPrice(Double.valueOf(discountPrice));
				}

				else
				{
					entry.setDiscountPrice(Double.valueOf(discountPrice));
				}
				modelService.save(entry);
			}
		}
	}

	// Checking if StatePromotion is already applied or not
	protected boolean isStatePromotion(final AbstractOrderModel source)
	{
		boolean isState = false;
		final Set<PromotionResultModel> promotions = source.getAllPromotionResults();
		for (final PromotionResultModel promotionResultModel : promotions)
		{
			if (promotionResultModel.getPromotion() instanceof OshStatePromotionModel)
			{
				isState = true;
			}
		}
		return isState;
	}

	//Checking if NoSalesTax(NST) promotion is already applied or not
	protected boolean isTaxPromotion(final AbstractOrderModel source)
	{
		boolean isTaxPromo = false;
		final Set<PromotionResultModel> promotions = source.getAllPromotionResults();
		for (final PromotionResultModel promotionResultModel : promotions)
		{
			if (promotionResultModel.getPromotion() instanceof OshTaxPromotionModel)
			{
				isTaxPromo = true;
			}
		}
		return isTaxPromo;
	}

	@SuppressWarnings("boxing")
	protected double removeDiscount(final AbstractOrderModel source)
	{
		double deliveryCost = 0.0;
		double tax = 0.0;
		double discountToRemove = 0.0;

		if (isStatePromotion(source))
		{
			deliveryCost = source.getDeliveryCost();
		}

		if (isTaxPromotion(source))
		{
			tax = source.getTotalTax();
		}

		if (isStateVoucher(source))
		{
			deliveryCost = source.getDeliveryCost();
		}
		discountToRemove = deliveryCost + tax;
		return discountToRemove;
	}

	//Checking if voucher for free shipping is already applied or not
	@SuppressWarnings("boxing")
	protected boolean isStateVoucher(final AbstractOrderModel source)
	{
		boolean stateVoucher = false;
		final List<DiscountModel> disc = source.getDiscounts();
		if (disc != null && !(disc.isEmpty()))
		{
			for (final DiscountModel discountModel : disc)
			{
				if (discountModel instanceof PromotionVoucherModel && ((PromotionVoucherModel) discountModel).getFreeShipping())
				{
					stateVoucher = true;
				}
			}
		}
		return stateVoucher;

	}
}
