/**
 * 
 */
package com.hybris.osh.core.dao.impl;

import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hybris.osh.core.dao.FindMediaContainerDao;


/**
 * 
 */
public class DefaultFindMediaContainerDao implements FindMediaContainerDao
{

	private FlexibleSearchService flexibleSearchService;

	@Override
	public List<MediaContainerModel> findMediaContainer()
	{
		final Map<String, Object> params = new HashMap<String, Object>();
		final String version = "Staged";
		List<MediaContainerModel> MediaContainer = new ArrayList<MediaContainerModel>();
		final FlexibleSearchQuery query = new FlexibleSearchQuery(
				"select {pk} from {mediaContainer} where {qualifier} LIKE '%B-%' AND {catalogVersion}In({{select {pk} from {CatalogVersion} where {version}=?version}})");
		params.put("version", version);
		query.addQueryParameters(params);
		final SearchResult<MediaContainerModel> result = flexibleSearchService.search(query);
		if (result != null)
		{
			MediaContainer = result.getResult();
		}
		return MediaContainer;
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
