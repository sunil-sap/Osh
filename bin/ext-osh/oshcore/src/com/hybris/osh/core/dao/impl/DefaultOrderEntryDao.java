/**
 * 
 */
package com.hybris.osh.core.dao.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hybris.osh.core.dao.OrderEntryDao;


/**
 * This implementation is used to fetch the orderEntry for a product and its orderType
 */
public class DefaultOrderEntryDao implements OrderEntryDao
{
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.osh.core.dao.OrderEntryDao#getOrderEntries(de.hybris.platform.core.model.order.CartModel,
	 * de.hybris.platform.core.model.product.ProductModel, java.lang.String)
	 */
	@Override
	public List<AbstractOrderEntryModel> getOrderEntries(final CartModel cartModel, final ProductModel productModel,
			final String orderType)
	{
		final Map values = new HashMap();
		values.put("cartModel", cartModel);
		values.put("product", productModel);
		values.put("orderType", orderType);
		final StringBuilder queryString = new StringBuilder();
		queryString.append("SELECT {").append(AbstractOrderEntryModel.PK).append("} FROM {").append("CartEntry")
				.append("} WHERE {").append(AbstractOrderEntryModel.ORDER).append("} = ?cartModel AND {")
				.append(AbstractOrderEntryModel.PRODUCT).append("} = ?product AND {").append(AbstractOrderEntryModel.ORDERTYPE)
				.append("} = ?orderType");
		final FlexibleSearchQuery query = new FlexibleSearchQuery(queryString.toString(), values);
		query.setResultClassList(Collections.singletonList(AbstractOrderEntryModel.class));
		final SearchResult<AbstractOrderEntryModel> result = getFlexibleSearchService().search(query);

		return result.getResult() == null ? Collections.EMPTY_LIST : result.getResult();

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
