package com.hybris.osh.core.service;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.List;



public interface ReadyToPickupReminderService
{
	/**
	 * this method will check the consignments for ready for pickup reminder mail
	 * 
	 * @return
	 */
	boolean isReadyToPickupOrders();

	/**
	 * this method will sort out consignments for ready for pickup reminder and will trigger event for ready for pickup
	 * reminder email
	 * 
	 * @param consignments
	 * @return
	 */
	boolean getConsignmentEntryForReminder(final List<ConsignmentModel> consignments);

	/**
	 * this method will generate list for days interval
	 * 
	 * @return intervals list
	 */
	public List<Integer> getReminderIntervals();
}
