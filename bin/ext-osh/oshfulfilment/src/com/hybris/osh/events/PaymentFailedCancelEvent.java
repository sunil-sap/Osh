package com.hybris.osh.events;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class PaymentFailedCancelEvent extends AbstractEvent
{
	private final OrderProcessModel process;
	private final OrderModel order;

	/**
	 * @param process
	 */
	public PaymentFailedCancelEvent(final OrderProcessModel process, final OrderModel order)
	{
		this.process = process;
		this.order = order;
	}


	/**
	 * @return
	 */
	public OrderProcessModel getProcess()
	{
		return process;
	}

	/**
	 * @return
	 */
	public OrderModel getOrder()
	{
		return order;
	}



}
