package com.hybris.osh.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
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


public class ReadyForPickUpEmailContext extends AbstractEmailContext
{

	private Converter<OrderModel, OrderData> orderConverter;
	private EnumerationService enumerationService;
	private OshOrderData orderData;

	@Autowired
	PriceDataFactory priceDataFactory;

	@Override
	public void init(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof OrderProcessModel)
		{
			double orderSubTotal = 0.0d;
			double productDiscounts = 0.0d;
			double orderDiscount = 0.0d;
			double totalTax = 0.0d;
			double totalQty = 0.0d;
			double totalReadyForPickupQty = 0.0d;
			final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
			final OrderModel orderModel = ((OrderProcessModel) businessProcessModel).getOrder();
			orderData = (OshOrderData) getOrderConverter().convert(orderModel);
			final List<Double> readyForPickUpTotalQtyPrice = new ArrayList<Double>();
			final Set<ConsignmentModel> consignments = orderModel.getConsignments();
			for (final ConsignmentModel consignmentModel : consignments)
			{
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
					final long consignmentQty = consignmentEntryModel.getQuantity().longValue();
					totalQty = totalQty + consignmentQty;
					if (consignmentEntryModel.getEntryLevelStatus() != null
							&& (consignmentEntryModel.getEntryLevelStatus().getCode().equals(OshCoreConstants.READY_FOR_PICKUP) || consignmentEntryModel
									.getEntryLevelStatus().getCode().equals(OshCoreConstants.PICKEDUP_AT_STORE)))
					{

						final String productCode = consignmentEntryModel.getOrderEntry().getProduct().getCode();
						final String orderType = consignmentEntryModel.getOrderEntry().getOrderType();
						final long qty = consignmentEntryModel.getOrderEntry().getQuantity().longValue();
						totalReadyForPickupQty = totalReadyForPickupQty + qty;
						double orderEntryPrice = 0.0;
						final Collection<OrderEntryData> Orderentries = orderData.getEntries();
						for (final OrderEntryData orderEntryData : Orderentries)
						{
							double productDiscount = 0.0;
							if (orderEntryData.getProduct().getCode().equals(productCode)
									&& ((OshOrderEntryData) orderEntryData).getOrderType().equals(orderType))
							{
								double totalQtyPrice = 0.0;
								final OshProductData oshproduct = (OshProductData) orderEntryData.getProduct();
								orderEntryData.setQuantity(Long.valueOf(qty));
								orderEntryPrice = orderEntryData.getBasePrice().getValue().doubleValue() * qty;
								totalQtyPrice = ((OshOrderEntryData) orderEntryData).getTotalPrice().getValue().doubleValue();
								readyForPickUpTotalQtyPrice.add(Double.valueOf(totalQtyPrice));
								productDiscount = (((consignmentEntryModel.getOrderEntry().getBasePrice().doubleValue() * consignmentQty) - consignmentEntryModel
										.getOrderEntry().getTotalPrice().doubleValue()) / consignmentQty)
										* qty;
								productDiscounts = productDiscounts + productDiscount;
								orderEntryData.setTotalPrice(priceDataFactory.create("BUY", BigDecimal.valueOf(orderEntryPrice), "USD"));
								orderSubTotal = orderSubTotal + orderEntryPrice;
								totalTax = totalTax + consignmentEntryModel.getOrderEntry().getTaxPerUnit().doubleValue() * qty;
								totalTax = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), totalTax).doubleValue();
								if (consignmentEntryModel.getEntryLevelStatus().getCode().equals(OshCoreConstants.READY_FOR_PICKUP))
								{
									oshproduct.setStatus(OshCoreConstants.READY_FOR_PICKUP);
								}
								else
								{
									oshproduct.setStatus(OshCoreConstants.PICKEDUP_AT_STORE);
								}
							}

						}
					}

				}
			}
			if (orderData.getOrderDiscounts() != null && !readyForPickUpTotalQtyPrice.isEmpty()
					&& orderData.getOrderDiscounts().getValue().doubleValue() != 0.0)
			{
				for (final Double discountPrice : readyForPickUpTotalQtyPrice)
				{
					final double promotionDiscount = (discountPrice.doubleValue() * (orderData.getOrderDiscounts().getValue()
							.doubleValue())) / (orderData.getSubTotal().getValue().doubleValue());
					orderDiscount += promotionDiscount;
				}
				orderDiscount = orderDiscount * (totalReadyForPickupQty / totalQty);
				orderDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), orderDiscount).doubleValue();
				orderData.setOrderDiscounts(priceDataFactory.create("BUY", BigDecimal.valueOf(orderDiscount), "USD"));
			}
			orderData.setProductDiscounts(priceDataFactory.create("BUY", BigDecimal.valueOf(productDiscounts), "USD"));
			orderData.setSubTotal(priceDataFactory.create("BUY", BigDecimal.valueOf(orderSubTotal), "USD"));
			orderData.setTotalTax(priceDataFactory.create("BUY", BigDecimal.valueOf(totalTax), "USD"));
			orderData.setTotalPrice(priceDataFactory.create("BUY",
					BigDecimal.valueOf(totalTax + orderSubTotal - (productDiscounts + orderDiscount)), "USD"));
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

	/**
	 * @return the enumerationService
	 */
	public EnumerationService getEnumerationService()
	{
		return enumerationService;
	}

	/**
	 * @param enumerationService
	 *           the enumerationService to set
	 */
	public void setEnumerationService(final EnumerationService enumerationService)
	{
		this.enumerationService = enumerationService;
	}

}
