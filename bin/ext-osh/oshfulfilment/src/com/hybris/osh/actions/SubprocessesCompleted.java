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

import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import org.apache.log4j.Logger;


/**
 *
 */
public class SubprocessesCompleted extends AbstractSimpleDecisionAction
{

	@Override
	public Transition executeAction(final BusinessProcessModel process)
	{
		Logger.getLogger(getClass()).info("Process: " + process.getCode() + " in step " + getClass());

		Logger.getLogger(getClass()).info(
				"Process: " + process.getCode() + " is checking for  "
						+ ((OrderProcessModel) process).getConsignmentProcesses().size() + " subprocess results");

		for (final ConsignmentProcessModel subProcess : ((OrderProcessModel) process).getConsignmentProcesses())
		{
			if (!subProcess.isDone())
			{
				Logger.getLogger(getClass()).info(
						"Process: " + process.getCode() + " found  subprocess " + subProcess.getCode() + " incomplete -> wait again!");
				return Transition.NOK;
			}
			Logger.getLogger(getClass()).info(
					"Process: " + process.getCode() + " found  subprocess " + subProcess.getCode() + " complete ...");
		}
		Logger.getLogger(getClass()).info("Process: " + process.getCode() + " found all subprocesses complete");
		return Transition.OK;

	}


}
