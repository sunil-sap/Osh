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
package de.hybris.platform.warehouse.impl;

import com.hybris.osh.constants.OshfulfilmentConstants;
import com.hybris.osh.enums.WarehouseConsignmentState;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.processengine.BusinessProcessService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehouse.Warehouse2ProcessAdapter;
import de.hybris.platform.warehouse.WarehouseConsignmentStatus;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class DefaultWarehouse2ProcessAdapter implements Warehouse2ProcessAdapter, ApplicationContextAware
{
	private Map<WarehouseConsignmentStatus, WarehouseConsignmentState> statusMap;
	private ModelService modelService;
	private ApplicationContext applicationContext;

	@Override
	public void receiveConsignmentStatus(final ConsignmentModel consignment, final WarehouseConsignmentStatus status)
	{
		for (final ConsignmentProcessModel process : consignment.getConsignmentProcesses())
		{
			final WarehouseConsignmentState state = statusMap.get(status);
			if (state == null)
			{
				throw new NullPointerException("No mapping for WarehouseConsignmentStatus: " + status);//NOPMD
			}
			process.setWarehouseConsignmentState(state);
			modelService.save(process);

			//			PLA-8746 uncomment this and you will see that the state is null on item
			//			final ConsignmentProcess item = ((ConsignmentProcess) modelService.getSource(process));
			//			System.out.println("BBB " + item + " " + item.getWarehouseConsignmentState() + " alive: " + item.isAlive());

			getBusinessProcessService().triggerEvent(process.getCode() + "_" + OshfulfilmentConstants.WAIT_FOR_WAREHOUSE);
		}
	}

	public void setStatusMap(final Map statusMap)
	{
		this.statusMap = statusMap;
	}


	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public BusinessProcessService getBusinessProcessService()
	{
		return applicationContext.getBean(BusinessProcessService.class);
	}

	@Override
	public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException
	{
		this.applicationContext = applicationContext;

	}
}
