package com.hybris.osh.ordercancel.impl.orderstatechangingstrategies;

import de.hybris.platform.ordercancel.OrderStatusChangeStrategy;
import de.hybris.platform.ordercancel.model.OrderCancelRecordEntryModel;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.model.ModelService;

import org.springframework.beans.factory.annotation.Required;

import com.hybris.osh.events.OrderCancelEvent;

public class OshEnterCancellingStrategy
 implements OrderStatusChangeStrategy
{
	private EventService eventService;

	
 public OshEnterCancellingStrategy()
 {
 }

	@Override
	public void changeOrderStatusAfterCancelOperation(OrderCancelRecordEntryModel orderCancelRecordEntry, boolean saveOrderModel)
 {
		/*
		 * orderCancelRecordEntry.getModificationRecord().getOrder().setStatus(OrderStatus.PENDING); for (ConsignmentModel
		 * consign : orderCancelRecordEntry.getModificationRecord().getOrder().getConsignments()) {
		 * consign.setStatus(ConsignmentStatus.PENDING); modelService.save(consign); }
		 */if (saveOrderModel)
			modelService.save(orderCancelRecordEntry.getModificationRecord().getOrder());
		eventService.publishEvent(new OrderCancelEvent(orderCancelRecordEntry.getModificationRecord().getOrder()));

 }

 public ModelService getModelService()
 {
     return modelService;
 }

 public void setModelService(ModelService modelService)
 {
     this.modelService = modelService;
 }

 	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
 private ModelService modelService;
}
