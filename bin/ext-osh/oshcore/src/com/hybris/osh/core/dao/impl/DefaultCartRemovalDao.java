/**
 * 
 */
package com.hybris.osh.core.dao.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.hybris.osh.core.dao.CartRemovalDao;


/**
 * This class will fetch all the cart from the Database
 */
public class DefaultCartRemovalDao implements CartRemovalDao
{

	private FlexibleSearchService flexibleSearchService;
	@SuppressWarnings("unused")
	protected static final Logger LOG = Logger.getLogger(DefaultCartRemovalDao.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.dao.CartRemovalDao#findAllCartModel()
	 */
	@Override
	public List<CartModel> findAbandonedCarts(final Date previousYearDate)
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("previousYearDate", previousYearDate);

		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"Select {pk} from {cart} where {modifiedtime} < ?previousYearDate");
		query.addQueryParameters(params);
		final SearchResult<CartModel> result = flexibleSearchService.search(query);

		if (result.getResult() != null && !result.getResult().isEmpty())
		{
			return result.getResult();
		}
		return Collections.emptyList();

	}

	/**
	 * @return the flexibleSearchService
	 */
	public FlexibleSearchService getFlexibleSearchService()
	{
		return flexibleSearchService;
	}

	/**
	 * @param flexibleSearchService
	 *           the flexibleSearchService to set
	 */
	public void setFlexibleSearchService(final FlexibleSearchService flexibleSearchService)
	{
		this.flexibleSearchService = flexibleSearchService;
	}

}
