/**
 * 
 */
package com.hybris.osh.core.customer.dao.impl;

import static de.hybris.platform.servicelayer.util.ServicesUtil.validateParameterNotNull;

import de.hybris.platform.commerceservices.customer.dao.impl.DefaultCustomerAccountDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.store.BaseStoreModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class DefaultOshCustomerAccountDao extends DefaultCustomerAccountDao
{
	private static final String FIND_ORDERS_BY_CUSTOMER_STORE_QUERY = "SELECT {" + OrderModel.PK + "}, {"
			+ OrderModel.CREATIONTIME + "}, {" + OrderModel.CODE + "} FROM {" + OrderModel._TYPECODE + "} WHERE {" + OrderModel.USER
			+ "} = ?customer AND {" + OrderModel.VERSIONID + "} IS NULL AND {" + OrderModel.STORE + "} = ?store";

	private static final String SORT_ORDERS_BY_STATUS = " ORDER BY {" + OrderModel.STATUS + "},{" + OrderModel.CREATIONTIME
			+ "} DESC, {" + OrderModel.PK + "}";
	private static final String FIND_ORDERS_BY_CUSTOMER_STORE_QUERY_AND_STATUS = FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + " AND {"
			+ OrderModel.STATUS + "} IN (?statusList)";
	private static final String SORT_ORDERS_BY_DATE = " ORDER BY {" + OrderModel.CREATIONTIME + "} DESC, {" + OrderModel.PK + "}";

	private static final String SORT_ORDERS_BY_CODE = " ORDER BY {" + OrderModel.CODE + "},{" + OrderModel.CREATIONTIME
			+ "} DESC, {" + OrderModel.PK + "}";

	@Override
	public SearchPageData<OrderModel> findOrdersByCustomerAndStore(final CustomerModel customerModel, final BaseStoreModel store,
			final OrderStatus[] status, final PageableData pageableData)
	{
		validateParameterNotNull(customerModel, "Customer must not be null");
		validateParameterNotNull(store, "Store must not be null");

		final Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("customer", customerModel);
		queryParams.put("store", store);

		final List<SortQueryData> sortQueries;

		if (status != null && status.length > 0)
		{
			queryParams.put("statusList", Arrays.asList(status));
			sortQueries = Arrays.asList(
					createSortQueryData("byDate", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY_AND_STATUS + SORT_ORDERS_BY_DATE),
					createSortQueryData("byOrderNumber", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY_AND_STATUS + SORT_ORDERS_BY_CODE),
					createSortQueryData("byOrderStatus", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY_AND_STATUS + SORT_ORDERS_BY_STATUS));
		}
		else
		{

			sortQueries = Arrays.asList(createSortQueryData("byDate", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SORT_ORDERS_BY_DATE),
					createSortQueryData("byOrderNumber", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SORT_ORDERS_BY_CODE),
					createSortQueryData("byOrderStatus", FIND_ORDERS_BY_CUSTOMER_STORE_QUERY + SORT_ORDERS_BY_STATUS));

		}

		return getPagedFlexibleSearchService().search(sortQueries, "byDate", queryParams, pageableData);
	}
}
