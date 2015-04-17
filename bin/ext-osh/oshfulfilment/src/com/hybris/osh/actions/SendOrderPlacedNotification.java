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
package com.hybris.osh.actions;

import de.hybris.platform.core.enums.OrderStatus;
import com.hybris.osh.events.OrderPlacedEvent;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.event.EventService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


public class SendOrderPlacedNotification extends AbstractProceduralAction<OrderProcessModel>
{
	private EventService eventService;

	@Override
	public void executeAction(final OrderProcessModel process)
	{
		eventService.publishEvent(new OrderPlacedEvent(process));
		if (process.getOrder() != null)
		{
			process.getOrder().setStatus(OrderStatus.ORDER_PLACED_NOTIFICATION_SENT);
		}
		modelService.save(process.getOrder());
		Logger.getLogger(getClass()).info("Process: " + process.getCode() + " in step " + getClass());
	}


	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}
}
