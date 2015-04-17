/**
 * 
 */
package com.hybris.osh.facades.converters.populator;

import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.product.data.PriceData;
import de.hybris.platform.commercefacades.product.data.PriceData.PriceType;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.commerceservices.converter.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.voucher.model.PromotionVoucherModel;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.hybris.osh.core.promotion.model.OshPromotionVoucherModel;
import com.hybris.osh.facades.order.data.OshOrderData;


/**
 * 
 */
public class OshOrderDataPopulator implements Populator<AbstractOrderModel, OrderData>
{
	@Autowired
	private PriceDataFactory priceDataFactory;

	private Converter<AddressModel, AddressData> addressConverter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.commerceservices.converter.Populator#populate(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void populate(final AbstractOrderModel source, final OrderData target) throws ConversionException
	{
		if (target instanceof OshOrderData)
		{
			((OshOrderData) target).setOrderType(source.getOrderType());
			((OshOrderData) target).setGift(source.isGift());
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
			((OshOrderData) target).setTotalAmount(totalAmt);
			if (source.getStoreAddress() != null)
			{
				((OshOrderData) target).setStoreAddress(getAddressConverter().convert(source.getStoreAddress()));
			}
			((OshOrderData) target).setGiftMesaage(source.getGiftMessage());
			((OshOrderData) target).setLoyaltyVoucher(source.getLoyaltyVoucher());
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
				((OshOrderData) target).setVoucherCode(voucherCode);

			}
		}

	}

	/**
	 * @return the addressConverter
	 */
	public Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	/**
	 * @param addressConverter
	 *           the addressConverter to set
	 */
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}
}
