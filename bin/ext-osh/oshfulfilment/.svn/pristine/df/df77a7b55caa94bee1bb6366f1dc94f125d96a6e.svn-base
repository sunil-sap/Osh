package com.hybris.osh.actions;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.orderprocessing.model.OrderProcessModel;
import de.hybris.platform.ordersplitting.WarehouseService;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.processengine.action.AbstractSimpleDecisionAction;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.stock.StockService;
import de.hybris.platform.task.RetryLaterException;

import org.apache.log4j.Logger;

import com.hybris.osh.constants.OshfulfilmentConstants;

/** 
 * This class is used to update the stock once the order is placed
 * 
 */
public class UpdateStockAmount extends AbstractSimpleDecisionAction 
{
	protected static final Logger LOG = Logger.getLogger(UpdateStockAmount.class);
	
	private StockService stockService;
	private WarehouseService warehouseService;


	@Override
	public Transition executeAction(final BusinessProcessModel process) throws RetryLaterException, Exception 
	{
		final OrderModel order = ((OrderProcessModel) process).getOrder();
		WarehouseModel warehouseModel;
		StockLevelModel stockLevelModel;
		LOG.info("Updating the stock level");
		
		for (AbstractOrderEntryModel entry : order.getEntries()) 
		{
			final ProductModel productModel = entry.getProduct();
			if (entry.getOrderType().equalsIgnoreCase("online") && !entry.getProduct().getCode().startsWith("8"))
			{
				warehouseModel=warehouseService.getWarehouseForCode(OshfulfilmentConstants.ONLINE);
				stockLevelModel=stockService.getStockLevel(productModel, warehouseModel);
				int availableQty=stockLevelModel.getAvailable();
				int orderQty=entry.getQuantity().intValue();
				stockService.updateActualStockLevel(productModel, warehouseModel, (availableQty-orderQty), order.getCode());
			}
			else if (entry.getOrderType().equalsIgnoreCase("online") && entry.getProduct().getCode().startsWith("8"))
			{
				warehouseModel=warehouseService.getWarehouseForCode(OshfulfilmentConstants.DROPSHIP);
				stockLevelModel=stockService.getStockLevel(productModel, warehouseModel);
				int availableQty=stockLevelModel.getAvailable();
				int orderQty=entry.getQuantity().intValue();
				stockService.updateActualStockLevel(productModel, warehouseModel, (availableQty-orderQty), order.getCode());
			}
			else
			{
				warehouseModel=warehouseService.getWarehouseForCode(entry.getOrderType());
				stockLevelModel=stockService.getStockLevel(productModel, warehouseModel);
				int availableQty=stockLevelModel.getAvailable();
				int orderQty=entry.getQuantity().intValue();
				stockService.updateActualStockLevel(productModel, warehouseModel, (availableQty-orderQty), order.getCode());
			}
			
		}
		
		return Transition.OK;
    }


	public StockService getStockService() {
		return stockService;
	}


	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}


	public WarehouseService getWarehouseService() {
		return warehouseService;
	}


	public void setWarehouseService(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

}
