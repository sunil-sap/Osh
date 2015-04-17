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
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;

import org.apache.log4j.Logger;


/**
 *
 */
public class SubprocessEnd extends AbstractTestConsActionTemp
{
	@Override
	public String execute(final BusinessProcessModel process) throws Exception //NOPMD
	{
		super.execute(process);

		//		getBusinessProcessService().triggerEvent(getParentProcess(process),
		//				OshfulfilmentConstants.CONSIGNMENT_SUBPROCESS_END_EVENT_NAME);

		final ConsignmentProcessModel consProc = (ConsignmentProcessModel) process;
		getBusinessProcessService().triggerEvent(consProc.getParentProcess().getCode() + "_ConsignmentSubprocessEnd");
		Logger.getLogger(getClass()).info(
				"Process: " + process.getCode() + " fire event " + OshfulfilmentConstants.CONSIGNMENT_SUBPROCESS_END_EVENT_NAME);
		((ConsignmentProcessModel) process).setDone(true);
		modelService.save(process);
		return getResult();

	}
}
