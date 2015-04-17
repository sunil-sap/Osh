/**
 * 
 */
package com.hybris.osh.core.service;

import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.List;
import java.util.Set;


public interface DropshipPOFileCreatorService
{
	/**
	 * This method is used to create HDR Record for PO Files
	 * 
	 * @return String
	 */
	public String createPOHDRRecords(final OrderModel order);

	/**
	 * This method is used to create CST Record for PO Files
	 * 
	 * @return String
	 */
	public String createPOCSTBilingRecords(final OrderModel order);

	/**
	 * This method is used to create CST Record for PO Files
	 * 
	 * @return String
	 */
	public String createPOCSTShippingRecords(final OrderModel order);

	/**
	 * This method is used to create HDR Record for PO Files
	 * 
	 * @return String
	 */
	public String createPOSKURecords(final OrderEntryModel orderEntry);

	/**
	 * This method is used to create HDR Record for PO Files
	 * 
	 * @return String
	 */
	public String createPOSHPRecords(final OrderModel order);

	/**
	 * This method is used to create HDR Record for PO Files
	 * 
	 * @return String
	 */
	public String createPOTOTRecords(final OrderModel order);

	/**
	 * This method is used to create HDR Record for PO Files
	 * 
	 * @return String
	 */
	public String createPOTNDRecords(final OrderModel order);

	/**
	 * 
	 * @param order
	 * @return
	 */
	public String createPOSHPTAXRecords(final OrderModel order);

	public List<String> createSKULevelRecords(final ConsignmentModel consignment);

	public List<ConsignmentEntryModel> getEligiblePOConsignment(final Set<ConsignmentModel> consignments);

	/**
	 * 
	 * @param consignment
	 * @return
	 */
	public List<String> createCancelSKULevelRecords(final ConsignmentModel consignment);

}
