package com.hybris.osh.core.dao;

import de.hybris.platform.ordersplitting.model.ConsignmentModel;

import java.util.Date;
import java.util.List;


public interface ReadyToPickupReminderDao
{

	/**
	 * This method will give all the consignment whose
	 * 
	 * @param reminderDate
	 * @return
	 */
	List<ConsignmentModel> findReadyToPickupOrders(Date reminderDate);
}
