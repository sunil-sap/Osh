/**
 * 
 */
package com.hybris.osh.core.dao.impl;

import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hybris.osh.core.dao.OshSFTPDao;
import com.hybris.osh.core.model.OshSFTPModel;


/**
 * 
 */
public class DefaultOshSFTPDao implements OshSFTPDao
{

	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<OshSFTPModel> getOshSFTPModel()
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {OshSFTP}");
		query.addQueryParameters(params);
		final SearchResult<OshSFTPModel> result = flexibleSearchService.search(query);
		return result.getResult();
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
