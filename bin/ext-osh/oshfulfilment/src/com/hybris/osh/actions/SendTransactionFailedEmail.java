package com.hybris.osh.actions;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.task.RetryLaterException;

import java.util.Set;

import org.apache.log4j.Logger;

import com.hybris.osh.events.PaymentFailedCancelEvent;


public class SendTransactionFailedEmail extends AbstractProceduralAction
{


	private EventService eventService;

	@Override
	public void executeAction(BusinessProcessModel process) throws RetryLaterException, Exception
	{
		final OrderProcessModel orderProcessModel = ((OrderProcessModel) process);
		final OrderModel order = ((OrderProcessModel) process).getOrder();
		final Set<ConsignmentModel> consignments = order.getConsignments();
		for (ConsignmentModel consignmentModel : consignments)
		{

			if (consignmentModel.getCode().contains("dropship-"))
			{
				eventService.publishEvent(new PaymentFailedCancelEvent(orderProcessModel, order));
				Logger.getLogger(getClass()).info("Process: " + process.getCode() + " in step " + getClass());
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
	public void setEventService(EventService eventService)
	{
		this.eventService = eventService;
	}

}
