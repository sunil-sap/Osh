package com.hybris.osh.core.event;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;

import java.util.HashSet;
import java.util.Set;

import com.hybris.osh.events.ReadyToPickUpEvent;


public class ReadyForPickUpReminderEventLisner extends AbstractEventListener<ReadyToPickUpEvent>
{
	public BusinessProcessService getBusinessProcessService()
	{
		return (BusinessProcessService) Registry.getApplicationContext().getBean("businessProcessService");
	}

	public ModelService getModelServiceViaLookup()
	{
		throw new UnsupportedOperationException(
				"Please define in the spring configuration a <lookup-method> for getModelServiceViaLookup().");
	}

	@Override
	protected void onEvent(final ReadyToPickUpEvent readyToPickUpEvent)
	{
		final OrderProcessModel orderProcessModel = (OrderProcessModel) getBusinessProcessService().createProcess(
				"readyForPickupReminderEmailProcess" + System.currentTimeMillis(), "readyForPickupReminderEmailProcess");
		final OrderModel orderModel = (OrderModel) readyToPickUpEvent.getOrder();
		final Set<ConsignmentModel> consignments = new HashSet<ConsignmentModel>();
		consignments.add(readyToPickUpEvent.getConsignment());
		orderModel.setConsignments(consignments);
		orderProcessModel.setOrder(orderModel);
		getModelServiceViaLookup().save(orderProcessModel);
		getBusinessProcessService().startProcess(orderProcessModel);
	}
}
