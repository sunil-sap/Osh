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
package com.hybris.osh.test.actions.consignmentfulfilment;

import com.hybris.osh.constants.OshfulfilmentConstants;
import com.hybris.osh.test.actions.TestActionTemp;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import org.apache.log4j.Logger;


/**
 *
 */
public abstract class AbstractTestConsActionTemp extends TestActionTemp
{

	@Override
	public String execute(final BusinessProcessModel process) throws Exception //NOPMD
	{
		//getQueueService().actionExecuted(getParentProcess(process), this);
		Logger.getLogger(getClass()).info(getResult());
		return getResult();
	}


	public BusinessProcessModel getParentProcess(final BusinessProcessModel process)
	{
		final String parentCode = (String) getProcessParameterValue(process, OshfulfilmentConstants.PARENT_PROCESS);
		return getBusinessProcessService().getProcess(parentCode);
	}
}
