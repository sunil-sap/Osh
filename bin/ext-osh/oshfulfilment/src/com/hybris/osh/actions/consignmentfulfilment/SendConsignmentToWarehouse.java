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
import de.hybris.platform.warehouse.Process2WarehouseAdapter;

import org.apache.log4j.Logger;


public class SendConsignmentToWarehouse extends AbstractConsignmentProceduralAction
{

	private Process2WarehouseAdapter process2WarehouseAdapter;

	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		process2WarehouseAdapter.prepareConsignment(process.getConsignment());
		process.setWaitingForConsignment(true);
		Logger.getLogger(getClass()).info("Setting waitForConsignment to true");
		modelService.save(process);
	}

	public void setProcess2WarehouseAdapter(final Process2WarehouseAdapter process2WarehouseAdapter)
	{
		this.process2WarehouseAdapter = process2WarehouseAdapter;
	}

}
