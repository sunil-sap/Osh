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


import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;


/**
 * 
 */
public abstract class AbstractConsignmentSimpleDecisionAction extends AbstractSimpleDecisionAction
{

	public abstract Transition executeAction(final ConsignmentProcessModel process);

	@Override
	public Transition executeAction(final BusinessProcessModel process)
	{
		return executeAction((ConsignmentProcessModel) process);
	}

}
