/**
 * 
 */
package com.hybris.osh.core.flexisearch;

import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.List;


public class OshFlexiSearchUtil
{
	private FlexibleSearchService flexibleSearchService;

	public List<OrderModel> retrieveOrders()
	{

		final SearchResult<OrderModel> orders = flexibleSearchService.search("SELECT {pk} from {order}");

		return orders.getResult();
	}

	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}
}
