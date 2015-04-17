package com.hybris.osh.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.converters.OrderEntryConverter;
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
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.promotions.util.Helper;
import de.hybris.platform.returns.model.RefundEntryModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.osh.core.promotion.model.OshTaxPromotionModel;
import com.hybris.osh.facades.order.data.OshOrderData;
import com.hybris.osh.facades.product.data.OshProductData;


public class OrderRMAMailContext extends AbstractEmailContext
{

	private Converter<OrderModel, OrderData> orderConverter;
	private boolean isTaxPromo = false;
	@Resource
	OrderEntryConverter orderEntryConverter;

	private EnumerationService enumerationService;
	private OshOrderData orderData;
	final String ORDER_STATUS_WAIT = "WAIT";

	@Autowired
	PriceDataFactory priceDataFactory;

	@Override
	public void init(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof OrderProcessModel)
		{
			final SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();

			final OrderModel orderModel = ((OrderProcessModel) businessProcessModel).getOrder();
			final Set<PromotionResultModel> promotions = orderModel.getAllPromotionResults();
			for (final PromotionResultModel promotionResultModel : promotions)
			{
				if (promotionResultModel.getPromotion() instanceof OshTaxPromotionModel)
				{
					isTaxPromo = true;
				}
			}
			orderData = (OshOrderData) getOrderConverter().convert(orderModel);
			final Format formatter = new SimpleDateFormat("MMMM dd, yyyy");
			final String formattedStartingDate = formatter.format(orderData.getCreated());
			orderData.setCreationTime(formattedStartingDate);
			final Set<ConsignmentModel> consignments = orderModel.getConsignments();
			double totalTax = 0.0d;
			double orderSubTotal = 0.0d;
			final LinkedHashMap<String, Long> codeQtyMap = new LinkedHashMap<String, Long>();
			final LinkedHashMap<ReturnEntryModel, Double> returnPriceMap = new LinkedHashMap<ReturnEntryModel, Double>();
			double productDiscounts = 0.0d;
			double orderDiscount = 0.0d;
			double totalReturnQty = 0.0d;
			double totalQty = 0.0d;
			boolean isReturn = true;
			final List<ReturnEntryModel> returnTotalQtyPrice = new ArrayList<ReturnEntryModel>();
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
					codeQtyMap.put(consignmentEntryModel.getOrderEntry().getProduct().getCode(), consignmentEntryModel.getQuantity());
					final long consignmentQty = consignmentEntryModel.getQuantity().longValue();
					totalQty = totalQty + consignmentQty;
				}

				final ReturnRequestModel returnRequestModel = orderModel.getReturnRequests().get(
						orderModel.getReturnRequests().size() - 1);

				if (isReturn)
				{
					String rmaNumber = "";
					isReturn = false;
					final List<OrderEntryData> returnOrderEntryData = new ArrayList<OrderEntryData>();
					final List<ReturnEntryModel> returnRequestEntries = returnRequestModel.getReturnEntries();
					double entryLevelPrice = 0.0;
					for (final ReturnEntryModel returnEntryModel : returnRequestEntries)
					{
						final OrderEntryData orderEntryData = orderEntryConverter.convert(returnEntryModel.getOrderEntry());
						final long expectedQty = returnEntryModel.getExpectedQuantity().longValue();
						totalReturnQty = totalReturnQty + expectedQty;
						returnPriceMap.put(returnEntryModel, Double.valueOf(orderEntryData.getBasePrice().getValue().doubleValue()));
						returnTotalQtyPrice.add(returnEntryModel);
						entryLevelPrice = orderEntryData.getBasePrice().getValue().doubleValue() * expectedQty;
						orderEntryData.setQuantity(Long.valueOf(expectedQty));
						orderSubTotal = orderSubTotal + entryLevelPrice;
						orderEntryData.setTotalPrice(priceDataFactory.create("BUY", BigDecimal.valueOf(entryLevelPrice), "USD"));
						double calculateTax = returnEntryModel.getOrderEntry().getTaxPerUnit().doubleValue() * expectedQty;
						calculateTax = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), calculateTax).doubleValue();
						totalTax = totalTax + calculateTax;
						totalTax = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), totalTax).doubleValue();

						final OshProductData oshproduct = (OshProductData) orderEntryData.getProduct();
						if (rmaNumber.isEmpty())
						{
							rmaNumber = returnRequestModel.getRMA();
							oshproduct.setRmaNumber(returnRequestModel.getRMA());
						}
						else
						{
							oshproduct.setRmaNumber("");
						}

						oshproduct.setStatus(ORDER_STATUS_WAIT);
						final RefundEntryModel reund = (RefundEntryModel) returnEntryModel;
						oshproduct.setReturnReason(reund.getReason().toString());
						returnOrderEntryData.add(orderEntryData);
					}

					orderData.setEntries(returnOrderEntryData);
				}

			}
			if (orderData.getOrderDiscounts() != null && !returnTotalQtyPrice.isEmpty()
					&& orderData.getOrderDiscounts().getValue().doubleValue() != 0.0)
			{
				for (final Map.Entry entry : returnPriceMap.entrySet())
				{
					final ReturnEntryModel returnEntryModel = (ReturnEntryModel) entry.getKey();
					final Long entryQty = codeQtyMap.get(returnEntryModel.getOrderEntry().getProduct().getCode());
					final double totalQtyPrice = returnEntryModel.getOrderEntry().getTotalPrice().doubleValue();
					double promotionDiscount = ((totalQtyPrice * (orderData.getOrderDiscounts().getValue().doubleValue()))
							/ (orderData.getSubTotal().getValue().doubleValue()) / entryQty.doubleValue());
					promotionDiscount = promotionDiscount * returnEntryModel.getExpectedQuantity().doubleValue();
					orderDiscount += promotionDiscount;
				}
				orderData.setOrderDiscounts(priceDataFactory.create("BUY", BigDecimal.valueOf(orderDiscount), "USD"));
			}

			if (orderData.getProductDiscounts() != null && orderData.getProductDiscounts().getValue().doubleValue() != 0.0)
			{
				for (final Map.Entry entry : returnPriceMap.entrySet())
				{
					final ReturnEntryModel returnEntryModel = (ReturnEntryModel) entry.getKey();
					final Long expectedQty = returnEntryModel.getExpectedQuantity();
					final Long entryQty = codeQtyMap.get(returnEntryModel.getOrderEntry().getProduct().getCode());
					final double basePrice = returnEntryModel.getOrderEntry().getBasePrice().doubleValue() * entryQty.doubleValue();
					final double unitDiscountPrice = ((basePrice - returnEntryModel.getOrderEntry().getTotalPrice().doubleValue()) / entryQty
							.doubleValue());
					final double discountPrice = unitDiscountPrice * expectedQty.doubleValue();
					productDiscounts += discountPrice;

				}
				orderData.setProductDiscounts(priceDataFactory.create("BUY", BigDecimal.valueOf(productDiscounts), "USD"));
			}
			orderData.setSubTotal(priceDataFactory.create("BUY", BigDecimal.valueOf(orderSubTotal), "USD"));
			orderData.setTotalTax(priceDataFactory.create("BUY", BigDecimal.valueOf(totalTax), "USD"));
			double totalDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(),
					(totalTax + orderSubTotal - (productDiscounts + orderDiscount))).doubleValue();
			if (isTaxPromo)
			{
				orderDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), orderDiscount).doubleValue();
				totalDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(),
						(totalTax + orderSubTotal - (productDiscounts + orderDiscount))).doubleValue();
			}
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

}
