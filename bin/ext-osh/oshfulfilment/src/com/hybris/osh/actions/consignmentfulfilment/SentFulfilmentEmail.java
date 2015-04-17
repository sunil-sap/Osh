package com.hybris.osh.actions.consignmentfulfilment;

import com.hybris.osh.events.OrderChangedStatusEvent;
import com.hybris.osh.events.ReadyForPickupStatusEvent;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.event.EventService;

public class SentFulfilmentEmail extends AbstractConsignmentProceduralAction {
	private EventService eventService;
	final String CONSIGNMENT_ONLINE=  "online-" ;
	final String CONSIGNMENT_DROPSHIP=  "dropship-" ;
	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		final ConsignmentModel consignmentModel = process.getConsignment();
		if (consignmentModel.getCode().contains(CONSIGNMENT_ONLINE)
				|| process.getConsignment().getCode().contains(CONSIGNMENT_DROPSHIP))
		{
				eventService.publishEvent(new OrderChangedStatusEvent(process.getConsignment()));
		}
		else
		{
			if(consignmentModel.getStatus().equals(ConsignmentStatus.READY_FOR_PICKUP) ||consignmentModel.getStatus().equals(ConsignmentStatus.READY_FOR_PICKUP_PARTIAL) || consignmentModel.getStatus().equals(ConsignmentStatus.PICKEDUP_AT_STORE))
			{
			 eventService.publishEvent(new ReadyForPickupStatusEvent(process.getConsignment()));
			}
		}
	}

	/**
	 * @return the eventService
	 */
	public EventService getEventService()
	{
		return eventService;
	}

	/**
	 * @param eventService
	 *           the eventService to set
	 */
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

}
