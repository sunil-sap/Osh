package com.hybris.osh.core.service;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;

import java.util.Date;
import java.util.List;
import java.util.Map;


public interface PopulateRecordTypesService
{

	/**
	 * This method will prepare the data related to the HDR Record type
	 * 
	 * @param order
	 * @return String to append in the Tlog File
	 */
	public String createHDRRecords(final OrderModel order, final Map<String, Object> retunInfo);

	/**
	 * 
	 * @param order
	 * @return
	 */
	public String createCSTBillingRecords(final OrderModel order);

	/**
	 * 
	 * @param order
	 * @return
	 */
	public String createCSTShippingRecords(final OrderModel order);

	/**
	 * 
	 * @param consignment
	 * @return
	 */
	public List<String> createAllSKURecords(final ConsignmentModel consignment, final List<String> poStatus);

	/**
	 * 
	 * @param orderEntry
	 * @return
	 */

	public String createSKURecords(ConsignmentEntryModel consignmentEntry);

	/**
	 * 
	 * @param order
	 * @return
	 */
	public String createLDSRecords(final OrderModel order, Map<String, Object> retunInfo);

	/**
	 * 
	 * @param order
	 * @return
	 */
	public String createSHPRecords(final OrderModel order);

	/**
	 * 
	 * @param orderEntry
	 * @return
	 */
	public String createTAXRecords(final OrderModel order);

	/**
	 * 
	 * @param orderEntry
	 * @return
	 */
	public String createTNDRecords(final OrderModel order);

	/**
	 * 
	 * @param orders
	 * @return
	 */
	public String createSCRRecords(final List<OrderModel> orders);

	/**
	 * 
	 * @param cart
	 * @return
	 */
	public boolean setCashRegiserNo(final CartModel cart);

	/**
	 * 
	 * @param reurnRequests
	 * @return
	 */
	public Map<String, Object> createEligibleReturnEntries(final List<ReturnRequestModel> reurnRequests);

	/**
	 * 
	 * @param returnEntry
	 * @return
	 */
	public String createReturnSKURecords(final ReturnEntryModel returnEntry);

	/**
	 * 
	 * @param order
	 */
	public void calculateOrderAmount(final OrderModel order);

	/**
	 * 
	 * @param source
	 * @return
	 */
	public double getProductsDiscountsAmount(final AbstractOrderModel source);

	/**
	 * 
	 * @param orderEntry
	 * @return
	 */
	public String createRTNRecord(final AbstractOrderEntryModel orderEntry);

	/**
	 * 
	 * @param order
	 * @return
	 */
	public double getLastTransactionAmount(final OrderModel order, final Date triggerDate);

	/**
	 * @param orderModel
	 * @return
	 */
	public String createHDRRecordForCreditOrder(final OrderModel order);

}
