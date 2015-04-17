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
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.facades.order.data.OshOrderData;
import com.hybris.osh.facades.order.data.OshOrderEntryData;
import com.hybris.osh.facades.product.data.OshProductData;


/**
 * Velocity context for a order notification email.
 */
public class OrderNotificationEmailContext extends AbstractEmailContext
{
	private Converter<OrderModel, OrderData> orderConverter;
	private EnumerationService enumerationService;
	private OshOrderData orderData;


	@Override
	public void init(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);
		if (businessProcessModel instanceof OrderProcessModel)
		{
			final OrderModel orderModel = ((OrderProcessModel) businessProcessModel).getOrder();
			orderData = (OshOrderData) getOrderConverter().convert(orderModel);
			final Set<ConsignmentModel> consignments = orderModel.getConsignments();
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
			for (final ConsignmentModel consignmentModel : consignments)
			{

				final Set<ConsignmentEntryModel> consignmentEntry = consignmentModel.getConsignmentEntries();
				for (final ConsignmentEntryModel consignmentEntryModel : consignmentEntry)
				{
					if (consignmentEntryModel.getEntryLevelStatus() != null
							&& consignmentEntryModel.getEntryLevelStatus().getCode().equals(OshCoreConstants.READY_FOR_PICKUP))
					{
						final String productCode = consignmentEntryModel.getOrderEntry().getProduct().getCode();
						final String orderType = consignmentEntryModel.getOrderEntry().getOrderType();
						final Long qty = consignmentEntryModel.getOrderEntry().getQuantity();
						final Collection<OrderEntryData> Orderentries = orderData.getEntries();
						for (final OrderEntryData orderEntryData : Orderentries)
						{

							if (orderEntryData.getProduct().getCode().equals(productCode)
									&& ((OshOrderEntryData) orderEntryData).getOrderType().equals(orderType)
									&& (orderEntryData.getQuantity().equals(qty)))
							{
								final OshProductData oshproduct = (OshProductData) orderEntryData.getProduct();
								oshproduct.setStatus(OshCoreConstants.READY_FOR_PICKUP);
							}
							else
							{
								final OshProductData oshproduct = (OshProductData) orderEntryData.getProduct();
								if (oshproduct.getStatus() != null && !oshproduct.getStatus().equals(OshCoreConstants.READY_FOR_PICKUP))
								{
									oshproduct.setStatus(OshCoreConstants.PENDING);
								}
							}
						}
					}

				}
			}
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
