package com.hybris.osh.core.dao.impl;

import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hybris.osh.core.dao.ReadyToPickupReminderDao;
import com.hybris.osh.core.enums.ConsignmentEntryStatus;


public class DefaultReadyToPickupReminderDao implements ReadyToPickupReminderDao
{
	@Resource(name = "flexibleSearchService")
	private FlexibleSearchService flexibleSearchService;

	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;


	@Override
	public List<ConsignmentModel> findReadyToPickupOrders(final Date reminderDate)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		List<ConsignmentModel> orders = new ArrayList<ConsignmentModel>();
		final ConsignmentEntryStatus status = enumerationService.getEnumerationValue(ConsignmentEntryStatus.class,
				"READY_FOR_PICKUP");

		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"Select DISTINCT {consignment} from {ConsignmentEntry} where {entryLevelStatus} = ?status and {readyForPickupTimeStamp} <= ?reminderDate");
		params.put("reminderDate", reminderDate);
		params.put("status", status);
		query.addQueryParameters(params);
		final SearchResult<ConsignmentModel> result = flexibleSearchService.search(query);
		if (result != null)
		{
			orders = result.getResult();
		}
		return orders;
	}

}
