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
import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;


/**
 * 
 */
public abstract class AbstractProceduralOrderAction extends AbstractProceduralAction
{

	public abstract void executeAction(OrderProcessModel process) throws RetryLaterException, Exception;

	@Override
	public void executeAction(final BusinessProcessModel process) throws RetryLaterException, Exception
	{
		executeAction((OrderProcessModel) process);

	}




}
