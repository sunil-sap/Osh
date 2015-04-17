package com.hybris.osh.warehouse.impl;


import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.warehouse.Process2WarehouseAdapter;
import de.hybris.platform.warehouse.Warehouse2ProcessAdapter;
import de.hybris.platform.warehouse.WarehouseConsignmentStatus;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.enums.ConsignmentEntryStatus;
import com.hybris.osh.fulfilmentservices.CalculateOrderStatusService;


public class DefaultOshWarehouseService implements Process2WarehouseAdapter
{
	private ModelService modelService;
	private Warehouse2ProcessAdapter warehouse2ProcessAdapter;
	
	@Resource(name = "calculateOrderStatusService")
	CalculateOrderStatusService calculateOrderStatusService;
	
	private static final Logger LOG = Logger.getLogger(DefaultOshWarehouseService.class);

	@Override
	public void prepareConsignment(final ConsignmentModel consignment)
	{
		validateParameterNotNull(consignment, "Consignment model cannot be null");
		/*
		 * commented because it set every consignment entry's shippedQuantity
		 */
		/*
		 * for (final ConsignmentEntryModel consignmentEntries : consignment.getConsignmentEntries()) {
		 * consignmentEntries.setShippedQuantity(consignmentEntries.getQuantity()); }
		 */

		//consignment.setStatus(ConsignmentStatus.PENDING);
		//modelService.save(consignment);


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
		
		validateParameterNotNull(consignment, "Consignment model cannot be null");
		
		final int noOfEntriesInConsignment = consignment.getConsignmentEntries().size();
			if(consignment.getCode().contains("online-")||consignment.getCode().contains("dropship-"))
			{
				int totalEntriesShipped = 0;
				
			for (final ConsignmentEntryModel entry : consignment.getConsignmentEntries())

			{
				if(entry.isTransactionDone())
				{
					entry.setShippedQuantity(entry.getOrderEntry().getQuantity());
					entry.setEntryLevelStatus(ConsignmentEntryStatus.SHIPPED);
					++totalEntriesShipped;
				}
				
				modelService.save(entry);
				modelService.refresh(entry);
			}
			if(totalEntriesShipped>0 && totalEntriesShipped < noOfEntriesInConsignment)
			{
				consignment.setStatus(ConsignmentStatus.PARTIAL_FULFILLED);
				if (LOG.isDebugEnabled())
				{
				LOG.info("Consignment [" + consignment.getCode() + "] PARTIAL_FULFILLED");
				}
			}
			else if(totalEntriesShipped == noOfEntriesInConsignment)
			{
				consignment.setStatus(ConsignmentStatus.SHIPPED);
				if (LOG.isDebugEnabled())
				{
					LOG.info("Consignment [" + consignment.getCode() + "] SHIPPED");
				}
			}
			
			modelService.save(consignment);
			modelService.refresh(consignment);
			
			}
			
			
			else
			{
				int totalEntriesPickPacked= 0 ;
				int totalEntriesReadyForPickUp= 0 ;
				for (final ConsignmentEntryModel entry : consignment.getConsignmentEntries())
				{

					if(entry.isTransactionDone())
					{
					entry.setShippedQuantity(entry.getOrderEntry().getQuantity());
						entry.setEntryLevelStatus(ConsignmentEntryStatus.PICKEDUP_AT_STORE);
						++totalEntriesPickPacked;
					}
					else if(entry.getEntryLevelStatus().getCode().equals(ConsignmentStatus.READY_FOR_PICKUP.getCode()))
					{
						++totalEntriesReadyForPickUp;
					}
					
					modelService.save(entry);
					modelService.refresh(entry);
				}
				if(totalEntriesPickPacked>0 )
				{
					if(totalEntriesPickPacked < noOfEntriesInConsignment)
					{	
					consignment.setStatus(ConsignmentStatus.PARTIAL_FULFILLED);
					if (LOG.isDebugEnabled()) {
						LOG.info("Consignment [" + consignment.getCode()
								+ "] partially Picked");
					}
					}
					else
					{
						consignment.setStatus(ConsignmentStatus.PICKEDUP_AT_STORE);
						if (LOG.isDebugEnabled())
						{
							LOG.info("Consignment [" + consignment.getCode() + "] partially Picked");
						}
					}
					
				}
				
				else if(totalEntriesReadyForPickUp > 0 && totalEntriesPickPacked ==0 )
				{
					if(totalEntriesReadyForPickUp == noOfEntriesInConsignment)
					{
						consignment.setStatus(ConsignmentStatus.READY_FOR_PICKUP);
						if (LOG.isDebugEnabled())
						{
							LOG.info("Consignment [" + consignment.getCode() + "] READY_FOR_PICKUP");
						}
					}
					else{
						consignment.setStatus(ConsignmentStatus.READY_FOR_PICKUP_PARTIAL);
						if (LOG.isDebugEnabled())
						{
							LOG.info("Consignment [" + consignment.getCode() + "] READY_FOR_PICKUP_PARTIAL");
						}
					}
				}
				
				modelService.save(consignment);
				modelService.refresh(consignment);	
				//calculateOrderStatusService.setOrderStatus(consignment);
			}
			calculateOrderStatusService.setOrderStatus(consignment);
			
	}
	
	/*public CalculateOrderStatusService getCalculateOrderStatusService() {
		return calculateOrderStatusService;
	}



	public void setCalculateOrderStatusService(
			CalculateOrderStatusService calculateOrderStatusService) {
		this.calculateOrderStatusService = calculateOrderStatusService;
	}
*/

	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	public void setWarehouse2ProcessAdapter(final Warehouse2ProcessAdapter warehouse2ProcessAdapter)
	{
		this.warehouse2ProcessAdapter = warehouse2ProcessAdapter;
	}


}