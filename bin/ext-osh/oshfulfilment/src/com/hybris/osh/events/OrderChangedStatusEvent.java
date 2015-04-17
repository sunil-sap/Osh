package com.hybris.osh.events;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


public class OrderChangedStatusEvent extends AbstractEvent
{

	private final ConsignmentModel consignment;

	public OrderChangedStatusEvent(final ConsignmentModel consignment)
	{
		super();
		this.consignment = consignment;
	}

	public ConsignmentModel getConsignment()
	{
		return consignment;
	}

	public AbstractOrderModel getOrder()
	{
		return consignment.getOrder();
	}

}
