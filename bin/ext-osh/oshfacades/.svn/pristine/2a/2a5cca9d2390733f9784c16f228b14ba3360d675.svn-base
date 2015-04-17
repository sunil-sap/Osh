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
package com.hybris.osh.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.facades.order.data.OshOrderData;
import com.hybris.osh.facades.order.data.OshOrderEntryData;
import com.hybris.osh.facades.product.data.OshProductData;


/**
 * Velocity context for a order cancellation notification email.
 */
public class OrderCancelNotificationEmailContext extends AbstractEmailContext
{
	private Converter<OrderModel, OrderData> orderConverter;
	private OshOrderData orderData;

	@Autowired
	PriceDataFactory priceDataFactory;

	@Override
	public void init(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof OrderProcessModel)
		{
			final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
			double orderSubTotal = 0.0d;
			double productDiscounts = 0.0d;
			double orderDiscount = 0.0d;
			double totalTax = 0.0d;
			double totalCancelQty = 0.0d;
			double totalQty = 0.0d;
			final List<ConsignmentEntryModel> cencelTotalQtyPrice = new ArrayList<ConsignmentEntryModel>();
			final OrderModel orderModel = ((OrderProcessModel) businessProcessModel).getOrder();
			orderData = (OshOrderData) getOrderConverter().convert(orderModel);
			/**
			 * Commented code will be useful in the enhancement part.
			 */
			/*
			 * final OrderModificationRecordModel modifiedRecord = orderModel.getModificationRecords().iterator().next();
			 * final Collection<OrderModificationRecordEntryModel> modifiedEntries =
			 * modifiedRecord.getModificationRecordEntries(); final OrderModificationRecordEntryModel modifiedEntry =
			 * modifiedEntries.iterator().next(); final OrderCancelRecordEntryModel cancelOrderEntries =
			 * (OrderCancelRecordEntryModel) modifiedEntry;
			 * orderData.setCancellationReason(cancelOrderEntries.getCancelReason().toString());
			 */
			final Format formatter = new SimpleDateFormat("MMMM dd, yyyy");
			final String formattedStartingDate = formatter.format(orderData.getCreated());
			orderData.setCreationTime(formattedStartingDate);
			/*
			 * final String formattedCancellationDate = formatter.format(orderModel.getCancelDate());
			 * orderData.setCancellationTime(formattedCancellationDate);
			 */

			final Set<ConsignmentModel> consignments = orderModel.getConsignments();
			for (final ConsignmentModel consignmentModel : consignments)
			{
				if (consignmentModel.getCode().contains("online-") || consignmentModel.getCode().contains("dropship-"))
				{
					orderData.setIsOnlinePresent("OnlinePresent");
				}
				else
				{
					orderData.setIsBopisPresent("BopisPresent");
				}

				if (orderData.getLoyaltyVoucher() != null)
				{
					orderData.setShowDiscounts(true);
				}
				if (orderData.getDeliveryAddress() != null)
				{
					if (orderData.getDeliveryAddress().getLine2() == null || orderData.getDeliveryAddress().getLine2().isEmpty())
					{
						orderData.getDeliveryAddress().setLine2("");
					}
					if (orderData.getDeliveryAddress().getPhone() == null || orderData.getDeliveryAddress().getPhone().isEmpty())
					{
						orderData.getDeliveryAddress().setPhone("");
					}
				}
				if (orderData.getPaymentInfo() != null)
				{
					if (orderData.getPaymentInfo().getBillingAddress() != null)
					{
						if (orderData.getPaymentInfo().getBillingAddress().getLine2() == null
								|| orderData.getPaymentInfo().getBillingAddress().getLine2().isEmpty())
						{
							orderData.getPaymentInfo().getBillingAddress().setLine2("");
						}
						if (orderData.getPaymentInfo().getBillingAddress().getPhone() == null
								|| orderData.getPaymentInfo().getBillingAddress().getPhone().isEmpty())
						{
							orderData.getPaymentInfo().getBillingAddress().setPhone("");
						}
					}

				}
				final Set<ConsignmentEntryModel> consignmentEntry = consignmentModel.getConsignmentEntries();
				for (final ConsignmentEntryModel consignmentEntryModel : consignmentEntry)
				{
					long qty = 0;
					final String productCode = consignmentEntryModel.getOrderEntry().getProduct().getCode();
					final String orderType = consignmentEntryModel.getOrderEntry().getOrderType();
					if (consignmentEntryModel.getCancelQty() != null)
					{
						qty = consignmentEntryModel.getCancelQty().longValue();
					}
					double orderEntryPrice = 0.0;
					final long consignmentQty = consignmentEntryModel.getQuantity().longValue();
					totalQty = totalQty + consignmentQty;
					final Collection<OrderEntryData> Orderentries = orderData.getEntries();
					for (final OrderEntryData orderEntryData : Orderentries)
					{
						double productDiscount = 0.0;
						if (orderEntryData.getProduct().getCode().equals(productCode)
								&& ((OshOrderEntryData) orderEntryData).getOrderType().equals(orderType) && qty != 0)
						{
							final OshProductData oshproduct = (OshProductData) orderEntryData.getProduct();
							orderEntryData.setQuantity(Long.valueOf(qty));
							totalCancelQty = totalCancelQty + qty;
							cencelTotalQtyPrice.add(consignmentEntryModel);
							orderEntryPrice = orderEntryData.getBasePrice().getValue().doubleValue() * qty;
							productDiscount = (((consignmentEntryModel.getOrderEntry().getBasePrice().doubleValue() * consignmentQty) - consignmentEntryModel
									.getOrderEntry().getTotalPrice().doubleValue()) / consignmentQty)
									* qty;
							productDiscounts = productDiscounts + productDiscount;
							orderEntryData.setTotalPrice(priceDataFactory.create("BUY", BigDecimal.valueOf(orderEntryPrice), "USD"));
							orderSubTotal = orderSubTotal + orderEntryPrice;
							totalTax = totalTax + consignmentEntryModel.getOrderEntry().getTaxPerUnit().doubleValue() * qty;
							totalTax = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), totalTax).doubleValue();

							oshproduct.setStatus(OshCoreConstants.CANCELLED);
						}
					}
				}
			}
			if (orderData.getOrderDiscounts() != null && !cencelTotalQtyPrice.isEmpty()
					&& orderData.getOrderDiscounts().getValue().doubleValue() != 0.0)
			{
				for (final ConsignmentEntryModel consignmentEntryModel : cencelTotalQtyPrice)
				{
					final double totalQtyPrice = consignmentEntryModel.getOrderEntry().getTotalPrice().doubleValue();
					double promotionDiscount = ((totalQtyPrice * (orderData.getOrderDiscounts().getValue().doubleValue()))
							/ (orderData.getSubTotal().getValue().doubleValue()) / consignmentEntryModel.getQuantity().doubleValue());
					promotionDiscount = promotionDiscount * consignmentEntryModel.getCancelQty().doubleValue();
					orderDiscount += promotionDiscount;
				}
				orderData.setOrderDiscounts(priceDataFactory.create("BUY", BigDecimal.valueOf(orderDiscount), "USD"));
			}
			orderData.setProductDiscounts(priceDataFactory.create("BUY", BigDecimal.valueOf(productDiscounts), "USD"));
			orderData.setSubTotal(priceDataFactory.create("BUY", BigDecimal.valueOf(orderSubTotal), "USD"));
			orderData.setTotalTax(priceDataFactory.create("BUY", BigDecimal.valueOf(totalTax), "USD"));
			if (orderModel.getStatus().getCode().equalsIgnoreCase(OshCoreConstants.CANCELLED))
			{
				orderData.setShowShipping(true);
				orderSubTotal = orderSubTotal + orderModel.getDeliveryCost().doubleValue();
			}
			final double totalDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(),
					(totalTax + orderSubTotal - (productDiscounts + orderDiscount))).doubleValue();
			orderData.setTotalPrice(priceDataFactory.create("BUY", BigDecimal.valueOf(totalDiscount), "USD"));
		}
	}

	@Override
	protected BaseSiteModel getSite(final BusinessProcessModel businessProcessModel)
	{
		if (businessProcessModel instanceof OrderProcessModel)
		{
			return ((OrderProcessModel) businessProcessModel).getOrder().getSite();
		}

		return null;
	}

	@Override
	protected CustomerModel getCustomer(final BusinessProcessModel businessProcessModel)
	{
		if (businessProcessModel instanceof OrderProcessModel)
		{
			return (CustomerModel) ((OrderProcessModel) businessProcessModel).getOrder().getUser();
		}

		return null;
	}

	protected Converter<OrderModel, OrderData> getOrderConverter()
	{
		return orderConverter;
	}

	@Required
	public void setOrderConverter(final Converter<OrderModel, OrderData> orderConverter)
	{
		this.orderConverter = orderConverter;
	}

	public OrderData getOrder()
	{
		return orderData;
	}

}
