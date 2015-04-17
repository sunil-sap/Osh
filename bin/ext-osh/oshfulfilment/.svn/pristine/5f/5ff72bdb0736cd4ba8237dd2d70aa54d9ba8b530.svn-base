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
package com.hybris.osh.test.actions;


import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;

import org.apache.log4j.Logger;


/**
 *
 */
public class SubprocessesCompleted extends TestActionTemp
{
	@Override
	public String execute(final OrderProcessModel process) throws Exception//NOPMD
	{
		for (final ConsignmentProcessModel subProcess : process.getConsignmentProcesses())
		{
			modelService.refresh(subProcess);
			if (!subProcess.isDone())
			{

				Logger.getLogger(getClass()).info(
						"Process: " + process.getCode() + " found  subprocess " + subProcess.getCode() + " incomplete -> wait again!");
				//getQueueService().actionExecuted(process, this);
				return "NOK";
			}
		}
		//getQueueService().actionExecuted(process, this);
		return "OK";
	}


}
