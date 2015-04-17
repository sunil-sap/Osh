package com.hybris.osh.order.cancel.strategy;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordermodify.model.OrderEntryModificationRecordEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;

import java.util.Collection;


public interface CancelOrderStrategy
{
	/**
	 * 
	 * @param orderModifiedEntries
	 * @param order
	 * @return transactionstatus
	 * 
	 *         this method will internally done the calculation of the price after canceling particular order entry and
	 *         also will athorize the remaining amount
	 * @throws Exception
	 */
	public String AutherizeCancelableOrderEntries(Collection<OrderEntryModificationRecordEntryModel> orderModifiedEntries,
			OrderModel order, PaymentTransactionEntryModel paymentTransactionEntry) throws Exception;

	/**
	 * 
	 * @param orderModifiedEntries
	 *           this method will check the status of consignment entry status and according to that it will set the
	 *           consignment status
	 */
	public void setConsignmentStatusForCancelOrder(final Collection<OrderEntryModificationRecordEntryModel> orderModifiedEntries);
	
	/**
	 * 
	 * @param orderModifiedEntries
	 */
	public void setStatusForCancelAndShippedOrder(final Collection<OrderEntryModificationRecordEntryModel> orderModifiedEntries);
	
	/**
	 * @param Set order status in case of partial cancel
	 */
	public boolean setOrderStatusForCancelAndShippedOrder(final OrderModel orderModel);
}
