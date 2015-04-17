package com.hybris.osh.core.dao;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.ordersplitting.model.ConsignmentProcessModel;
import de.hybris.platform.payment.model.PaymentTransactionEntryModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;

import java.util.Date;
import java.util.List;


/**
 * Interface for finding the different types of order
 * 
 */
public interface OshOrderDao
{
	/**
	 * 
	 * finding the orders with the status not equal to complete
	 */
	public List<OrderModel> getIncompleteOrders();

	/**
	 * 
	 * finding the consignments for drop ship
	 */
	public List<ConsignmentModel> getConsignmentModelsForDropship();

	/**
	 * 
	 * finding the orders with the status Pending
	 */
	public List<OrderModel> getPendingOrders();

	/**
	 * 
	 * finding the non drop-ship consignments with status pending
	 */
	public List<ConsignmentModel> getConsignmentModelsOfNonDropshipAndPending();


	/**
	 * 
	 * finding the non drop-ship consignments with status pending
	 */
	public List<ConsignmentModel> getNonDropshipAndPendingConsignmentModelsBetweenAnHour();

	/**
	 * 
	 * finding the orders with the status equal to complete
	 */
	public List<OrderModel> getCompleteOrders();

	/**
	 * 
	 * finding the drop-ship consignments shipped
	 */
	public List<ConsignmentModel> getShippedConsignmentModelsOfDropship();

	/**
	 * Getting the order model based on the order code
	 * 
	 * @param orderCode
	 * @return OrderModel
	 */
	public OrderModel getOrderFromOrderCode(String orderCode);

	/**
	 * 
	 * @param lastHourDate
	 * @return
	 */
	public List<OrderModel> getLastHourOrders(final Date lastHourDate);

	/**
	 * this method is used to count all the orders present in the
	 * 
	 * @return list of all the orders
	 */
	public List<OrderModel> getAllOrders();

	/**
	 * 
	 * @return list of order models which are having pending and canceled entry
	 */
	public List<OrderModel> getPoRelatedOrders(final Date lastDay);

	/**
	 * 
	 * @param lastDay
	 * @return
	 */
	public List<OrderModel> getPoConfirmationOrders(final Date lastDay);

	/**
	 * 
	 * @param code
	 * @return
	 */
	public PaymentTransactionModel getPaymentTransaction(final String code);

	/**
	 * 
	 * @param paymentTransaction
	 * @return
	 */
	public List<PaymentTransactionEntryModel> getCapturedTransactionEntries(final PK paymentTransaction, final Date triggerDate);


	/**
	 * 
	 * @param paymentTransaction
	 * @return
	 */
	public List<PaymentTransactionEntryModel> getReturnedTransactionEntries(final PK paymentTransaction, final Date triggerDate);


	/**
	 * 
	 * @param consignment
	 * @return
	 */
	public List<ConsignmentProcessModel> getProcessForConsignment(final ConsignmentModel consignment);

	/**
	 * @return
	 */
	public List<OrderModel> getCreditOrders();
}
