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
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.math.BigDecimal;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.facades.order.data.OshOrderData;
import com.hybris.osh.facades.order.data.OshOrderEntryData;
import com.hybris.osh.facades.product.data.OshProductData;


public class OrderConfirmationEmailContext extends AbstractEmailContext
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
		try
		{
			Thread.sleep(30000);
		}
		catch (final InterruptedException e)
		{
			e.printStackTrace();
		}
		if (businessProcessModel instanceof OrderProcessModel)
		{
			final OrderModel orderModel = ((OrderProcessModel) businessProcessModel).getOrder();
			orderData = (OshOrderData) getOrderConverter().convert(orderModel);
			final Format formatter = new SimpleDateFormat("MMMM dd, yyyy");
			final String formattedStartingDate = formatter.format(orderData.getCreated());
			orderData.setCreationTime(formattedStartingDate);
			double orderSubTotal = 0.0;
			final Set<ConsignmentModel> consignments = orderModel.getConsignments();
			for (final ConsignmentModel consignmentModel : consignments)
			{
				if (consignmentModel.getCode().contains("online-") || consignmentModel.getCode().contains("dropship-"))
				{
					orderData.setShowShipping(true);
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

					if (consignmentEntryModel.getEntryLevelStatus() != null)
					{
						final String productCode = consignmentEntryModel.getOrderEntry().getProduct().getCode();
						final String orderType = consignmentEntryModel.getOrderEntry().getOrderType();
						final Collection<OrderEntryData> Orderentries = orderData.getEntries();
						double entryLevelPrice = 0.0;
						for (final OrderEntryData orderEntryData : Orderentries)
						{

							if (orderEntryData.getProduct().getCode().equals(productCode)
									&& ((OshOrderEntryData) orderEntryData).getOrderType().equals(orderType))
							{
								final OshProductData oshproduct = (OshProductData) orderEntryData.getProduct();
								entryLevelPrice = consignmentEntryModel.getOrderEntry().getBasePrice().doubleValue()
										* consignmentEntryModel.getOrderEntry().getQuantity().doubleValue();
								orderSubTotal = orderSubTotal + entryLevelPrice;
								orderEntryData.setTotalPrice(priceDataFactory.create("BUY", BigDecimal.valueOf(entryLevelPrice), "USD"));
								oshproduct.setStatus(OshCoreConstants.READY_FOR_PICKUP);
							}

						}
					}

				}
			}
			orderData.setSubTotal(priceDataFactory.create("BUY", BigDecimal.valueOf(orderSubTotal), "USD"));
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
