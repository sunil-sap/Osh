package com.hybris.osh.events;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;


/**
 * Ready For Pickup Status Changed event
 */
public class ReadyToPickUpEvent extends AbstractEvent
{

	private final ConsignmentModel consignment;

	public ReadyToPickUpEvent(final ConsignmentModel consignment)
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
