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
import de.hybris.platform.servicelayer.model.ModelService;

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
import com.hybris.osh.facades.order.data.OshOrderEntryData;
import com.hybris.osh.facades.product.data.OshProductData;


public class OrderReturnEmailContext extends AbstractEmailContext
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

	@Resource(name = "modelService")
	private ModelService modelService;

	@Override
	public void init(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);
		final String RETURNED_STATUS = "RECEIVED";
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
			double subTotalPrice = 0.0d;
			double productDiscounts = 0.0d;
			double orderDiscount = 0.0d;
			double totalReturnQty = 0.0d;
			double totalQty = 0.0d;
			String rmaNumber = "";
			final List<ReturnEntryModel> returnTotalQtyPrice = new ArrayList<ReturnEntryModel>();
			final LinkedHashMap<String, Long> codeQtyMap = new LinkedHashMap<String, Long>();
			final LinkedHashMap<ReturnEntryModel, Double> returnPriceMap = new LinkedHashMap<ReturnEntryModel, Double>();
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
			final List<OrderEntryData> returnOrderEntryData = new ArrayList<OrderEntryData>();
			for (final ConsignmentModel consignmentModel : consignments)
			{
				final Set<ConsignmentEntryModel> consignmentEntry = consignmentModel.getConsignmentEntries();
				for (final ConsignmentEntryModel consignmentEntryModel : consignmentEntry)
				{
					codeQtyMap.put(consignmentEntryModel.getOrderEntry().getProduct().getCode(), consignmentEntryModel.getQuantity());
					final long consignmentQty = consignmentEntryModel.getQuantity().longValue();
					totalQty = totalQty + consignmentQty;
					if (consignmentEntryModel.getEntryLevelStatus() != null)
					{
						final String productCode = consignmentEntryModel.getOrderEntry().getProduct().getCode();
						final String orderType = consignmentEntryModel.getOrderEntry().getOrderType();
						final List<ReturnRequestModel> returnRequestModels = orderModel.getReturnRequests();
						for (final ReturnRequestModel returnRequestModel : returnRequestModels)
						{

							final List<ReturnEntryModel> returnRequestEntries = returnRequestModel.getReturnEntries();
							for (final ReturnEntryModel returnEntryModel : returnRequestEntries)
							{

								double productDiscount = 0.0;
								final RefundEntryModel refundEntryModel = (RefundEntryModel) returnEntryModel;
								if (refundEntryModel.getStatus().getCode().equals(RETURNED_STATUS) && !refundEntryModel.isRefundConfirm())
								{

									final OrderEntryData orderEntryData = orderEntryConverter.convert(returnEntryModel.getOrderEntry());
									final long expectedQty = returnEntryModel.getExpectedQuantity().longValue();
									if (orderEntryData.getProduct().getCode().equals(productCode)
											&& ((OshOrderEntryData) orderEntryData).getOrderType().equals(orderType) && expectedQty != 0)
									{
										totalReturnQty = totalReturnQty + expectedQty;
										returnPriceMap.put(returnEntryModel,
												Double.valueOf(orderEntryData.getBasePrice().getValue().doubleValue()));
										final double entryLevelPrice = orderEntryData.getBasePrice().getValue().doubleValue() * expectedQty;
										returnTotalQtyPrice.add(returnEntryModel);
										productDiscount = (((consignmentEntryModel.getOrderEntry().getBasePrice().doubleValue() * consignmentQty) - consignmentEntryModel
												.getOrderEntry().getTotalPrice().doubleValue()) / consignmentQty)
												* expectedQty;
										productDiscounts = productDiscounts + productDiscount;
										orderEntryData.setQuantity(Long.valueOf(expectedQty));
										subTotalPrice += entryLevelPrice;
										orderEntryData.setTotalPrice(priceDataFactory.create("BUY", BigDecimal.valueOf(entryLevelPrice),
												"USD"));
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
										oshproduct.setStatus(RETURNED_STATUS);
										oshproduct.setReturnReason(refundEntryModel.getReason().toString());
										oshproduct.setReturnedQuantity(returnEntryModel.getExpectedQuantity());
										refundEntryModel.setRefundConfirm(true);
										modelService.save(refundEntryModel);
										modelService.refresh(refundEntryModel);
										returnOrderEntryData.add(orderEntryData);
									}

								}

							}
						}
					}
				}
				orderData.setEntries(returnOrderEntryData);
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
			orderData.setProductDiscounts(priceDataFactory.create("BUY", BigDecimal.valueOf(productDiscounts), "USD"));
			orderData.setSubTotal(priceDataFactory.create("BUY", BigDecimal.valueOf(subTotalPrice), "USD"));
			double totalDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(),
					(totalTax + subTotalPrice - (productDiscounts + orderDiscount))).doubleValue();
			if (isTaxPromo)
			{
				orderDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(), orderDiscount).doubleValue();
				totalDiscount = Helper.roundCurrencyValue(ctx, ctx.getCurrency(),
						(totalTax + subTotalPrice - (productDiscounts + orderDiscount))).doubleValue();
			}
			orderData.setTotalPrice(priceDataFactory.create("BUY", BigDecimal.valueOf(totalDiscount), "USD"));
			orderData.setTotalTax(priceDataFactory.create("BUY", BigDecimal.valueOf(totalTax), "USD"));
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
