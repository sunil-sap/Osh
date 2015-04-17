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

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehouse.Process2WarehouseAdapter;
import de.hybris.platform.warehouse.Warehouse2ProcessAdapter;
import de.hybris.platform.warehouse.WarehouseConsignmentStatus;

import org.apache.log4j.Logger;

import com.hybris.osh.core.enums.ConsignmentEntryStatus;


public class WarehouseMock implements Process2WarehouseAdapter
{
	private ModelService modelService;
	private Warehouse2ProcessAdapter warehouse2ProcessAdapter;

	private static final Logger LOG = Logger.getLogger(WarehouseMock.class);

	@Override
	public void prepareConsignment(final ConsignmentModel consignment)
	{
		for (final ConsignmentEntryModel consignmentEntries : consignment.getConsignmentEntries())
		{
			consignmentEntries.setShippedQuantity(consignmentEntries.getQuantity());
		}
		consignment.setStatus(ConsignmentStatus.READY);
		modelService.save(consignment);


		new WarehouseThread(Registry.getCurrentTenant().getTenantID(), consignment.getPk().getLongValue()).start();

		// Only needed in cause of PLA-8746
		try
		{
			Thread.sleep(3000);
		}
		catch (final InterruptedException e)
		{
			//nothing to do

			//LOG.error("Sleep interrupted", e);
		}
	}

	private class WarehouseThread extends Thread
	{
		private final long consignment;
		private final String tenant;

		public WarehouseThread(final String tenant, final long consignment)
		{
			super();

			this.consignment = consignment;
			this.tenant = tenant;
		}

		@Override
		public void run()
		{
			Registry.setCurrentTenant(Registry.getTenantByID(tenant));
			try
			{
				final ConsignmentModel model = modelService.get(PK.fromLong(consignment));
				warehouse2ProcessAdapter.receiveConsignmentStatus(model, WarehouseConsignmentStatus.COMPLETE);
			}
			finally
			{
				Registry.unsetCurrentTenant();
			}
		}
	}


	@Override
	public void shipConsignment(final ConsignmentModel consignment)
	{
		int noOfEntriesInConsignment = consignment.getConsignmentEntries().size();
			if(consignment.getCode().startsWith("a"))
			{
				int totalEntriesNotShipped = 0;
				
			for (final ConsignmentEntryModel entry : consignment.getConsignmentEntries())

			{
				entry.setShippedQuantity(entry.getOrderEntry().getQuantity());
				if(entry.isTransactionDone())
				{
					entry.setEntryLevelStatus(ConsignmentEntryStatus.SHIPPED);
					
				}
				else
				{
					entry.setEntryLevelStatus(ConsignmentEntryStatus.PENDING);
					++totalEntriesNotShipped;
				}
				modelService.save(entry);
				modelService.refresh(entry);
			}
			if(totalEntriesNotShipped>0 && totalEntriesNotShipped != noOfEntriesInConsignment)
			{
				consignment.setStatus(ConsignmentStatus.PARTIAL_FULFILLED);
				LOG.info("Consignment [" + consignment.getCode() + "] partially shipped");
			}
			else if(totalEntriesNotShipped == 0)
			{
				consignment.setStatus(ConsignmentStatus.PENDING);
				LOG.info("Consignment [" + consignment.getCode() + "] pending");
			}
			else
			{
				consignment.setStatus(ConsignmentStatus.SHIPPED);
				LOG.info("Consignment [" + consignment.getCode() + "] shipped");
			}
			modelService.save(consignment);
			modelService.refresh(consignment);
			}
			
			if(consignment.getCode().startsWith("b"))
			{
				int totalEntriesNotPickPacked= 0 ;
			for (final ConsignmentEntryModel entry : consignment.getConsignmentEntries())
			{
				entry.setShippedQuantity(entry.getOrderEntry().getQuantity());
				if(entry.isTransactionDone())
				{
					entry.setEntryLevelStatus(ConsignmentEntryStatus.COMPLETE);
					
				}
				else
				{
					++totalEntriesNotPickPacked;
				}
				modelService.save(entry);
				modelService.refresh(entry);
			}
			if(totalEntriesNotPickPacked>0 && totalEntriesNotPickPacked != noOfEntriesInConsignment)
			{
				consignment.setStatus(ConsignmentStatus.PARTIAL_FULFILLED);
				LOG.info("Consignment [" + consignment.getCode() + "] partially Picked");
			}
			else if(totalEntriesNotPickPacked == 0)
			{
				consignment.setStatus(ConsignmentStatus.PENDING);
				LOG.info("Consignment [" + consignment.getCode() + "] pending");
			}
			else
			{
				consignment.setStatus(ConsignmentStatus.COMPLETE);
				LOG.info("Consignment [" + consignment.getCode() + "] complete");
			}
			
			modelService.save(consignment);
			modelService.refresh(consignment);
			
			}
	}

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setWarehouse2ProcessAdapter(final Warehouse2ProcessAdapter warehouse2ProcessAdapter)
	{
		this.warehouse2ProcessAdapter = warehouse2ProcessAdapter;
	}


}
