/**
 * 
 */
package com.hybris.osh.core.checkout.ordersplitting.strategy.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.ordersplitting.strategy.impl.OrderEntryGroup;
import de.hybris.platform.ordersplitting.strategy.impl.SplitByWarehouse;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.hybris.osh.core.constants.OshCoreConstants;
import com.hybris.osh.core.service.impl.DefaultCartRemovalService;



public class OshSplitByWarehouse extends SplitByWarehouse
{
	@Resource(name = "modelService")
	private ModelService modelService;
	private WarehouseService warehouseService;
	SessionService sessionService;
	protected static final Logger LOG = Logger.getLogger(DefaultCartRemovalService.class);



	@Override
	public WarehouseModel getWarehouse(final OrderEntryGroup orderEntries)
	{
		return chooseBestWarehouse(orderEntries);
	}

	@Override
	public void setWarehouseService(final WarehouseService warehouseService)
	{
		this.warehouseService = warehouseService;
	}


	@Override
	public List<OrderEntryGroup> perform(final List<OrderEntryGroup> orderEntryGroup)
	{
		final List result = new ArrayList();
		for (final Iterator iterator = orderEntryGroup.iterator(); iterator.hasNext();)
		{
			final OrderEntryGroup orderEntry = (OrderEntryGroup) iterator.next();
			final List tmpList = splitForWarehouses(orderEntry);
			OrderEntryGroup tmpOrderEntryGroup;
			for (final Iterator iterator1 = tmpList.iterator(); iterator1.hasNext(); result.add(tmpOrderEntryGroup))
			{
				tmpOrderEntryGroup = (OrderEntryGroup) iterator1.next();
			}

		}

		return result;
	}



	@Override
	public List<OrderEntryGroup> splitForWarehouses(final OrderEntryGroup orderEntryList)
	{
		final List result = new ArrayList();
		final OrderEntryGroup todoEntryList = orderEntryList.getEmpty();
		OrderEntryGroup workingOrderEntryList = sortOrderEntryBeforeWarehouseSplitting(orderEntryList);
		final OrderEntryGroup emptyOrderEntryList = orderEntryList.getEmpty();
		do
		{
			todoEntryList.clear();
			List tmpWarehouseResult = null;
			final OrderEntryGroup tmpOrderEntryResult = orderEntryList.getEmpty();
			for (final Iterator iterator = workingOrderEntryList.iterator(); iterator.hasNext();)
			{
				final AbstractOrderEntryModel orderEntry = (AbstractOrderEntryModel) iterator.next();
				//final List currentPossibleWarehouses = new ArrayList(warehouseService.getWarehousesWithProductsInStock(orderEntry));
				final List currentPossibleWarehouses = new ArrayList();
				if (orderEntry.getOrderType().equalsIgnoreCase(OshCoreConstants.WAREHOUSE)
						&& !orderEntry.getProduct().getCode().startsWith("8"))
				{
					currentPossibleWarehouses.add(warehouseService.getWarehouseForCode(OshCoreConstants.WAREHOUSE));
				}
				else if (orderEntry.getOrderType().equalsIgnoreCase(OshCoreConstants.WAREHOUSE)
						&& orderEntry.getProduct().getCode().startsWith("8"))
				{
					currentPossibleWarehouses.add(warehouseService.getWarehouseForCode(OshCoreConstants.DROPSHIP));
					orderEntry.setOrderType(OshCoreConstants.DROPSHIP);
					modelService.save(orderEntry);
					modelService.refresh(orderEntry);
				}
				else
				{
					currentPossibleWarehouses.add(warehouseService.getWarehouseForCode(orderEntry.getOrderType()));
				}
				if (currentPossibleWarehouses.isEmpty())
				{
					emptyOrderEntryList.add(orderEntry);
				}
				else
				{
					if (tmpWarehouseResult != null)
					{
						currentPossibleWarehouses.retainAll(tmpWarehouseResult);
					}
					if (currentPossibleWarehouses.isEmpty())
					{
						todoEntryList.add(orderEntry);
					}
					else
					{
						tmpWarehouseResult = currentPossibleWarehouses;
						tmpOrderEntryResult.add(orderEntry);
					}
				}
			}

			if (!tmpOrderEntryResult.isEmpty())
			{
				tmpOrderEntryResult.setParameter("WAREHOUSE_LIST", tmpWarehouseResult);
				result.add(tmpOrderEntryResult);
			}
			workingOrderEntryList = todoEntryList.getEmpty();
			workingOrderEntryList.addAll(todoEntryList);
		}
		while (!todoEntryList.isEmpty());
		if (!emptyOrderEntryList.isEmpty())
		{
			result.add(emptyOrderEntryList);
		}
		return result;
	}
}
