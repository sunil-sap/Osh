/**
 * 
 */
package com.hybris.osh.facades.process.email.context;

import de.hybris.platform.acceleratorservices.model.cms2.pages.EmailPageModel;
import de.hybris.platform.acceleratorservices.process.email.context.AbstractEmailContext;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commercefacades.order.data.OrderData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;

import java.util.Set;

import javax.annotation.Resource;

import com.hybris.osh.facades.order.data.OshOrderData;


public class PaymentFailedEmailContext extends AbstractEmailContext
{

	private EnumerationService enumerationService;
	@Resource
	private Converter<OrderModel, OrderData> orderConverter;

	OshOrderData orderData;

	@Override
	public void init(final BusinessProcessModel businessProcessModel, final EmailPageModel emailPageModel)
	{
		super.init(businessProcessModel, emailPageModel);


		if (businessProcessModel instanceof OrderProcessModel)
		{
			final OrderModel orderModel = ((OrderProcessModel) businessProcessModel).getOrder();
			orderData = (OshOrderData) orderConverter.convert(orderModel);
			final Set<ConsignmentModel> consignments = orderModel.getConsignments();

			for (final ConsignmentModel consignmentModel : consignments)
			{
				if ((consignmentModel.getCode().contains("online-")) || (consignmentModel.getCode().contains("dropship-")))
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

	public OrderData getOrder()
	{
		return orderData;
	}
}
