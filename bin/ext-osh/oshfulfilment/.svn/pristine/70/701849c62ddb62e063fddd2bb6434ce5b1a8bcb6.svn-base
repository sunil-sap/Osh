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

import java.util.Set;

import com.hybris.osh.events.OrderChangedStatusEvent;
import com.hybris.osh.events.ReadyForPickupStatusEvent;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.servicelayer.event.EventService;

public class SendDeliveryMessage extends AbstractConsignmentProceduralAction
{
	private EventService eventService;
	final String CONSIGNMENT_ONLINE=  "online-" ;
	final String CONSIGNMENT_DROPSHIP=  "dropship-" ;
	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		/***********Email process is break***** Mail will go when Tracking ID will be update********/
		
		final ConsignmentModel consignmentModel = process.getConsignment();
		final OrderModel orderModel=(OrderModel) consignmentModel.getOrder();
		final Set<ConsignmentModel> consignments = orderModel.getConsignments();
		boolean isMailSent=false;
		if (consignmentModel.getCode().contains(CONSIGNMENT_ONLINE)
				|| process.getConsignment().getCode().contains(CONSIGNMENT_DROPSHIP))
		{
			if(consignmentModel.getStatus().equals(ConsignmentStatus.SHIPPED)&& consignmentModel.getTrackingID()!=null && !consignmentModel.getTrackingID().isEmpty()||consignmentModel.getStatus().equals(ConsignmentStatus.PARTIAL_FULFILLED)&& consignmentModel.getTrackingID()!=null&& !consignmentModel.getTrackingID().isEmpty())
			{
				for(final ConsignmentModel Consignment : consignments){
					if (!Consignment.isShipedEmailSent() && Consignment.getTrackingID() != null
							&& !Consignment.getTrackingID().isEmpty() && Consignment.isUpdateTrackingID()){
						isMailSent=true;
					}
					
				}
				
				if(isMailSent){
					eventService.publishEvent(new OrderChangedStatusEvent(process.getConsignment()));
				}
			}
		}
		else
		{
			if(consignmentModel.getStatus().equals(ConsignmentStatus.READY_FOR_PICKUP)&& consignmentModel.getTrackingID()!=null && !consignmentModel.getTrackingID().isEmpty()||consignmentModel.getStatus().equals(ConsignmentStatus.READY_FOR_PICKUP_PARTIAL)&& consignmentModel.getTrackingID()!=null && !consignmentModel.getTrackingID().isEmpty()|| consignmentModel.getStatus().equals(ConsignmentStatus.PICKEDUP_AT_STORE)&& consignmentModel.getTrackingID()!=null&& !consignmentModel.getTrackingID().isEmpty())
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
