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
package com.hybris.osh.actions.consignmentfulfilment;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.servicelayer.event.EventService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

import com.hybris.osh.events.PaymentFailedEvent;




/**
 * 
 */
public class SendPaymentFailedMessage extends AbstractProceduralAction<ConsignmentProcessModel>
{

	private EventService eventService;

	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		ConsignmentModel consignment = process.getConsignment();
		if (consignment.getCode().contains("dropship-"))
		{
			eventService.publishEvent(new PaymentFailedEvent(process.getParentProcess(), (OrderModel) consignment.getOrder()));
		Logger.getLogger(getClass()).info("Process: " + process.getCode() + " in step " + getClass());
		}
	}


	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

}
