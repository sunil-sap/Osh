/**
 * 
 */
package com.hybris.osh.facades.converters.populator;


import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceData.PriceType;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.osh.core.promotion.model.OshPromotionVoucherModel;
import com.hybris.osh.core.service.DiscountPriceService;
import com.hybris.osh.facades.order.data.OshCartData;


/**
 *
 */
public class OshCartDataPopulator implements Populator<AbstractOrderModel, CartData>
{
	@Autowired
	private PriceDataFactory priceDataFactory;
	@Autowired
	private DiscountPriceService discountPriceService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final AbstractOrderModel source, final CartData target) throws ConversionException
	{
		if (target instanceof OshCartData)
		{
			((OshCartData) target).setOrderType(source.getOrderType());
			double totalAmount = 0.0;
			final double totalPrice = source.getTotalPrice().doubleValue() + source.getTotalTax().doubleValue();
			if (source.getTotalPrice().doubleValue() == totalPrice)
			{
				totalAmount = source.getTotalPrice().doubleValue();
			}
			else
			{
				totalAmount = totalPrice;
			}
			final PriceData totalAmt = priceDataFactory.create(PriceType.BUY, BigDecimal.valueOf(totalAmount), source.getCurrency()
					.getIsocode());
			((OshCartData) target).setTotalAmount(totalAmt);

			if ((source.getDiscounts()) != null && !(source.getDiscounts()).isEmpty())
			{
				String voucherCode = null;
				final DiscountModel voucherModel = source.getDiscounts().get(0);
				if (voucherModel instanceof PromotionVoucherModel)
				{
					voucherCode = ((PromotionVoucherModel) voucherModel).getVoucherCode();
				}
				else if (voucherModel instanceof OshPromotionVoucherModel)
				{
					voucherCode = ((OshPromotionVoucherModel) voucherModel).getVoucherCode();
				}
				((OshCartData) target).setVoucherCode(voucherCode);

			}

			if (target.getOrderDiscounts().getValue().doubleValue() != 0.0
					&& target.getProductDiscounts().getValue().doubleValue() != 0.0)
			{
				discountPriceService.setOrderAndProductDiscountPrice(source);
			}

			else if (target.getOrderDiscounts().getValue().doubleValue() != 0.0)
			{
				discountPriceService.setOrderDiscountPrice(source);
			}

			else if (target.getProductDiscounts().getValue().doubleValue() != 0.0)
			{
				discountPriceService.setProductDiscountPrice(source);
			}

			else
			{
				discountPriceService.setDiscountPriceWithoutPromotion(source);
			}

			((OshCartData) target).setShippingSurchargeStatus(source.isShippingSurchargeStatus());

		}
	}
}