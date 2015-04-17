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

import com.hybris.osh.constants.OshfulfilmentConstants;


import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 *
 */
public class SubprocessEnd extends AbstractConsignmentProceduralAction implements ApplicationContextAware
{

	private ApplicationContext applicationContext;

	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		final OrderModel order = (OrderModel) process.getConsignment().getOrder();
		Logger.getLogger(getClass()).info("Process: " + process.getCode() + " in step " + getClass());

		try
		{
			// simulate different ending times
			Thread.sleep((long) (Math.random() * 2000));
		}
		catch (final InterruptedException e)
		{
			// can't help it
		}
		process.setDone(true);
		save(process);
		Logger.getLogger(getClass()).info("Process: " + process.getCode() + " wrote DONE marker");
		getBusinessProcessService().triggerEvent(
				process.getCode() + "_" + OshfulfilmentConstants.CONSIGNMENT_SUBPROCESS_END_EVENT_NAME);
		Logger.getLogger(getClass()).info(
				"Process: " + process.getCode() + " fired event " + OshfulfilmentConstants.CONSIGNMENT_SUBPROCESS_END_EVENT_NAME);
		order.setTransactionComplete(true);
		modelService.save(order);
		modelService.refresh(order);
	}

	public BusinessProcessService getBusinessProcessService()
	{
		return applicationContext.getBean(BusinessProcessService.class);
	}


	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;
	}

	
}
