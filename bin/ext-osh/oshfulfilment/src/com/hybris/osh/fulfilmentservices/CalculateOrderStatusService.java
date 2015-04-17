package com.hybris.osh.fulfilmentservices;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;
/**
 * 
 * Interface for calculating the order status on the basis of consignment status.
 *
 */
public interface CalculateOrderStatusService {
	public void setOrderStatus(final ConsignmentModel consignment);
	
}
