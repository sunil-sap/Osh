package com.hybris.osh.events;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class RMAGeneratedEmailEvent extends AbstractEvent
{

	private  ConsignmentModel consignment;
	private  OrderModel orderModel;
	

	public RMAGeneratedEmailEvent(final ConsignmentModel consignment)
	{

		super();
		this.consignment = consignment;
		this.orderModel=(OrderModel) consignment.getOrder();
	}

	public RMAGeneratedEmailEvent(final OrderModel orderModel)
	{

		super();
		this.orderModel = orderModel;
		this.consignment= null;
	}

	public ConsignmentModel getConsignment()
	{
		return consignment;
	}

	public AbstractOrderModel getOrder()
	{
		return orderModel;
	}

}
