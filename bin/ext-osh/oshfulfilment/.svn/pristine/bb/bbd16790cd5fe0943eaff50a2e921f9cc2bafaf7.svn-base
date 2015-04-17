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


/**
 * The Class alowShipment.
 */
public class AllowShipment extends AbstractConsignmentProceduralAction
{
	private Process2WarehouseAdapter process2WarehouseAdapter;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.hybris.osh.actions.consignmentfulfilment.AbstractConsignmentProceduralAction#executeAction(
	 * de.hybris .processdemo.model.ConsignmentProcessModel)
	 */
	@Override
	public void executeAction(final ConsignmentProcessModel process)
	{
		process2WarehouseAdapter.shipConsignment(process.getConsignment());
	}

	/**
	 * @param process2WarehouseAdapter
	 *           the process2WarehouseAdapter to set
	 */
	public void setProcess2WarehouseAdapter(final Process2WarehouseAdapter process2WarehouseAdapter)
	{
		this.process2WarehouseAdapter = process2WarehouseAdapter;
	}

	/**
	 * @return the process2WarehouseAdapter
	 */
	public Process2WarehouseAdapter getProcess2WarehouseAdapter()
	{
		return process2WarehouseAdapter;
	}

}
