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

import de.hybris.platform.processengine.action.AbstractProceduralAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import org.apache.log4j.Logger;


/**
 * 
 */
public class GetPaymentDetails extends AbstractProceduralAction
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.hybris.platform.processengine.spring.Action#execute(de.hybris.platform.processengine.model.BusinessProcessModel
	 * )
	 */
	@Override
	public void executeAction(final BusinessProcessModel process)
	{
		Logger.getLogger(getClass()).info("Process: " + process.getCode() + " in step " + getClass());
	}

}
