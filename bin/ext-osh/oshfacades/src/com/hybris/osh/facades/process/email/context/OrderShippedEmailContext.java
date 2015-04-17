package com.hybris.osh.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.PriceDataFactory;
import de.hybris.platform.commercefacades.user.data.AddressData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.AddressModel;
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
import de.hybris.platform.servicelayer.model.ModelService;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.facades.order.data.OshOrderData;
import com.hybris.osh.facades.order.data.OshOrderEntryData;
import com.hybris.osh.facades.product.data.OshProductData;
import com.hybris.osh.facades.user.data.OshAddressData;


public class OrderShippedEmailContext extends AbstractEmailContext
{

	private Converter<OrderModel, OrderData> orderConverter;
	private EnumerationService enumerationService;
	private OshOrderData orderData;
	private OshAddressData paymentData;
	private Converter<AddressModel, AddressData> addressConverter;
	@Autowired
	PriceDataFactory priceDataFactory;

	@Resource(name = "modelService")
	ModelService modelService;


	@Override
	public void init(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof OrderProcessModel)
		{
			final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
			double orderSubTotal = 0.0d;
			double totalTax = 0.0d;
			double productDiscounts = 0.0d;
			double orderDiscount = 0.0d;
			double totalShippedQty = 0.0d;
			double totalQty = 0.0d;
			boolean isOnlyDropship = true;
			final OrderModel orderModel = ((OrderProcessModel) businessProcessModel).getOrder();
			orderData = (OshOrderData) getOrderConverter().convert(orderModel);
			paymentData = (OshAddressData) getAddressConverter().convert(orderModel.getPaymentAddress());
			final Set<ConsignmentModel> consignments = orderModel.getConsignments();
			final Format formatter = new SimpleDateFormat("MMMM dd, yyyy");
			final String formattedStartingDate = formatter.format(orderData.getCreated());
			orderData.setCreationTime(formattedStartingDate);
			final LinkedHashMap<String, Long> codeQtyMap = new LinkedHashMap<String, Long>();
			final LinkedHashMap<OrderEntryData, Double> shippedPriceMap = new LinkedHashMap<OrderEntryData, Double>();
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
				if (orderData.getDeliveryAddress().getEmail() == null || orderData.getDeliveryAddress().getEmail().isEmpty())
				{
					orderData.getDeliveryAddress().setPhone("");
				}
			}
			if (paymentData.getLine2() == null || paymentData.getLine2().isEmpty())
			{
				paymentData.setLine2("");
			}
			if (paymentData.getPhone() == null || paymentData.getPhone().isEmpty())
			{
				paymentData.setPhone("");
			}
			if (paymentData.getEmail() == null || paymentData.getEmail().isEmpty())
			{
				paymentData.setPhone("");
			}
			for (final ConsignmentModel consignmentModel : consignments)
			{
				if (consignmentModel.getCode().contains("online-"))
				{
					isOnlyDropship = false;
				}
				if (!consignmentModel.isShipedEmailSent() && consignmentModel.getTrackingID() != null
						&& !consignmentModel.getTrackingID().isEmpty() && consignmentModel.isUpdateTrackingID())
				{
					final Set<ConsignmentEntryModel> consignmentEntry = consignmentModel.getConsignmentEntries();
					for (final ConsignmentEntryModel consignmentEntryModel : consignmentEntry)
					{
						codeQtyMap.put(consignmentEntryModel.getOrderEntry().getProduct().getCode(),
								consignmentEntryModel.getQuantity());
						final long consignmentQty = consignmentEntryModel.getQuantity().longValue();
						totalQty = totalQty + consignmentQty;

						if (consignmentEntryModel.getEntryLevelStatus() != null
								&& consignmentEntryModel.getEntryLevelStatus().getCode().equals(OshCoreConstants.SHIPPED)
								|| !consignmentModel.isPaymentCapture())
						{
							final String productCode = consignmentEntryModel.getOrderEntry().getProduct().getCode();
							final String orderType = consignmentEntryModel.getOrderEntry().getOrderType();
							long qty = 0;
							if (consignmentEntryModel.getOrderEntry().getQuantity() != null)
							{
								qty = consignmentEntryModel.getOrderEntry().getQuantity().longValue();
							}
							double orderEntryPrice = 0.0;
							final Collection<OrderEntryData> Orderentries = orderData.getEntries();
							for (final OrderEntryData orderEntryData : Orderentries)
							{
								double productDiscount = 0.0;
								if (orderEntryData.getProduct().getCode().equals(productCode)
										&& ((OshOrderEntryData) orderEntryData).getOrderType().equals(orderType)
										&& (orderEntryData.getQuantity().equals(Long.valueOf(qty))) && qty != 0)
								{
									double totalRemainingTax = 0.0d;
									double totalCancelTax = 0.0d;
									final OshProductData oshproduct = (OshProductData) orderEntryData.getProduct();
									orderEntryData.setQuantity(Long.valueOf(qty));
									totalShippedQty = totalShippedQty + qty;
									shippedPriceMap.put(orderEntryData,
											Double.valueOf(orderEntryData.getTotalPrice().getValue().doubleValue()));
									orderEntryPrice = orderEntryData.getBasePrice().getValue().doubleValue() * qty;
									productDiscount = (((consignmentEntryModel.getOrderEntry().getBasePrice().doubleValue() * consignmentQty) - consignmentEntryModel
											.getOrderEntry().getTotalPrice().doubleValue()) / consignmentQty)
											* qty;
									productDiscounts = productDiscounts + productDiscount;
									orderEntryData
											.setTotalPrice(priceDataFactory.create("BUY", BigDecimal.valueOf(orderEntryPrice), "USD"));
									orderSubTotal = orderSubTotal + orderEntryPrice;
									final double taxPerUnit = consignmentEntryModel.getOrderEntry().getTaxPerUnit().doubleValue();
									long cancelQty = 0L;
									if (consignmentEntryModel.getCancelQty() == null)
									{
										cancelQty = 0L;
										totalCancelTax = taxPerUnit * cancelQty;
									}
									else
									{
										totalCancelTax = taxPerUnit * consignmentEntryModel.getCancelQty().doubleValue();
									}
									totalCancelTax = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), totalCancelTax).doubleValue();
									totalRemainingTax = (consignmentEntryModel.getOrderEntry().getTaxAmount().doubleValue())
											- totalCancelTax;
									totalTax += totalRemainingTax;

									if (consignmentModel.getTrackingID() == null || consignmentModel.getTrackingID().isEmpty())
									{
										oshproduct.setTrackingId("");
									}
									else
									{
										oshproduct.setTrackingId(consignmentModel.getTrackingID());
									}
									oshproduct.setStatus(OshCoreConstants.SHIPPED);
								}

							}
							if (consignmentModel.getCode().contains("online-")
									&& consignmentEntryModel.getEntryLevelStatus().getCode().equals(OshCoreConstants.SHIPPED))
							{
								isOnlyDropship = true;
							}
							consignmentModel.setUpdateTrackingID(false);
							consignmentModel.setShipedEmailSent(true);
							modelService.save(consignmentModel);
							modelService.refresh(consignmentModel);
						}
					}
				}
			}
			if (orderData.getOrderDiscounts() != null && orderData.getOrderDiscounts().getValue().doubleValue() != 0.0)
			{
				for (final Map.Entry entry : shippedPriceMap.entrySet())
				{
					final OrderEntryData orderEntryData = (OrderEntryData) entry.getKey();
					final Long entryQty = codeQtyMap.get(orderEntryData.getProduct().getCode());
					final Double totalQtyPrice = (Double) entry.getValue();
					double promotionDiscount = ((totalQtyPrice.doubleValue() * (orderData.getOrderDiscounts().getValue().doubleValue()))
							/ (orderData.getSubTotal().getValue().doubleValue()) / entryQty.doubleValue());
					promotionDiscount = promotionDiscount * orderEntryData.getQuantity().doubleValue();
					orderDiscount += promotionDiscount;
				}
				if (Double.isNaN(orderDiscount) || Double.isInfinite(orderDiscount))
				{
					orderDiscount = 0.0d;
				}

			}
			orderData.setOrderDiscounts(priceDataFactory.create("BUY", BigDecimal.valueOf(orderDiscount), "USD"));
			orderData.setProductDiscounts(priceDataFactory.create("BUY", BigDecimal.valueOf(productDiscounts), "USD"));
			orderData.setSubTotal(priceDataFactory.create("BUY", BigDecimal.valueOf(orderSubTotal), "USD"));
			orderData.setTotalTax(priceDataFactory.create("BUY", BigDecimal.valueOf(totalTax), "USD"));
			if (isOnlyDropship)
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


	/**
	 * @return the paymentData
	 */
	public OshAddressData getPaymentData()
	{
		return paymentData;
	}

	/**
	 * @param paymentData
	 *           the paymentData to set
	 */
	public void setPaymentData(final OshAddressData paymentData)
	{
		this.paymentData = paymentData;
	}

	protected Converter<AddressModel, AddressData> getAddressConverter()
	{
		return addressConverter;
	}

	@Required
	public void setAddressConverter(final Converter<AddressModel, AddressData> addressConverter)
	{
		this.addressConverter = addressConverter;
	}

}
