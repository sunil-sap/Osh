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




public class WaitBeforeTransmission extends AbstractConsignmentSimpleDecisionAction
{

	@Override
	public Transition executeAction(final ConsignmentProcessModel process)
	{
		// If you return NOK this action will be called again.
		// You might want to do this when you want to poll a resource to be ready.
		return Transition.OK;
	}

}
