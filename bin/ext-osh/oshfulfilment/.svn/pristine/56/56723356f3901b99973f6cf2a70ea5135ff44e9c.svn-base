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
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.task.RetryLaterException;


/**
 * 
 */
public abstract class AbstractSimpleOrderDecisionAction extends AbstractSimpleDecisionAction
{

	@Override
	public Transition executeAction(final BusinessProcessModel process) throws RetryLaterException, Exception
	{
		// YTODO Auto-generated method stub
		return executeAction((OrderProcessModel) process);
	}

	public abstract Transition executeAction(OrderProcessModel process);

}
